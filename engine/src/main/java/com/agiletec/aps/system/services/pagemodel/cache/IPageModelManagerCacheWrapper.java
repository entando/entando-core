/*
 * Copyright 2018-Present Entando Inc. (http://www.entando.com) All rights reserved.
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
package com.agiletec.aps.system.services.pagemodel.cache;

import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.aps.system.services.pagemodel.IPageModelDAO;
import com.agiletec.aps.system.services.pagemodel.PageModel;
import java.util.Collection;

/**
 * @author E.Santoboni
 */
public interface IPageModelManagerCacheWrapper {

	public static final String PAGE_MODEL_MANAGER_CACHE_NAME = "Entando_PageModelManager";
	public static final String PAGE_MODEL_CACHE_NAME_PREFIX = "PageModelManager_model_";
	public static final String PAGE_MODEL_CODES_CACHE_NAME = "PageModelManager_codes";

	public void initCache(IPageModelDAO pageModelDAO) throws ApsSystemException;

	public PageModel getPageModel(String name);

	public Collection<PageModel> getPageModels();

	public void addPageModel(PageModel pageModel);

	public void updatePageModel(PageModel pageModel);

	public void deletePageModel(String code);

}
