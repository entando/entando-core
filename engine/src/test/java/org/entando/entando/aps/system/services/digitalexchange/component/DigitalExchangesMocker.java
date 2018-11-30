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
import java.util.stream.Collectors;
import org.entando.entando.aps.system.services.digitalexchange.DigitalExchangesService;
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

public class DigitalExchangesMocker {

    private final RestTemplate restTemplate;
    private final DigitalExchangesService digitalExchangesService;

    private final List<DigitalExchange> fakeDigitalExchanges;
    private final Map<String, List<DigitalExchangeComponent>> componentsMap;
    private final List<String> brokenInstances;

    public DigitalExchangesMocker(RestTemplate restTemplate, DigitalExchangesService digitalExchangesService) {
        this.restTemplate = restTemplate;
        this.digitalExchangesService = digitalExchangesService;
        fakeDigitalExchanges = new ArrayList<>();
        componentsMap = new HashMap<>();
        brokenInstances = new ArrayList<>();
    }

    public DigitalExchangesMocker addDigitalExchange(String name, String... componentNames) {

        DigitalExchange digitalExchange = new DigitalExchange();
        digitalExchange.setName(name);
        digitalExchange.setUrl(String.format("http://de%s.entando.org", fakeDigitalExchanges.size()));

        fakeDigitalExchanges.add(digitalExchange);

        if (componentNames.length == 0) {
            brokenInstances.add(name);
        } else {
            componentsMap.put(name, getDigitalExchangeComponents(componentNames));
        }

        return this;
    }

    public DigitalExchangesMocker initMocks() {
        initDigitalExchangesServiceMocks();
        initRestTemplateMocks();
        return this;
    }

    public long getTotalComponentsCount() {
        return componentsMap.values().stream().flatMap(List::stream).count();
    }

    private void initDigitalExchangesServiceMocks() {
        when(digitalExchangesService.getDigitalExchanges())
                .thenReturn(fakeDigitalExchanges);
    }

    private void initRestTemplateMocks() {
        when(restTemplate.exchange(any(String.class), eq(HttpMethod.GET), eq(null), any(ParameterizedTypeReference.class)))
                .thenAnswer(invocation -> {
                    String url = invocation.getArgument(0);

                    DigitalExchange calledDE = digitalExchangesService
                            .getDigitalExchanges().stream()
                            .filter(de -> url.startsWith(de.getUrl()))
                            .findFirst().get();

                    if (brokenInstances.contains(calledDE.getName())) {
                        throw new HttpServerErrorException(HttpStatus.INTERNAL_SERVER_ERROR, "This instance is broken");
                    }

                    List<DigitalExchangeComponent> components = componentsMap.get(calledDE.getName());

                    int questionMarkPosition = url.indexOf("?");
                    Map<String, String> urlParameters = getURLParameters(url);
                    int pageSize = Integer.parseInt(urlParameters.get("pageSize"));
                    String nameFilter = urlParameters.get("filters[0].value");

                    return getFakeResponse(components, pageSize, nameFilter);
                });
    }

    private List<DigitalExchangeComponent> getDigitalExchangeComponents(String... names) {
        return Arrays.asList(names).stream().map(name -> {
            DigitalExchangeComponent component = new DigitalExchangeComponent();
            component.setName(name);
            return component;
        }).collect(Collectors.toList());
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
