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
package org.entando.entando.plugins.jacms.aps.tags;

import com.agiletec.aps.system.RequestContext;
import com.agiletec.aps.system.SystemConstants;
import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.aps.system.services.lang.Lang;
import com.agiletec.aps.system.services.page.Widget;
import com.agiletec.aps.util.ApsProperties;
import com.agiletec.plugins.jacms.aps.system.services.content.widget.IContentListWidgetHelper;
import com.agiletec.plugins.jacms.aps.tags.ContentListTag;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import javax.servlet.ServletRequest;
import javax.servlet.jsp.JspException;

import org.apache.commons.lang3.StringUtils;
import org.entando.entando.plugins.jacms.aps.system.services.content.widget.RowContentListHelper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Loads a list of contents IDs by applying the filters (if any).
 * @author E.Santoboni
 */
public class RowContentListTag extends ContentListTag {

	private static final Logger _logger = LoggerFactory.getLogger(RowContentListTag.class);
	
	public RowContentListTag() {
		super();
		this.release();
	}
	
	@Override
	public int doStartTag() throws JspException {
		return EVAL_BODY_INCLUDE;
	}
	
	@Override
	public int doEndTag() throws JspException {
		ServletRequest request =  this.pageContext.getRequest();
		RequestContext reqCtx = (RequestContext) request.getAttribute(RequestContext.REQCTX);
		try {
			this.extractExtraWidgetParameters(reqCtx);
			List<Properties> contents = this.getContents(reqCtx);
			this.pageContext.setAttribute(this.getListName(), contents);
		} catch (Throwable t) {
			_logger.error("error in end tag", t);
			throw new JspException("Error detected while finalising the tag", t);
		}
		this.release();
		return EVAL_PAGE;
	}
	
	protected List<Properties> getContents(RequestContext reqCtx) throws ApsSystemException {
		List<Properties> contents = null;
		try {
			Widget currentWidget = (Widget) reqCtx.getExtraParam(SystemConstants.EXTRAPAR_CURRENT_WIDGET);
			if (null == currentWidget || null == currentWidget.getConfig()) {
				return new ArrayList<Properties>();
			}
			String widgetConfig = currentWidget.getConfig().getProperty("contents");
			contents = RowContentListHelper.fromParameterToContents(widgetConfig);
			if (StringUtils.isBlank(widgetConfig)) {
				return contents;
			}
		} catch (Throwable t) {
			_logger.error("Error extracting contents", t);
			throw new ApsSystemException("Error extracting contents", t);
		}
		return contents;
	}
	
	private void extractExtraWidgetParameters(RequestContext reqCtx) {
		try {
			Widget currentWidget = (Widget) reqCtx.getExtraParam((SystemConstants.EXTRAPAR_CURRENT_WIDGET));
			ApsProperties config = (null != currentWidget) ? currentWidget.getConfig() : null;
			if (null != config) {
				Lang currentLang = (Lang) reqCtx.getExtraParam((SystemConstants.EXTRAPAR_CURRENT_LANG));
				this.addMultilanguageWidgetParameter(config, IContentListWidgetHelper.WIDGET_PARAM_TITLE, currentLang, this.getTitleVar());
				this.addMultilanguageWidgetParameter(config, IContentListWidgetHelper.WIDGET_PARAM_PAGE_LINK_DESCR, currentLang, this.getPageLinkDescriptionVar());
				if (null != this.getPageLinkVar()) {
					String pageLink = config.getProperty(IContentListWidgetHelper.WIDGET_PARAM_PAGE_LINK);
					if (null != pageLink) {
						this.pageContext.setAttribute(this.getPageLinkVar(), pageLink);
					}
				}
			}
		} catch (Throwable t) {
			_logger.error("Error extracting extra parameters", t);
		}
	}
	
}