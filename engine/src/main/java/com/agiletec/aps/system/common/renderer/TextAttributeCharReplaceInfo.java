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
