package org.spring.finalproject.exception;

import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ExceptionMessageResolver {

    private final MessageSource messageSource;

    public String resolve(BusinessException ex) {
        return resolve(ex.getMessageKey(), ex.getArgs());
    }

    public String resolve(String messageKey, Object... args) {
        return messageSource.getMessage(
                messageKey,
                args,
                messageKey,
                LocaleContextHolder.getLocale());
    }
}
