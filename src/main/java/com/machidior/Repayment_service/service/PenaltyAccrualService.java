package com.machidior.Repayment_service.service;

import com.machidior.Repayment_service.repo.PenaltyAccrualRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PenaltyAccrualService {

    private final PenaltyAccrualRepository repository;


    //ToDo: DailyPenaltyCalculation
    //ToDo: Increase penaltyAccrued to the installment
    //ToDo: Stop when installment is paid [ installment status = PAID]

}
