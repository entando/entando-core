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
package com.agiletec.plugins.jacms.apsadmin.portal.specialwidget.listviewer;

import java.util.Properties;

import com.agiletec.aps.system.common.entity.model.EntitySearchFilter;

/**
 * Classe action per la configurazione del filtro su attributo tipo "Boolean".
 * @author E.Santoboni
 */
public class BooleanAttributeFilterAction extends BaseFilterAction {
	
	@Override
	protected Properties createFilterProperties() {
		Properties properties =  super.createFilterProperties();
		if (null != this.getBooleanValue() && this.getBooleanValue().trim().length()>0) {
			properties.put(EntitySearchFilter.VALUE_PARAM, this.getBooleanValue());
		}
		return properties;
	}
	
	@Override
	public int getFilterTypeId() {
		return BOOLEAN_ATTRIBUTE_FILTER_TYPE;
	}
	
	public String getBooleanValue() {
		return _booleanValue;
	}
	public void setBooleanValue(String value) {
		this._booleanValue = value;
	}
	
	private String _booleanValue;
	
}
