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

import java.util.Comparator;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Predicate;
import org.entando.entando.aps.system.services.RequestListProcessor;
import org.entando.entando.web.common.model.RestListRequest;
import org.entando.entando.web.digitalexchange.component.DigitalExchangeComponent;

public class DigitalExchangeComponentListProcessor extends RequestListProcessor<DigitalExchangeComponent> {

    private static final String NAME = "name";
    private static final String LAST_UPDATE = "lastUpdate";
    private static final String VERSION = "version";
    private static final String TYPE = "type";
    private static final String RATING = "rating";
    private static final String INSTALLED = "installed";

    public DigitalExchangeComponentListProcessor(RestListRequest restListRequest, List<DigitalExchangeComponent> components) {
        super(restListRequest, components);
    }

    @Override
    protected BiFunction<String, String, Predicate<DigitalExchangeComponent>> getPredicates() {
        return (attribute, value) -> {
            switch (attribute) {
                case NAME:
                    return c -> c.getName().toLowerCase().contains(value.toLowerCase());
                case VERSION:
                    return c -> c.getVersion().toLowerCase().contains(value.toLowerCase());
                case TYPE:
                    return c -> c.getType().toLowerCase().contains(value.toLowerCase());
                case RATING:
                    return c -> c.getRating() == Integer.parseInt(value);
                case INSTALLED:
                    return c -> c.isInstalled() == Boolean.parseBoolean(value.toLowerCase());
                default:
                    return null;
            }
        };
    }

    @Override
    protected Function<String, Comparator<DigitalExchangeComponent>> getComparators() {
        return sort -> {
            switch (sort) {
                case LAST_UPDATE:
                    return (a, b) -> a.getLastUpdate().compareTo(b.getLastUpdate());
                case VERSION:
                    return (a, b) -> a.getVersion().compareToIgnoreCase(b.getVersion());
                case TYPE:
                    return (a, b) -> a.getType().compareToIgnoreCase(b.getType());
                case RATING:
                    return (a, b) -> Integer.compare(a.getRating(), b.getRating());
                case INSTALLED:
                    return (a, b) -> Boolean.compare(a.isInstalled(), b.isInstalled());
                case NAME:
                default:
                    return (a, b) -> a.getName().compareToIgnoreCase(b.getName());
            }
        };
    }
}
