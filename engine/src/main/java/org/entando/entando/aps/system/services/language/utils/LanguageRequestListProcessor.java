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
package org.entando.entando.aps.system.services.language.utils;

import java.util.Comparator;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Predicate;
import org.entando.entando.aps.system.services.RequestListProcessor;
import org.entando.entando.aps.system.services.language.LanguageDto;
import org.entando.entando.web.common.model.RestListRequest;

public class LanguageRequestListProcessor extends RequestListProcessor<LanguageDto> {

    private static final String CODE = "code";
    private static final String DESCRIPTION = "description";
    private static final String ACTIVE = "isActive";
    private static final String DEFAULT = "isDefault";

    public LanguageRequestListProcessor(RestListRequest restListRequest, List<LanguageDto> languages) {
        super(restListRequest, languages);
    }

    @Override
    protected BiFunction<String, String, Predicate<LanguageDto>> getPredicates() {
        return (attribute, value) -> {
            switch (attribute) {
                case CODE:
                    return p -> p.getCode().equalsIgnoreCase(value);
                case DESCRIPTION:
                    return p -> p.getDescription().toLowerCase().contains(value.toLowerCase());
                case DEFAULT:
                    return p -> p.isDefaultLang() == Boolean.valueOf(value);
                case ACTIVE:
                    return p -> p.isActive() == Boolean.valueOf(value);
            }
            return null;
        };
    }

    @Override
    protected Function<String, Comparator<LanguageDto>> getComparators() {
        return sort -> {
            switch (sort) {
                case DESCRIPTION:
                    return (a, b) -> a.getDescription().compareTo(b.getDescription());
                case DEFAULT:
                    return (a, b) -> Boolean.compare(a.isDefaultLang(), b.isDefaultLang());
                case ACTIVE:
                    return (a, b) -> Boolean.compare(a.isActive(), b.isActive());
                case CODE:
                default:
                    return (a, b) -> a.getCode().compareTo(b.getCode());
            }
        };
    }
}
