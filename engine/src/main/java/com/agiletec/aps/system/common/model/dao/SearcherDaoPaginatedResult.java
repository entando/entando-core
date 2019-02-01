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
package com.agiletec.aps.system.common.model.dao;

import java.util.List;

public class SearcherDaoPaginatedResult<T> {

    private Integer count;
    private List<T> list;

    public SearcherDaoPaginatedResult() {

    }

    public SearcherDaoPaginatedResult(Integer count, List<T> list) {
        super();
        this.count = count;
        this.list = list;
    }

    public SearcherDaoPaginatedResult(List<T> list) {
        super();
        this.count = list == null ? 0 : list.size();
        this.list = list;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public List<T> getList() {
        return list;
    }

    public void setList(List<T> list) {
        this.list = list;
    }

}
