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
package com.agiletec.aps.system.common;

import java.io.Serializable;

import com.agiletec.aps.system.exception.ApsException;

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
