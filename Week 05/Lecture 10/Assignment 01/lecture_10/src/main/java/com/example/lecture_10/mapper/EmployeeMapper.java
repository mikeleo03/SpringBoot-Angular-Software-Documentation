package com.example.lecture_10.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import com.example.lecture_10.data.model.Employee;
import com.example.lecture_10.dto.EmployeeDTO;

@Mapper(componentModel = "spring")
public interface EmployeeMapper {
    
    // Mapper instance
    EmployeeMapper INSTANCE = Mappers.getMapper(EmployeeMapper.class);

    // Mapper to Employee DTO
    EmployeeDTO toEmployeeDTO(Employee employee);

    // Mappet to Employee model
    Employee toEmployee(EmployeeDTO employeeDTO);
}
