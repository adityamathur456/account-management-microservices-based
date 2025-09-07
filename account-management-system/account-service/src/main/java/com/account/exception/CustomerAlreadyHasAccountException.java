package com.account.exception;

public class CustomerAlreadyHasAccountException extends RuntimeException {
    public CustomerAlreadyHasAccountException(String customerId) {
        super("Customer with ID " + customerId + " already has an account.");
    }
}

