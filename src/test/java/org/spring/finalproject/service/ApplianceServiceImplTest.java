package org.spring.finalproject.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.spring.finalproject.dto.ApplianceDto;
import org.spring.finalproject.entity.Appliance;
import org.spring.finalproject.entity.Manufacturer;
import org.spring.finalproject.exception.EntityInUseException;
import org.spring.finalproject.exception.EntityNotFoundException;
import org.spring.finalproject.mapper.ApplianceMapper;
import org.spring.finalproject.repository.ApplianceRepository;
import org.spring.finalproject.repository.ManufacturerRepository;
import org.spring.finalproject.repository.OrderRowRepository;
import org.spring.finalproject.service.impl.ApplianceServiceImpl;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ApplianceServiceImplTest {

    @Mock
    private ApplianceRepository applianceRepository;

    @Mock
    private ManufacturerRepository manufacturerRepository;

    @Mock
    private OrderRowRepository orderRowRepository;

    @Mock
    private ApplianceMapper applianceMapper;

    @InjectMocks
    private ApplianceServiceImpl applianceService;

    @Test
    void save_createsApplianceWithManufacturer() {
        ApplianceDto dto = new ApplianceDto();
        dto.setName("TV");
        dto.setManufacturerId(1L);

        Manufacturer manufacturer = new Manufacturer();
        manufacturer.setId(1L);

        Appliance entity = new Appliance();
        Appliance saved = new Appliance();
        saved.setId(10L);
        saved.setName("TV");

        when(manufacturerRepository.findById(1L)).thenReturn(Optional.of(manufacturer));
        when(applianceMapper.toEntity(dto)).thenReturn(entity);
        when(applianceRepository.save(entity)).thenReturn(saved);
        when(applianceMapper.toDto(saved)).thenReturn(dto);

        ApplianceDto result = applianceService.save(dto);

        assertEquals("TV", result.getName());
        verify(applianceRepository).save(entity);
    }

    @Test
    void findById_throwsWhenNotFound() {
        when(applianceRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class,
                () -> applianceService.findById(99L));
    }

    @Test
    void save_throwsWhenManufacturerMissing() {
        ApplianceDto dto = new ApplianceDto();
        dto.setManufacturerId(5L);

        when(manufacturerRepository.findById(5L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class,
                () -> applianceService.save(dto));
    }

    @Test
    void delete_throwsWhenUsedInOrders() {
        when(applianceRepository.existsById(1L)).thenReturn(true);
        when(orderRowRepository.existsByAppliance_Id(1L)).thenReturn(true);

        assertThrows(EntityInUseException.class,
                () -> applianceService.delete(1L));
    }

    @Test
    void delete_succeedsWhenNotReferenced() {
        when(applianceRepository.existsById(2L)).thenReturn(true);
        when(orderRowRepository.existsByAppliance_Id(2L)).thenReturn(false);

        applianceService.delete(2L);

        verify(applianceRepository).deleteById(2L);
    }

    @Test
    void update_updatesFields() {
        ApplianceDto dto = new ApplianceDto();
        dto.setName("Updated");
        dto.setModel("X1");
        dto.setPrice(BigDecimal.TEN);
        dto.setQuantity(5);
        dto.setManufacturerId(1L);

        Appliance appliance = new Appliance();
        Manufacturer manufacturer = new Manufacturer();
        manufacturer.setId(1L);

        when(applianceRepository.findById(3L)).thenReturn(Optional.of(appliance));
        when(manufacturerRepository.findById(1L)).thenReturn(Optional.of(manufacturer));
        when(applianceMapper.toDto(appliance)).thenReturn(dto);

        ApplianceDto result = applianceService.update(3L, dto);

        assertEquals("Updated", result.getName());
        assertEquals("X1", appliance.getModel());
        verify(applianceMapper).toDto(appliance);
    }
}
