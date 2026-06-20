package org.spring.finalproject.service;

import org.spring.finalproject.dto.EmployeeDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface EmployeeService {

    EmployeeDto findById(Long id);

    EmployeeDto save(EmployeeDto dto);

    EmployeeDto update(Long id, EmployeeDto dto);

    void delete(Long id);

    Page<EmployeeDto> findAll(
            String search,
            Pageable pageable);
}
