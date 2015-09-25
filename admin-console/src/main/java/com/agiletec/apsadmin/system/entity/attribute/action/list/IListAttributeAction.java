/*
 * Copyright 2015-Present Entando Inc. (http://www.entando.com) All rights reserved.
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