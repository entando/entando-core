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
package com.agiletec.apsadmin.system.entity;

import com.agiletec.aps.system.common.entity.model.IApsEntity;
import com.agiletec.aps.system.services.baseconfig.ConfigInterface;
import com.agiletec.aps.system.services.lang.Lang;
import com.agiletec.apsadmin.system.BaseAction;

/**
 * Abstract action for ApsEntity management.
 * @author E.Santoboni
 */
public abstract class AbstractApsEntityAction extends BaseAction implements IApsEntityAction {
	
	@Override
	public void validate() {
		super.validate();
		this.getEntityActionHelper().updateEntity(this.getApsEntity(), this.getRequest());
		this.getEntityActionHelper().scanEntity(this.getApsEntity(), this);
	}
	
	/**
	 * Return the entity on edit.
	 * @return The current entity on edit.
	 */
	public abstract IApsEntity getApsEntity();
	
	/**
	 * Return the default system lang.
	 * @return The default lang.
	 */
	public Lang getDefaultLang() {
		return this.getLangManager().getDefaultLang();
	}
	
	/**
	 * Return the code of current html editor, set on system config user interface.
	 * @return The code of current html editor.
	 */
	public String getHtmlEditorCode() {
		return this.getConfigManager().getParam("hypertextEditor");
	}
	
	protected IEntityActionHelper getEntityActionHelper() {
		return _entityActionHelper;
	}
	public void setEntityActionHelper(IEntityActionHelper entityActionHelper) {
		this._entityActionHelper = entityActionHelper;
	}
	
	protected ConfigInterface getConfigManager() {
		return _configManager;
	}
	public void setConfigManager(ConfigInterface configManager) {
		this._configManager = configManager;
	}
	
	public String getEntityId() {
		return _entityId;
	}
	public void setEntityId(String entityId) {
		this._entityId = entityId;
	}
	
	private IEntityActionHelper _entityActionHelper;
	
	private ConfigInterface _configManager;
	
	private String _entityId;
	
}