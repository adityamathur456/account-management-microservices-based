package com.account.controller;


import com.account.dto.AccountDTO;
import com.account.dto.AccountDetailsDTO;
import com.account.service.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("api/accounts")
@RequiredArgsConstructor
public class AccountController {

    private final AccountService accountService;

    @PostMapping
    public ResponseEntity<AccountDTO> createAccount(@RequestBody AccountDTO dto) {
        return ResponseEntity.ok(accountService.createAccount(dto));
    }

    @GetMapping
    public ResponseEntity<List<AccountDTO>> getAll() {
        return ResponseEntity.ok(accountService.getAllAccounts());
    }

    // Get account details by account number
    @GetMapping("/{accountNo}")
    public ResponseEntity<AccountDTO> getAccountByAccountNo(@PathVariable("accountNo") String accountNo) {
        return ResponseEntity.ok(accountService.getByAccountNo(accountNo));
    }

    // Debit endpoint
    @PutMapping("/{accountNo}/debit")
    public ResponseEntity<String> debit(@PathVariable("accountNo") String accountNo,  @RequestParam("amount") Double amount) {
        try {
            accountService.debit(accountNo, amount);
            return ResponseEntity.ok("Debited successfully");
        } catch (RuntimeException ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }

    // Credit endpoint
    @PutMapping("/{accountNo}/credit")
    public ResponseEntity<String> credit(@PathVariable("accountNo") String accountNo, @RequestParam("amount") Double amount) {
        try {
            accountService.credit(accountNo, amount);
            return ResponseEntity.ok("Credited successfully");
        } catch (RuntimeException ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }

    @GetMapping("/AccountDetail/{customerId}")
    public ResponseEntity<Optional<AccountDTO>> getAccountDetailsByCustomerId(@PathVariable("customerId") String customerId){
        Optional<AccountDTO> account = accountService.getAccountByCustomerId(customerId);
        if (account.isEmpty())
            return ResponseEntity.notFound().build();
        return ResponseEntity.ok(account);
    }

    @GetMapping("/{accountNo}/details")
    public ResponseEntity<AccountDetailsDTO> getAccountDetails(@PathVariable("accountNo") String accountNo) {
        return ResponseEntity.ok(accountService.getAccountDetails(accountNo));
    }

//    @PatchMapping("/{accountNo}/balance")
//    public ResponseEntity<AccountDTO> updateBalance(@PathVariable("accountNo") String accountNo, @RequestParam("amount") Double amount) {
//        AccountDTO updated = accountService.updateBalance(accountNo, amount);
//        return ResponseEntity.ok(updated);
//    }

    @DeleteMapping("/{accountNo}")
    public ResponseEntity<String> deleteAccount(@PathVariable("accountNo") String accountNo) {
        boolean deleted = accountService.deleteAccount(accountNo);
        if(deleted)
            return ResponseEntity.ok("Deleted Successfully");
        else
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Account Not Found");
    }
}
