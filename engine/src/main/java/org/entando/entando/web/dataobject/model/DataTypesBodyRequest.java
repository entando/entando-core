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
package org.entando.entando.web.dataobject.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.ArrayList;
import org.entando.entando.web.entity.model.*;
import java.util.List;
import javax.validation.constraints.NotNull;

/**
 * @author E.Santoboni
 */
public class DataTypesBodyRequest implements IEntityTypesBodyRequest {

    @NotNull(message = "dataTypes.list.notBlank")
    private List<DataTypeDtoRequest> dataTypes;

    public DataTypesBodyRequest() {
    }

    public DataTypesBodyRequest(List<DataTypeDtoRequest> dataTypes) {
        this.dataTypes = dataTypes;
    }

    @Override
    @JsonIgnore
    public List<EntityTypeDtoRequest> getEntityTypes() {
        List<EntityTypeDtoRequest> list = new ArrayList<>();
        if (null != this.getDataTypes()) {
            this.getDataTypes().stream().forEach(i -> list.add(i));
        }
        return list;
    }

    public List<DataTypeDtoRequest> getDataTypes() {
        return dataTypes;
    }

    public void setDataTypes(List<DataTypeDtoRequest> dataTypes) {
        this.dataTypes = dataTypes;
    }

}
