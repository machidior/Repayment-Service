package com.machidior.Repayment_service.service;

import com.machidior.Repayment_service.enums.InstallmentPenaltyStatus;
import com.machidior.Repayment_service.enums.InstallmentStatus;
import com.machidior.Repayment_service.enums.LoanProductType;
import com.machidior.Repayment_service.exceptions.ResourceNotFoundException;
import com.machidior.Repayment_service.model.Installment;
import com.machidior.Repayment_service.model.LoanSchedule;
import com.machidior.Repayment_service.repo.LoanScheduleRepository;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.Nullable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class LoanScheduleService {

    private final LoanScheduleRepository repository;


    public LoanSchedule getLoanSchedule(Long id){

        return repository.findById(id)
                .orElseThrow(()->new ResourceNotFoundException("Loan schedule with id " + id + " is not found!"));
    }

    public LoanSchedule getLoanScheduleByLoanId(String loanId){
        return repository.findByLoanId(loanId)
                .orElseThrow(()->new ResourceNotFoundException("Loan schedule with loan id " + loanId + " is not found!"));
    }

    public List<LoanSchedule> getLoanScheduleByProductType(LoanProductType productType){
        return repository.findByProductType(productType);
    }

    public void deleteLoanSchedule(Long id){
        LoanSchedule schedule = repository.findById(id)
                .orElseThrow(()->new ResourceNotFoundException("Loan schedule not found!"));

        repository.deleteById(schedule.getId());
    }

    public List<LoanSchedule> getAllSchedules() {
        return repository.findAll();
    }


    //😜 Just implement this temporary, don't deploy it...
    public LoanSchedule setInstallmentsPenaltyInactive (Long id) {
        LoanSchedule schedule = repository.findById(id)
                .orElseThrow(()->new ResourceNotFoundException("Loan schedule not found!"));

        for (Installment installment: schedule.getInstallments()) {
            installment.setPenaltyStatus(InstallmentPenaltyStatus.INACTIVE);
        }

        return repository.save(schedule);
    }

    public LoanSchedule enablePenalty(Long id) {
        LoanSchedule schedule = repository.findById(id)
                .orElseThrow(()->new ResourceNotFoundException("Loan schedule not found!"));
        schedule.setEnablePenalty(true);
        return repository.save(schedule);
    }
}
