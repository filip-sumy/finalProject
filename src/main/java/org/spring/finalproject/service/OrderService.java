package org.spring.finalproject.service;

import org.spring.finalproject.dto.ClientDto;
import org.spring.finalproject.dto.OrderDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface OrderService {

    List<OrderDto> findAll();

    Page<OrderDto> findAll(
            String search,
            Pageable pageable);

    OrderDto findById(Long id);

    OrderDto create(OrderDto dto);

    OrderDto update(Long id,
                    OrderDto dto);

    void delete(Long id);

    void approve(Long id);
}
