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
package com.agiletec.aps.system.common.entity.parse;

import org.jdom.Element;

/**
 * JDOM Interface that maps an Entity Type to a XML object.
 * @author E.Santoboni
 */
public interface IApsEntityDOM {

	/**
	 * DOM initialization.
	 * Method to invoke	when fields valorization is starting.
	 */
	public void init();
	
	public void dispose();
	
	/**
	 * Set up the entity ID.
	 * @param id The entity ID.
	 */
	public void setId(String id);

	/**
	 * Set up the Entity Type code.
	 * @param typeCode The Entity Tyoe code.
	 */
	public void setTypeCode(String typeCode);

	/**
	 * Assign a description to the current Entity Type. 
	 * @param typeDescription The description.
	 */
	public void setTypeDescription(String typeDescription);
	@Deprecated
	public void setTypeDescr(String typeDescr);
	
	/**
	 *Set the entity description.
	 * @param description The description
	 */
	public void setDescription(String description);
	@Deprecated
	public void setDescr(String descr);
	
	/**
	 * Set the main group this entity belongs to.
	 * @param group The main group.
	 */
	public void setMainGroup(String group);

	/**
	 * Add the code of a group authorized to visualize the entity.
	 * @param groupName The group to add.
	 */
	public void addGroup(String groupName);

	/**
	 * Add, setting its value, a new element to the categories tag.
	 * @param categoryCode The value of the category tag.
	 */
	public void addCategory(String categoryCode);

	/**
	 * Add a new attribute to a tag.
	 * @param attributeElem The element, which corresponds to an attribute, to add
	 * to the entity XML structure.
	 */
	public void addAttribute(Element attributeElem);

	/**
	 * Return the XML structure of the entity.
	 * @return String The XML structure of the entity.
	 */
	public String getXMLDocument();

	/**
	 * Assign a name to the root element.
	 * @param rootElementName The name of the root element
	 */
	public void setRootElementName(String rootElementName);


}
