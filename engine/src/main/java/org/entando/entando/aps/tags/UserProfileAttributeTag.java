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
package org.entando.entando.aps.tags;

import javax.servlet.jsp.JspException;

import org.entando.entando.aps.system.services.userprofile.IUserProfileManager;
import org.entando.entando.aps.system.services.userprofile.model.IUserProfile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agiletec.aps.system.SystemConstants;
import com.agiletec.aps.system.common.entity.model.attribute.ITextAttribute;
import com.agiletec.aps.util.ApsWebApplicationUtils;

/**
 * User Profile tag.
 * Return an attribute value of an user profile.
 * @author E.Santoboni
 */
public class UserProfileAttributeTag extends ExtendedTagSupport {

	private static final Logger _logger =  LoggerFactory.getLogger(UserProfileAttributeTag.class);
	
    @Override
    public int doStartTag() throws JspException {
        try {
            IUserProfile profile = this.getUserProfile();
            if (null == profile) {
                return super.doStartTag();
            }
            Object value = null;
            if (null != this.getAttributeRoleName()) {
                ITextAttribute textAttribute = (ITextAttribute) profile.getAttributeByRole(this.getAttributeRoleName());
                value = (null != textAttribute) ? textAttribute.getText() : null;
            } else {
                value = profile.getValue(this.getAttributeName());
            }
            if (null == value) {
                return super.doStartTag();
            }
            if (this.getVar() != null) {
                this.pageContext.setAttribute(this.getVar(), value);
            } else {
                if (this.getEscapeXml()) {
                    out(this.pageContext, this.getEscapeXml(), value);
                } else {
                    this.pageContext.getOut().print(value);
                }
            }
        } catch (Throwable t) {
        	_logger.error("error in doStartTag", t);
            //ApsSystemUtils.logThrowable(t, this, "doStartTag");
            throw new JspException("Error during tag initialization", t);
        }
        return super.doStartTag();
    }
    
    protected IUserProfile getUserProfile() throws Throwable {
        IUserProfileManager userProfileManager = (IUserProfileManager) ApsWebApplicationUtils.getBean(SystemConstants.USER_PROFILE_MANAGER, this.pageContext);
        return userProfileManager.getProfile(this.getUsername());
    }
    
	@Override
    public void release() {
        super.release();
        this.setVar(null);
		this.setAttributeName(null);
		this.setAttributeRoleName(null);
    }
    
    /**
     * Imposta l'attributo che definisce il nome della variabile di output.
     * @param var Il nome della variabile.
     */
    public void setVar(String var) {
        this._var = var;
    }

    /**
     * Restituisce l'attributo che definisce il nome della variabile di output.
     * @return Il nome della variabile.
     */
    public String getVar() {
        return _var;
    }
	
	public String getAttributeName() {
		return _attributeName;
	}
	public void setAttributeName(String attributeName) {
		this._attributeName = attributeName;
	}
	
	public String getAttributeRoleName() {
		return _attributeRoleName;
	}
	public void setAttributeRoleName(String attributeRoleName) {
		this._attributeRoleName = attributeRoleName;
	}
	
    public String getUsername() {
        return _username;
    }
    public void setUsername(String username) {
        this._username = username;
    }
	
    private String _var;
	private String _attributeRoleName;
	private String _attributeName;
    private String _username;
    
}
