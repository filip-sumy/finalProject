package org.spring.finalproject.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.spring.finalproject.dto.ManufacturerDto;
import org.spring.finalproject.service.ManufacturerService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/manufacturers")
@RequiredArgsConstructor
public class ManufacturerController {

    private final ManufacturerService manufacturerService;

    @GetMapping
    public String findAll(Model model) {

        model.addAttribute(
                "manufacturers",
                manufacturerService.findAll());

        return "manufacturer/list";
    }

    @GetMapping("/create")
    public String createForm(Model model) {

        model.addAttribute("manufacturer", new ManufacturerDto());
        model.addAttribute("editMode", false);

        return "manufacturer/form";
    }

    @PostMapping("/create")
    public String create(
            @Valid @ModelAttribute("manufacturer")
            ManufacturerDto manufacturerDto,
            BindingResult bindingResult,
            Model model) {

        if (bindingResult.hasErrors()) {
            model.addAttribute("editMode", false);
            return "manufacturer/form";
        }

        manufacturerService.save(manufacturerDto);

        return "redirect:/manufacturers";
    }

    @GetMapping("/edit/{id}")
    public String editForm(
            @PathVariable Long id,
            Model model) {

        model.addAttribute(
                "manufacturer",
                manufacturerService.findById(id));

        model.addAttribute("editMode", true);

        return "manufacturer/form";
    }

    @PostMapping("/edit/{id}")
    public String update(
            @PathVariable Long id,
            @Valid @ModelAttribute("manufacturer")
            ManufacturerDto manufacturerDto,
            BindingResult bindingResult,
            Model model) {

        if (bindingResult.hasErrors()) {
            model.addAttribute("editMode", true);
            return "manufacturer/form";
        }

        manufacturerService.update(id, manufacturerDto);

        return "redirect:/manufacturers";
    }

    @GetMapping("/delete/{id}")
    public String delete(
            @PathVariable Long id) {

        manufacturerService.delete(id);

        return "redirect:/manufacturers";
    }
}