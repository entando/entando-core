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
package com.agiletec.apsadmin.portal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agiletec.aps.system.SystemConstants;
import com.agiletec.aps.system.services.baseconfig.SystemParamsUtils;
import com.agiletec.apsadmin.admin.BaseAdminAction;

public class PageSettingsAction extends BaseAdminAction {

	private static final Logger _logger = LoggerFactory.getLogger(PageSettingsAction.class);
	
    /**
	 * Update the system params.
	 * @return the result code.
	 */
	public String updateSystemParams() {
        try {
            this.initLocalMap();
            this.updateLocalParams(true);
            String xmlParams = this.getConfigManager().getConfigItem(SystemConstants.CONFIG_ITEM_PARAMS);
            this.extractExtraParameters();
            String newXmlParams = SystemParamsUtils.getNewXmlParams(xmlParams, this.getSystemParams());
            this.getConfigManager().updateConfigItem(SystemConstants.CONFIG_ITEM_PARAMS, newXmlParams);
            this.addActionMessage(this.getText("message.configSystemParams.ok"));
        } catch (Throwable t) {
        	_logger.error("error in updateSystemParams", t);
            return FAILURE;
        }
        return SUCCESS;
    }
	
}
