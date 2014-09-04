/*
*
* Copyright 2013 Entando S.r.l. (http://www.entando.com) All rights reserved.
*
* This file is part of Entando software.
* Entando is a free software;
* You can redistribute it and/or modify it
* under the terms of the GNU General Public License (GPL) as published by the Free Software Foundation; version 2.
* 
* See the file License for the specific language governing permissions   
* and limitations under the License
* 
* 
* 
* Copyright 2013 Entando S.r.l. (http://www.entando.com) All rights reserved.
*
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