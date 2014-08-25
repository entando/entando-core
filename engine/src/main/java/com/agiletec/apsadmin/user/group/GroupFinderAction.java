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
package com.agiletec.apsadmin.user.group;

import java.util.Collections;
import java.util.List;

import org.apache.commons.beanutils.BeanComparator;

import com.agiletec.aps.system.services.group.Group;
import com.agiletec.aps.system.services.group.IGroupManager;
import com.agiletec.apsadmin.system.BaseAction;

/**
 * Classe action della lista Gruppi.
 * @author E.Mezzano - E.Santoboni
 */
public class GroupFinderAction extends BaseAction implements IGroupFinderAction {
	
	@Override
	public List<Group> getGroups() {
		List<Group> groups = this.getGroupManager().getGroups();
		BeanComparator comparator = new BeanComparator("descr");
		Collections.sort(groups, comparator);
		return groups;
	}
	
	protected IGroupManager getGroupManager() {
		return _groupManager;
	}
	public void setGroupManager(IGroupManager groupManager) {
		this._groupManager = groupManager;
	}
	
	private IGroupManager _groupManager;
	
}