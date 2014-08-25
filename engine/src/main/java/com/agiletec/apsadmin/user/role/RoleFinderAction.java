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
package com.agiletec.apsadmin.user.role;

import java.util.Collections;
import java.util.List;

import org.apache.commons.beanutils.BeanComparator;

import com.agiletec.aps.system.services.role.IRoleManager;
import com.agiletec.aps.system.services.role.Role;
import com.agiletec.apsadmin.system.BaseAction;

/**
 * Classi action della lista Ruoli.
 * @version 1.0
 * @author E.Santoboni
 */
public class RoleFinderAction extends BaseAction implements IRoleFinderAction {
	
	@Override
	public List<Role> getRoles() {
		List<Role> roles = this.getRoleManager().getRoles();
		BeanComparator comparator = new BeanComparator("description");
		Collections.sort(roles, comparator);
		return roles;
	}
	
	protected IRoleManager getRoleManager() {
		return _roleManager;
	}
	public void setRoleManager(IRoleManager roleManager) {
		this._roleManager = roleManager;
	}
	
	private IRoleManager _roleManager;
	
}