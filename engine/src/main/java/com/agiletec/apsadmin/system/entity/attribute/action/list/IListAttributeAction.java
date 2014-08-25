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
package com.agiletec.apsadmin.system.entity.attribute.action.list;

/**
 * Interface for those actions which manage the operations on list attributes
 * @author E.Santoboni
 */
public interface IListAttributeAction {
	
	/**
	 * Add a new element in the list. The elements needed in order
	 * to perform the operation are obtained from the parameters 
	 * of the current request.
	 *  
	 * @return The code describing the result of the operation.
	 */
	public String addListElement();
	
	/**
	 * Remove an element from the list. The elements needed in order
	 * to perform the operation are obtained from the parameters 
	 * of the current request.
	 *  
	 * @return The code describing the result of the operation.
	 */
	public String removeListElement();
	
	/**
	 * Move an element in the list. The elements needed in order
	 * to perform the operation are obtained from the parameters 
	 * of the current request.
	 *  
	 * @return The code describing the result of the operation.
	 */
	public String moveListElement();
	
	public static final String MOVEMENT_UP_CODE = "UP";
	public static final String MOVEMENT_DOWN_CODE = "DOWN";
	
}