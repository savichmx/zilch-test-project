package com.zilch.service;

import com.zilch.dto.TransactionDTO;
import com.zilch.entity.Transaction;
import com.zilch.exception.CustomerNotFoundException;
import com.zilch.exception.TransactionNotFoundException;
import com.zilch.mapper.TransactionDtoMapper;
import com.zilch.repository.CustomerRepository;
import com.zilch.repository.TransactionRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static data.TestDataProvider.createCustomer;
import static data.TestDataProvider.createTransaction;
import static data.TestDataProvider.createTransactionDTO;
import static java.util.Collections.singletonList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TransactionServiceTest {

    @InjectMocks
    private TransactionService transactionService;

    @Mock
    private TransactionRepository transactionRepository;

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private TransactionDtoMapper transactionDtoMapper;

    @Test
    void shouldFindAllTransactionsByCustomerId() {
        UUID customerId = UUID.randomUUID();
        List<Transaction> transactions = singletonList(createTransaction());
        List<TransactionDTO> mappedTransactions = singletonList(createTransactionDTO());
        when(transactionRepository.findAllByCustomerCustomerId(customerId)).thenReturn(transactions);
        when(transactionDtoMapper.mapToDTO(transactions.get(0))).thenReturn(mappedTransactions.get(0));

        List<TransactionDTO> result = transactionService.loadTransactionsByCustomerId(customerId);

        assertThat(result).containsExactlyElementsOf(mappedTransactions);
    }

    @Test
    void shouldFindTransactionByTransactionId() {
        UUID transactionId = UUID.randomUUID();
        Transaction transaction = createTransaction();
        TransactionDTO mappedTransaction = createTransactionDTO();
        when(transactionRepository.findById(transactionId)).thenReturn(Optional.of(transaction));
        when(transactionDtoMapper.mapToDTO(transaction)).thenReturn(mappedTransaction);

        TransactionDTO result = transactionService.getTransactionById(transactionId);

        assertThat(result).isEqualTo(mappedTransaction);
    }

    @Test
    void shouldThrowTransactionNotFoundExceptionIfDidNotFindByTransactionId() {
        UUID transactionId = UUID.randomUUID();
        Transaction transaction = createTransaction();
        when(transactionRepository.findById(transactionId)).thenReturn(Optional.empty());

        assertThrows(TransactionNotFoundException.class,
                () -> transactionService.getTransactionById(transactionId),
                "There is no transaction with such transaction id.");

        verify(transactionDtoMapper, never()).mapToDTO(any(Transaction.class));
    }

    @Test
    void shouldNotThrowTransactionNotFoundExceptionIfTransactionWasDeletedByTransactionId() {
        UUID transactionId = UUID.randomUUID();
        when(transactionRepository.deleteByTransactionId(transactionId)).thenReturn(1);

        transactionService.deleteTransactionById(transactionId);

        verify(transactionRepository).deleteByTransactionId(transactionId);
    }

    @Test
    void shouldThrowTransactionNotFoundExceptionIfTransactionWasNotDeletedByTransactionId() {
        UUID transactionId = UUID.randomUUID();
        when(transactionRepository.deleteByTransactionId(transactionId)).thenReturn(0);

        assertThrows(TransactionNotFoundException.class,
                () -> transactionService.deleteTransactionById(transactionId),
                "It is not possible to delete transaction. Transaction with such id is not found.");
    }

    @Test
    void shouldSaveTransactionWithLoadedCustomer() {
        TransactionDTO inputTransactionDTO = createTransactionDTO();
        Transaction inputTransaction = createTransaction();
        when(transactionDtoMapper.mapToEntity(inputTransactionDTO)).thenReturn(inputTransaction);
        when(customerRepository.findById(inputTransaction.getCustomer().getCustomerId())).thenReturn(Optional.of(createCustomer()));
        Transaction savedTransaction = createTransaction();
        TransactionDTO mappedSavedTransaction = createTransactionDTO();
        when(transactionRepository.save(inputTransaction)).thenReturn(savedTransaction);
        when(transactionDtoMapper.mapToDTO(savedTransaction)).thenReturn(mappedSavedTransaction);

        TransactionDTO result = transactionService.saveTransaction(inputTransactionDTO);

        assertThat(result).isEqualTo(mappedSavedTransaction);
    }

    @Test
    void shouldThrowCustomerNotFoundExceptionWhenSaveTransactionWithLoadedCustomer() {
        TransactionDTO inputTransactionDTO = createTransactionDTO();
        Transaction inputTransaction = createTransaction();
        when(transactionDtoMapper.mapToEntity(inputTransactionDTO)).thenReturn(inputTransaction);
        when(customerRepository.findById(inputTransaction.getCustomer().getCustomerId())).thenReturn(Optional.empty());

        assertThrows(CustomerNotFoundException.class,
                () -> transactionService.saveTransaction(inputTransactionDTO),
                "It is not possible to delete transaction. Transaction with such id is not found.");
    }

}