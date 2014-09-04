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
package org.entando.entando.aps.tags;

import javax.servlet.jsp.JspException;

import org.apache.taglibs.standard.tag.common.core.OutSupport;
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
public class UserProfileAttributeTag extends OutSupport {

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
        super.escapeXml = true;
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

    /**
     * Determina se effettuare l'escape dei caratteri speciali nella label ricavata.
     * @return True nel caso si debba effettuare l'escape, false in caso contrario.
     */
    public boolean getEscapeXml() {
        return super.escapeXml;
    }

    /**
     * Setta se effettuare l'escape dei caratteri speciali nella label ricavata.
     * @param escapeXml True nel caso si debba effettuare l'escape, false in caso contrario.
     */
    public void setEscapeXml(boolean escapeXml) {
        super.escapeXml = escapeXml;
    }
    
    private String _var;
	private String _attributeRoleName;
	private String _attributeName;
    private String _username;
    
}
