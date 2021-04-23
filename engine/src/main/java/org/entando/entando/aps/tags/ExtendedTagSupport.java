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
package org.entando.entando.aps.tags;

import java.io.IOException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.TagSupport;
import org.apache.commons.lang3.StringEscapeUtils;

/**
 * @author E.Santoboni
 */
public abstract class ExtendedTagSupport extends TagSupport {
	
	private boolean _escapeXml = true;
	
	public ExtendedTagSupport() {
		super();
		this.init();
	}
	
	// resets local state
	private void init() {
		this._escapeXml = true;
	}
	
	@Override
	public void release() {
		super.release();
		init();
	}
	
	public boolean getEscapeXml() {
		return _escapeXml;
	}
	public void setEscapeXml(boolean escapeXml) {
		this._escapeXml = escapeXml;
	}
	
	public static void out(PageContext pageContext, boolean escapeXml, Object obj) throws IOException {
		if (null != obj) {
			String text = (escapeXml) ? StringEscapeUtils.escapeXml(obj.toString()) : obj.toString();
			JspWriter w = pageContext.getOut();
			w.write(text);
		}
	}
	
}
