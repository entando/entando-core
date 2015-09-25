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