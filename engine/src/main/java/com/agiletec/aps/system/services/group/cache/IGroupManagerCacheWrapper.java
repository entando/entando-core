/*
 * Copyright 2018-Present Entando Inc. (http://www.entando.com) All rights reserved.
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
package com.agiletec.aps.system.services.group.cache;

import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.aps.system.services.group.Group;
import com.agiletec.aps.system.services.group.IGroupDAO;
import java.util.Map;

/**
 * @author E.Santoboni
 */
public interface IGroupManagerCacheWrapper {

	public static final String GROUP_MANAGER_CACHE_NAME = "Entando_GroupManager";
	public static final String GROUP_CACHE_NAME_PREFIX = "GroupManager_group_";
	public static final String GROUP_CODES_CACHE_NAME = "GroupManager_groups";

	public void initCache(IGroupDAO groupDAO) throws ApsSystemException;

	public Map<String, Group> getGroups();

	public Group getGroup(String code);

	public void addGroup(Group group);

	public void updateGroup(Group group);

	public void removeGroup(Group group);

}
