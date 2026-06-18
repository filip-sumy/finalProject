package org.spring.finalproject.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.spring.finalproject.dto.EmployeeDto;
import org.spring.finalproject.service.EmployeeService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/employees")
@PreAuthorize("hasRole('ADMIN')")
@RequiredArgsConstructor
public class EmployeeController {

    private final EmployeeService employeeService;

    @GetMapping
    public String findAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sort,
            @RequestParam(defaultValue = "asc") String direction,
            Model model) {

        Sort sortOrder = direction.equalsIgnoreCase("desc")
                ? Sort.by(sort).descending()
                : Sort.by(sort).ascending();

        Page<EmployeeDto> result = employeeService.findAll(
                PageRequest.of(page, size, sortOrder));

        model.addAttribute("employees", result.getContent());
        model.addAttribute("page", result);
        model.addAttribute("sort", sort);
        model.addAttribute("direction", direction);

        return "employee/list";
    }

    @GetMapping("/create")
    public String createForm(Model model) {

        model.addAttribute("employee", new EmployeeDto());
        model.addAttribute("editMode", false);

        return "employee/form";
    }

    @PostMapping("/create")
    public String create(
            @Valid @ModelAttribute("employee") EmployeeDto dto,
            BindingResult result,
            Model model) {

        if (dto.getPassword() == null || dto.getPassword().isBlank()) {
            result.rejectValue(
                    "password", "password.required");
        }

        if (result.hasErrors()) {
            model.addAttribute("editMode", false);
            return "employee/form";
        }

        employeeService.save(dto);

        return "redirect:/employees";
    }

    @GetMapping("/edit/{id}")
    public String editForm(
            @PathVariable Long id,
            Model model) {

        EmployeeDto dto = employeeService.findById(id);
        dto.setPassword(null);

        model.addAttribute("employee", dto);
        model.addAttribute("editMode", true);

        return "employee/form";
    }

    @PostMapping("/edit/{id}")
    public String update(
            @PathVariable Long id,
            @Valid @ModelAttribute("employee") EmployeeDto dto,
            BindingResult result,
            Model model) {

        dto.setId(id);

        if (result.hasErrors()) {
            model.addAttribute("editMode", true);
            return "employee/form";
        }

        employeeService.update(id, dto);

        return "redirect:/employees";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Long id) {

        employeeService.delete(id);

        return "redirect:/employees";
    }
}
