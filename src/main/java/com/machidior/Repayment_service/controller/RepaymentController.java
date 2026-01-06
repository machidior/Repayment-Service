package com.machidior.Repayment_service.controller;

import com.google.protobuf.Descriptors;
import com.machidior.Repayment_service.dtos.PenaltyPolicy;
import com.machidior.Repayment_service.dtos.RepaymentRequest;
import com.machidior.Repayment_service.dtos.ReversalRequest;
import com.machidior.Repayment_service.model.Repayment;
import com.machidior.Repayment_service.service.RepaymentService;
import com.machidior.grpc.loanconfig.LoanProductType;
import com.machidior.grpc.loanconfig.PenaltyPolicyResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/repayments")
@RequiredArgsConstructor
public class RepaymentController {

    private final RepaymentService repaymentService;

    @PostMapping("/apply/{installmentId}")
    public ResponseEntity<?> applyRepayment(@PathVariable Long installmentId, @RequestBody RepaymentRequest request){
        repaymentService.applyRepayment(installmentId,request);
        return ResponseEntity.ok().body("Repayment applied successfully!");
    }

    @GetMapping("/all")
    public ResponseEntity<List<Repayment>> getAllRepayments(){
        return ResponseEntity.ok(repaymentService.getAllRepayments());
    }

    @PostMapping("/reverse/{repaymentId}")
    public ResponseEntity<String> reverseRepayment(@PathVariable Long repaymentId, @RequestBody ReversalRequest request){
        repaymentService.reverseRepayment(repaymentId, request);
        return ResponseEntity.ok().body("Repayment reversed successfully!");
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteRepayment(@PathVariable Long id){
        repaymentService.deleteRepayment(id);
        return ResponseEntity.ok().body("Repayment deleted successfully!");
    }


}


