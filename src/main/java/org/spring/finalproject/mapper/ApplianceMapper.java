package org.spring.finalproject.mapper;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.spring.finalproject.dto.request.ApplianceDto;
import org.spring.finalproject.dto.request.ClientDto;
import org.spring.finalproject.entity.Appliance;
import org.spring.finalproject.entity.Client;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ApplianceMapper {
    private final ModelMapper modelMapper;

    public ApplianceDto toDto(Appliance appliance) {
        return modelMapper.map(appliance,ApplianceDto.class);
    }

    public Appliance toEntity(ApplianceDto dto) {
        return modelMapper.map(dto, Appliance.class);
    }
}
