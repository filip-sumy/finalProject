package org.spring.finalproject.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.spring.finalproject.dto.EmployeeDto;
import org.spring.finalproject.entity.Employee;
import org.spring.finalproject.entity.Role;
import org.spring.finalproject.exception.EntityNotFoundException;
import org.spring.finalproject.mapper.EmployeeMapper;
import org.spring.finalproject.repository.EmployeeRepository;
import org.spring.finalproject.service.impl.EmployeeServiceImpl;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class EmployeeServiceImplTest {

    @Mock
    private EmployeeRepository employeeRepository;

    @Mock
    private EmployeeMapper employeeMapper;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private EmployeeServiceImpl employeeService;

    @Test
    void findById_returnsDto_whenEmployeeExists() {

        Employee employee = new Employee();
        employee.setId(1L);
        employee.setEmail("test@test.com");

        EmployeeDto dto = new EmployeeDto();
        dto.setId(1L);

        when(employeeRepository.findById(1L))
                .thenReturn(Optional.of(employee));
        when(employeeMapper.toDto(employee)).thenReturn(dto);

        EmployeeDto result = employeeService.findById(1L);

        assertEquals(1L, result.getId());
    }

    @Test
    void findById_throws_whenEmployeeMissing() {

        when(employeeRepository.findById(99L))
                .thenReturn(Optional.empty());

        assertThrows(
                EntityNotFoundException.class,
                () -> employeeService.findById(99L));
    }

    @Test
    void save_encodesPasswordAndSetsRole() {

        EmployeeDto dto = new EmployeeDto();
        dto.setPassword("Password1!");
        dto.setEmail("new@test.com");
        dto.setFirstName("A");
        dto.setLastName("B");
        dto.setSalary(BigDecimal.TEN);

        Employee entity = new Employee();
        Employee saved = new Employee();
        saved.setId(5L);
        saved.setEmail("new@test.com");

        when(employeeMapper.toEntity(dto)).thenReturn(entity);
        when(passwordEncoder.encode("Password1!"))
                .thenReturn("encoded");
        when(employeeRepository.save(entity)).thenReturn(saved);
        when(employeeMapper.toDto(saved)).thenReturn(dto);

        employeeService.save(dto);

        verify(employeeRepository).save(entity);
        assertEquals(Role.ROLE_EMPLOYEE, entity.getRole());
        assertEquals("encoded", entity.getPassword());
    }
}
