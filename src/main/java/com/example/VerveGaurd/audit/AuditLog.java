package com.example.VerveGaurd.audit;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Getter
@Setter
public class AuditLog {

    private String timestamp;
    private String layer;        // SERVICE, REPOSITORY, REQUEST
    private String className;
    private String methodName;
    private long durationMs;
    private String status;       // OK, SLOW, ERROR
    private String notes;        // any extra info

    public AuditLog(String layer, String className, String methodName, long durationMs) {
        this.timestamp = LocalDateTime.now()
                .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        this.layer = layer;
        this.className = className;
        this.methodName = methodName;
        this.durationMs = durationMs;
        this.status = resolveStatus(layer, durationMs);
    }

    // determine status based on duration thresholds
    private String resolveStatus(String layer, long durationMs) {
        if ("REQUEST".equals(layer)) {
            return durationMs > 500 ? "SLOW" : "OK";
        }
        if ("REPOSITORY".equals(layer)) {
            return durationMs > 100 ? "SLOW" : "OK";
        }
        return durationMs > 200 ? "SLOW" : "OK";
    }

    @Override
    public String toString() {
        return String.format(
                "[%s] | layer=%-12s | %s.%s() | duration=%dms | status=%s %s",
                timestamp,
                layer,
                className,
                methodName,
                durationMs,
                status,
                notes != null ? "| notes=" + notes : ""
        );
    }


}