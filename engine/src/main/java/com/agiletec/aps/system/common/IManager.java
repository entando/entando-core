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
package com.agiletec.aps.system.common;

import java.io.Serializable;

/**
 * Base interface for implementing Services.
 * @author E.Santoboni
 */
public interface IManager extends RefreshableBean, Serializable {
	
	/**
	 * Service initialization.
	 * @throws Exception In the case of error when service is initialized.
	 */
	public void init() throws Exception;
	
	/**
	 * Destroy method invoked on bean factory shutdown.
	 */
	public void destroy();
	
	/** 
	 * Return the service name.
	 * @return the service name.
	 */
	public String getName();

	
}
