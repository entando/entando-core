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

/**
 * This base interface declares the default actions for the system Languages.
 * @author E.Santoboni
 */
public interface ILangAction {
	
	/**
	 * Executes the specific action to add a lang to the system languages.
	 * @return The result code.
	 */
	public String add();
	
	/**
	 * Executes the specific action to remove a lang from the system languages.
	 * @return The result code.
	 */
	public String remove();
	
}