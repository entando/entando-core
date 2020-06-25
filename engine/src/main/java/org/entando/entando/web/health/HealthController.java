/*
 * Copyright 2015-Present Entando Inc. (http://www.entando.com) All rights reserved.
 *
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 */
package org.entando.entando.web.health;

import org.entando.entando.aps.system.services.health.IHealthService;
import org.entando.entando.web.health.model.HealthResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/health")
public class HealthController {

    private final String STATUS_UP = "UP";
    private final String STATUS_DOWN = "DOWN";

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private IHealthService healthService;

    @Autowired
    public HealthController(IHealthService healthService) {
        this.healthService = healthService;
    }

    @GetMapping
    public ResponseEntity<HealthResponse> isHealthy() {

        logger.debug("health check");

        boolean healthy = this.healthService.isHealthy();

        return new ResponseEntity<>(new HealthResponse(healthy ? STATUS_UP : STATUS_DOWN), HttpStatus.OK);
    }
}