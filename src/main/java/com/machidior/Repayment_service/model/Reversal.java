package com.machidior.Repayment_service.model;

import com.machidior.Repayment_service.enums.ReversalStatus;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Reversal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long repaymentId;
    private String reason;
    private String reversedBy;
    @CreationTimestamp
    private LocalDateTime reversedDate;
    @Enumerated(EnumType.STRING)
    private ReversalStatus status;
}
