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
package com.agiletec.apsadmin.tags;

import com.agiletec.aps.system.SystemConstants;
import com.agiletec.aps.system.services.page.IPageManager;
import com.agiletec.aps.util.ApsWebApplicationUtils;

/**
 * Returns a page (or one of its property) through the code.
 * You can choose whether to return the entire object (leaving the attribute "property" empty) or a single property.
 * The names of the available property of "Page": "code" (the code page), "parent (the parent page), "children" (the list of child pages),
 * "position" (the position relative to other pages), "titles" (map of titles indexed by the system languages), "model" (the page model),
 * "group" (the code of the owner group), "extraGroups" (the list of extra group codes), "showable" (whether to be displayed in the menu),
 * "useExtraTitles" (if the page must use any extra titles), "widgets" (the widgets applied to the page).
 * @author E.Santoboni
 */
public class PageInfoTag extends AbstractObjectInfoTag {

	@Override
	protected Object getMasterObject(String keyValue) throws Throwable {
		IPageManager pageManager = (IPageManager) ApsWebApplicationUtils.getBean(SystemConstants.PAGE_MANAGER, this.pageContext);
		return pageManager.getDraftPage(keyValue);
	}

}