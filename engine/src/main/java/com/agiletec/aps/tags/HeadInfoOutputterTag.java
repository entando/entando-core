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

import java.util.List;

import javax.servlet.ServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

import com.agiletec.aps.system.RequestContext;
import com.agiletec.aps.system.SystemConstants;
import com.agiletec.aps.tags.util.HeadInfoContainer;

/**
 * Iterates over various information in HTML header displaying them.
 * It retrieves data from a "HeadInfoContainer" object located in the
 * RequestContext.
 * This tag works in conjunction with other sub-tag specific to the information
 * type
 * Please note that the body can contain only a sub-tag, or information, at once.<br/>
 * This tag must be used <b>only</b> in a page model. 
 */
@SuppressWarnings("serial")
public class HeadInfoOutputterTag extends TagSupport {
	
	public int doStartTag() throws JspException {
		ServletRequest request =  this.pageContext.getRequest();
		RequestContext reqCtx = (RequestContext) request.getAttribute(RequestContext.REQCTX);
		HeadInfoContainer headInfo = (HeadInfoContainer) reqCtx.getExtraParam(SystemConstants.EXTRAPAR_HEAD_INFO_CONTAINER);
		int retVal;
		List<Object> infos = headInfo.getInfos(this.getType());
		if (infos != null) {
			this._infos = infos;
			this._index = 0;
			retVal = EVAL_BODY_INCLUDE;
		} else {
			retVal = SKIP_BODY;
		}
		return retVal;
	}
	
	public int doAfterBody() throws JspException {
		int retVal;
		this._index++;
		if (this._index >= this._infos.size()) {
			retVal = SKIP_BODY;
		} else {
			retVal = EVAL_BODY_AGAIN;
		}
		return retVal;
	}
	
	public Object getCurrentInfo() {
		return this._infos.get(this._index);
	}
	
	public void release() {
		this._type = null;
		this._infos = null;
	}
	
	public String getType() {
		return _type;
	}
	
	public void setType(String type) {
		this._type = type;
	}
	
	private List<Object> _infos;
	private String _type;
	private int _index;
	
}
