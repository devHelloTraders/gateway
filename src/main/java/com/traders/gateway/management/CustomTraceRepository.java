package com.traders.gateway.management;

import org.springframework.boot.actuate.web.exchanges.HttpExchange;
import org.springframework.boot.actuate.web.exchanges.HttpExchangeRepository;
import org.springframework.stereotype.Repository;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

@Repository
public class CustomTraceRepository implements HttpExchangeRepository {

    private final List<TraceEntry> traces = Collections.synchronizedList(new ArrayList<>());
    private static final Duration RETENTION_PERIOD = Duration.ofHours(2); // 2 hours

    @Override
    public List<HttpExchange> findAll() {
        synchronized (traces) {
            Iterator<TraceEntry> iterator = traces.iterator();
            Instant now = Instant.now();
            while (iterator.hasNext()) {
                TraceEntry entry = iterator.next();
                if (Duration.between(entry.timestamp, now).compareTo(RETENTION_PERIOD) > 0) {
                    iterator.remove();
                }
            }
            // Return only the HttpExchange objects
            return traces.stream()
                .map(entry -> entry.exchange)
                .collect(Collectors.toList());
        }
    }

    @Override
    public void add(HttpExchange trace) {
        synchronized (traces) {
            // Add new trace with current timestamp
            traces.add(new TraceEntry(trace, Instant.now()));
            // Optional: Limit size to prevent memory issues (e.g., max 1000 traces)
            if (traces.size() > 500) {
                traces.remove(0); // Remove oldest if exceeding limit
            }
        }
    }

    // Inner class to pair HttpExchange with timestamp
    private static class TraceEntry {
        final HttpExchange exchange;
        final Instant timestamp;

        TraceEntry(HttpExchange exchange, Instant timestamp) {
            this.exchange = exchange;
            this.timestamp = timestamp;
        }
    }
}
