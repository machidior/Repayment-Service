package com.machidior.Repayment_service.service;

import com.machidior.Repayment_service.exceptions.ResourceNotFoundException;
import com.machidior.Repayment_service.model.Reversal;
import com.machidior.Repayment_service.repo.ReversalRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ReversalService {

    private final ReversalRepository repository;

    public Reversal getReversalByRepaymentId(Long repaymentId){
        return repository.findByRepaymentId(repaymentId)
                .orElseThrow(
                        ()->new ResourceNotFoundException("No repayment reversal found with repayment id: " + repaymentId)
                );
    }

    public List<Reversal> getAllReversals(){
        return repository.findAll();
    }

    //ToDo: reverse penalties
    //ToDo: Create Audit record.

}
