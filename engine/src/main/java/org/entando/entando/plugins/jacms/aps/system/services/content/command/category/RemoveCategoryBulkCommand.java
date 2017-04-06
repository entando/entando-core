package org.entando.entando.plugins.jacms.aps.system.services.content.command.category;

import java.util.Collection;

import org.entando.entando.aps.system.common.command.constants.ApsCommandErrorCode;
import org.entando.entando.plugins.jacms.aps.system.services.content.command.common.BaseContentPropertyBulkCommand;

import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.aps.system.services.category.Category;
import com.agiletec.plugins.jacms.aps.system.services.content.model.Content;

public class RemoveCategoryBulkCommand extends BaseContentPropertyBulkCommand<Category> {

	public static String BEAN_NAME = "jacmsRemoveCategoryBulkCommand";

	@Override
	protected boolean apply(Content content) throws ApsSystemException {
		Collection<Category> categories = this.getItemProperties();
		if (null == categories || categories.isEmpty()) {
			this.getTracer().traceError(content.getId(), ApsCommandErrorCode.PARAMS_NOT_VALID);
			return false;
		} else {
			for (Category category : categories) {
				if (null != category && !category.getCode().equals(category.getParentCode())) {
					content.removeCategory(category);
				}
			}
			this.getApplier().saveContent(content);
			this.getTracer().traceSuccess(content.getId());
		}
		return true;
	}

}
