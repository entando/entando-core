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
package com.agiletec.aps.system.common.entity.loader;

import com.agiletec.aps.system.common.IManager;
import com.agiletec.aps.system.common.entity.IEntityManager;
import com.agiletec.aps.system.common.entity.model.attribute.AttributeInterface;

/**
 * The Wrapper Class of the extra attribute.
 * @author E.Santoboni
 */
public class ExtraAttributeWrapper {
	
	public AttributeInterface getAttribute() {
		return _attribute;
	}
	public void setAttribute(AttributeInterface attribute) {
		this._attribute = attribute;
	}
	
	/**
	 * (**DEPRECATED since Entando 3.0.1** Use setEntityManagerNameDest) Set the entity manager destination.
	 * @param entityManagerDest The entity manager destination.
	 * @deprecated Since Entando 3.0.1. To avoid circolar references. Use setEntityManagerNameDest
	 */
	public void setEntityManagerDest(IEntityManager entityManagerDest) {
		String name = ((IManager) entityManagerDest).getName();
		this.setEntityManagerNameDest(name);
	}
	
	protected String getEntityManagerNameDest() {
		return _entityManagerNameDest;
	}
	public void setEntityManagerNameDest(String entityManagerNameDest) {
		this._entityManagerNameDest = entityManagerNameDest;
	}
	
	private AttributeInterface _attribute;
	private String _entityManagerNameDest;
	
}