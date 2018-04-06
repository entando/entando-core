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

import org.entando.entando.aps.system.services.dataobject.model.DataTypeDto;
import org.entando.entando.aps.system.services.entity.model.AttributeTypeDto;
import org.entando.entando.aps.system.services.entity.model.EntityAttributeFullDto;
import org.entando.entando.aps.system.services.entity.model.EntityTypeShortDto;
import org.entando.entando.web.common.model.PagedMetadata;
import org.entando.entando.web.common.model.RestListRequest;
import org.entando.entando.web.dataobject.model.DataTypeDtoRequest;
import org.springframework.validation.BindingResult;

/**
 * @author E.Santoboni
 */
public interface IDataObjectService {

    public PagedMetadata<EntityTypeShortDto> getShortDataTypes(RestListRequest requestList);

    public DataTypeDto getDataType(String dataTypeCode);

    public DataTypeDto addDataType(DataTypeDtoRequest bodyRequest, BindingResult bindingResult);

    public DataTypeDto updateDataType(DataTypeDtoRequest request, BindingResult bindingResult);

    public void deleteDataType(String dataTypeCode);

    // ----------------------------------
    public PagedMetadata<String> getAttributeTypes(RestListRequest requestList);

    public AttributeTypeDto getAttributeType(String attributeCode);

    // ----------------------------------
    public EntityAttributeFullDto getDataTypeAttribute(String dataTypeCode, String attributeCode);

    public EntityAttributeFullDto addDataTypeAttribute(String dataTypeCode, EntityAttributeFullDto bodyRequest, BindingResult bindingResult);

    public EntityAttributeFullDto updateDataTypeAttribute(String dataTypeCode, EntityAttributeFullDto bodyRequest, BindingResult bindingResult);

    public void deleteDataTypeAttribute(String dataTypeCode, String attributeCode);

    public void reloadDataTypeReferences(String dataTypeCode);

}
