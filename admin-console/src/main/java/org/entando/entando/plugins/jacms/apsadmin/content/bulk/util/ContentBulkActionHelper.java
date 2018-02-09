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
package org.entando.entando.plugins.jacms.apsadmin.content.bulk.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.entando.entando.aps.system.common.command.report.BulkCommandReport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agiletec.aps.system.services.category.Category;
import com.agiletec.aps.system.services.category.ICategoryManager;
import com.agiletec.aps.system.services.group.Group;
import com.agiletec.plugins.jacms.aps.system.services.content.IContentManager;
import com.agiletec.plugins.jacms.aps.system.services.content.model.ContentRecordVO;
import com.opensymphony.xwork2.TextProvider;
import com.opensymphony.xwork2.interceptor.ValidationAware;

public class ContentBulkActionHelper implements IContentBulkActionHelper {

	private static final Logger _logger = LoggerFactory.getLogger(ContentBulkActionHelper.class);

	/**
	 * Checks if the contents are allowed.<br/>
	 * The check can be done only on the number of elements, or can be more 
	 * @return True if the contents are allowed, false otherwise.
	 */
	@Override
	public boolean checkAllowedContents(Collection<String> contentIds, ValidationAware validation, TextProvider textProvider) {
		boolean result = true;
		if (contentIds == null || contentIds.isEmpty()) {
			validation.addActionError(textProvider.getText("error.content.bulk.noItems"));
			result = false;
		}
		return result;
	}

	@Override
	public boolean checkGroups(Collection<Group> allowedGroups, Collection<String> selectedGroupCodes, ValidationAware validation, TextProvider textProvider) {
		boolean allowed = true;
		if (selectedGroupCodes == null || selectedGroupCodes.isEmpty()) {
			validation.addActionError(textProvider.getText("error.bulk.groups.empty"));
			allowed = false;
		} else {
			List<String> groupNames = this.getAllowedGroupNames(allowedGroups);
			for (String groupName : selectedGroupCodes) {
				if (!groupNames.contains(groupName)) {
					validation.addActionError(textProvider.getText("error.bulk.groups.notAllowed", new String[] {groupName}));
					allowed = false;
				}
			}
		}
		return allowed;
	}

	@Override
	public boolean checkCategories(Collection<String> categoryCodes, ValidationAware validation, TextProvider textProvider) {
		boolean allowed = true;
		if (categoryCodes == null || categoryCodes.isEmpty()) {
			validation.addActionError(textProvider.getText("error.bulk.categories.empty"));
			allowed = false;
		}
		return allowed;
	}
	
	@Override
	public List<Category> getCategoriesToManage(Collection<String> categoryCodes, ValidationAware validation, TextProvider textProvider) {
		boolean allowed = true;
		List<Category> categories = new ArrayList<Category>();
		if (categoryCodes == null || categoryCodes.isEmpty()) {
			validation.addActionError(textProvider.getText("error.bulk.categories.empty"));
			allowed = false;
		} else {
			ICategoryManager categoryManager = this.getCategoryManager();
			for (String categoryCode : categoryCodes) {
				Category category = categoryManager.getCategory(categoryCode);
				if (category == null || category.isRoot()) {
					validation.addActionError(textProvider.getText("error.bulk.categories.notAllowed", new String[] {categoryCode}));
					allowed = false;
				} else {
					categories.add(category);
				}
			}
		}
		return allowed ? categories : null;
	}

	@Override
	public ContentBulkActionSummary getSummary(Collection<String> contentIds) {
		ContentBulkActionSummary summary = new ContentBulkActionSummary();
		try {
			IContentManager contentManager = this.getContentManager();
			for (String contentId : contentIds) {
				ContentRecordVO contentVO = contentManager.loadContentVO(contentId);
				if (contentVO.isOnLine()) {
					if (contentVO.isSync()) {
						summary.addAligned(contentId);
					} else {
						summary.addWorkAhead(contentId);
					}
				} else {
					summary.addNotOnline(contentId);
				}
			}
		} catch (Throwable t) {
			_logger.error("Error checking content to generate summary for bulk action", t);
		}
		return summary;
	}

	@Override
	public SmallBulkCommandReport getSmallReport(BulkCommandReport<?> report) {
		SmallBulkCommandReport smallReport = null;
		if (report != null) {
			smallReport = new SmallBulkCommandReport();
			smallReport.setCommandId(report.getCommandId());
			smallReport.setStatus(report.getStatus());
			smallReport.setEndingTime(report.getEndingTime());
			smallReport.setTotal(report.getTotal());
			smallReport.setApplyTotal(report.getApplyTotal());
			smallReport.setApplySuccesses(report.getApplySuccesses());
			smallReport.setApplyErrors(report.getApplyErrors());
		}
		return smallReport;
	}

	protected List<String> getAllowedGroupNames(Collection<Group> allowedGroups) {
		List<String> groupNames = new ArrayList<String>(allowedGroups != null ? allowedGroups.size() : 0);
		for (Group group : allowedGroups) {
			groupNames.add(group.getAuthority());
		}
		return groupNames;
	}

	protected IContentManager getContentManager() {
		return _contentManager;
	}
	public void setContentManager(IContentManager contentManager) {
		this._contentManager = contentManager;
	}

	protected ICategoryManager getCategoryManager() {
		return _categoryManager;
	}
	public void setCategoryManager(ICategoryManager categoryManager) {
		this._categoryManager = categoryManager;
	}

	private IContentManager _contentManager;
	private ICategoryManager _categoryManager;

}
