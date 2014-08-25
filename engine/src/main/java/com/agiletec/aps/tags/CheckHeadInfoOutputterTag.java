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
 * Utility tag for the compilation of the header informations.
 * It verifies the availability of the informations of the specified type.
 * 
 * @author E.Santoboni
 */
public class CheckHeadInfoOutputterTag extends TagSupport {

	public int doStartTag() throws JspException {
		ServletRequest request =  this.pageContext.getRequest();
		RequestContext reqCtx = (RequestContext) request.getAttribute(RequestContext.REQCTX);
		HeadInfoContainer headInfo = (HeadInfoContainer) reqCtx.getExtraParam(SystemConstants.EXTRAPAR_HEAD_INFO_CONTAINER);
		List<Object> infos = headInfo.getInfos(this.getType());
		if (infos == null || infos.size() == 0) {
			return SKIP_BODY;
		} else {
			return EVAL_BODY_INCLUDE;
		}
	}

	public void release() {
		this._type = null;
	}

	/**
	 * Return the type of the information being verified.
	 * @return The information type.
	 */
	public String getType() {
		return _type;
	}

	/**
	 * Set the type of the information being verified.
	 * @param type The information type.
	 */
	public void setType(String type) {
		this._type = type;
	}

	private String _type;

}