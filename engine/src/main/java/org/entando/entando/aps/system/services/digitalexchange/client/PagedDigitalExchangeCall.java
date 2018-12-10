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
import java.util.List;
import org.entando.entando.aps.system.services.RequestListProcessor;
import org.entando.entando.aps.system.services.digitalexchange.model.DigitalExchange;
import org.entando.entando.aps.system.services.digitalexchange.model.ResilientPagedMetadata;
import org.entando.entando.web.common.model.PagedMetadata;
import org.entando.entando.web.common.model.PagedRestResponse;
import org.entando.entando.web.common.model.RestError;
import org.entando.entando.web.common.model.RestListRequest;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;

/**
 * Provides the logic for combining a set of PagedRestResponse retrieved from DE
 * instances.
 */
public abstract class PagedDigitalExchangeCall<T> extends DigitalExchangeCall<PagedRestResponse<T>, ResilientPagedMetadata<T>> {

    private final RestListRequest request;

    public PagedDigitalExchangeCall(HttpMethod method, RestListRequest restListRequest,
            ParameterizedTypeReference<PagedRestResponse<T>> parameterizedTypeReference, String... urlSegments) {
        super(method, parameterizedTypeReference, urlSegments);
        this.request = restListRequest;
    }

    public PagedDigitalExchangeCall(RestListRequest restListRequest,
            ParameterizedTypeReference<PagedRestResponse<T>> parameterizedTypeReference, String... urlSegments) {
        this(HttpMethod.GET, restListRequest, parameterizedTypeReference, urlSegments);
    }

    @Override
    protected boolean isResponseParsable(PagedRestResponse<T> response) {
        return super.isResponseParsable(response) && response.getPayload() != null;
    }

    @Override
    protected String getURL(DigitalExchange digitalExchange) {
        String url = super.getURL(digitalExchange);
        return new RestListRequestUriBuilder(url, transformRequest(request)).toUriString();
    }

    /**
     * When we forward a RestListRequest to a DE instance we need to modify the
     * pagination parameters, because filtering, sorting and pagination will be
     * applied to the combined result and we need to retrieve all the
     * potentially useful items.<br/>
     * Example: if the user asks for page number 2 with a page size of 5 we need
     * to retrieve the first 10 results from each DE instance in order to be
     * sure to obtain the correct combined result.
     */
    private RestListRequest transformRequest(RestListRequest userRequest) {
        RestListRequest digitalExchangeRequest = new RestListRequest();

        // retrieve all the items in one page
        digitalExchangeRequest.setPage(1);
        digitalExchangeRequest.setPageSize(userRequest.getPage() * userRequest.getPageSize());

        // copy the other fields
        digitalExchangeRequest.setSort(userRequest.getSort());
        digitalExchangeRequest.setDirection(userRequest.getDirection());
        digitalExchangeRequest.setFilters(userRequest.getFilters());

        return digitalExchangeRequest;
    }

    @Override
    protected PagedRestResponse<T> getEmptyRestResponse() {
        return new PagedRestResponse<>(new PagedMetadata<>());
    }

    @Override
    public ResilientPagedMetadata<T> combineResults(List<PagedRestResponse<T>> allResults) {

        List<T> joinedList = new ArrayList<>();
        int total = 0;
        List<RestError> errors = new ArrayList<>();

        for (PagedRestResponse<T> response : allResults) {
            if (response.getErrors().isEmpty()) {
                joinedList.addAll(response.getPayload());
                total += response.getMetaData().getTotalItems();
            } else {
                errors.addAll(response.getErrors());
            }
        }

        RequestListProcessor<T> requestListProcessor = getRequestListProcessor(request, joinedList);
        if (requestListProcessor != null) {
            joinedList = requestListProcessor.filterAndSort().toList();
        }

        List<T> subList = request.getSublist(joinedList);

        ResilientPagedMetadata<T> pagedMetadata = new ResilientPagedMetadata<>(request, subList, total);
        pagedMetadata.setErrors(errors);

        return pagedMetadata;
    }

    protected abstract RequestListProcessor<T> getRequestListProcessor(RestListRequest request, List<T> joinedList);
}
