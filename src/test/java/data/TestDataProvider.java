package data;

import com.zilch.dto.TransactionDTO;
import com.zilch.entity.Customer;
import com.zilch.entity.Transaction;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public class TestDataProvider {

    public static Transaction createTransaction() {
        return Transaction.builder()
                .transactionId(UUID.randomUUID())
                .transactionType(Transaction.TransactionType.INCOME_TRANSFER)
                .amount(BigDecimal.TEN)
                .currency("USD")
                .executionTime(LocalDateTime.now())
                .customer(Customer.builder()
                        .customerId(UUID.randomUUID())
                        .firstName("first name")
                        .lastName("last name")
                        .build())
                .build();
    }

    public static TransactionDTO createTransactionDTO() {
        return TransactionDTO.builder()
                .transactionId(UUID.randomUUID().toString())
                .amount(BigDecimal.ONE)
                .currency("PLN")
                .customerFirstName("John")
                .customerLastName("Walker")
                .customerId(UUID.randomUUID().toString())
                .executionTime(LocalDateTime.now())
                .transactionType(TransactionDTO.TransactionType.PAYMENT)
                .build();
    }

    public static Customer createCustomer() {
        return Customer.builder()
                .customerId(UUID.randomUUID())
                .firstName("first name")
                .lastName("last name")
                .build();
    }

}
