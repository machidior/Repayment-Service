package com.machidior.Repayment_service.kafka;

import com.machidior.Repayment_service.dtos.InstallmentRequest;
import com.machidior.Repayment_service.dtos.LoanDisbursedEventDTO;
import com.machidior.Repayment_service.enums.InstallmentFrequency;
import com.machidior.Repayment_service.enums.LoanScheduleStatus;
import com.machidior.Repayment_service.generator.RepaymentScheduleGenerator;
import com.machidior.Repayment_service.mapper.InstallmentMapper;
import com.machidior.Repayment_service.model.Installment;
import com.machidior.Repayment_service.model.LoanSchedule;
import com.machidior.Repayment_service.repo.LoanScheduleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import tools.jackson.databind.ObjectMapper;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class LoanRepaymentConsumer {

    private final LoanScheduleRepository scheduleRepository;
    private final ObjectMapper objectMapper;
    private final RepaymentScheduleGenerator repaymentScheduleGenerator;
    private final InstallmentMapper installmentMapper;

    @KafkaListener(topics = "loan-disbursed-topic", groupId = "repayment-service")
    public void listenLoanDisbursed(String message) {

            LoanDisbursedEventDTO eventDTO = objectMapper.readValue(message, LoanDisbursedEventDTO.class);

            BigDecimal principal = eventDTO.getPrincipal();
            BigDecimal monthlyRate = eventDTO.getInterestRate().divide(BigDecimal.valueOf(100));
            BigDecimal loanFeeRate = eventDTO.getLoanFeeRate().divide(BigDecimal.valueOf(100));
            int termMonths = eventDTO.getTermMonths();
           InstallmentFrequency frequency = eventDTO.getInstallmentFrequency();

            List<InstallmentRequest> installmentRequests = repaymentScheduleGenerator.generateFlatSchedule(
                    principal,
                    monthlyRate,
                    loanFeeRate,
                    termMonths,
                    eventDTO.getDisbursedOn(),
                    frequency
            );

            LoanSchedule schedule = LoanSchedule.builder()
                    .loanId(eventDTO.getLoanId())
                    .productType(eventDTO.getProductType())
                    .customerId(eventDTO.getCustomerId())
                    .totalPrincipal(principal)
                    .totalInterest(monthlyRate.multiply(BigDecimal.valueOf(termMonths)).multiply(principal))
                    .totalLoanFees(loanFeeRate.multiply(principal).multiply(BigDecimal.valueOf(termMonths)))
                    .paidInstallments(0)
                    .status(LoanScheduleStatus.PENDING)
                    .build();
            List<Installment> installments = installmentRequests.stream()
                            .map(installmentRequest -> installmentMapper.toEntity(installmentRequest,schedule))
                                    .toList();
            schedule.setInstallments(installments);
            schedule.setTotalInstallments(installments.size());

            scheduleRepository.save(schedule);
            System.out.println("Loan schedule created for loan: " + eventDTO.getLoanId());


    }
}
