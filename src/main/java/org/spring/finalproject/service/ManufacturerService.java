package org.spring.finalproject.service;

import org.spring.finalproject.dto.request.ManufacturerDto;

import java.util.List;

public interface ManufacturerService {

    List<ManufacturerDto> findAll();

    ManufacturerDto findById(Long id);

    ManufacturerDto save(ManufacturerDto dto);

    ManufacturerDto update(Long id,
                           ManufacturerDto dto);

    void delete(Long id);
}
