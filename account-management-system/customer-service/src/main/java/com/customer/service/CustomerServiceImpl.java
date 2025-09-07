package com.customer.service;

import com.customer.dto.CustomerDTO;
import com.customer.dto.AddressDTO;
import com.customer.entity.Customer;
import com.customer.entity.Address;
import com.customer.mapper.CustomerMapper;
import com.customer.repository.CustomerRepository;
import com.customer.service.CustomerService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepository;
    private final CustomerMapper mapper;
    @Override
    @Transactional
    public CustomerDTO createCustomer(CustomerDTO customerDTO) {
        Customer customer = mapper.toEntity(customerDTO);
        Customer createdCustomer = customerRepository.save(customer);
        return mapper.toDto(createdCustomer);
    }

    @Override
    public CustomerDTO getCustomerById(String id) {
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Customer not found with id: " + id));
        return entityToDTO(customer);
    }

    public Boolean checkCustomerExists(String id) {
        return customerRepository.existsById(id);
    }

    @Override
    public List<CustomerDTO> getAllCustomers() {
        return customerRepository.findAll()
                .stream()
                .map(this::entityToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public CustomerDTO updateCustomer(String id, CustomerDTO customerDTO) {
        Customer existing = customerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Customer not found with id: " + id));

        existing.setFirstName(customerDTO.getFirstName());
        existing.setLastName(customerDTO.getLastName());
        existing.setEmail(customerDTO.getEmail());
        existing.setPhoneNumber(customerDTO.getPhoneNumber());
        existing.setDateOfBirth(customerDTO.getDateOfBirth());
        existing.setAddress(dtoToEntity(customerDTO.getAddress()));

        return entityToDTO(customerRepository.save(existing));
    }

    @Override
    public CustomerDTO findByEmail(String email) {
        Customer customer = customerRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Customer not found with email: " + email));
        return entityToDTO(customer);
    }

    @Override
    public CustomerDTO findByPhoneNumber(String phoneNumber) {
        Customer customer = customerRepository.findByPhoneNumber(phoneNumber)
                .orElseThrow(() -> new RuntimeException("Customer not found with phone number: " + phoneNumber));
        return entityToDTO(customer);
    }


    @Override
    public void deleteCustomer(String id) {
        customerRepository.deleteById(id);
    }

    @Override
    public void deleteAll() {
        customerRepository.deleteAll();
    }

    // ---------- Mappers ----------
    private Customer dtoToEntity(CustomerDTO dto) {
        return Customer.builder()
                .id(dto.getId())
                .firstName(dto.getFirstName())
                .lastName(dto.getLastName())
                .email(dto.getEmail())
                .phoneNumber(dto.getPhoneNumber())
                .dateOfBirth(dto.getDateOfBirth())
                .address(dtoToEntity(dto.getAddress()))
                .build();
    }

    private Address dtoToEntity(AddressDTO dto) {
        if (dto == null) return null;
        return Address.builder()
                .street(dto.getStreet())
                .houseNo(dto.getHouseNo())
                .city(dto.getCity())
                .state(dto.getState())
                .pincode(dto.getPincode())
                .build();
    }

    private CustomerDTO entityToDTO(Customer entity) {
        return CustomerDTO.builder()
                .id(entity.getId())
                .firstName(entity.getFirstName())
                .lastName(entity.getLastName())
                .email(entity.getEmail())
                .phoneNumber(entity.getPhoneNumber())
                .dateOfBirth(entity.getDateOfBirth())
                .address(entityToDTO(entity.getAddress()))
                .build();
    }

    private AddressDTO entityToDTO(Address entity) {
        if (entity == null) return null;
        return AddressDTO.builder()
                .street(entity.getStreet())
                .houseNo(entity.getHouseNo())
                .city(entity.getCity())
                .state(entity.getState())
                .pincode(entity.getPincode())
                .build();
    }
}
