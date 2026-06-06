package org.spring.finalproject.service;

import org.spring.finalproject.dto.request.ApplianceDto;

import java.util.List;

public interface ApplianceService {

    List<ApplianceDto> findAll();

    ApplianceDto findById(Long id);

    ApplianceDto save(ApplianceDto dto);

    ApplianceDto update(Long id,
                        ApplianceDto dto);

    void delete(Long id);
}
