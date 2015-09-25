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
