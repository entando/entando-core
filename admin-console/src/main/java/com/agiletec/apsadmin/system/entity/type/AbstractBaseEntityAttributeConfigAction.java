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
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.ListableBeanFactory;

import com.agiletec.aps.system.common.entity.IEntityManager;
import com.agiletec.aps.system.common.entity.model.IApsEntity;
import com.agiletec.aps.system.common.entity.model.attribute.AttributeInterface;
import com.agiletec.aps.system.common.entity.model.attribute.AttributeRole;
import com.agiletec.aps.system.common.entity.model.attribute.DateAttribute;
import com.agiletec.aps.system.common.entity.model.attribute.EnumeratorAttribute;
import com.agiletec.aps.system.common.entity.model.attribute.NumberAttribute;
import com.agiletec.aps.system.common.entity.model.attribute.util.DateAttributeValidationRules;
import com.agiletec.aps.system.common.entity.model.attribute.util.EnumeratorAttributeItemsExtractor;
import com.agiletec.aps.system.common.entity.model.attribute.util.IAttributeValidationRules;
import com.agiletec.aps.system.common.entity.model.attribute.util.NumberAttributeValidationRules;
import com.agiletec.aps.system.common.entity.model.attribute.util.OgnlValidationRule;
import com.agiletec.aps.system.common.entity.model.attribute.util.TextAttributeValidationRules;
import com.agiletec.aps.system.common.searchengine.IndexableAttributeInterface;
import com.agiletec.apsadmin.system.BaseAction;

/**
 * Base action for Configure Entity Attributes.
 * @author E.Santoboni
 */
public class AbstractBaseEntityAttributeConfigAction extends BaseAction implements BeanFactoryAware {

	private static final Logger _logger = LoggerFactory.getLogger(AbstractBaseEntityAttributeConfigAction.class);
	
	/**
	 * Fill form fields.
	 * @param attribute 
	 */
	protected void valueFormFields(AttributeInterface attribute) {
		this.setAttributeName(attribute.getName());
		if (null != attribute.getDescription() && attribute.getDescription().trim().length() > 0) {
			this.setAttributeDescription(attribute.getDescription());
		}
		this.setAttributeTypeCode(attribute.getType());
		if (null != attribute.getRoles()) {
			this.setAttributeRoles(Arrays.asList(attribute.getRoles()));
		}
		if (null != attribute.getDisablingCodes()) {
			this.setDisablingCodes(Arrays.asList(attribute.getDisablingCodes()));
		}
		IAttributeValidationRules valRule = attribute.getValidationRules();
		this.setRequired(new Boolean(valRule.isRequired()));
		this.setOgnlValidationRule(valRule.getOgnlValidationRule());
		this.setSearchable(new Boolean(attribute.isSearchable()));
		String indexingType = attribute.getIndexingType();
		if (null != indexingType) {
			this.setIndexable(indexingType.equalsIgnoreCase(IndexableAttributeInterface.INDEXING_TYPE_TEXT));
		}
		if (attribute.isTextAttribute()) {
			TextAttributeValidationRules textValRule = (TextAttributeValidationRules) valRule;
			if (null != textValRule.getMaxLength() && textValRule.getMaxLength() > -1) {
				this.setMaxLength(textValRule.getMaxLength());
			}
			if (null != textValRule.getMinLength() && textValRule.getMinLength() > -1) {
				this.setMinLength(textValRule.getMinLength());
			}
			this.setRegexp(textValRule.getRegexp());
			this.setRangeEndString((String) textValRule.getRangeEnd());
			this.setRangeStartString((String) textValRule.getRangeStart());
			this.setEqualString((String) textValRule.getValue());
			this.setRangeEndStringAttribute(textValRule.getRangeEndAttribute());
			this.setRangeStartStringAttribute(textValRule.getRangeStartAttribute());
			this.setEqualStringAttribute(textValRule.getValueAttribute());
			if (attribute instanceof EnumeratorAttribute) {
				EnumeratorAttribute enumeratorAttribute = (EnumeratorAttribute) attribute;
				this.setEnumeratorStaticItems(enumeratorAttribute.getStaticItems());
				this.setEnumeratorStaticItemsSeparator(enumeratorAttribute.getCustomSeparator());
				this.setEnumeratorExtractorBean(enumeratorAttribute.getExtractorBeanName());
			}
		}
		if (attribute instanceof DateAttribute) {
			DateAttributeValidationRules dateValRule = (DateAttributeValidationRules) valRule;
			this.setRangeEndDate((Date) dateValRule.getRangeEnd());
			this.setRangeStartDate((Date) dateValRule.getRangeStart());
			this.setEqualDate((Date) dateValRule.getValue());
			this.setRangeEndDateAttribute(dateValRule.getRangeEndAttribute());
			this.setRangeStartDateAttribute(dateValRule.getRangeStartAttribute());
			this.setEqualDateAttribute(dateValRule.getValueAttribute());
		}
		if (attribute instanceof NumberAttribute) {
			NumberAttributeValidationRules nulValRule = (NumberAttributeValidationRules) valRule;
			this.setRangeEndNumber((Integer) nulValRule.getRangeEnd());
			this.setRangeStartNumber((Integer) nulValRule.getRangeStart());
			this.setEqualNumber((Integer) nulValRule.getValue());
			this.setRangeEndNumberAttribute(nulValRule.getRangeEndAttribute());
			this.setRangeStartNumberAttribute(nulValRule.getRangeStartAttribute());
			this.setEqualNumberAttribute(nulValRule.getValueAttribute());
		}
	}
	
