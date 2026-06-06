package org.spring.finalproject.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
public class HomeController {

    private final MessageSource messageSource;

    @GetMapping("/")
    public String home() {
        return "index";
    }

    @GetMapping("/error")
    public String error(
            @RequestParam(required = false) Integer code,
            Model model) {

        if (!model.containsAttribute("errorCode")) {
            model.addAttribute(
                    "errorCode",
                    code != null ? code : 500);
        }

        if (!model.containsAttribute("errorMessage")) {
            String key = (code != null && code == 403)
                    ? "error.forbidden"
                    : "error.internal";

            model.addAttribute(
                    "errorMessage",
                    messageSource.getMessage(
                            key,
                            null,
                            key,
                            LocaleContextHolder.getLocale()));
        }

        return "error";
    }
}
