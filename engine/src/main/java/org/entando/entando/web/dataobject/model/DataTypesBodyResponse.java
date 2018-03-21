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

import java.util.ArrayList;
import java.util.List;
import org.entando.entando.aps.system.services.dataobject.model.DataTypeDto;

/**
 * @author E.Santoboni
 */
public class DataTypesBodyResponse {

    private List<DataTypeDto> dataTypes = new ArrayList<>();

    public DataTypesBodyResponse() {
    }

    public DataTypesBodyResponse(List<DataTypeDto> dataTypes) {
        this.setDataTypes(dataTypes);
    }

    public List<DataTypeDto> getDataTypes() {
        return dataTypes;
    }

    public void setDataTypes(List<DataTypeDto> dataTypes) {
        this.dataTypes = dataTypes;
    }

}
