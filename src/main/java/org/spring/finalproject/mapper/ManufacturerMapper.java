package org.spring.finalproject.mapper;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.spring.finalproject.dto.ManufacturerDto;
import org.spring.finalproject.entity.Manufacturer;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class ManufacturerMapper {
    private final ModelMapper modelMapper;

    public ManufacturerDto toDto(Manufacturer manufacturer) {
        return modelMapper.map(manufacturer, ManufacturerDto.class);
    }

    public Manufacturer toEntity(ManufacturerDto dto) {
        return modelMapper.map(dto, Manufacturer.class);
    }
}
