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

/**
 * Base interface for those action which handle the pages.
 * @author E.Santoboni
 */
public interface IPageAction {
	
	/**
	 * Create a new page
	 * @return The code describing the result of the operation.
	 */
	public String newPage();
	
	/**
	 * Edit a page.
	 * @return The result code.
	 */
	public String edit();
	
	/**
	 * Add an extra group.
	 * @return The code describing the result of the operation.
	 */
	public String joinExtraGroup();
	
	/**
	 * Remove an extra group.
	 * @return The code describing the result of the operation.
	 */
	public String removeExtraGroup();
	
	/**
	 * Show the detail of the page.
	 * @return The code describing the result of the operation.
	 */
	public String showDetail();
	
	/**
	 * Paste a page previously copied. 
	 * @return The code describing the result of the operation.
	 */
	public String paste();
	
	/**
	 * Save a page.
	 * @return The code describing the result of the operation.
	 */
	public String save();
	
	/**
	 * Start the deletion operations for the given page.
	 * @return The result code.
	 */
	public String trash();
	
	/**
	 * Delete a page from the system.
	 * @return The result code.
	 */
	public String delete();
	
}
