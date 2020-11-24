package com.zilch.controller;


import com.zilch.dto.TransactionDTO;
import com.zilch.service.TransactionService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.UUID;

import static data.TestDataProvider.createTransactionDTO;
import static java.util.Collections.singletonList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TransactionControllerTest {

    @InjectMocks
    private TransactionController transactionController;

    @Mock
    private TransactionService transactionService;

    @Test
    void shouldGetAllTransactionsByCustomerIdFromService() {
        UUID customerId = UUID.randomUUID();
        List<TransactionDTO> transactions = singletonList(createTransactionDTO());
        when(transactionService.loadTransactionsByCustomerId(customerId)).thenReturn(transactions);

        List<TransactionDTO> result = transactionController.getAllByCustomerId(customerId);

        assertThat(result).containsExactlyElementsOf(transactions);
    }

    @Test
    void shouldGetTransactionByTransactionIdFromService() {
        UUID transactionId = UUID.randomUUID();
        TransactionDTO transaction = createTransactionDTO();
        when(transactionService.getTransactionById(transactionId)).thenReturn(transaction);

        TransactionDTO result = transactionController.getByTransactionId(transactionId);

        assertThat(result).isEqualTo(transaction);
    }

    @Test
    void shouldDeleteTransactionByTransactionIdByService() {
        UUID transactionId = UUID.randomUUID();

        ResponseEntity result = transactionController.deleteByTransactionId(transactionId);

        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
        verify(transactionService).deleteTransactionById(transactionId);
    }

    @Test
    void shouldSaveTransactionByService() {
        TransactionDTO inputTransaction = createTransactionDTO();
        TransactionDTO savedTransaction = createTransactionDTO();
        when(transactionService.saveTransaction(inputTransaction)).thenReturn(savedTransaction);

        TransactionDTO result = transactionController.saveTransaction(inputTransaction);

        assertThat(result).isEqualTo(savedTransaction);
    }

}