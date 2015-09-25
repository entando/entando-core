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
package com.agiletec.apsadmin.tags.form;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.jsp.JspException;

import org.apache.struts2.components.Component;

import com.agiletec.apsadmin.tags.util.AutoIndexingTagHelper;
import com.agiletec.apsadmin.tags.util.IAutoIndexingTag;
import com.agiletec.apsadmin.tags.util.Submit;
import com.opensymphony.xwork2.util.ValueStack;

/**
 * This class extends the org.apache.struts2.views.jsp.ui.SubmitTag 
 * in order to handle auto-incrementing Tabindex 
 * @author E.Santoboni
 */
public class SubmitTag extends org.apache.struts2.views.jsp.ui.SubmitTag implements IAutoIndexingTag {
	
	@Override
	public int doStartTag() throws JspException {
		String currentCounter = this.getCurrentIndex();
		if (null != currentCounter) {
			this.setTabindex(currentCounter);
		}
		return super.doStartTag();
	}
	
	@Override
    public Component getBean(ValueStack stack, HttpServletRequest request, HttpServletResponse response) {
		return new Submit(stack, request, response);
    }
	
	@Override
	public String getCurrentIndex() {
		return AutoIndexingTagHelper.getCurrentIndex(this, this.pageContext.getRequest());
	}
	
	@Override
	public Integer getStep() {
		return _step;
	}
	public void setStep(Integer step) {
		this._step = step;
	}
	
	@Override
	public Boolean getUseTabindexAutoIncrement() {
		return _useTabindexAutoIncrement;
	}
	public void setUseTabindexAutoIncrement(Boolean useTabindexAutoIncrement) {
		this._useTabindexAutoIncrement = useTabindexAutoIncrement;
	}
	
	private Integer _step = 1;
	private Boolean _useTabindexAutoIncrement;
	
}