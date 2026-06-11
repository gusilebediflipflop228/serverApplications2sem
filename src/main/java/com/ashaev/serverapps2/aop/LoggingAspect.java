package com.ashaev.serverapps2.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Arrays;


//* Лоогирование вызовом методов

@Aspect
@Component
@Slf4j
public class LoggingAspect {

    @Pointcut("execution(* com.ashaev.serverapps2.service.*.*(..))")
    public void serviceMethods() {}

    @Around("serviceMethods()")
    public Object profileMethod(ProceedingJoinPoint joinPoint) throws Throwable {
        long start = System.currentTimeMillis();
        String username = SecurityContextHolder.getContext().getAuthentication() != null
                ? SecurityContextHolder.getContext().getAuthentication().getName()
                : "Anonymous";

        log.info("[AUTH: {}] Calling: {}.{} | Args: {}",
                username, joinPoint.getSignature().getDeclaringType().getSimpleName(),
                joinPoint.getSignature().getName(), Arrays.toString(joinPoint.getArgs()));

        try {
            Object result = joinPoint.proceed();
            long executionTime = System.currentTimeMillis() - start;
            log.info("Method {}.{} executed in {}ms",
                    joinPoint.getSignature().getDeclaringType().getSimpleName(),
                    joinPoint.getSignature().getName(), executionTime);
            return result;
        } catch (Exception e) {
            log.error("Method {}.{} failed with exception: {}",
                    joinPoint.getSignature().getDeclaringType().getSimpleName(),
                    joinPoint.getSignature().getName(), e.getMessage());
            throw e;
        }
    }
}