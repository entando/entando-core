/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.entando.entando.apsadmin.portal.guifragment;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agiletec.apsadmin.admin.BaseAdminAction;

/**
 *
 * @author paco
 */
public class GuiFragmentSettingAction extends BaseAdminAction {

	private static final Logger _logger = LoggerFactory.getLogger(GuiFragmentSettingAction.class);

	/**
	 * Update the system params.
	 * 
	 * @return the result code.
	 */
	@Override
	public String updateSystemParams() {
		return this.updateSystemParams(true);
	}

}
