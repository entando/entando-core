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
package org.entando.entando.aps.system.services.controller.executor;

import com.agiletec.aps.system.RequestContext;
import com.agiletec.aps.system.SystemConstants;
import com.agiletec.aps.system.services.page.IPage;
import com.agiletec.aps.tags.util.HeadInfoContainer;

import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author E.Santoboni
 */
public class WidgetExecutorService extends AbstractWidgetExecutorService implements ExecutorServiceInterface {
	
	private static final Logger _logger = LoggerFactory.getLogger(WidgetExecutorService.class);
	
	@Override
	public void afterPropertiesSet() throws Exception {
		//nothing to do
	}
	
	@Override
	public void service(RequestContext reqCtx) {
		try {
			reqCtx.addExtraParam(SystemConstants.EXTRAPAR_HEAD_INFO_CONTAINER, new HeadInfoContainer());
			IPage page = (IPage) reqCtx.getExtraParam(SystemConstants.EXTRAPAR_CURRENT_PAGE);
			String[] widgetOutput = new String[page.getWidgets().length];
			reqCtx.addExtraParam("ShowletOutput", widgetOutput);
			this.buildWidgetsOutput(reqCtx, page, widgetOutput);
			String redirect = (String) reqCtx.getExtraParam(SystemConstants.EXTRAPAR_EXTERNAL_REDIRECT);
			if (null != redirect) {
				HttpServletResponse response = (HttpServletResponse) reqCtx.getResponse();
				response.sendRedirect(redirect);
				return;
			}
		} catch (Throwable t) {
			String msg = "Error detected during widget preprocessing";
			_logger.error(msg, t);
			throw new RuntimeException(msg, t);
		}
		return;
	}
	
}
