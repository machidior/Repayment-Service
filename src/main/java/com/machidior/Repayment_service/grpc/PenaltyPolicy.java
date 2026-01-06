package com.machidior.Repayment_service.grpc;

import com.machidior.grpc.loanconfig.LoanPenaltyServiceGrpc;
import com.machidior.grpc.loanconfig.LoanProductType;
import com.machidior.grpc.loanconfig.PenaltyPolicyRequest;
import com.machidior.grpc.loanconfig.PenaltyPolicyResponse;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class PenaltyPolicy {

    @GrpcClient("loan-config-service")
    private LoanPenaltyServiceGrpc.LoanPenaltyServiceBlockingStub penaltyStub;

    public PenaltyPolicyResponse getPenaltyPolicy(LoanProductType productType) {

        return penaltyStub.getPenaltyPolicy(
                PenaltyPolicyRequest.newBuilder().setProductType(productType).build()
        );
    }

    public BigDecimal getPenaltyRate(LoanProductType productType) {

        PenaltyPolicyResponse policyResponse = penaltyStub.getPenaltyPolicy(
                PenaltyPolicyRequest.newBuilder().setProductType(productType).build());

        return BigDecimal.valueOf(policyResponse.getLatePenaltyRate());

    }

    public Integer getGracePeriodDays(LoanProductType productType) {

        PenaltyPolicyResponse policyResponse = penaltyStub.getPenaltyPolicy(
                PenaltyPolicyRequest.newBuilder().setProductType(productType).build());

        return policyResponse.getGracePeriodDays();
    }
}
