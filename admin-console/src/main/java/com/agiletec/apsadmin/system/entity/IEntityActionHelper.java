/*
 * Copyright 2015-Present Entando Inc. (http://www.entando.com) All rights reserved.
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
package com.agiletec.apsadmin.system.entity;

import javax.servlet.http.HttpServletRequest;

import com.agiletec.aps.system.common.entity.model.ApsEntity;
import com.agiletec.aps.system.common.entity.model.EntitySearchFilter;
import com.agiletec.aps.system.common.entity.model.IApsEntity;
import com.opensymphony.xwork2.ActionSupport;

/**
 * Base interface for the helper classes that support the actions which
 * handle the elements based on 'ApsEntity'.
 * @author E.Santoboni
 */
public interface IEntityActionHelper {

	public void updateEntity(IApsEntity currentEntity, HttpServletRequest request);

	public void scanEntity(IApsEntity currentEntity, ActionSupport action);

	/**
	 * Return the entity (attribute) search filter.
	 * @param entityFinderAction The finder action
	 * @param prototype The entity prototype
	 * @return the entity search filter.
	 * @deprecated use getAttributeFilters(AbstractApsEntityFinderAction, IApsEntity)
	 */
	public EntitySearchFilter[] getSearchFilters(AbstractApsEntityFinderAction entityFinderAction, IApsEntity prototype);

	/**
	 * Return the entity role search filter.
	 * @param entityFinderAction The finder action
	 * @return the search filter.
	 */
	public EntitySearchFilter[] getRoleFilters(AbstractApsEntityFinderAction entityFinderAction);

	/**
	 * Return the entity attribute search filter.
	 * @param entityFinderAction The finder action
	 * @param prototype The entity prototype
	 * @return the search filter.
	 */
	public EntitySearchFilter[] getAttributeFilters(AbstractApsEntityFinderAction entityFinderAction, IApsEntity prototype);

	
	/**
	 * Returns the AttributeFilter field name(s). 
	 * If the attribute expects a start/end value (DateAttribute and NumberAttrivute ) the the returned array will contain two elements, otherwise one.
	 * @param prototype The entity prototype used to extract the attributes
	 * @param attrName The name of the attribute 
	 * @return an array of field names used as AttributeFilter
	 */
	public String[] getAttributeFilterFieldName(ApsEntity prototype, String attrName);

}
