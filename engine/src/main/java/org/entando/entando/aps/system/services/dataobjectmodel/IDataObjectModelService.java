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

import com.fasterxml.jackson.databind.JsonNode;
import org.entando.entando.aps.system.services.dataobjectmodel.model.DataModelDto;
import org.entando.entando.aps.system.services.dataobjectmodel.model.IEntityModelDictionary;
import org.entando.entando.web.common.model.PagedMetadata;
import org.entando.entando.web.common.model.RestListRequest;
import org.entando.entando.web.dataobjectmodel.model.DataObjectModelRequest;

public interface IDataObjectModelService {

    public PagedMetadata<DataModelDto> getDataObjectModels(RestListRequest restListReq);

    public DataModelDto getDataObjectModel(Long dataModelId);

    public DataModelDto addDataObjectModel(DataObjectModelRequest dataObjectModelRequest);

    public DataModelDto updateDataObjectModel(DataObjectModelRequest dataObjectModelRequest);

    public DataModelDto getPatchedDataObjectModel(Long dataModelId, JsonNode jsonPatch);

    public void removeDataObjectModel(Long dataModelId);

    public IEntityModelDictionary getDataModelDictionary(String typeCode);
}
