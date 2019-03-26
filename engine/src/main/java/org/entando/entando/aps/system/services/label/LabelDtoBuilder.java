/*
 * Copyright 2018-Present Entando S.r.l. (http://www.entando.com) All rights reserved.
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
package org.entando.entando.aps.system.services.label;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import com.agiletec.aps.util.ApsProperties;
import org.entando.entando.aps.system.services.DtoBuilder;
import org.entando.entando.aps.system.services.label.model.LabelDto;

public class LabelDtoBuilder extends DtoBuilder<ApsProperties, LabelDto> {

    @Deprecated
    @Override
    public LabelDto convert(ApsProperties entity) {
        throw new UnsupportedOperationException("use: convert(String key, ApsProperties entity)");
    }

    @Deprecated
    @Override
    public List<LabelDto> convert(List<ApsProperties> list) {
        throw new UnsupportedOperationException("use: convert(Map<String, ApsProperties> list)");
    }

    @Deprecated
    @Override
    protected LabelDto toDto(ApsProperties src) {
        throw new UnsupportedOperationException("use: toDto(String key, ApsProperties src)");
    }

    public LabelDto convert(String key, ApsProperties entity) {
        return Optional.ofNullable(entity)
                .map(e -> toDto(key, e))
                .orElse(null);
    }

    public List<LabelDto> convert(Map<String, ApsProperties> list) {
        return Optional.ofNullable(list)
                .map(l -> l.entrySet().stream()
                        .map(e -> toDto(e.getKey(), e.getValue()))
                        .collect(Collectors.toList()))
                .orElse(null);
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    protected LabelDto toDto(final String key, final ApsProperties src) {
        return new LabelDto(key, (Map) src);
    }

}

