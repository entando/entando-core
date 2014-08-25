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
package com.agiletec.plugins.jacms.aps.system.services.dispenser;

import com.agiletec.aps.system.RequestContext;

/**
 * Basic interface for service providers formatted content.
 * Implementations may or may not use the caching engine.
 * @author M.Diana - E.Santoboni
 */
public interface IContentDispenser {
	
	/**
	 * Return the formatted content.
	 * @param contentId The content id.
	 * @param modelId The velocity model id.
	 * @param langCode The code of the current lang.
	 * @param reqCtx The request context.
	 * @return The formatted content.
	 * @deprecated use getRenderizationInfo(String, long, String, RequestContext)
	 */
	public String getRenderedContent(String contentId, long modelId, String langCode, RequestContext reqCtx);
	
	/**
	 * Return the object that contains the renderization info of the content.
	 * @param contentId The content id.
	 * @param modelId The velocity model id.
	 * @param langCode The code of the current lang.
	 * @param reqCtx The request context.
	 * @return The formatted content.
	 */
	public ContentRenderizationInfo getRenderizationInfo(String contentId, long modelId, String langCode, RequestContext reqCtx);
	
	public void resolveLinks(ContentRenderizationInfo renderizationInfo, RequestContext reqCtx);
	
}