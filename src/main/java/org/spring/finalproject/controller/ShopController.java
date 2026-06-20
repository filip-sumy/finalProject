package org.spring.finalproject.controller;

import lombok.RequiredArgsConstructor;
import org.spring.finalproject.service.ApplianceService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/shop")
@RequiredArgsConstructor
@PreAuthorize("hasRole('CLIENT')")
public class ShopController {

    private final ApplianceService applianceService;

    @GetMapping
    public String catalog(Model model) {
        model.addAttribute("appliances", applianceService.findAll());
        return "shop/catalog";
    }
}
