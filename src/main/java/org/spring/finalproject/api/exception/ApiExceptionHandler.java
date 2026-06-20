package org.spring.finalproject.api.exception;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.spring.finalproject.api.dto.response.ApiErrorResponse;
import org.spring.finalproject.exception.BusinessException;
import org.spring.finalproject.exception.ExceptionMessageResolver;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice(basePackages = "org.spring.finalproject.api")
@Slf4j
@RequiredArgsConstructor
public class ApiExceptionHandler {

    private final ExceptionMessageResolver messageResolver;

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ApiErrorResponse> handleBusiness(BusinessException ex) {
        log.warn("API business error: {}", ex.getMessageKey());
        HttpStatus status = resolveStatus(ex);
        return ResponseEntity
                .status(status)
                .body(new ApiErrorResponse(status.value(), messageResolver.resolve(ex)));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiErrorResponse> handleValidation(
            MethodArgumentNotValidException ex) {

        String message = ex.getBindingResult().getFieldErrors().stream()
                .findFirst()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .orElse("Validation failed");

        return ResponseEntity
                .badRequest()
                .body(new ApiErrorResponse(400, message));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiErrorResponse> handleGeneral(Exception ex) {
        log.error("Unexpected API error", ex);
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ApiErrorResponse(
                        500,
                        messageResolver.resolve("error.internal")));
    }

    private HttpStatus resolveStatus(BusinessException ex) {
        return switch (ex.getClass().getSimpleName()) {
            case "EntityNotFoundException" -> HttpStatus.NOT_FOUND;
            case "EntityInUseException" -> HttpStatus.CONFLICT;
            case "OrderAlreadyApprovedException", "InsufficientStockException" ->
                    HttpStatus.BAD_REQUEST;
            default -> HttpStatus.BAD_REQUEST;
        };
    }
}
