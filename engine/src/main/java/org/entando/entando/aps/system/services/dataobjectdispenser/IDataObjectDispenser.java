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
package org.entando.entando.aps.system.services.dataobjectdispenser;

import com.agiletec.aps.system.RequestContext;
import org.entando.entando.aps.system.services.dataobject.model.DataObject;

/**
 * Basic interface for service providers formatted dataObject. Implementations
 * may or may not use the caching engine.
 *
 * @author M.Diana - E.Santoboni
 */
public interface IDataObjectDispenser {

	/**
	 * Return the object that contains the renderization info of the dataObject.
	 *
	 * @param dataObjectId The dataObject id.
	 * @param modelId The velocity model id.
	 * @param langCode The code of the current lang.
	 * @param reqCtx The request context.
	 * @return The formatted dataObject.
	 */
	public DataObjectRenderizationInfo getRenderizationInfo(String dataObjectId,
			long modelId, String langCode, RequestContext reqCtx);

	public DataObjectRenderizationInfo getRenderizationInfo(String dataObjectId,
			long modelId, String langCode, RequestContext reqCtx, boolean useCache);

	public DataObjectRenderizationInfo getBaseRenderizationInfo(DataObject contentToRender,
			long modelId, String langCode, RequestContext reqCtx);

}
