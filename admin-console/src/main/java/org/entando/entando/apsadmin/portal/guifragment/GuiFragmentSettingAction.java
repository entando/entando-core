/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.entando.entando.apsadmin.portal.guifragment;

import com.agiletec.aps.system.SystemConstants;
import com.agiletec.aps.system.services.baseconfig.SystemParamsUtils;
import com.agiletec.apsadmin.admin.BaseAdminAction;
import static com.agiletec.apsadmin.system.BaseAction.FAILURE;
import static com.opensymphony.xwork2.Action.SUCCESS;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author paco
 */
public class GuiFragmentSettingAction extends BaseAdminAction {

	private static final Logger _logger = LoggerFactory.getLogger(GuiFragmentSettingAction.class);
	
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
