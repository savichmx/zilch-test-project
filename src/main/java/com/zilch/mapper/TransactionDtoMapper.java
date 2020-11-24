package com.zilch.mapper;

import com.zilch.dto.TransactionDTO;
import com.zilch.entity.Customer;
import com.zilch.entity.Transaction;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.UUID;

import static java.util.Objects.isNull;
import static java.util.Optional.ofNullable;

@Component
@RequiredArgsConstructor
public class TransactionDtoMapper {

    private final ModelMapper modelMapper;

    public TransactionDTO mapToDTO(Transaction transaction) {
        return modelMapper.map(transaction, TransactionDTO.class);
    }

    public Transaction mapToEntity(TransactionDTO transactionDTO) {
        Transaction transaction = modelMapper.map(transactionDTO, Transaction.class);

        transaction.setTransactionId(
                ofNullable(transactionDTO.getTransactionId())
                .map(UUID::fromString)
                .orElse(null)
        );

        if (isNull(transaction.getCustomer())) {
            transaction.setCustomer(new Customer());
        }
        transaction.getCustomer().setCustomerId(UUID.fromString(transactionDTO.getCustomerId()));

        return transaction;
    }

}
