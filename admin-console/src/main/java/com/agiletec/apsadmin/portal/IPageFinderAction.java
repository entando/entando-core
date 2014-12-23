/*
 * Copyright 2013-Present Entando Corporation (http://www.entando.com) All rights reserved.
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

import java.util.List;

import com.agiletec.aps.system.services.page.IPage;

/**
 * This class declares the method used to search through pages. Noticeably we don't have here a 
 * standard method whose return type is either the string 'SUCCESS' or 'INPUT' (or whatever); as of
 * the current version we have a method to invoke directly from the jsp of the view layer.
 * @author M.E. Minnai
 */
public interface IPageFinderAction {

	/**
	 * Return a list containing the given token in its code.
	 * @param pageCodeToken the piece of code to look for among the pages
	 * @return a List of the page code and containing the full page informations.
	 */
	public List<IPage> getPagesFound();
	
}
