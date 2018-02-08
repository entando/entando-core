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
package com.agiletec.aps.tags;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agiletec.aps.system.RequestContext;
import com.agiletec.aps.system.SystemConstants;
import com.agiletec.aps.system.services.category.Category;
import com.agiletec.aps.system.services.category.ICategoryManager;
import com.agiletec.aps.system.services.lang.Lang;
import com.agiletec.aps.util.ApsWebApplicationUtils;
import com.agiletec.aps.util.SelectItem;
import java.util.Arrays;

/**
 * Return the list of the system categories.
 *
 * @author E.Santoboni
 */
public class CategoriesTag extends TagSupport {

	private static final Logger _logger = LoggerFactory.getLogger(CategoriesTag.class);
	
	private static final String TITLE_TYPE_DEFAULT = "default";
	private static final String TITLE_TYPE_FULL = "full";
	private static final String TITLE_TYPE_PRETTY_FULL = "prettyFull";
	
	private static final String[] ALLOWED_TITLE_TYPES = {TITLE_TYPE_DEFAULT, TITLE_TYPE_FULL, TITLE_TYPE_PRETTY_FULL};

	private String _titleStyle;
	private String _fullTitleSeparator;
	private String _var;
	private String _root;

	@Override
	public int doStartTag() throws JspException {
		ServletRequest request = this.pageContext.getRequest();
		RequestContext reqCtx = (RequestContext) request.getAttribute(RequestContext.REQCTX);
		ICategoryManager catManager = (ICategoryManager) ApsWebApplicationUtils.getBean(SystemConstants.CATEGORY_MANAGER, this.pageContext);
		try {
			List<SelectItem> categories = new ArrayList<SelectItem>();
			Category root = (null != this.getRoot()) ? catManager.getCategory(this.getRoot()) : null;
			if (null == root) {
				root = catManager.getRoot();
			}
			Lang currentLang = (Lang) reqCtx.getExtraParam(SystemConstants.EXTRAPAR_CURRENT_LANG);
			String langCode = currentLang.getCode();
			String reqTitStyle = this.getTitleStyle();
			List<String> allowedStyles = Arrays.asList(ALLOWED_TITLE_TYPES);
			String titleStyle = (null != reqTitStyle && (allowedStyles.contains(reqTitStyle))) ? reqTitStyle : null;
			this.addSmallCategory(categories, root, langCode, titleStyle, catManager);
			this.pageContext.setAttribute(this.getVar(), categories);
		} catch (Throwable t) {
			_logger.error("Error starting tag", t);
			throw new JspException("Error starting tag", t);
		}
		return super.doStartTag();
	}

	private void addSmallCategory(List<SelectItem> categories,
			Category category, String langCode, String titleStyle, ICategoryManager catManager) {
		String[] children = category.getChildrenCodes();
		if (null == children) {
			return;
		}
		for (int i = 0; i < children.length; i++) {
			Category child = catManager.getCategory(children[i]);
			String title = null;
			if (null == titleStyle || titleStyle.equals(TITLE_TYPE_DEFAULT)) {
				title = child.getTitles().getProperty(langCode);
			} else if (titleStyle.equals(TITLE_TYPE_FULL)) {
				if (null != this.getFullTitleSeparator()) {
					title = child.getFullTitle(langCode, this.getFullTitleSeparator());
				} else {
					title = child.getFullTitle(langCode);
				}
			} else if (titleStyle.equals(TITLE_TYPE_PRETTY_FULL)) {
				if (null != this.getFullTitleSeparator()) {
					title = child.getShortFullTitle(langCode, this.getFullTitleSeparator());
				} else {
					title = child.getShortFullTitle(langCode);
				}
			}
			SelectItem catSmall = new SelectItem(child.getCode(), title);
			categories.add(catSmall);
			this.addSmallCategory(categories, child, langCode, titleStyle, catManager);
		}
	}

	@Override
	public int doEndTag() throws JspException {
		this.release();
		return super.doEndTag();
	}

	@Override
	public void release() {
		this.setVar(null);
		this.setTitleStyle(null);
		this.setFullTitleSeparator(null);
	}

	public String getTitleStyle() {
		return _titleStyle;
	}

	public void setTitleStyle(String titleStyle) {
		this._titleStyle = titleStyle;
	}

	public String getFullTitleSeparator() {
		return _fullTitleSeparator;
	}

	public void setFullTitleSeparator(String fullTitleSeparator) {
		this._fullTitleSeparator = fullTitleSeparator;
	}

	public String getVar() {
		return _var;
	}

	public void setVar(String var) {
		this._var = var;
	}

	public String getRoot() {
		return _root;
	}

	public void setRoot(String root) {
		this._root = root;
	}

}
