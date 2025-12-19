package com.machidior.Repayment_service.service;

import com.machidior.Repayment_service.dtos.RepaymentRequest;
import com.machidior.Repayment_service.enums.InstallmentStatus;
import com.machidior.Repayment_service.enums.RepaymentStatus;
import com.machidior.Repayment_service.exceptions.ResourceNotFoundException;
import com.machidior.Repayment_service.kafka.RepaymentAppliedEvent;
import com.machidior.Repayment_service.mapper.RepaymentMapper;
import com.machidior.Repayment_service.model.Installment;
import com.machidior.Repayment_service.model.LoanSchedule;
import com.machidior.Repayment_service.model.Repayment;
import com.machidior.Repayment_service.model.RepaymentApplication;
import com.machidior.Repayment_service.repo.InstallmentRepository;
import com.machidior.Repayment_service.repo.LoanScheduleRepository;
import com.machidior.Repayment_service.repo.RepaymentRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.function.Consumer;

@Service
@RequiredArgsConstructor
public class RepaymentService {

    private final RepaymentRepository repository;
    private final InstallmentRepository installmentRepository;
    private final LoanScheduleRepository loanScheduleRepository;
    private final RepaymentMapper repaymentMapper;
    private final OverpaymentWalletService overpaymentWalletService;
    private final ApplicationEventPublisher eventPublisher;

