package com.machidior.Repayment_service.service;

import com.machidior.Repayment_service.enums.LoanProductType;
import com.machidior.Repayment_service.exceptions.ResourceNotFoundException;
import com.machidior.Repayment_service.model.LoanSchedule;
import com.machidior.Repayment_service.repo.LoanScheduleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class LoanScheduleService {

    private final LoanScheduleRepository repository;

    public LoanSchedule createKuzaLoanSchedule(){



        return new LoanSchedule();

    }

    public LoanSchedule createBusinessLoanSchedule(){

        return new LoanSchedule();
    }

    public LoanSchedule createSalaryLoanSchedule(){

        return new LoanSchedule();
    }

    public LoanSchedule createStaffLoanSchedule(){

        return new LoanSchedule();
    }

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
}
