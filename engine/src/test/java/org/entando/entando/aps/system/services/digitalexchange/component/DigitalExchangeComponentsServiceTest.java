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

import java.util.Arrays;
import org.entando.entando.web.common.model.RestListRequest;
import org.entando.entando.web.digitalexchange.component.DigitalExchangeComponent;
import org.entando.entando.aps.system.services.digitalexchange.DigitalExchangesService;
import org.entando.entando.web.common.model.Filter;
import org.entando.entando.web.common.model.ResilientPagedMetadata;
import org.springframework.web.client.RestTemplate;
import org.springframework.context.MessageSource;
import org.springframework.web.client.RestClientException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertArrayEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.entando.entando.aps.system.services.digitalexchange.component.DigitalExchangeComponentsServiceImpl.*;

public class DigitalExchangeComponentsServiceTest {

    @Mock
    private DigitalExchangesService digitalExchangesService;

    @Mock
    private RestTemplate restTemplate;

    @Mock
    private MessageSource messageSource;

    @InjectMocks
    private DigitalExchangeComponentsServiceImpl service;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        DigitalExchangesMocker mocker
                = DigitalExchangesMocker.initRestTemplateMocks(restTemplate);

        mocker.addDigitalExchange("Unreachable DE",
                () -> {
                    throw new RestClientException("Connection refused");
                })
                .addDigitalExchange("Wrong Payload DE", () -> new ResponseEntity(HttpStatus.OK))
                .addDigitalExchange("Wrong URL DE", "invalid-url", () -> null);

        when(digitalExchangesService.getDigitalExchanges())
                .thenReturn(mocker.getFakeExchanges());

        when(messageSource.getMessage(any(), any(), any())).thenReturn("Mocked Message");
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
        assertEquals(DigitalExchangesMocker.getTotalComponentsCount(), pagedMetadata.getTotalItems());
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
        assertEquals(4, pagedMetadata.getErrors().size());

        assertTrue(pagedMetadata.getErrors().stream().map(e -> e.getCode()).allMatch(code
                -> Arrays.asList(ERRCODE_DE_HTTP_ERROR, ERRCODE_DE_UNREACHABLE,
                        ERRCODE_DE_INVALID_URL, ERRCODE_DE_WRONG_PAYLOAD).
                        contains(code)));
    }
}
