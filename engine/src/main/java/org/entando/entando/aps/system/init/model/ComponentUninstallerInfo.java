/*
*
* Copyright 2014 Entando S.r.l. (http://www.entando.com) All rights reserved.
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
* Copyright 2014 Entando S.r.l. (http://www.entando.com) All rights reserved.
*
*/
package org.entando.entando.aps.system.init.model;

import com.agiletec.aps.system.exception.ApsSystemException;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import org.jdom.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author E.Santoboni
 */
public class ComponentUninstallerInfo extends AbstractComponentModule implements Serializable {

	private static final Logger _logger = LoggerFactory.getLogger(ComponentUninstallerInfo.class);
	
	public ComponentUninstallerInfo(Element environmentElement, Map<String, String> postProcessClasses) throws Throwable {
		try {
			Element defaultSqlResourcesElement = environmentElement.getChild("sqlResources");
			super.extractSqlResources(defaultSqlResourcesElement);
			Element postProcessesElement = environmentElement.getChild("postProcesses");
			super.createPostProcesses(postProcessesElement, postProcessClasses);
		} catch (Throwable t) {
			_logger.error("Error creating ComponentUninstallerInfo", t);
			throw new ApsSystemException("Error creating ComponentUninstallerInfo", t);
		}
	}
	
	public List<String> getResourcesPaths() {
		return _resourcesPaths;
	}
	protected void setResourcesPaths(List<String> resourcesPaths) {
		this._resourcesPaths = resourcesPaths;
	}
	
	private List<String> _resourcesPaths;
	
}