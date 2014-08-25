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
