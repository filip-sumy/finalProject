package org.spring.finalproject.exception;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
@Slf4j
@RequiredArgsConstructor
public class GlobalExceptionHandler {

    private final MessageSource messageSource;

    @ExceptionHandler(EntityNotFoundException.class)
    public String handleNotFound(
            EntityNotFoundException ex,
            Model model) {

        log.warn("Entity not found: {}", ex.getMessage());
        addError(model, 404, ex.getMessage());
        return "error";
    }

    @ExceptionHandler(EntityInUseException.class)
    public String handleEntityInUse(
            EntityInUseException ex,
            Model model) {

        log.warn("Entity in use: {}", ex.getMessageKey());
        addError(model, 409, resolveMessage(ex.getMessageKey()));
        return "error";
    }

    @ExceptionHandler(OrderAlreadyApprovedException.class)
    public String handleOrderApproved(
            OrderAlreadyApprovedException ex,
            Model model) {

        log.warn("Order operation rejected: {}", ex.getMessage());
        addError(model, 400, ex.getMessage());
        return "error";
    }

    @ExceptionHandler(InsufficientStockException.class)
    public String handleInsufficientStock(
            InsufficientStockException ex,
            Model model) {

        log.warn("Insufficient stock: {}", ex.getMessage());
        addError(model, 400, ex.getMessage());
        return "error";
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public String handleDataIntegrity(
            DataIntegrityViolationException ex,
            Model model) {

        log.warn("Data integrity violation", ex);
        addError(model, 409,
                resolveMessage("error.delete.referenced"));
        return "error";
    }

    @ExceptionHandler(Exception.class)
    public String handleGeneral(Exception ex, Model model) {

        log.error("Unexpected error", ex);
        addError(model, 500,
                resolveMessage("error.internal"));
        return "error";
    }

    private void addError(Model model, int code, String message) {
        model.addAttribute("errorCode", code);
        model.addAttribute("errorMessage", message);
    }

    private String resolveMessage(String key) {
        return messageSource.getMessage(
                key,
                null,
                key,
                LocaleContextHolder.getLocale());
    }
}
