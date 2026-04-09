package com.example.VerveGaurd.audit;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class PerformanceAspect {

    private static final Logger log = LoggerFactory.getLogger(PerformanceAspect.class);

    @Pointcut("execution(* com.example.VerveGaurd..*Service.*(..))")
    public void serviceLayer() {}

    @Pointcut("execution(* com.example.VerveGaurd..*Repository.*(..))")
    public void repositoryLayer() {}

    @Around("serviceLayer()")
    public Object logServiceTime(ProceedingJoinPoint joinPoint) throws Throwable {
        return logExecution(joinPoint, "SERVICE");
    }

    @Around("repositoryLayer()")
    public Object logRepositoryTime(ProceedingJoinPoint joinPoint) throws Throwable {
        return logExecution(joinPoint, "REPOSITORY");
    }

    private Object logExecution(ProceedingJoinPoint joinPoint, String layer) throws Throwable {
        long start = System.currentTimeMillis();
        Exception caughtException = null;
        Object result = null;

        try {
            result = joinPoint.proceed(); // run the actual method
        } catch (Exception e) {
            caughtException = e;
        }

        long duration = System.currentTimeMillis() - start;

        AuditLog auditLog = new AuditLog(
                layer,
                joinPoint.getTarget().getClass().getSimpleName(),
                joinPoint.getSignature().getName(),
                duration
        );

        if (caughtException != null) {
            auditLog.setNotes("EXCEPTION: " + caughtException.getMessage());
            log.error(auditLog.toString());
            throw caughtException; // rethrow so GlobalExceptionHandler catches it
        }

        if ("SLOW".equals(auditLog.getStatus())) {
            log.warn(auditLog.toString());
        } else {
            log.info(auditLog.toString());
        }

        return result;
    }
}