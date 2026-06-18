package org.spring.finalproject.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.spring.finalproject.dto.ApplianceDto;
import org.spring.finalproject.service.ApplianceService;
import org.spring.finalproject.service.ManufacturerService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
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
    public String findAll(
            @RequestParam(required = false) String search,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sort,
            @RequestParam(defaultValue = "asc") String direction,
            Model model) {

        Sort sortOrder = direction.equalsIgnoreCase("desc")
                ? Sort.by(sort).descending()
                : Sort.by(sort).ascending();

        Page<ApplianceDto> result = applianceService.findAll(
                search,
                PageRequest.of(page, size, sortOrder));

        model.addAttribute("appliances", result.getContent());
        model.addAttribute("page", result);
        model.addAttribute("search", search);
        model.addAttribute("sort", sort);
        model.addAttribute("direction", direction);
        model.addAttribute("size", size);
        return "appliance/list";
    }

    @GetMapping("/create")
    public String createForm(Model model) {

        model.addAttribute("appliance", new ApplianceDto());
        model.addAttribute("manufacturers", manufacturerService.findAll());
        model.addAttribute("editMode", false);

        return "appliance/form";
    }

    @PostMapping("/create")
    public String create(
            @Valid @ModelAttribute("appliance") ApplianceDto dto,
            BindingResult bindingResult,
            Model model) {

        if (bindingResult.hasErrors()) {
            model.addAttribute("manufacturers", manufacturerService.findAll());
            model.addAttribute("editMode", false);
            return "appliance/form";
        }

        applianceService.save(dto);

        return "redirect:/appliances";
    }

    @GetMapping("/edit/{id}")
    public String editForm(
            @PathVariable Long id,
            Model model) {

        model.addAttribute("appliance", applianceService.findById(id));
        model.addAttribute("manufacturers", manufacturerService.findAll());
        model.addAttribute("editMode", true);

        return "appliance/form";
    }

    @PostMapping("/edit/{id}")
    public String update(
            @PathVariable Long id,
            @Valid @ModelAttribute("appliance") ApplianceDto dto,
            BindingResult bindingResult,
            Model model) {

        if (bindingResult.hasErrors()) {
            model.addAttribute("manufacturers", manufacturerService.findAll());
            model.addAttribute("editMode", true);
            return "appliance/form";
        }

        applianceService.update(id, dto);

        return "redirect:/appliances";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Long id) {

        applianceService.delete(id);

        return "redirect:/appliances";
    }
}
