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
package org.entando.entando.aps.system.services.digitalexchange.client;

import java.net.SocketTimeoutException;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;
import org.entando.entando.aps.system.services.RequestListProcessor;
import org.entando.entando.aps.system.services.digitalexchange.DigitalExchangesManager;
import org.entando.entando.aps.system.services.digitalexchange.model.ResilientPagedMetadata;
import org.entando.entando.web.common.model.PagedMetadata;
import org.entando.entando.web.common.model.PagedRestResponse;
import org.entando.entando.web.common.model.RestListRequest;
import org.entando.entando.aps.system.services.digitalexchange.model.ResilientListWrapper;
import org.entando.entando.web.common.model.RestError;
import org.entando.entando.web.common.model.Filter;
import org.entando.entando.web.common.model.SimpleRestResponse;
import org.entando.entando.web.common.model.RestResponse;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.security.oauth2.common.exceptions.OAuth2Exception;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.InjectMocks;

import static org.mockito.Mockito.when;
import static org.mockito.ArgumentMatchers.any;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.entando.entando.aps.system.services.digitalexchange.client.DigitalExchangesClientImpl.*;

public class DigitalExchangesClientTest {

    @Mock
    private DigitalExchangesManager manager;

    @Mock
    private MessageSource messageSource;

    @InjectMocks
    private DigitalExchangesClientImpl client;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        DigitalExchangesMocker mocker = new DigitalExchangesMocker()
                .addDigitalExchange("DE 1", buildWorkingDE("A", "C"))
                .addDigitalExchange("DE 2", buildWorkingDE("B", "D"))
                .addDigitalExchange("Broken DE", () -> {
                    throw new HttpServerErrorException(HttpStatus.INTERNAL_SERVER_ERROR, "This instance is broken");
                })
                .addDigitalExchange("Unreachable DE", () -> {
                    throw new ResourceAccessException("Connection refused");
                })
                .addDigitalExchange("Unreachable DE During Authentication", () -> {
                    throw new OAuth2Exception("", new ResourceAccessException("Connection refused"));
                })
                .addDigitalExchange("OAuth issue DE", () -> {
                    throw new OAuth2Exception("error retriving token");
                })
                .addDigitalExchange("Wrong Payload DE", () -> new ResponseEntity<>(HttpStatus.OK))
                .addDigitalExchange("Wrong URL DE", () -> {
                }, de -> {
                    de.setUrl("invalid-url");
                })
                .addDigitalExchange("Timeout DE", () -> {
                    throw new ResourceAccessException("", new SocketTimeoutException());
                })
                .addDigitalExchange("Disabled DE", () -> {
                }, de -> de.setActive(false));

        DigitalExchangeOAuth2RestTemplateFactory restTemplateFactory = mocker.initMocks();

        when(manager.getDigitalExchanges()).thenReturn(mocker.getFakeExchanges());
        OAuth2RestTemplate restTemplate = restTemplateFactory.createOAuth2RestTemplate(null);
        when(manager.getRestTemplate(any())).thenReturn(restTemplate);
        when(messageSource.getMessage(any(), any(), any())).thenReturn("Mocked Message");
    }

    private Function<DigitalExchangeMockedRequest, RestResponse<?, ?>> buildWorkingDE(String... values) {
        return request -> {
            switch (request.getMethod()) {
                case GET:
                    PagedMetadata<String> pagedMetadata = new PagedMetadata<>(new RestListRequest(), Arrays.asList(values), 2);
                    return new PagedRestResponse<>(pagedMetadata);
                case POST:
                    return new SimpleRestResponse<>(values[0] + ":" + request.getEntity().getBody());
            }
            return null;
        };
    }

    @Test
    public void testPaginatedResultWithGET() {

        RestListRequest request = new RestListRequest();
        request.setPageSize(2);
        request.setPage(1);

        ResilientPagedMetadata<String> pagedMetadata
                = client.getCombinedResult(new TestPagedDigitalExchangeCall(request));

        assertEquals(2, pagedMetadata.getBody().size());
        assertEquals("A", pagedMetadata.getBody().get(0));
        assertEquals("B", pagedMetadata.getBody().get(1));
        assertEquals(4, pagedMetadata.getTotalItems());
        verifyResilience(pagedMetadata.getErrors());

        request.setPage(2);

        pagedMetadata = client.getCombinedResult(new TestPagedDigitalExchangeCall(request));

        assertEquals(2, pagedMetadata.getBody().size());
        assertEquals("C", pagedMetadata.getBody().get(0));
        assertEquals("D", pagedMetadata.getBody().get(1));
        assertEquals(4, pagedMetadata.getTotalItems());
        verifyResilience(pagedMetadata.getErrors());
    }

    @Test
    public void testSimpleResultWithPOST() {

        SimpleDigitalExchangeCall<String> postCall = new SimpleDigitalExchangeCall<>(
                HttpMethod.POST, new ParameterizedTypeReference<SimpleRestResponse<String>>() {
        }, "test");
        postCall.setEntity(new HttpEntity<>("entity"));

        ResilientListWrapper<String> result = client.getCombinedResult(postCall);

        assertEquals(2, result.getList().size());
        assertTrue(result.getList().containsAll(Arrays.asList("A:entity", "B:entity")));
        verifyResilience(result.getErrors());
    }

    private <T> void verifyResilience(List<RestError> errors) {
        assertEquals(6, errors.stream()
                .map(e -> e.getCode()).distinct().count());

        assertTrue(errors.stream().map(e -> e.getCode()).allMatch(code
                -> Arrays.asList(ERRCODE_DE_HTTP_ERROR, ERRCODE_DE_UNREACHABLE,
                        ERRCODE_DE_INVALID_URL, ERRCODE_DE_WRONG_PAYLOAD,
                        ERRCODE_DE_TIMEOUT, ERRCODE_DE_AUTH).
                        contains(code)));
    }

    private static class TestPagedDigitalExchangeCall extends PagedDigitalExchangeCall<String> {

        public TestPagedDigitalExchangeCall(RestListRequest restListRequest) {
            super(restListRequest, new ParameterizedTypeReference<PagedRestResponse<String>>() {
            }, "test");
        }

        @Override
        protected RequestListProcessor<String> getRequestListProcessor(RestListRequest request, List<String> joinedList) {

            return new RequestListProcessor<String>(request, joinedList) {

                @Override
                protected Function<Filter, Predicate<String>> getPredicates() {
                    return null;
                }

                @Override
                protected Function<String, Comparator<String>> getComparators() {
                    return sort -> String.CASE_INSENSITIVE_ORDER;
                }
            };
        }
    }
}
