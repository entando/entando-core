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

import org.entando.entando.aps.system.common.command.constants.ApsCommandErrorCode;
import org.entando.entando.plugins.jacms.aps.system.services.content.command.common.BaseContentBulkCommand;
import org.entando.entando.plugins.jacms.aps.system.services.content.command.common.ContentBulkCommandContext;

import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.plugins.jacms.aps.system.services.content.model.Content;

public class DeleteContentBulkCommand extends BaseContentBulkCommand<ContentBulkCommandContext> {

	public static final String BEAN_NAME = "jacmsDeleteContentBulkCommand";
	public static final String COMMAND_NAME = "content.delete";

	@Override
	public String getName() {
		return COMMAND_NAME;
	}

	@Override
	protected boolean apply(Content content) throws ApsSystemException {
		if (content.isOnLine()) {
			this.getTracer().traceError(content.getId(), ApsCommandErrorCode.NOT_APPLICABLE);
			return false;
		} else {
			this.getApplier().deleteContent(content);
			this.getTracer().traceSuccess(content.getId());
			return true;
		}
	}

}
