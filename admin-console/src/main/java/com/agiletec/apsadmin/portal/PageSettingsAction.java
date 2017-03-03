package com.agiletec.apsadmin.portal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agiletec.aps.system.SystemConstants;
import com.agiletec.apsadmin.admin.BaseAdminAction;
import com.agiletec.apsadmin.admin.SystemParamsUtils;

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
