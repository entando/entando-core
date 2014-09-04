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
package com.agiletec.apsadmin.system.entity.type;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.BeanComparator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.BeanFactoryAware;

import com.agiletec.aps.system.common.entity.IEntityManager;
import com.agiletec.aps.system.common.entity.model.IApsEntity;
import com.agiletec.aps.system.common.entity.model.attribute.AbstractListAttribute;
import com.agiletec.aps.system.common.entity.model.attribute.AttributeInterface;
import com.agiletec.aps.system.common.entity.model.attribute.CompositeAttribute;
import com.agiletec.aps.system.common.entity.model.attribute.MonoListAttribute;
import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.apsadmin.system.ApsAdminSystemConstants;

/**
 * @author E.Santoboni
 */
public class CompositeAttributeConfigAction extends AbstractBaseEntityAttributeConfigAction implements ICompositeAttributeConfigAction, BeanFactoryAware {

	private static final Logger _logger = LoggerFactory.getLogger(CompositeAttributeConfigAction.class);
	
	@Override
	public void validate() {
		super.validate();
		CompositeAttribute composite = this.getCompositeAttributeOnEdit();
		if (null != composite.getAttribute(this.getAttributeName())) {
			String[] args = {this.getAttributeName()};
			this.addFieldError("attributeName", this.getText("error.entity.attribute.name.already.exists", args));
		}
	}
	
	@Override
	public String addAttributeElement() {
		this.setStrutsAction(ApsAdminSystemConstants.ADD);
		return SUCCESS;
	}
	
	@Override
	public String moveAttributeElement() {
		try {
			CompositeAttribute composite = this.getCompositeAttributeOnEdit();
			int elementIndex = this.getAttributeIndex();
			String movement = this.getMovement();
			List<AttributeInterface> attributes = composite.getAttributes();
			if (!(elementIndex==0 && movement.equals(ApsAdminSystemConstants.MOVEMENT_UP_CODE)) && 
					!(elementIndex==attributes.size()-1 && movement.equals(ApsAdminSystemConstants.MOVEMENT_DOWN_CODE))) {
				AttributeInterface attributeToMove = attributes.get(elementIndex);
				attributes.remove(elementIndex);
				if (movement.equals(ApsAdminSystemConstants.MOVEMENT_UP_CODE)) {
					attributes.add(elementIndex-1, attributeToMove);
				}
				if (movement.equals(ApsAdminSystemConstants.MOVEMENT_DOWN_CODE)) {
					attributes.add(elementIndex+1, attributeToMove);
				}
			}
		} catch (Throwable t) {
			_logger.error("error in moveAttribute", t);
			//ApsSystemUtils.logThrowable(t, this, "moveAttribute");
			return FAILURE;
		}
		return SUCCESS;
	}
	
	@Override
	public String removeAttributeElement() {
		try {
			int elementIndex = this.getAttributeIndex();
			CompositeAttribute composite = this.getCompositeAttributeOnEdit();
			AttributeInterface attributeToRemove = composite.getAttributes().get(elementIndex);
			composite.getAttributes().remove(elementIndex);
			composite.getAttributeMap().remove(attributeToRemove.getName());
		} catch (Throwable t) {
			_logger.error("error in removeAttribute", t);
			//ApsSystemUtils.logThrowable(t, this, "removeAttribute");
			return FAILURE;
		}
		return SUCCESS;
	}
	
	@Override
	public String saveAttributeElement() {
		try {
			CompositeAttribute composite = this.getCompositeAttributeOnEdit();
			if (this.getStrutsAction() == ApsAdminSystemConstants.EDIT) {
				throw new RuntimeException("The edit operation on this attribute is not supported");
			} else {
				AttributeInterface attribute = this.getAttributePrototype(this.getAttributeTypeCode());
				attribute.setName(this.getAttributeName());
				super.fillAttributeFields(attribute);
				composite.getAttributes().add(attribute);
				composite.getAttributeMap().put(attribute.getName(), attribute);
			}
		} catch (Throwable t) {
			_logger.error("error in saveAttributeElement", t);
			//ApsSystemUtils.logThrowable(t, this, "saveAttributeElement");
			return FAILURE;
		}
		return SUCCESS;
	}
	
	@Override
	public String saveCompositeAttribute() {
		try {
			CompositeAttribute composite = this.getCompositeAttributeOnEdit();
			IApsEntity entityType = this.getEntityType();
			AttributeInterface attribute = (AttributeInterface) entityType.getAttribute(composite.getName());
			if (attribute instanceof MonoListAttribute) {
				((MonoListAttribute) attribute).setNestedAttributeType(composite);
			} else if (!attribute.getName().equals(composite.getName())) {
				throw new ApsSystemException("Attribute Name '" + attribute.getName() + "' incompatible with composite Attribute name '" + composite.getName() + "'");
			} else if (!attribute.getType().equals(composite.getType())) {
				throw new ApsSystemException("Attribute Type '" + attribute.getType() + "' incompatible with composite Attribute type '" + composite.getType() + "'");
			}
			this.getRequest().getSession().removeAttribute(COMPOSITE_ATTRIBUTE_ON_EDIT_SESSION_PARAM);
		} catch (Throwable t) {
			_logger.error("error in saveAttribute", t);
			//ApsSystemUtils.logThrowable(t, this, "saveAttribute");
			return FAILURE;
		}
		return SUCCESS;
	}
	
	public List<AttributeInterface> getAllowedAttributeElementTypes() {
		List<AttributeInterface> attributes = new ArrayList<AttributeInterface>();
		try {
			IEntityManager entityManager = this.getEntityManager();
			Map<String, AttributeInterface> attributeTypes = entityManager.getEntityAttributePrototypes();
			Iterator<AttributeInterface> attributeIter = attributeTypes.values().iterator();
			while (attributeIter.hasNext()) {
				AttributeInterface attribute = attributeIter.next();
				if (attribute.isSimple()) {
					attributes.add(attribute);
				}
			}
			Collections.sort(attributes, new BeanComparator("type"));
		} catch (Throwable t) {
			_logger.error("Error extracting the allowed types of attribute elements", t);
			//ApsSystemUtils.logThrowable(t, this, "getAllowedAttributeElementTypes");
			throw new RuntimeException("Error extracting the allowed types of attribute elements", t);
		}
		return attributes;
	}
	
	public CompositeAttribute getCompositeAttributeOnEdit() {
		return (CompositeAttribute) this.getRequest().getSession().getAttribute(COMPOSITE_ATTRIBUTE_ON_EDIT_SESSION_PARAM);
	}
	
	public AbstractListAttribute getListAttribute() {
		return (AbstractListAttribute) this.getRequest().getSession().getAttribute(IListElementAttributeConfigAction.LIST_ATTRIBUTE_ON_EDIT_SESSION_PARAM);
	}
	
	public int getAttributeIndex() {
		return _attributeIndex;
	}
	public void setAttributeIndex(int attributeIndex) {
		this._attributeIndex = attributeIndex;
	}
	
	public String getMovement() {
		return _movement;
	}
	public void setMovement(String movement) {
		this._movement = movement;
	}
	
	private int _attributeIndex;
	private String _movement;
	
}