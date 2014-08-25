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

import javax.servlet.ServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agiletec.aps.system.RequestContext;
import com.agiletec.aps.system.common.entity.helper.IEntityFilterBean;
import com.agiletec.aps.util.ApsWebApplicationUtils;
import com.agiletec.plugins.jacms.aps.system.JacmsSystemConstants;
import com.agiletec.plugins.jacms.aps.system.services.content.widget.IContentListTagBean;
import com.agiletec.plugins.jacms.aps.system.services.content.widget.IContentListWidgetHelper;
import com.agiletec.plugins.jacms.aps.system.services.content.widget.UserFilterOptionBean;

/**
 * ContentListTag" sub-tag, it creates a custom user filter to restrict the result of the content search by front-end user.
 * @author E.Santoboni
 */
public class ContentListUserFilterOptionTag extends TagSupport implements IEntityFilterBean {

	private static final Logger _logger = LoggerFactory.getLogger(ContentListUserFilterOptionTag.class);
	
	public ContentListUserFilterOptionTag() {
		super();
		this.release();
	}
	
	@Override
	public int doEndTag() throws JspException {
		ServletRequest request =  this.pageContext.getRequest();
		RequestContext reqCtx = (RequestContext) request.getAttribute(RequestContext.REQCTX);
		try {
			if (!this.isRightKey()) {
				String message = "";
				for (int i=0; i < IContentListWidgetHelper.allowedMetadataUserFilterOptionKeys.length; i++) {
					if (i!=0) message.concat(",");
					message.concat(IContentListWidgetHelper.allowedMetadataUserFilterOptionKeys[i]);
				}
				throw new RuntimeException("The key '" + this.getKey() + "' is unknown; " +
						"Please use a valid one - " + message);
			}
			IContentListWidgetHelper helper = (IContentListWidgetHelper) ApsWebApplicationUtils.getBean(JacmsSystemConstants.CONTENT_LIST_HELPER, this.pageContext);
			IContentListTagBean parent = (IContentListTagBean) findAncestorWithClass(this, IContentListTagBean.class);
			String contentType = parent.getContentType();
			UserFilterOptionBean filter = helper.getUserFilterOption(contentType, this, reqCtx);
			if (null != filter) {
				parent.addUserFilterOption(filter);
			}
		} catch (Throwable t) {
			_logger.error("error in do end tag", t);
			//ApsSystemUtils.logThrowable(t, this, "doEndTag");
			throw new JspException("Tag error detected ", t);
		}
		return super.doEndTag();
	}
	
	private boolean isRightKey() {
		if (this.isAttributeFilter()) {
			return true;
		} else {
			for (int i = 0; i < IContentListWidgetHelper.allowedMetadataUserFilterOptionKeys.length; i++) {
				if (IContentListWidgetHelper.allowedMetadataUserFilterOptionKeys[i].equals(this.getKey())) return true;
			}
		}
		return false;
	}
	
	@Override
	public void release() {
		this._key = null;
		this._attributeFilter = false;
	}
	
	/**
	 * Return the key of the filter.
	 * This string can be:<br/>
	 * - the name of a content attribute compatible with the type declared in the "contentListTag" (it requires the "attributeFilter" attribute to be "true")<br/>
	 * - the ID of one of the content metadata (the "attributeFilter" must be false)<br/>
	 * The allowed filter key that can be applied to content metadata are:<br/>
	 * - "fulltext" allows filter by full-text search<br />
	 * - "category" allows filter by a system category.
	 * @return The key of the filter.
	 */
	@Override
	public String getKey() {
		return _key;
	}
	
	/**
	 * Set the key of the filter.
	 * This string can be:<br/>
	 * - the name of a content attribute compatible with the type declared in the "contentListTag" (it requires the "attributeFilter" attribute to be "true")<br/>
	 * - the ID of one of the content metadata (the "attributeFilter" must be false)<br/>
	 * The allowed filter key that can be applied to content metadata are:<br/>
	 * - "fulltext" allows filter by full-text search<br />
	 * - "category" allows filter by a system category.
	 * @param key The key to set
	 */
	public void setKey(String key) {
		this._key = key;
	}
	
	/**
	 * Checks whether the filter must be applied to an attribute or to a content metadata
	 * @return true if the filter must be applied to an attribute.
	 */
	@Override
	public boolean isAttributeFilter() {
		return _attributeFilter;
	}
	
	/**
	 * Decides whether the filter must be applied to an attribute or to a
	 * content metadata, admitted values are (true|false).<br/>
	 * The "key" attribute will be checked for validity if the filter is going to be applied to a metadata
	 * @param attributeFilter true if the filter must be applied to an attribute.
	 */
	public void setAttributeFilter(boolean attributeFilter) {
		this._attributeFilter = attributeFilter;
	}
	
	@Override
	public String getEnd() {return null;}
	@Override
	public String getOrder() {return null;}
	@Override
	public String getStart() {return null;}
	@Override
	public String getValue() {return null;}
	@Override
	public boolean getLikeOption() {return false;}
	@Override
	public String getLikeOptionType() {return null;}
	
	private String _key;
	private boolean _attributeFilter;
	
}
