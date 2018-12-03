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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.stream.Collectors;
import org.entando.entando.aps.system.services.digitalexchange.model.DigitalExchange;
import org.entando.entando.web.common.model.PagedRestResponse;
import org.entando.entando.web.common.model.ResilientPagedMetadata;
import org.entando.entando.web.digitalexchange.component.DigitalExchangeComponent;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.mock;

public class DigitalExchangesMocker {

    private static final String[] COMPONENTS_1 = new String[]{"A", "B", "C", "F", "I", "M", "N", "P"};
    private static final String[] COMPONENTS_2 = new String[]{"D", "E", "G", "H", "L", "O"};

    private final RestTemplate restTemplate;
    private final List<DigitalExchange> exchanges;
    private final Map<String, List<DigitalExchangeComponent>> componentsMap;
    private final Map<String, Callable<ResponseEntity<PagedRestResponse<DigitalExchangeComponent>>>> brokenInstances;

    private DigitalExchangesMocker(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
        this.exchanges = new ArrayList<>();
        componentsMap = new HashMap<>();
        brokenInstances = new HashMap<>();
    }

    public static RestTemplate getRestMockedRestTemplate() {
        RestTemplate restTemplate = mock(RestTemplate.class);
        initRestTemplateMocks(restTemplate);
        return restTemplate;
    }

    public static DigitalExchangesMocker initRestTemplateMocks(RestTemplate restTemplate) {
        DigitalExchangesMocker mocker = new DigitalExchangesMocker(restTemplate)
                .addDigitalExchange("DE 1", COMPONENTS_1)
                .addDigitalExchange("DE 2", COMPONENTS_2)
                // Broken instance
                .addDigitalExchange("Broken DE",
                        () -> {
                            throw new HttpServerErrorException(HttpStatus.INTERNAL_SERVER_ERROR, "This instance is broken");
                        });
        mocker.initMocks();
        return mocker;
    }

    public List<DigitalExchange> getFakeExchanges() {
        return exchanges;
    }

    public static int getTotalComponentsCount() {
        return COMPONENTS_1.length + COMPONENTS_2.length;
    }

    private DigitalExchange getDigitalExchange(String name) {
        return getDigitalExchange(name, String.format("https://de%s.entando.com/", exchanges.size() + 1));
    }

    private DigitalExchange getDigitalExchange(String name, String url) {
        DigitalExchange digitalExchange = new DigitalExchange();
        digitalExchange.setName(name);
        digitalExchange.setUrl(url);
        return digitalExchange;
    }

    public DigitalExchangesMocker addDigitalExchange(String name, String... componentNames) {
        DigitalExchange digitalExchange = getDigitalExchange(name);
        exchanges.add(digitalExchange);
        componentsMap.put(digitalExchange.getUrl(), getDigitalExchangeComponents(componentNames));
        return this;
    }

    public DigitalExchangesMocker addDigitalExchange(String name, Callable<ResponseEntity<PagedRestResponse<DigitalExchangeComponent>>> callable) {
        DigitalExchange digitalExchange = getDigitalExchange(name);
        exchanges.add(digitalExchange);
        brokenInstances.put(digitalExchange.getUrl(), callable);
        return this;
    }

    public DigitalExchangesMocker addDigitalExchange(String name, String url, Callable<ResponseEntity<PagedRestResponse<DigitalExchangeComponent>>> callable) {
        DigitalExchange digitalExchange = getDigitalExchange(name, url);
        exchanges.add(digitalExchange);
        brokenInstances.put(digitalExchange.getUrl(), callable);
        return this;
    }

    private List<DigitalExchangeComponent> getDigitalExchangeComponents(String... names) {
        return Arrays.asList(names).stream().map(name -> {
            DigitalExchangeComponent component = new DigitalExchangeComponent();
            component.setName(name);
            return component;
        }).collect(Collectors.toList());
    }

    private RestTemplate initMocks() {

        when(restTemplate.exchange(any(String.class), eq(HttpMethod.GET), eq(null), any(ParameterizedTypeReference.class)))
                .thenAnswer(invocation -> {
                    String url = invocation.getArgument(0);
                    String baseUrl = getBaseUrl(url);

                    if (brokenInstances.containsKey(baseUrl)) {
                        return brokenInstances.get(baseUrl).call();
                    }

                    List<DigitalExchangeComponent> components = componentsMap.get(baseUrl);

                    Map<String, String> urlParameters = getURLParameters(url);
                    int pageSize = Integer.parseInt(urlParameters.get("pageSize"));
                    String nameFilter = urlParameters.get("filters[0].value");

                    return getFakeResponse(components, pageSize, nameFilter);
                });

        return restTemplate;
    }

    private String getBaseUrl(String url) {
        return exchanges.stream().map(ex -> ex.getUrl())
                .filter(baseUrl -> url.startsWith(baseUrl)).findFirst().get();
    }

    private Map<String, String> getURLParameters(String url) {
        int questionMarkPosition = url.indexOf("?");
        String query = url.substring(questionMarkPosition + 1);
        Map<String, String> map = new HashMap<>();
        for (String queryPart : query.split("&")) {
            String[] split = queryPart.split("=");
            map.put(split[0], split[1]);
        }
        return map;
    }

    private ResponseEntity<PagedRestResponse<DigitalExchangeComponent>> getFakeResponse(List<DigitalExchangeComponent> components, int pageSize, String nameFilter) {
        ResilientPagedMetadata<DigitalExchangeComponent> pagedMetadata = new ResilientPagedMetadata<>();

        List<DigitalExchangeComponent> list;
        if (nameFilter == null) {
            list = components.subList(0, Math.min(pageSize, components.size()));
            pagedMetadata.setTotalItems(components.size());
        } else {
            list = components.stream().filter(c -> c.getName().equals(nameFilter)).collect(Collectors.toList());
            pagedMetadata.setTotalItems(list.size());
        }

        pagedMetadata.setBody(list);
        pagedMetadata.setPage(1);
        pagedMetadata.setPageSize(pageSize);
        PagedRestResponse<DigitalExchangeComponent> response = new PagedRestResponse<>(pagedMetadata);
        return ResponseEntity.ok(response);
    }
}
