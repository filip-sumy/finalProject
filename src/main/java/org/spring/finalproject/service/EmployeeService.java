package org.spring.finalproject.service;

import org.spring.finalproject.dto.ClientDto;
import org.spring.finalproject.dto.EmployeeDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface EmployeeService {

    List<EmployeeDto> findAll();

    Page<EmployeeDto> findAll(Pageable pageable);

    EmployeeDto findById(Long id);

    EmployeeDto save(EmployeeDto dto);

    EmployeeDto update(Long id, EmployeeDto dto);

    void delete(Long id);

    Page<EmployeeDto> findAll(
            String search,
            Pageable pageable);

}
