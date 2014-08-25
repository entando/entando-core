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
package com.agiletec.aps.system.common.renderer;

import com.agiletec.aps.system.common.entity.model.attribute.ITextAttribute;

/**
 * @author E.Santoboni
 */
public final class TextAttributeCharReplaceInfo {
	
	protected TextAttributeCharReplaceInfo(ITextAttribute textAttribute, String oldtext, String langCode) {
		this.setTextAttribute(textAttribute);
		this.setOldtext(oldtext);
		this.setLangCode(langCode);
	}
	
	protected void restore() {
		this.getTextAttribute().setText(this.getOldtext(), this.getLangCode());
	}
	
	private ITextAttribute getTextAttribute() {
		return _textAttribute;
	}
	private void setTextAttribute(ITextAttribute textAttribute) {
		this._textAttribute = textAttribute;
	}
	private String getOldtext() {
		return _oldtext;
	}
	private void setOldtext(String oldtext) {
		this._oldtext = oldtext;
	}
	private String getLangCode() {
		return _langCode;
	}
	private void setLangCode(String langCode) {
		this._langCode = langCode;
	}
	
	private ITextAttribute _textAttribute;
	private String _oldtext;
	private String _langCode;
	
}
