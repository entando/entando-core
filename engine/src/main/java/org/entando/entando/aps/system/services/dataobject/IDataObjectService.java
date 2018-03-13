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
package org.entando.entando.aps.system.services.dataobject;

import java.util.List;
import org.entando.entando.aps.system.services.dataobject.model.DataTypeDto;
import org.entando.entando.aps.system.services.entity.IEntityManagerService;
import org.entando.entando.web.dataobject.model.DataTypeDtoRequest;
import org.entando.entando.web.dataobject.model.DataTypesBodyRequest;

/**
 * @author E.Santoboni
 */
public interface IDataObjectService extends IEntityManagerService {

    public DataTypeDto getDataType(String entityTypeCode);

    public List<DataTypeDto> addDataTypes(DataTypesBodyRequest bodyRequest);

    public DataTypeDto updateDataType(DataTypeDtoRequest request);

    public void deleteDataType(String entityTypeCode);

}
