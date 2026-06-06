package org.spring.finalproject.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Slf4j
public class LoggingAspect {

    @Before("execution(* org.spring.finalproject.service..*(..))")
    public void logServiceCall(JoinPoint joinPoint) {
        log.debug("Service call: {}.{}",
                joinPoint.getSignature().getDeclaringTypeName(),
                joinPoint.getSignature().getName());
    }

    @AfterReturning(
            pointcut = "execution(* org.spring.finalproject.service..*(..))",
            returning = "result")
    public void logServiceReturn(JoinPoint joinPoint, Object result) {
        log.debug("Service completed: {}.{}",
                joinPoint.getSignature().getDeclaringTypeName(),
                joinPoint.getSignature().getName());
    }

    @AfterThrowing(
            pointcut = "execution(* org.spring.finalproject.service..*(..))",
            throwing = "ex")
    public void logServiceError(JoinPoint joinPoint, Throwable ex) {
        log.error("Service error in {}.{}: {}",
                joinPoint.getSignature().getDeclaringTypeName(),
                joinPoint.getSignature().getName(),
                ex.getMessage());
    }
}
