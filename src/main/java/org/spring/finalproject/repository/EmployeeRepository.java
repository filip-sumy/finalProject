package org.spring.finalproject.repository;

import org.spring.finalproject.entity.Client;
import org.spring.finalproject.entity.Employee;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {

    Optional<Employee> findByEmail(String email);

    boolean existsByEmail(String email);

    List<Employee> findByFirstNameContainingIgnoreCase(String name);

    Page<Employee> findByFirstNameContainingIgnoreCase(
            String name,
            Pageable pageable
    );
}
