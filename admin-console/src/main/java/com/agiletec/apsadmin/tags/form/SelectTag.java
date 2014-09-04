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

import javax.servlet.jsp.JspException;

import com.agiletec.apsadmin.tags.util.AutoIndexingTagHelper;
import com.agiletec.apsadmin.tags.util.IAutoIndexingTag;

/**
 * This class extends the org.apache.struts2.views.jsp.ui.SelectTag 
 * in order to handle auto-incrementing Tabindex 
 * @author E.Santoboni
 * @deprecated Use default struts2 s:select tag
 */
public class SelectTag extends org.apache.struts2.views.jsp.ui.SelectTag implements IAutoIndexingTag {
	
	@Override
	public int doStartTag() throws JspException {
		String currentCounter = this.getCurrentIndex();
		if (null != currentCounter) {
			this.setTabindex(currentCounter);
		}
		return super.doStartTag();
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