package org.spring.finalproject.repository;

import org.spring.finalproject.entity.Appliance;
import org.spring.finalproject.entity.Manufacturer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ManufacturerRepository
        extends JpaRepository<Manufacturer, Long> {

    List<Manufacturer> findByNameContainingIgnoreCase(String name);

    Page<Manufacturer> findByNameContainingIgnoreCase(
            String name,
            Pageable pageable
    );
}
