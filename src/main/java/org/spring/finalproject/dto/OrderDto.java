package org.spring.finalproject.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.spring.finalproject.entity.OrderStatus;

import java.util.List;

@Data
@AllArgsConstructor
public class OrderDto {

    private Long id;

    @NotNull(message = "{order.client.required}")
    private Long clientId;

    private OrderStatus status;

    @Valid
    @NotEmpty(message = "{order.rows.required}")
    private List<OrderRowDto> rows;

    public OrderDto() {

        rows = List.of(
                new OrderRowDto()
        );
    }
}
