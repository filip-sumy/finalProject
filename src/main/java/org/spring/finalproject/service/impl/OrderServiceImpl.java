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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    public Page<OrderDto> findByClient(
            Long clientId,
            String search,
            Pageable pageable) {

        Page<Order> page;

        if (search != null && !search.isBlank()) {
            page = orderRepository.searchByClientId(
                    clientId,
                    search.trim(),
                    pageable
            );
        } else {
            page = orderRepository.findByClientId(clientId, pageable);
        }

        return page.map(orderMapper::toDto);
    }

    @Transactional(readOnly = true)
    @Override
    public Page<OrderDto> findAll(
            String search,
            Pageable pageable) {

        Page<Order> page;

        if (search != null && !search.isBlank()) {
            page = orderRepository.search(
                    search.trim(),
                    pageable
            );
        } else {
            page = orderRepository.findAll(pageable);
        }

        return page.map(orderMapper::toDto);
    }
    @Override
    @Transactional(readOnly = true)
    public OrderDto findById(Long id) {

        Order order = orderRepository.findById(id)
                .orElseThrow(() ->
                        new EntityNotFoundException(
                                "error.not.found.order", id));

        return orderMapper.toDto(order);
    }

    @Override
    public OrderDto create(OrderDto dto) {

        Client client = clientRepository.findById(dto.getClientId())
                .orElseThrow(() ->
                        new EntityNotFoundException(
                                "error.not.found.client", dto.getClientId()));

        Order order = new Order();

        order.setClient(client);
        order.setCreatedAt(LocalDateTime.now());
        order.setStatus(OrderStatus.CREATED);
        order.setRows(buildRows(order, dto.getRows()));

        Order saved = orderRepository.save(order);

        log.info("Order created. Id={}, Client={}",
                saved.getId(),
                client.getEmail());

        return orderMapper.toDto(saved);
    }

    @Override
    public OrderDto update(Long id, OrderDto dto) {

        Order order = orderRepository.findById(id)
                .orElseThrow(() ->
                        new EntityNotFoundException("error.not.found.order", id));

        if (order.getStatus() == OrderStatus.APPROVED) {
            throw new OrderAlreadyApprovedException("error.order.cannot.modify.approved");
        }

        Client client = clientRepository.findById(dto.getClientId())
                .orElseThrow(() ->
                        new EntityNotFoundException("error.not.found.client.generic"));

        order.setClient(client);
        order.getRows().clear();
        order.getRows().addAll(buildRows(order, dto.getRows()));

        Order saved = orderRepository.save(order);

        log.info("Order updated: {}", id);

        return orderMapper.toDto(saved);
    }

    @Override
    public void delete(Long id) {

        Order order = orderRepository.findById(id)
                .orElseThrow(() ->
                        new EntityNotFoundException("error.not.found.order.generic"));

        if (order.getStatus() == OrderStatus.APPROVED) {

            throw new OrderAlreadyApprovedException("error.order.cannot.delete.approved");
        }

        orderRepository.delete(order);

        log.warn("Order deleted: {}", id);
    }

    @Override
    public void approve(Long id) {

        Order order = orderRepository.findById(id)
                .orElseThrow(() ->
                        new EntityNotFoundException("error.not.found.order.generic"));

        if (order.getStatus() == OrderStatus.APPROVED) {
            throw new OrderAlreadyApprovedException("error.order.already.approved");
        }

        for (OrderRow row : order.getRows()) {

            Appliance appliance = row.getAppliance();

            if (appliance.getQuantity() < row.getQuantity()) {
                throw new InsufficientStockException(
                        "error.stock.insufficient", appliance.getName());
            }

            appliance.setQuantity(
                    appliance.getQuantity() - row.getQuantity()
            );

            applianceRepository.save(appliance);
        }

        order.setStatus(OrderStatus.APPROVED);

        orderRepository.save(order);

        log.info("Order approved: {}", id);
    }

    private List<OrderRow> buildRows(Order order, List<OrderRowDto> rowDtos) {
        List<OrderRow> rows = new ArrayList<>();

        for (OrderRowDto rowDto : rowDtos) {
            Appliance appliance = applianceRepository.findById(rowDto.getApplianceId())
                    .orElseThrow(() ->
                            new EntityNotFoundException(
                                    "error.not.found.appliance", rowDto.getApplianceId()));

            validateStock(appliance, rowDto.getQuantity());

            OrderRow row = new OrderRow();
            row.setOrder(order);
            row.setAppliance(appliance);
            row.setQuantity(rowDto.getQuantity());
            row.setPrice(appliance.getPrice());
            rows.add(row);
        }

        return rows;
    }

    private void validateStock(Appliance appliance, int quantity) {
        if (appliance.getQuantity() == null || appliance.getQuantity() < quantity) {
            throw new InsufficientStockException(
                    "error.stock.insufficient", appliance.getName());
        }
    }
}
