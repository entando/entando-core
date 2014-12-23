/*
 * Copyright 2013-Present Entando Corporation (http://www.entando.com) All rights reserved.
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
package com.agiletec.aps.system.services.controller.control;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agiletec.aps.system.RequestContext;
import com.agiletec.aps.system.services.controller.ControllerManager;

/**
 * Implementazione del sottoservizio di controllo che redirezione verso i servizi esecutori.
 * @author M.Diana
 */
public class Executor implements ControlServiceInterface {

	private static final Logger _logger = LoggerFactory.getLogger(Executor.class);
	
	@Override
	public void afterPropertiesSet() throws Exception {
		_logger.debug("{} : initialized", this.getClass().getName());
	}
	
	@Override
	public int service(RequestContext reqCtx, int status) {
		if (status == ControllerManager.ERROR) {
			return status;
		}
		return ControllerManager.OUTPUT;
	}
	
}
