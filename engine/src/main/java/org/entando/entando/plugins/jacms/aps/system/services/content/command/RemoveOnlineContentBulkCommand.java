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
package org.entando.entando.plugins.jacms.aps.system.services.content.command;

import java.util.List;
import java.util.Map;

import org.entando.entando.aps.system.common.command.constants.ApsCommandErrorCode;
import org.entando.entando.plugins.jacms.aps.system.services.content.command.common.BaseContentBulkCommand;
import org.entando.entando.plugins.jacms.aps.system.services.content.command.common.ContentBulkCommandContext;
import org.entando.entando.plugins.jacms.aps.system.services.content.helper.IContentHelper;

import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.plugins.jacms.aps.system.services.content.model.Content;

public class RemoveOnlineContentBulkCommand extends BaseContentBulkCommand<ContentBulkCommandContext> {

	public static String BEAN_NAME = "jacmsRemoveOnlineContentBulkCommand";
	public static String COMMAND_NAME = "content.offline";

	@Override
	public String getName() {
		return COMMAND_NAME;
	}

	@Override
	protected boolean apply(Content content) throws ApsSystemException {
		if (!this.validateContent(content)) {
			this.getTracer().traceError(content.getId(), ApsCommandErrorCode.NOT_APPLICABLE);
			return false;
		} else {
			this.getApplier().removeOnLineContent(content);
			this.getTracer().traceSuccess(content.getId());
			return true;
		}
	}

	protected boolean validateContent(Content content) throws ApsSystemException {
		Map<String, List<?>> references = this.getContentHelper().getReferencingObjects(content, this.getContentUtilizers());
		return references == null || references.isEmpty();
	}

	protected IContentHelper getContentHelper() {
		return _contentHelper;
	}
	public void setContentHelper(IContentHelper contentHelper) {
		this._contentHelper = contentHelper;
	}

	private IContentHelper _contentHelper;

}
