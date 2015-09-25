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
package com.agiletec.apsadmin.category;

/**
 * Interface defining the actions needed in order to handle categories.  
 * @author E.Santoboni
 */
public interface ICategoryAction {
	
	/**
	 * Create a new category.
	 * @return The result code.
	 */
	public String add();
	
	/**
	 * Edit an existing category.
	 * @return The result code.
	 */
	public String edit();
	
	/**
	 * Show the detail of a category.
	 * @return The result code.
	 */
	public String showDetail();
	
	/**
	 * Start the deletion process of a category.
	 * @return The result code.
	 */
	public String trash();
	
	/**
	 * Delete a category permanently.
	 * @return The result code.
	 */
	public String delete();
	
	/**
	 * Save a category.
	 * @return The result code.
	 */
	public String save();
	
}
