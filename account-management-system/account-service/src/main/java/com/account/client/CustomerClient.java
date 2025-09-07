package com.account.client;

import com.account.dto.CustomerDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

// Feign Client to communicate with Customer Service

@FeignClient(
        name = "customer-service",
        configuration = com.account.config.FeignConfig.class
)
public interface CustomerClient {

    @GetMapping("/api/customers/{id}/exists")
    boolean checkCustomerExists(@PathVariable("id") String customerId);

    @GetMapping("/api/customers/{id}")
    CustomerDTO getCustomerById(@PathVariable("id") String customerId);
}
