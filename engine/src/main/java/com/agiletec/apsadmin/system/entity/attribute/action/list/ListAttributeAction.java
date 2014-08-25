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
package com.agiletec.apsadmin.system.entity.attribute.action.list;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agiletec.aps.system.common.entity.model.IApsEntity;
import com.agiletec.aps.system.common.entity.model.attribute.AttributeInterface;
import com.agiletec.aps.system.common.entity.model.attribute.ListAttribute;
import com.agiletec.aps.system.common.entity.model.attribute.ListAttributeInterface;
import com.agiletec.aps.system.common.entity.model.attribute.MonoListAttribute;
import com.agiletec.apsadmin.system.BaseAction;

/**
 * This action class implements all the methods needed to handle the attributes of the list
 * @author E.Santoboni
 */
public abstract class ListAttributeAction extends BaseAction implements IListAttributeAction {

	private static final Logger _logger = LoggerFactory.getLogger(ListAttributeAction.class);
	
	@Override
	public String addListElement() {
		IApsEntity entity = this.getCurrentApsEntity();
		try {
			ListAttributeInterface currentAttribute = (ListAttributeInterface) entity.getAttribute(this.getAttributeName());
			if (currentAttribute instanceof MonoListAttribute) {
				((MonoListAttribute) currentAttribute).addAttribute();
			} else if (currentAttribute instanceof ListAttribute) {
				((ListAttribute) currentAttribute).addAttribute(this.getListLangCode());
			}
			_logger.debug("Added element of type {} to the list {}", currentAttribute.getNestedAttributeTypeCode(), currentAttribute.getName());
		} catch (Throwable t) {
			_logger.error("error in addListElement", t);
			//ApsSystemUtils.logThrowable(t, this, "addListElement");
			return FAILURE;
		}
		return SUCCESS;
	}
	
	@Override
	public String moveListElement() {
		IApsEntity entity = this.getCurrentApsEntity();
		try {
			int elementIndex = this.getElementIndex();
			ListAttributeInterface currentAttribute = (ListAttributeInterface) entity.getAttribute(this.getAttributeName());
			if (currentAttribute instanceof MonoListAttribute) {
				List<AttributeInterface> monoList = ((MonoListAttribute) currentAttribute).getAttributes();		
				this.moveListElement(monoList, elementIndex, this.getMovement());
			} else if (currentAttribute instanceof ListAttribute) {
				List<AttributeInterface> list = ((ListAttribute) currentAttribute).getAttributeList(this.getListLangCode());
				this.moveListElement(list, elementIndex, this.getMovement());
			}
			_logger.debug("Moved element of type {} of the list {} in the position {} with a '{}' movement ", currentAttribute.getNestedAttributeTypeCode(), currentAttribute.getName(), elementIndex, this.getMovement());
		} catch (Throwable t) {
			_logger.error("error in moveListElement", t);
			//ApsSystemUtils.logThrowable(t, this, "moveListElement");
			return FAILURE;
		}
		return SUCCESS;
	}
	
	/**
	 * Request the movement of the given element in the list. Note that this
	 * method blocks not allowed movements.
	 * 
	 * @param list The list holding the element to move
	 * @param elementIndex The index of the element to move
	 * @param movement The code of the requested movement
	 */
	protected void moveListElement(List<AttributeInterface> list, int elementIndex, String movement) {
		if (!(elementIndex==0 && movement.equals(IListAttributeAction.MOVEMENT_UP_CODE)) && 
				!(elementIndex==list.size()-1 && movement.equals(IListAttributeAction.MOVEMENT_DOWN_CODE))) {
			AttributeInterface elementAttributeToMove = (AttributeInterface) list.get(elementIndex);
			list.remove(elementIndex);
			if (movement.equals(IListAttributeAction.MOVEMENT_UP_CODE)) {
				list.add(elementIndex-1, elementAttributeToMove);
			} 
			if (movement.equals(IListAttributeAction.MOVEMENT_DOWN_CODE)) {
				list.add(elementIndex+1, elementAttributeToMove);
			}
		}
	}
	
	@Override
	public String removeListElement() {
		IApsEntity entity = this.getCurrentApsEntity();
		try {
			int elementIndex = this.getElementIndex();
			ListAttributeInterface currentAttribute = (ListAttributeInterface) entity.getAttribute(this.getAttributeName());
			if (currentAttribute instanceof MonoListAttribute) {
				((MonoListAttribute) currentAttribute).removeAttribute(elementIndex);
			} else if (currentAttribute instanceof ListAttribute) {
				((ListAttribute) currentAttribute).removeAttribute(this.getListLangCode(), elementIndex);
			}
			_logger.debug("Element oy type {} removed fomr the list {}", currentAttribute.getNestedAttributeTypeCode(), currentAttribute.getName());
		} catch (Throwable t) {
			_logger.error("error in removeListElement", t);
			//ApsSystemUtils.logThrowable(t, this, "removeListElement");
			return FAILURE;
		}
		return SUCCESS;
	}
	
	protected abstract IApsEntity getCurrentApsEntity();
	
	public String getAttributeName() {
		return _attributeName;
	}
	public void setAttributeName(String attributeName) {
		this._attributeName = attributeName;
	}
	
	public int getElementIndex() {
		return _elementIndex;
	}
	public void setElementIndex(int elementIndex) {
		this._elementIndex = elementIndex;
	}
	
	public String getListLangCode() {
		return _listLangCode;
	}
	public void setListLangCode(String listLangCode) {
		this._listLangCode = listLangCode;
	}
	
	public String getMovement() {
		return _movement;
	}
	public void setMovement(String movement) {
		this._movement = movement;
	}
	
	private String _attributeName;
	private int _elementIndex;
	private String _listLangCode;
	private String _movement;
	
}
