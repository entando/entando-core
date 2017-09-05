/*
 * Copyright 2015-Present Entando Inc. (http://www.entando.com) All rights reserved.
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
package org.entando.entando.aps.system.services.datatypemodel;

import java.util.List;
import java.util.Map;

import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.aps.system.services.page.IPage;
import org.entando.entando.aps.system.services.datatype.model.SmallDataType;

/**
 * Interfaccia base per i Manager dei modelli di datatype.
 *
 * @author E.Santoboni
 */
public interface IDataModelManager {

	public void addContentModel(DataModel model) throws ApsSystemException;

	public void removeContentModel(DataModel model) throws ApsSystemException;

	public void updateContentModel(DataModel model) throws ApsSystemException;

	public DataModel getContentModel(long contentModelId);

	public List<DataModel> getContentModels();

	public List<DataModel> getModelsForContentType(String contentType);

	public Map<String, List<IPage>> getReferencingPages(long modelId);

	public SmallDataType getDefaultUtilizer(long modelId);

}
