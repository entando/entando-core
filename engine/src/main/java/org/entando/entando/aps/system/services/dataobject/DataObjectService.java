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

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;

import com.agiletec.aps.system.SystemConstants;
import com.agiletec.aps.system.common.entity.IEntityManager;
import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.aps.system.services.category.CategoryUtilizer;
import com.agiletec.aps.system.services.group.GroupUtilizer;
import com.agiletec.aps.system.services.page.IPageManager;
import org.apache.commons.lang3.StringUtils;
import org.entando.entando.aps.system.exception.RestServerError;
import org.entando.entando.aps.system.services.DtoBuilder;
import org.entando.entando.aps.system.services.IDtoBuilder;
import org.entando.entando.aps.system.services.category.CategoryServiceUtilizer;
import org.entando.entando.aps.system.services.dataobject.model.DataObject;
import org.entando.entando.aps.system.services.dataobject.model.DataObjectDto;
import org.entando.entando.aps.system.services.dataobject.model.DataTypeDto;
import org.entando.entando.aps.system.services.dataobject.model.DataTypeDtoBuilder;
import org.entando.entando.aps.system.services.dataobjectmodel.DataObjectModel;
import org.entando.entando.aps.system.services.dataobjectmodel.IDataObjectModelManager;
import org.entando.entando.aps.system.services.entity.AbstractEntityTypeService;
import org.entando.entando.aps.system.services.entity.model.AttributeTypeDto;
import org.entando.entando.aps.system.services.entity.model.EntityTypeAttributeFullDto;
import org.entando.entando.aps.system.services.entity.model.EntityTypeShortDto;
import org.entando.entando.aps.system.services.group.GroupServiceUtilizer;
import org.entando.entando.web.common.model.PagedMetadata;
import org.entando.entando.web.common.model.RestListRequest;
import org.entando.entando.web.dataobject.model.DataTypeDtoRequest;
import org.entando.entando.web.dataobject.validator.DataTypeValidator;
import org.entando.entando.web.entity.model.EntityTypeDtoRequest;
import org.springframework.validation.BindingResult;

/**
 * @author E.Santoboni
 */
