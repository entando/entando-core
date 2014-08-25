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
package org.entando.entando.aps.servlet;

import com.agiletec.aps.system.RequestContext;
import com.agiletec.aps.system.SystemConstants;
import com.agiletec.aps.system.services.controller.ControllerManager;
import com.agiletec.aps.util.ApsWebApplicationUtils;
import freemarker.ext.jsp.TaglibFactory;
import freemarker.ext.servlet.AllHttpScopesHashModel;
import freemarker.ext.servlet.ServletContextHashModel;

import freemarker.template.Configuration;
import freemarker.template.DefaultObjectWrapper;
import freemarker.template.ObjectWrapper;
import freemarker.template.TemplateExceptionHandler;
import freemarker.template.TemplateModel;
import freemarker.template.TemplateModelException;

import java.io.IOException;
import java.util.List;
import javax.servlet.ServletContext;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.entando.entando.aps.system.services.controller.executor.ExecutorBeanContainer;
import org.entando.entando.aps.system.services.controller.executor.ExecutorServiceInterface;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Servlet di controllo, punto di ingresso per le richieste di pagine del portale.
 * Predispone il contesto di richiesta, invoca il controller e ne gestisce lo stato di uscita.
 * @author  
 */
public class ControllerServlet extends freemarker.ext.servlet.FreemarkerServlet {
	
	private static final Logger _logger = LoggerFactory.getLogger(ControllerServlet.class);
	
	@Override
    protected void service(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        RequestContext reqCtx = new RequestContext();
        _logger.debug("Request:" + request.getServletPath());
        request.setAttribute(RequestContext.REQCTX, reqCtx);
        reqCtx.setRequest(request);
        reqCtx.setResponse(response);
		ControllerManager controller = 
				(ControllerManager) ApsWebApplicationUtils.getBean(SystemConstants.CONTROLLER_MANAGER, request);
        int status = controller.service(reqCtx);
        if (status == ControllerManager.REDIRECT) {
        	_logger.debug("Redirection");
            this.redirect(reqCtx, response);
        } else if (status == ControllerManager.OUTPUT) {
        	_logger.debug("Output");
			try {
				Configuration config = new Configuration();
				DefaultObjectWrapper wrapper = new DefaultObjectWrapper();
				config.setTemplateExceptionHandler(TemplateExceptionHandler.HTML_DEBUG_HANDLER);
				config.setObjectWrapper(wrapper);
				config.setTemplateExceptionHandler(TemplateExceptionHandler.DEBUG_HANDLER);
				TemplateModel templateModel = this.createModel(wrapper, this.getServletContext(), request, response);
				ExecutorBeanContainer ebc = new ExecutorBeanContainer(config, templateModel);
				reqCtx.addExtraParam(SystemConstants.EXTRAPAR_EXECUTOR_BEAN_CONTAINER, ebc);
				List<ExecutorServiceInterface> executors = 
						(List<ExecutorServiceInterface>) ApsWebApplicationUtils.getBean("ExecutorServices", request);
				for (int i = 0; i < executors.size(); i++) {
					ExecutorServiceInterface executor = executors.get(i);
					executor.service(reqCtx);
				}
			} catch (Throwable t) {
				_logger.error("Error building response", t);
				throw new ServletException("Error building response", t);
			}
        } else if (status == ControllerManager.ERROR) {
			_logger.debug("Error");
            this.outputError(reqCtx, response);
        } else {
        	_logger.error("Error: final status = {} - request: {}", ControllerManager.getStatusDescription(status),request.getServletPath());
            throw new ServletException("Service not available");
        }
        return;
    }
	
	@Override
	protected TemplateModel createModel(ObjectWrapper wrapper, ServletContext servletContext, 
			HttpServletRequest request, HttpServletResponse response) throws TemplateModelException {
		TemplateModel template = super.createModel(wrapper, servletContext, request, response);
		if (template instanceof AllHttpScopesHashModel) {
			AllHttpScopesHashModel hashModel = ((AllHttpScopesHashModel) template);
			ServletContextHashModel servletContextModel = (ServletContextHashModel) hashModel.get(KEY_APPLICATION);
			if (null == servletContextModel.getServlet()) {
				ServletContextHashModel newServletContextModel = new ServletContextHashModel(this, wrapper);
				servletContextModel = new ServletContextHashModel(this, wrapper);
				servletContext.setAttribute(ATTR_APPLICATION_MODEL, servletContextModel);
				TaglibFactory taglibs = new TaglibFactory(servletContext);
				servletContext.setAttribute(ATTR_JSP_TAGLIBS_MODEL, taglibs);
				hashModel.putUnlistedModel(KEY_APPLICATION, newServletContextModel);
				hashModel.putUnlistedModel(KEY_APPLICATION_PRIVATE, newServletContextModel);
			}
		}
		return template;
	}
	
    private void redirect(RequestContext reqCtx, HttpServletResponse response) throws ServletException {
        try {
            String url = (String) reqCtx.getExtraParam(RequestContext.EXTRAPAR_REDIRECT_URL);
            response.sendRedirect(url);
        } catch (Exception e) {
            throw new ServletException("Service not available", e);
        }
    }
    
    private void outputError(RequestContext reqCtx, HttpServletResponse response)
            throws ServletException {
        try {
            if (!response.isCommitted()) {
                Integer httpErrorCode = (Integer) reqCtx.getExtraParam("errorCode");
                if (httpErrorCode == null) {
                    httpErrorCode = new Integer(HttpServletResponse.SC_SERVICE_UNAVAILABLE);
                }
                response.sendError(httpErrorCode.intValue());
            }
        } catch (IOException e) {
        	_logger.error("outputError", e);
            throw new ServletException("Service not available");
        }
    }
    
	private static final String ATTR_APPLICATION_MODEL = ".freemarker.Application";
	private static final String ATTR_JSP_TAGLIBS_MODEL = ".freemarker.JspTaglibs";
	
}
