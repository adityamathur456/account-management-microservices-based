package com.transaction.controller;

import com.transaction.dto.TransactionRequestDTO;
import com.transaction.dto.TransactionResponseDTO;
import com.transaction.service.TransactionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/transactions")
@RequiredArgsConstructor
public class TransactionController {

    private final TransactionService transactionService;

    @PostMapping
    public ResponseEntity<TransactionResponseDTO> createTransaction(@Valid @RequestBody TransactionRequestDTO request) {
        return ResponseEntity.ok(transactionService.createTransaction(request));
    }

    @GetMapping
    public ResponseEntity<List<TransactionResponseDTO>> getAllTransactions() {
        return ResponseEntity.ok(transactionService.getAllTransactions());
    }

    @GetMapping("/{id}")
    public ResponseEntity<TransactionResponseDTO> getTransactionById(@PathVariable("id") Long id) {
        TransactionResponseDTO dto = transactionService.getTransactionById(id);
        return dto != null ? ResponseEntity.ok(dto) : ResponseEntity.notFound().build();
    }

    @GetMapping("/account/{accountNo}")
    public ResponseEntity<List<TransactionResponseDTO>> getTransactionsByAccount(@PathVariable("accountNo") String accountNo) {
        return ResponseEntity.ok(transactionService.getTransactionsByAccount(accountNo));
    }
}
