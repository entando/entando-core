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
package com.agiletec.apsadmin.tags;

import org.apache.struts2.components.Component;

import com.agiletec.aps.system.common.entity.model.AttributeTracer;
import com.agiletec.aps.system.services.lang.Lang;
import com.opensymphony.xwork2.util.ValueStack;

/**
 * Component del tag TracerFactoryTag.
 * Il componente si occupa della creazione e restituisce di 
 * un tracciatore valorizzato con la lingua specificata.
 * @version 1.0
 * @author S.Puddu
 */
public class TracerFactory extends Component {
	
	public TracerFactory(ValueStack stack, Lang lang) {
		super(stack);
		this._attributeTracer = new AttributeTracer();
		this._attributeTracer.setLang(lang);
	}
	
	public void setAttributeTracer(AttributeTracer attributeTracer) {
		this._attributeTracer = attributeTracer;
	}
	
	public AttributeTracer getAttributeTracer() {
		return _attributeTracer;
	}
	
	private AttributeTracer _attributeTracer;
	
}
