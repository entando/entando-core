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
import com.agiletec.aps.tags.util.HeadInfoContainer;

/**
 * Tag for the declaration of the informations to insert in the header of the HTML page
 */
public class HeadInfoTag extends TagSupport {

	private static final Logger _logger = LoggerFactory.getLogger(HeadInfoTag.class);
	
	public int doEndTag() throws JspException {
		ServletRequest request =  this.pageContext.getRequest();
		RequestContext reqCtx = (RequestContext) request.getAttribute(RequestContext.REQCTX);
		try {
			HeadInfoContainer headInfo  = (HeadInfoContainer) reqCtx.getExtraParam(SystemConstants.EXTRAPAR_HEAD_INFO_CONTAINER);
			if (_type != null && (_info != null || _var != null)){
				Object info = _info;
				if(_info != null) {
					info = _info;
				} else {
					info = this.pageContext.getAttribute(_var);
				}
				headInfo.addInfo(_type, info);
			}
		} catch (Throwable t) {
			_logger.error("Error closing tag", t);
			//ApsSystemUtils.logThrowable(t, this, "doEndTag");
			throw new JspException("Error closing tag ", t);
		}
		return EVAL_BODY_INCLUDE;
	}

	public void release() {
		_type = null;
		_var = null;
		_info = null;
	}

	public String getInfo() {
		return _info;
	}

	public void setInfo(String info) {
		this._info = info;
	}

	public void setVar(String var) {
		this._var = var;
	}

	public String getVar() {
		return _var;
	}

	public String getType() {
		return _type;
	}

	public void setType(String type) {
		this._type = type;
	}

	private String _var;
	private String _info;
	private String _type;

}