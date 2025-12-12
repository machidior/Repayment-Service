//package com.machidior.Repayment_service.kafka;
//
//import lombok.RequiredArgsConstructor;
//import org.springframework.kafka.annotation.KafkaListener;
//import org.springframework.stereotype.Service;
//
//@Service
//@RequiredArgsConstructor
//public class LoanEventConsumer {
//
//    @KafkaListener(topics = "loan_disbursed", groupId = "repayment-service")
//    public void consumeLoanDisbursed(LoanDisbursedEvent event) {
//        System.out.println("Received loan disbursed event: " + event);
//        // Generate repayment schedule based on event
//    }
//}
