package org.entando.entando.aps.system.services.health;

import com.agiletec.aps.system.services.health.IHealthDAO;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class HealthServiceTest {

    @Mock
    private IHealthDAO healthDAO;

    private HealthService healthService;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        healthService = new HealthService(healthDAO);
    }

    @Test
    public void withPortSchemaNotReachableShouldReturnFalse() {

        when(healthDAO.isPortDBConnectionHealthy()).thenReturn(false);

        assertFalse(healthService.isHealthy());
    }

    @Test
    public void withServSchemaNotReachableShouldReturnFalse() {

        when(healthDAO.isPortDBConnectionHealthy()).thenReturn(true);
        when(healthDAO.isServDBConnectionHealthy()).thenReturn(false);

        assertFalse(healthService.isHealthy());
    }

    @Test
    public void withAllSchemaaReachableShouldReturnTrue() {

        when(healthDAO.isPortDBConnectionHealthy()).thenReturn(true);
        when(healthDAO.isServDBConnectionHealthy()).thenReturn(true);

        assertTrue(healthService.isHealthy());
    }
}
