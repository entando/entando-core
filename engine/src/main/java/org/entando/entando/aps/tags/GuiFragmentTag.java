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
package org.entando.entando.aps.tags;

import com.agiletec.aps.system.RequestContext;
import com.agiletec.aps.system.SystemConstants;
import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.aps.util.ApsWebApplicationUtils;

import freemarker.template.Template;

import java.io.ByteArrayOutputStream;
import java.io.OutputStreamWriter;
import java.io.StringReader;
import java.io.Writer;
import javax.servlet.ServletRequest;
import javax.servlet.jsp.JspException;

import org.apache.commons.lang3.StringUtils;

import org.entando.entando.aps.system.services.controller.executor.ExecutorBeanContainer;
import org.entando.entando.aps.system.services.guifragment.GuiFragment;
import org.entando.entando.aps.system.services.guifragment.IGuiFragmentManager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Print a gui fragment output by the given code.
 * @author E.Santoboni
 */
public class GuiFragmentTag extends ExtendedTagSupport {
	
	private static final Logger _logger =  LoggerFactory.getLogger(GuiFragmentTag.class);
	
	@Override
    public int doStartTag() throws JspException {
		ServletRequest request = this.pageContext.getRequest();
		RequestContext reqCtx = (RequestContext) request.getAttribute(RequestContext.REQCTX);
		try {
			Object extractedValue = this.extractFragmentOutput(reqCtx);
            if (null == extractedValue) {
				_logger.info("The fragment '{}' is not available", this.getCode());
				extractedValue = "The fragment '" + this.getCode() + "' is not available";
            }
            if (this.getVar() != null) {
                this.pageContext.setAttribute(this.getVar(), extractedValue);
            } else {
                if (this.getEscapeXml()) {
                    out(this.pageContext, this.getEscapeXml(), extractedValue);
                } else {
                    this.pageContext.getOut().print(extractedValue);
                }
            }
        } catch (Throwable t) {
        	_logger.error("error in doStartTag", t);
            throw new JspException("Error during tag initialization", t);
        }
        return super.doStartTag();
    }
	
	protected String extractFragmentOutput(RequestContext reqCtx) throws ApsSystemException {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		try {
			IGuiFragmentManager guiFragmentManager = (IGuiFragmentManager) ApsWebApplicationUtils.getBean(SystemConstants.GUI_FRAGMENT_MANAGER, this.pageContext);
			GuiFragment guiFragment = guiFragmentManager.getGuiFragment(this.getCode());
			if (null == guiFragment || StringUtils.isBlank(guiFragment.getCurrentGui())) {
				return null;
			}
			ExecutorBeanContainer ebc = (ExecutorBeanContainer) reqCtx.getExtraParam(SystemConstants.EXTRAPAR_EXECUTOR_BEAN_CONTAINER);
			Writer out = new OutputStreamWriter(baos);
			Template template = new Template(this.getCode(), new StringReader(guiFragment.getCurrentGui()), ebc.getConfiguration());
			template.process(ebc.getTemplateModel(), out);
			out.flush();
		} catch (Throwable t) {
			String msg = "Error creating fragment output - code '" + this.getCode() + "'";
			_logger.error(msg, t);
			throw new ApsSystemException(msg, t);
		}
		return baos.toString();
	}
	
	@Override
	public int doEndTag() throws JspException {
		this.release();
		return super.doEndTag();
	}
	
	@Override
	public void release() {
		super.release();
		this.setVar(null);
		this.setCode(null);
	}
	
	public String getVar() {
		return _var;
	}
	public void setVar(String var) {
		this._var = var;
	}
	
	public String getCode() {
		return _code;
	}
	public void setCode(String code) {
		this._code = code;
	}
	
	private String _var;
	private String _code;
	
}