	/**
	 * Fill attribute fields.
	 * @param attribute The attribute to edit with the form values.
	 * @return A customized return code in the attribute needs a extra configuration, else null.
	 */
	protected String fillAttributeFields(AttributeInterface attribute) {
		if (null != this.getAttributeDescription() && this.getAttributeDescription().trim().length() > 0) {
			attribute.setDescription(this.getAttributeDescription().trim());
		} else {
			attribute.setDescription(null);
		}
		attribute.setRoles(this.createStringArray(this.getAttributeRoles()));
		attribute.setDisablingCodes(this.createStringArray(this.getDisablingCodes()));
		attribute.setSearchable(null != this.getSearchable() && this.getSearchable());
		String indexingType = IndexableAttributeInterface.INDEXING_TYPE_NONE;
		if (null != this.getIndexable()) {
			indexingType = IndexableAttributeInterface.INDEXING_TYPE_TEXT;
		}
		attribute.setIndexingType(indexingType);
		IAttributeValidationRules valCond = attribute.getValidationRules();
		valCond.setRequired(null != this.getRequired() && this.getRequired());
		valCond.setOgnlValidationRule(this.getOgnlValidationRule());
		if (attribute.isTextAttribute()) {
			TextAttributeValidationRules valRule = (TextAttributeValidationRules) valCond;
			//int maxLength = ((null != this.getMaxLength()) ? this.getMaxLength().intValue() : -1);
			//valRule.setMaxLength(maxLength);
			valRule.setMaxLength(this.getMaxLength());
			//int minLength = ((null != this.getMinLength()) ? this.getMinLength().intValue() : -1);
			//valRule.setMinLength(minLength);
			valRule.setMinLength(this.getMinLength());
			valRule.setRegexp(this.getRegexp());
			valRule.setRangeEnd(this.getRangeEndString());
			valRule.setRangeStart(this.getRangeStartString());
			valRule.setValue(this.getEqualString());
			valRule.setRangeEndAttribute(this.getRangeEndStringAttribute());
			valRule.setRangeStartAttribute(this.getRangeStartStringAttribute());
			valRule.setValueAttribute(this.getEqualStringAttribute());
			if (attribute instanceof EnumeratorAttribute) {
				EnumeratorAttribute enumeratorAttribute = (EnumeratorAttribute) attribute;
				enumeratorAttribute.setStaticItems(this.getEnumeratorStaticItems());
				if (null != this.getEnumeratorStaticItemsSeparator() && this.getEnumeratorStaticItemsSeparator().length()>0) {
					enumeratorAttribute.setCustomSeparator(this.getEnumeratorStaticItemsSeparator());
				}
				if (null != this.getEnumeratorExtractorBean() && this.getEnumeratorExtractorBean().trim().length() > 0) {
					enumeratorAttribute.setExtractorBeanName(this.getEnumeratorExtractorBean());
				} else {
					enumeratorAttribute.setExtractorBeanName(null);
				}
			}
		}
		if (attribute instanceof DateAttribute) {
			DateAttributeValidationRules dateValRule = (DateAttributeValidationRules) valCond;
			dateValRule.setRangeEnd(this.getRangeEndDate());
			dateValRule.setRangeStart(this.getRangeStartDate());
			dateValRule.setValue(this.getEqualDate());
			dateValRule.setRangeEndAttribute(this.getRangeEndDateAttribute());
			dateValRule.setRangeStartAttribute(this.getRangeStartDateAttribute());
			dateValRule.setValueAttribute(this.getEqualDateAttribute());
		}
		if (attribute instanceof NumberAttribute) {
			NumberAttributeValidationRules nulValRule = (NumberAttributeValidationRules) valCond;
			nulValRule.setRangeEnd(this.getRangeEndNumber());
			nulValRule.setRangeStart(this.getRangeStartNumber());
			nulValRule.setValue(this.getEqualNumber());
			nulValRule.setRangeEndAttribute(this.getRangeEndNumberAttribute());
			nulValRule.setRangeStartAttribute(this.getRangeStartNumberAttribute());
			nulValRule.setValueAttribute(this.getEqualNumberAttribute());
		}
		return null;
	}
	
