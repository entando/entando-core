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
package org.entando.entando.aps.internalservlet.system.dispatcher;

import com.agiletec.aps.system.RequestContext;
import com.agiletec.aps.system.SystemConstants;
import com.agiletec.aps.util.ApsWebApplicationUtils;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionInvocation;

import freemarker.template.Template;

import java.io.IOException;
import java.io.StringReader;
import java.io.Writer;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.result.ServletDispatcherResult;

import org.entando.entando.aps.system.services.controller.executor.ExecutorBeanContainer;
import org.entando.entando.aps.system.services.guifragment.GuiFragment;
import org.entando.entando.aps.system.services.guifragment.IGuiFragmentManager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Renders a view using a GuiFragment (Entando object) builded by Freemarker.
 * If the fragment isn't available, includes or forwards to a view (usually a jsp) {@link ServletDispatcherResult}
 *
 * <b>This result type takes the following parameters:</b>
 *
 * <!-- START SNIPPET: params -->
 *
 * <ul>
 *
 * <li><b>code (default)</b> - the code of the fragment to process.</li>
 * <li><b>jspLocation</b> - the location to go to after execution (jsp) if the fragment isn't available.</li>
 *
 * </ul>
 *
 * <!-- END SNIPPET: params -->
 *
 * <b>Example:</b>
 *
 * <pre>
 * <!-- START SNIPPET: example -->
 *
 * &lt;result name="success" type="guiFragment"&gt;fooCode&lt;/result&gt;
 *
 * <!-- END SNIPPET: example -->
 * </pre>
 * @author E.Santoboni
 */
public class GuiFragmentResult extends ServletDispatcherResult {

	private static final Logger _logger = LoggerFactory.getLogger(GuiFragmentResult.class);
	
	/** The default parameter */
    public static final String DEFAULT_PARAM = "code";
	
	private Writer _writer;
	
	protected String _code;
	protected String _jspLocation;
	
	public GuiFragmentResult() {
		super();
	}
	
	public GuiFragmentResult(String code) {
		super(code);
		this._code = code;
	}
	
	public GuiFragmentResult(String code, String jspLocation) {
		super(code);
		this._code = code;
		this._jspLocation = jspLocation;
	}
	
	/**
	 * Execute this result, using the specified fragment.
	 * @param code The code of the fragment
	 * @param invocation The invocation
	 */
	@Override
	public void doExecute(String code, ActionInvocation invocation) throws Exception {
		if (null == code) {
			code = conditionalParse(this._code, invocation);
		}
		if (null == code) {
			this.executeDispatcherResult(invocation);
			return;
		}
		ActionContext ctx = invocation.getInvocationContext();
		HttpServletRequest req = (HttpServletRequest) ctx.get(ServletActionContext.HTTP_REQUEST);
		IGuiFragmentManager guiFragmentManager =
				(IGuiFragmentManager) ApsWebApplicationUtils.getBean(SystemConstants.GUI_FRAGMENT_MANAGER, req);
		try {
			GuiFragment guiFragment = guiFragmentManager.getGuiFragment(code);
			String output = (null != guiFragment) ? guiFragment.getCurrentGui() : null;
			if (StringUtils.isBlank(output)) {
				_logger.info("The fragment '{}' is not available - Action '{}' - Namespace '{}'", 
						code, invocation.getProxy().getActionName(), invocation.getProxy().getNamespace());
				boolean execution = this.executeDispatcherResult(invocation);
				if (!execution) {
					output = "The fragment '" + code + "' is not available";
				} else {
					return;
				}
			}
			RequestContext reqCtx = (RequestContext) req.getAttribute(RequestContext.REQCTX);
			ExecutorBeanContainer ebc = (ExecutorBeanContainer) reqCtx.getExtraParam(SystemConstants.EXTRAPAR_EXECUTOR_BEAN_CONTAINER);
			Writer writer = this.getWriter();
			Template template = new Template(code, new StringReader(output), ebc.getConfiguration());
			template.process(ebc.getTemplateModel(), writer);
		} catch (Throwable t) {
			_logger.error("Error processing GuiFragment result!", t);
			throw new RuntimeException("Error processing GuiFragment result!", t);
		}
	}
	
	protected boolean executeDispatcherResult(ActionInvocation invocation) throws Exception {
		String finalLocation = conditionalParse(this._jspLocation, invocation);
		if (null != finalLocation) {
			super.doExecute(finalLocation, invocation);
			return true;
		}
		return false;
	}
	
	public void setWriter(Writer writer) {
		this._writer = writer;
	}
	
	protected Writer getWriter() throws IOException {
		if (_writer != null) {
			return _writer;
		}
		return ServletActionContext.getResponse().getWriter();
	}
	
	public void setCode(String code) {
		this._code = code;
	}
	
	public void setJspLocation(String jspLocation) {
		this._jspLocation = jspLocation;
	}
	
}
