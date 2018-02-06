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
package org.entando.entando.aps.system.services.controller.executor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agiletec.aps.system.RequestContext;
import com.agiletec.aps.system.SystemConstants;
import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.aps.system.services.page.IPage;

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
			String pageCharset = page.getCharset();
			String charset = (null != page && null != pageCharset && pageCharset.trim().length() > 0) ? pageCharset : DEFAULT_CHARSET;
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
				String mimeType = page.getMimeType();
				String customMimeType = (null != mimeType && mimeType.trim().length() > 0) ? mimeType : null;
				if (null != customMimeType) {
					boolean isAcceptedMimeType = (header.indexOf(mimeType) >= 0);
					if (isAcceptedMimeType) {
						return mimeType;
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