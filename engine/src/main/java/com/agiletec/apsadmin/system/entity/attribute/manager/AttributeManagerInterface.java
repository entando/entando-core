/*
*
* Copyright 2013 Entando S.r.l. (http://www.entando.com) All rights reserved.
*
* This file is part of Entando software.
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
