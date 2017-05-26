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
package com.agiletec.apsadmin.portal.helper;

import com.agiletec.aps.system.services.page.IPage;
import com.opensymphony.xwork2.ActionSupport;

public interface IPageActionReferencesHelper {

	/**
	 * Check if the page contains valid contents.
	 * <p>
	 * For each invalid content found, adds an actionErrot to the action
	 * 
	 * @param page
	 * the page to scan
	 * @param action
	 * current action
	 * @return true if the scan is performed without exceptions, even when
	 * invalid contents are found
	 */
	boolean checkContentsForSetOnline(IPage page, ActionSupport action);

}
