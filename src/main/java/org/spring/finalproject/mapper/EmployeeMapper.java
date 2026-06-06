package org.spring.finalproject.mapper;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.spring.finalproject.dto.EmployeeDto;
import org.spring.finalproject.entity.Employee;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class EmployeeMapper {

    private final ModelMapper modelMapper;

    public EmployeeDto toDto(Employee employee) {
        return modelMapper.map(employee, EmployeeDto.class);
    }

    public Employee toEntity(EmployeeDto dto) {
        return modelMapper.map(dto, Employee.class);
    }
}
