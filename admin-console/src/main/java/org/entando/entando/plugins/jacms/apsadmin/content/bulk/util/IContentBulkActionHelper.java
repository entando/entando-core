package org.entando.entando.plugins.jacms.apsadmin.content.bulk.util;

import java.util.Collection;
import java.util.List;

import org.entando.entando.aps.system.common.command.report.BulkCommandReport;

import com.agiletec.aps.system.services.category.Category;
import com.agiletec.aps.system.services.group.Group;
import com.opensymphony.xwork2.TextProvider;
import com.opensymphony.xwork2.interceptor.ValidationAware;

public interface IContentBulkActionHelper {
	
	public boolean checkAllowedContents(Collection<String> contentIds, boolean fullCheck, ValidationAware validation, TextProvider textProvider);
	
	public boolean checkGroups(Collection<Group> allowedGroups, Collection<String> selectedGroupCodes, ValidationAware validation, TextProvider textProvider);
	
	public boolean checkCategories(Collection<String> categoryCodes, ValidationAware validation, TextProvider textProvider);
	
	public List<Category> getCategoriesToManage(Collection<String> categoryCodes, ValidationAware validation, TextProvider textProvider);
	
	public ContentBulkActionSummary getSummary(Collection<String> contentIds);
	
	public SmallBulkCommandReport getSmallReport(BulkCommandReport<?> report);
	
	public static final String BULK_COMMAND_OWNER = "jacms";

}
