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
package com.agiletec.aps.system.common.entity.model.attribute;

/**
 * Interface used for the Entity Attributes of both "List" and "Monolist"
 * Monolist list are those lists that do no support multiple languages (unlike
 * their elements that can support ultiple languages, depending on the attributes
 * their are made of).
 * @author M.Diana
 */
public interface ListAttributeInterface extends AttributeInterface {
	
	/**
	 * Set up an attribute object to use as prototype to create the elements 
	 * to add to the list of attributes. 
	 * @param attribute The attribute prototype
	 */
	public void setNestedAttributeType(AttributeInterface attribute);

	/**
	 * Return the identifying string of the attribute types which will be held in
	 * this class.
	 * @return The code of the attribute type.
	 */
	public String getNestedAttributeTypeCode();
	
}
