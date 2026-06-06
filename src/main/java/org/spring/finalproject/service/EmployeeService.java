package org.spring.finalproject.service;

import org.spring.finalproject.dto.request.EmployeeDto;

import java.util.List;

public interface EmployeeService {

    List<EmployeeDto> findAll();

    EmployeeDto findById(Long id);

    EmployeeDto save(EmployeeDto dto);

    EmployeeDto update(Long id, EmployeeDto dto);

    void delete(Long id);
}
