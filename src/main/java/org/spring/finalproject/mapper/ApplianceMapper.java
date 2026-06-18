package org.spring.finalproject.mapper;

import org.spring.finalproject.dto.ApplianceDto;
import org.spring.finalproject.entity.Appliance;
import org.springframework.stereotype.Component;

@Component
public class ApplianceMapper {

    public ApplianceDto toDto(Appliance appliance) {

        ApplianceDto dto = new ApplianceDto();
        dto.setId(appliance.getId());
        dto.setName(appliance.getName());
        dto.setModel(appliance.getModel());
        dto.setPrice(appliance.getPrice());
        dto.setQuantity(appliance.getQuantity());

        if (appliance.getManufacturer() != null) {
            dto.setManufacturerId(
                    appliance.getManufacturer().getId());
        }

        return dto;
    }

    public Appliance toEntity(ApplianceDto dto) {

        Appliance appliance = new Appliance();
        //appliance.setId(dto.getId());
        appliance.setName(dto.getName());
        appliance.setModel(dto.getModel());
        appliance.setPrice(dto.getPrice());
        appliance.setQuantity(dto.getQuantity());
        return appliance;
    }
}
