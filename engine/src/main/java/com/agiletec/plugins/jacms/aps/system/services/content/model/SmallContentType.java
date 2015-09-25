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
package com.agiletec.plugins.jacms.aps.system.services.content.model;

import com.agiletec.aps.system.common.entity.model.SmallEntityType;

import java.io.Serializable;

/**
 * @author E.Santoboni
 * @deprecated  From Entando 4.1.2, use {@link SmallEntityType} class 
 */
public class SmallContentType extends SmallEntityType implements Serializable {
	
	public String getDescr() {
		return super.getDescription();
	}
	public void setDescr(String descr) {
		super.setDescription(descr);
	}
	
}