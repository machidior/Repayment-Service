package com.machidior.Repayment_service.service;

import com.machidior.Repayment_service.enums.InstallmentStatus;
import com.machidior.Repayment_service.model.Installment;
import com.machidior.Repayment_service.repo.InstallmentRepository;
import jakarta.annotation.PostConstruct;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class InstallmentService {

    private final InstallmentRepository installmentRepository;

    @Scheduled(cron = "0 0 0 * * ?")
    public void updateDueInstallment(){
        List<Installment> dueInstallments = installmentRepository.findByStatusAndDueDate(
                InstallmentStatus.PENDING,
                LocalDate.now()
        );

        for (Installment installment: dueInstallments){

            installment.setPrincipalPaid(BigDecimal.ZERO);
            installment.setInterestPaid(BigDecimal.ZERO);
            installment.setLoanFeePaid(BigDecimal.ZERO);
            installment.setTotalPaid(BigDecimal.ZERO);
            installment.setPenaltyAccrued(BigDecimal.ZERO);
            installment.setPenaltyPaid(BigDecimal.ZERO);
            installment.setStatus(InstallmentStatus.DUE);
        }

        installmentRepository.saveAll(dueInstallments);
    }

    @Scheduled(cron = "0 5 0 * * ?")
    @Transactional
    public void markOverdueInstallments() {

        List<Installment> dueInstallments =
                installmentRepository.findByStatusAndDueDateBefore(
                        InstallmentStatus.DUE,
                        LocalDate.now()
                );

        for (Installment installment : dueInstallments) {

            if (installment.getTotalPaid()
                    .compareTo(installment.getTotalDue()) < 0) {

                installment.setStatus(InstallmentStatus.OVERDUE);
            }
        }

        installmentRepository.saveAll(dueInstallments);
    }

//    @Scheduled(cron = "0 10 0 * * ?")
//    @Transactional
//    public void markDefaultedInstallments() {
//
//        List<Installment> overdueInstallments =
//                installmentRepository.findByStatus(InstallmentStatus.OVERDUE);
//
//        for (Installment installment : overdueInstallments) {
//
//            if (defaultPolicy.isDefaulted(installment)) {
//                installment.setStatus(InstallmentStatus.DEFAULTED);
//            }
//        }
//
//        installmentRepository.saveAll(overdueInstallments);
//    }



    @PostConstruct
    public void runOnStartup(){
        updateDueInstallment();
        markOverdueInstallments();
    }
}