	private String[] createStringArray(List<String> list) {
		if (null == list || list.isEmpty()) {
			return null;
		}
		String[] array = new String[list.size()];
		for (int i = 0; i < list.size(); i++) {
			array[i] = list.get(i);
		}
		return array;
	}
	
	/**
	 * Return the current entity type on edit.
	 * @return The current entity type.
	 */
	public IApsEntity getEntityType() {
		return (IApsEntity) this.getRequest().getSession().getAttribute(IEntityTypeConfigAction.ENTITY_TYPE_ON_EDIT_SESSION_PARAM);
	}
	
	/**
	 * Return the entity manager name that manages the current entity on edit.
	 * @return The entity manager name.
	 */
	public String getEntityManagerName() {
		return (String) this.getRequest().getSession().getAttribute(IEntityTypeConfigAction.ENTITY_TYPE_MANAGER_SESSION_PARAM);
	}
	
	/**
	 * Return the entity manager that manages the current entity on edit.
	 * @return The entity manager.
	 */
	protected IEntityManager getEntityManager() {
		String entityManagerName = this.getEntityManagerName();
		return (IEntityManager) this.getBeanFactory().getBean(entityManagerName);
	}
	
	/**
	 * Return the namespace prefix specific for the current entity manager.
	 * The prefix will extract by the object {@link EntityTypeNamespaceInfoBean} associated to the current entity manager.
	 * @return The namespace prefix specific for the current entity manager.
	 */
	public String getEntityTypeManagementNamespacePrefix() {
		try {
			EntityTypeNamespaceInfoBean infoBean = (EntityTypeNamespaceInfoBean) this.getBeanFactory().getBean(this.getEntityManagerName()+"NamespaceInfoBean");
			return infoBean.getNamespacePrefix();
		} catch (Throwable t) {
			//nothing to do
		}
		return "";
	}
	
	/**
	 * Indicates whether the current entity manager is a Search Engine user or not.
	 * @return True if the current entity manager is a Search Engine user, false otherwise.
	 */
	public boolean isEntityManagerSearchEngineUser() {
		return this.getEntityManager().isSearchEngineUser();
	}
	
	public boolean isIndexableOptionSupported(String attributeTypeCode) {
		try {
			AttributeInterface attribute = this.getAttributePrototype(attributeTypeCode);
			return (null == attribute.getIndexingType());
		} catch (Throwable t) {
			_logger.error("error in isIndexableOptionSupported", t);
			//ApsSystemUtils.logThrowable(t, this, "isIndexableOptionSupported");
		}
		return false;
	}
	
