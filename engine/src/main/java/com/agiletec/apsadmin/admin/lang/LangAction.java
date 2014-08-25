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
package com.agiletec.apsadmin.admin.lang;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agiletec.aps.system.services.lang.Lang;
import com.agiletec.apsadmin.system.BaseAction;

/**
 * This action class implements the default operations on System Languages.
 * @author E.Santoboni
 */
public class LangAction extends BaseAction implements ILangAction {

	private static final Logger _logger = LoggerFactory.getLogger(LangAction.class);
	
	@Override
	public String add() {
		try {
			Lang langToAdd = this.getLangManager().getLang(this.getLangCode());
			if (null != langToAdd) {
				String[] args = {this.getLangCode()};
				this.addActionError(this.getText("error.lang.alreadyPresent", args));
				return INPUT;
			}
			this.getLangManager().addLang(this.getLangCode());
		} catch (Throwable t) {
			_logger.error("error in add", t);
			//ApsSystemUtils.logThrowable(t, this, "add");
			return FAILURE;
		}
		return SUCCESS;
	}
	
	@Override
	public String remove() {
		try {
			Lang langToRemove = this.getLangManager().getLang(this.getLangCode());
			if (null == langToRemove || langToRemove.isDefault()) {
				this.addActionError(this.getText("error.lang.removeDefault"));
				return INPUT;
			}
			this.getLangManager().removeLang(this.getLangCode());
		} catch (Throwable t) {
			_logger.error("error in remove", t);
			//ApsSystemUtils.logThrowable(t, this, "remove");
			return FAILURE;
		}
		return SUCCESS;
	}
	
	public String getLangCode() {
		return _langCode;
	}
	public void setLangCode(String langCode) {
		this._langCode = langCode;
	}
	
	private String _langCode;
	
}