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
package com.agiletec.aps.system.common.entity.model.attribute.util;

import java.util.List;

/**
 * Base interface for those bean classes that must extract 'Enumerator' Attribute. 
 * @author E.Santoboni
 */
public interface EnumeratorAttributeItemsExtractor {
	
	/**
	 * Return the list of the items of the 'Enumerator' attribute.
	 * @return The items list.
	 */
	public List<String> getItems();
	
}