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
package com.agiletec.aps.tags;

import java.io.CharArrayWriter;
import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agiletec.aps.system.RequestContext;
import com.agiletec.aps.system.SystemConstants;
import com.agiletec.aps.system.services.page.IPage;
import com.agiletec.aps.system.services.page.Widget;
import com.agiletec.aps.util.ApsProperties;

/**
 * Tag for widget "InternalServlet".
 * Publish a function erogated throw a internal Servlet; the servlet is invoked by a path specificated 
 * by the tag attribute "actionPath" or by the widget parameter of the same name.
 * @author M.Casari - E.Santoboni
 */
public class InternalServletTag extends TagSupport {
	
	private static final Logger _logger = LoggerFactory.getLogger(InternalServletTag.class);
	
	/**
	 * Internal class that wrappers the response, extending the
	 * javax.servlet.http.HttpServletResponseWrapper class to
	 * define a proprietary output channel.
	 * It is used to retrieve the content response after having
	 * made an 'include' in the RequestDispatcher 
	 */
	public class ResponseWrapper extends HttpServletResponseWrapper {
		
		public ResponseWrapper(HttpServletResponse response) {
			super(response);
			_output = new CharArrayWriter();
		}
		
		@Override
		public PrintWriter getWriter() {
			return new PrintWriter(_output);
		}
		
		@Override
		public void sendRedirect(String path) throws IOException {
			this._redirectPath = path;
		}
		
		@Override
		public void addCookie(Cookie cookie) {
			super.addCookie(cookie);
			int len = (null == this._cookiesToAdd) ? 0 : this._cookiesToAdd.length;
			Cookie[] newCookiesToAdd = new Cookie[len + 1];
			if (null != this._cookiesToAdd) {
				for (int i=0; i < len; i++) {
					newCookiesToAdd[i] = this._cookiesToAdd[i];
				}
			}
			newCookiesToAdd[len] = cookie;
			this._cookiesToAdd = newCookiesToAdd;
		}
		
		protected Cookie[] getCookiesToAdd() {
			return _cookiesToAdd;
		}
		
		public boolean isRedirected() {
			return (_redirectPath != null);
		}
		
		public String getRedirectPath() {
			return _redirectPath;
		}
		
		@Override
		public String toString() {
			return _output.toString();
		}
		
		private String _redirectPath;
		private CharArrayWriter _output;
		
		private Cookie[] _cookiesToAdd;
		
	}
	
	/**
	 * Invokes the widget configured in the current page.
	 * @throws JspException in case of error that occurred in both this method 
	 * or in one of the included JSPs
	 */
	@Override
	public int doEndTag() throws JspException {
		int result = super.doEndTag();
		ServletRequest req =  this.pageContext.getRequest();
		RequestContext reqCtx = (RequestContext) req.getAttribute(RequestContext.REQCTX);
		try {
			IPage page = (IPage) reqCtx.getExtraParam(SystemConstants.EXTRAPAR_CURRENT_PAGE);
			ResponseWrapper responseWrapper = new ResponseWrapper((HttpServletResponse)this.pageContext.getResponse());
			String output = this.buildWidgetOutput(page, responseWrapper);
			if (responseWrapper.isRedirected()) {
				String redirect = responseWrapper.getRedirectPath();
				reqCtx.addExtraParam(SystemConstants.EXTRAPAR_EXTERNAL_REDIRECT, redirect);
				result = SKIP_PAGE;
			} else {
				this.pageContext.getOut().print(output);
			}
		} catch (Throwable t) {
			_logger.error("Error in widget preprocessing", t);
			String msg = "Error in widget preprocessing";
			throw new JspException(msg, t);
		}
		return result;
	}
	
	protected String buildWidgetOutput(IPage page, ResponseWrapper responseWrapper) throws JspException {
		String output = null;
		ServletRequest req =  this.pageContext.getRequest();
		RequestContext reqCtx = (RequestContext) req.getAttribute(RequestContext.REQCTX);
		try {
			Widget widget = (Widget) reqCtx.getExtraParam(SystemConstants.EXTRAPAR_CURRENT_WIDGET);
			this.includeWidget(reqCtx, responseWrapper, widget);
			Cookie[] cookies = responseWrapper.getCookiesToAdd();
			if (null != cookies) {
				for (int i = 0; i < cookies.length; i++) {
					Cookie cookie = cookies[i];
					reqCtx.getResponse().addCookie(cookie);
				}
			}
			output = responseWrapper.toString();
			responseWrapper.getWriter().close();
		} catch (Throwable t) {
			String msg = "Error building widget output";
			throw new JspException(msg, t);
		}
		return output;
	}
	
