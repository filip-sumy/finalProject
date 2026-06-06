package org.spring.finalproject.service;

import org.spring.finalproject.dto.ApplianceDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ApplianceService {

    List<ApplianceDto> findAll();

    Page<ApplianceDto> findAll(
            String search,
            Pageable pageable);

    ApplianceDto findById(Long id);

    ApplianceDto save(ApplianceDto dto);

    ApplianceDto update(Long id,
                        ApplianceDto dto);

    void delete(Long id);
}
