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
import com.agiletec.aps.system.common.entity.model.attribute.AttributeRole;
import com.agiletec.aps.system.common.entity.model.attribute.CompositeAttribute;
import com.agiletec.aps.system.common.entity.model.attribute.EnumeratorAttribute;
import com.agiletec.aps.system.common.entity.model.attribute.ListAttribute;
import com.agiletec.aps.system.common.entity.model.attribute.MonoListAttribute;
import com.agiletec.aps.system.services.lang.Lang;
import com.agiletec.apsadmin.system.ApsAdminSystemConstants;

/**
 * @author E.Santoboni
 */
public class EntityAttributeConfigAction extends AbstractBaseEntityAttributeConfigAction implements IEntityAttributeConfigAction, BeanFactoryAware {

	private static final Logger _logger = LoggerFactory.getLogger(EntityAttributeConfigAction.class);
	
	@Override
	public void validate() {
		super.validate();
		IApsEntity entityType = this.getEntityType();
		String attributeName = this.getAttributeName().trim();
		if (this.getStrutsAction() == ApsAdminSystemConstants.ADD && null != entityType.getAttribute(attributeName)) {
			String[] args = {attributeName};
			this.addFieldError("attributeName", this.getText("error.entity.attribute.name.already.exists", args));
		}
		AttributeInterface attributePrototype = this.getAttributePrototype(this.getAttributeTypeCode());
		if (null == attributePrototype) {
			String[] args = {this.getAttributeTypeCode()};
			this.addFieldError("attributeTypeCode", this.getText("error.entity.attribute.type.invalid", args));
		} else {
			if ((attributePrototype instanceof EnumeratorAttribute) 
					&& (this.getEnumeratorStaticItems() == null || this.getEnumeratorStaticItems().trim().length() == 0) 
					&& (null == this.getEnumeratorExtractorBean() || this.getEnumeratorExtractorBean().trim().length() == 0)) {
				String[] args = {this.getAttributeTypeCode()};
				this.addFieldError("enumeratorStaticItems", this.getText("error.entity.attribute.enumerator.items.missing",args));
			}
			if ((attributePrototype instanceof AbstractListAttribute) 
					&& (null == this.getListNestedType()|| null == this.getAttributePrototype(this.getAttributeTypeCode()))) {
				String[] args = {this.getAttributeTypeCode()};
				this.addFieldError("listNestedType", this.getText("error.entity.attribute.list.handled.missing",args));
			}
		}
	}
	
	@Override
	public String addAttribute() {
		this.setStrutsAction(ApsAdminSystemConstants.ADD);
		this.getRequest().getSession().removeAttribute(ICompositeAttributeConfigAction.COMPOSITE_ATTRIBUTE_ON_EDIT_SESSION_PARAM);
		this.getRequest().getSession().removeAttribute(IListElementAttributeConfigAction.LIST_ATTRIBUTE_ON_EDIT_SESSION_PARAM);
		this.getRequest().getSession().removeAttribute(IListElementAttributeConfigAction.LIST_ELEMENT_ON_EDIT_SESSION_PARAM);
		return SUCCESS;
	}
	
	@Override
	public String editAttribute() {
		this.setStrutsAction(ApsAdminSystemConstants.EDIT);
		this.getRequest().getSession().removeAttribute(ICompositeAttributeConfigAction.COMPOSITE_ATTRIBUTE_ON_EDIT_SESSION_PARAM);
		this.getRequest().getSession().removeAttribute(IListElementAttributeConfigAction.LIST_ATTRIBUTE_ON_EDIT_SESSION_PARAM);
		this.getRequest().getSession().removeAttribute(IListElementAttributeConfigAction.LIST_ELEMENT_ON_EDIT_SESSION_PARAM);
		try {
			AttributeInterface attribute = (AttributeInterface) this.getEntityType().getAttribute(this.getAttributeName());
			this.valueFormFields(attribute);
			if (attribute instanceof AbstractListAttribute) {
				AbstractListAttribute listAttribute = (AbstractListAttribute) attribute;
				this.setListNestedType(listAttribute.getNestedAttributeTypeCode());
			}
		} catch (Throwable t) {
			_logger.error("error in editAttribute", t);
			//ApsSystemUtils.logThrowable(t, this, "editAttribute");
			return FAILURE;
		}
		return SUCCESS;
	}
	
