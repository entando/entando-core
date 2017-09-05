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
package org.entando.entando.aps.system.services.dataobjectmodel;

import java.util.List;
import java.util.Map;

import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.aps.system.services.page.IPage;
import org.entando.entando.aps.system.services.dataobject.model.SmallDataType;

/**
 * Interfaccia base per i Manager dei modelli di datatype.
 *
 * @author E.Santoboni
 */
public interface IDataObjectModelManager {

	public void addContentModel(DataObjectModel model) throws ApsSystemException;

	public void removeContentModel(DataObjectModel model) throws ApsSystemException;

	public void updateContentModel(DataObjectModel model) throws ApsSystemException;

	public DataObjectModel getContentModel(long contentModelId);

	public List<DataObjectModel> getContentModels();

	public List<DataObjectModel> getModelsForContentType(String contentType);

	public Map<String, List<IPage>> getReferencingPages(long modelId);

	public SmallDataType getDefaultUtilizer(long modelId);

}
