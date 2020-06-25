/*
 * Copyright 2018-Present Entando Inc. (http://www.entando.com) All rights reserved.
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
import org.entando.entando.web.AbstractControllerIntegrationTest;
import org.entando.entando.web.MockMvcHelper;
import org.entando.entando.web.assertionhelper.HealthAssertionHelper;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class HealthControllerIntegrationTest extends AbstractControllerIntegrationTest {

    private final String healthEndpoint = "/health";

    @Autowired
    private IHealthService healthService;

    private MockMvcHelper mockMvcHelper;

    @Before
    public void setupTests() {
        mockMvcHelper = new MockMvcHelper(mockMvc);
    }

    @Test
    public void isHealthy() throws Exception {

        HealthAssertionHelper.assertSuccessfulRestResponse(mockMvcHelper.getMockMvc(healthEndpoint));
    }
}
