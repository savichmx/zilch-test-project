package com.zilch.service;

import com.zilch.dto.TransactionDTO;
import com.zilch.entity.Customer;
import com.zilch.entity.Transaction;
import com.zilch.exception.CustomerNotFoundException;
import com.zilch.exception.TransactionNotFoundException;
import com.zilch.mapper.TransactionDtoMapper;
import com.zilch.repository.CustomerRepository;
import com.zilch.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TransactionService {

    private final TransactionDtoMapper transactionDtoMapper;

    private final TransactionRepository transactionRepository;

    private final CustomerRepository customerRepository;

    public List<TransactionDTO> loadTransactionsByCustomerId(UUID customerId) {
        return transactionRepository.findAllByCustomerCustomerId(customerId).stream()
                .map(transactionDtoMapper::mapToDTO)
                .collect(Collectors.toList());
    }

    public TransactionDTO getTransactionById(UUID id) {
        return transactionRepository.findById(id)
                .map(transactionDtoMapper::mapToDTO)
                .orElseThrow(() -> new TransactionNotFoundException("There is no transaction with such transaction id."));
    }

    public void deleteTransactionById(UUID id) {
        int deletedCount = transactionRepository.deleteByTransactionId(id);
        if (deletedCount == 0) {
            throw new TransactionNotFoundException("It is not possible to delete transaction. Transaction with such id is not found.");
        }
    }

    public TransactionDTO saveTransaction(TransactionDTO transactionDTO) {
        Transaction transaction = transactionDtoMapper.mapToEntity(transactionDTO);
        Customer customer = customerRepository.findById(transaction.getCustomer().getCustomerId())
                .orElseThrow(() -> new CustomerNotFoundException("Customer with such customer id is not found"));
        transaction.setCustomer(customer);
        transaction.setExecutionTime(LocalDateTime.now());
        return transactionDtoMapper.mapToDTO(transactionRepository.save(transaction));
    }

}
