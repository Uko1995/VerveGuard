package com.example.VerveGaurd.audit;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.jspecify.annotations.NonNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.UUID;

@Component
public class PerformanceInterceptor implements HandlerInterceptor {

    private static final Logger log = LoggerFactory.getLogger(PerformanceInterceptor.class);

    @Override
    public boolean preHandle(HttpServletRequest request,
                             @NonNull HttpServletResponse response,
                             @NonNull Object handler) {
        long startTime = System.currentTimeMillis();
        String traceId = UUID.randomUUID().toString();
        
        request.setAttribute("startTime", startTime);
        request.setAttribute("traceId", traceId);
        
        // Put traceId in MDC so it appears in all logs for this request
        MDC.put("traceId", traceId);
        
        // Log separator for readability
        log.info("=".repeat(100));
        log.info("➤ REQUEST START | traceId={} | {} {}", traceId, request.getMethod(), request.getRequestURI());
        log.info("=".repeat(100));

        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request,
                                @NonNull HttpServletResponse response,
                                @NonNull Object handler,
                                Exception ex) {

        long start = (Long) request.getAttribute("startTime");
        String traceId = (String) request.getAttribute("traceId");
        long duration = System.currentTimeMillis() - start;

        AuditLog auditLog = new AuditLog(
                "REQUEST",
                "HTTP",
                request.getMethod() + " " + request.getRequestURI(),
                duration
        );

        StringBuilder notes = new StringBuilder();
        notes.append(String.format("traceId=%s | httpStatus=%d | duration=%dms", traceId, response.getStatus(), duration));

        if (ex != null) {
            notes.append(" | EXCEPTION: ").append(ex.getMessage());
        }

        auditLog.setNotes(notes.toString());

        if ("SLOW".equals(auditLog.getStatus())) {
            log.warn("⚠ [SLOW] {}", auditLog.toString());
        } else {
            log.info("✓ {}", auditLog.toString());
        }

        // Log separator for readability
        log.info("─".repeat(100));

        // Clean up MDC to prevent memory leaks
        MDC.remove("traceId");
    }
}