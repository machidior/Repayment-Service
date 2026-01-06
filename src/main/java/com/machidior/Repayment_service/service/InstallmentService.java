package com.machidior.Repayment_service.service;

import com.machidior.Repayment_service.enums.InstallmentPenaltyStatus;
import com.machidior.Repayment_service.enums.InstallmentStatus;
import com.machidior.Repayment_service.exceptions.ResourceNotFoundException;
import com.machidior.Repayment_service.grpc.PenaltyPolicy;
import com.machidior.Repayment_service.model.Installment;
import com.machidior.Repayment_service.model.OverpaymentWallet;
import com.machidior.Repayment_service.repo.InstallmentRepository;
import com.machidior.Repayment_service.repo.OverpaymentWalletRepository;
import com.machidior.grpc.loanconfig.LoanProductType;
import com.machidior.grpc.loanconfig.PenaltyPolicyResponse;
import jakarta.annotation.PostConstruct;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
@RequiredArgsConstructor
public class InstallmentService {

    private final InstallmentRepository installmentRepository;
    private final OverpaymentWalletRepository overpaymentWalletRepository;

    private final PenaltyPolicy penaltyPolicy;

    @Scheduled(cron = "0 0 0 * * ?")
    public void updateDueInstallment(){
        List<Installment> dueInstallments = installmentRepository.findByStatusAndDueDate(
                InstallmentStatus.PENDING,
                LocalDate.now()
        );


        for (Installment installment: dueInstallments){

            installment.getStatus().validateTransition(InstallmentStatus.DUE);
            OverpaymentWallet wallet = overpaymentWalletRepository.findByCustomerIdAndLoanId(
                    installment.getSchedule().getCustomerId(),
                    installment.getSchedule().getLoanId()
            )
                    .orElseThrow(
                            ()->new ResourceNotFoundException("This loan have no overpayment wallet!")
                    );

            if (wallet != null && wallet.getBalance().compareTo(BigDecimal.ZERO) > 0) {

                BigDecimal remaining = wallet.getBalance();

                BigDecimal penaltyApplied = BigDecimal.ZERO;
                BigDecimal feeApplied = BigDecimal.ZERO;
                BigDecimal interestApplied = BigDecimal.ZERO;
                BigDecimal principalApplied = BigDecimal.ZERO;

                penaltyApplied = allocate(remaining, installment.getPenaltyAccrued());
                remaining = remaining.subtract(penaltyApplied);

                feeApplied = allocate(remaining, installment.getInterestDue());
                remaining = remaining.subtract(feeApplied);

                interestApplied = allocate(remaining, installment.getLoanFeeDue());
                remaining = remaining.subtract(interestApplied);

                principalApplied = allocate(remaining, installment.getPrincipalPaid());
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
            }


            installment.setStatus(InstallmentStatus.DUE);
        }

        installmentRepository.saveAll(dueInstallments);
    }

    @Scheduled(cron = "0 5 0 * * ?")
    @Transactional
    public void markOverdueInstallments() {

        LocalDate today = LocalDate.now();

        List<Installment> overdueInstallments = installmentRepository.findOverdueInstallments(today);

        for(Installment installment: overdueInstallments) {

            if (installment.getStatus() == InstallmentStatus.PENDING) {
                installment.setPrincipalPaid(BigDecimal.ZERO);
                installment.setInterestPaid(BigDecimal.ZERO);
                installment.setLoanFeePaid(BigDecimal.ZERO);
                installment.setTotalPaid(BigDecimal.ZERO);
                installment.setPenaltyAccrued(BigDecimal.ZERO);
                installment.setPenaltyPaid(BigDecimal.ZERO);
            }
            installment.setStatus(InstallmentStatus.OVERDUE);
        }

        installmentRepository.saveAll(overdueInstallments);
    }

    @Transactional
    public void activateInstallmentPenaltyStatusAutomatically() {

        List<Installment> installments = installmentRepository.findByStatusAndPenaltyStatus(InstallmentStatus.OVERDUE, InstallmentPenaltyStatus.INACTIVE);

        LocalDate today = LocalDate.now();

        for (Installment installment: installments) {
            if (installment.getSchedule().getEnablePenalty()) {

                long daysOverdue = ChronoUnit.DAYS.between(installment.getDueDate(), today);
                PenaltyPolicyResponse penaltyPolicyResponse = penaltyPolicy.getPenaltyPolicy(LoanProductType
                        .valueOf(installment.getSchedule().getProductType().toString()));

                BigDecimal penaltyAmount = installment.getSchedule().getTotalPrincipal()
                        .multiply(BigDecimal.valueOf(penaltyPolicyResponse.getLatePenaltyRate()).divide(BigDecimal.valueOf(100)));

                if (daysOverdue > penaltyPolicyResponse.getGracePeriodDays()) {
                    installment.setPenaltyStatus(InstallmentPenaltyStatus.ACTIVE);
                    //for null values
                    if (installment.getPenaltyAccrued() == null) {
                        installment.setPenaltyAccrued(BigDecimal.ZERO);
                    }
                    installment.setPenaltyAccrued(installment.getPenaltyAccrued().add(penaltyAmount));
                    installment.setPenaltyPaid(BigDecimal.ZERO);
                }

            }

        }
        installmentRepository.saveAll(installments);
    }


    @Transactional
    public void OverdueInstallmentsPenaltyIncrement() {

        List<Installment> installments = installmentRepository.findByPenaltyStatus(InstallmentPenaltyStatus.ACTIVE);

        for (Installment installment: installments) {
            BigDecimal penaltyRate = penaltyPolicy.getPenaltyRate(LoanProductType.valueOf(installment.getSchedule().getProductType().toString()));
            BigDecimal penaltyAmount = installment.getSchedule().getTotalPrincipal().multiply(penaltyRate.divide(BigDecimal.valueOf(100)));

            installment.setPenaltyAccrued(installment.getPenaltyAccrued().add(penaltyAmount));
        }

        installmentRepository.saveAll(installments);
    }

//    ToDo: mark defaulted installments ( @Scheduled(cron = "0 10 0 * * ?") )

    private BigDecimal allocate(BigDecimal remaining, BigDecimal required) {
        return remaining.min(required);
    }


    @PostConstruct
    public void runOnStartup(){
        updateDueInstallment();
        markOverdueInstallments();
    }

}

//ToDo: Interest Recalculation.
// - Interest on overdue principal
// - Flat or reducing balance models.

//ToDo: Write-Off / Settlement
// - Close loan
// - Mark installments as WRITTEN_OFF
// - Record loss