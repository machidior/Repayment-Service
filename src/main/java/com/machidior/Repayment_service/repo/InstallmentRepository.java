package com.machidior.Repayment_service.repo;

import com.machidior.Repayment_service.enums.InstallmentStatus;
import com.machidior.Repayment_service.model.Installment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface InstallmentRepository extends JpaRepository<Installment,Long> {
    List<Installment> findByStatusAndDueDate(InstallmentStatus status, LocalDate dueDate);
    List<Installment> findByStatus(InstallmentStatus status);
    List<Installment> findByScheduleIdAndStatusInOrderByDueDateAsc(
            Long scheduleId,
            List<InstallmentStatus> statuses
    );

    List<Installment> findByStatusAndDueDateBefore(InstallmentStatus installmentStatus, LocalDate date);
}
