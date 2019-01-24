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
package com.agiletec.aps.system.services.pagemodel;

import com.agiletec.aps.system.common.FieldSearchFilter;
import com.agiletec.aps.system.common.model.dao.SearcherDaoPaginatedResult;
import com.agiletec.aps.system.exception.ApsSystemException;

import java.util.*;

/**
 * Interface of the page models manager.
 */
public interface IPageModelManager {

	/**
	 * Return a Page Model by the code.
	 *
	 * @param code The code of the Page Model
	 * @return The required Page Model
	 */
	PageModel getPageModel(String code);

	/**
	 * Return the collection of defined Page Models
	 *
	 * @return The collection of defined Page Models
	 */
	Collection<PageModel> getPageModels();

	void addPageModel(PageModel pageModel) throws ApsSystemException;

	void updatePageModel(PageModel pageModel) throws ApsSystemException;

	void deletePageModel(String code) throws ApsSystemException;

    SearcherDaoPaginatedResult<PageModel> searchPageModels(List<FieldSearchFilter> filters) throws ApsSystemException;

}
