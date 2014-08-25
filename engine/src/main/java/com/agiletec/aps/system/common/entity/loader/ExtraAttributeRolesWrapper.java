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
import com.agiletec.aps.system.common.entity.model.attribute.AttributeRole;
import com.agiletec.aps.system.common.entity.parse.AttributeRoleDOM;
import com.agiletec.aps.system.exception.ApsSystemException;

/**
 * The Wrapper Class of the extra attribute roles.
 * @author E.Santoboni
 */
public class ExtraAttributeRolesWrapper extends AbstractExtraAttributeSupportObject {

	private static final Logger _logger =  LoggerFactory.getLogger(ExtraAttributeRolesWrapper.class);
	
	public void executeLoading(Map<String, AttributeRole> collectionToFill, IEntityManager entityManager) throws ApsSystemException {
		String managerName = ((IManager) entityManager).getName();
		if (!managerName.equals(super.getEntityManagerNameDest())) {
			return;
		}
		AttributeRoleDOM dom = new AttributeRoleDOM();
		try {
			String xml = super.extractXml();
			Map<String, AttributeRole> attributeRoles = dom.extractRoles(xml, this.getDefsFilePath());
			List<AttributeRole> roles = new ArrayList<AttributeRole>(attributeRoles.values());
			for (int i = 0; i < roles.size(); i++) {
				AttributeRole role = roles.get(i);
				if (collectionToFill.containsKey(role.getName())) {
					_logger.warn("You can't override existing attribute role : {} - {}", role.getName(), role.getDescription());
				} else {
					collectionToFill.put(role.getName(), role);
					_logger.info("Added new attribute role : {} - {}",role.getName(), role.getDescription());
				}
			}
		} catch (Throwable t) {
			//ApsSystemUtils.logThrowable(t, this, "executeLoading", "Error loading extra attribute Roles");
			_logger.error("Error loading extra attribute Roles", t);
		}
	}
	
}