	public boolean isSearchableOptionSupported(String attributeTypeCode) {
		try {
			AttributeInterface attribute = this.getAttributePrototype(attributeTypeCode);
			return attribute.isSearchableOptionSupported();
		} catch (Throwable t) {
			_logger.error("error in isSearchableOptionSupported", t);
			//ApsSystemUtils.logThrowable(t, this, "isSearchableOptionSupported");
		}
		return false;
	}
	
	public AttributeInterface getAttributePrototype(String typeCode) {
		IEntityManager entityManager = this.getEntityManager();
		Map<String, AttributeInterface> attributeTypes = entityManager.getEntityAttributePrototypes();
		return attributeTypes.get(typeCode);
	}
	
	/**
	 * Return the list of the other entity attributes of the same type of the current on edit.
	 * @return The list of the other entity attributes of the same type.
	 */
	public List<AttributeInterface> getSameAttributes() {
		AttributeInterface attributePrototype = this.getAttributePrototype(this.getAttributeTypeCode());
		List<AttributeInterface> attributes = new ArrayList<AttributeInterface>();
		List<AttributeInterface> currentEntityAttributes = this.getEntityType().getAttributeList();
		for (int i = 0; i < currentEntityAttributes.size(); i++) {
			AttributeInterface attribute = currentEntityAttributes.get(i);
			if (attribute.getClass().isInstance(attributePrototype) 
					&& (null == this.getAttributeName() || !attribute.getName().equals(this.getAttributeName()))) {
				attributes.add(attribute);
			}
		}
		return attributes;
	}
	
	/**
	 * Return the list of the attribute role not in use on the entity type on edit.
	 * @return The list of the free attribute roles.
	 */
	public List<AttributeRole> getFreeAttributeRoleNames() {
		List<AttributeRole> freeRoles = new ArrayList<AttributeRole>();
		List<AttributeRole> roles = this.getAttributeRoles(this.getAttributeTypeCode());
		if (null == roles) {
			return freeRoles;
		}
		for (int i = 0; i < roles.size(); i++) {
			AttributeRole role = roles.get(i);
			AttributeInterface utilizer = this.getEntityType().getAttributeByRole(role.getName());
			if (null == utilizer || utilizer.getName().equals(this.getAttributeName())) {
				freeRoles.add(role);
			}
		}
		return freeRoles;
	}
	
	protected List<AttributeRole> getAttributeRoles(String attributeTypeCode) {
		List<AttributeRole> rolesByType = new ArrayList<AttributeRole>();
		List<AttributeRole> roles = this.getEntityManager().getAttributeRoles();
		if (null == roles) {
			return rolesByType;
		}
		for (int i = 0; i < roles.size(); i++) {
			AttributeRole role = roles.get(i);
			if (role.getAllowedAttributeTypes().contains(attributeTypeCode)) {
				rolesByType.add(role);
			}
		}
		return rolesByType;
	}
	
	/**
	 * Return an attribute role.
	 * @param roleName The name of the tole to return.
	 * @return The required role.
	 */
	public AttributeRole getAttributeRole(String roleName) {
		return this.getEntityManager().getAttributeRole(roleName);
	}
	
	public Map<String, String> getAttributeDisablingCodes() {
		return this.getEntityManager().getAttributeDisablingCodes();
	}
	
	public List<String> getEnumeratorExtractorBeans() {
		List<String> extractors = null;
		try {
			ListableBeanFactory factory = (ListableBeanFactory) this.getBeanFactory();
			String[] defNames = factory.getBeanNamesForType(EnumeratorAttributeItemsExtractor.class);
			extractors = Arrays.asList(defNames);
		} catch (Throwable t) {
			_logger.error("Error while extracting enumerator extractor beans", t);
			//ApsSystemUtils.logThrowable(t, this, "getEnumeratorExtractorBeans");
			throw new RuntimeException("Error while extracting enumerator extractor beans", t);
		}
		return extractors;
	}
	
	public int getStrutsAction() {
		return _strutsAction;
	}
	public void setStrutsAction(int strutsAction) {
		this._strutsAction = strutsAction;
	}
	
	public String getAttributeName() {
		return _attributeName;
	}
	public void setAttributeName(String attributeName) {
		this._attributeName = attributeName;
	}
	
