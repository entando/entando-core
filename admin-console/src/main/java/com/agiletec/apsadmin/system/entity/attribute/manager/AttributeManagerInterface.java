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
package com.agiletec.apsadmin.system.entity.attribute.manager;

import javax.servlet.http.HttpServletRequest;

import com.agiletec.aps.system.common.entity.model.AttributeFieldError;
import com.agiletec.aps.system.common.entity.model.attribute.AttributeInterface;
import com.opensymphony.xwork2.ActionSupport;

/**
 * Basic interface for the attributes managers.
 * The manager, specific for every entity type, handles the update and verification processes of
 * every single Entity Attribute.
 * @author E.Santoboni
 */
public interface AttributeManagerInterface {

    public void updateEntityAttribute(AttributeInterface attribute, HttpServletRequest request);

	/**
	 * Return the error message for the given Attribute field error.
	 * @param attributeFieldError The Field error
	 * @param action The current action.
	 * @return The error message for the given attribute error.
	 */
    public String getErrorMessage(AttributeFieldError attributeFieldError, ActionSupport action);

}
