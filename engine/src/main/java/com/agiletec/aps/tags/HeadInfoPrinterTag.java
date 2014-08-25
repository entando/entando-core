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
