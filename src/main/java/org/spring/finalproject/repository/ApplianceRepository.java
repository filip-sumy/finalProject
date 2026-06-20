package org.spring.finalproject.repository;

import org.spring.finalproject.entity.Appliance;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ApplianceRepository extends JpaRepository<Appliance, Long> {

    Page<Appliance> findByNameContainingIgnoreCase(
            String name,
            Pageable pageable
    );

    long countByManufacturer_Id(Long manufacturerId);
}