public class DataObjectService extends AbstractEntityTypeService<DataObject, DataTypeDto>
        implements IDataObjectService, GroupServiceUtilizer<DataObjectDto>, CategoryServiceUtilizer<DataObjectDto> {

    private IPageManager pageManager;
    private IDataObjectModelManager dataObjectModelManager;
    private IDtoBuilder<DataObject, DataObjectDto> dtoBuilder;

    @PostConstruct
    protected void setUp() {
        this.setDtoBuilder(new DtoBuilder<DataObject, DataObjectDto>() {
            @Override
            protected DataObjectDto toDto(DataObject src) {
                DataObjectDto dto = new DataObjectDto();
                dto.setId(src.getId());
                dto.setTypeCode(src.getTypeCode());
                dto.setTypeDescription(src.getTypeDescription());
                dto.setDescription(src.getDescription());
                dto.setMainGroup(src.getMainGroup());
                dto.setGroups(src.getGroups());
                return dto;
            }
        });
    }

    protected IDtoBuilder<DataObject, DataObjectDto> getDtoBuilder() {
        return dtoBuilder;
    }

    public void setDtoBuilder(IDtoBuilder<DataObject, DataObjectDto> dtoBuilder) {
        this.dtoBuilder = dtoBuilder;
    }

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
    public DataTypeDto addDataType(DataTypeDtoRequest bodyRequest, BindingResult bindingResult) {
        return super.addEntityType(SystemConstants.DATA_OBJECT_MANAGER, bodyRequest, bindingResult);
    }

    @Override
    public DataTypeDto updateDataType(DataTypeDtoRequest request, BindingResult bindingResult) {
        return (DataTypeDto) super.updateEntityType(SystemConstants.DATA_OBJECT_MANAGER, request, bindingResult);
    }

    @Override
    protected DataObject createEntityType(IEntityManager entityManager, EntityTypeDtoRequest dto, BindingResult bindingResult) throws Throwable {
        DataObject dataObject = super.createEntityType(entityManager, dto, bindingResult);
        DataTypeDtoRequest dtr = (DataTypeDtoRequest) dto;
        if (this.checkModel(false, dataObject.getTypeCode(), dtr.getListModel(), bindingResult)) {
            dataObject.setListModel(dtr.getListModel());
        }
        if (this.checkModel(true, dataObject.getTypeCode(), dtr.getDefaultModel(), bindingResult)) {
            dataObject.setDefaultModel(dtr.getDefaultModel());
        }
        if (this.checkPage(dataObject.getTypeCode(), dtr.getViewPage(), bindingResult)) {
            dataObject.setViewPage(dtr.getViewPage());
        }
        return dataObject;
    }

    private boolean checkModel(boolean isDefault, String typeCode, String modelIdString, BindingResult bindingResult) {
        if (StringUtils.isEmpty(modelIdString)) {
            return false;
        }
        Long longId = null;
        try {
            longId = Long.parseLong(modelIdString);
        } catch (Exception e) {
            this.addError((isDefault ? DataTypeValidator.ERRCODE_INVALID_DEFAULT_MODEL : DataTypeValidator.ERRCODE_INVALID_LIST_MODEL),
                    bindingResult, new String[]{typeCode, modelIdString}, "dataType.modelId.invalid");
            return false;
        }
        DataObjectModel model = this.getDataObjectModelManager().getDataObjectModel(longId);
        if (null == model) {
            this.addError((isDefault ? DataTypeValidator.ERRCODE_DEFAULT_MODEL_DOES_NOT_EXIST : DataTypeValidator.ERRCODE_LIST_MODEL_DOES_NOT_EXIST),
                    bindingResult, new String[]{typeCode, modelIdString}, "dataType.modelId.doesNotExist");
            return false;
        } else if (model.getDataType().equals(typeCode)) {
            this.addError((isDefault ? DataTypeValidator.ERRCODE_DEFAULT_MODEL_MISMATCH : DataTypeValidator.ERRCODE_LIST_MODEL_MISMATCH),
                    bindingResult, new String[]{typeCode, modelIdString, model.getDataType()}, "dataType.modelId.mismatch");
            return false;
        }
        return true;
    }

    private boolean checkPage(String typeCode, String pageCode, BindingResult bindingResult) {
        if (StringUtils.isEmpty(pageCode)) {
            return false;
        }
        if (null == this.getPageManager().getOnlinePage(pageCode)) {
            this.addError(DataTypeValidator.ERRCODE_INVALID_VIEW_PAGE, bindingResult, new String[]{typeCode, pageCode}, "dataType.pageCode.invalid");
            return false;
        }
        return true;
    }

    @Override
    public void deleteDataType(String dataTypeCode) {
        super.deleteEntityType(SystemConstants.DATA_OBJECT_MANAGER, dataTypeCode);
    }

    @Override
    public PagedMetadata<String> getAttributeTypes(RestListRequest requestList) {
        return super.getAttributeTypes(SystemConstants.DATA_OBJECT_MANAGER, requestList);
    }

    @Override
    public AttributeTypeDto getAttributeType(String attributeCode) {
        return super.getAttributeType(SystemConstants.DATA_OBJECT_MANAGER, attributeCode);
    }

    @Override
    public EntityTypeAttributeFullDto getDataTypeAttribute(String dataTypeCode, String attributeCode) {
        return super.getEntityAttribute(SystemConstants.DATA_OBJECT_MANAGER, dataTypeCode, attributeCode);
    }

    @Override
    public EntityTypeAttributeFullDto addDataTypeAttribute(String dataTypeCode, EntityTypeAttributeFullDto bodyRequest, BindingResult bindingResult) {
        return super.addEntityAttribute(SystemConstants.DATA_OBJECT_MANAGER, dataTypeCode, bodyRequest, bindingResult);
    }

    @Override
    public EntityTypeAttributeFullDto updateDataTypeAttribute(String dataTypeCode, EntityTypeAttributeFullDto bodyRequest, BindingResult bindingResult) {
        return super.updateEntityAttribute(SystemConstants.DATA_OBJECT_MANAGER, dataTypeCode, bodyRequest, bindingResult);
    }

    @Override
    public void deleteDataTypeAttribute(String dataTypeCode, String attributeCode) {
        super.deleteEntityAttribute(SystemConstants.DATA_OBJECT_MANAGER, dataTypeCode, attributeCode);
    }

    @Override
    public void moveDataTypeAttribute(String dataTypeCode, String attributeCode, boolean moveUp) {
        super.moveEntityAttribute(SystemConstants.DATA_OBJECT_MANAGER, dataTypeCode, attributeCode, moveUp);
    }

    @Override
    public void reloadDataTypeReferences(String dataTypeCode) {
        super.reloadEntityTypeReferences(SystemConstants.DATA_OBJECT_MANAGER, dataTypeCode);
    }

    protected IPageManager getPageManager() {
        return pageManager;
    }

    public void setPageManager(IPageManager pageManager) {
        this.pageManager = pageManager;
    }

    protected IDataObjectModelManager getDataObjectModelManager() {
        return dataObjectModelManager;
    }

    public void setDataObjectModelManager(IDataObjectModelManager dataObjectModelManager) {
        this.dataObjectModelManager = dataObjectModelManager;
    }

    @Override
    public String getManagerName() {
        return SystemConstants.DATA_OBJECT_MANAGER;
    }

    @Override
    public List<DataObjectDto> getGroupUtilizer(String groupCode) {
        try {
            DataObjectManager entityManager = (DataObjectManager) this.extractEntityManager(this.getManagerName());
            List<String> idList = ((GroupUtilizer<String>) entityManager).getGroupUtilizers(groupCode);
            return this.buildDtoList(idList, entityManager);
        } catch (ApsSystemException ex) {
            logger.error("Error loading dataobject references for group {}", groupCode, ex);
            throw new RestServerError("Error loading dataobject references for group", ex);
        }
    }

    @Override
    public List getCategoryUtilizer(String categoryCode) {
        try {
            DataObjectManager entityManager = (DataObjectManager) this.extractEntityManager(this.getManagerName());
            List<String> idList = ((CategoryUtilizer) entityManager).getCategoryUtilizers(categoryCode);
            return this.buildDtoList(idList, entityManager);
        } catch (ApsSystemException ex) {
            logger.error("Error loading dataobject references for category {}", categoryCode, ex);
            throw new RestServerError("Error loading dataobject references for category", ex);
        }
    }

    private List<DataObjectDto> buildDtoList(List<String> idList, DataObjectManager entityManager) {
        List<DataObjectDto> dtoList = new ArrayList<>();
        if (null != idList) {
            idList.stream().forEach(i -> {
                try {
                    dtoList.add(this.getDtoBuilder().convert(entityManager.loadDataObject(i, true)));
                } catch (ApsSystemException ex) {
                    logger.warn("error loading data object {}", i, ex);
                }
            });
        }
        return dtoList;
    }

}
