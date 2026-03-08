package org.example.fullstackrevconnect.common.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;

@Slf4j
@Aspect
@Component
public class LoggingAspect {


    @Pointcut("execution(* org.example.fullstackrevconnect.modules.service.impl.*.*(..))")
    public void serviceLayer() {}

    @Before("serviceLayer()")
    public void logBefore(JoinPoint joinPoint) {

        log.info("Entering method: {}",
                joinPoint.getSignature().toShortString());

        Object[] args = joinPoint.getArgs();

        for (Object arg : args) {
            log.info("Argument: {}", arg);
        }
    }

    @AfterReturning(pointcut = "serviceLayer()", returning = "result")
    public void logAfterReturning(JoinPoint joinPoint, Object result) {

        log.info("Exiting method: {}",
                joinPoint.getSignature().toShortString());

        log.info("Return value: {}", result);
    }

    @AfterThrowing(pointcut = "serviceLayer()", throwing = "ex")
    public void logAfterThrowing(JoinPoint joinPoint, Exception ex) {

        log.error("Exception in method: {}",
                joinPoint.getSignature().toShortString());

        log.error("Error message: {}", ex.getMessage());
    }
}