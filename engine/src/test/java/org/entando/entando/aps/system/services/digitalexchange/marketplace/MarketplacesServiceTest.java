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
package org.entando.entando.aps.system.services.digitalexchange.marketplace;

import com.agiletec.aps.system.SystemConstants;
import com.agiletec.aps.system.services.baseconfig.ConfigInterface;
import java.util.List;
import org.entando.entando.aps.system.services.digitalexchange.marketplace.model.Marketplace;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.entando.entando.aps.system.exception.RestRourceNotFoundException;
import org.entando.entando.web.common.exceptions.ValidationConflictException;
import org.entando.entando.web.digitalexchange.marketplace.MarketplaceValidator;
import static org.mockito.Mockito.when;
import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertNotNull;
import static junit.framework.TestCase.assertTrue;

public class MarketplacesServiceTest {

    @Mock
    private ConfigInterface configManager;

    @InjectMocks
    private MarketplacesServiceImpl service;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        mockValidMarketplacesConfig();
    }

    @Test
    public void shouldReturnMarketplaces() {

        List<Marketplace> marketplaces = service.getMarketplaces();

        assertNotNull(marketplaces);
        assertEquals(1, marketplaces.size());
        assertEquals("Marketplace 1", marketplaces.get(0).getName());
        assertEquals("http://www.entando.com/", marketplaces.get(0).getUrl());
    }

    @Test
    public void shouldReturnEmptyList() {

        // invalid configuration
        when(configManager.getConfigItem(SystemConstants.CONFIG_ITEM_MARKETPLACES))
                .thenReturn("");

        List<Marketplace> marketplaces = service.getMarketplaces();
        assertNotNull(marketplaces);
        assertTrue(marketplaces.isEmpty());

        // Restore valid configuration mocking for other tests
        mockValidMarketplacesConfig();
    }

    @Test
    public void shouldFindMarketplace() {
        Marketplace marketplace = service.findByName("Marketplace 1");
        assertNotNull(marketplace);
    }

    @Test(expected = RestRourceNotFoundException.class)
    public void shouldFindNothing() {
        service.findByName("Inexistent Marketplace");
    }

    @Test
    public void shouldAddMarketplace() {
        String newMarketplaceName = "Marketplace 2";
        Marketplace marketplace = service.create(getMarketplace(newMarketplaceName));
        assertNotNull(marketplace);
        assertEquals(newMarketplaceName, marketplace.getName());
    }

    @Test(expected = ValidationConflictException.class)
    public void shouldFailAddingMarketplace() {
        try {
            String newMarketplaceName = "Marketplace 1";
            Marketplace marketplace = service.create(getMarketplace(newMarketplaceName));
            assertNotNull(marketplace);
        } catch (ValidationConflictException ex) {
            assertEquals(1, ex.getBindingResult().getErrorCount());
            assertEquals(MarketplaceValidator.ERRCODE_MARKETPLACE_ALREADY_EXISTS,
                    ex.getBindingResult().getAllErrors().get(0).getCode());
            throw ex;
        }
    }

    @Test
    public void shouldUpdateMarketplace() {
        String marketplaceName = "Marketplace 1";
        Marketplace marketplace = getMarketplace(marketplaceName);
        String url = "http://www.entando.com";
        marketplace.setUrl(url);
        Marketplace updated = service.update(marketplace);
        assertNotNull(updated);
        assertEquals(marketplace.getName(), updated.getName());
        assertEquals(url, updated.getUrl());
    }

    @Test(expected = RestRourceNotFoundException.class)
    public void shouldFailUpdatingMarketplace() {
        service.update(getMarketplace("Inexistent Marketplace"));
    }

    @Test
    public void shouldDeleteMarketplace() {
        service.delete("Marketplace 1");
    }

    @Test(expected = RestRourceNotFoundException.class)
    public void shouldFailDeletingMarketplace() {
        service.delete("Inexistent Marketplace");
    }

    private void mockValidMarketplacesConfig() {

        when(configManager.getConfigItem(SystemConstants.CONFIG_ITEM_MARKETPLACES))
                .thenReturn("<marketplaces>"
                        + "  <marketplace>"
                        + "    <name>Marketplace 1</name>"
                        + "    <url>http://www.entando.com/</url>"
                        + "  </marketplace>"
                        + "</marketplaces>");
    }

    private Marketplace getMarketplace(String name) {
        Marketplace marketplace = new Marketplace();
        marketplace.setName(name);
        return marketplace;
    }
}
