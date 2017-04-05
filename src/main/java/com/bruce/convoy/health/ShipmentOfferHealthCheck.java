package com.bruce.convoy.health;

import com.codahale.metrics.health.HealthCheck;

public class ShipmentOfferHealthCheck extends HealthCheck {

    public ShipmentOfferHealthCheck() {
    }

    @Override
    protected Result check() throws Exception {
        return Result.healthy();
    }
}