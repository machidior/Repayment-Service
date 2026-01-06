package com.machidior.Repayment_service.model;

import com.machidior.Repayment_service.enums.DefaultStatus;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;

@Entity
@Table
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DefaultDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String loanId;
    @CreatedDate
    private LocalDateTime defaultDate;
    private Integer daysInDefault;
    private String defaultReason;
    @Enumerated(EnumType.STRING)
    private DefaultStatus status;
}
