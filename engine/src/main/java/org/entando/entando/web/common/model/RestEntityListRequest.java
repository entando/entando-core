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
package org.entando.entando.web.common.model;

import com.agiletec.aps.system.common.FieldSearchFilter;
import com.agiletec.aps.system.common.entity.model.EntitySearchFilter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang3.StringUtils;
import static org.entando.entando.web.common.model.RestListRequest.DIRECTION_VALUE_DEFAULT;

public class RestEntityListRequest extends RestListRequest {

    @Override
    public List<EntitySearchFilter> buildEntitySearchFilters() {
        List<EntitySearchFilter> entitySearchFilters = new ArrayList<>();
        if (null != this.getFilters()) {
            Arrays.stream(this.getFilters())
                    .filter(filter -> ((filter.getAttribute() != null || filter.getEntityAttr() != null)))
                    .forEach(i -> entitySearchFilters.add(i.getEntitySearchFilter()));
        }
        boolean hasOrderFilter = entitySearchFilters.stream().anyMatch(t -> null != t.getOrder());
        EntitySearchFilter sortFilter = this.buildSortFilter();
        if (!hasOrderFilter && null != sortFilter) {
            entitySearchFilters.add(sortFilter);
        }
        return entitySearchFilters;
    }

    private EntitySearchFilter buildSortFilter() {
        if (StringUtils.isNotBlank(StringEscapeUtils.escapeSql(this.getSort()))) {
            boolean isAttributeFilter = (this.getSort().contains("."));
            String fieldName = isAttributeFilter ? this.getSort().substring(this.getSort().indexOf(".") + 1) : this.getSort();
            EntitySearchFilter sort = new EntitySearchFilter(fieldName, isAttributeFilter);
            if (StringUtils.isNotBlank(this.getDirection())) {
                if (!this.getDirection().equalsIgnoreCase(FieldSearchFilter.ASC_ORDER) && !this.getDirection().equalsIgnoreCase(FieldSearchFilter.DESC_ORDER)) {
                    this.setDirection(DIRECTION_VALUE_DEFAULT);
                }
                sort.setOrder(FieldSearchFilter.Order.valueOf(StringEscapeUtils.escapeSql(this.getDirection())));
            }
            return sort;
        }
        return null;
    }

}
