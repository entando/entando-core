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
package org.entando.entando.apsadmin.system.services.shortcut.model;

/**
 * A Menu Section Object
 * @author E.Santoboni
 */
public class MenuSection extends AbstractBaseBean {
	
	public MenuSection(String id) {
		super(id);
	}
	
	@Override
	public MenuSection clone() {
		MenuSection clone = new MenuSection(this.getId());
		clone.setDescription(this.getDescription());
		clone.setDescriptionKey(this.getDescriptionKey());
		clone.setLongDescription(this.getLongDescription());
		clone.setLongDescriptionKey(this.getLongDescriptionKey());
		return clone;
	}
	
}