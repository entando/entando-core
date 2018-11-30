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
package org.entando.entando.aps.system.services;

import com.agiletec.aps.system.common.FieldSearchFilter;
import java.util.Comparator;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.entando.entando.web.common.model.Filter;
import org.entando.entando.web.common.model.RestListRequest;

public class RequestListProcessor<T> {

    private final RestListRequest restListRequest;
    private Stream<T> stream;

    private RequestListProcessor(RestListRequest restListRequest) {
        this.restListRequest = restListRequest;
    }

    public RequestListProcessor<T> filter(BiFunction<String, String, Predicate<T>> predicatesProvider) {

        if (null != restListRequest && null != restListRequest.getFilters()) {

            for (Filter filter : restListRequest.getFilters()) {
                String filterAttribute = filter.getAttribute();
                String filterValue = filter.getValue();
                if (filterAttribute != null && filterValue != null) {
                    Predicate<T> p = predicatesProvider.apply(filterAttribute, filterValue);
                    if (null != p) {
                        stream = stream.filter(p);
                    }
                }
            }
        }

        return this;
    }

    public RequestListProcessor<T> sort(Function<String, Comparator<T>> comparatorsProvider) {

        String sort = restListRequest.getSort();
        String direction = restListRequest.getDirection();

        if (sort != null && direction != null) {

            Comparator<T> comparator = comparatorsProvider.apply(restListRequest.getSort());

            if (comparator != null) {
                if (direction.equalsIgnoreCase(FieldSearchFilter.DESC_ORDER)) {
                    comparator = comparator.reversed();
                }
                stream = stream.sorted(comparator);
            }
        }

        return this;
    }

    public Stream<T> getStream() {
        return stream;
    }

    public List<T> toList() {
        List<T> resultList = stream.collect(Collectors.toList());
        return restListRequest.getSublist(resultList);
    }

    public static <T> RequestListProcessor<T> fromList(List<T> items, RestListRequest restListRequest) {
        return fromStream(items.stream(), restListRequest);
    }

    public static <T> RequestListProcessor<T> fromStream(Stream<T> stream, RestListRequest restListRequest) {
        RequestListProcessor<T> processor = new RequestListProcessor<>(restListRequest);
        processor.stream = stream;
        return processor;
    }
}
