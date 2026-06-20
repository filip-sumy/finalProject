package org.spring.finalproject.service;

import org.spring.finalproject.dto.ManufacturerDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ManufacturerService {

    List<ManufacturerDto> findAll();

    ManufacturerDto findById(Long id);

    ManufacturerDto save(ManufacturerDto dto);

    ManufacturerDto update(Long id,
                           ManufacturerDto dto);

    void delete(Long id);

    Page<ManufacturerDto> findAll(
            String search,
            Pageable pageable);

}
