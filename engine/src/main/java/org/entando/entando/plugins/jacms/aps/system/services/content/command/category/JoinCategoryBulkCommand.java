package org.entando.entando.plugins.jacms.aps.system.services.content.command.category;

import java.util.Collection;

import org.entando.entando.aps.system.common.command.constants.CommandErrorCode;
import org.entando.entando.aps.system.common.command.tracer.BulkCommandTracer;
import org.entando.entando.plugins.jacms.aps.system.services.content.command.common.BaseContentBulkCommand;

import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.aps.system.services.category.Category;
import com.agiletec.plugins.jacms.aps.system.services.content.IContentManager;
import com.agiletec.plugins.jacms.aps.system.services.content.model.Content;

public class JoinCategoryBulkCommand extends BaseContentBulkCommand<Category> {

	public JoinCategoryBulkCommand(Collection<String> items, Collection<Category> categories, IContentManager manager, BulkCommandTracer<String> tracer) {
		super(items, categories, manager, tracer);
	}

	@Override
	protected boolean apply(Content content) throws ApsSystemException {
		Collection<Category> categories = this.getItemProperties();
		if (null == categories || categories.isEmpty()) {
			this.getTracer().traceError(content.getId(), CommandErrorCode.PARAMS_NOT_VALID);
			return false;
		} else {
			for (Category category : categories) {
				if (null != category && !category.getCode().equals(category.getParentCode())) {
					content.addCategory(category);
				}
			}
			this.getApplier().saveContent(content);
			this.getTracer().traceSuccess(content.getId());
		}
		return true;
	}

}
