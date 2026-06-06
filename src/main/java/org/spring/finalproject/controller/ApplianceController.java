package org.spring.finalproject.controller;


import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.spring.finalproject.dto.request.ApplianceDto;
import org.spring.finalproject.service.ApplianceService;
import org.spring.finalproject.service.ManufacturerService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/appliances")
@RequiredArgsConstructor
public class ApplianceController {

    private final ApplianceService applianceService;
    private final ManufacturerService manufacturerService;

    @GetMapping
    public String findAll(Model model) {

        model.addAttribute(
                "appliances",
                applianceService.findAll());

        return "appliance/list";
    }

    @GetMapping("/create")
    public String createForm(Model model) {

        model.addAttribute(
                "appliance",
                new ApplianceDto());

        model.addAttribute(
                "manufacturers",
                manufacturerService.findAll());

        model.addAttribute("editMode", false);

        return "appliance/form";
    }

    @PostMapping("/create")
    public String create(
            @Valid @ModelAttribute("appliance")
            ApplianceDto dto,
            BindingResult bindingResult,
            Model model) {

        if (bindingResult.hasErrors()) {

            model.addAttribute(
                    "manufacturers",
                    manufacturerService.findAll());

            return "appliance/form";
        }

        applianceService.save(dto);

        return "redirect:/appliances";
    }

    @GetMapping("/edit/{id}")
    public String editForm(
            @PathVariable Long id,
            Model model) {

        model.addAttribute(
                "appliance",
                applianceService.findById(id));

        model.addAttribute(
                "manufacturers",
                manufacturerService.findAll());

        model.addAttribute("editMode", true);

        return "appliance/form";
    }

    @PostMapping("/edit/{id}")
    public String update(
            @PathVariable Long id,
            @Valid @ModelAttribute("appliance")
            ApplianceDto dto,
            BindingResult bindingResult,
            Model model) {

        if (bindingResult.hasErrors()) {

            model.addAttribute(
                    "manufacturers",
                    manufacturerService.findAll());

            return "appliance/form";
        }

        applianceService.update(id, dto);

        return "redirect:/appliances";
    }

    @GetMapping("/delete/{id}")
    public String delete(
            @PathVariable Long id) {

        applianceService.delete(id);

        return "redirect:/appliances";
    }
}
