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
package com.agiletec.plugins.jacms.aps.tags;

import java.util.List;

import javax.servlet.ServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agiletec.aps.system.RequestContext;
import com.agiletec.plugins.jacms.aps.tags.util.SearcherTagHelper;

/**
 * Generates a list of content IDs, restricting the result to the key word 
 * contained in the "search" parameter of the HTTP request
 * @author E.Santoboni
 */
public class SearcherTag extends TagSupport {

	private static final Logger _logger = LoggerFactory.getLogger(SearcherTag.class);
	
	@Override
	public int doStartTag() throws JspException {
		ServletRequest request =  this.pageContext.getRequest();
		RequestContext reqCtx = (RequestContext) request.getAttribute(RequestContext.REQCTX);
		try {
			String word = request.getParameter("search");
			SearcherTagHelper helper = new SearcherTagHelper();
			List<String> result = helper.executeSearch(word, reqCtx);
			this.pageContext.setAttribute(this.getListName(), result);
			request.setAttribute("search", word);
		} catch (Throwable t) {
			_logger.error("error in do start tag", t);
			//ApsSystemUtils.logThrowable(e, this, "doStartTag");
			throw new JspException("Error detected while initialising the tag", t);
		}
		return SKIP_BODY;
	}
	
	/**
	 * @return Returns the listName.
	 */
	public String getListName() {
		return _listName;
	}
	
	/**
	 * @param listName The listName to set.
	 */
	public void setListName(String listName) {
		this._listName = listName;
	}
	
	private String _listName;

}
