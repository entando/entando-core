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
package org.entando.entando.aps.system.services.digitalexchange;

import com.agiletec.aps.system.SystemConstants;
import com.agiletec.aps.system.services.baseconfig.ConfigInterface;
import java.util.List;
import org.entando.entando.aps.system.services.digitalexchange.model.DigitalExchange;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.entando.entando.aps.system.exception.RestRourceNotFoundException;
import org.entando.entando.web.common.exceptions.ValidationConflictException;
import org.entando.entando.web.digitalexchange.DigitalExchangeValidator;
import static org.mockito.Mockito.when;
import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertNotNull;
import static junit.framework.TestCase.assertTrue;

public class DigitalExchangesServiceTest {

    @Mock
    private ConfigInterface configManager;

    @InjectMocks
    private DigitalExchangesServiceImpl service;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        mockValidDigitalExchangesConfig();
    }

    @Test
    public void shouldReturnDigitalExchanges() {

        List<DigitalExchange> digitalExchanges = service.getDigitalExchanges();

        assertNotNull(digitalExchanges);
        assertEquals(1, digitalExchanges.size());
        assertEquals("DE 1", digitalExchanges.get(0).getName());
        assertEquals("http://www.entando.com/", digitalExchanges.get(0).getUrl());
    }

    @Test
    public void shouldReturnEmptyList() {

        // invalid configuration
        when(configManager.getConfigItem(SystemConstants.CONFIG_ITEM_DIGITAL_EXCHANGES))
                .thenReturn("");

        List<DigitalExchange> digitalExchanges = service.getDigitalExchanges();
        assertNotNull(digitalExchanges);
        assertTrue(digitalExchanges.isEmpty());

        // Restore valid configuration mocking for other tests
        mockValidDigitalExchangesConfig();
    }

    @Test
    public void shouldFindDigitalExchange() {
        DigitalExchange digitalExchange = service.findByName("DE 1");
        assertNotNull(digitalExchange);
    }

    @Test(expected = RestRourceNotFoundException.class)
    public void shouldFindNothing() {
        service.findByName("Inexistent DE");
    }

    @Test
    public void shouldAddDigitalExchange() {
        String newDigitalExchangeName = "DE 2";
        DigitalExchange digitalExchange = service.create(getDigitalExchange(newDigitalExchangeName));
        assertNotNull(digitalExchange);
        assertEquals(newDigitalExchangeName, digitalExchange.getName());
    }

    @Test(expected = ValidationConflictException.class)
    public void shouldFailAddingDigitalExchange() {
        try {
            String newDigitalExchangeName = "DE 1";
            DigitalExchange digitalExchange = service.create(getDigitalExchange(newDigitalExchangeName));
            assertNotNull(digitalExchange);
        } catch (ValidationConflictException ex) {
            assertEquals(1, ex.getBindingResult().getErrorCount());
            assertEquals(DigitalExchangeValidator.ERRCODE_DIGITAL_EXCHANGE_ALREADY_EXISTS,
                    ex.getBindingResult().getAllErrors().get(0).getCode());
            throw ex;
        }
    }

    @Test
    public void shouldUpdateDigitalExchange() {
        String DigitalExchangeName = "DE 1";
        DigitalExchange digitalExchange = getDigitalExchange(DigitalExchangeName);
        String url = "http://www.entando.com";
        digitalExchange.setUrl(url);
        DigitalExchange updated = service.update(digitalExchange);
        assertNotNull(updated);
        assertEquals(digitalExchange.getName(), updated.getName());
        assertEquals(url, updated.getUrl());
    }

    @Test(expected = RestRourceNotFoundException.class)
    public void shouldFailUpdatingDigitalExchange() {
        service.update(getDigitalExchange("Inexistent DE"));
    }

    @Test
    public void shouldDeleteDigitalExchange() {
        service.delete("DE 1");
    }

    @Test(expected = RestRourceNotFoundException.class)
    public void shouldFailDeletingDigitalExchange() {
        service.delete("Inexistent DE");
    }

    private void mockValidDigitalExchangesConfig() {

        when(configManager.getConfigItem(SystemConstants.CONFIG_ITEM_DIGITAL_EXCHANGES))
                .thenReturn("<digitalExchanges>"
                        + "  <digitalExchange>"
                        + "    <name>DE 1</name>"
                        + "    <url>http://www.entando.com/</url>"
                        + "  </digitalExchange>"
                        + "</digitalExchanges>");
    }

    private DigitalExchange getDigitalExchange(String name) {
        DigitalExchange digitalExchange = new DigitalExchange();
        digitalExchange.setName(name);
        return digitalExchange;
    }
}
