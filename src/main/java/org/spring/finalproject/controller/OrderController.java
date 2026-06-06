package org.spring.finalproject.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.spring.finalproject.dto.request.OrderDto;
import org.spring.finalproject.service.ApplianceService;
import org.spring.finalproject.service.ClientService;
import org.spring.finalproject.service.OrderService;
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
    public String findAll(Model model) {

        model.addAttribute(
                "orders",
                orderService.findAll());

        return "order/list";
    }

    @GetMapping("/create")
    public String createForm(Model model) {

        model.addAttribute(
                "order",
                new OrderDto());

        model.addAttribute(
                "clients",
                clientService.findAll());

        model.addAttribute(
                "appliances",
                applianceService.findAll());

        return "order/form";
    }

    @PostMapping("/create")
    public String create(
            @Valid
            @ModelAttribute("order")
            OrderDto orderDto,
            BindingResult result,
            Model model) {

        if (result.hasErrors()) {

            model.addAttribute(
                    "clients",
                    clientService.findAll());

            model.addAttribute(
                    "appliances",
                    applianceService.findAll());

            return "order/form";
        }

        orderService.create(orderDto);

        return "redirect:/orders";
    }

    @GetMapping("/delete/{id}")
    public String delete(
            @PathVariable Long id) {

        orderService.delete(id);

        return "redirect:/orders";
    }

    @PostMapping("/approve/{id}")
    public String approve(
            @PathVariable Long id) {

        orderService.approve(id);

        return "redirect:/orders";
    }
}