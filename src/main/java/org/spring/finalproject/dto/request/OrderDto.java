package org.spring.finalproject.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.spring.finalproject.OrderStatus;

import java.util.List;

@Data
@AllArgsConstructor
public class OrderDto {

    private Long id;

    private Long clientId;

    private OrderStatus status;

    private List<OrderRowDto> rows;

    public OrderDto() {

        rows = List.of(
                new OrderRowDto()
        );
    }
}