	public String getAttributeDescription() {
		return _attributeDescription;
	}
	public void setAttributeDescription(String attributeDescription) {
		this._attributeDescription = attributeDescription;
	}
	
	public String getAttributeTypeCode() {
		return _attributeTypeCode;
	}
	public void setAttributeTypeCode(String attributeTypeCode) {
		this._attributeTypeCode = attributeTypeCode;
	}
	
	@Deprecated
	public Boolean getSearcheable() {
		return this.getSearchable();
	}
	@Deprecated
	public void setSearcheable(Boolean searchable) {
		this.setSearchable(searchable);
	}
	
	public Boolean getSearchable() {
		return _searchable;
	}
	public void setSearchable(Boolean searchable) {
		this._searchable = searchable;
	}
	
	public Boolean getIndexable() {
		return _indexable;
	}
	public void setIndexable(Boolean indexable) {
		this._indexable = indexable;
	}
	
	public List<String> getAttributeRoles() {
		return _attributeRoles;
	}
	public void setAttributeRoles(List<String> attributeRoles) {
		this._attributeRoles = attributeRoles;
	}
	
	public List<String> getDisablingCodes() {
		return _disablingCodes;
	}
	public void setDisablingCodes(List<String> disablingCodes) {
		this._disablingCodes = disablingCodes;
	}
	
	public Boolean getRequired() {
		return _required;
	}
	public void setRequired(Boolean required) {
		this._required = required;
	}
	
	public OgnlValidationRule getOgnlValidationRule() {
		return _ognlValidationRule;
	}
	public void setOgnlValidationRule(OgnlValidationRule ognlValidationRule) {
		this._ognlValidationRule = ognlValidationRule;
	}
	
	public Integer getMinLength() {
		return _minLength;
	}
	public void setMinLength(Integer minLength) {
		this._minLength = minLength;
	}
	
	public Integer getMaxLength() {
		return _maxLength;
	}
	public void setMaxLength(Integer maxLength) {
		this._maxLength = maxLength;
	}
	
	public String getRegexp() {
		return _regexp;
	}
	public void setRegexp(String regexp) {
		this._regexp = regexp;
	}
	
	public String getRangeStartString() {
		return _rangeStartString;
	}
	public void setRangeStartString(String rangeStartString) {
		this._rangeStartString = rangeStartString;
	}
	
	public String getRangeEndString() {
		return _rangeEndString;
	}
	public void setRangeEndString(String rangeEndString) {
		this._rangeEndString = rangeEndString;
	}
	
	public String getRangeStartStringAttribute() {
		return _rangeStartStringAttribute;
	}
	public void setRangeStartStringAttribute(String rangeStartStringAttribute) {
		this._rangeStartStringAttribute = rangeStartStringAttribute;
	}
	
	public String getRangeEndStringAttribute() {
		return _rangeEndStringAttribute;
	}
	public void setRangeEndStringAttribute(String rangeEndStringAttribute) {
		this._rangeEndStringAttribute = rangeEndStringAttribute;
	}
	
	public String getEqualString() {
		return _equalString;
	}
	public void setEqualString(String equalString) {
		this._equalString = equalString;
	}
	
	public String getEqualStringAttribute() {
		return _equalStringAttribute;
	}
	public void setEqualStringAttribute(String equalStringAttribute) {
		this._equalStringAttribute = equalStringAttribute;
	}
	
	public Date getRangeStartDate() {
		return _rangeStartDate;
	}
	public void setRangeStartDate(Date rangeStartDate) {
		this._rangeStartDate = rangeStartDate;
	}
	
	public Date getRangeEndDate() {
		return _rangeEndDate;
	}
	public void setRangeEndDate(Date rangeEndDate) {
		this._rangeEndDate = rangeEndDate;
	}
	
	public String getRangeStartDateAttribute() {
		return _rangeStartDateAttribute;
	}
	public void setRangeStartDateAttribute(String rangeStartDateAttribute) {
		this._rangeStartDateAttribute = rangeStartDateAttribute;
	}
	
	public String getRangeEndDateAttribute() {
		return _rangeEndDateAttribute;
	}
	public void setRangeEndDateAttribute(String rangeEndDateAttribute) {
		this._rangeEndDateAttribute = rangeEndDateAttribute;
	}
	
