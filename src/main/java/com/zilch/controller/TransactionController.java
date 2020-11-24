package com.zilch.controller;

import com.zilch.dto.TransactionDTO;
import com.zilch.service.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

import static org.springframework.http.HttpStatus.ACCEPTED;
import static org.springframework.http.HttpStatus.NO_CONTENT;

@RestController
@RequestMapping("transactions")
@RequiredArgsConstructor
class TransactionController {

    private final TransactionService transactionService;

    @GetMapping("/all")
    List<TransactionDTO> getAllByCustomerId(@RequestParam UUID customerId) {
        return transactionService.loadTransactionsByCustomerId(customerId);
    }

    @GetMapping("/get")
    TransactionDTO getByTransactionId(@RequestParam UUID id) {
        return transactionService.getTransactionById(id);
    }

    @DeleteMapping("/delete")
    ResponseEntity deleteByTransactionId(@RequestParam UUID id) {
        transactionService.deleteTransactionById(id);
        return new ResponseEntity(NO_CONTENT);
    }

    @PutMapping("/save")
    @ResponseStatus(ACCEPTED)
    TransactionDTO saveTransaction(@RequestBody TransactionDTO transactionDTO) {
        return transactionService.saveTransaction(transactionDTO);
    }

}
