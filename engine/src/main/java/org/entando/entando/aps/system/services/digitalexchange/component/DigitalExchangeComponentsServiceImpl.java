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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;
import org.entando.entando.aps.system.services.RequestListProcessor;
import org.entando.entando.aps.system.services.digitalexchange.DigitalExchangesService;
import org.entando.entando.aps.system.services.digitalexchange.model.DigitalExchange;
import org.entando.entando.web.common.RestListRequestUriBuilder;
import org.entando.entando.web.common.model.PagedMetadata;
import org.entando.entando.web.common.model.PagedRestResponse;
import org.entando.entando.web.common.model.ResilientPagedMetadata;
import org.entando.entando.web.common.model.RestError;
import org.entando.entando.web.common.model.RestListRequest;
import org.entando.entando.web.digitalexchange.component.DigitalExchangeComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

public class DigitalExchangeComponentsServiceImpl implements DigitalExchangeComponentsService {

    public static final String ERRCODE_DE_HTTP_ERROR = "1";
    public static final String ERRCODE_DE_EMPTY_BODY = "2";

    private static final String DE_COMPONENTS_ENDPOINT = "digitalExchange/components";

    private static final Logger logger = LoggerFactory.getLogger(DigitalExchangeComponentsServiceImpl.class);

    private final DigitalExchangesService digitalExchangesService;
    private final RestTemplate restTemplate;

    @Autowired
    public DigitalExchangeComponentsServiceImpl(DigitalExchangesService digitalExchangesService, RestTemplate restTemplate) {
        this.digitalExchangesService = digitalExchangesService;
        this.restTemplate = restTemplate;
    }

    @Override
    public ResilientPagedMetadata<DigitalExchangeComponent> getComponents(RestListRequest requestList) {

        List<PagedRestResponse<DigitalExchangeComponent>> allResults
                = queryAllDigitalExchanges(requestList);

        List<DigitalExchangeComponent> joinedList = new ArrayList<>();
        int total = 0;
        List<RestError> errors = new ArrayList<>();

        for (PagedRestResponse<DigitalExchangeComponent> response : allResults) {
            if (response.getErrors().isEmpty()) {
                joinedList.addAll(response.getPayload());
                total += response.getMetaData().getTotalItems();
            } else {
                errors.addAll(response.getErrors());
            }
        }

        joinedList = filterAndSort(joinedList, requestList);

        ResilientPagedMetadata pagedMetadata = new ResilientPagedMetadata(requestList, joinedList, total);
        pagedMetadata.setErrors(errors);

        return pagedMetadata;
    }

    private List<PagedRestResponse<DigitalExchangeComponent>> queryAllDigitalExchanges(RestListRequest requestList) {

        CompletableFuture<PagedRestResponse<DigitalExchangeComponent>>[] futureResults
                = digitalExchangesService.getDigitalExchanges()
                        .stream().map(m -> getComponents(m, requestList))
                        .toArray(CompletableFuture[]::new);

        return CompletableFuture.allOf(futureResults)
                .thenApply(v -> {
                    return Arrays.asList(futureResults).stream()
                            .map(future -> future.join())
                            .collect(Collectors.toList());
                }).join();
    }

    private List<DigitalExchangeComponent> filterAndSort(List<DigitalExchangeComponent> joinedList, RestListRequest listRequest) {

        return RequestListProcessor.fromList(joinedList, listRequest)
                .filter((attribute, value) -> {
                    switch (attribute) {
                        case "name":
                            return c -> c.getName().toLowerCase().contains(value.toLowerCase());
                        case "version":
                            return c -> c.getVersion().toLowerCase().contains(value.toLowerCase());
                        case "type":
                            return c -> c.getType().toLowerCase().contains(value.toLowerCase());
                        case "rating":
                            return c -> c.getRating() == Integer.parseInt(value);
                        case "installed":
                            return c -> c.isInstalled() == Boolean.parseBoolean(value.toLowerCase());
                    }
                    return null;
                })
                .sort(sort -> {
                    switch (sort) {
                        case "name":
                            return (a, b) -> a.getName().compareToIgnoreCase(b.getName());
                        case "lastUpdate":
                            return (a, b) -> a.getLastUpdate().compareTo(b.getLastUpdate());
                        case "version":
                            return (a, b) -> a.getVersion().compareToIgnoreCase(b.getVersion());
                        case "type":
                            return (a, b) -> a.getType().compareToIgnoreCase(b.getType());
                        case "rating":
                            return (a, b) -> Integer.compare(a.getRating(), b.getRating());
                        case "installed":
                            return (a, b) -> Boolean.compare(a.isInstalled(), b.isInstalled());
                    }
                    return null;
                })
                .toList();
    }

    @Async
    private CompletableFuture<PagedRestResponse<DigitalExchangeComponent>> getComponents(DigitalExchange digitalExchange, RestListRequest requestList) {

        PagedRestResponse<DigitalExchangeComponent> result;

        String url = getComponentsURL(digitalExchange, requestList);

        try {
            ResponseEntity<PagedRestResponse<DigitalExchangeComponent>> responseEntity = restTemplate
                    .exchange(url, HttpMethod.GET, null, new ParameterizedTypeReference<PagedRestResponse<DigitalExchangeComponent>>() {
                    });
            PagedRestResponse<DigitalExchangeComponent> response = responseEntity.getBody();
            if (response == null) {
                // TODO error msg
                result = getErrorResponse(ERRCODE_DE_EMPTY_BODY, "response == null");
            } else {
                result = response;
            }
        } catch (HttpStatusCodeException ex) {
            logger.error("Error retrieving components", ex);
            logger.error("Error calling {}. Status code {}. Server response:\n{}",
                    url, ex.getStatusCode().value(), ex.getResponseBodyAsString());
            // TODO error msg
            result = getErrorResponse(ERRCODE_DE_HTTP_ERROR, String.valueOf(ex.getStatusCode().value()));
        }

        return CompletableFuture.completedFuture(result);
    }

    private PagedRestResponse<DigitalExchangeComponent> getErrorResponse(String errorCode, String errorMessage) {
        PagedMetadata<DigitalExchangeComponent> emptyMetadata = new PagedMetadata<>();
        PagedRestResponse<DigitalExchangeComponent> errorResponse = new PagedRestResponse<>(emptyMetadata);
        errorResponse.addError(new RestError(errorCode, errorMessage));
        return errorResponse;
    }

    private String getComponentsURL(DigitalExchange digitalExchange, RestListRequest requestList) {

        RestListRequest digitalExchangeRequest = getDigitalExchangeRestListRequest(requestList);

        UriComponentsBuilder baseBuilder = UriComponentsBuilder
                .fromHttpUrl(digitalExchange.getUrl())
                .path(DE_COMPONENTS_ENDPOINT);

        return new RestListRequestUriBuilder(baseBuilder, digitalExchangeRequest).toUriString();
    }

    private RestListRequest getDigitalExchangeRestListRequest(RestListRequest userRequest) {

        RestListRequest digitalExchangeRequest = new RestListRequest();

        // we need to modify the page size
        digitalExchangeRequest.setPage(1);
        digitalExchangeRequest.setPageSize(userRequest.getPage() * userRequest.getPageSize());

        // copy the other fields
        digitalExchangeRequest.setSort(userRequest.getSort());
        digitalExchangeRequest.setDirection(userRequest.getDirection());
        digitalExchangeRequest.setFilters(userRequest.getFilters());

        return digitalExchangeRequest;
    }
}
