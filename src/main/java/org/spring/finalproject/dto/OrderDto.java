package org.spring.finalproject.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.spring.finalproject.dto.response.ClientResponseDto;
import org.spring.finalproject.entity.OrderStatus;

import java.math.BigDecimal;
import java.util.List;

@Data
@AllArgsConstructor
public class OrderDto {

    private Long id;

    private ClientResponseDto client;

    private Long clientId;

    private OrderStatus status;

    private BigDecimal totalPrice;

    @Valid
    @NotEmpty(message = "{order.rows.required}")
    private List<OrderRowDto> rows;

    public OrderDto() {

        rows = List.of(
                new OrderRowDto()
        );
    }
}
