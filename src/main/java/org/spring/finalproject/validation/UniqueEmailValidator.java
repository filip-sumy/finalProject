package org.spring.finalproject.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;
import org.spring.finalproject.dto.ClientDto;
import org.spring.finalproject.dto.EmployeeDto;
import org.spring.finalproject.repository.UserRepository;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UniqueEmailValidator
        implements ConstraintValidator<UniqueEmail, Object> {

    private final UserRepository userRepository;

    @Override
    public boolean isValid(Object value,
                           ConstraintValidatorContext context) {

        if (value == null) {
            return true;
        }

        String email;
        Long id;

        if (value instanceof EmployeeDto dto) {
            email = dto.getEmail();
            id = dto.getId();
        } else if (value instanceof ClientDto dto) {
            email = dto.getEmail();
            id = dto.getId();
        } else {
            return true;
        }

        if (email == null || email.isBlank()) {
            return true;
        }

        if (id == null) {
            return !userRepository.existsByEmail(email);
        }

        return !userRepository.existsByEmailAndIdNot(email, id);
    }
}
