package org.spring.finalproject.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.spring.finalproject.dto.request.EmployeeDto;
import org.spring.finalproject.service.EmployeeService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/employees")
@RequiredArgsConstructor
public class EmployeeController {

    private final EmployeeService employeeService;

    @GetMapping
    public String findAll(Model model) {

        model.addAttribute(
                "employees",
                employeeService.findAll());

        return "employee/list";
    }

    @GetMapping("/create")
    public String createForm(Model model) {

        model.addAttribute(
                "employee",
                new EmployeeDto());

        model.addAttribute(
                "editMode",
                false);

        return "employee/form";
    }

    @PostMapping("/create")
    public String create(
            @Valid
            @ModelAttribute("employee")
            EmployeeDto dto,
            BindingResult result) {

        if (result.hasErrors()) {
            return "employee/form";
        }

        employeeService.save(dto);

        return "redirect:/employees";
    }

    @GetMapping("/edit/{id}")
    public String editForm(
            @PathVariable Long id,
            Model model) {

        model.addAttribute(
                "employee",
                employeeService.findById(id));

        model.addAttribute(
                "editMode",
                true);

        return "employee/form";
    }

    @PostMapping("/edit/{id}")
    public String update(
            @PathVariable Long id,
            @Valid
            @ModelAttribute("employee")
            EmployeeDto dto,
            BindingResult result) {

        if (result.hasErrors()) {
            return "employee/form";
        }

        employeeService.update(id, dto);

        return "redirect:/employees";
    }

    @GetMapping("/delete/{id}")
    public String delete(
            @PathVariable Long id) {

        employeeService.delete(id);

        return "redirect:/employees";
    }
}
