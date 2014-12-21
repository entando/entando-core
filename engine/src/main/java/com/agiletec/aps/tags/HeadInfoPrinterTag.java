/*
 * Copyright 2013-Present Entando Corporation (http://www.entando.com) All rights reserved.
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
package com.agiletec.aps.tags;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This tag must be used in conjunction with "HeadInfoOutputterTag"; it
 * returns the information to output.
 */
public class HeadInfoPrinterTag extends TagSupport {

	private static final Logger _logger = LoggerFactory.getLogger(HeadInfoPrinterTag.class);
	
	public int doEndTag() throws JspException {
		HeadInfoOutputterTag parent = 
			(HeadInfoOutputterTag) findAncestorWithClass(this, HeadInfoOutputterTag.class);
		String info = (String) parent.getCurrentInfo();
		try {
			this.pageContext.getOut().print(info);
		} catch (Throwable t) {
			_logger.error("error in doEndTag", t);
			//ApsSystemUtils.logThrowable(t, this, "doEndTag");
			throw new JspException("Error closing tag ", t);
		}
		return EVAL_PAGE;
	}
	
}
