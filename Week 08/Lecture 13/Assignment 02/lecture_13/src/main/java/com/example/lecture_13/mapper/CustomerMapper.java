package com.example.lecture_13.mapper;

import com.example.lecture_13.data.model.Customer;
import com.example.lecture_13.dto.CustomerDTO;
import com.example.lecture_13.dto.CustomerShowDTO;
import com.example.lecture_13.dto.CustomerSaveDTO;

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

    // Customer - CustomerShowDTO
    CustomerShowDTO toCustomerShowDTO(Customer customer);

    @Mapping(target = "status", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    Customer toCustomer(CustomerShowDTO customerShowDTO);

    // Customer - CustomerSaveDTO
    CustomerSaveDTO toCustomerSaveDTO(Customer customer);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    Customer toCustomer(CustomerSaveDTO customerSaveDTO);
}
