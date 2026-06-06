package org.spring.finalproject.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ApplianceDto {

    private Long id;

    @NotBlank
    private String name;

    private String model;

    @Positive
    private BigDecimal price;

    @PositiveOrZero
    private Integer quantity;

    private Long manufacturerId;
}
