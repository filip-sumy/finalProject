package org.spring.finalproject.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
public class OrderRowDto {

    @NotNull(message = "{order.appliance.required}")
    private Long applianceId;

    @NotNull
    @Positive(message = "{order.quantity.positive}")
    private Integer quantity;

    private String applianceName;

    private BigDecimal price;

    public OrderRowDto(Long applianceId, Integer quantity) {
        this.applianceId = applianceId;
        this.quantity = quantity;
    }
}
