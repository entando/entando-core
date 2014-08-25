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
