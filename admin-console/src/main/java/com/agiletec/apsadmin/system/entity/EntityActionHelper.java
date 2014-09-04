/*
*
* Copyright 2013 Entando S.r.l. (http://www.entando.com) All rights reserved.
*
* This file is part of Entando software.
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
package com.agiletec.apsadmin.system.entity;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;

import com.agiletec.aps.system.common.entity.model.AttributeFieldError;
import com.agiletec.aps.system.common.entity.model.AttributeTracer;
import com.agiletec.aps.system.common.entity.model.EntitySearchFilter;
import com.agiletec.aps.system.common.entity.model.IApsEntity;
import com.agiletec.aps.system.common.entity.model.attribute.AttributeInterface;
import com.agiletec.aps.system.common.entity.model.attribute.AttributeRole;
import com.agiletec.aps.system.common.entity.model.attribute.BooleanAttribute;
import com.agiletec.aps.system.common.entity.model.attribute.DateAttribute;
import com.agiletec.aps.system.common.entity.model.attribute.ITextAttribute;
import com.agiletec.aps.system.common.entity.model.attribute.NumberAttribute;
import com.agiletec.aps.util.DateConverter;
import com.agiletec.apsadmin.system.BaseActionHelper;
import com.agiletec.apsadmin.system.entity.attribute.manager.AbstractAttributeManager;
import com.agiletec.apsadmin.system.entity.attribute.manager.AttributeManagerInterface;
import com.agiletec.apsadmin.util.CheckFormatUtil;
import com.opensymphony.xwork2.ActionSupport;

/**
 * This abstract class supports all the helper classes that, in turn, support those
 * classes which handle elements built with the "ApsEntity' entries.
 * @author E.Santoboni
 */
public class EntityActionHelper extends BaseActionHelper implements IEntityActionHelper, BeanFactoryAware {

	private static final Logger _logger = LoggerFactory.getLogger(EntityActionHelper.class);
	
	@Override
	public void updateEntity(IApsEntity currentEntity, HttpServletRequest request) {
		try {
			List<AttributeInterface> attributes = currentEntity.getAttributeList();
			for (int i = 0; i < attributes.size(); i++) {
				AttributeInterface attribute = attributes.get(i);
				if (attribute.isActive()) {
					AttributeManagerInterface attributeManager = this.getManager(attribute);
					if (attributeManager != null) {
						attributeManager.updateEntityAttribute(attribute, request);
					}
				}
			}
		} catch (Throwable t) {
			_logger.error("Error updating Entity", t);
			//ApsSystemUtils.logThrowable(t, this, "updateEntity");
			throw new RuntimeException("Error updating Entity", t);
		}
	}
    
	@Override
	public void scanEntity(IApsEntity currentEntity, ActionSupport action) {
		try {
			List<AttributeInterface> attributes = currentEntity.getAttributeList();
			for (int i = 0; i < attributes.size(); i++) {
				AttributeInterface entityAttribute = attributes.get(i);
				if (entityAttribute.isActive()) {
					List<AttributeFieldError> errors = entityAttribute.validate(new AttributeTracer());
					if (null != errors && errors.size() > 0) {
						for (int j = 0; j < errors.size(); j++) {
							AttributeFieldError attributeFieldError = errors.get(j);
							AttributeTracer tracer = attributeFieldError.getTracer();
							AttributeInterface attribute = attributeFieldError.getAttribute();
							String messageAttributePositionPrefix = this.createErrorMessageAttributePositionPrefix(action, attribute, tracer);
							AttributeManagerInterface attributeManager = this.getManager(attribute);
							String errorMessage = attributeManager.getErrorMessage(attributeFieldError, action);
							String formFieldName = tracer.getFormFieldName(attributeFieldError.getAttribute());
							action.addFieldError(formFieldName, messageAttributePositionPrefix + " " + errorMessage);
						}
					}
				}
			}
		} catch (Throwable t) {
			_logger.error("Error scanning Entity", t);
			//ApsSystemUtils.logThrowable(t, this, "scanEntity");
			throw new RuntimeException("Error scanning Entity", t);
		}
	}
    
    private String createErrorMessageAttributePositionPrefix(ActionSupport action, AttributeInterface attribute, com.agiletec.aps.system.common.entity.model.AttributeTracer tracer) {
        if (tracer.isMonoListElement()) {
            if (tracer.isCompositeElement()) {
                String[] args = {tracer.getParentAttribute().getName(), String.valueOf(tracer.getListIndex() + 1), attribute.getName()};
                return action.getText("EntityAttribute.compositeListAttributeElement.errorMessage.prefix", args);
            } else {
                String[] args = {attribute.getName(), String.valueOf(tracer.getListIndex() + 1)};
                return action.getText("EntityAttribute.monolistAttributeElement.errorMessage.prefix", args);
            }
        } else if (tracer.isCompositeElement()) {
            String[] args = {tracer.getParentAttribute().getName(), attribute.getName()};
            return action.getText("EntityAttribute.compositeAttributeElement.errorMessage.prefix", args);
        } else if (tracer.isListElement()) {
            String[] args = {attribute.getName(), tracer.getListLang().getDescr(), String.valueOf(tracer.getListIndex() + 1)};
            return action.getText("EntityAttribute.listAttributeElement.errorMessage.prefix", args);
        } else {
            String[] args = {attribute.getName()};
            return action.getText("EntityAttribute.singleAttribute.errorMessage.prefix", args);
        }
    }
    
