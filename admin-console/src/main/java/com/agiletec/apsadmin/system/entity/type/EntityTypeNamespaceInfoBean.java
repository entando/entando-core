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
package com.agiletec.apsadmin.system.entity.type;

/**
 * @author E.Santoboni
 */
public final class EntityTypeNamespaceInfoBean {
	
	public String getEntityManagerName() {
		return _entityManagerName;
	}
	public void setEntityManagerName(String entityManagerName) {
		this._entityManagerName = entityManagerName;
	}
	public String getNamespacePrefix() {
		return _namespacePrefix;
	}
	public void setNamespacePrefix(String namespacePrefix) {
		this._namespacePrefix = namespacePrefix;
	}
	
	private String _entityManagerName;
	private String _namespacePrefix;
	
}
