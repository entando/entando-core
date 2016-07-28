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

import java.io.Serializable;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.output.XMLOutputter;

import com.agiletec.aps.system.common.entity.ApsEntityManager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class generates the XML of the single entity, and it is used by all those managers that manages
 * ApsEntity elements.
 * This class is used by the managers that manage the ApsEntities; it is utilized by default in the 
 * Spring definition of the abstract service {@link ApsEntityManager}; this definition can be substituted
 * in the declaration of those services that, extending the 'ApsEntityManager'use a custom DOM class to
 * build the XML code that maps the structure of a new Entity Type. Such Entity Type must implement the 
 * 'IApsEntity' interface.
 * @author M.Morini - S.Didaci - E.Santoboni
 */
public class ApsEntityDOM implements IApsEntityDOM, Serializable {
	
	private static final Logger _logger =  LoggerFactory.getLogger(ApsEntityDOM.class);
	
	@Override
	public ApsEntityDOM clone() {
		ApsEntityDOM copy = null;
		try {
			copy = this.getClass().newInstance();
			this.copyInto(copy);
		} catch (Throwable t) {
			_logger.error("Error cloning {}" + this.getClass(), t);
			throw new RuntimeException("Error cloning " + this.getClass(), t);
		}
		return copy;
	}
	
	protected void copyInto(IApsEntityDOM copy) {
		copy.setRootElementName(this._rootElementName);
	}
	
	/**
	 * 
	 * DOM initialization;
	 * this method must be invoked when starting to populate the fields.
	 */
	@Override
	public void init() {
		this.buildDOM();
	}
	
	@Override
	public void dispose() {
		this._doc = null;
		this._root = null;
	}
	
	/**
	 * Assign the ID string to the entity.
	 * @param id The entity ID
	 */
	@Override
	public void setId(String id) {
		this._root.setAttribute("id", id);
	}
	
	/**
	 * Set up the Entity Type code
	 * @param typeCode The entity Type code.
	 */
	@Override
	public void setTypeCode(String typeCode) {
		this._root.setAttribute("typecode", typeCode);
	}
	
	/**
	 * Assign the given description to the Entity Type.
	 * @param typeDescription The Entity Type description to associate.
	 */
	@Override
	public void setTypeDescription(String typeDescription) {
		this._root.setAttribute("typedescr", typeDescription);
	}
	@Override
	@Deprecated
	public void setTypeDescr(String typeDescr) {
		this.setTypeDescription(typeDescr);
	}
	
	/**
	 * Assign the given description to the entity.
	 * @param description The entity description.
	 */
	@Override
	public void setDescription(String description) {
		this._root.getChild(TAG_DESCR).setText(description);
	}
	@Override
	@Deprecated
	public void setDescr(String description) {
		this.setDescription(description);
	}
	
	/**
	 * Assign the main group this entity belongs to.
	 * @param group The main group.
	 */
	@Override
	public void setMainGroup(String group){
		if (group != null) {
			this._root.getChild(TAG_GROUPS).setAttribute("mainGroup", group);
		}
	}
	
	/**
	 * Add the code of a group authorized to visualize the entity.
	 * @param groupName The group to add.
	 */
	@Override
	public void addGroup(String groupName) {
		if (null != groupName) {
			Element groupTag = new Element("group");
			groupTag.setAttribute("name", groupName);
			this._root.getChild(TAG_GROUPS).addContent(groupTag);
		}
	}
	
	/**
	 * Add, setting its value, a new element to the categories tag.
	 * @param categoryCode The value of the category tag.
	 */
	@Override
	public void addCategory(String categoryCode) {
		Element tag = new Element("category");
		tag.setAttribute("id", categoryCode);
		_root.getChild(TAG_CATEGORIES).addContent(tag);
	}
	
	/**
	 * Add a new attribute to a tag.
	 * @param attributeElem The element, which corresponds to an attribute, 
	 * to add to the entity XML structure.
	 */
	@Override
	public void addAttribute(Element attributeElem) {
		this._root.getChild(TAG_ATTRIBUTES).addContent(attributeElem);
	}

	private void buildDOM() {
		this._doc = new Document();
		this._root = new Element(this._rootElementName);
		for (int i = 0; i < TAGS.length; i++) {
			Element tag = new Element(TAGS[i]);
			_root.addContent(tag);
		}
		_doc.setRootElement(_root);
	}

	/**
	 * Return the XML structure of the entity.
	 * @return String The XML structure of the entity.
	 */
	@Override
	public String getXMLDocument(){
		XMLOutputter out = new XMLOutputter();
		String xml = out.outputString(_doc);
		return xml;
	}

	@Override
	public void setRootElementName(String rootElementName) {
		this._rootElementName = rootElementName;
	}

	/**
	 * The JDOM document of the entity.
	 */
	protected Document _doc;

	/**
	 * The root of the JDOM document.
	 */
	protected Element _root;

	private final static String TAG_DESCR = "descr";
	private final static String TAG_GROUPS = "groups";
	private final static String TAG_CATEGORIES = "categories";
	private final static String TAG_ATTRIBUTES = "attributes";
	private final static String[] TAGS = {TAG_DESCR, TAG_GROUPS, TAG_CATEGORIES, TAG_ATTRIBUTES};

	private String _rootElementName;

}
