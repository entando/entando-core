/*
 * Copyright 2013-Present Entando Inc. (http://www.entando.com) All rights reserved.
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
public class PageFinderAction extends AbstractPortalAction {

	private static final Logger _logger = LoggerFactory.getLogger(PageFinderAction.class);
	
	public List<IPage> getPagesFound() {	
		List<IPage> result = null;
		try {
			List<String> allowedGroupCodes = this.getAllowedGroupCodes();
			result = this.getPageManager().searchPages(this.getPageCodeToken(), allowedGroupCodes);
			BeanComparator comparator = new BeanComparator("code");
			Collections.sort(result, comparator);
		} catch (Throwable t) {
			_logger.error("Error on searching pages", t);
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