	public Date getEqualDate() {
		return _equalDate;
	}
	public void setEqualDate(Date equalDate) {
		this._equalDate = equalDate;
	}
	
	public String getEqualDateAttribute() {
		return _equalDateAttribute;
	}
	public void setEqualDateAttribute(String equalDateAttribute) {
		this._equalDateAttribute = equalDateAttribute;
	}
	
	public Integer getRangeStartNumber() {
		return _rangeStartNumber;
	}
	public void setRangeStartNumber(Integer rangeStartNumber) {
		this._rangeStartNumber = rangeStartNumber;
	}
	
	public String getRangeStartNumberAttribute() {
		return _rangeStartNumberAttribute;
	}
	public void setRangeStartNumberAttribute(String rangeStartNumberAttribute) {
		this._rangeStartNumberAttribute = rangeStartNumberAttribute;
	}
	
	public Integer getRangeEndNumber() {
		return _rangeEndNumber;
	}
	public void setRangeEndNumber(Integer rangeEndNumber) {
		this._rangeEndNumber = rangeEndNumber;
	}
	
	public String getRangeEndNumberAttribute() {
		return _rangeEndNumberAttribute;
	}
	public void setRangeEndNumberAttribute(String rangeEndNumberAttribute) {
		this._rangeEndNumberAttribute = rangeEndNumberAttribute;
	}
	
	public Integer getEqualNumber() {
		return _equalNumber;
	}
	public void setEqualNumber(Integer equalNumber) {
		this._equalNumber = equalNumber;
	}
	
	public String getEqualNumberAttribute() {
		return _equalNumberAttribute;
	}
	public void setEqualNumberAttribute(String equalNumberAttribute) {
		this._equalNumberAttribute = equalNumberAttribute;
	}
	
	public String getEnumeratorStaticItems() {
		return _enumeratorStaticItems;
	}
	public void setEnumeratorStaticItems(String enumeratorStaticItems) {
		this._enumeratorStaticItems = enumeratorStaticItems;
	}
	
	public String getEnumeratorStaticItemsSeparator() {
		return _enumeratorStaticItemsSeparator;
	}
	public void setEnumeratorStaticItemsSeparator(String enumeratorStaticItemsSeparator) {
		this._enumeratorStaticItemsSeparator = enumeratorStaticItemsSeparator;
	}
	
	public String getEnumeratorExtractorBean() {
		return _enumeratorExtractorBean;
	}
	public void setEnumeratorExtractorBean(String enumeratorExtractorBean) {
		this._enumeratorExtractorBean = enumeratorExtractorBean;
	}
	
	protected BeanFactory getBeanFactory() {
		return _beanFactory;
	}
	@Override
	public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
		this._beanFactory = beanFactory;
	}
	
	private int _strutsAction;
	
	private String _attributeName;
	private String _attributeDescription;
	private String _attributeTypeCode;
	private Boolean _searchable;
	private Boolean _indexable;
	private List<String> _attributeRoles;
	private List<String> _disablingCodes;
	
	private Boolean _required;
	
	private OgnlValidationRule _ognlValidationRule;
	
	private Integer _minLength;
	private Integer _maxLength;
	private String _regexp;
	
	private String _rangeStartString;
	private String _rangeEndString;
	private String _rangeStartStringAttribute;
	private String _rangeEndStringAttribute;
	private String _equalString;
	private String _equalStringAttribute;
	
	private Date _rangeStartDate;
	private Date _rangeEndDate;
	private String _rangeStartDateAttribute;
	private String _rangeEndDateAttribute;
	private Date _equalDate;
	private String _equalDateAttribute;
	
	private Integer _rangeStartNumber;
	private String _rangeStartNumberAttribute;
	private Integer _rangeEndNumber;
	private String _rangeEndNumberAttribute;
	private Integer _equalNumber;
	private String _equalNumberAttribute;
	
	private String _enumeratorStaticItems;
	private String _enumeratorStaticItemsSeparator;
	private String _enumeratorExtractorBean;
	
	private BeanFactory _beanFactory;
	
}