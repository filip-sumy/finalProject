package org.spring.finalproject.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.spring.finalproject.entity.OrderStatus;
import org.spring.finalproject.dto.OrderDto;
import org.spring.finalproject.dto.OrderRowDto;
import org.spring.finalproject.entity.Appliance;
import org.spring.finalproject.entity.Client;
import org.spring.finalproject.entity.Order;
import org.spring.finalproject.entity.OrderRow;
import org.spring.finalproject.exception.EntityNotFoundException;
import org.spring.finalproject.exception.InsufficientStockException;
import org.spring.finalproject.exception.OrderAlreadyApprovedException;
import org.spring.finalproject.mapper.OrderMapper;
import org.spring.finalproject.repository.ApplianceRepository;
import org.spring.finalproject.repository.ClientRepository;
import org.spring.finalproject.repository.OrderRepository;
import org.spring.finalproject.service.OrderService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final ClientRepository clientRepository;
    private final ApplianceRepository applianceRepository;
    private final OrderMapper orderMapper;

    @Override
    @Transactional(readOnly = true)
    public List<OrderDto> findAll() {

        log.debug("Fetching all orders");

        return orderRepository.findAll()
                .stream()
                .map(orderMapper::toDto)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public OrderDto findById(Long id) {

        Order order = orderRepository.findById(id)
                .orElseThrow(() ->
                        new EntityNotFoundException(
                                "Order not found: " + id));

        return orderMapper.toDto(order);
    }

    @Override
    public OrderDto create(OrderDto dto) {

        Client client = clientRepository.findById(dto.getClientId())
                .orElseThrow(() ->
                        new EntityNotFoundException(
                                "Client not found: "
                                        + dto.getClientId()));

        Order order = new Order();

        order.setClient(client);
        order.setCreatedAt(LocalDateTime.now());
        order.setStatus(OrderStatus.CREATED);

        List<OrderRow> rows = new ArrayList<>();

        BigDecimal totalPrice = BigDecimal.ZERO;

        for (OrderRowDto rowDto : dto.getRows()) {

            Appliance appliance =
                    applianceRepository.findById(
                                    rowDto.getApplianceId())
                            .orElseThrow(() ->
                                    new EntityNotFoundException(
                                            "Appliance not found: "
                                                    + rowDto.getApplianceId()));

            OrderRow row = new OrderRow();

            row.setOrder(order);
            row.setAppliance(appliance);

            row.setQuantity(rowDto.getQuantity());

            row.setPrice(appliance.getPrice());

            rows.add(row);

            totalPrice = totalPrice.add(
                    appliance.getPrice()
                            .multiply(
                                    BigDecimal.valueOf(
                                            rowDto.getQuantity()
                                    )
                            )
            );
        }

        order.setRows(rows);


        Order saved = orderRepository.save(order);

        log.info("Order created. Id={}, Client={}",
                saved.getId(),
                client.getEmail());

        return orderMapper.toDto(saved);
    }

    @Override
    public OrderDto update(Long id,
                           OrderDto dto) {

        Order order = orderRepository.findById(id)
                .orElseThrow(() ->
                        new EntityNotFoundException(
                                "Order not found: " + id));

        if (order.getStatus() == OrderStatus.APPROVED) {

            throw new OrderAlreadyApprovedException(
                    "Approved order cannot be modified");
        }

        Client client = clientRepository.findById(dto.getClientId())
                .orElseThrow(() ->
                        new EntityNotFoundException(
                                "Client not found: "
                                        + dto.getClientId()));

        order.setClient(client);
        order.getRows().clear();

        BigDecimal totalPrice = BigDecimal.ZERO;

        for (OrderRowDto rowDto : dto.getRows()) {

            Appliance appliance =
                    applianceRepository.findById(
                                    rowDto.getApplianceId())
                            .orElseThrow(() ->
                                    new EntityNotFoundException(
                                            "Appliance not found"));

            OrderRow row = new OrderRow();

            row.setOrder(order);
            row.setAppliance(appliance);

            row.setQuantity(rowDto.getQuantity());

            row.setPrice(appliance.getPrice());

            order.getRows().add(row);

            totalPrice = totalPrice.add(
                    appliance.getPrice()
                            .multiply(
                                    BigDecimal.valueOf(
                                            rowDto.getQuantity()
                                    )
                            )
            );
        }

        log.info("Order updated: {}", id);

        return orderMapper.toDto(order);
    }

    @Override
    public void delete(Long id) {

        Order order = orderRepository.findById(id)
                .orElseThrow(() ->
                        new EntityNotFoundException(
                                "Order not found"));

        if (order.getStatus() == OrderStatus.APPROVED) {

            throw new OrderAlreadyApprovedException(
                    "Approved order cannot be deleted");
        }

        orderRepository.delete(order);

        log.warn("Order deleted: {}", id);
    }

    @Override
    public void approve(Long id) {

        Order order = orderRepository.findById(id)
                .orElseThrow(() ->
                        new EntityNotFoundException(
                                "Order not found"));

        if (order.getStatus() == OrderStatus.APPROVED) {

            throw new OrderAlreadyApprovedException(
                    "Order already approved");
        }

        for (OrderRow row : order.getRows()) {

            Appliance appliance = row.getAppliance();

            if (appliance.getQuantity()
                    < row.getQuantity()) {

                throw new InsufficientStockException(
                        "Not enough stock for appliance: "
                                + appliance.getName());
            }

            appliance.setQuantity(
                    appliance.getQuantity()
                            - row.getQuantity());
        }

        order.setStatus(OrderStatus.APPROVED);

        log.info("Order approved: {}", id);
    }
}
