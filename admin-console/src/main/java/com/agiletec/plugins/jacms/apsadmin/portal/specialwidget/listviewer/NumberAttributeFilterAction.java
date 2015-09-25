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
 * Classe action per la configurazione del filtro su attributo tipo "Number".
 * @author E.Santoboni
 */
public class NumberAttributeFilterAction extends BaseFilterAction {
	
	@Override
	protected Properties createFilterProperties() {
		Properties properties =  super.createFilterProperties();
		int filterOption = this.getFilterOptionId();
		if (filterOption == VALUE_FILTER_OPTION) {
			this.setNumberFilterParam(properties, EntitySearchFilter.VALUE_PARAM, this.getNumberValue());
		}
		if (filterOption == RANGE_FILTER_OPTION) {
			this.setNumberFilterParam(properties, EntitySearchFilter.START_PARAM, this.getNumberStart());
			this.setNumberFilterParam(properties, EntitySearchFilter.END_PARAM, this.getNumberEnd());
		}
		return properties;
	}
	
	protected void setNumberFilterParam(Properties properties, String paramName, Integer number) {
		if (null != number) {
			properties.put(paramName, String.valueOf(number));
		}
	}
	
	@Override
	public int getFilterTypeId() {
		return NUMBER_ATTRIBUTE_FILTER_TYPE;
	}
	
	public Integer getNumberValue() {
		return _numberValue;
	}
	public void setNumberValue(Integer numberValue) {
		this._numberValue = numberValue;
	}
	
	public Integer getNumberStart() {
		return _numberStart;
	}
	public void setNumberStart(Integer numberStart) {
		this._numberStart = numberStart;
	}
	
	public Integer getNumberEnd() {
		return _numberEnd;
	}
	public void setNumberEnd(Integer numberEnd) {
		this._numberEnd = numberEnd;
	}
	
	private Integer _numberValue;
	private Integer _numberStart;
	private Integer _numberEnd;
	
}
