package com.example.VerveGaurd.service;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Service
public class RateLimiterService {
    // store IPs with their timestamps
    private final Map<String, List<Long>> requestCounts = new ConcurrentHashMap<>();

    private static final short max_requests = 5;
    private static final Long one_minute_in_ms = 60_000L;

    public boolean isRateLimited(String ipAddress) {
        long now = System.currentTimeMillis();
        requestCounts.putIfAbsent(ipAddress, new ArrayList<>());
        List<Long> timestamps = requestCounts.get(ipAddress);
        timestamps.removeIf(timestamp -> now - timestamp > one_minute_in_ms);

        if(timestamps.size() >= max_requests) return true;
        timestamps.add(now);
        return false;
    }
}
