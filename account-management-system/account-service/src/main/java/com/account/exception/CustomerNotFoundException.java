package com.account.exception;

public class CustomerNotFoundException extends RuntimeException {
    public CustomerNotFoundException(String customerId) {
        super("Customer with ID " + customerId + " does not exist.");
    }
}
