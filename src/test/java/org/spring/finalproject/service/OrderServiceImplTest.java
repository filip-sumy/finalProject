package org.spring.finalproject.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.spring.finalproject.OrderStatus;
import org.spring.finalproject.dto.OrderDto;
import org.spring.finalproject.dto.OrderRowDto;
import org.spring.finalproject.entity.Appliance;
import org.spring.finalproject.entity.Client;
import org.spring.finalproject.entity.Order;
import org.spring.finalproject.entity.OrderRow;
import org.spring.finalproject.exception.InsufficientStockException;
import org.spring.finalproject.exception.OrderAlreadyApprovedException;
import org.spring.finalproject.mapper.OrderMapper;
import org.spring.finalproject.repository.ApplianceRepository;
import org.spring.finalproject.repository.ClientRepository;
import org.spring.finalproject.repository.OrderRepository;
import org.spring.finalproject.service.impl.OrderServiceImpl;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OrderServiceImplTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private ClientRepository clientRepository;

    @Mock
    private ApplianceRepository applianceRepository;

    @Mock
    private OrderMapper orderMapper;

    @InjectMocks
    private OrderServiceImpl orderService;

    @Test
    void approve_reducesStock_whenEnoughQuantity() {

        Appliance appliance = new Appliance();
        appliance.setId(1L);
        appliance.setName("Fridge");
        appliance.setQuantity(10);

        OrderRow row = new OrderRow();
        row.setAppliance(appliance);
        row.setQuantity(3);

        Order order = new Order();
        order.setId(1L);
        order.setStatus(OrderStatus.CREATED);
        order.setRows(List.of(row));

        when(orderRepository.findById(1L))
                .thenReturn(Optional.of(order));

        orderService.approve(1L);

        assertEquals(7, appliance.getQuantity());
        assertEquals(OrderStatus.APPROVED, order.getStatus());
    }

    @Test
    void approve_throws_whenInsufficientStock() {

        Appliance appliance = new Appliance();
        appliance.setName("Oven");
        appliance.setQuantity(1);

        OrderRow row = new OrderRow();
        row.setAppliance(appliance);
        row.setQuantity(5);

        Order order = new Order();
        order.setId(2L);
        order.setStatus(OrderStatus.CREATED);
        order.setRows(List.of(row));

        when(orderRepository.findById(2L))
                .thenReturn(Optional.of(order));

        assertThrows(
                InsufficientStockException.class,
                () -> orderService.approve(2L));
    }

    @Test
    void delete_throws_whenOrderApproved() {

        Order order = new Order();
        order.setStatus(OrderStatus.APPROVED);

        when(orderRepository.findById(3L))
                .thenReturn(Optional.of(order));

        assertThrows(
                OrderAlreadyApprovedException.class,
                () -> orderService.delete(3L));
    }

    @Test
    void create_savesOrderWithRows() {

        Client client = new Client();
        client.setId(1L);
        client.setEmail("john@test.com");

        Appliance appliance = new Appliance();
        appliance.setId(2L);
        appliance.setPrice(BigDecimal.valueOf(100));

        OrderDto dto = new OrderDto();
        dto.setClientId(1L);
        dto.setRows(List.of(new OrderRowDto(2L, 2)));

        Order saved = new Order();
        saved.setId(10L);

        when(clientRepository.findById(1L))
                .thenReturn(Optional.of(client));
        when(applianceRepository.findById(2L))
                .thenReturn(Optional.of(appliance));
        when(orderRepository.save(org.mockito.ArgumentMatchers.any()))
                .thenReturn(saved);
        when(orderMapper.toDto(saved)).thenReturn(dto);

        OrderDto result = orderService.create(dto);

        assertEquals(1L, result.getClientId());
    }
}
