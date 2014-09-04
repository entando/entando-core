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
package org.entando.entando.apsadmin.portal.model.helper;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.aps.system.services.pagemodel.PageModel;
import com.agiletec.aps.system.services.pagemodel.PageModelUtilizer;
import com.agiletec.aps.util.ApsWebApplicationUtils;
import com.agiletec.apsadmin.system.BaseActionHelper;

/**
 * Helper class for PageModel Actions.
 * @author E.Santoboni
 */
public class PageModelActionHelper extends BaseActionHelper implements IPageModelActionHelper {

	private static final Logger _logger = LoggerFactory.getLogger(PageModelActionHelper.class);
	
	@Override
	public Map<String, List<Object>> getReferencingObjects(PageModel pageModel, HttpServletRequest request) throws ApsSystemException {
		Map<String, List<Object>> references = new HashMap<String, List<Object>>();
    	try {
    		String[] defNames = ApsWebApplicationUtils.getWebApplicationContext(request).getBeanNamesForType(PageModelUtilizer.class);
			for (int i=0; i<defNames.length; i++) {
				Object service = null;
				try {
					service = ApsWebApplicationUtils.getWebApplicationContext(request).getBean(defNames[i]);
				} catch (Throwable t) {
					_logger.error("error in hasReferencingObjects", t);
					service = null;
				}
				if (service != null) {
					PageModelUtilizer pageModelUtilizer = (PageModelUtilizer) service;
					List<Object> utilizers = pageModelUtilizer.getPageModelUtilizers(pageModel.getCode());
					if (utilizers != null && !utilizers.isEmpty()) {
						references.put(pageModelUtilizer.getName()+"Utilizers", utilizers);
					}
				}
			}
    	} catch (Throwable t) {
    		throw new ApsSystemException("Error on getReferencingObjects methods", t);
    	}
    	return references;
	}
	
}
