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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;
import org.entando.entando.aps.system.services.digitalexchange.model.DigitalExchange;
import org.entando.entando.web.common.model.RestResponse;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Helper class that can be used to fake the behavior of a set of a DE instances
 * in a consistent way.
 *
 * Example:
 * <pre>
 * new DigitalExchangesMocker()
 *        // Simulate a hello response
 *        .addDigitalExchange("DE 1", request -> {
 *            // here you can obtain information about the mocked request (HTTP
 *            // method, URL parameters and payload) from the request variable
 *            return new SimpleRestResponse<>("hello");
 *        })
 *        // Simulate an broken server
 *        .addDigitalExchange("Unreachable DE", () -> {
 *            throw new RestClientException("Connection refused");
 *        })
 *        .initMocks();
 * </pre>
 */
public class DigitalExchangesMocker {

    private final RestTemplate restTemplate;
    private final List<DigitalExchange> exchanges;
    private final Map<String, Function<DigitalExchangeMockedRequest, RestResponse>> responsesMap;

    public DigitalExchangesMocker() {
        this(mock(RestTemplate.class));
    }

    public DigitalExchangesMocker(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
        this.exchanges = new ArrayList<>();
        this.responsesMap = new HashMap<>();
    }

    public RestTemplate getRestTemplate() {
        return restTemplate;
    }

    public List<DigitalExchange> getFakeExchanges() {
        return exchanges;
    }

    private DigitalExchange getDigitalExchange(String name) {
        return getDigitalExchange(name, String.format("https://de%s.entando.com/", exchanges.size() + 1));
    }

    private DigitalExchange getDigitalExchange(String name, String url) {
        DigitalExchange digitalExchange = new DigitalExchange();
        digitalExchange.setName(name);
        digitalExchange.setUrl(url);
        digitalExchange.setTimeout(500);
        digitalExchange.setActive(true);
        return digitalExchange;
    }

    public DigitalExchangesMocker addDigitalExchange(String name, RestResponse result) {
        return addDigitalExchange(name, (request) -> result, null);
    }

    public DigitalExchangesMocker addDigitalExchange(String name, Runnable runnable) {
        return addDigitalExchange(name, runnable, null);
    }

    public DigitalExchangesMocker addDigitalExchange(String name, Runnable runnable, Consumer<DigitalExchange> deConsumer) {
        return addDigitalExchange(name, (request) -> {
            runnable.run();
            return null;
        }, deConsumer);
    }

    public DigitalExchangesMocker addDigitalExchange(String name, Function<DigitalExchangeMockedRequest, RestResponse> function) {
        return addDigitalExchange(name, function, null);
    }

    public DigitalExchangesMocker addDigitalExchange(String name, Function<DigitalExchangeMockedRequest, RestResponse> function, Consumer<DigitalExchange> deConsumer) {

        DigitalExchange digitalExchange = getDigitalExchange(name);
        if (deConsumer != null) {
            deConsumer.accept(digitalExchange);
        }
        exchanges.add(digitalExchange);

        responsesMap.put(digitalExchange.getUrl(), function);

        return this;
    }

    public RestTemplate initMocks() {

        when(restTemplate.exchange(any(String.class), any(HttpMethod.class), any(), any(ParameterizedTypeReference.class)))
                .thenAnswer(invocation -> {

                    String url = invocation.getArgument(0);
                    String baseUrl = getBaseUrl(url);
                    Map<String, String> urlParameters = getURLParameters(url);

                    DigitalExchangeMockedRequest request = new DigitalExchangeMockedRequest()
                            .setMethod(invocation.getArgument(1))
                            .setUrlParams(urlParameters)
                            .setEntity(invocation.getArgument(2));

                    return ResponseEntity.ok(responsesMap.get(baseUrl).apply(request));
                });

        return restTemplate;
    }

    private String getBaseUrl(String url) {
        return exchanges.stream().map(ex -> ex.getUrl())
                .filter(baseUrl -> url.startsWith(baseUrl)).findFirst().get();
    }

    private Map<String, String> getURLParameters(String url) {
        Map<String, String> map = new HashMap<>();
        int questionMarkPosition = url.indexOf("?");
        if (questionMarkPosition != -1) {
            String query = url.substring(questionMarkPosition + 1);
            for (String queryPart : query.split("&")) {
                String[] split = queryPart.split("=");
                map.put(split[0], split[1]);
            }
        }
        return map;
    }
}
