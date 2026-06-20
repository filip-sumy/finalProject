package org.spring.finalproject.service;

import org.spring.finalproject.dto.OrderDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface OrderService {

    Page<OrderDto> findByClient(
            Long clientId,
            String search,
            Pageable pageable);

    Page<OrderDto> findAll(
            String search,
            Pageable pageable);

    OrderDto findById(Long id);

    OrderDto create(OrderDto dto);

    OrderDto update(Long id, OrderDto dto);

    void delete(Long id);

    void approve(Long id);
}
