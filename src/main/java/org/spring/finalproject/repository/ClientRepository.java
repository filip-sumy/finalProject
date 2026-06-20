package org.spring.finalproject.repository;

import org.spring.finalproject.entity.Client;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ClientRepository extends JpaRepository<Client, Long> {

    Optional<Client> findByEmail(String email);

    Page<Client> findByFirstNameContainingIgnoreCase(
            String name,
            Pageable pageable
    );
}
