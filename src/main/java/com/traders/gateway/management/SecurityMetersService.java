package com.traders.gateway.management;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.stereotype.Service;

@Service
public class SecurityMetersService extends com.traders.common.management.SecurityMetersService {

    public SecurityMetersService(MeterRegistry registry) {
        super(registry);
    }
}
