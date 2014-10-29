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
package com.agiletec.aps.system.services.authorization;

import com.agiletec.aps.system.services.group.Group;
import com.agiletec.aps.system.services.role.Role;

import java.util.List;
import java.util.Map;

/**
 * @author E.Santoboni
 */
public interface IAuthorizationDAO {
	
	public void addUserAuthorization(String username, Authorization authorization);
	
	public void addUserAuthorizations(String username, List<Authorization> authorizations);
	
	public void updateUserAuthorizations(String username, List<Authorization> authorizations);
	
	public void deleteUserAuthorization(String username, String groupname, String rolename);
	
	public List<Authorization> getUserAuthorizations(String username, Map<String, Group> groups, Map<String, Role> roles);
	
	public void deleteUserAuthorizations(String username);
	
	public List<String> getUsersByAuthority(IApsAuthority authority);
	
	public List<String> getUsersByAuthority(String authorityName, boolean isRole);
	
}