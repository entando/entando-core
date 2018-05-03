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
package org.entando.entando.plugins.jacms.aps.system.services.content.command.category;

import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.aps.system.services.category.Category;
import com.agiletec.plugins.jacms.aps.system.services.content.model.Content;
import org.entando.entando.aps.system.common.command.constants.ApsCommandErrorCode;
import org.entando.entando.plugins.jacms.aps.system.services.content.command.common.BaseContentPropertyBulkCommand;

import java.util.Collection;

public class JoinCategoryBulkCommand extends BaseContentPropertyBulkCommand<Category> {

	public static final String BEAN_NAME = "jacmsJoinCategoryBulkCommand";
	public static final String COMMAND_NAME = "content.category.join";

	@Override
	public String getName() {
		return COMMAND_NAME;
	}

	@Override
	protected boolean apply(Content content) throws ApsSystemException {
		Collection<Category> categories = this.getItemProperties();
		if (null == categories || categories.isEmpty()) {
			this.getTracer().traceError(content.getId(), ApsCommandErrorCode.PARAMS_NOT_VALID);
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
