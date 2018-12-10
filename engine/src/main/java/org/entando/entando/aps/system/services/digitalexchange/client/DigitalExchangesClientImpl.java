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

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import javax.annotation.PreDestroy;
import org.entando.entando.aps.system.services.digitalexchange.DigitalExchangesService;
import org.entando.entando.aps.system.services.digitalexchange.model.DigitalExchange;
import org.entando.entando.web.common.model.RestError;
import org.entando.entando.web.common.model.RestResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;

/**
 * Implementation notes. We want to be able to configure a possibly different
 * timeout for each DE instance. Currently this implementation uses the Spring
 * RestTemplate class for performing HTTP calls. RestTemplate doesn't provide a
 * way for configuring a different timeout for each call (it can only be
 * configured globally), however it is possible to define a timeout using the
 * CompletableFuture class. Currently it is necessary to use CompletableFuture
 * combined with a ScheduledExecutorService to achieve this. Starting from Java
 * 9 it will be possible to simplify the code using the new orTimeout() method
 * of CompletableFuture. Moreover the RestTemplate class could be deprecated in
 * the future in favor of the new WebClient (this will imply adding Spring
 * WebFlux dependency to the project).
 */
@Component
public class DigitalExchangesClientImpl implements DigitalExchangesClient {

    private static final Logger logger = LoggerFactory.getLogger(DigitalExchangesClientImpl.class);

    public static final String ERRCODE_DE_HTTP_ERROR = "1";
    public static final String ERRCODE_DE_UNREACHABLE = "2";
    public static final String ERRCODE_DE_TIMEOUT = "3";
    public static final String ERRCODE_DE_INVALID_URL = "4";
    public static final String ERRCODE_DE_WRONG_PAYLOAD = "5";

    private static final int DEFAULT_TIMEOUT = 10000;

    private final DigitalExchangesService digitalExchangesService;
    private final RestTemplate restTemplate;
    private final MessageSource messageSource;
    private final ScheduledExecutorService timeoutService;

    @Autowired
    public DigitalExchangesClientImpl(DigitalExchangesService digitalExchangesService,
            RestTemplate restTemplate, MessageSource messageSource) {
        this.digitalExchangesService = digitalExchangesService;
        this.restTemplate = restTemplate;
        this.messageSource = messageSource;
        timeoutService = Executors.newSingleThreadScheduledExecutor();
    }

    @Override
    public <R extends RestResponse, C> C getCombinedResult(DigitalExchangeCall<R, C> call) {
        List<R> allResults = queryAllDigitalExchanges(call);
        return call.combineResults(allResults);
    }

    private <R extends RestResponse, C> List<R> queryAllDigitalExchanges(DigitalExchangeCall<R, C> call) {

        @SuppressWarnings("unchecked")
        CompletableFuture<R>[] futureResults
                = digitalExchangesService.getDigitalExchanges()
                        .stream()
                        .filter(de -> de.isActive())
                        .map(de -> getResponseOrTimeout(de, call))
                        .toArray(CompletableFuture[]::new);

        return CompletableFuture.allOf(futureResults)
                .thenApply(v -> {
                    return Arrays.stream(futureResults)
                            .map(CompletableFuture::join)
                            .collect(Collectors.toList());
                }).join();
    }

    private <R extends RestResponse, C> CompletableFuture getResponseOrTimeout(DigitalExchange digitalExchange, DigitalExchangeCall<R, C> call) {
        CompletableFuture<R> responseFuture = CompletableFuture.supplyAsync(() -> getResponse(digitalExchange, call));
        CompletableFuture<R> timeoutFuture = getTimeoutFuture(digitalExchange, call);

        CompletableFuture result = CompletableFuture.anyOf(responseFuture, timeoutFuture);

        result.thenRun(() -> {
            if (timeoutFuture.isDone()) {
                logger.error("Error calling {}. Timeout after {} milliseconds", digitalExchange.getUrl(), digitalExchange.getTimeout());
                responseFuture.cancel(true);
            }
        });

        return result;
    }

    private <R extends RestResponse, C> R getResponse(DigitalExchange digitalExchange, DigitalExchangeCall<R, C> call) {

        String url;

        try {
            url = call.getURL(digitalExchange);
        } catch (IllegalArgumentException ex) {
            logger.error("Error calling {}. Invalid URL", digitalExchange.getUrl());
            return getErrorResponse(call, ERRCODE_DE_INVALID_URL, "digitalExchange.invalidUrl", digitalExchange.getName(), digitalExchange.getUrl());
        }

        try {
            ResponseEntity<R> responseEntity = restTemplate
                    .exchange(url, call.getMethod(), call.getEntity(), call.getParameterizedTypeReference());

            R response = responseEntity.getBody();

            if (call.isResponseParsable(response)) {
                return response;
            } else {
                logger.error("Error calling {}. Unable to parse response", url);
                return getErrorResponse(call, ERRCODE_DE_WRONG_PAYLOAD, "digitalExchange.unparsableResponse", digitalExchange.getName());
            }

        } catch (RestClientResponseException ex) { // Error response
            logger.error("Error calling {}. Status code: {}", url, ex.getRawStatusCode());
            return getErrorResponse(call, ERRCODE_DE_HTTP_ERROR, "digitalExchange.httpError", digitalExchange.getName(), ex.getRawStatusCode());

        } catch (RestClientException ex) { // Other (e.g. unknown host)
            logger.error("Error calling {}. Exception message: {}", url, ex.getMessage());
            return getErrorResponse(call, ERRCODE_DE_UNREACHABLE, "digitalExchange.unreachable", digitalExchange.getName());

        } catch (Throwable t) {
            logger.error("Error calling {}", url);
            logger.error("Unexpected exception", t);
            throw t;
        }
    }

    private <R extends RestResponse, C> R getErrorResponse(DigitalExchangeCall<R, C> call, String errorCode, String msgCode, Object... msgParams) {
        R errorResponse = call.getEmptyRestResponse();
        String errorMessage = messageSource.getMessage(msgCode, msgParams, null);
        errorResponse.addError(new RestError(errorCode, errorMessage));
        return errorResponse;
    }

    private <R extends RestResponse, C> CompletableFuture<R> getTimeoutFuture(DigitalExchange digitalExchange, DigitalExchangeCall<R, C> call) {

        int timeout = digitalExchange.getTimeout() > 0 ? digitalExchange.getTimeout() : DEFAULT_TIMEOUT;

        CompletableFuture<R> result = new CompletableFuture<>();

        timeoutService.schedule(() -> result.complete(
                getErrorResponse(call, ERRCODE_DE_TIMEOUT, "digitalExchange.timeout",
                        digitalExchange.getName(), digitalExchange.getTimeout())),
                timeout, TimeUnit.MILLISECONDS);

        return result;
    }

    @PreDestroy
    public void onDestroy() {
        timeoutService.shutdown();
    }
}