	@Override
	public String addAttributeRole() {
		try {
			String attributeRoleName = this.getAttributeRoleName();
			if (null == this.getAttributeRoles()) {
				this.setAttributeRoles(new ArrayList<String>());
			}
			List<AttributeRole> allowedRoles = this.getAttributeRoles(this.getAttributeTypeCode());
			boolean allowed = false;
			if (null != allowedRoles) {
				for (int i = 0; i < allowedRoles.size(); i++) {
					AttributeRole role = allowedRoles.get(i);
					if (role.getName().equals(this.getAttributeRoleName())) {
						allowed = true;
						break;
					}
				}
			}
			if (!allowed) {
				//TODO ADD FIELD ERRORS - Role not allowed for type ...
				return SUCCESS;
			}
			if (!this.getAttributeRoles().contains(attributeRoleName)) {
				this.getAttributeRoles().add(attributeRoleName);
			}
		} catch (Throwable t) {
			_logger.error("error in addAttributeRole", t);
			//ApsSystemUtils.logThrowable(t, this, "addAttributeRole");
			return FAILURE;
		}
		return SUCCESS;
	}
	
	@Override
	public String removeAttributeRole() {
		try {
			String attributeRoleName = this.getAttributeRoleName();
			if (null == this.getAttributeRoles()) {
				return SUCCESS;
			}
			if (this.getAttributeRoles().contains(attributeRoleName)) {
				this.getAttributeRoles().remove(attributeRoleName);
			}
		} catch (Throwable t) {
			_logger.error("error in removeAttributeRole", t);
			//ApsSystemUtils.logThrowable(t, this, "removeAttributeRole");
			return FAILURE;
		}
		return SUCCESS;
	}
	
	@Override
	public String addAttributeDisablingCode() {
		try {
			String disablingCode = this.getDisablingCode();
			if (null == this.getDisablingCodes()) {
				this.setDisablingCodes(new ArrayList<String>());
			}
			if (!this.getDisablingCodes().contains(disablingCode)) {
				this.getDisablingCodes().add(disablingCode);
			}
		} catch (Throwable t) {
			_logger.error("error in addAttributeDisablingCode", t);
			//ApsSystemUtils.logThrowable(t, this, "addAttributeDisablingCode");
			return FAILURE;
		}
		return SUCCESS;
	}

	@Override
	public String removeAttributeDisablingCode() {
		try {
			String disablingCode = this.getDisablingCode();
			if (null == this.getDisablingCodes()) {
				return SUCCESS;
			}
			if (this.getDisablingCodes().contains(disablingCode)) {
				this.getDisablingCodes().remove(disablingCode);
			}
		} catch (Throwable t) {
			_logger.error("error in removeAttributeDisablingCode", t);
			//ApsSystemUtils.logThrowable(t, this, "removeAttributeDisablingCode");
			return FAILURE;
		}
		return SUCCESS;
	}
	
	@Override
	public String saveAttribute() {
		AttributeInterface attribute = null;
		try {
			if (this.getStrutsAction() == ApsAdminSystemConstants.EDIT) {
				attribute = (AttributeInterface) this.getEntityType().getAttribute(this.getAttributeName());
			} else {
				attribute = this.getAttributePrototype(this.getAttributeTypeCode());
				attribute.setName(this.getAttributeName().trim());
				this.getEntityType().addAttribute(attribute);
			}
			String resultCode = this.fillAttributeFields(attribute);
			if (null != resultCode) {
				return resultCode;
			}
		} catch (Throwable t) {
			_logger.error("error in saveAttribute", t);
			//ApsSystemUtils.logThrowable(t, this, "saveAttribute");
			return FAILURE;
		}
		return SUCCESS;
	}
	
