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
 * Interface of action delegated to execute administrative tasks on resources
 * @author E.Santoboni
 */
public interface IResourceAdminAction {
	
	/**
	 * Refresh all the resources instance (not the "main" instance)
	 * @return The code describing the result of the operation.
	 */
	public String refreshResourcesInstances();
	
}