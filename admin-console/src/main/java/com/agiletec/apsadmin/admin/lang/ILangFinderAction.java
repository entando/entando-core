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
package com.agiletec.apsadmin.admin.lang;

import java.util.List;

import com.agiletec.aps.system.services.lang.Lang;

/**
 * This base interface declares the default actions to search and display among the system languages.   
 * @author E.Santoboni
 */
public interface ILangFinderAction {
	
	/**
	 * Return the list of system languages
	 * @return the list of system languages
	 */
	public List<Lang> getLangs();
	
}
