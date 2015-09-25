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
package com.agiletec.plugins.jacms.apsadmin.content;

/**
 * Base interface for the Action class that manages the category tree operation on content finding GUI interface 
 * and the relationships between content and categories.
 * @author E.Santoboni
 */
public interface IContentCategoryAction {
	
	/**
	 * Performs the action of adding of a category to the content.
	 * @return The result code.
	 */
	public String joinCategory();
	
	/**
	 * Performs the action of removing a category from the content.
	 * @return The result code.
	 */
	public String removeCategory();
	
}