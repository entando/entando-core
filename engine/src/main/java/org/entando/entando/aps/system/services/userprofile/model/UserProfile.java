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
package org.entando.entando.aps.system.services.userprofile.model;

import java.util.List;

import com.agiletec.aps.system.SystemConstants;
import com.agiletec.aps.system.common.entity.model.ApsEntity;
import com.agiletec.aps.system.common.entity.model.attribute.AbstractComplexAttribute;
import com.agiletec.aps.system.common.entity.model.attribute.AttributeInterface;
import com.agiletec.aps.system.common.entity.model.attribute.BooleanAttribute;
import com.agiletec.aps.system.common.entity.model.attribute.DateAttribute;
import com.agiletec.aps.system.common.entity.model.attribute.ITextAttribute;
import com.agiletec.aps.system.common.entity.model.attribute.NumberAttribute;

/**
 * A IUserProfile implementation. 
 * It contains a set of attributes specified in the configuration of ProfileManager.
 * @author E.Santoboni
 */
public class UserProfile extends ApsEntity implements IUserProfile {
	
	@Override
	public String getUsername() {
		return this.getId();
	}
	
	@Override
	public String getDisplayName() {
		String displayName = (String) this.getValueByRole(SystemConstants.USER_PROFILE_ATTRIBUTE_ROLE_FULL_NAME);
		//diplayName may contain an empty string
		if (null == displayName || displayName.trim().length() == 0) {
			displayName = this.getUsername();
		}
		return displayName;
	}
	
	@Override
	public String toString() {
		return this.getDisplayName();
	}
	
	@Override
	public Object getValue(String key) {
		AttributeInterface attribute = (AttributeInterface) this.getAttribute(key);
		if (null != attribute) {
			return this.getValue(attribute);
		}
		return null;
	}
	
	@Override
	public Object getValueByRole(String rolename) {
		AttributeInterface attribute = (AttributeInterface) this.getAttributeByRole(rolename);
		if (null != attribute) {
			return this.getValue(attribute);
		}
		return null;
	}
	
	private Object getValue(AttributeInterface attribute) {
		if (null == attribute) return "";
		if (attribute.isTextAttribute()) {
			return ((ITextAttribute) attribute).getText();
		} else if (attribute instanceof NumberAttribute) {
			return ((NumberAttribute) attribute).getValue();
		} else if (attribute instanceof BooleanAttribute) {
			return ((BooleanAttribute) attribute).getValue();
		} else if (attribute instanceof DateAttribute) {
			return ((DateAttribute) attribute).getDate();
		} else if (!attribute.isSimple()) {
			String text = "";
			List<AttributeInterface> attributes = ((AbstractComplexAttribute) attribute).getAttributes();
			for (int i=0; i<attributes.size(); i++) {
				if (i>0) text += ",";
				AttributeInterface attributeElem = attributes.get(i);
				text += this.getValue(attributeElem);
			}
			return text;
		}
		return null;
	}
	
	@Override
	public String getFullNameAttributeName() {
		AttributeInterface attribute = this.getAttributeByRole(SystemConstants.USER_PROFILE_ATTRIBUTE_ROLE_FULL_NAME);
		if (null != attribute) {
			return attribute.getName();
		}
		return null;
	}
	
	@Override
	public String getMailAttributeName() {
		AttributeInterface attribute = this.getAttributeByRole(SystemConstants.USER_PROFILE_ATTRIBUTE_ROLE_MAIL);
		if (null != attribute) {
			return attribute.getName();
		}
		return null;
	}
	
	@Override
	public boolean isPublicProfile() {
		return _publicProfile;
	}
	@Override
	public void setPublicProfile(boolean publicProfile) {
		this._publicProfile = publicProfile;
	}
	
	private boolean _publicProfile;
	
}