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

import com.agiletec.aps.system.exception.ApsSystemException;
import java.util.Collection;

/**
 * Interface of the page models manager.
 * @author E.Santoboni
 */
public interface IPageModelManager {

	/**
	 * Return a Page Model by the code.
	 * @param code The code of the Page Model
	 * @return The required Page Model
	 */
	public PageModel getPageModel(String code);
	
	/**
	 * Return the collection of defined Page Models
	 * @return The collection of defined Page Models
	 */
	public Collection<PageModel> getPageModels();
	
	public void addPageModel(PageModel pageModel) throws ApsSystemException;
	
	public void updatePageModel(PageModel pageModel) throws ApsSystemException;
	
	public void deletePageModel(String code) throws ApsSystemException;
	
}