	protected AttributeManagerInterface getManager(AttributeInterface attribute) {
		String managerClassName = attribute.getAttributeManagerClassName();
        try {
			if (null == managerClassName) return null;
            Class managerClass = Class.forName(managerClassName);
            Object managerInstance = managerClass.newInstance();
            if (managerInstance instanceof AbstractAttributeManager) {
				AbstractAttributeManager manager = (AbstractAttributeManager) managerInstance;
				manager.setBeanFactory(this.getBeanFactory());
				return manager;
			}
        } catch (Throwable t) {
            String message = "Error creating manager of attribute '"
                    + attribute.getName() + "' type '" + attribute.getType() + "' -  Manager class '" + managerClassName + "'";
            _logger.error("Error creating manager of attribute '{}', type: {} - Manager class: {}", attribute.getName(),attribute.getType(), managerClassName,  t);
            //ApsSystemUtils.logThrowable(t, this, "getManager", message);;
            throw new RuntimeException(message, t);
        }
        return null;
    }
	
	@Override
	@Deprecated
	public EntitySearchFilter[] getSearchFilters(AbstractApsEntityFinderAction entityFinderAction, IApsEntity prototype) {
		return this.getAttributeFilters(entityFinderAction, prototype);
	}
	
	@Override
	public EntitySearchFilter[] getRoleFilters(AbstractApsEntityFinderAction entityFinderAction) {
		EntitySearchFilter[] filters = new EntitySearchFilter[0];
		List<AttributeRole> attributeRoles = entityFinderAction.getAttributeRoles();
		if (null != attributeRoles) {
			for (int i = 0; i < attributeRoles.size(); i++) {
				AttributeRole attributeRole = attributeRoles.get(i);
				if (AttributeRole.FormFieldTypes.TEXT.equals(attributeRole.getFormFieldType())) {
					String insertedText = entityFinderAction.getSearchFormFieldValue(attributeRole.getName() + "_textFieldName");
					if (null != insertedText && insertedText.trim().length() > 0) {
						EntitySearchFilter filterToAdd = EntitySearchFilter.createRoleFilter(attributeRole.getName(), insertedText.trim(), true);
						filters = this.addFilter(filters, filterToAdd);
					}
				} else if (AttributeRole.FormFieldTypes.DATE.equals(attributeRole.getFormFieldType())) {
					Date dateStart = this.getDateSearchFormValue(entityFinderAction, attributeRole.getName(), "_dateStartFieldName", true);
					Date dateEnd = this.getDateSearchFormValue(entityFinderAction, attributeRole.getName(), "_dateEndFieldName", false);
					if (null != dateStart || null != dateEnd) {
						EntitySearchFilter filterToAdd = EntitySearchFilter.createRoleFilter(attributeRole.getName(), dateStart, dateEnd);
						filters = this.addFilter(filters, filterToAdd);
					}
				} else if (AttributeRole.FormFieldTypes.BOOLEAN.equals(attributeRole.getFormFieldType())) {
					String booleanValue = entityFinderAction.getSearchFormFieldValue(attributeRole.getName() + "_booleanFieldName");
					if (null != booleanValue && booleanValue.trim().length() > 0) {
						EntitySearchFilter filterToAdd = EntitySearchFilter.createRoleFilter(attributeRole.getName(), booleanValue, false);
						filters = this.addFilter(filters, filterToAdd);
					}
				} else if (AttributeRole.FormFieldTypes.NUMBER.equals(attributeRole.getFormFieldType())) {
					BigDecimal numberStart = this.getNumberSearchFormValue(entityFinderAction, attributeRole.getName(), "_numberStartFieldName", true);
					BigDecimal numberEnd = this.getNumberSearchFormValue(entityFinderAction, attributeRole.getName(), "_numberEndFieldName", false);
					if (null != numberStart || null != numberEnd) {
						EntitySearchFilter filterToAdd = EntitySearchFilter.createRoleFilter(attributeRole.getName(), numberStart, numberEnd);
						filters = this.addFilter(filters, filterToAdd);
					}
				}
			}
		}
		return filters;
	}
	
