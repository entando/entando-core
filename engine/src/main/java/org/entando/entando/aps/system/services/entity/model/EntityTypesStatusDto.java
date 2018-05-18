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
package org.entando.entando.aps.system.services.entity.model;

import com.agiletec.aps.system.common.entity.IEntityManager;
import com.agiletec.aps.system.common.entity.model.SmallEntityType;
import java.util.ArrayList;
import java.util.List;

/**
 * @author E.Santoboni
 */
public class EntityTypesStatusDto {

    private List<String> ready = new ArrayList<>();
    private List<String> toRefresh = new ArrayList<>();
    private List<String> refreshing = new ArrayList<>();

    public EntityTypesStatusDto(IEntityManager entityManager) {
        List<SmallEntityType> types = entityManager.getSmallEntityTypes();
        for (SmallEntityType type : types) {
            Integer status = entityManager.getStatus(type.getCode());
            if (status.equals(IEntityManager.STATUS_READY)) {
                this.getReady().add(type.getCode());
            } else if (status.equals(IEntityManager.STATUS_NEED_TO_RELOAD_REFERENCES)) {
                this.getToRefresh().add(type.getCode());
            } else if (status.equals(IEntityManager.STATUS_RELOADING_REFERENCES_IN_PROGRESS)) {
                this.getRefreshing().add(type.getCode());
            }
        }
    }

    public List<String> getReady() {
        return ready;
    }

    public void setReady(List<String> ready) {
        this.ready = ready;
    }

    public List<String> getToRefresh() {
        return toRefresh;
    }

    public void setToRefresh(List<String> toRefresh) {
        this.toRefresh = toRefresh;
    }

    public List<String> getRefreshing() {
        return refreshing;
    }

    public void setRefreshing(List<String> refreshing) {
        this.refreshing = refreshing;
    }

}
