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