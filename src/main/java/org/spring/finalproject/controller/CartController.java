package org.spring.finalproject.controller;

import lombok.RequiredArgsConstructor;
import org.spring.finalproject.dto.ApplianceDto;
import org.spring.finalproject.dto.ClientDto;
import org.spring.finalproject.exception.InsufficientStockException;
import org.spring.finalproject.service.ApplianceService;
import org.spring.finalproject.service.ClientService;
import org.spring.finalproject.service.OrderService;
import org.spring.finalproject.service.cart.Cart;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/cart")
@RequiredArgsConstructor
@PreAuthorize("hasRole('CLIENT')")
public class CartController {

    private final Cart cart;
    private final ApplianceService applianceService;
    private final OrderService orderService;
    private final ClientService clientService;

    @GetMapping
    public String view(Model model) {
        model.addAttribute("items", cart.getItems());
        model.addAttribute("totalPrice", cart.getTotalPrice());
        return "cart/view";
    }

    @PostMapping("/add")
    public String add(@RequestParam Long applianceId,
                      @RequestParam(defaultValue = "1") int quantity,
                      RedirectAttributes redirectAttributes) {

        try {
            ApplianceDto appliance = applianceService.findById(applianceId);
            cart.addItem(appliance, quantity);
            redirectAttributes.addFlashAttribute("message", "cart.added");
        } catch (InsufficientStockException ex) {
            redirectAttributes.addFlashAttribute("error", ex.getMessage());
        }

        return "redirect:/cart";
    }

    @PostMapping("/update/{applianceId}")
    public String update(@PathVariable Long applianceId,
                         @RequestParam int quantity,
                         RedirectAttributes redirectAttributes) {

        try {
            ApplianceDto appliance = applianceService.findById(applianceId);
            cart.updateQuantity(applianceId, quantity, appliance);
            redirectAttributes.addFlashAttribute("message", "cart.updated");
        } catch (InsufficientStockException | IllegalArgumentException ex) {
            redirectAttributes.addFlashAttribute("error", ex.getMessage());
        }

        return "redirect:/cart";
    }

    @PostMapping("/remove/{applianceId}")
    public String remove(@PathVariable Long applianceId,
                         RedirectAttributes redirectAttributes) {

        cart.removeItem(applianceId);
        redirectAttributes.addFlashAttribute("message", "cart.removed");
        return "redirect:/cart";
    }

    @PostMapping("/checkout")
    public String checkout(Authentication authentication,
                           RedirectAttributes redirectAttributes) {

        if (cart.isEmpty()) {
            redirectAttributes.addFlashAttribute("errorKey", "cart.empty");
            return "redirect:/cart";
        }

        try {
            ClientDto client = clientService.findByEmail(authentication.getName());
            var orderDto = cart.toOrderDto();
            orderDto.setClientId(client.getId());
            orderService.create(orderDto);
            cart.clear();
            redirectAttributes.addFlashAttribute("message", "cart.checkout.success");
            return "redirect:/orders";
        } catch (InsufficientStockException ex) {
            redirectAttributes.addFlashAttribute("error", ex.getMessage());
            return "redirect:/cart";
        }
    }
}
