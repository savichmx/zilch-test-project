package com.zilch.repository;

import com.zilch.entity.Transaction;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Transactional
public interface TransactionRepository extends JpaRepository<Transaction, UUID> {

    @Cacheable(value = "transactionsByCustomerIdCache")
    List<Transaction> findAllByCustomerCustomerId(UUID customerId);

    int deleteByTransactionId(UUID transactionId);
}
