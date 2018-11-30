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
package org.entando.entando.aps.system.services.digitalexchange.component;

import org.entando.entando.web.common.model.RestListRequest;
import org.entando.entando.web.digitalexchange.component.DigitalExchangeComponent;
import org.entando.entando.aps.system.services.digitalexchange.DigitalExchangesService;
import org.entando.entando.web.common.model.Filter;
import org.entando.entando.web.common.model.ResilientPagedMetadata;
import org.springframework.web.client.RestTemplate;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertArrayEquals;

public class DigitalExchangeComponentsServiceTest {

    @Mock
    private DigitalExchangesService digitalExchangesService;

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private DigitalExchangeComponentsServiceImpl service;

    private DigitalExchangesMocker digitalExchangesMocker;
    
    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        
        digitalExchangesMocker = new DigitalExchangesMocker(restTemplate, digitalExchangesService)
                .addDigitalExchange("DE 1", "A", "B", "C", "F", "I", "M", "N", "P")
                .addDigitalExchange("DE 2", "D", "E", "G", "H", "L", "O")
                .addDigitalExchange("Broken DE")
                .initMocks();
    }

    @Test
    public void shouldPaginate() {

        RestListRequest listRequest = new RestListRequest();
        listRequest.setPageSize(3);
        listRequest.setSort("name");

        listRequest.setPage(1);
        verifyPage(service.getComponents(listRequest), "A", "B", "C");

        listRequest.setPage(2);
        verifyPage(service.getComponents(listRequest), "D", "E", "F");

        listRequest.setPage(3);
        verifyPage(service.getComponents(listRequest), "G", "H", "I");

        listRequest.setPage(4);
        verifyPage(service.getComponents(listRequest), "L", "M", "N");

        listRequest.setPage(5);
        verifyPage(service.getComponents(listRequest), "O", "P");
    }

    private void verifyPage(ResilientPagedMetadata<DigitalExchangeComponent> pagedMetadata, String... values) {
        assertNotNull(pagedMetadata.getBody());
        assertEquals(values.length, pagedMetadata.getBody().size());
        assertEquals(digitalExchangesMocker.getTotalComponentsCount(), pagedMetadata.getTotalItems());
        assertArrayEquals(pagedMetadata.getBody().stream()
                .map(c -> c.getName()).toArray(String[]::new), values);

        verifyResilience(pagedMetadata);
    }

    @Test
    public void shouldFilter() {

        RestListRequest listRequest = new RestListRequest();

        Filter filter = new Filter();
        filter.setAttribute("name");
        filter.setValue("M");
        listRequest.addFilter(filter);

        ResilientPagedMetadata<DigitalExchangeComponent> pagedMetadata = service.getComponents(listRequest);

        assertNotNull(pagedMetadata.getBody());
        assertEquals(1, pagedMetadata.getBody().size());
        assertEquals("M", pagedMetadata.getBody().get(0).getName());
        assertEquals(1, pagedMetadata.getTotalItems());
    }

    private void verifyResilience(ResilientPagedMetadata<DigitalExchangeComponent> pagedMetadata) {
        assertEquals(1, pagedMetadata.getErrors().size());
    }
}
