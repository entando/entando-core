/*
 * Copyright 2013-Present Entando Inc. (http://www.entando.com) All rights reserved.
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
package com.agiletec.plugins.jacms.aps.system.services.content.model.extraAttribute;

import java.util.List;

import com.agiletec.aps.system.services.lang.Lang;
import com.agiletec.plugins.jacms.aps.system.services.content.model.CmsAttributeReference;

/**
 * Interfaccia base per gli attributi specifici per il cms.
 * @author E.Santoboni
 */
public interface IReferenceableAttribute {
	
	/**
	 * Restituisce la lista di referenze (in oggetti 
	 * tipo CmsAttributeReference) generati dall'attributo.
	 * @param systemLangs The system langs.
	 * @return La lista di referenze.
	 */
	public List<CmsAttributeReference> getReferences(List<Lang> systemLangs);
	
}
