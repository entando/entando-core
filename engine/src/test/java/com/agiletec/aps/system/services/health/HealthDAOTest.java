package com.agiletec.aps.system.services.health;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class HealthDAOTest {

    @Mock
    private DataSource portDataSource;
    @Mock
    private DataSource servDataSource;
    @Mock
    private Connection connection;

    private HealthDAO healthDAO;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        healthDAO = new HealthDAO()
                .setPortDataSource(portDataSource)
                .setServDataSource(servDataSource);
    }

    @Test
    public void isServDBConnectionHealthyWithWorkingDataSourceShouldReturnTrue() throws Exception {

        when(servDataSource.getConnection()).thenReturn(connection);

        assertTrue(healthDAO.isServDBConnectionHealthy());
    }

    @Test
    public void isServDBConnectionHealthyWithNotWorkingDataSourceShouldReturnFalse() throws Exception {

        when(servDataSource.getConnection()).thenThrow(new SQLException());

        assertFalse(healthDAO.isServDBConnectionHealthy());
    }

    @Test
    public void isPortDBConnectionHealthyWithWorkingDataSourceShouldReturnTrue() throws Exception {

        when(portDataSource.getConnection()).thenReturn(connection);

        assertTrue(healthDAO.isPortDBConnectionHealthy());
    }

    @Test
    public void isPortDBConnectionHealthyWithNotWorkingDataSourceShouldReturnFalse() throws Exception {

        when(portDataSource.getConnection()).thenThrow(new SQLException());

        assertFalse(healthDAO.isPortDBConnectionHealthy());
    }
}
