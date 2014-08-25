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
	 * @param typeDescr The description.
	 */
	public void setTypeDescr(String typeDescr);

	/**
	 *Set the entity description.
	 * @param descr The description
	 */
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
