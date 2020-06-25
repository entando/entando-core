package org.entando.entando.web.health;

import org.entando.entando.aps.system.services.health.HealthService;
import org.entando.entando.web.assertionhelper.HealthAssertionHelper;
import org.entando.entando.web.common.exceptions.EntandoHealthException;
import org.entando.entando.web.health.model.HealthErrorResponse;
import org.entando.entando.web.health.model.HealthResponse;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.time.ZoneId;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
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
    public void isHealthyWithWorkingSystemShouldReturnUpStatus() {

        when(healthService.isHealthy()).thenReturn(true);

        ResponseEntity<HealthResponse> expected = new ResponseEntity<>(new HealthResponse("UP"), HttpStatus.OK);

        HealthAssertionHelper.assertHealthResponse(expected, healthController.isHealthy());
    }

    @Test
    public void isHealthyWithNotWorkingSystemShouldReturnErrorResponse() {

        EntandoHealthException exception = new EntandoHealthException("Can't establish connection with Port database schema");

        when(healthService.isHealthy()).thenThrow(exception);

        HealthErrorResponse expected = new HealthErrorResponse()
                .setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .setTimestamp(LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli())
                .setError("Entando-core is UNHEALTHY")
                .setMessage(exception.getMessage());

        try {
            healthController.isHealthy();
        } catch (Exception e) {
            assertTrue(e instanceof EntandoHealthException);
            assertEquals(exception.getMessage(), e.getMessage());
        }
    }
}
