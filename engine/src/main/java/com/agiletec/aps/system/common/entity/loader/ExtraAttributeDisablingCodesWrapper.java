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
package com.agiletec.aps.system.common.entity.loader;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agiletec.aps.system.common.IManager;
import com.agiletec.aps.system.common.entity.IEntityManager;
import com.agiletec.aps.system.common.entity.parse.AttributeDisablingCodesDOM;
import com.agiletec.aps.system.exception.ApsSystemException;

/**
 * The Class of the extra attribute disabling codes.
 * @author E.Santoboni
 */
public class ExtraAttributeDisablingCodesWrapper extends AbstractExtraAttributeSupportObject {

	private static final Logger _logger =  LoggerFactory.getLogger(ExtraAttributeDisablingCodesWrapper.class);
	
	public void executeLoading(Map<String, String> collectionToFill, IEntityManager entityManager) throws ApsSystemException {
		String managerName = ((IManager) entityManager).getName();
		if (!managerName.equals(super.getEntityManagerNameDest())) {
			return;
		}
		try {
			String xml = super.extractXml();
			AttributeDisablingCodesDOM dom = new AttributeDisablingCodesDOM();
			Map<String, String> codeMap = dom.extractDisablingCodes(xml, this.getDefsFilePath());
			List<String> codes = new ArrayList<String>(codeMap.keySet());
			for (int i = 0; i < codes.size(); i++) {
				String code = codes.get(i);
				if (collectionToFill.containsKey(code)) {
					_logger.warn("You can't override existing disabling code : {} - {}", code, collectionToFill.get(code));
				} else {
					collectionToFill.put(code, codeMap.get(code));
					_logger.info("Added new disabling code : {} - {}", code, collectionToFill.get(code));
				}
			}
		} catch (Throwable t) {
			//ApsSystemUtils.logThrowable(t, this, "executeLoading", "Error loading extra attribute disabling codes");
			_logger.error("Error loading extra attribute disabling codes", t);
		}
	}
	
}