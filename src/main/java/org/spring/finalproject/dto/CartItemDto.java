package org.spring.finalproject.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
public class CartItemDto implements Serializable {

    private Long applianceId;
    private String applianceName;
    private BigDecimal price;
    private Integer quantity;
    private Integer stockAvailable;

    public CartItemDto(Long applianceId,
                       String applianceName,
                       BigDecimal price,
                       Integer quantity,
                       Integer stockAvailable) {
        this.applianceId = applianceId;
        this.applianceName = applianceName;
        this.price = price;
        this.quantity = quantity;
        this.stockAvailable = stockAvailable;
    }

    public BigDecimal getLineTotal() {
        if (price == null || quantity == null) {
            return BigDecimal.ZERO;
        }
        return price.multiply(BigDecimal.valueOf(quantity));
    }
}
