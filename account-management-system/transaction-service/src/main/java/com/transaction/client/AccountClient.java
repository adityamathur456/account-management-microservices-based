package com.transaction.client;

import com.transaction.dto.AccountDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient(
        name = "account-service",
        path = "/api/accounts",
        configuration = com.transaction.config.FeignConfig.class
)
public interface AccountClient {

    @GetMapping("/{accountNo}")
    AccountDTO getAccountByAccountNo(@PathVariable("accountNo") String accountNo);

    @PutMapping("/{accountNo}/debit")
    void debit(@PathVariable("accountNo") String accountNo, @RequestParam("amount") Double amount);

    @PutMapping("/{accountNo}/credit")
    void credit(@PathVariable("accountNo") String accountNo, @RequestParam("amount") Double amount);
}
