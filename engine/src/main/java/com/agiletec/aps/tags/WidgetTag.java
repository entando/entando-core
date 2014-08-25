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

import javax.servlet.ServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agiletec.aps.system.RequestContext;
import com.agiletec.aps.system.SystemConstants;
import com.agiletec.aps.system.services.page.IPage;

/**
 * Includes the jsp associated to the widget as configured in the page frame
 * 
 */
public class WidgetTag extends TagSupport {

	private static final Logger _logger = LoggerFactory.getLogger(WidgetTag.class);
	
	public int doEndTag() throws JspException {
		ServletRequest req =  this.pageContext.getRequest();
		RequestContext reqCtx = (RequestContext) req.getAttribute(RequestContext.REQCTX);
		IPage page = (IPage) reqCtx.getExtraParam(SystemConstants.EXTRAPAR_CURRENT_PAGE);
		try {
			String showletOutput[] = (String[]) reqCtx.getExtraParam("ShowletOutput");
			if(_frame <0 || _frame >= showletOutput.length){
				_logger.error("Frame attribute {} exceeds the limit in the page {}", _frame, page.getCode());
				String msg = "Frame attribute =" + _frame + " exceeds the limit in the page " + page.getCode();
				throw new JspException(msg);
				//ApsSystemUtils.getLogger().error(msg);				
			}
			String showlet = showletOutput[_frame];
			if (null == showlet) 
				showlet = "";
			this.pageContext.getOut().print(showlet);
		} catch (Throwable t) {
			String msg = "Error detected in the inclusion of the output widget";
			_logger.error("Error detected in the inclusion of the output widget", t);
			//ApsSystemUtils.logThrowable(t, this, "doEndTag", msg);
			throw new JspException(msg, t);
		}
		
		return EVAL_PAGE;
	}
	
	/**
	 * Return the frame attribute
	 * @return The positional frame number
	 */
	public int getFrame() {
		return _frame;
	}

	/**
	 * Set the frame attribute
	 * @param frame The positional frame number
	 */
	public void setFrame(int frame) {
		this._frame = frame;
	}

	private int _frame;
}
