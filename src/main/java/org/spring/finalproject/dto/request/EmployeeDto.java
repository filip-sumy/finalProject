package org.spring.finalproject.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeDto {

    private Long id;

    @NotBlank(message = "{employee.firstname.notblank}")
    private String firstName;

    @NotBlank(message = "{employee.lastname.notblank}")
    private String lastName;

    @Email(message = "{employee.email.invalid}")
    private String email;

    private String position;

    @Positive
    private BigDecimal salary;

    @NotBlank
    private String password;
}
