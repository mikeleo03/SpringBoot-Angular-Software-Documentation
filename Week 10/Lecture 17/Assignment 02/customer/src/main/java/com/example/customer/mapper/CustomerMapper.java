package com.example.customer.mapper;

import com.example.customer.data.model.Customer;
import com.example.customer.dto.CustomerDTO;
import com.example.customer.dto.CustomerSaveDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface CustomerMapper {

    CustomerMapper INSTANCE = Mappers.getMapper(CustomerMapper.class);

    // Customer - CustomerDTO
    CustomerDTO toCustomerDTO(Customer customer);

    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    Customer toCustomer(CustomerDTO customerDTO);

    // Customer - CustomerSaveDTO
    CustomerSaveDTO toCustomerSaveDTO(Customer customer);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    Customer toCustomer(CustomerSaveDTO customerSaveDTO);
}

