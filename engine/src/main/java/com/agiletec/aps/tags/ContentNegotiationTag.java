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

import javax.servlet.jsp.tagext.TagSupport;

/**
 * Performs the Content Negotiation.
 * Checks whether the request Mime-type is accepted by the user Agent, eventually declaring it.
 * If the Mime-Type is not accepted by the User Agent then the default text/html is declared.
 * The given charset is appended to the declaration
 * @deprecated from Entando 4.1.0, moved function into executor service
 * @author William Ghelfi
 */
public class ContentNegotiationTag extends TagSupport {
	/*
	private static final Logger _logger = LoggerFactory.getLogger(ContentNegotiationTag.class);
	
	@Override
	public int doStartTag() throws JspException {
		try {
			boolean isAcceptedMimeType = this.isAcceptedMimeType();
			if (!isAcceptedMimeType) {
				this.setMimeType(ContentNegotiationTag.DEFAULT_MIMETYPE);
			}
		} catch (Throwable t) {
			_logger.error("error in doStartTag", t);
			//ApsSystemUtils.logThrowable(t, this, "doStartTag");
			throw new JspException("Error during tag initialization ", t);
		}
		return super.doStartTag();
	}
	*/
	/*
	 * Declares the correct contentType as resulted from the Content Negotiation
	 * @see javax.servlet.jsp.tagext.Tag#doEndTag()
	 */
	/*
	@Override
	public int doEndTag() throws JspException {
		ServletRequest request = this.pageContext.getRequest();
		RequestContext reqCtx = (RequestContext) request.getAttribute(RequestContext.REQCTX);
		ServletResponse response = this.pageContext.getResponse();
		try {
			IPage page = null;
			if (null != reqCtx) {
				page = (IPage) reqCtx.getExtraParam(SystemConstants.EXTRAPAR_CURRENT_PAGE);
			}
			StringBuilder contentType = new StringBuilder(this.getMimeType());
			contentType.append("; charset=");
			String charset = (null != page && null != page.getCharset() && page.getCharset().trim().length() > 0) ? page.getCharset() : this.getCharset();
			contentType.append(charset);
			response.setContentType(contentType.toString());
		} catch (Throwable t) {
			_logger.error("Error detected while setting content type", t);
			//ApsSystemUtils.logThrowable(t, this, "doEndTag");
			throw new JspException("Error detected while setting content type", t);
		}
		return EVAL_PAGE;
	}
	
	@Override
	public void release() {
		this._mimeType = null;
		this._charset = null;
	}
	
	public String getMimeType() {
		return _mimeType;
	}
	*/
	public void setMimeType(String mimeType) {
		//this._mimeType = mimeType;
	}
	/*
	public String getCharset() {
		return _charset;
	}
	*/
	public void setCharset(String charset) {
		//this._charset = charset;
	}
	/*
	private boolean isAcceptedMimeType() throws Throwable {
		HttpServletRequest request = (HttpServletRequest) this.pageContext.getRequest();
		RequestContext reqCtx = (RequestContext) request.getAttribute(RequestContext.REQCTX);
		boolean isAcceptedMimeType = false;
		try {
			String header = request.getHeader("accept");
			if (null != header) {
				if (null != reqCtx) {
					IPage page = (IPage) reqCtx.getExtraParam(SystemConstants.EXTRAPAR_CURRENT_PAGE);
					String customMimeType = (null != page.getMimeType() && page.getMimeType().trim().length() > 0) ? page.getMimeType() : null;
					if (null != customMimeType) {
						isAcceptedMimeType = (header.indexOf(page.getMimeType()) >= 0);
						if (isAcceptedMimeType) {
							this.setMimeType(page.getMimeType());
							return true;
						}
					}
				}
				String mimeType = this.getMimeType();
				isAcceptedMimeType = (header.indexOf(mimeType) >= 0);
			}
		} catch (Throwable t) {
			_logger.error("Error detected while verifying mimetype", t);
			//ApsSystemUtils.logThrowable(t, this, "isAcceptedMimeType");
			throw new ApsSystemException("Error detected while verifying mimetype", t);
		}
		return isAcceptedMimeType;
	}
	
	private String _mimeType;
	private String _charset;
	*/
	/*
	 * Default Mime-Type to use if the given one is not accepted by the User Agent.
 	 */
	//private static final String DEFAULT_MIMETYPE = "text/html";
	
}
