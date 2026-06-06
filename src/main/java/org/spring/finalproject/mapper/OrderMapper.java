package org.spring.finalproject.mapper;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.spring.finalproject.dto.request.ManufacturerDto;
import org.spring.finalproject.dto.request.OrderDto;
import org.spring.finalproject.entity.Manufacturer;
import org.spring.finalproject.entity.Order;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OrderMapper {
    private final ModelMapper modelMapper;

    public OrderDto toDto(Order order) {
        return modelMapper.map(order, OrderDto.class);
    }

    public Order toEntity(OrderDto dto) {
        return modelMapper.map(dto, Order.class);
    }
}
