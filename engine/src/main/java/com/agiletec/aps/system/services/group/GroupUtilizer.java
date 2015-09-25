/*
 * Copyright 2013-Present Entando Inc. (http://www.entando.com) All rights reserved.
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
package com.agiletec.aps.system.services.group;

import java.util.List;

import com.agiletec.aps.system.exception.ApsSystemException;

/**
 * Basic interface for those services whose handled elements are based on groups.
 * @author E.Santoboni
 */
public interface GroupUtilizer {
	
	/**
	 * Return the id of the utilizing service.
	 * @return The id of the utilizer.
	 */
	public String getName();
	
	/**
	 * Return the list of the objects members of the group identified by the given name.
	 * @param groupName The name of the group
	 * @return The list of the objects members of the given group.
	 * @throws ApsSystemException In case of error
	 */
	public List getGroupUtilizers(String groupName) throws ApsSystemException;
	
}
