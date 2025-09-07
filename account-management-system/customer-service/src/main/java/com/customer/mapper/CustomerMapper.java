package com.customer.mapper;

import com.customer.dto.CustomerDTO;
import com.customer.dto.AddressDTO;
import com.customer.entity.Customer;
import com.customer.entity.Address;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface CustomerMapper {

    @Mapping(source = "address", target = "address")
    Customer toEntity(CustomerDTO customerDTO);

    @Mapping(source = "address", target = "address")
    CustomerDTO toDto(Customer customer);

    Address toEntity(AddressDTO addressDTO);
    AddressDTO toDto(Address address);
}




