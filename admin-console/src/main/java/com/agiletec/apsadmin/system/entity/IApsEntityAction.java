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
package com.agiletec.apsadmin.system.entity;

/**
 * @author E.Santoboni
 */
public interface IApsEntityAction {
	
	/**
	 * Create a new entity.
	 * @return The result code.
	 */
	public String createNew();
	
	/**
	 * View an existing entity.
	 * @return The result code.
	 */
	public String view();
	
	/**
	 * Edit an existing entity.
	 * @return The result code.
	 */
	public String edit();
	
	/**
	 * Save an entity.
	 * @return The result code.
	 */
	public String save();
	
}