	protected void includeWidget(RequestContext reqCtx, ResponseWrapper responseWrapper, Widget widget) throws ServletException, IOException {
		HttpServletRequest request = reqCtx.getRequest();
		try {
			String actionPath = this.extractIntroActionPath(reqCtx, widget);
			if (!this.isStaticAction()) {
				String requestActionPath = request.getParameter(REQUEST_PARAM_ACTIONPATH);
				String currentFrameActionPath = request.getParameter(REQUEST_PARAM_FRAMEDEST);
				Integer currentFrame = (Integer) reqCtx.getExtraParam(SystemConstants.EXTRAPAR_CURRENT_FRAME);
				if (requestActionPath != null && currentFrameActionPath != null && currentFrame.toString().equals(currentFrameActionPath)) {
					if (this.isAllowedRequestPath(requestActionPath) && !this.isRecursivePath(requestActionPath, request)) {
						actionPath = requestActionPath;
					}
				}
			}
			reqCtx.addExtraParam(EXTRAPAR_STATIC_ACTION, this.isStaticAction());
			RequestDispatcher requestDispatcher = request.getRequestDispatcher(actionPath);
			requestDispatcher.include(request, responseWrapper);
		} catch (Throwable t) {
			_logger.error("Error including widget", t);
			RequestDispatcher requestDispatcher = request.getRequestDispatcher("/WEB-INF/aps/jsp/system/internalServlet_error.jsp");
			requestDispatcher.include(request, responseWrapper);
		}
	}
	
	protected boolean isAllowedRequestPath(String requestActionPath) {
		String rapLowerCase = requestActionPath.toLowerCase();
		if (rapLowerCase.contains("web-inf") 
				|| rapLowerCase.contains("meta-inf") 
				|| rapLowerCase.contains("../") 
				|| rapLowerCase.contains("%2e%2e%2f") 
				|| rapLowerCase.endsWith(".txt") 
				|| rapLowerCase.contains("<") 
				|| rapLowerCase.endsWith("%3c") 
				|| rapLowerCase.endsWith("%00") 
				|| rapLowerCase.endsWith("'") 
				|| rapLowerCase.endsWith("\"")) {
			return false;
		}
		return true;
	}
	
	protected boolean isRecursivePath(String requestActionPath, HttpServletRequest request) {
		String contextPath = request.getContextPath();
		if (!requestActionPath.contains(contextPath)) {
			return false;
		}
		String prefix = contextPath + "/pages/";
		return (requestActionPath.contains(".wp") || requestActionPath.contains(".page") || requestActionPath.contains(prefix));
	}
	
	/**
	 * Extract the init Action Path. 
	 * Return the tag attribute (if set), else the widget parameter.
	 * @param reqCtx The request context.
	 * @param widget The current widget.
	 * @return The init Action Path
	 */
	protected String extractIntroActionPath(RequestContext reqCtx, Widget widget) {
		String actionPath = this.getActionPath();
		if (null == this.getActionPath()) {
			ApsProperties config = widget.getConfig();
			if (widget.getType().isLogic()) {
				config = widget.getType().getConfig();
			}
			if (null != config) {
				actionPath = config.getProperty(CONFIG_PARAM_ACTIONPATH);
			}
		}
		if (null == actionPath || actionPath.trim().length() == 0) {
			IPage page = (IPage) reqCtx.getExtraParam(SystemConstants.EXTRAPAR_CURRENT_PAGE);
			throw new RuntimeException("Null init action path : page " + page.getCode());
		}
		return actionPath;
	}
	
	@Override
	public void release() {
		super.release();
		this.setActionPath(null);
		this.setStaticAction(false);
	}
	
	public String getActionPath() {
		return _actionPath;
	}
	public void setActionPath(String actionPath) {
		this._actionPath = actionPath;
	}
	
	public boolean isStaticAction() {
		return _staticAction;
	}
	public void setStaticAction(boolean staticAction) {
		this._staticAction = staticAction;
	}
	
	private String _actionPath;
	private boolean _staticAction;
	
	public static final String CONFIG_PARAM_ACTIONPATH = "actionPath";
	public static final String REQUEST_PARAM_ACTIONPATH = "internalServletActionPath";
	public static final String REQUEST_PARAM_FRAMEDEST = "internalServletFrameDest";
	
	public static final String EXTRAPAR_STATIC_ACTION = "internalServletStaticAction";
	
}