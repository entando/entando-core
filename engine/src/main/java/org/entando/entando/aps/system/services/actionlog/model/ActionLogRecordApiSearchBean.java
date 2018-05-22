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
package org.entando.entando.aps.system.services.actionlog.model;

/**
 * @author E.Santoboni
 */
public class ActionLogRecordApiSearchBean extends ActionLogRecordSearchBean {

    private String _orderBy;
    private String _direction;
    private Integer offset;
    private Integer pageSize;

    public String getOrderBy() {
        return _orderBy;
    }

    public void setOrderBy(String orderBy) {
        this._orderBy = orderBy;
    }

    public String getDirection() {
        return _direction;
    }

    public void setDirection(String direction) {
        this._direction = direction;
    }

    public Integer getOffset() {
        return offset;
    }

    public void setOffset(Integer offset) {
        this.offset = offset;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

}
