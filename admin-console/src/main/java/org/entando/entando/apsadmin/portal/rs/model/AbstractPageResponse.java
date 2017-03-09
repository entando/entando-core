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
package org.entando.entando.apsadmin.portal.rs.model;

import com.agiletec.aps.system.services.page.IPage;

public abstract class AbstractPageResponse {

	protected PageJO copyPage(IPage srcPage) {
		PageJO copiedPage = null;
		if (null != srcPage) {
			copiedPage = new PageJO();
			copiedPage.setCode(srcPage.getCode());
			copiedPage.setRoot(srcPage.isRoot());
			copiedPage.setOnline(srcPage.isOnline());
			copiedPage.setChanged(srcPage.isChanged());
			copiedPage.setParentCode(srcPage.getParentCode());
			copiedPage.setGroup(srcPage.getGroup());
			copiedPage.setPosition(srcPage.getPosition());
			copiedPage.setDraftMetadata(srcPage.getDraftMetadata());
			copiedPage.setDraftWidgets(srcPage.getDraftWidgets());
			copiedPage.setOnlineMetadata(srcPage.getOnlineMetadata());
			copiedPage.setOnlineWidgets(srcPage.getOnlineWidgets());
		}
		return copiedPage;
	}

}
