package com.account.exception;

public class CustomerIdRequiredException extends RuntimeException {
    public CustomerIdRequiredException() {
        super("CustomerId is required to create an account.");
    }
}