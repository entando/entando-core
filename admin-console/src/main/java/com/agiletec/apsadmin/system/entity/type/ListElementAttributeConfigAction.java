/*
 * Copyright 2013-Present Entando Corporation (http://www.entando.com) All rights reserved.
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
package com.agiletec.apsadmin.system.entity.type;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agiletec.aps.system.common.entity.model.attribute.AbstractListAttribute;
import com.agiletec.aps.system.common.entity.model.attribute.AttributeInterface;

/**
 * @author E.Santoboni
 */
public class ListElementAttributeConfigAction extends AbstractBaseEntityAttributeConfigAction implements IListElementAttributeConfigAction {

	private static final Logger _logger = LoggerFactory.getLogger(ListElementAttributeConfigAction.class);
	
	@Override
	public String configureListElement() {
		this.valueFormFields(this.getAttributeElement());
		return SUCCESS;
	}
	
	@Override
	public String saveListElement() {
		try {
			AttributeInterface attribute = this.getAttributeElement();
			this.fillAttributeFields(attribute);
			this.getRequest().getSession().removeAttribute(IListElementAttributeConfigAction.LIST_ATTRIBUTE_ON_EDIT_SESSION_PARAM);
			this.getRequest().getSession().removeAttribute(IListElementAttributeConfigAction.LIST_ELEMENT_ON_EDIT_SESSION_PARAM);
		} catch (Throwable t) {
			_logger.error("error in saveListElement", t);
			//ApsSystemUtils.logThrowable(t, this, "saveListElement");
			return FAILURE;
		}
		return SUCCESS;
	}
	
	public AbstractListAttribute getListAttribute() {
		return (AbstractListAttribute) this.getRequest().getSession().getAttribute(LIST_ATTRIBUTE_ON_EDIT_SESSION_PARAM);
	}
	
	public AttributeInterface getAttributeElement() {
		return (AttributeInterface) this.getRequest().getSession().getAttribute(LIST_ELEMENT_ON_EDIT_SESSION_PARAM);
	}
	
}
