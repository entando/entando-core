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
package org.entando.entando.aps.system.services.dataobjectmodel;

import com.agiletec.aps.system.common.FieldSearchFilter;
import com.agiletec.aps.system.common.model.dao.SearcherDaoPaginatedResult;
import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.aps.system.services.page.IPage;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.entando.entando.aps.system.exception.RestRourceNotFoundException;
import org.entando.entando.aps.system.exception.RestServerError;
import org.entando.entando.aps.system.services.IDtoBuilder;
import org.entando.entando.aps.system.services.dataobjectmodel.model.DataModelDto;
import org.entando.entando.web.common.exceptions.ValidationConflictException;
import org.entando.entando.web.common.model.PagedMetadata;
import org.entando.entando.web.common.model.RestListRequest;
import org.entando.entando.web.dataobjectmodel.model.DataObjectModelRequest;
import org.entando.entando.web.guifragment.validator.GuiFragmentValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BeanPropertyBindingResult;

public class DataObjectModelService implements IDataObjectModelService {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private IDataObjectModelManager dataObjectModelManager;

    @Autowired
    private IDtoBuilder<DataObjectModel, DataModelDto> dtoBuilder;

    protected IDataObjectModelManager getDataObjectModelManager() {
        return dataObjectModelManager;
    }

    public void setDataObjectModelManager(IDataObjectModelManager dataObjectModelManager) {
        this.dataObjectModelManager = dataObjectModelManager;
    }

    protected IDtoBuilder<DataObjectModel, DataModelDto> getDtoBuilder() {
        return dtoBuilder;
    }

    public void setDtoBuilder(IDtoBuilder<DataObjectModel, DataModelDto> dtoBuilder) {
        this.dtoBuilder = dtoBuilder;
    }

    @Override
    public PagedMetadata<DataModelDto> getDataObjectModels(RestListRequest restListReq) {
        PagedMetadata<DataModelDto> pagedMetadata = null;
        try {
            List<FieldSearchFilter> filters = new ArrayList<>(restListReq.buildFieldSearchFilters());
            //transforms the filters by overriding the key specified in the request with the correct one known by the dto
            filters.stream().filter(searchFilter -> searchFilter.getKey() != null)
                    .forEach(searchFilter -> {
                        searchFilter.setKey(DataModelDto.getEntityFieldName(searchFilter.getKey()));
                        if (searchFilter.getKey().equals("modelid")) {
                            String stringValue = searchFilter.getValue().toString();
                            Long value = Long.parseLong(stringValue);
                            searchFilter = new FieldSearchFilter("modelid", value, true);
                        }
                    });
            SearcherDaoPaginatedResult<DataObjectModel> models = this.getDataObjectModelManager().getDataObjectModels(restListReq.buildFieldSearchFilters());
            List<DataModelDto> dtoList = this.getDtoBuilder().convert(models.getList());
            pagedMetadata = new PagedMetadata<>(restListReq, models);
            pagedMetadata.setBody(dtoList);
        } catch (Throwable t) {
            logger.error("error in search data models", t);
            throw new RestServerError("error in search data models", t);
        }
        return pagedMetadata;
    }

    @Override
    public DataModelDto getDataObjectModel(Long dataModelId) {
        DataObjectModel model = this.getDataObjectModelManager().getDataObjectModel(dataModelId);
        if (null == model) {
            logger.warn("no model found with code {}", dataModelId);
            throw new RestRourceNotFoundException("dataModelId", String.valueOf(dataModelId));
        }
        return this.getDtoBuilder().convert(model);
    }

    @Override
    public DataModelDto addDataObjectModel(DataObjectModelRequest dataObjectModelRequest) {
        try {
            DataObjectModel dataModel = this.createDataObjectModel(dataObjectModelRequest);
            this.getDataObjectModelManager().addDataObjectModel(dataModel);
            return this.getDtoBuilder().convert(dataModel);
        } catch (ApsSystemException e) {
            logger.error("Error adding DataObjectModel", e);
            throw new RestServerError("error add DataObjectModel", e);
        }
    }

    @Override
    public DataModelDto updateDataObjectModel(DataObjectModelRequest dataObjectModelRequest) {
        Long code = dataObjectModelRequest.getModelId();
        try {
            DataObjectModel dataObjectModel = this.getDataObjectModelManager().getDataObjectModel(code);
            if (null == dataObjectModel) {
                throw new RestRourceNotFoundException("dataObjectModel", String.valueOf(code));
            }
            dataObjectModel.setDataType(dataObjectModelRequest.getType());
            dataObjectModel.setDescription(dataObjectModelRequest.getDescr());
            dataObjectModel.setShape(dataObjectModelRequest.getModel());
            dataObjectModel.setStylesheet(dataObjectModelRequest.getStylesheet());
            this.getDataObjectModelManager().updateDataObjectModel(dataObjectModel);
            return this.getDtoBuilder().convert(dataObjectModel);
        } catch (RestRourceNotFoundException e) {
            throw e;
        } catch (ApsSystemException e) {
            logger.error("Error updating DataObjectModel {}", code, e);
            throw new RestServerError("error in update DataObjectModel", e);
        }
    }

    @Override
    public void removeDataObjectModel(Long dataModelId) {
        try {
            DataObjectModel dataObjectModel = this.getDataObjectModelManager().getDataObjectModel(dataModelId);
            if (null == dataObjectModel) {
                return;
            }
            DataModelDto dto = this.getDtoBuilder().convert(dataObjectModel);
            BeanPropertyBindingResult validationResult = this.checkDataObjectModelForDelete(dataObjectModel, dto);
            if (validationResult.hasErrors()) {
                throw new ValidationConflictException(validationResult);
            }
            this.getDataObjectModelManager().removeDataObjectModel(dataObjectModel);
        } catch (ApsSystemException e) {
            logger.error("Error in delete DataObjectModel {}", dataModelId, e);
            throw new RestServerError("error in delete DataObjectModel", e);
        }
    }

    protected DataObjectModel createDataObjectModel(DataObjectModelRequest dataObjectModelRequest) {
        DataObjectModel model = new DataObjectModel();
        model.setDataType(dataObjectModelRequest.getType());
        model.setDescription(dataObjectModelRequest.getDescr());
        model.setId(dataObjectModelRequest.getModelId());
        model.setShape(dataObjectModelRequest.getModel());
        model.setStylesheet(dataObjectModelRequest.getStylesheet());
        return model;
    }

    protected BeanPropertyBindingResult checkDataObjectModelForDelete(DataObjectModel model, DataModelDto dto) throws ApsSystemException {
        BeanPropertyBindingResult bindingResult = new BeanPropertyBindingResult(model, "dataObjectModel");
        if (null == model) {
            return bindingResult;
        }
        Map<String, List<IPage>> pages = this.getDataObjectModelManager().getReferencingPages(model.getId());
        if (!bindingResult.hasErrors() && !pages.isEmpty()) {
            bindingResult.reject(GuiFragmentValidator.ERRCODE_FRAGMENT_REFERENCES, new Object[]{String.valueOf(model.getId())}, "guifragment.cannot.delete.references");
        }
        return bindingResult;
    }

}
