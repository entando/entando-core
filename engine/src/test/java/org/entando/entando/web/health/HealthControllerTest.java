package org.entando.entando.web.health;

import org.entando.entando.aps.system.services.health.HealthService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

public class HealthControllerTest {

    @Mock
    private HealthService healthService;

    private HealthController healthController;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        healthController = new HealthController(healthService);
    }

    @Test
    public void isHealthyWithWorkingSystemShouldReturnStatus200() {

        when(healthService.isHealthy()).thenReturn(true);

        assertEquals(HttpStatus.OK.value(), healthController.isHealthy().getStatusCodeValue());
    }

    @Test
    public void isHealthyWithNotWorkingSystemShouldReturnStatus500() {

        when(healthService.isHealthy()).thenReturn(false);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.value(), healthController.isHealthy().getStatusCodeValue());
    }
}
