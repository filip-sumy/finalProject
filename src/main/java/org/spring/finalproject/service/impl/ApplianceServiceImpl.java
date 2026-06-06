package org.spring.finalproject.service.impl;

import lombok.RequiredArgsConstructor;
import org.spring.finalproject.dto.request.ApplianceDto;
import org.spring.finalproject.entity.Appliance;
import org.spring.finalproject.entity.Manufacturer;
import org.spring.finalproject.exception.EntityNotFoundException;
import org.spring.finalproject.mapper.ApplianceMapper;
import org.spring.finalproject.repository.ApplianceRepository;
import org.spring.finalproject.repository.ManufacturerRepository;
import org.spring.finalproject.service.ApplianceService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class ApplianceServiceImpl implements ApplianceService {

    private final ApplianceRepository applianceRepository;
    private final ManufacturerRepository manufacturerRepository;
    private final ApplianceMapper applianceMapper;

    @Override
    public List<ApplianceDto> findAll() {

        return applianceRepository.findAll()
                .stream()
                .map(applianceMapper::toDto)
                .toList();
    }

    @Override
    public ApplianceDto findById(Long id) {
        return null;
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

        appliance.setName(dto.getName());
        appliance.setModel(dto.getModel());
        appliance.setPrice(dto.getPrice());
        appliance.setQuantity(dto.getQuantity());

        return applianceMapper.toDto(appliance);
    }

    @Override
    public void delete(Long id) {

        applianceRepository.deleteById(id);
    }
}
