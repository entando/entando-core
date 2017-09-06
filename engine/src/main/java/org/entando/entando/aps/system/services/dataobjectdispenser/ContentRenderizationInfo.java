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
package org.entando.entando.aps.system.services.dataobjectdispenser;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.agiletec.aps.system.common.entity.model.attribute.AttributeInterface;
import com.agiletec.aps.system.common.entity.model.attribute.AttributeRole;
import org.entando.entando.aps.system.services.dataobject.model.DataObject;

/**
 * Represents the rendering information of a dataobject. The enhanced object is
 * cached by alphanumeric identifier produced by the identifier of the
 * dataobject, the model an the rendering lang.
 *
 * @author E.Santoboni
 */
public class ContentRenderizationInfo implements Serializable {

	private static final long serialVersionUID = 2514275355950411866L;

	protected ContentRenderizationInfo() {
	}

	public ContentRenderizationInfo(DataObject dataObject,
			String cachedRenderedDataobject, long modelId, String renderingLang, List<AttributeRole> roles) {
		if (null != dataObject) {
			this._dataObjectId = dataObject.getId();
			this._dataType = dataObject.getTypeCode();
		}
		this._renderedDataobject = cachedRenderedDataobject;
		this._renderingLang = renderingLang;
		this._modelId = modelId;
		if (null == roles) {
			return;
		}
		for (int i = 0; i < roles.size(); i++) {
			AttributeRole role = roles.get(i);
			AttributeInterface attribute = dataObject.getAttributeByRole(role.getName());
			if (null != attribute) {
				this.getAttributeValues().put(role.getName(), attribute.getValue());
			}
		}
	}

	@Override
	public ContentRenderizationInfo clone() {
		ContentRenderizationInfo clone = new ContentRenderizationInfo();
		clone.setContentId(this.getContentId());
		clone.setContentType(this.getContentType());
		clone.setModelId(this.getModelId());
		clone.setRenderedDataobject(this.getRenderedDataobject());
		clone.setRenderingLang(this.getRenderingLang());
		clone.getAttributeValues().putAll(this.getAttributeValues());
		return clone;
	}

	public String getContentId() {
		return _dataObjectId;
	}

	protected void setContentId(String contentId) {
		this._dataObjectId = contentId;
	}

	public String getContentType() {
		return _dataType;
	}

	protected void setContentType(String contentType) {
		this._dataType = contentType;
	}

	public String getRenderedDataobject() {
		return _renderedDataobject;
	}

	public void setRenderedDataobject(String renderedDataobject) {
		this._renderedDataobject = renderedDataobject;
	}

	public String getRenderingLang() {
		return _renderingLang;
	}

	protected void setRenderingLang(String renderingLang) {
		this._renderingLang = renderingLang;
	}

	public long getModelId() {
		return _modelId;
	}

	protected void setModelId(long modelId) {
		this._modelId = modelId;
	}

	/**
	 * Return the map of the attribute values indexed by the attribute role.
	 *
	 * @return The map of the attribute values
	 */
	public Map<String, Object> getAttributeValues() {
		return _attributeValues;
	}

	/**
	 * Set the map of the attribute values indexed by the attribute role.
	 *
	 * @param attributeValues The map of the attribute values to set.
	 */
	public void setAttributeValues(Map<String, Object> attributeValues) {
		this._attributeValues = attributeValues;
	}

	private String _dataObjectId;
	private String _dataType;

	private String _renderedDataobject;
	private String _renderingLang;
	private long _modelId;

	private Map<String, Object> _attributeValues = new HashMap<String, Object>();

}
