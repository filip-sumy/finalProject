package org.spring.finalproject.api.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class LoginRequest {

    /**
     * Database users: email (e.g. {@code john@test.com}).
     * In-memory fallback: short username ({@code admin}, {@code employee}).
     */
    @NotBlank
    private String username;

    @NotBlank
    private String password;
}
