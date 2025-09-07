// src/main/java/com/auth/client/CustomerClient.java
package com.auth.client;

import com.auth.dto.CustomerDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

        import org.springframework.http.MediaType;

@FeignClient(name = "customer-service")
public interface CustomerClient {

    @PostMapping(value = "/api/customers")
    CustomerDTO createCustomer(
            @RequestHeader("Authorization") String bearerToken,
            @RequestBody CustomerDTO customerDTO
    );
}

