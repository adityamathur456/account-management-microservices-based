package com.customer.service;

import com.customer.dto.CustomerDTO;
import java.util.List;

public interface CustomerService {
    CustomerDTO createCustomer(CustomerDTO customerDTO);
    CustomerDTO getCustomerById(String id);
    List<CustomerDTO> getAllCustomers();
    CustomerDTO updateCustomer(String id, CustomerDTO customerDTO);
    void deleteCustomer(String id);
    void deleteAll();

    // New methods
    CustomerDTO findByEmail(String email);
    CustomerDTO findByPhoneNumber(String phoneNumber);
    Boolean checkCustomerExists(String id);
}
