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
package com.agiletec.aps.system.common.entity.parse.attribute;

/**
 * Interface for those Handler classes that parse the XML codes of the 'Simple Attributes' that support
 * multiple languages (multi-language attributes).
 * @author E.Santoboni
 */
public abstract class AbstractMLAttributeHandler extends AbstractAttributeHandler {
	
	/**
	 * Return the code of the current language.
	 * @return The code of the current language.
	 */
	public String getCurrentLangId() {
		return _currentLangId;
	}
	
	/**
	 * Set up the code of the current language.
	 * @param currentLangId The language code.
	 */
	public void setCurrentLangId(String currentLangId) {
		this._currentLangId = currentLangId;
	}
	
	/**
	 * The code of the current language.
	 */
	private String _currentLangId;
	
}
