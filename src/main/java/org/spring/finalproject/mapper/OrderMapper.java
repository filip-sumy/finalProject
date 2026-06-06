package org.spring.finalproject.mapper;

import org.spring.finalproject.dto.OrderDto;
import org.spring.finalproject.dto.OrderRowDto;
import org.spring.finalproject.entity.Order;
import org.springframework.stereotype.Component;

@Component
public class OrderMapper {

    public OrderDto toDto(Order order) {

        OrderDto dto = new OrderDto();
        dto.setId(order.getId());
        dto.setStatus(order.getStatus());

        if (order.getClient() != null) {
            dto.setClientId(order.getClient().getId());
        }

        dto.setRows(order.getRows().stream()
                .map(row -> {
                    OrderRowDto rowDto = new OrderRowDto();
                    rowDto.setApplianceId(
                            row.getAppliance().getId());
                    rowDto.setQuantity(row.getQuantity());
                    return rowDto;
                })
                .toList());

        return dto;
    }
}
