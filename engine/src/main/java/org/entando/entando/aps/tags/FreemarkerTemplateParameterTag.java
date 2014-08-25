/*
*
* Copyright 2013 Entando S.r.l. (http://www.entando.com) All rights reserved.
*
* This file is part of Entando software.
* Entando is a free software; 
* you can redistribute it and/or modify it
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
package org.entando.entando.aps.tags;

import javax.servlet.ServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

import com.agiletec.aps.system.RequestContext;
import com.agiletec.aps.system.SystemConstants;

import freemarker.core.Environment;
import freemarker.ext.beans.StringModel;
import freemarker.ext.servlet.AllHttpScopesHashModel;
import freemarker.template.TemplateModel;

import org.entando.entando.aps.system.services.controller.executor.ExecutorBeanContainer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Add a parameter into the Freemarker's TemplateModel Map
 * @author E.Santoboni
 */
public class FreemarkerTemplateParameterTag extends TagSupport {
	
	private static final Logger _logger =  LoggerFactory.getLogger(FreemarkerTemplateParameterTag.class);
	
	@Override
    public int doStartTag() throws JspException {
		ServletRequest request = this.pageContext.getRequest();
		RequestContext reqCtx = (RequestContext) request.getAttribute(RequestContext.REQCTX);
		try {
			ExecutorBeanContainer ebc = (ExecutorBeanContainer) reqCtx.getExtraParam(SystemConstants.EXTRAPAR_EXECUTOR_BEAN_CONTAINER);
			TemplateModel templateModel = ebc.getTemplateModel();
			if (!(templateModel instanceof AllHttpScopesHashModel)) {
				return EVAL_BODY_INCLUDE;
			}
			AllHttpScopesHashModel hashModel = (AllHttpScopesHashModel) templateModel;
			Object object = this.pageContext.getAttribute(this.getValueName());
			if (null == object) {
				Environment environment = Environment.getCurrentEnvironment();
				if (null != environment) {
					Object wrapper = environment.getVariable(this.getValueName());
					if (null != wrapper) {
						if (wrapper instanceof StringModel) {
							object = ((StringModel) wrapper).getWrappedObject();
						} else {
							object = wrapper;
						}
					}
				}
			}
			if (null != object) {
				hashModel.put(this.getVar(), object);
			}
        } catch (Throwable t) {
        	_logger.error("error in doStartTag", t);
            throw new JspException("Error during tag initialization", t);
        }
        return EVAL_BODY_INCLUDE;
    }
	
	@Override
	public int doEndTag() throws JspException {
		if (this.isRemoveOnEndTag()) {
			ServletRequest request = this.pageContext.getRequest();
			RequestContext reqCtx = (RequestContext) request.getAttribute(RequestContext.REQCTX);
			try {
				ExecutorBeanContainer ebc = (ExecutorBeanContainer) reqCtx.getExtraParam(SystemConstants.EXTRAPAR_EXECUTOR_BEAN_CONTAINER);
				TemplateModel templateModel = ebc.getTemplateModel();
				if (templateModel instanceof AllHttpScopesHashModel) {
					AllHttpScopesHashModel hashModel = (AllHttpScopesHashModel) templateModel;
					hashModel.remove(this.getVar());
				}
			} catch (Throwable t) {
				_logger.error("error in doEndTag", t);
				throw new JspException("Error evaluating di end tag", t);
			}
		}
		this.release();
		return EVAL_PAGE;
	}
	
	@Override
	public void release() {
		this.setVar(null);
		this.setValueName(null);
		this.setRemoveOnEndTag(false);
	}
	
	public String getVar() {
		return _var;
	}
	public void setVar(String var) {
		this._var = var;
	}
	
	public String getValueName() {
		return valueName;
	}
	public void setValueName(String valueName) {
		this.valueName = valueName;
	}
	
	public boolean isRemoveOnEndTag() {
		return _removeOnEndTag;
	}
	public void setRemoveOnEndTag(boolean removeOnEndTag) {
		this._removeOnEndTag = removeOnEndTag;
	}
	
	private String _var;
	private String valueName;
	private boolean _removeOnEndTag = false;
	
}