	/**
	 * Fill attribute fields.
	 * @param attribute The attribute to edit with the form values.
	 * @return A custom return code in the attribute neads a extra configuration, else null.
	 */
	@Override
	protected String fillAttributeFields(AttributeInterface attribute) {
		super.fillAttributeFields(attribute);
		AttributeInterface nestedType = null;
		if (attribute instanceof AbstractListAttribute) {
			AbstractListAttribute listAttribute = (AbstractListAttribute) attribute;
			if (this.getStrutsAction() == ApsAdminSystemConstants.EDIT && 
					listAttribute.getNestedAttributeTypeCode().equals(this.getListNestedType())) {
				if (listAttribute instanceof ListAttribute) {
					Lang defaultLang = this.getLangManager().getDefaultLang();
					nestedType = ((ListAttribute) listAttribute).addAttribute(defaultLang.getCode());//Composite Element
					((ListAttribute) listAttribute).getAttributeList(defaultLang.getCode()).clear();
				} else {
					nestedType = ((MonoListAttribute) listAttribute).addAttribute();//Composite Element
					((MonoListAttribute) listAttribute).getAttributes().clear();
				}
			} else {
				nestedType = this.getAttributePrototype(this.getListNestedType());
				nestedType.setName(this.getAttributeName());
			}
			listAttribute.setNestedAttributeType(nestedType);
			nestedType.setName(attribute.getName());
		}
		if ((attribute instanceof CompositeAttribute) || (nestedType != null && nestedType instanceof CompositeAttribute)) {
			CompositeAttribute composite = ((attribute instanceof CompositeAttribute) ? (CompositeAttribute) attribute : (CompositeAttribute) nestedType);
			if (null != nestedType) {
				this.getRequest().getSession().setAttribute(IListElementAttributeConfigAction.LIST_ATTRIBUTE_ON_EDIT_SESSION_PARAM, (AbstractListAttribute) attribute);
			}
			this.getRequest().getSession().setAttribute(ICompositeAttributeConfigAction.COMPOSITE_ATTRIBUTE_ON_EDIT_SESSION_PARAM, composite);
			return "configureCompositeAttribute";
		}
		if (nestedType != null) {
			this.getRequest().getSession().setAttribute(IListElementAttributeConfigAction.LIST_ATTRIBUTE_ON_EDIT_SESSION_PARAM, (AbstractListAttribute) attribute);
			this.getRequest().getSession().setAttribute(IListElementAttributeConfigAction.LIST_ELEMENT_ON_EDIT_SESSION_PARAM, nestedType);
			return "configureListElementAttribute";
		}
		return null;
	}
	
	public List<AttributeInterface> getAllowedNestedTypes(AttributeInterface listType) {
		List<AttributeInterface> attributes = new ArrayList<AttributeInterface>();
		try {
			IEntityManager entityManager = this.getEntityManager();
			Map<String, AttributeInterface> attributeTypes = entityManager.getEntityAttributePrototypes();
			Iterator<AttributeInterface> attributeIter = attributeTypes.values().iterator();
			while (attributeIter.hasNext()) {
				AttributeInterface attribute = attributeIter.next();
				boolean simple = attribute.isSimple();
				boolean multiLanguage = attribute.isMultilingual();
				if ((listType instanceof ListAttribute && simple && !multiLanguage) 
						|| (listType instanceof MonoListAttribute && !(attribute instanceof AbstractListAttribute))) {
					attributes.add(attribute);
				}
			}
			Collections.sort(attributes, new BeanComparator("type"));
		} catch (Throwable t) {
			_logger.error("Error while extracting Allowed Nested Types", t);
			//ApsSystemUtils.logThrowable(t, this, "getAllowedNestedTypes");
			throw new RuntimeException("Error while extracting Allowed Nested Types", t);
		}
		return attributes;
	}
	
	public String getListNestedType() {
		return _listNestedType;
	}
	public void setListNestedType(String listNestedType) {
		this._listNestedType = listNestedType;
	}
	
	public String getAttributeRoleName() {
		return _attributeRoleName;
	}
	public void setAttributeRoleName(String attributeRoleName) {
		this._attributeRoleName = attributeRoleName;
	}
	
	public String getDisablingCode() {
		return _disablingCode;
	}
	public void setDisablingCode(String disablingCode) {
		this._disablingCode = disablingCode;
	}
	
	private String _listNestedType;
	private String _attributeRoleName;
	private String _disablingCode;
	
}