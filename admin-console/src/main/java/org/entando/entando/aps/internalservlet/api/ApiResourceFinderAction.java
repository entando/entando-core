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
package org.entando.entando.aps.internalservlet.api;

import org.entando.entando.aps.system.services.api.model.ApiMethod;
import org.entando.entando.aps.system.services.api.model.ApiResource;
import org.entando.entando.apsadmin.api.AbstractApiFinderAction;

/**
 * @author E.Santoboni
 */
public class ApiResourceFinderAction extends AbstractApiFinderAction {
    
	@Override
	protected boolean includeIntoMapping(ApiResource apiResource) {
		ApiMethod GETMethod = apiResource.getGetMethod();
		ApiMethod POSTMethod = apiResource.getPostMethod();
		ApiMethod PUTMethod = apiResource.getPutMethod();
		ApiMethod DELETEMethod = apiResource.getDeleteMethod();
		return (this.isVisible(GETMethod) || this.isVisible(POSTMethod) || this.isVisible(PUTMethod) || this.isVisible(DELETEMethod));
	}
	
	private boolean isVisible(ApiMethod method) {
		return (null != method && method.isActive() && !method.getHidden());
	}
	
}