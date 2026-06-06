package org.spring.finalproject.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderRowDto {

    @NotNull(message = "{order.appliance.required}")
    private Long applianceId;

    @NotNull
    @Positive(message = "{order.quantity.positive}")
    private Integer quantity;
}
