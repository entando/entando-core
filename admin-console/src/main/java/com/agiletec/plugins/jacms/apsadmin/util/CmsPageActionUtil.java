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
package com.agiletec.plugins.jacms.apsadmin.util;

import org.entando.entando.plugins.jacms.aps.util.CmsPageUtil;

import com.agiletec.aps.system.services.page.IPage;
import com.agiletec.plugins.jacms.aps.system.services.content.model.Content;

/**
 * @author E.Santoboni
 */
public class CmsPageActionUtil {

	/**
	 * Check if is possible to publish the content in the given page.
	 * @param publishingContent The publishing content.
	 * @param page The page where you want to publish the content.
	 * @return True/false if is possible to publish the content in the given page.
	 * @deprecated Use {@link CmsPageUtil#isContentPublishableOnPageOnline(Content, IPage)} or {@link CmsPageUtil#isContentPublishableOnPageDraft(Content, IPage)} instead.
	 */
	@Deprecated
	public static boolean isContentPublishableOnPage(Content publishingContent, IPage page) {
		return CmsPageUtil.isContentPublishableOnPageOnline(publishingContent, page);
	}
	
	/**
	 * Check if is possible to link a page by the given content.
	 * @param page The page to link
	 * @param content The content where link the given page.
	 * @return True/false if is possible to link the page in the given content.
	 * @deprecated Use {@link CmsPageUtil#isPageLinkableByContentOnline(IPage, Content)} or {@link CmsPageUtil#isPageLinkableByContentDraft(IPage, Content)} instead.
	 */
	@Deprecated
	public static boolean isPageLinkableByContent(IPage page, Content content) {
		return CmsPageUtil.isPageLinkableByContentOnline(page, content);
	}
	
	/**
	 * Check whether the page can publish free content.
	 * @param page The page to check.
	 * @param viewerWidgetCode The code of the viewer widget (optional)
	 * @return True if the page can publish free content, false else.
	 * @deprecated Use {@link CmsPageUtil#isOnlineFreeViewerPage(IPage, String)} or {@link CmsPageUtil#isDraftFreeViewerPage(IPage, String)} instead.
	 */
	public static boolean isFreeViewerPage(IPage page, String viewerWidgetCode) {
		return CmsPageUtil.isOnlineFreeViewerPage(page, viewerWidgetCode);
	}
	
}