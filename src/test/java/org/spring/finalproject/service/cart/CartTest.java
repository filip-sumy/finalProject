package org.spring.finalproject.service.cart;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.spring.finalproject.dto.ApplianceDto;
import org.spring.finalproject.dto.OrderDto;
import org.spring.finalproject.exception.InsufficientStockException;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CartTest {

    private Cart cart;

    @BeforeEach
    void setUp() {
        cart = new Cart();
    }

    @Test
    void addItem_increasesCountAndTotal() {
        cart.addItem(appliance(1L, "TV", 100, 10), 2);

        assertEquals(2, cart.getItemCount());
        assertEquals(0, new BigDecimal("200").compareTo(cart.getTotalPrice()));
    }

    @Test
    void addItem_mergesSameAppliance() {
        cart.addItem(appliance(1L, "TV", 100, 10), 1);
        cart.addItem(appliance(1L, "TV", 100, 10), 2);

        assertEquals(1, cart.getItems().size());
        assertEquals(3, cart.getItemCount());
    }

    @Test
    void addItem_throwsWhenInsufficientStock() {
        assertThrows(InsufficientStockException.class,
                () -> cart.addItem(appliance(1L, "TV", 100, 1), 5));
    }

    @Test
    void toOrderDto_mapsRows() {
        cart.addItem(appliance(2L, "Fridge", 500, 3), 1);

        OrderDto orderDto = cart.toOrderDto();

        assertEquals(1, orderDto.getRows().size());
        assertEquals(2L, orderDto.getRows().get(0).getApplianceId());
        assertEquals(1, orderDto.getRows().get(0).getQuantity());
    }

    @Test
    void clear_emptiesCart() {
        cart.addItem(appliance(1L, "TV", 100, 10), 1);
        cart.clear();
        assertTrue(cart.isEmpty());
    }

    private static ApplianceDto appliance(Long id, String name, int price, int stock) {
        ApplianceDto dto = new ApplianceDto();
        dto.setId(id);
        dto.setName(name);
        dto.setPrice(BigDecimal.valueOf(price));
        dto.setQuantity(stock);
        return dto;
    }
}
