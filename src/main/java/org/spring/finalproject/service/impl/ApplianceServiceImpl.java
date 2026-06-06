package org.spring.finalproject.service.impl;

import lombok.RequiredArgsConstructor;
import org.spring.finalproject.dto.ApplianceDto;
import org.spring.finalproject.entity.Appliance;
import org.spring.finalproject.entity.Manufacturer;
import org.spring.finalproject.exception.EntityNotFoundException;
import org.spring.finalproject.mapper.ApplianceMapper;
import org.spring.finalproject.exception.EntityInUseException;
import org.spring.finalproject.repository.ApplianceRepository;
import org.spring.finalproject.repository.ManufacturerRepository;
import org.spring.finalproject.repository.OrderRowRepository;
import org.spring.finalproject.service.ApplianceService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class ApplianceServiceImpl implements ApplianceService {

    private final ApplianceRepository applianceRepository;
    private final ManufacturerRepository manufacturerRepository;
    private final OrderRowRepository orderRowRepository;
    private final ApplianceMapper applianceMapper;

    @Override
    @Transactional(readOnly = true)
    public List<ApplianceDto> findAll() {

        return applianceRepository.findAll()
                .stream()
                .map(applianceMapper::toDto)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ApplianceDto> findAll(
            String search,
            Pageable pageable) {

        Page<Appliance> page;

        if (search != null && !search.isBlank()) {
            page = applianceRepository
                    .findByNameContainingIgnoreCase(
                            search.trim(), pageable);
        } else {
            page = applianceRepository.findAll(pageable);
        }

        return page.map(applianceMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public ApplianceDto findById(Long id) {

        Appliance appliance = applianceRepository.findById(id)
                .orElseThrow(() ->
                        new EntityNotFoundException(
                                "Appliance not found: " + id));

        return applianceMapper.toDto(appliance);
    }

    @Override
    public ApplianceDto save(ApplianceDto dto) {

        Manufacturer manufacturer =
                manufacturerRepository.findById(
                                dto.getManufacturerId())
                        .orElseThrow(() ->
                                new EntityNotFoundException(
                                        "Manufacturer not found"));

        Appliance appliance =
                applianceMapper.toEntity(dto);

        appliance.setManufacturer(manufacturer);

        Appliance saved =
                applianceRepository.save(appliance);

        return applianceMapper.toDto(saved);
    }

    @Override
    public ApplianceDto update(Long id,
                               ApplianceDto dto) {

        Appliance appliance =
                applianceRepository.findById(id)
                        .orElseThrow(() ->
                                new EntityNotFoundException(
                                        "Appliance not found"));

        Manufacturer manufacturer =
                manufacturerRepository.findById(
                                dto.getManufacturerId())
                        .orElseThrow(() ->
                                new EntityNotFoundException(
                                        "Manufacturer not found"));

        appliance.setName(dto.getName());
        appliance.setModel(dto.getModel());
        appliance.setPrice(dto.getPrice());
        appliance.setQuantity(dto.getQuantity());
        appliance.setManufacturer(manufacturer);

        return applianceMapper.toDto(appliance);
    }

    @Override
    public void delete(Long id) {

        if (!applianceRepository.existsById(id)) {
            throw new EntityNotFoundException(
                    "Appliance not found: " + id);
        }

        if (orderRowRepository.existsByAppliance_Id(id)) {
            throw new EntityInUseException(
                    "error.delete.appliance.in.use");
        }

        applianceRepository.deleteById(id);
    }
}
