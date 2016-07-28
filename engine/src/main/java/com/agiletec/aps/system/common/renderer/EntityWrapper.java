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
package com.agiletec.aps.system.common.renderer;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.BeanFactory;

import com.agiletec.aps.system.common.entity.model.IApsEntity;
import com.agiletec.aps.system.common.entity.model.attribute.AbstractComplexAttribute;
import com.agiletec.aps.system.common.entity.model.attribute.AttributeInterface;
import com.agiletec.aps.system.services.category.Category;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class represents an entity suitable for the rendering process. This class extends
 * the HashMap object to make the access to the entity objects easier.
 * @author M.Diana - W.Ambu - E.Santoboni
 */
public class EntityWrapper implements Map {
	
	private static final Logger _logger = LoggerFactory.getLogger(EntityWrapper.class);
	
	/**
	 * Wrapper initialization.
	 * @param entity The entity to pass to the rendering service.
	 */
	public EntityWrapper(IApsEntity entity) {
		this._entity = entity;
		this._attributeMap = _entity.getAttributeMap();
	}
	
	public EntityWrapper(IApsEntity entity, BeanFactory beanFactory) {
		this(entity);
		this.setBeanFactory(beanFactory);
	}
	
	/**
	 * Return the informations needed for the renderization. In presence of simple attributes,
	 * it return the attributes; for complex ones it returns a structure suitable for
	 * the renderization - for instance, a list is returned to render a monolist.
	 * @param key The name of the attribute.
	 * @return An attribute or a List Attribute.
	 * @see java.util.Map#get(java.lang.Object)
	 */
	@Override
	public Object get(Object key) {
		AttributeInterface attribute = this._attributeMap.get(key);
		if (null == attribute) {
			_logger.debug("Required null attribute {}", key);
			return null;
		}
		if (attribute.isSimple()) {
			return attribute;
		} else {
			AbstractComplexAttribute complexAttr = (AbstractComplexAttribute) attribute;
			return complexAttr.getRenderingAttributes();
		}
	}
	
	/**
	 * Return the categories list of the entity.
	 * @return The list of categories.
	 */
	public List<Category> getCategories() {
		List<Category> categories = new ArrayList<Category>();
		List<Category> contentCategories = this._entity.getCategories();
		for (int i=0; i<contentCategories.size(); i++) {
			Category cat = contentCategories.get(i);
			Category clone = cat.getCloneForWrapper();
			clone.setRenderingLang(this._renderingLang);
			categories.add(clone);
		}
		return categories;
	}
	
	/**
	 * Return the ID of the entity.
	 * @return The ID of the entity.
	 */
	public String getId() {
		return _entity.getId();
	}
	
	/**
	 * Set up the code of the rendering language.
	 * @param langCode The code of the rendering language.
	 */
	public void setRenderingLang(String langCode) {
		this._renderingLang = langCode;
		this._entity.setRenderingLang(this._renderingLang);
	}
	
	@Override
	public int size() {
		return _attributeMap.size();
	}
	
	@Override
	public boolean isEmpty() {
		return _attributeMap.isEmpty();
	}
	
	@Override
	public boolean containsKey(Object key) {
		return _attributeMap.containsKey(key);
	}
	
	@Override
	public boolean containsValue(Object value) {
		return _attributeMap.containsValue(value);
	}
	
	@Override
	public Set keySet() {
		return _attributeMap.keySet();
	}
	
	@Override
	public Collection values() {
		return _attributeMap.values();
	}
	
	@Override
	public Set entrySet() {
		return _attributeMap.entrySet();
	}	
	
	@Override
	public boolean equals(Object o) {
		return false;
	}
	
	@Override
	public int hashCode() {
		return _attributeMap.hashCode();
	}
	
	@Override
	public Object put(Object key, Object value) {
		throw new UnsupportedOperationException("Operation not permitted");
	}
	
	@Override
	public Object remove(Object key) {
		throw new UnsupportedOperationException("Operation not permitted");
	}
	
	@Override
	public void putAll(Map t) {
		throw new UnsupportedOperationException("Operation not permitted");
	}
	
	@Override
	public void clear() {
		throw new UnsupportedOperationException("Operation not permitted");
	}
	
	protected IApsEntity getEntity() {
		return _entity;
	}
	
	protected Map<String, AttributeInterface> getAttributeMap() {
		return _attributeMap;
	}
	
	protected String getRenderingLang() {
		return _renderingLang;
	}
	
	protected BeanFactory getBeanFactory() {
		return this._beanFactory;
	}
	protected void setBeanFactory(BeanFactory beanFactory) {
		this._beanFactory = beanFactory;
	}
	
	private IApsEntity _entity;
	private Map<String, AttributeInterface> _attributeMap;
	private String _renderingLang;
	
	private BeanFactory _beanFactory;
	
}
