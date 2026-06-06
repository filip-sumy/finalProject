package org.spring.finalproject.dto.request;

import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderRowDto {

    private Long applianceId;

    @Positive
    private Integer quantity;
}
