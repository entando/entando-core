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
package org.entando.entando.aps.system.services.controller.executor;

import freemarker.template.Configuration;
import freemarker.template.TemplateModel;

/**
 * Object that contains helpers, template service objects (and so on) 
 * used by the executors services.
 * @author E.Santoboni
 */
public class ExecutorBeanContainer {
	
	public ExecutorBeanContainer(Configuration configuration, TemplateModel templateModel) {
		this.setConfiguration(configuration);
		this.setTemplateModel(templateModel);
	}
	
	public Configuration getConfiguration() {
		return _configuration;
	}
	protected void setConfiguration(Configuration configuration) {
		this._configuration = configuration;
	}
	
	public TemplateModel getTemplateModel() {
		return _templateModel;
	}
	protected void setTemplateModel(TemplateModel templateModel) {
		this._templateModel = templateModel;
	}
	
	private Configuration _configuration;
	private TemplateModel _templateModel;
	
}
