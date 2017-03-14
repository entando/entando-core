package org.entando.entando.plugins.jacms.aps.system.services.content.command.category;

import java.util.Collection;

import org.entando.entando.aps.system.common.command.constants.CommandErrorCode;
import org.entando.entando.aps.system.common.command.constants.CommandWarningCode;
import org.entando.entando.aps.system.common.command.tracer.BulkCommandTracer;
import org.entando.entando.plugins.jacms.aps.system.services.content.command.common.BaseContentBulkCommand;

import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.aps.system.services.category.Category;
import com.agiletec.plugins.jacms.aps.system.services.content.IContentManager;
import com.agiletec.plugins.jacms.aps.system.services.content.model.Content;

public class JoinCategoryBulkCommand extends BaseContentBulkCommand<Category> {

	public JoinCategoryBulkCommand(Collection<String> items, Category category, IContentManager manager, BulkCommandTracer<String> tracer) {
		super(items, category, manager, tracer);
	}

	@Override
	protected boolean apply(Content content) throws ApsSystemException {
		Category category = this.getItemProperty();
		if (null == category || category.getCode().equals(category.getParentCode())) {
			this.getTracer().traceError(content.getId(), CommandErrorCode.PARAMS_NOT_VALID);
			return false;
		} else if (content.getCategories().contains(category)) {
			this.getTracer().traceWarning(content.getId(), CommandWarningCode.NOT_NECESSARY);
		} else {
			content.addCategory(category);
			this.getApplier().saveContent(content);
			this.getTracer().traceSuccess(content.getId());
		}
		return true;
	}

}
