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
package org.entando.entando.aps.internalservlet.system.dispatcher;

import java.io.IOException;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.dispatcher.Dispatcher;
import org.apache.struts2.dispatcher.mapper.ActionMapper;
import org.apache.struts2.dispatcher.mapper.ActionMapping;
import org.apache.struts2.dispatcher.ng.ExecuteOperations;
import org.apache.struts2.dispatcher.ng.servlet.ServletHostConfig;

import com.agiletec.aps.system.RequestContext;
import com.agiletec.aps.system.SystemConstants;
import com.agiletec.aps.tags.InternalServletTag;

import com.agiletec.apsadmin.system.dispatcher.EntandoActionUtils;
import com.agiletec.apsadmin.system.dispatcher.InitOperations;
import com.agiletec.apsadmin.system.dispatcher.PrepareOperations;

import org.entando.entando.aps.internalservlet.system.dispatcher.mapper.ExtendedDefaultActionMapper;

/**
 * Servlet a servizio del widget "Internal Servlet".
 * La servlet ha lo scopo di permettere la erogazione nel Front-End di Entando (all'interno dei frames di pagine) 
 * di funzionalità implementate su base Struts2.
 * @author E.Santoboni
 */
public class Struts2ServletDispatcher extends HttpServlet {
	
    private PrepareOperations prepare;
    private ExecuteOperations execute;
	
    @Override
    public void init(ServletConfig filterConfig) throws ServletException {
        InitOperations init = new InitOperations();
        Dispatcher dispatcher = null;
        try {
            ServletHostConfig config = new ServletHostConfig(filterConfig);
            init.initLogging(config);
            dispatcher = init.initDispatcher(config);
            init.initStaticContentLoader(config, dispatcher);
            prepare = new PrepareOperations(filterConfig.getServletContext(), dispatcher);
            execute = new ExecuteOperations(filterConfig.getServletContext(), dispatcher);
        } finally {
            if (dispatcher != null) {
                dispatcher.cleanUpAfterInit();
            }
            init.cleanup();
        }
    }
	
    @Override
    public void service(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        try {
			RequestContext reqCtx = (RequestContext) request.getAttribute(RequestContext.REQCTX);
			String currentFrameActionPath = request.getParameter(InternalServletTag.REQUEST_PARAM_FRAMEDEST);
			Integer currentFrame = (Integer) reqCtx.getExtraParam(SystemConstants.EXTRAPAR_CURRENT_FRAME);
			Boolean staticAction = (Boolean) reqCtx.getExtraParam(InternalServletTag.EXTRAPAR_STATIC_ACTION);
			
			boolean executeCustomAction = (null == staticAction || !staticAction) 
					&& (null == currentFrameActionPath || Integer.parseInt(currentFrameActionPath) == currentFrame.intValue());
			
            prepare.createActionContext(request, response);
            prepare.assignDispatcherToThread();
            prepare.setEncodingAndLocale(request, response);
            request = prepare.wrapRequest(request);
			ActionMapper actionMapper = new ExtendedDefaultActionMapper();
			Dispatcher dispatcher = prepare.getDispatcher();
			String entandoActionName = EntandoActionUtils.extractEntandoActionName(request);
			ActionMapping mapping = actionMapper.getMapping(request, dispatcher.getConfigurationManager());
			if (mapping != null && null != entandoActionName && executeCustomAction) {
				mapping.setName(entandoActionName);
			}
			if (mapping == null) {
                boolean handled = execute.executeStaticResourceRequest(request, response);
                if (!handled) {
					throw new ServletException("Resource loading not supported, use the StrutsPrepareAndExecuteFilter instead.");
				}
            } else {
                execute.executeAction(request, response, mapping);
            }
        } finally {
            prepare.cleanupRequest(request);
        }
    }
	
    @Override
    public void destroy() {
        prepare.cleanupDispatcher();
    }
	
}
