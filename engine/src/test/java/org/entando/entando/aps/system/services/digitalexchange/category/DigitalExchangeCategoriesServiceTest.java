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
package org.entando.entando.aps.system.services.digitalexchange.category;

import com.agiletec.aps.system.SystemConstants;
import com.agiletec.aps.system.services.baseconfig.ConfigInterface;
import java.util.Arrays;
import java.util.List;
import org.entando.entando.aps.system.services.digitalexchange.client.DigitalExchangesClient;
import org.entando.entando.aps.system.services.digitalexchange.client.DigitalExchangesClientMocker;
import org.entando.entando.aps.system.services.digitalexchange.model.ResilientListWrapper;
import org.entando.entando.web.common.model.SimpleRestResponse;
import org.junit.Before;
import org.junit.Test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;

public class DigitalExchangeCategoriesServiceTest {

    private ConfigInterface configManager;
    private DigitalExchangesClientMocker clientMocker;
    private DigitalExchangesClient client;

    private DigitalExchangeCategoriesService service;

    @Before
    public void setUp() {
        initClientMocks();
        configManager = mock(ConfigInterface.class);
        service = new DigitalExchangeCategoriesServiceImpl(configManager, clientMocker.getMessageSource(), client);
    }

    @Test
    public void shouldGetCategories() {

        when(configManager.getConfigItem(SystemConstants.CONFIG_ITEM_DIGITAL_EXCHANGE_CATEGORIES))
                .thenReturn("pageModels,fragments,widgets,contentModels,contentTypes");

        ResilientListWrapper<String> result = service.getCategories();

        assertTrue(result.getErrors().isEmpty());
        assertEquals(3, result.getList().size());
        assertTrue(result.getList().containsAll(Arrays.asList("pageModels", "fragments", "widgets")));
    }

    @Test
    public void shouldReturnErrorForMissingCategoriesConfig() {

        when(configManager.getConfigItem(SystemConstants.CONFIG_ITEM_DIGITAL_EXCHANGE_CATEGORIES))
                .thenReturn(null);

        ResilientListWrapper<String> result = service.getCategories();

        assertEquals(1, result.getErrors().size());
        assertTrue(result.getList().isEmpty());
    }

    private void initClientMocks() {
        List<String> de1Categories = Arrays.asList("pageModels", "fragments", "unsupportedType1");
        List<String> de2Categories = Arrays.asList("pageModels", "widgets", "unsupportedType2");

        clientMocker = new DigitalExchangesClientMocker();
        clientMocker.getDigitalExchangesMocker()
                .addDigitalExchange("DE 1", new SimpleRestResponse<>(de1Categories))
                .addDigitalExchange("DE 1", new SimpleRestResponse<>(de2Categories));

        client = clientMocker.build();
    }
}
