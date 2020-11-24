package com.zilch.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.lang.NonNull;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TransactionDTO {

    private String transactionId;

    @NonNull
    private BigDecimal amount;

    @NonNull
    private String currency;

    @NonNull
    private TransactionType transactionType;

    private LocalDateTime executionTime;

    @NonNull
    private String customerId;

    private String customerFirstName;

    private String customerLastName;

    public enum TransactionType {
        PAYMENT, INCOME_TRANSFER, OUTCOME_TRANSFER
    }
}
