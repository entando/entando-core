/*
*
* Copyright 2013 Entando S.r.l. (http://www.entando.com) All rights reserved.
*
* This file is part of Entando software.
* Entando is a free software;
* You can redistribute it and/or modify it
* under the terms of the GNU General Public License (GPL) as published by the Free Software Foundation; version 2.
* 
* See the file License for the specific language governing permissions   
* and limitations under the License
* 
* 
* 
* Copyright 2013 Entando S.r.l. (http://www.entando.com) All rights reserved.
*
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
