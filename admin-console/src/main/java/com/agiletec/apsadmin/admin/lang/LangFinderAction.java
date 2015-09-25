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
package com.agiletec.apsadmin.admin.lang;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agiletec.aps.system.services.lang.Lang;
import com.agiletec.apsadmin.system.BaseAction;

/**
 * This action class implements the default actions to display and search among the system languages.
 * @author E.Santoboni
 */
public class LangFinderAction extends BaseAction implements ILangFinderAction {

	private static final Logger _logger = LoggerFactory.getLogger(LangFinderAction.class);
	
	public List<Lang> getLangs() {
		List<Lang> langs = null;
		try {
			langs = this.getLangManager().getLangs();
		} catch(Throwable t) {
			_logger.error("Error extracting system lang ", t);
			//ApsSystemUtils.logThrowable(t, this, "getLangs");
			throw new RuntimeException("Error extracting system lang", t);
		}
		return langs;
	}
	
	/**
	 * Return the list of available languages for the system, sorted by description. 
	 * @return The list of available languages.
	 */
	public List<Lang> getAssignableLangs() {
		List<Lang> assignableLangs = null;
		try {
			assignableLangs = this.getLangManager().getAssignableLangs();
		} catch(Throwable t) {
			_logger.error("Error extracting assignable langs", t);
			//ApsSystemUtils.logThrowable(t, this, "getAssignableLangs");
			throw new RuntimeException("Error extracting assignable langs", t);
		}
		return assignableLangs;
	}
	
}