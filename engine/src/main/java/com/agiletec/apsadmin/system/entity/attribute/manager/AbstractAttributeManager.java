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
package com.agiletec.apsadmin.system.entity.attribute.manager;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.BeanFactory;

import com.agiletec.aps.system.SystemConstants;
import com.agiletec.aps.system.common.entity.model.AttributeFieldError;
import com.agiletec.aps.system.common.entity.model.AttributeTracer;
import com.agiletec.aps.system.common.entity.model.FieldError;
import com.agiletec.aps.system.common.entity.model.attribute.AttributeInterface;
import com.agiletec.aps.system.services.i18n.II18nManager;
import com.agiletec.aps.system.services.lang.ILangManager;
import com.agiletec.aps.system.services.lang.Lang;
import com.agiletec.apsadmin.system.BaseAction;
import com.opensymphony.xwork2.ActionSupport;

/**
 * This abstract class is the base for the managers of all attributes.
 * For the 'complex' attributes this class must be directly extended, otherwise
 * -for 'simple' attributes- this class is extended by the managers of the 
 * 'mono-language' and 'multi-language' Attributes.
 * @author E.Santoboni
 */
public abstract class AbstractAttributeManager implements AttributeManagerInterface {

	private static final Logger _logger = LoggerFactory.getLogger(AbstractAttributeManager.class);
	
    /**
     * Return the key of the message to retrieve when an attribute is not valid.
     * If a customized message is needed eg. due to the nature of the attribute, extend this method.
     * @return The key of the message to return. 
     */
    protected String getInvalidAttributeMessage() {
        return "EntityAttribute.fieldError.invalidAttribute";
    }
	
    /**
     * Return the key of the message used when a mandatory attribute is not populated.
     * If a customized message is needed eg. due to the nature of the attribute, extend this method.
     * @return The key of the message to return.
     */
    protected String getRequiredAttributeMessage() {
        return "EntityAttribute.fieldError.required";
    }
	
	@Override
    public void updateEntityAttribute(AttributeInterface attribute, HttpServletRequest request) {
        this.updateAttribute(attribute, new AttributeTracer(), request);
    }
	
	/**
     * Updates the attribute with the criteria specified in the content editing form.
     * @param attribute The current attribute (simple or complex) to check.
     * @param tracer The 'tracer' class needed to find the position of the attribute inside a 'composite' one.
     * @param request The request.
     */
    protected abstract void updateAttribute(AttributeInterface attribute, AttributeTracer tracer, HttpServletRequest request);
    
    /**
     * Return the value of the current attribute passed from the form.
     * 
     * @param attribute The current attribute (simple or complex) to check.
     * @param tracer The 'tracer' class needed to find the position of the attribute inside a 'composite' one.
     * @param request The request.
     * @return The value passed in the form
     */
    protected String getValueFromForm(AttributeInterface attribute, AttributeTracer tracer, HttpServletRequest request) {
        String formFieldName = tracer.getFormFieldName(attribute);
        return request.getParameter(formFieldName);
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
        } catch (Throwable e) {
            String message = "Error creating manager of attribute '"
                    + attribute.getName() + "' type '" + attribute.getType() + "' -  Manager class '" + managerClassName + "'";
            _logger.error("Error creating manager of attribute '{}', type: '{}' - Manager class: {}", attribute.getName(), attribute.getType(), managerClassName, e);
            //ApsSystemUtils.logThrowable(e, this, "getManager", message);
            throw new RuntimeException(message, e);
        }
        return null;
    }
	
    /**
     * Set the extra properties in the given manager.
     * This method is used when creating a manager to handle the attribute element of a complex
     * attribute and must be implemented when setting extra attributes.
     * @param manager The manager to create.
     */
    protected void setExtraPropertyTo(AttributeManagerInterface manager) {
        //nothing to do
    }
    
	@Override
    public String getErrorMessage(AttributeFieldError attributeFieldError, ActionSupport action) {
        try {
            String errorCode = attributeFieldError.getErrorCode();
            if (errorCode.equals(FieldError.MANDATORY)) {
                return action.getText(this.getRequiredAttributeMessage());
            } else if (errorCode.equals(AttributeFieldError.OGNL_VALIDATION)) {
                String message = attributeFieldError.getMessage();
                if (null != message && message.trim().length() > 0) {
                    return message;
                }
                String label = null;
                String labelKey = attributeFieldError.getMessageKey();
                if (null != labelKey && labelKey.trim().length() > 0) {
                    Lang currentLang = this.getLangManager().getDefaultLang();
                    if (action instanceof BaseAction) {
                        currentLang = ((BaseAction) action).getCurrentLang();
                    }
                    label = this.getI18nManager().getLabel(labelKey, currentLang.getCode());
                }
                if (label != null) {
                    return label;
                } else return this.getCustomAttributeErrorMessage(attributeFieldError, action);
            } else {
                return this.getCustomAttributeErrorMessage(attributeFieldError, action);
            }
        } catch (Throwable t) {
        	_logger.error("Error creating Error Message", t);
            //ApsSystemUtils.logThrowable(t, this, "getErrorMessage");
            throw new RuntimeException("Error creating Error Message", t);
        }
    }
    
    /**
     * Return a custom error message.
     * This method shouwld to be extended for custom attribute manager
     * @param errorCode The error code 
     * @return The message for the specific error code.
     */
    protected String getCustomAttributeErrorMessage(AttributeFieldError attributeFieldError, ActionSupport action) {
        return action.getText(this.getInvalidAttributeMessage());
    }
    
    protected II18nManager getI18nManager() {
        return (II18nManager) this.getBeanFactory().getBean(SystemConstants.I18N_MANAGER, II18nManager.class);
    }
	
    protected ILangManager getLangManager() {
        return (ILangManager) this.getBeanFactory().getBean(SystemConstants.LANGUAGE_MANAGER, ILangManager.class);
    }
	
	protected BeanFactory getBeanFactory() {
		return _beanFactory;
	}
	public void setBeanFactory(BeanFactory beanFactory) {
		this._beanFactory = beanFactory;
	}
	
	private BeanFactory _beanFactory;
	
}
