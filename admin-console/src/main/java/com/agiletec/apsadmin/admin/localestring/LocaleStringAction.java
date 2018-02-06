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
package com.agiletec.apsadmin.admin.localestring;

import java.util.Iterator;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agiletec.aps.system.services.i18n.II18nManager;
import com.agiletec.aps.system.services.lang.Lang;
import com.agiletec.aps.util.ApsProperties;
import com.agiletec.apsadmin.system.ApsAdminSystemConstants;
import com.agiletec.apsadmin.system.BaseAction;

/**
 * This base action class declares the default operations for the Localization
 * Strings.
 * 
 * @author E.Santoboni
 */
public class LocaleStringAction extends BaseAction {

	private static final Logger _logger = LoggerFactory.getLogger(LocaleStringAction.class);

	@Override
	public void validate() {
		super.validate();
		this.checkKey();
		this.checkLabels();
	}

	public String newLabel() {
		this.setStrutsAction(ApsAdminSystemConstants.ADD);
		return SUCCESS;
	}

	public String edit() {
		try {
			String key = this.getKey();
			ApsProperties labels = (ApsProperties) this.getI18nManager().getLabelGroups().get(key);
			this.setLabels(labels);
			this.setStrutsAction(ApsAdminSystemConstants.EDIT);
		} catch (Throwable t) {
			_logger.error("error in edit", t);
			return FAILURE;
		}
		return SUCCESS;
	}

	public String save() {
		try {
			int strutsAction = this.getStrutsAction();
			if (ApsAdminSystemConstants.ADD == strutsAction) {
				this.getI18nManager().addLabelGroup(this.getKey(), this.getLabels());
			} else if (ApsAdminSystemConstants.EDIT == strutsAction) {
				this.getI18nManager().updateLabelGroup(this.getKey(), this.getLabels());
			}
		} catch (Throwable t) {
			_logger.error("error in save", t);
			return FAILURE;
		}
		return SUCCESS;
	}

	public String trash() {
		String key = this.getKey();
		ApsProperties labels = (ApsProperties) this.getI18nManager().getLabelGroups().get(key);
		this.setLabels(labels);
		return SUCCESS;
	}

	public String delete() {
		try {
			this.getI18nManager().deleteLabelGroup(this.getKey());
		} catch (Throwable t) {
			_logger.error("error in delete", t);
			return FAILURE;
		}
		return SUCCESS;
	}

	private void checkKey() {
		String key = this.getKey();
		if (this.getStrutsAction() == ApsAdminSystemConstants.ADD && null != key && key.trim().length() > 0) {
			String currectKey = key.trim();
			this.setKey(currectKey);
			if (null != this.getI18nManager().getLabelGroups().get(currectKey)) {
				String[] args = { currectKey };
				this.addFieldError("key", this.getText("error.label.keyAlreadyPresent", args));
			}
			this.setKey(currectKey);
		}
	}

	private void checkLabels() {
		Iterator<Lang> langsIter = this.getLangManager().getLangs().iterator();
		while (langsIter.hasNext()) {
			Lang lang = langsIter.next();
			String code = lang.getCode();
			String label = this.getRequest().getParameter(code);
			if (null != label && label.trim().length() > 0) {
				this.addLabel(code, label);
			} else {
				this.getLabels().remove(code);
				if (lang.isDefault()) {
					String[] args = { lang.getDescr() };
					this.addFieldError(code, this.getText("error.label.valueMandatory", args));
				}
			}
		}
	}

	public List<Lang> getLangs() {
		return this.getLangManager().getLangs();
	}

	public String getKey() {
		return _key;
	}

	public void setKey(String key) {
		this._key = key;
	}

	public ApsProperties getLabels() {
		return _labels;
	}

	protected void setLabels(ApsProperties labels) {
		this._labels = labels;
	}

	protected void addLabel(String code, String label) {
		this._labels.put(code, label);
	}

	public int getStrutsAction() {
		return _strutsAction;
	}

	public void setStrutsAction(int strutsAction) {
		this._strutsAction = strutsAction;
	}

	protected II18nManager getI18nManager() {
		return _i18nManager;
	}

	public void setI18nManager(II18nManager i18nManager) {
		_i18nManager = i18nManager;
	}

	private String _key;
	private ApsProperties _labels = new ApsProperties();
	private int _strutsAction;

	private II18nManager _i18nManager;

}