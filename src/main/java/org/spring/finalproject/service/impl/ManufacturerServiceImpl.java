package org.spring.finalproject.service.impl;

import lombok.RequiredArgsConstructor;
import org.spring.finalproject.dto.request.ManufacturerDto;
import org.spring.finalproject.entity.Manufacturer;
import org.spring.finalproject.exception.EntityNotFoundException;
import org.spring.finalproject.mapper.ManufacturerMapper;
import org.spring.finalproject.repository.ManufacturerRepository;
import org.spring.finalproject.service.ManufacturerService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class ManufacturerServiceImpl
        implements ManufacturerService {

    private final ManufacturerRepository repository;
    private final ManufacturerMapper mapper;

    @Override
    public List<ManufacturerDto> findAll() {

        return repository.findAll()
                .stream()
                .map(mapper::toDto)
                .toList();
    }

    @Override
    public ManufacturerDto findById(Long id) {

        Manufacturer manufacturer =
                repository.findById(id)
                        .orElseThrow(() ->
                                new EntityNotFoundException(
                                        "Manufacturer not found"));

        return mapper.toDto(manufacturer);
    }

    @Override
    public ManufacturerDto save(ManufacturerDto dto) {

        Manufacturer saved =
                repository.save(mapper.toEntity(dto));

        return mapper.toDto(saved);
    }

    @Override
    public ManufacturerDto update(Long id,
                                  ManufacturerDto dto) {

        Manufacturer manufacturer =
                repository.findById(id)
                        .orElseThrow(() ->
                                new EntityNotFoundException(
                                        "Manufacturer not found"));

        manufacturer.setName(dto.getName());
        manufacturer.setCountry(dto.getCountry());

        return mapper.toDto(manufacturer);
    }

    @Override
    public void delete(Long id) {

        repository.deleteById(id);
    }
}
