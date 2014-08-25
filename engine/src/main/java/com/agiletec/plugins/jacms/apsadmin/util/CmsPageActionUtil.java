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
package com.agiletec.plugins.jacms.apsadmin.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.entando.entando.aps.system.services.widgettype.WidgetTypeParameter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agiletec.aps.system.services.group.Group;
import com.agiletec.aps.system.services.page.IPage;
import com.agiletec.aps.system.services.page.Widget;
import com.agiletec.plugins.jacms.aps.system.services.content.model.Content;
import com.agiletec.plugins.jacms.apsadmin.tags.ResourceInfoTag;

/**
 * @author E.Santoboni
 */
public class CmsPageActionUtil {

	private static final Logger _logger = LoggerFactory.getLogger(CmsPageActionUtil.class);
	
	public static boolean isContentPublishableOnPage(Content publishingContent, IPage page) {
		if (publishingContent.getMainGroup().equals(Group.FREE_GROUP_NAME) || publishingContent.getGroups().contains(Group.FREE_GROUP_NAME)) {
			return true;
		}
		//tutti i gruppi posseduti dalla pagina devono essere contemplati nel contenuto.
		List<String> pageGroups = new ArrayList<String>();
		pageGroups.add(page.getGroup());
		if (null != page.getExtraGroups()) {
			pageGroups.addAll(page.getExtraGroups());
		}
		List<String> contentGroups = getContentGroups(publishingContent);
		for (int i = 0; i < pageGroups.size(); i++) {
			String pageGroup = pageGroups.get(i);
			if (!pageGroup.equals(Group.ADMINS_GROUP_NAME) && !contentGroups.contains(pageGroup)) return false;
		}
		return true;
	}
	
	public static boolean isPageLinkableByContent(IPage page, Content content) {
		Collection<String> extraPageGroups = page.getExtraGroups();
		if (page.getGroup().equals(Group.FREE_GROUP_NAME) 
				|| (null != extraPageGroups && extraPageGroups.contains(Group.FREE_GROUP_NAME))) {
			return true;
		}
		if (content.getMainGroup().equals(Group.ADMINS_GROUP_NAME)) return true;
		List<String> contentGroups = getContentGroups(content);
		for (int i = 0; i < contentGroups.size(); i++) {
			String contentGroup = contentGroups.get(i);
			if (contentGroup.equals(page.getGroup())) return true;
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
	 * Check whether the page can publish free content.
	 * @param page The page to check.
	 * @param viewerShowletCode The code of the viewer widget (optional)
	 * @return True if the page can publish free content, false else.
	 */
	public static boolean isFreeViewerPage(IPage page, String viewerShowletCode) {
		try {
			int mainFrame = page.getModel().getMainFrame();
			if (mainFrame < 0) return false;
			Widget viewer = page.getWidgets()[mainFrame];
			if (null == viewer) return false;
			boolean isRightCode = null == viewerShowletCode || viewer.getType().getCode().equals(viewerShowletCode);
			String actionName = viewer.getType().getAction();
			boolean isRightAction = null != actionName && actionName.toLowerCase().indexOf("viewer") >= 0;
			List<WidgetTypeParameter> typeParameters = viewer.getType().getTypeParameters();
			if ((isRightCode || isRightAction )  
					&& (null != typeParameters && !typeParameters.isEmpty())
					&& (null == viewer.getConfig() || viewer.getConfig().isEmpty())) {
				return true;
			}
		} catch (Throwable t) {
			_logger.error("Error while checking page '{}'", page.getCode(), t);
			//ApsSystemUtils.logThrowable(t, CmsPageActionUtil.class, "isViewerPage", "Error while checking page '" + page.getCode() + "'");
		}
		return false;
	}
	
}