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
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;
import org.entando.entando.aps.system.services.digitalexchange.DigitalExchangesManager;
import org.entando.entando.aps.system.services.digitalexchange.model.DigitalExchange;
import org.entando.entando.web.common.model.RestError;
import org.entando.entando.web.common.model.RestResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.common.exceptions.OAuth2Exception;
import org.springframework.stereotype.Component;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClientResponseException;

@Component
public class DigitalExchangesClientImpl implements DigitalExchangesClient {

    private static final Logger logger = LoggerFactory.getLogger(DigitalExchangesClientImpl.class);

    public static final String ERRCODE_DE_HTTP_ERROR = "1";
    public static final String ERRCODE_DE_UNREACHABLE = "2";
    public static final String ERRCODE_DE_TIMEOUT = "3";
    public static final String ERRCODE_DE_INVALID_URL = "4";
    public static final String ERRCODE_DE_WRONG_PAYLOAD = "5";
    public static final String ERRCODE_DE_AUTH = "6";

    private final DigitalExchangesManager digitalExchangesManager;
    private final MessageSource messageSource;

    @Autowired
    public DigitalExchangesClientImpl(DigitalExchangesManager digitalExchangesManager,
            MessageSource messageSource) {
        this.digitalExchangesManager = digitalExchangesManager;
        this.messageSource = messageSource;
    }

    @Override
    public <R extends RestResponse<?, ?>, C> C getCombinedResult(DigitalExchangeCall<R, C> call) {
        List<R> allResults = queryAllDigitalExchanges(call);
        return call.combineResults(allResults);
    }

    private <R extends RestResponse<?, ?>, C> List<R> queryAllDigitalExchanges(DigitalExchangeCall<R, C> call) {

        @SuppressWarnings("unchecked")
        CompletableFuture<R>[] futureResults
                = digitalExchangesManager.getDigitalExchanges()
                        .stream()
                        .filter(de -> de.isActive())
                        .map(de -> CompletableFuture.supplyAsync(() -> getResponse(de, call)))
                        .toArray(CompletableFuture[]::new);

        return CompletableFuture.allOf(futureResults)
                .thenApply(v -> {
                    return Arrays.stream(futureResults)
                            .map(CompletableFuture::join)
                            .collect(Collectors.toList());
                }).join();
    }

    private <R extends RestResponse<?, ?>, C> R getResponse(DigitalExchange digitalExchange, DigitalExchangeCall<R, C> call) {

        String url;

        try {
            url = call.getURL(digitalExchange);
        } catch (IllegalArgumentException ex) {
            logger.error("Error calling {}. Invalid URL", digitalExchange.getUrl());
            return getErrorResponse(call, ERRCODE_DE_INVALID_URL, "digitalExchange.invalidUrl", digitalExchange.getName(), digitalExchange.getUrl());
        }

        try {
            ResponseEntity<R> responseEntity = digitalExchangesManager.getRestTemplate(digitalExchange.getName())
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

        } catch (ResourceAccessException ex) { // Other (e.g. unknown host)

            return manageResourceAccessException(call, url, digitalExchange, ex);

        } catch (OAuth2Exception ex) {

            if (ex.getCause() != null && ex.getCause() instanceof ResourceAccessException) { // Server down, unknown host, ...
                return manageResourceAccessException(call, url, digitalExchange, (ResourceAccessException) ex.getCause());
            } else {
                logger.error("Error calling {}. Exception message: {}", url, ex.getMessage());
                return getErrorResponse(call, ERRCODE_DE_AUTH, "digitalExchange.oauth2Error", digitalExchange.getName());
            }

        } catch (Throwable t) {
            logger.error("Error calling {}", url);
            logger.error("Unexpected exception", t);
            throw t;
        }
    }

    private <R extends RestResponse<?, ?>, C> R manageResourceAccessException(DigitalExchangeCall<R, C> call, String url, DigitalExchange digitalExchange, ResourceAccessException ex) {

        if (ex.getCause() != null && ex.getCause() instanceof SocketTimeoutException) {
            logger.error("Timeout calling {}", url);

            return getErrorResponse(call, ERRCODE_DE_TIMEOUT, "digitalExchange.timeout",
                    digitalExchange.getName(), digitalExchange.getTimeout());
        }

        logger.error("Error calling {}. Exception message: {}", url, ex.getMessage());
        return getErrorResponse(call, ERRCODE_DE_UNREACHABLE, "digitalExchange.unreachable", digitalExchange.getName());
    }

    private <R extends RestResponse<?, ?>, C> R getErrorResponse(DigitalExchangeCall<R, C> call, String errorCode, String msgCode, Object... msgParams) {
        R errorResponse = call.getEmptyRestResponse();
        String errorMessage = messageSource.getMessage(msgCode, msgParams, null);
        errorResponse.addError(new RestError(errorCode, errorMessage));
        return errorResponse;
    }
}
