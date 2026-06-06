package org.spring.finalproject.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.spring.finalproject.exception.EntityInUseException;
import org.spring.finalproject.mapper.ManufacturerMapper;
import org.spring.finalproject.repository.ApplianceRepository;
import org.spring.finalproject.repository.ManufacturerRepository;
import org.spring.finalproject.service.impl.ManufacturerServiceImpl;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ManufacturerServiceImplTest {

    @Mock
    private ManufacturerRepository repository;

    @Mock
    private ApplianceRepository applianceRepository;

    @Mock
    private ManufacturerMapper mapper;

    @InjectMocks
    private ManufacturerServiceImpl manufacturerService;

    @Test
    void delete_throws_whenManufacturerHasAppliances() {

        when(repository.existsById(1L)).thenReturn(true);
        when(applianceRepository.countByManufacturer_Id(1L)).thenReturn(2L);

        assertThrows(
                EntityInUseException.class,
                () -> manufacturerService.delete(1L));
    }

    @Test
    void delete_succeeds_whenNoAppliances() {

        when(repository.existsById(3L)).thenReturn(true);
        when(applianceRepository.countByManufacturer_Id(3L)).thenReturn(0L);

        manufacturerService.delete(3L);

        verify(repository).deleteById(3L);
    }
}
