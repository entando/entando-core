/*
*
* Copyright 2014 Entando S.r.l. (http://www.entando.com) All rights reserved.
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
* Copyright 2014 Entando S.r.l. (http://www.entando.com) All rights reserved.
*
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
