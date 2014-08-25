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

import com.agiletec.aps.system.common.entity.model.IApsEntity;

/**
 * Provides core UserProfile information.
 * @author E.Santoboni
 */
public interface IUserProfile extends IApsEntity {
	
	/**
	 * Return of the username that is associated with the profile.
	 * @return The username.
	 */
	public String getUsername();
	
	public String getDisplayName();
	
	/**
	 * Returns the value of an attribute identified by his key. 
	 * The value can be of any type.
	 * @param key The key of the attribute.
	 * @return The value of the attribute.
	 */
	public Object getValue(String key);
	
	public Object getValueByRole(String rolename);
	
	/**
	 * Return the name of the attribute that represents the fullname.
	 * @return the name of the attribute.
	 */
	public String getFullNameAttributeName();
	
	/**
	 * Return the name of the attribute that represents the mail address.
	 * @return the name of the attribute.
	 */
	public String getMailAttributeName();
	
	public boolean isPublicProfile();
	
	public void setPublicProfile(boolean isPublic);
	
}