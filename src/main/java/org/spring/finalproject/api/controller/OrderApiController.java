package org.spring.finalproject.api.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.spring.finalproject.dto.ClientDto;
import org.spring.finalproject.dto.OrderDto;
import org.spring.finalproject.security.SecurityUtils;
import org.spring.finalproject.service.ClientService;
import org.spring.finalproject.service.OrderService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/orders")
@RequiredArgsConstructor
public class OrderApiController {

    private final OrderService orderService;
    private final ClientService clientService;

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN','EMPLOYEE','CLIENT')")
    public Page<OrderDto> findAll(
            @RequestParam(required = false) String search,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sort,
            @RequestParam(defaultValue = "asc") String direction,
            @AuthenticationPrincipal UserDetails user) {

        Sort sortOrder = direction.equalsIgnoreCase("desc")
                ? Sort.by(sort).descending()
                : Sort.by(sort).ascending();

        PageRequest pageRequest = PageRequest.of(page, size, sortOrder);

        if (SecurityUtils.isClient(user)) {
            ClientDto client = clientService.findByEmail(user.getUsername());
            return orderService.findByClient(client.getId(), search, pageRequest);
        }

        return orderService.findAll(search, pageRequest);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','EMPLOYEE','CLIENT')")
    public OrderDto findById(@PathVariable Long id) {
        return orderService.findById(id);
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN','EMPLOYEE','CLIENT')")
    public OrderDto create(@Valid @RequestBody OrderDto orderDto,
                           @AuthenticationPrincipal UserDetails user) {

        if (SecurityUtils.isClient(user)) {
            ClientDto client = clientService.findByEmail(user.getUsername());
            orderDto.setClientId(client.getId());
        }

        return orderService.create(orderDto);
    }

    @PostMapping("/{id}/approve")
    @PreAuthorize("hasAnyRole('ADMIN','EMPLOYEE')")
    public void approve(@PathVariable Long id) {
        orderService.approve(id);
    }
}
