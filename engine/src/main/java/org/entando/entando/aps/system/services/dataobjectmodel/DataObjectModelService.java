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
import java.util.ArrayList;
import java.util.List;
import org.entando.entando.aps.system.exception.RestServerError;
import org.entando.entando.aps.system.services.IDtoBuilder;
import org.entando.entando.aps.system.services.dataobjectmodel.model.DataModelDto;
import org.entando.entando.web.common.model.PagedMetadata;
import org.entando.entando.web.common.model.RestListRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

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

}
