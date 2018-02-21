/*
 * Copyright 2015-Present Entando Inc. (http://www.entando.com) All rights reserved.
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

import java.util.ArrayList;
import java.util.List;

public abstract class DtoBuilder<I, O> implements IDtoBuilder<I, O> {

    @Override
    public O convert(I entity) {
        O dto = null;
        if (null != entity) {

            dto = toDto(entity);
        }
        return dto;
    }

    @Override
    public List<O> convert(List<I> list) {
        if (null == list) {
            return null;
        }
        List<O> dto = new ArrayList<>();
        for (I entry : list) {
            dto.add(convert(entry));
        }
        return dto;
    }

    protected abstract O toDto(I src);

}
