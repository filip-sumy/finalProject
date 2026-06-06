package org.spring.finalproject.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.spring.finalproject.dto.request.ClientDto;
import org.spring.finalproject.service.ClientService;
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
    public String findAll(Model model) {

        model.addAttribute(
                "clients",
                clientService.findAll());

        return "client/list";
    }

    @GetMapping("/create")
    public String createForm(Model model) {

        model.addAttribute(
                "client",
                new ClientDto());

        model.addAttribute("editMode", false);

        return "client/form";
    }

    @PostMapping("/create")
    public String create(
            @Valid @ModelAttribute("client")
            ClientDto dto,
            BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            return "client/form";
        }

        clientService.save(dto);

        return "redirect:/clients";
    }

    @GetMapping("/edit/{id}")
    public String editForm(
            @PathVariable Long id,
            Model model) {

        model.addAttribute(
                "client",
                clientService.findById(id));

        model.addAttribute("editMode", true);

        return "client/form";
    }

    @PostMapping("/edit/{id}")
    public String update(
            @PathVariable Long id,
            @Valid @ModelAttribute("client")
            ClientDto dto,
            BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            return "client/form";
        }

        clientService.update(id, dto);

        return "redirect:/clients";
    }

    @GetMapping("/delete/{id}")
    public String delete(
            @PathVariable Long id) {

        clientService.delete(id);

        return "redirect:/clients";
    }
}
