package org.spring.finalproject.service.cart;

import org.spring.finalproject.dto.ApplianceDto;
import org.spring.finalproject.dto.CartItemDto;
import org.spring.finalproject.dto.OrderDto;
import org.spring.finalproject.dto.OrderRowDto;
import org.spring.finalproject.exception.InsufficientStockException;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
@Scope(value = WebApplicationContext.SCOPE_SESSION, proxyMode = ScopedProxyMode.TARGET_CLASS)
public class Cart implements Serializable {

    private final List<CartItemDto> items = new ArrayList<>();

    public List<CartItemDto> getItems() {
        return List.copyOf(items);
    }

    public int getItemCount() {
        return items.stream()
                .mapToInt(CartItemDto::getQuantity)
                .sum();
    }

    public BigDecimal getTotalPrice() {
        return items.stream()
                .map(CartItemDto::getLineTotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public boolean isEmpty() {
        return items.isEmpty();
    }

    public void addItem(ApplianceDto appliance, int quantity) {
        validateQuantity(appliance, quantity);

        Optional<CartItemDto> existing = items.stream()
                .filter(item -> item.getApplianceId().equals(appliance.getId()))
                .findFirst();

        if (existing.isPresent()) {
            CartItemDto item = existing.get();
            int newQty = item.getQuantity() + quantity;
            validateQuantity(appliance, newQty);
            item.setQuantity(newQty);
        } else {
            items.add(new CartItemDto(
                    appliance.getId(),
                    appliance.getName(),
                    appliance.getPrice(),
                    quantity,
                    appliance.getQuantity()
            ));
        }
    }

    public void updateQuantity(Long applianceId, int quantity, ApplianceDto appliance) {
        CartItemDto item = findItem(applianceId);
        validateQuantity(appliance, quantity);
        item.setQuantity(quantity);
        item.setStockAvailable(appliance.getQuantity());
        item.setPrice(appliance.getPrice());
    }

    public void removeItem(Long applianceId) {
        items.removeIf(item -> item.getApplianceId().equals(applianceId));
    }

    public void clear() {
        items.clear();
    }

    public OrderDto toOrderDto() {
        OrderDto orderDto = new OrderDto();
        orderDto.setRows(items.stream()
                .map(item -> new OrderRowDto(item.getApplianceId(), item.getQuantity()))
                .toList());
        return orderDto;
    }

    private CartItemDto findItem(Long applianceId) {
        return items.stream()
                .filter(item -> item.getApplianceId().equals(applianceId))
                .findFirst()
                .orElseThrow(() ->
                        new IllegalArgumentException("Cart item not found: " + applianceId));
    }

    private void validateQuantity(ApplianceDto appliance, int quantity) {
        if (quantity <= 0) {
            throw new IllegalArgumentException("Quantity must be positive");
        }
        if (appliance.getQuantity() == null || appliance.getQuantity() < quantity) {
            throw new InsufficientStockException(
                    "Not enough stock for appliance: " + appliance.getName());
        }
    }
}
