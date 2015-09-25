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
package com.agiletec.plugins.jacms.apsadmin.resource;

/**
 * Basic interface for actions related to resource handling 
 * @version 1.0
 * @author E.Santoboni
 */
public interface IResourceAction {
	
	/**
	 * Executes the specific action to create a new content 
	 * @return The result code.
	 */
	public String newResource();
	
	/**
	 * Executes the specific action to modify an existing resource.
	 * @return The result code.
	 */
	public String edit();
	
	/**
	 * Executes the specific action to modify an existing resource. 
	 * @return The result code.
	 */
	public String save();
	
	/**
	 * Executes the specific action in order to associate a category to the resource on edit.
	 * @return The result code.
	 */
	public String joinCategory();
	
	/**
	 * Executes the specific action in order to remove the association between a category and the resource on edit. 
	 * @return The result code.
	 */
	public String removeCategory();
	
	/**
	 * Executes the specific action to delete a resource. This does NOT perform any deletion, it just ensures that there are
	 * no hindrances to a deletion process.
	 * @return The result code.
	 */
	public String trash();
	
	/**
	 * This forces the deletion of a resource.<br>NOTE! This method is invoked, in the administration interface,
	 * when deleting a referenced resource.
	 * @return The result code.
	 */
	public String delete();
	
}
