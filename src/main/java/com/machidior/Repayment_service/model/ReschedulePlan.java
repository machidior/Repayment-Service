package com.machidior.Repayment_service.model;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ReschedulePlan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long OldScheduleId;
    private Long newScheduleId;
    private BigDecimal differenceInTotalInterest;
    private String approvedBy;
    @CreatedDate
    private LocalDateTime approvedAt;
}
