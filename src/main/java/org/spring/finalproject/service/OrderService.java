package org.spring.finalproject.service;

import org.spring.finalproject.dto.OrderDto;

import java.util.List;

public interface OrderService {

    List<OrderDto> findAll();

    OrderDto findById(Long id);

    OrderDto create(OrderDto dto);

    OrderDto update(Long id,
                    OrderDto dto);

    void delete(Long id);

    void approve(Long id);
}
