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
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.entando.entando.web.common.model.Filter;
import org.entando.entando.web.common.model.RestListRequest;

/**
 * Base class used for defining filtering and sorting at service layer. This is
 * useful when entities have already been retrieved from the database and are
 * inside a cache or when the DAO layer is missing (e.g. the data is retrieved
 * from a web service and not from a database). The other filters are usually
 * managed at DAO layer.
 */
public abstract class RequestListProcessor<T> {

    private final RestListRequest restListRequest;
    private Stream<T> stream;

    public RequestListProcessor(RestListRequest restListRequest, Stream<T> stream) {
        this.restListRequest = restListRequest;
        this.stream = stream;
    }

    public RequestListProcessor(RestListRequest restListRequest, List<T> items) {
        this(restListRequest, items.stream());
    }

    protected abstract Function<Filter, Predicate<T>> getPredicates();

    protected abstract Function<String, Comparator<T>> getComparators();

    public RequestListProcessor<T> filter() {

        Function<Filter, Predicate<T>> predicatesProvider = getPredicates();

        if (null != restListRequest && null != restListRequest.getFilters()) {

            for (Filter filter : restListRequest.getFilters()) {

                String filterAttribute = filter.getAttribute();
                String filterValue = filter.getValue();

                if (filterAttribute != null && !filterAttribute.isEmpty()
                        && ((filterValue != null && !filterValue.isEmpty())
                        || (filter.getAllowedValues() != null && filter.getAllowedValues().length > 0))) {

                    Predicate<T> p = predicatesProvider.apply(filter);
                    if (null != p) {
                        stream = stream.filter(p);
                    }
                }
            }
        }

        return this;
    }

    public RequestListProcessor<T> sort() {

        Function<String, Comparator<T>> comparatorsProvider = getComparators();

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

    public RequestListProcessor<T> filterAndSort() {
        return filter().sort();
    }

    public Stream<T> getStream() {
        return stream;
    }

    public List<T> toList() {
        return stream.collect(Collectors.toList());
    }
}
