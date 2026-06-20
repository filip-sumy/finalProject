package org.spring.finalproject.mapper;

import lombok.RequiredArgsConstructor;
import org.spring.finalproject.dto.OrderDto;
import org.spring.finalproject.dto.OrderRowDto;
import org.spring.finalproject.entity.Order;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
@Component
@RequiredArgsConstructor
public class OrderMapper {

    private final ClientMapper clientMapper;

    public OrderDto toDto(Order order) {

        OrderDto dto = new OrderDto();

        dto.setId(order.getId());
        dto.setStatus(order.getStatus());

        if (order.getClient() != null) {
            dto.setClientId(order.getClient().getId());
            dto.setClient(clientMapper.toResponseDto(order.getClient()));
        }

        BigDecimal totalPrice = BigDecimal.ZERO;

        dto.setRows(order.getRows().stream()
                .map(row -> {
                    OrderRowDto rowDto = new OrderRowDto();
                    rowDto.setApplianceId(row.getAppliance().getId());
                    rowDto.setApplianceName(row.getAppliance().getName());
                    rowDto.setQuantity(row.getQuantity());
                    rowDto.setPrice(row.getPrice());
                    return rowDto;
                })
                .toList());

        for (OrderRowDto rowDto : dto.getRows()) {
            if (rowDto.getPrice() != null && rowDto.getQuantity() != null) {
                totalPrice = totalPrice.add(
                        rowDto.getPrice().multiply(BigDecimal.valueOf(rowDto.getQuantity())));
            }
        }

        dto.setTotalPrice(totalPrice);
        return dto;
    }
}