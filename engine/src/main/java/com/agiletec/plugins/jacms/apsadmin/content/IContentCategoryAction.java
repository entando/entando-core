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