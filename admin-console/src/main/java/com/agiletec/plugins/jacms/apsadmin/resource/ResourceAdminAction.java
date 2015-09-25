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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Action delegated to execute administrative tasks on resources
 * @author E.Santoboni
 */
public class ResourceAdminAction extends AbstractResourceAction implements IResourceAdminAction {

	private static final Logger _logger = LoggerFactory.getLogger(ResourceAction.class);
	
	@Override
	public String refreshResourcesInstances() {
		try {
			this.getResourceManager().refreshResourcesInstances(this.getResourceTypeCode());
			_logger.info("Refreshing started");
		} catch (Throwable t) {
			_logger.error("error in refreshResourcesInstances", t);
			//ApsSystemUtils.logThrowable(t, this, "refreshResourcesInstances");
			return FAILURE;
		}
		return SUCCESS;
	}
	
	public int getResourceManagerStatus() {
		return this.getResourceManager().getStatus();
	}
	
}
