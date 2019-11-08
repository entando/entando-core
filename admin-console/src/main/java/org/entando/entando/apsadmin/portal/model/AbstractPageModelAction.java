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
package org.entando.entando.apsadmin.portal.model;

import com.agiletec.aps.system.services.pagemodel.*;
import com.agiletec.apsadmin.system.BaseAction;

public abstract class AbstractPageModelAction extends BaseAction {

	private IPageModelManager pageModelManager;

	protected PageModel getPageModel(String code) {
		PageModel pageModel = this.getPageModelManager().getPageModel(code);
		if (null != pageModel) {
			return pageModel.clone();
		}
		return null;
	}
	
	protected IPageModelManager getPageModelManager() {
		return pageModelManager;
	}
	public void setPageModelManager(IPageModelManager pageModelManager) {
		this.pageModelManager = pageModelManager;
	}
	
}