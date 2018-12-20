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
import java.util.stream.Collectors;
import org.entando.entando.aps.system.services.digitalexchange.DigitalExchangesManager;
import org.entando.entando.aps.system.services.digitalexchange.model.DigitalExchange;
import org.entando.entando.web.common.model.RestResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.stereotype.Component;

@Component
public class DigitalExchangesClientImpl implements DigitalExchangesClient {

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
                        .map(de -> CompletableFuture.supplyAsync(() -> getSingleResponse(de, call)))
                        .toArray(CompletableFuture[]::new);

        return CompletableFuture.allOf(futureResults)
                .thenApply(v -> {
                    return Arrays.stream(futureResults)
                            .map(CompletableFuture::join)
                            .collect(Collectors.toList());
                }).join();
    }

    @Override
    public <R extends RestResponse<?, ?>, C> R getSingleResponse(DigitalExchange digitalExchange, DigitalExchangeCall<R, C> call) {
        OAuth2RestTemplate restTemplate = digitalExchangesManager.getRestTemplate(digitalExchange.getName());
        return new DigitalExchangeCallExecutor<>(messageSource, digitalExchange, restTemplate, call).getResponse();
    }

    @Override
    public <R extends RestResponse<?, ?>, C> R getSingleResponse(String digitalExchangeName, DigitalExchangeCall<R, C> call) {
        return getSingleResponse(digitalExchangesManager.findByName(digitalExchangeName).get(), call);
    }
}