    @Transactional
    public void applyRepayment(Long installmentId, RepaymentRequest repaymentRequest) {

        Installment installment = installmentRepository.findById(installmentId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Installment with id "+ installmentId +" is not found"));

        BigDecimal amountPaid = repaymentRequest.getAmountPaid();

        if (amountPaid.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Payment amount must be greater than zero");
        }

        BigDecimal penaltyOutstanding =
                installment.getPenaltyAccrued().subtract(installment.getPenaltyPaid());

        BigDecimal feeOutstanding =
                installment.getLoanFeeDue().subtract(installment.getLoanFeePaid());

        BigDecimal interestOutstanding =
                installment.getInterestDue().subtract(installment.getInterestPaid());

        BigDecimal principalOutstanding =
                installment.getPrincipalDue().subtract(installment.getPrincipalPaid());

        BigDecimal remaining = amountPaid;

        BigDecimal penaltyApplied = BigDecimal.ZERO;
        BigDecimal feeApplied = BigDecimal.ZERO;
        BigDecimal interestApplied = BigDecimal.ZERO;
        BigDecimal principalApplied = BigDecimal.ZERO;

        penaltyApplied = allocate(remaining, penaltyOutstanding);
        remaining = remaining.subtract(penaltyApplied);

        feeApplied = allocate(remaining, feeOutstanding);
        remaining = remaining.subtract(feeApplied);

        interestApplied = allocate(remaining, interestOutstanding);
        remaining = remaining.subtract(interestApplied);

        principalApplied = allocate(remaining, principalOutstanding);
        remaining = remaining.subtract(principalApplied);

        installment.setPenaltyPaid(installment.getPenaltyPaid().add(penaltyApplied));
        installment.setLoanFeePaid(installment.getLoanFeePaid().add(feeApplied));
        installment.setInterestPaid(installment.getInterestPaid().add(interestApplied));
        installment.setPrincipalPaid(installment.getPrincipalPaid().add(principalApplied));

        BigDecimal totalApplied = penaltyApplied
                .add(feeApplied)
                .add(interestApplied)
                .add(principalApplied);

        installment.setTotalPaid(installment.getTotalPaid().add(totalApplied));

        if (installment.getTotalPaid().compareTo(installment.getTotalDue()) >= 0) {
            installment.setStatus(InstallmentStatus.PAID);
        } else {
            installment.setStatus(InstallmentStatus.PARTIAL);
        }

        installmentRepository.save(installment);

        BigDecimal excessAmount = remaining.max(BigDecimal.ZERO);

        if (excessAmount.compareTo(BigDecimal.ZERO) > 0) {
            overpaymentWalletService.credit(
                    installment.getSchedule().getLoanId(),
                    installment.getSchedule().getCustomerId(),
                    excessAmount
            );
        }

        Repayment repayment = repaymentMapper.toEntity(repaymentRequest);
        repayment.setLoanId(installment.getSchedule().getLoanId());
        repayment.setCustomerId(installment.getSchedule().getCustomerId());
        repayment.setInstallmentId(installment.getId());
        repayment.setStatus(RepaymentStatus.APPLIED);

        RepaymentApplication application = RepaymentApplication.builder()
                .penaltyApplied(penaltyApplied)
                .loanFeeApplied(feeApplied)
                .interestApplied(interestApplied)
                .principalApplied(principalApplied)
                .totalApplied(totalApplied)
                .excessAmount(excessAmount)
                .build();

        repayment.setRepaymentApplication(application);
        Repayment savedRepayment = repository.save(repayment);

        eventPublisher.publishEvent(
                new RepaymentAppliedEvent(
                        savedRepayment.getId(),
                        savedRepayment.getLoanId(),
                        savedRepayment.getInstallmentId(),
                        savedRepayment.getCustomerId(),
                        totalApplied,
                        excessAmount,
                        LocalDateTime.now()
                )
        );

    }


    private BigDecimal allocate(BigDecimal remaining, BigDecimal required) {
        return remaining.min(required);
    }

    private BigDecimal min(BigDecimal a, BigDecimal b){
        return a.compareTo(b)<0?a:b;
    }
    private BigDecimal maxZero(BigDecimal value){
        return value.compareTo(BigDecimal.ZERO)<0?BigDecimal.ZERO:value;
    }

    @Transactional
    public void autoApplyOverpayment(
            Long scheduleId,
            BigDecimal excessAmount,
            String customerId) {

        if (excessAmount.compareTo(BigDecimal.ZERO) <= 0) return;

        LoanSchedule schedule = loanScheduleRepository.findById(scheduleId)
                .orElseThrow(()->new ResourceNotFoundException("Schedule not found"));

        List<Installment> nextInstallments =
                installmentRepository
                        .findByScheduleIdAndStatusInOrderByDueDateAsc(
                                scheduleId,
                                List.of(InstallmentStatus.PENDING, InstallmentStatus.PARTIAL)
                        );

        BigDecimal remaining = excessAmount;

        for (Installment installment : nextInstallments) {
            if (remaining.compareTo(BigDecimal.ZERO) <= 0) break;

            remaining = applyRepaymentInternally(installment, remaining);
        }

        if (remaining.compareTo(BigDecimal.ZERO) > 0) {
            overpaymentWalletService.credit(schedule.getLoanId(), customerId, remaining);
        }
    }

    private BigDecimal applyRepaymentInternally(
            Installment installment,
            BigDecimal amount) {

        BigDecimal remaining = amount;

        remaining = allocateAndUpdate(remaining,
                installment.getPenaltyAccrued().subtract(installment.getPenaltyPaid()),
                installment::setPenaltyPaid);

        remaining = allocateAndUpdate(remaining,
                installment.getLoanFeeDue().subtract(installment.getLoanFeePaid()),
                installment::setLoanFeePaid);

        remaining = allocateAndUpdate(remaining,
                installment.getInterestDue().subtract(installment.getInterestPaid()),
                installment::setInterestPaid);

        remaining = allocateAndUpdate(remaining,
                installment.getPrincipalDue().subtract(installment.getPrincipalPaid()),
                installment::setPrincipalPaid);

        BigDecimal applied = amount.subtract(remaining);
        installment.setTotalPaid(installment.getTotalPaid().add(applied));

        if (installment.getTotalPaid().compareTo(installment.getTotalDue()) >= 0) {
            installment.setStatus(InstallmentStatus.PAID);
        } else {
            installment.setStatus(InstallmentStatus.PARTIAL);
        }

        installmentRepository.save(installment);
        return remaining;
    }

    private BigDecimal allocateAndUpdate(
            BigDecimal remaining,
            BigDecimal required,
            Consumer<BigDecimal> updater
    ) {
        if (remaining.compareTo(BigDecimal.ZERO) <= 0 || required.compareTo(BigDecimal.ZERO) <= 0) {
            return remaining;
        }

        BigDecimal applied = remaining.min(required);

        updater.accept(applied);

        return remaining.subtract(applied);
    }

    @Transactional
    public void reverseRepayment(Long repaymentId) {
        Repayment repayment = repository.findById(repaymentId)
                .orElseThrow(() -> new ResourceNotFoundException("Repayment not found with id: " + repaymentId));

        Installment installment = installmentRepository.findById(repayment.getInstallmentId())
                .orElseThrow(() -> new ResourceNotFoundException("Installment not found"));

        BigDecimal penaltyReversed = repayment.getRepaymentApplication().getPenaltyApplied();
        BigDecimal feeReversed = repayment.getRepaymentApplication().getLoanFeeApplied();
        BigDecimal interestReversed = repayment.getRepaymentApplication().getInterestApplied();
        BigDecimal principalReversed = repayment.getRepaymentApplication().getPrincipalApplied();

        installment.setPenaltyPaid(installment.getPenaltyPaid().subtract(penaltyReversed));
        installment.setLoanFeePaid(installment.getLoanFeePaid().subtract(feeReversed));
        installment.setInterestPaid(installment.getInterestPaid().subtract(interestReversed));
        installment.setPrincipalPaid(installment.getPrincipalPaid().subtract(principalReversed));
        installment.setTotalPaid(installment.getTotalPaid().subtract(penaltyReversed.add(feeReversed).add(interestReversed).add(principalReversed)));

        if (installment.getTotalPaid().compareTo(installment.getTotalDue()) < 0) {
            installment.setStatus(InstallmentStatus.PARTIAL);
        } else {
            installment.setStatus(InstallmentStatus.PENDING);
        }

        installmentRepository.save(installment);

        repayment.setStatus(RepaymentStatus.REVERSED);
        repository.save(repayment);
    }



    public List<Repayment> getAllRepayments(){
        return repository.findAll();
    }

    public void deleteRepayment(Long id){
        Repayment repayment = repository.findById(id)
                .orElseThrow(()->new ResourceNotFoundException("Repayment not found!"));
        repository.deleteById(repayment.getId());
    }
}
