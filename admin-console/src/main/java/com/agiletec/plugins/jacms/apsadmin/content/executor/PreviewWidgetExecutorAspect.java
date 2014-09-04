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
package com.agiletec.plugins.jacms.apsadmin.content.executor;

import com.agiletec.aps.system.RequestContext;
import com.agiletec.aps.system.SystemConstants;
import com.agiletec.aps.system.services.page.IPage;
import com.agiletec.aps.system.services.page.Widget;
import com.agiletec.plugins.jacms.aps.system.services.content.model.Content;
import com.agiletec.plugins.jacms.apsadmin.content.ContentActionConstants;

import freemarker.template.Configuration;
import freemarker.template.TemplateModel;

import javax.servlet.http.HttpServletRequest;

import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;

import org.entando.entando.aps.system.services.controller.executor.WidgetExecutorService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author E.Santoboni
 */
@Aspect
public class PreviewWidgetExecutorAspect extends WidgetExecutorService {
	
	private static final Logger _logger = LoggerFactory.getLogger(PreviewWidgetExecutorAspect.class);
	
	@After("execution(* org.entando.entando.aps.system.services.controller.executor.WidgetExecutorService.service(..)) && args(freemarkerConfig, templateModel, reqCtx)")
	public void checkContentPreview(Configuration freemarkerConfig, TemplateModel templateModel, RequestContext reqCtx) {
		HttpServletRequest request = reqCtx.getRequest();
		String contentOnSessionMarker = (String) request.getAttribute("contentOnSessionMarker");
		if (null == contentOnSessionMarker || contentOnSessionMarker.trim().length() == 0) {
			contentOnSessionMarker = request.getParameter("contentOnSessionMarker");
		}
		if (null == contentOnSessionMarker) {
			return;
		}
		Content contentOnSession = (Content) request.getSession()
				.getAttribute(ContentActionConstants.SESSION_PARAM_NAME_CURRENT_CONTENT_PREXIX + contentOnSessionMarker);
		if (null == contentOnSession) {
			return;
		}
		try {
			IPage currentPage = (IPage) reqCtx.getExtraParam(SystemConstants.EXTRAPAR_CURRENT_PAGE);
			Widget[] widgets = currentPage.getWidgets();
			for (int frame = 0; frame < widgets.length; frame++) {
				Widget widget = widgets[frame];
				if (widget != null && "viewerConfig".equals(widget.getType().getAction())) {
					if ((currentPage.getCode().equals(contentOnSession.getViewPage()) && (widget.getConfig() == null || widget.getConfig().size() == 0)) 
							|| (widget.getConfig() != null && widget.getConfig().get("contentId") != null && widget.getConfig().get("contentId").equals(contentOnSession.getId()))) {
						reqCtx.addExtraParam(SystemConstants.EXTRAPAR_CURRENT_WIDGET, widget);
						reqCtx.addExtraParam(SystemConstants.EXTRAPAR_CURRENT_FRAME, new Integer(frame));
						String output = super.extractJspOutput(reqCtx, CONTENT_VIEWER_JSP);
						String[] widgetOutput = (String[]) reqCtx.getExtraParam("ShowletOutput");
						widgetOutput[frame] = output; 
						return;
					}
				}
			}
		} catch (Throwable t) {
			String msg = "Error detected while include content preview";
			_logger.error(msg, t);
			throw new RuntimeException(msg, t);
		}
	}
	
	private final String CONTENT_VIEWER_JSP="/WEB-INF/plugins/jacms/apsadmin/jsp/content/preview/content_viewer.jsp";
	
}
