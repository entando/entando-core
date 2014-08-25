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
package com.agiletec.apsadmin.tags.form;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.jsp.JspException;

import org.apache.struts2.components.Component;

import com.agiletec.apsadmin.tags.util.AutoIndexingTagHelper;
import com.agiletec.apsadmin.tags.util.IAutoIndexingTag;
import com.agiletec.apsadmin.tags.util.RadioMap;
import com.opensymphony.xwork2.util.ValueStack;

/**
 * This class implements the radio button tag which inherits some of the Struts2 radio functionalities. 
 * @author M.Minnai - E.Santoboni
 */
public class RadioTag extends org.apache.struts2.views.jsp.ui.AbstractUITag implements IAutoIndexingTag {
	
	@Override
	public int doStartTag() throws JspException {
		String currentCounter = this.getCurrentIndex();
		if (null != currentCounter) {
			this.setTabindex(currentCounter);
		}
		return super.doStartTag();
	}
	
	@Override
    public Component getBean(ValueStack stack, HttpServletRequest req, HttpServletResponse res) {
		return new RadioMap(stack, req, res, this.getChecked(), this.getCheckedVar());
    }
	
	public void setChecked(String checked) {
		this._checked = checked;
	}
	
	public String getChecked() {
		return _checked;
	}
	
	public void setCheckedVar(String checkedVar) {
		this._checkedVar = checkedVar;
	}
	
	public String getCheckedVar() {
		return _checkedVar;
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
	
	/**
	 * This toggles the checked condition of the radio button.
	 */
	private String _checked;
	
	/**
	 * This is the name of the context parameter which holds the value to check against the 'value' attribute.
	 */
	private String _checkedVar;
	
	private Integer _step = 1;
	private Boolean _useTabindexAutoIncrement;
	
}