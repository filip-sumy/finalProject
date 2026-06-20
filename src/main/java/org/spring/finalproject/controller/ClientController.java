package org.spring.finalproject.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.spring.finalproject.dto.ApplianceDto;
import org.spring.finalproject.dto.ClientDto;
import org.spring.finalproject.service.ClientService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/clients")
@RequiredArgsConstructor
public class ClientController {

    private final ClientService clientService;

    @GetMapping
    public String findAll(@RequestParam(required = false) String search,
                          @RequestParam(defaultValue = "0") int page,
                          @RequestParam(defaultValue = "10") int size,
                          @RequestParam(defaultValue = "id") String sort,
                          @RequestParam(defaultValue = "asc") String direction,
                          Model model) {
        Sort sortOrder = direction.equalsIgnoreCase("desc")
                ? Sort.by(sort).descending()
                : Sort.by(sort).ascending();

        Page<ClientDto> result = clientService.findAll(
                search,
                PageRequest.of(page, size, sortOrder));

        model.addAttribute("clients", result.getContent());
        model.addAttribute("page", result);
        model.addAttribute("search", search);
        model.addAttribute("sort", sort);
        model.addAttribute("direction", direction);
        model.addAttribute("size", size);
        return "client/list";
    }

    @GetMapping("/create")
    public String createForm(Model model) {

        model.addAttribute("client", new ClientDto());
        model.addAttribute("editMode", false);

        return "client/form";
    }

    @PostMapping("/create")
    public String create(
            @Valid @ModelAttribute("client") ClientDto dto,
            BindingResult bindingResult,
            Model model) {

        if (dto.getPassword() == null || dto.getPassword().isBlank()) {
            bindingResult.rejectValue(
                    "password", "password.required");
        }

        if (bindingResult.hasErrors()) {
            model.addAttribute("editMode", false);
            return "client/form";
        }

        clientService.save(dto);

        return "redirect:/clients";
    }

    @GetMapping("/edit/{id}")
    public String editForm(
            @PathVariable Long id,
            Model model) {

        ClientDto dto = clientService.findById(id);
        dto.setPassword(null);

        model.addAttribute("client", dto);
        model.addAttribute("editMode", true);

        return "client/form";
    }

    @PostMapping("/edit/{id}")
    public String update(
            @PathVariable Long id,
            @Valid @ModelAttribute("client") ClientDto dto,
            BindingResult bindingResult,
            Model model) {

        dto.setId(id);

        if (bindingResult.hasErrors()) {
            model.addAttribute("editMode", true);
            return "client/form";
        }

        clientService.update(id, dto);

        return "redirect:/clients";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Long id) {

        clientService.delete(id);

        return "redirect:/clients";
    }
}
