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
package org.entando.entando.aps.system.common.entity.model.attribute.util;

import com.agiletec.aps.util.SelectItem;
import java.util.List;

/**
 * Base interface for those bean classes that must extract 'EnumeratorMap' Attribute. 
 * @author E.Santoboni
 */
public interface EnumeratorMapAttributeItemsExtractor {
	
	/**
	 * Return the list of the items of the 'Enumerator' attribute.
	 * @return The items list.
	 */
	public List<SelectItem> getMapItems();
	
}