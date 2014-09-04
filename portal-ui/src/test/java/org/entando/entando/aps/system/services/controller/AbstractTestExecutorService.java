/*
*
* Copyright 2014 Entando S.r.l. (http://www.entando.com) All rights reserved.
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
* Copyright 2014 Entando S.r.l. (http://www.entando.com) All rights reserved.
*
*/
package org.entando.entando.aps.system.services.controller;

import com.agiletec.aps.BaseTestCase;
import com.agiletec.aps.system.SystemConstants;

import freemarker.ext.jsp.TaglibFactory;
import freemarker.ext.servlet.AllHttpScopesHashModel;
import freemarker.ext.servlet.FreemarkerServlet;
import freemarker.ext.servlet.HttpRequestHashModel;
import freemarker.ext.servlet.ServletContextHashModel;
import freemarker.template.Configuration;
import freemarker.template.DefaultObjectWrapper;
import freemarker.template.ObjectWrapper;
import freemarker.template.TemplateExceptionHandler;
import freemarker.template.TemplateModel;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.entando.entando.aps.servlet.ControllerServlet;
import org.entando.entando.aps.system.services.controller.executor.ExecutorBeanContainer;

import org.springframework.mock.web.MockServletConfig;

/**
 * @author E.Santoboni
 */
public class AbstractTestExecutorService extends BaseTestCase {
	
	@Override
	protected void setUp() throws Exception {
		super.setUp();
		try {
			Configuration config = new Configuration();
			DefaultObjectWrapper wrapper = new DefaultObjectWrapper();
			config.setTemplateExceptionHandler(TemplateExceptionHandler.HTML_DEBUG_HANDLER);
			config.setObjectWrapper(wrapper);
			config.setTemplateExceptionHandler(TemplateExceptionHandler.DEBUG_HANDLER);
			TemplateModel templateModel = this.createModel(wrapper);
			ExecutorBeanContainer ebc = new ExecutorBeanContainer(config, templateModel);
			super.getRequestContext().addExtraParam(SystemConstants.EXTRAPAR_EXECUTOR_BEAN_CONTAINER, ebc);
		} catch (Throwable t) {
			throw new Exception(t);
		}
	}
	
	protected TemplateModel createModel(ObjectWrapper wrapper) throws Throwable {
		HttpServletRequest request = super.getRequestContext().getRequest();
		HttpServletResponse response = super.getRequestContext().getResponse();
		ServletContext servletContext = request.getSession().getServletContext();
		AllHttpScopesHashModel hashModel = new AllHttpScopesHashModel(wrapper, servletContext, request); //super.createModel(wrapper, servletContext, request, response);
		ControllerServlet servlet = new ControllerServlet();
		MockServletConfig config = new MockServletConfig(servletContext);
		servlet.init(config);
		ServletContextHashModel newServletContextModel = new ServletContextHashModel(servlet, wrapper);
		ServletContextHashModel servletContextModel = new ServletContextHashModel(servlet, wrapper);
		servletContext.setAttribute(ATTR_APPLICATION_MODEL, servletContextModel);
		TaglibFactory taglibs = new TaglibFactory(servletContext);
		servletContext.setAttribute(ATTR_JSP_TAGLIBS_MODEL, taglibs);
		hashModel.putUnlistedModel(FreemarkerServlet.KEY_APPLICATION, newServletContextModel);
		hashModel.putUnlistedModel(FreemarkerServlet.KEY_APPLICATION_PRIVATE, newServletContextModel);
		hashModel.putUnlistedModel(FreemarkerServlet.KEY_JSP_TAGLIBS, taglibs);
		HttpRequestHashModel requestModel = new HttpRequestHashModel(request, response, wrapper);
        request.setAttribute(ATTR_REQUEST_MODEL, requestModel);
		hashModel.putUnlistedModel(FreemarkerServlet.KEY_REQUEST_PRIVATE, requestModel);
		return hashModel;
	}
	
	private static final String ATTR_APPLICATION_MODEL = ".freemarker.Application";
	private static final String ATTR_JSP_TAGLIBS_MODEL = ".freemarker.JspTaglibs";
	private static final String ATTR_REQUEST_MODEL = ".freemarker.Request";
	
}
