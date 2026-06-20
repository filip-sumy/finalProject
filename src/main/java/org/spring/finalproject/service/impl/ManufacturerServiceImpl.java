package org.spring.finalproject.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.spring.finalproject.dto.ManufacturerDto;
import org.spring.finalproject.entity.Manufacturer;
import org.spring.finalproject.exception.EntityInUseException;
import org.spring.finalproject.exception.EntityNotFoundException;
import org.spring.finalproject.mapper.ManufacturerMapper;
import org.spring.finalproject.repository.ApplianceRepository;
import org.spring.finalproject.repository.ManufacturerRepository;
import org.spring.finalproject.service.ManufacturerService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class ManufacturerServiceImpl implements ManufacturerService {

    private final ManufacturerRepository repository;
    private final ApplianceRepository applianceRepository;
    private final ManufacturerMapper manufacturerMapper;

    @Override
    @Transactional(readOnly = true)
    public List<ManufacturerDto> findAll() {
        return repository.findAll()
                .stream()
                .map(manufacturerMapper::toDto)
                .toList();
    }

    @Transactional(readOnly = true)
    @Override
    public Page<ManufacturerDto> findAll(String search, Pageable pageable) {
        Page<Manufacturer> page;

        if (search != null && !search.isBlank()) {
            page = repository.findByNameContainingIgnoreCase(search.trim(), pageable);
        } else {
            page = repository.findAll(pageable);
        }

        return page.map(manufacturerMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public ManufacturerDto findById(Long id) {
        Manufacturer manufacturer = repository.findById(id)
                .orElseThrow(() ->
                        new EntityNotFoundException("error.not.found.manufacturer"));

        return manufacturerMapper.toDto(manufacturer);
    }

    @Override
    public ManufacturerDto save(ManufacturerDto dto) {
        Manufacturer saved = repository.save(manufacturerMapper.toEntity(dto));
        log.info("Manufacturer created: id={}, name={}", saved.getId(), saved.getName());
        return manufacturerMapper.toDto(saved);
    }

    @Override
    public ManufacturerDto update(Long id, ManufacturerDto dto) {
        Manufacturer manufacturer = repository.findById(id)
                .orElseThrow(() ->
                        new EntityNotFoundException("error.not.found.manufacturer"));

        manufacturer.setName(dto.getName());
        manufacturer.setCountry(dto.getCountry());

        log.info("Manufacturer updated: id={}", id);
        return manufacturerMapper.toDto(manufacturer);
    }

    @Override
    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new EntityNotFoundException("error.not.found.manufacturer.with.id", id);
        }

        if (applianceRepository.countByManufacturer_Id(id) > 0) {
            throw new EntityInUseException("error.delete.manufacturer.in.use");
        }

        repository.deleteById(id);
        log.warn("Manufacturer deleted: id={}", id);
    }
}
