package com.zilch.module.testing;

import com.zilch.configuration.ModuleTestingConfiguration;
import com.zilch.dto.TransactionDTO;
import com.zilch.entity.Transaction;
import com.zilch.mapper.TransactionDtoMapper;
import com.zilch.repository.TransactionRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ContextConfiguration;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import static data.TestDataProvider.createTransaction;
import static java.text.MessageFormat.format;
import static java.util.Collections.singletonList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ContextConfiguration(classes = ModuleTestingConfiguration.class)
class TransactionWorkflowModuleTest {

    @LocalServerPort
    private Integer port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private TransactionDtoMapper transactionDtoMapper;

    @Test
    void shouldProperlyLoadTransactionsByCustomerId() {
        // given
        UUID customerId = UUID.randomUUID();
        Transaction storedTransaction = createTransaction();
        when(transactionRepository.findAllByCustomerCustomerId(customerId)).thenReturn(singletonList(storedTransaction));

        // when
        List<TransactionDTO> returnedTransactions = restTemplate.exchange(
                prepareUrl("/all?customerId={id}"),
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<TransactionDTO>>() {},
                customerId
        ).getBody();

        // then
        assertThat(returnedTransactions).containsExactly(transactionDtoMapper.mapToDTO(storedTransaction));
    }

    @Test
    void shouldReturnTransactionByIdIfItExists() {
        // given
        UUID transactionId = UUID.randomUUID();
        Transaction storedTransaction = createTransaction();
        when(transactionRepository.findById(transactionId)).thenReturn(Optional.of(storedTransaction));

        // when
        TransactionDTO returnedTransaction = restTemplate.getForObject(
                prepareUrl("/get?id={id}"),
                TransactionDTO.class,
                transactionId
        );

        // then
        assertThat(returnedTransaction).isEqualTo(transactionDtoMapper.mapToDTO(storedTransaction));
    }

    @Test
    void shouldReturnTransactionNotFoundExceptionWhenFindByIdIfItDoesNotExist() {
        // given
        UUID transactionId = UUID.randomUUID();
        when(transactionRepository.findById(transactionId)).thenReturn(Optional.empty());

        // when
        Map<String, Object> error = restTemplate.exchange(
                prepareUrl("/get?id={id}"),
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<Map<String, Object>>() {},
                transactionId
        ).getBody();

        // then
        assertThat(error).extractingByKey("status").isEqualTo(HttpStatus.NOT_FOUND.value());
        assertThat(error).extractingByKey("error").isEqualTo("Not Found");
        assertThat(error).extractingByKey("message").isEqualTo("There is no transaction with such transaction id.");
        assertThat(error).extractingByKey("path").isEqualTo("/transactions/get");
    }

    private String prepareUrl(String path) {
        return format("http://localhost:{0}/transactions{1}", port.toString(), path);
    }

}
