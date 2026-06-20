package org.spring.finalproject.exception;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
@Slf4j
@RequiredArgsConstructor
public class GlobalExceptionHandler {

    private final ExceptionMessageResolver messageResolver;

    @ExceptionHandler(EntityNotFoundException.class)
    public String handleNotFound(EntityNotFoundException ex, Model model) {
        log.warn("Entity not found: {}", ex.getMessageKey());
        addError(model, 404, messageResolver.resolve(ex));
        return "error";
    }

    @ExceptionHandler(EntityInUseException.class)
    public String handleEntityInUse(EntityInUseException ex, Model model) {
        log.warn("Entity in use: {}", ex.getMessageKey());
        addError(model, 409, messageResolver.resolve(ex));
        return "error";
    }

    @ExceptionHandler(OrderAlreadyApprovedException.class)
    public String handleOrderApproved(OrderAlreadyApprovedException ex, Model model) {
        log.warn("Order operation rejected: {}", ex.getMessageKey());
        addError(model, 400, messageResolver.resolve(ex));
        return "error";
    }

    @ExceptionHandler(InsufficientStockException.class)
    public String handleInsufficientStock(InsufficientStockException ex, Model model) {
        log.warn("Insufficient stock: {}", ex.getMessageKey());
        addError(model, 400, messageResolver.resolve(ex));
        return "error";
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public String handleDataIntegrity(DataIntegrityViolationException ex, Model model) {
        log.warn("Data integrity violation", ex);
        addError(model, 409, messageResolver.resolve("error.delete.referenced"));
        return "error";
    }

    @ExceptionHandler(Exception.class)
    public String handleGeneral(Exception ex, Model model) {
        log.error("Unexpected error", ex);
        addError(model, 500, messageResolver.resolve("error.internal"));
        return "error";
    }

    private void addError(Model model, int code, String message) {
        model.addAttribute("errorCode", code);
        model.addAttribute("errorMessage", message);
    }
}
