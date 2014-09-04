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
package org.entando.entando.apsadmin.common.currentuser;

import org.entando.entando.aps.system.services.userprofile.model.IUserProfile;
import org.entando.entando.apsadmin.common.UserAvatarAction;

import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.aps.system.services.user.UserDetails;

/**
 * @author E.Santoboni
 */
public class CurrentAvatarAction extends UserAvatarAction {
	
	@Override
	protected IUserProfile getUserProfile() throws ApsSystemException {
		UserDetails currentUser = super.getCurrentUser();
		IUserProfile profile = (null != currentUser && null != currentUser.getProfile()) 
				? (IUserProfile) currentUser.getProfile() 
				: null;
		return profile;
	}
	
}