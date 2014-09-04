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
package com.agiletec.apsadmin.portal;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.beanutils.BeanComparator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agiletec.aps.system.services.group.Group;
import com.agiletec.aps.system.services.page.IPage;
import com.agiletec.aps.system.services.user.UserDetails;

/**
 * This action class contains all the elements currently use to perform searches across the portal pages.
 * @author M.E. Minnai
 */
public class PageFinderAction extends AbstractPortalAction implements IPageFinderAction {

	private static final Logger _logger = LoggerFactory.getLogger(PageFinderAction.class);
	
	@Override
	public List<IPage> getPagesFound() {	
		List<IPage> result = null;
		try {
			List<String> allowedGroupCodes = this.getAllowedGroupCodes();
			result = this.getPageManager().searchPages(this.getPageCodeToken(), allowedGroupCodes);
			BeanComparator comparator = new BeanComparator("code");
			Collections.sort(result, comparator);
		} catch (Throwable t) {
			_logger.error("Error on searching pages", t);
			//ApsSystemUtils.logThrowable(t, this, "Error on searching pages");
			throw new RuntimeException("Error on searching pages", t);
		}
		return result;
	}
	
	private List<String> getAllowedGroupCodes() {
		List<String> allowedGroups = new ArrayList<String>();
		UserDetails currentUser = this.getCurrentUser();
		List<Group> userGroups = this.getAuthorizationManager().getUserGroups(currentUser);
		Iterator<Group> iter = userGroups.iterator();
    	while (iter.hasNext()) {
    		Group group = iter.next();
    		allowedGroups.add(group.getName());
    	}
    	return allowedGroups;
	}
	
	/**
	 * Set the token to be searched among the pages
	 * @param pageCodeToken The token to be searched among the pages
	 */
	public void setPageCodeToken(String pageCodeToken) {
		this._pageCodeToken = pageCodeToken;
	}
	
	/**
	 * Return the token to be searched among the pages
	 * @return The token to be searched among the pages
	 */
	public String getPageCodeToken() {
		return _pageCodeToken;
	}
	
	private String _pageCodeToken;
	
}