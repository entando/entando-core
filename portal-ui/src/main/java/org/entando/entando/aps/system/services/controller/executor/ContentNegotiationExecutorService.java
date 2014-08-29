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
package org.entando.entando.aps.system.services.controller.executor;

import com.agiletec.aps.system.RequestContext;
import com.agiletec.aps.system.SystemConstants;
import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.aps.system.services.page.IPage;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Performs the Content Negotiation.
 * Checks whether the request Mime-type is accepted by the user Agent, eventually declaring it.
 * If the Mime-Type is not accepted by the User Agent then the default text/html is declared.
 * The given charset is appended to the declaration
 * @author W.Ghelfi - E.Santoboni
 */
public class ContentNegotiationExecutorService implements ExecutorServiceInterface {
	
	private static final Logger _logger = LoggerFactory.getLogger(ContentNegotiationExecutorService.class);
	
	@Override
	public void afterPropertiesSet() throws Exception {
		//nothing to do
	}
	
	@Override
	public void service(RequestContext reqCtx) {
		HttpServletResponse response = reqCtx.getResponse();
		try {
			String mimetype = this.extractMimeType(reqCtx);
			IPage page = (IPage) reqCtx.getExtraParam(SystemConstants.EXTRAPAR_CURRENT_PAGE);
			StringBuilder contentType = new StringBuilder(mimetype);
			contentType.append("; charset=");
			String charset = (null != page && null != page.getCharset() && page.getCharset().trim().length() > 0) ? page.getCharset() : DEFAULT_CHARSET;
			contentType.append(charset);
			response.setContentType(contentType.toString());
		} catch (Throwable t) {
			_logger.error("Error detected while setting content type", t);
			throw new RuntimeException("Error detected while setting content type", t);
		}
	}
	
	private String extractMimeType(RequestContext reqCtx) throws Throwable {
		HttpServletRequest request = reqCtx.getRequest();
		try {
			String header = request.getHeader("accept");
			if (null != header) {
				IPage page = (IPage) reqCtx.getExtraParam(SystemConstants.EXTRAPAR_CURRENT_PAGE);
				String customMimeType = (null != page.getMimeType() && page.getMimeType().trim().length() > 0) ? page.getMimeType() : null;
				if (null != customMimeType) {
					boolean isAcceptedMimeType = (header.indexOf(page.getMimeType()) >= 0);
					if (isAcceptedMimeType) {
						return page.getMimeType();
					}
				}
			}
		} catch (Throwable t) {
			_logger.error("Error detected while verifying mimetype", t);
			throw new ApsSystemException("Error detected while verifying mimetype", t);
		}
		return DEFAULT_MIMETYPE;
	}
	
	private static final String DEFAULT_MIMETYPE = "text/html";
	private static final String DEFAULT_CHARSET = "UTF-8";
	
}