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
package org.entando.entando.plugins.jacms.aps.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.entando.entando.aps.system.services.widgettype.WidgetTypeParameter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agiletec.aps.system.services.group.Group;
import com.agiletec.aps.system.services.page.IPage;
import com.agiletec.aps.system.services.page.PageMetadata;
import com.agiletec.aps.system.services.page.Widget;
import com.agiletec.aps.system.services.pagemodel.PageModel;
import com.agiletec.plugins.jacms.aps.system.services.content.model.Content;

/**
 * @author E.Santoboni
 */
public class CmsPageUtil {

	private static final Logger _logger = LoggerFactory.getLogger(CmsPageUtil.class);

	/**
	 * Check if is possible to publish the content in the given page, checking
	 * its online configuration.
	 * 
	 * @param publishingContent
	 * The publishing content.
	 * @param page
	 * The page where you want to publish the content.
	 * @return True/false if is possible to publish the content in the given
	 * page.
	 */
	public static boolean isContentPublishableOnPageOnline(Content publishingContent, IPage page) {
		if (!page.isOnlineInstance()) {
			_logger.warn("this check expects an online instance of the page");
			return false;
		}
		return isContentPublishableOnPage(publishingContent, page);
	}

	/**
	 * Check if is possible to publish the content in the given page, checking
	 * its draft configuration.
	 * 
	 * @param publishingContent
	 * The publishing content.
	 * @param page
	 * The page where you want to publish the content.
	 * @return True/false if is possible to publish the content in the given
	 * page.
	 */
	public static boolean isContentPublishableOnPageDraft(Content publishingContent, IPage page) {
		if (page.isOnlineInstance()) {
			_logger.warn("this check expects a draft instance of the page");
			return false;
		}
		return isContentPublishableOnPage(publishingContent, page);
	}

	/**
	 * Check if is possible to publish the content in the given page, checking
	 * the given page metadata.
	 * 
	 * @param publishingContent
	 * The publishing content.
	 * @param page
	 * The page where you want to publish the content.
	 * @param metadata
	 * The metadata of the page where you want to publish the content.
	 * @return True/false if is possible to publish the content in the given
	 * page.
	 */
	public static boolean isContentPublishableOnPage(Content publishingContent, IPage page) {
		if (publishingContent.getMainGroup().equals(Group.FREE_GROUP_NAME) || publishingContent.getGroups().contains(
				Group.FREE_GROUP_NAME)) {
			return true;
		}

		// tutti i gruppi posseduti dalla pagina devono essere contemplati nel
		// contenuto.
		List<String> pageGroups = new ArrayList<String>();
		pageGroups.add(page.getGroup());
		PageMetadata metadata = page.getMetadata();
		if (metadata != null && null != metadata.getExtraGroups()) {
			pageGroups.addAll(metadata.getExtraGroups());
		}
		List<String> contentGroups = getContentGroups(publishingContent);
		for (int i = 0; i < pageGroups.size(); i++) {
			String pageGroup = pageGroups.get(i);
			if (!pageGroup.equals(Group.ADMINS_GROUP_NAME) && !contentGroups.contains(pageGroup))
				return false;
		}
		return true;
	}

	/**
	 * Check if is possible to link a page by the given content, checking the
	 * page in its online configuration.
	 * 
	 * @param page
	 * The page to link.
	 * @param content
	 * The content where link the given page.
	 * @return True/false if is possible to link the page in the given content.
	 */
	public static boolean isPageLinkableByContentOnline(IPage page, Content content) {
		if (!page.isOnlineInstance()) {
			_logger.warn("this check expects an online instance of the page");
			return false;
		}
		return isPageLinkableByContent(page, page.getMetadata(), content);
	}

	/**
	 * Check if is possible to link a page by the given content, checking the
	 * page in its draft configuration.
	 * 
	 * @param page
	 * The page to link.
	 * @param content
	 * The content where link the given page.
	 * @return True/false if is possible to link the page in the given content.
	 */
	public static boolean isPageLinkableByContentDraft(IPage page, Content content) {
		if (page.isOnlineInstance()) {
			_logger.warn("this check expects a draft instance of the page");
			return false;
		}
		return isPageLinkableByContent(page, page.getMetadata(), content);
	}

