package org.spring.finalproject.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.spring.finalproject.dto.ClientDto;
import org.spring.finalproject.dto.OrderDto;
import org.spring.finalproject.security.SecurityUtils;
import org.spring.finalproject.service.ApplianceService;
import org.spring.finalproject.service.ClientService;
import org.spring.finalproject.service.OrderService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
@Controller
@RequestMapping("/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;
    private final ClientService clientService;
    private final ApplianceService applianceService;

    @GetMapping
    public String findAll(@RequestParam(required = false) String search,
                          @RequestParam(defaultValue = "0") int page,
                          @RequestParam(defaultValue = "10") int size,
                          @RequestParam(defaultValue = "id") String sort,
                          @RequestParam(defaultValue = "asc") String direction,
                          @AuthenticationPrincipal UserDetails user,
                          Model model) {

        Sort sortOrder = direction.equalsIgnoreCase("desc")
                ? Sort.by(sort).descending()
                : Sort.by(sort).ascending();

        boolean isClient = SecurityUtils.isClient(user);

        Page<OrderDto> result;

        if (isClient) {

            ClientDto client = clientService.findByEmail(user.getUsername());

            result = orderService.findByClient(
                    client.getId(),
                    search,
                    PageRequest.of(page, size, sortOrder)
            );

        } else {
            result = orderService.findAll(
                    search,
                    PageRequest.of(page, size, sortOrder)
            );
        }

        model.addAttribute("orders", result.getContent());
        model.addAttribute("page", result);
        model.addAttribute("search", search);
        model.addAttribute("sort", sort);
        model.addAttribute("direction", direction);
        model.addAttribute("size", size);
        model.addAttribute("clientView", isClient);

        return "order/list";
    }

    @GetMapping("/create")
    @PreAuthorize("hasAnyRole('ADMIN','EMPLOYEE')")
    public String createForm(Model model) {

        model.addAttribute("order", new OrderDto());
        model.addAttribute("appliances", applianceService.findAll());
        model.addAttribute("clients", clientService.findAll());
        model.addAttribute("editMode", false);
        return "order/form";
    }

    @PostMapping("/create")
    @PreAuthorize("hasAnyRole('ADMIN','EMPLOYEE')")
    public String create(@Valid @ModelAttribute("order") OrderDto orderDto,
                         BindingResult result,
                         Model model) {

        validateClientId(orderDto, result);

        if (result.hasErrors()) {
            model.addAttribute("appliances", applianceService.findAll());
            model.addAttribute("clients", clientService.findAll());
            model.addAttribute("editMode", false);
            return "order/form";
        }

        orderService.create(orderDto);
        return "redirect:/orders";
    }

    @GetMapping("/edit/{id}")
    @PreAuthorize("@orderSecurity.isOwner(#id, authentication) or hasAnyRole('ADMIN','EMPLOYEE')")
    public String editForm(@PathVariable Long id,
                           @AuthenticationPrincipal UserDetails user,
                           Model model) {

        boolean isClient = SecurityUtils.isClient(user);

        model.addAttribute("order", orderService.findById(id));
        model.addAttribute("appliances", applianceService.findAll());

        if (!isClient) {
            model.addAttribute("clients", clientService.findAll());
        }

        model.addAttribute("editMode", true);
        return "order/form";
    }

    @PostMapping("/edit/{id}")
    @PreAuthorize("@orderSecurity.isOwner(#id, authentication) or hasAnyRole('ADMIN','EMPLOYEE')")
    public String update(@PathVariable Long id,
                         @Valid @ModelAttribute("order") OrderDto orderDto,
                         BindingResult result,
                         @AuthenticationPrincipal UserDetails user,
                         Model model) {

        boolean isClient = SecurityUtils.isClient(user);

        if (isClient) {
            ClientDto client = clientService.findByEmail(user.getUsername());
            orderDto.setClientId(client.getId());
        } else {
            validateClientId(orderDto, result);
        }

        if (result.hasErrors()) {
            model.addAttribute("appliances", applianceService.findAll());

            if (!isClient) {
                model.addAttribute("clients", clientService.findAll());
            }

            model.addAttribute("editMode", true);
            return "order/form";
        }

        orderService.update(id, orderDto);
        return "redirect:/orders";
    }

    @PostMapping("/delete/{id}")
    @PreAuthorize("@orderSecurity.isOwner(#id, authentication) or hasAnyRole('ADMIN','EMPLOYEE')")
    public String delete(@PathVariable Long id) {

        orderService.delete(id);
        return "redirect:/orders";
    }

    @PostMapping("/approve/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','EMPLOYEE')")
    public String approve(@PathVariable Long id) {

        orderService.approve(id);
        return "redirect:/orders";
    }

    private void validateClientId(OrderDto orderDto, BindingResult result) {
        if (orderDto.getClientId() == null) {
            result.rejectValue("clientId", "order.client.required");
        }
    }
}