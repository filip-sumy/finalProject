package org.spring.finalproject.repository;

import org.spring.finalproject.entity.Appliance;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ApplianceRepository extends JpaRepository<Appliance, Long> {

    List<Appliance> findByNameContainingIgnoreCase(String name);

    Page<Appliance> findByNameContainingIgnoreCase(
            String name,
            Pageable pageable
    );
}
