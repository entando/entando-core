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
package org.entando.entando.apsadmin.portal.helper;

import java.util.Collection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agiletec.aps.system.services.page.IPage;
import com.agiletec.apsadmin.portal.helper.AbstractPageActionHelper;

/**
 *
 */
public class PageActionHelperOnline extends AbstractPageActionHelper {

	private static final Logger _logger = LoggerFactory.getLogger(PageActionHelperOnline.class);

	@Override
	protected IPage getPage(String pageCode) {
		return this.getPageManager().getOnlinePage(pageCode);
	}

	@Override
	protected IPage getRoot() {
		return this.getPageManager().getOnlineRoot();
	}

	@Override
	protected boolean isPageAllowed(IPage page, Collection<String> groupCodes, boolean alsoFreeViewPages) {
		return page != null && page.isOnline() && super.isPageAllowed(page, groupCodes, alsoFreeViewPages);
	}

}