	@Override
	public EntitySearchFilter[] getAttributeFilters(AbstractApsEntityFinderAction entityFinderAction, IApsEntity prototype) {
		EntitySearchFilter[] filters = new EntitySearchFilter[0];
		if (null == prototype) {
			return filters;
		}
		List<AttributeInterface> contentAttributes = prototype.getAttributeList();
		for (int i = 0; i < contentAttributes.size(); i++) {
			AttributeInterface attribute = contentAttributes.get(i);
			if (attribute.isActive() && attribute.isSearcheable()) {
				if (attribute instanceof ITextAttribute) {
					String insertedText = entityFinderAction.getSearchFormFieldValue(attribute.getName() + "_textFieldName");
					if (null != insertedText && insertedText.trim().length() > 0) {
						EntitySearchFilter filterToAdd = new EntitySearchFilter(attribute.getName(), true, insertedText.trim(), true);
						filters = this.addFilter(filters, filterToAdd);
					}
				} else if (attribute instanceof DateAttribute) {
					Date dateStart = this.getDateSearchFormValue(entityFinderAction, attribute.getName(), "_dateStartFieldName", true);
					Date dateEnd = this.getDateSearchFormValue(entityFinderAction, attribute.getName(), "_dateEndFieldName", false);
					if (null != dateStart || null != dateEnd) {
						EntitySearchFilter filterToAdd = new EntitySearchFilter(attribute.getName(), true, dateStart, dateEnd);
						filters = this.addFilter(filters, filterToAdd);
					}
				} else if (attribute instanceof BooleanAttribute) {
					String booleanValue = entityFinderAction.getSearchFormFieldValue(attribute.getName() + "_booleanFieldName");
					if (null != booleanValue && booleanValue.trim().length() > 0) {
						EntitySearchFilter filterToAdd = new EntitySearchFilter(attribute.getName(), true, booleanValue, false);
						filters = this.addFilter(filters, filterToAdd);
					}
				} else if (attribute instanceof NumberAttribute) {
					BigDecimal numberStart = this.getNumberSearchFormValue(entityFinderAction, attribute.getName(), "_numberStartFieldName", true);
					BigDecimal numberEnd = this.getNumberSearchFormValue(entityFinderAction, attribute.getName(), "_numberEndFieldName", false);
					if (null != numberStart || null != numberEnd) {
						EntitySearchFilter filterToAdd = new EntitySearchFilter(attribute.getName(), true, numberStart, numberEnd);
						filters = this.addFilter(filters, filterToAdd);
					}
				}
			}
		}
		return filters;
	}
	
    private Date getDateSearchFormValue(AbstractApsEntityFinderAction entityFinderAction,
            String fieldName, String dateFieldNameSuffix, boolean start) {
        String inputFormName = fieldName + dateFieldNameSuffix;
        String insertedDate = entityFinderAction.getSearchFormFieldValue(inputFormName);
        Date date = null;
        if (insertedDate != null && insertedDate.trim().length() > 0) {
            if (CheckFormatUtil.isValidDate(insertedDate.trim())) {
                date = DateConverter.parseDate(insertedDate.trim(), "dd/MM/yyyy");
            } else {
                String[] args = {fieldName};
                if (start) {
                    entityFinderAction.addFieldError(inputFormName, entityFinderAction.getText("error.attribute.startDate.invalid", args));
                } else {
                    entityFinderAction.addFieldError(inputFormName, entityFinderAction.getText("error.attribute.endDate.invalid", args));
                }
            }
        }
        return date;
    }

    private BigDecimal getNumberSearchFormValue(AbstractApsEntityFinderAction entityFinderAction,
            String fieldName, String numberFieldNameSuffix, boolean start) {
        String inputFormName = fieldName + numberFieldNameSuffix;
        String insertedNumberString = entityFinderAction.getSearchFormFieldValue(inputFormName);
        BigDecimal bigdecimal = null;
        if (insertedNumberString != null && insertedNumberString.trim().length() > 0) {
            if (CheckFormatUtil.isValidNumber(insertedNumberString.trim())) {
                bigdecimal = new BigDecimal(Integer.parseInt(insertedNumberString.trim()));
            } else {
                String[] args = {fieldName};
                if (start) {
                    entityFinderAction.addFieldError(inputFormName, entityFinderAction.getText("error.attribute.startNumber.invalid", args));
                } else {
                    entityFinderAction.addFieldError(inputFormName, entityFinderAction.getText("error.attribute.endNumber.invalid", args));
                }
            }
        }
        return bigdecimal;
    }
	
    private EntitySearchFilter[] addFilter(EntitySearchFilter[] filters, EntitySearchFilter filterToAdd) {
        int len = filters.length;
        EntitySearchFilter[] newFilters = new EntitySearchFilter[len + 1];
        for (int i = 0; i < len; i++) {
            newFilters[i] = filters[i];
        }
        newFilters[len] = filterToAdd;
        return newFilters;
    }
    
	protected BeanFactory getBeanFactory() {
		return _beanFactory;
	}
	@Override
	public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
		this._beanFactory = beanFactory;
	}
	
	private BeanFactory _beanFactory;
	
}
