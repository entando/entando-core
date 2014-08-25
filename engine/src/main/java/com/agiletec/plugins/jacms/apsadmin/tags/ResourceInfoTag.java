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
package com.agiletec.plugins.jacms.apsadmin.tags;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.aps.util.ApsWebApplicationUtils;
import com.agiletec.apsadmin.tags.AbstractObjectInfoTag;
import com.agiletec.plugins.jacms.aps.system.JacmsSystemConstants;
import com.agiletec.plugins.jacms.aps.system.services.resource.IResourceManager;

/**
 * Returns a resource (or one of its property) through the code.
 * You can choose whether to return the entire object (leaving the attribute "property" empty) or a single property.
 * The names of the available property of "Resource": "id", "descr", "type", 
 * "mainGroup" (code), "folder", "categories" (list of categories), "allowedFileTypes".
 * @author E.Santoboni
 */
public class ResourceInfoTag extends AbstractObjectInfoTag {

	private static final Logger _logger = LoggerFactory.getLogger(ResourceInfoTag.class);
	
	@Override
	protected Object getMasterObject(String keyValue) throws Throwable {
		try {
			IResourceManager resourceManager = (IResourceManager) ApsWebApplicationUtils.getBean(JacmsSystemConstants.RESOURCE_MANAGER, this.pageContext);
			return resourceManager.loadResource(keyValue);
		} catch (Throwable t) {
			_logger.error("Error extracting resource : id '{}'", keyValue, t);
			String message = "Error extracting resource : id '" + keyValue + "'";
			//ApsSystemUtils.logThrowable(t, this, "getMasterObject", message);
			throw new ApsSystemException(message, t);
		}
	}
	
	public void setResourceId(String resourceId) {
		super.setKey(resourceId);
	}
	
}