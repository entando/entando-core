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
