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

import java.util.Collection;
import java.util.List;

import org.entando.entando.aps.system.common.command.report.BulkCommandReport;

import com.agiletec.aps.system.services.category.Category;
import com.agiletec.aps.system.services.group.Group;
import com.opensymphony.xwork2.TextProvider;
import com.opensymphony.xwork2.interceptor.ValidationAware;

public interface IContentBulkActionHelper {
	
	public boolean checkAllowedContents(Collection<String> contentIds, ValidationAware validation, TextProvider textProvider);
	
	public boolean checkGroups(Collection<Group> allowedGroups, Collection<String> selectedGroupCodes, ValidationAware validation, TextProvider textProvider);
	
	public boolean checkCategories(Collection<String> categoryCodes, ValidationAware validation, TextProvider textProvider);
	
	public List<Category> getCategoriesToManage(Collection<String> categoryCodes, ValidationAware validation, TextProvider textProvider);
	
	public ContentBulkActionSummary getSummary(Collection<String> contentIds);
	
	public SmallBulkCommandReport getSmallReport(BulkCommandReport<?> report);
	
	public static final String BULK_COMMAND_OWNER = "jacms";
	
}
