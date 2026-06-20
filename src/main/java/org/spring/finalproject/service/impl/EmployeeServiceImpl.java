package org.spring.finalproject.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.spring.finalproject.entity.Role;
import org.spring.finalproject.dto.EmployeeDto;
import org.spring.finalproject.entity.Employee;
import org.spring.finalproject.exception.EntityNotFoundException;
import org.spring.finalproject.mapper.EmployeeMapper;
import org.spring.finalproject.repository.EmployeeRepository;
import org.spring.finalproject.service.EmployeeService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class EmployeeServiceImpl implements EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final EmployeeMapper employeeMapper;
    private final PasswordEncoder passwordEncoder;

    @Transactional(readOnly = true)
    @Override
    public Page<EmployeeDto> findAll(
            String search,
            Pageable pageable) {

        Page<Employee> page;

        if (search != null && !search.isBlank()) {
            page = employeeRepository
                    .findByFirstNameContainingIgnoreCase(
                            search.trim(), pageable);
        } else {
            page = employeeRepository.findAll(pageable);
        }

        return page.map(employeeMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public EmployeeDto findById(Long id) {

        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() ->
                        new EntityNotFoundException(
                                "error.not.found.employee", id));

        return employeeMapper.toDto(employee);
    }

    @Override
    public EmployeeDto save(EmployeeDto dto) {

        Employee employee = employeeMapper.toEntity(dto);

        employee.setRole(Role.ROLE_EMPLOYEE);

        employee.setPassword(
                passwordEncoder.encode(
                        dto.getPassword()));

        Employee saved = employeeRepository.save(employee);

        log.info("Employee created: {}", saved.getEmail());

        return employeeMapper.toDto(saved);
    }

    @Override
    public EmployeeDto update(Long id, EmployeeDto dto) {

        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() ->
                        new EntityNotFoundException(
                                "error.not.found.employee", id));

        employee.setFirstName(dto.getFirstName());
        employee.setLastName(dto.getLastName());
        employee.setEmail(dto.getEmail());
        employee.setPosition(dto.getPosition());
        employee.setSalary(dto.getSalary());

        if (dto.getPassword() != null
                && !dto.getPassword().isBlank()) {

            employee.setPassword(
                    passwordEncoder.encode(dto.getPassword()));
        }

        log.info("Employee updated: {}", id);

        return employeeMapper.toDto(employee);
    }

    @Override
    public void delete(Long id) {

        if (!employeeRepository.existsById(id)) {

            throw new EntityNotFoundException(
                    "error.not.found.employee", id);
        }

        employeeRepository.deleteById(id);

        log.warn("Employee deleted: {}", id);
    }
}