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

import com.agiletec.aps.system.SystemConstants;
import com.agiletec.aps.system.common.entity.IEntityManager;
import java.util.List;
import org.entando.entando.aps.system.services.IDtoBuilder;
import org.entando.entando.aps.system.services.dataobject.model.DataObject;
import org.entando.entando.aps.system.services.dataobject.model.DataTypeDto;
import org.entando.entando.aps.system.services.dataobject.model.DataTypeDtoBuilder;
import org.entando.entando.aps.system.services.entity.AbstractEntityService;
import org.entando.entando.aps.system.services.entity.model.EntityTypeShortDto;
import org.entando.entando.web.common.model.PagedMetadata;
import org.entando.entando.web.common.model.RestListRequest;
import org.entando.entando.web.dataobject.model.DataTypeDtoRequest;
import org.entando.entando.web.dataobject.model.DataTypesBodyRequest;
import org.entando.entando.web.entity.model.EntityTypeDtoRequest;
import org.springframework.validation.BindingResult;

/**
 * @author E.Santoboni
 */
public class DataObjectService extends AbstractEntityService<DataObject, DataTypeDto> implements IDataObjectService {

    @Override
    public PagedMetadata<EntityTypeShortDto> getShortDataTypes(RestListRequest requestList) {
        return super.getShortEntityTypes(SystemConstants.DATA_OBJECT_MANAGER, requestList);
    }

    @Override
    protected IDtoBuilder<DataObject, DataTypeDto> getEntityTypeFullDtoBuilder(IEntityManager masterManager) {
        return new DataTypeDtoBuilder(masterManager.getAttributeRoles());
    }

    @Override
    public DataTypeDto getDataType(String dataTypeCode) {
        return (DataTypeDto) super.getFullEntityType(SystemConstants.DATA_OBJECT_MANAGER, dataTypeCode);
    }

    @Override
    public List<DataTypeDto> addDataTypes(DataTypesBodyRequest bodyRequest, BindingResult bindingResult) {
        return super.addEntityTypes(SystemConstants.DATA_OBJECT_MANAGER, bodyRequest, bindingResult);
    }

    @Override
    public DataTypeDto updateDataType(DataTypeDtoRequest request, BindingResult bindingResult) {
        return (DataTypeDto) super.updateEntityType(SystemConstants.DATA_OBJECT_MANAGER, request, bindingResult);
    }

    @Override
    protected DataObject createEntityType(IEntityManager entityManager, EntityTypeDtoRequest dto, BindingResult bindingResult) throws Throwable {
        DataObject dataObject = super.createEntityType(entityManager, dto, bindingResult);
        DataTypeDtoRequest dtr = (DataTypeDtoRequest) dto;
        dataObject.setDefaultModel(dtr.getDefaultModel());
        dataObject.setListModel(dtr.getListModel());
        dataObject.setViewPage(dtr.getViewPage());
        return dataObject;
    }

    @Override
    public void deleteDataType(String entityTypeCode) {
        super.deleteEntityType(SystemConstants.DATA_OBJECT_MANAGER, entityTypeCode);
    }

}
