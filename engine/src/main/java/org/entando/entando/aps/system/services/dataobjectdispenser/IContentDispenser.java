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

/**
 * Basic interface for service providers formatted content. Implementations may
 * or may not use the caching engine.
 *
 * @author M.Diana - E.Santoboni
 */
public interface IContentDispenser {

	/**
	 * Return the object that contains the renderization info of the content.
	 *
	 * @param contentId The content id.
	 * @param modelId The velocity model id.
	 * @param langCode The code of the current lang.
	 * @param reqCtx The request context.
	 * @return The formatted content.
	 */
	public ContentRenderizationInfo getRenderizationInfo(String contentId,
			long modelId, String langCode, RequestContext reqCtx);

	public ContentRenderizationInfo getRenderizationInfo(String contentId,
			long modelId, String langCode, RequestContext reqCtx, boolean useCache);

}
