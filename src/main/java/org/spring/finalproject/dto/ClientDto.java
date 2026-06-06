package org.spring.finalproject.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.spring.finalproject.validation.PasswordPolicy;
import org.spring.finalproject.validation.UniqueEmail;

@Data
@NoArgsConstructor
@AllArgsConstructor
@UniqueEmail
public class ClientDto {

    private Long id;

    @NotBlank
    private String firstName;

    @NotBlank
    private String lastName;

    @Email
    private String email;

    @PasswordPolicy
    private String password;

    private String phone;

    private String address;
}