	/**
	 * Check if is possible to link a page by the given content, checking the
	 * given page metadata.
	 * 
	 * @param page
	 * The page to link.
	 * @param metadata
	 * The metadata of the page you want to link in the given content.
	 * @param content
	 * The content where link the given page.
	 * @return True/false if is possible to link the page in the given content.
	 */
	public static boolean isPageLinkableByContent(IPage page, PageMetadata metadata, Content content) {
		Collection<String> extraPageGroups = metadata.getExtraGroups();
		if (page.getGroup().equals(Group.FREE_GROUP_NAME) || (null != extraPageGroups && extraPageGroups.contains(Group.FREE_GROUP_NAME))) {
			return true;
		}
		if (content.getMainGroup().equals(Group.ADMINS_GROUP_NAME))
			return true;
		List<String> contentGroups = getContentGroups(content);
		for (int i = 0; i < contentGroups.size(); i++) {
			String contentGroup = contentGroups.get(i);
			if (contentGroup.equals(page.getGroup()))
				return true;
		}
		return false;
	}

	private static List<String> getContentGroups(Content content) {
		List<String> contentGroups = new ArrayList<String>();
		contentGroups.add(content.getMainGroup());
		if (null != content.getGroups()) {
			contentGroups.addAll(content.getGroups());
		}
		return contentGroups;
	}

	/**
	 * Check whether the page can publish free content, related to the draft
	 * configuration of the page.
	 * 
	 * @param page
	 * The page to check.
	 * @param viewerWidgetCode
	 * The code of the viewer widget (optional)
	 * @return True if the page can publish free content, false else.
	 */
	public static boolean isDraftFreeViewerPage(IPage page, String viewerWidgetCode) {

		if (page.isOnlineInstance()) {
			_logger.warn("this check expects a draft instance of the page");
			return false;
		}

		boolean found = false;
		PageMetadata metadata = page.getMetadata();
		Widget[] widgets = page.getWidgets();
		if (metadata != null) {
			found = isFreeViewerPage(metadata.getModel(), widgets, viewerWidgetCode);
		}
		return found;
	}

	/**
	 * Check whether the page can publish free content, related to the online
	 * configuration of the page.
	 * 
	 * @param page
	 * The page to check.
	 * @param viewerWidgetCode
	 * The code of the viewer widget (optional)
	 * @return True if the page can publish free content, false else.
	 */
	public static boolean isOnlineFreeViewerPage(IPage page, String viewerWidgetCode) {
		if (!page.isOnlineInstance()) {
			_logger.warn("this check expects an online instance of the page");
			return false;
		}
		boolean found = false;
		PageMetadata metadata = page.getMetadata();
		Widget[] widgets = page.getWidgets();
		if (metadata != null) {
			found = isFreeViewerPage(metadata.getModel(), widgets, viewerWidgetCode);
		}
		return found;
	}

	/**
	 * Check whether the page can publish free content, related to the model and
	 * the widgets of the page.
	 * 
	 * @param model
	 * The model of the page to check.
	 * @param widgets
	 * The widgets of the page to check.
	 * @param viewerWidgetCode
	 * The code of the viewer widget (optional)
	 * @return True if the page can publish free content, false else.
	 */
	public static boolean isFreeViewerPage(PageModel model, Widget[] widgets, String viewerWidgetCode) {
		try {
			if (model != null && widgets != null) {
				int mainFrame = model.getMainFrame();
				if (mainFrame < 0)
					return false;
				Widget viewer = widgets[mainFrame];
				if (null == viewer)
					return false;
				boolean isRightCode = null == viewerWidgetCode || viewer.getType().getCode().equals(viewerWidgetCode);
				String actionName = viewer.getType().getAction();
				boolean isRightAction = null != actionName && actionName.toLowerCase().indexOf("viewer") >= 0;
				List<WidgetTypeParameter> typeParameters = viewer.getType().getTypeParameters();
				if ((isRightCode || isRightAction) && (null != typeParameters && !typeParameters.isEmpty()) && (null == viewer.getConfig()
						|| viewer.getConfig().isEmpty())) {
					return true;
				}
			}
		} catch (Throwable t) {
			_logger.error("Error while checking page for widget '{}'", viewerWidgetCode, t);
			// ApsSystemUtils.logThrowable(t, CmsPageActionUtil.class,
			// "isViewerPage", "Error while checking page '" + page.getCode() +
			// "'");
		}
		return false;
	}

}
