package org.entando.entando.plugins.jacms.apsadmin.content.bulk;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import org.entando.entando.aps.system.common.command.BaseBulkCommand;
import org.entando.entando.aps.system.common.command.report.BulkCommandReport;
import org.entando.entando.aps.system.common.command.tracer.BulkCommandTracer;
import org.entando.entando.aps.system.common.command.tracer.DefaultBulkCommandTracer;
import org.entando.entando.aps.system.services.command.IBulkCommandManager;
import org.entando.entando.plugins.jacms.aps.system.services.content.command.category.JoinCategoryBulkCommand;
import org.entando.entando.plugins.jacms.aps.system.services.content.command.category.RemoveCategoryBulkCommand;
import org.entando.entando.plugins.jacms.aps.system.services.content.command.common.BaseContentBulkCommand;
import org.entando.entando.plugins.jacms.apsadmin.content.bulk.util.ContentBulkActionSummary;
import org.entando.entando.plugins.jacms.apsadmin.content.bulk.util.IContentBulkActionHelper;
import org.entando.entando.plugins.jacms.apsadmin.content.bulk.util.SmallBulkCommandReport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agiletec.aps.system.services.category.Category;
import com.agiletec.aps.system.services.category.ICategoryManager;
import com.agiletec.apsadmin.system.ApsAdminSystemConstants;
import com.agiletec.plugins.jacms.aps.system.services.content.IContentManager;

public class ContentCategoryBulkAction extends BaseContentBulkAction {

	private static final Logger _logger = LoggerFactory.getLogger(ContentCategoryBulkAction.class);

	public String entry() {
		return this.checkAllowedContents(false) ? SUCCESS : "list";
	}

	public String join() {
		try {
			String categoryCode = this.getCategoryCode();
			Category category = this.getCategoryManager().getCategory(categoryCode);
			if (null != category) {
				this.getCategoryCodes().add(categoryCode);
			}
		} catch (Throwable t) {
			_logger.error("Error joining category for bulk action", t);
			return FAILURE;
		}
		return SUCCESS;
	}

	public String disjoin() {
		try {
			this.getCategoryCodes().remove(this.getCategoryCode());
		} catch (Throwable t) {
			_logger.error("Error removing category from bulk action", t);
			return FAILURE;
		}
		return SUCCESS;
	}

	public String checkApply() {
		return this.checkCategories() ? SUCCESS : INPUT;
	}

	public String apply() {
		try {
			if (!this.checkAllowedContents(true)) {
				return "list";
			} else {
				List<Category> categories = this.getCategoriesToManage();
				if (categories == null) {
					return INPUT;
				} else {
					BulkCommandTracer<String> tracer = new DefaultBulkCommandTracer<String>();
					BaseContentBulkCommand<Category> command = null;
					if (ApsAdminSystemConstants.DELETE == this.getStrutsAction()) {
						command = new RemoveCategoryBulkCommand(this.getContentIds(), categories, 
								this.getContentManager(), tracer);
					} else {
						command = new JoinCategoryBulkCommand(this.getContentIds(), categories, 
								this.getContentManager(), tracer);
					}
					BulkCommandReport<String> report = this.getBulkCommandManager().addCommand(this.getCommandOwner(), command);
					this.setCommandId(report.getCommandId());
				}
			}
		} catch (Throwable t) {
			_logger.error("Error occurred applying categories (add/remove)", t);
			return FAILURE;
		}
		return SUCCESS;
	}

	protected boolean checkCategories() {
		boolean allowed = true;
		Set<String> categoryCodes = this.getCategoryCodes();
		if (categoryCodes == null || categoryCodes.isEmpty()) {
			this.addActionError(this.getText("error.bulk.categories.empty"));
			allowed = false;
		}
		return allowed;
	}

	protected List<Category> getCategoriesToManage() {
		boolean allowed = true;
		List<Category> categories = new ArrayList<Category>();
		Set<String> categoryCodes = this.getCategoryCodes();
		if (categoryCodes == null || categoryCodes.isEmpty()) {
			this.addActionError(this.getText("error.bulk.categories.empty"));
			allowed = false;
		} else {
			ICategoryManager categoryManager = this.getCategoryManager();
			for (String categoryCode : categoryCodes) {
				Category category = categoryManager.getCategory(categoryCode);
				if (category == null || category.isRoot()) {
					this.addActionError(this.getText("error.bulk.categories.notAllowed", categoryCode));
					allowed = false;
				} else {
					categories.add(category);
				}
			}
		}
		return allowed ? categories : null;
	}

	public String viewResult() {
		return this.getReport() == null ? "expired" : SUCCESS;
	}

	public ContentBulkActionSummary getSummary() {
		return this.getBulkActionHelper().getSummary(this.getContentIds());
	}

	public BaseBulkCommand<?, ?> getCommand() {
		return this.getBulkCommandManager().getCommand(this.getCommandOwner(), this.getCommandId());
	}

	public BulkCommandReport<?> getReport() {
		return this.getBulkCommandManager().getCommandReport(this.getCommandOwner(), this.getCommandId());
	}

	public SmallBulkCommandReport getSmallReport() {
		return this.getBulkActionHelper().getSmallReport(this.getReport());
	}

	protected boolean checkAllowedContents(boolean fullCheck) {
		return this.getBulkActionHelper().checkAllowedContents(this.getContentIds(), fullCheck, this, this);
	}

	public String getCommandOwner() {
		return IContentBulkActionHelper.BULK_COMMAND_OWNER;
	}

	public Set<String> getContentIds() {
		return _contentIds;
	}
	public void setContentIds(Set<String> contentIds) {
		this._contentIds = contentIds;
	}

	public int getStrutsAction() {
		return _strutsAction;
	}
	public void setStrutsAction(int strutsAction) {
		this._strutsAction = strutsAction;
	}

	public String getCommandId() {
		return _commandId;
	}
	public void setCommandId(String commandId) {
		this._commandId = commandId;
	}

	public Set<String> getCategoryCodes() {
		return _categoryCodes;
	}
	public void setCategoryCodes(Set<String> categoryCodes) {
		this._categoryCodes = categoryCodes;
	}

	public String getCategoryCode() {
		return _categoryCode;
	}
	public void setCategoryCode(String categoryCode) {
		this._categoryCode = categoryCode;
	}

	protected ICategoryManager getCategoryManager() {
		return _categoryManager;
	}
	public void setCategoryManager(ICategoryManager categoryManager) {
		this._categoryManager = categoryManager;
	}

	protected IBulkCommandManager getBulkCommandManager() {
		return _bulkCommandManager;
	}
	public void setBulkCommandManager(IBulkCommandManager bulkCommandManager) {
		this._bulkCommandManager = bulkCommandManager;
	}

	protected IContentManager getContentManager() {
		return _contentManager;
	}
	public void setContentManager(IContentManager contentManager) {
		this._contentManager = contentManager;
	}

	protected IContentBulkActionHelper getBulkActionHelper() {
		return _bulkActionHelper;
	}
	public void setBulkActionHelper(IContentBulkActionHelper bulkActionHelper) {
		this._bulkActionHelper = bulkActionHelper;
	}

	private Set<String> _contentIds;

	private int _strutsAction;
	private String _commandId;

	private Set<String> _categoryCodes = new TreeSet<String>();
	private String _categoryCode;
	private ICategoryManager _categoryManager;

	private IBulkCommandManager _bulkCommandManager;
	private IContentManager _contentManager;
	private IContentBulkActionHelper _bulkActionHelper;

}
