package com.example.VerveGaurd.audit;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.jspecify.annotations.NonNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class PerformanceInterceptor implements HandlerInterceptor {

    private static final Logger log = LoggerFactory.getLogger(PerformanceInterceptor.class);

    @Override
    public boolean preHandle(HttpServletRequest request,
                             @NonNull HttpServletResponse response,
                             @NonNull Object handler) {
        request.setAttribute("startTime", System.currentTimeMillis());
        request.setAttribute("requestId", generateRequestId()); // unique ID per request
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request,
                                @NonNull HttpServletResponse response,
                                @NonNull Object handler,
                                Exception ex) {

        long start = (Long) request.getAttribute("startTime");
        String requestId = (String) request.getAttribute("requestId");
        long duration = System.currentTimeMillis() - start;

        AuditLog auditLog = new AuditLog(
                "REQUEST",
                "HTTP",
                request.getMethod() + " " + request.getRequestURI(),
                duration
        );

        if (ex != null) {
            auditLog.setNotes("EXCEPTION: " + ex.getMessage());
        }

        auditLog.setNotes(
                String.format("requestId=%s | httpStatus=%d", requestId, response.getStatus())
        );

        if ("SLOW".equals(auditLog.getStatus())) {
            log.warn(auditLog.toString());
        } else {
            log.info(auditLog.toString());
        }
    }

    private String generateRequestId() {
        // simple unique ID for tracing a request through all log lines
        return "REQ-" + System.currentTimeMillis();
    }
}