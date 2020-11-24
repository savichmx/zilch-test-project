package com.zilch.mapper;

import com.zilch.dto.TransactionDTO;
import com.zilch.entity.Transaction;
import data.TestDataProvider;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;

import java.math.BigDecimal;
import java.util.UUID;

import static com.zilch.dto.TransactionDTO.TransactionType.PAYMENT;
import static org.assertj.core.api.Assertions.assertThat;

class TransactionDtoMapperTest {

    private final TransactionDtoMapper transactionDtoMapper = new TransactionDtoMapper(new ModelMapper());

    @Test
    void shouldMapAllFieldsFromEntityToDTO() {
        Transaction transaction = TestDataProvider.createTransaction();

        TransactionDTO transactionDTO = transactionDtoMapper.mapToDTO(transaction);

        assertThat(transactionDTO.getTransactionId()).isEqualTo(transaction.getTransactionId().toString());
        assertThat(transactionDTO.getAmount()).isEqualTo(transaction.getAmount());
        assertThat(transactionDTO.getTransactionType().toString()).isEqualTo(transaction.getTransactionType().toString());
        assertThat(transactionDTO.getAmount()).isEqualTo(transaction.getAmount());
        assertThat(transactionDTO.getCurrency()).isEqualTo(transaction.getCurrency());
        assertThat(transactionDTO.getExecutionTime()).isEqualTo(transaction.getExecutionTime());
        assertThat(transactionDTO.getCustomerId()).isEqualTo(transaction.getCustomer().getCustomerId().toString());
        assertThat(transactionDTO.getCustomerFirstName()).isEqualTo(transaction.getCustomer().getFirstName());
        assertThat(transactionDTO.getCustomerLastName()).isEqualTo(transaction.getCustomer().getLastName());
    }

    @Test
    void shouldMapAllFieldsFromDTOToEntity() {
        TransactionDTO transactionDTO = TestDataProvider.createTransactionDTO();

        Transaction transaction = transactionDtoMapper.mapToEntity(transactionDTO);

        assertThat(transaction.getAmount()).isEqualTo(transactionDTO.getAmount());
        assertThat(transaction.getCurrency()).isEqualTo(transactionDTO.getCurrency());
        assertThat(transaction.getTransactionId().toString()).isEqualTo(transactionDTO.getTransactionId());
        assertThat(transaction.getCustomer().getFirstName()).isEqualTo(transactionDTO.getCustomerFirstName());
        assertThat(transaction.getCustomer().getLastName()).isEqualTo(transactionDTO.getCustomerLastName());
        assertThat(transaction.getCustomer().getCustomerId().toString()).isEqualTo(transactionDTO.getCustomerId());
        assertThat(transaction.getExecutionTime()).isEqualTo(transactionDTO.getExecutionTime());
        assertThat(transaction.getTransactionType().toString()).isEqualTo(transactionDTO.getTransactionType().toString());
    }

    @Test
    void shouldCreateCustomerIfItWasNotCreatedByMapperInMapToEntity() {
        TransactionDTO transactionDTO = TransactionDTO.builder()
                .amount(BigDecimal.ONE)
                .currency("USD")
                .transactionType(PAYMENT)
                .customerId(UUID.randomUUID().toString())
                .build();

        Transaction transaction = transactionDtoMapper.mapToEntity(transactionDTO);

        assertThat(transaction.getCustomer()).isNotNull();
    }

}