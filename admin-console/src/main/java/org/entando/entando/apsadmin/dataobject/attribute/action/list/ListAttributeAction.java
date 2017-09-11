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
package org.entando.entando.apsadmin.dataobject.attribute.action.list;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agiletec.aps.system.common.entity.model.IApsEntity;
import org.entando.entando.aps.system.services.dataobject.model.DataObject;
import org.entando.entando.apsadmin.dataobject.DataObjectActionConstants;
import org.entando.entando.apsadmin.dataobject.helper.IDataObjectActionHelper;

/**
 * Classi action base delegata alla gestione delle operazione sugli attributi di
 * DataObject tipo lista.
 *
 * @author E.Santoboni
 */
public class ListAttributeAction extends com.agiletec.apsadmin.system.entity.attribute.action.list.ListAttributeAction {

	private static final Logger _logger = LoggerFactory.getLogger(ListAttributeAction.class);

	/*
	@Override
	public String addListElement() {
		try {
			super.addListElement();
			Content content = this.getContent();
			int index = -1;
			ListAttributeInterface currentAttribute = (ListAttributeInterface) content.getAttribute(this.getAttributeName());
			String nestedType = currentAttribute.getNestedAttributeTypeCode();
			if (!nestedType.equals("Attach") && !nestedType.equals("Image") && !nestedType.equals("Link")) {
				return SUCCESS;
			}
			if (currentAttribute instanceof MonoListAttribute) {
				List<AttributeInterface> attributes = ((MonoListAttribute) currentAttribute).getAttributes();
				index = attributes.size() - 1;
			} else if (currentAttribute instanceof ListAttribute) {
				List<AttributeInterface> attributes = ((ListAttribute) currentAttribute).getAttributeList(this.getListLangCode());
				index = attributes.size() - 1;
			}
			this.setElementIndex(index);
			if (nestedType.equals("Attach") || nestedType.equals("Image")) {
				this.setResourceTypeCode(nestedType);
				return "chooseResource";
			} else {
				return "chooseLink";
			}
		} catch (Throwable t) {
			_logger.error("error in addListElement", t);
			//ApsSystemUtils.logThrowable(t, this, "addListElement");
			return FAILURE;
		}
	}
	 */
	@Override
	protected IApsEntity getCurrentApsEntity() {
		return this.updateContentOnSession();
	}

	public DataObject getContent() {
		return (DataObject) this.getRequest().getSession()
				.getAttribute(DataObjectActionConstants.SESSION_PARAM_NAME_CURRENT_DATA_OBJECT_PREXIX + this.getContentOnSessionMarker());
	}

	protected DataObject updateContentOnSession() {
		DataObject content = this.getContent();
		this.getDataObjectActionHelper().updateEntity(content, this.getRequest());
		return content;
	}

	public String getEntryContentAnchorDest() {
		return "contentedit_" + this.getListLangCode() + "_" + this.getAttributeName();
	}

	public String getContentOnSessionMarker() {
		return _dataObjectOnSessionMarker;
	}

	public void setContentOnSessionMarker(String dataObjectOnSessionMarker) {
		this._dataObjectOnSessionMarker = dataObjectOnSessionMarker;
	}

	public IDataObjectActionHelper getDataObjectActionHelper() {
		return _dataObjectActionHelper;
	}

	public void setDataObjectActionHelper(IDataObjectActionHelper dataObjectActionHelper) {
		this._dataObjectActionHelper = dataObjectActionHelper;
	}

	private String _dataObjectOnSessionMarker;

	private IDataObjectActionHelper _dataObjectActionHelper;

}
