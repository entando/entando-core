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
package org.entando.entando.plugins.jacms.aps.system.services.content.command.group;

import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.plugins.jacms.aps.system.services.content.model.Content;
import org.entando.entando.aps.system.common.command.constants.ApsCommandErrorCode;

import java.util.Collection;

public class RemoveGroupBulkCommand extends BaseContentGroupBulkCommand {

	public static final String BEAN_NAME = "jacmsRemoveGroupBulkCommand";
	public static final String COMMAND_NAME = "content.group.rem";

	@Override
	public String getName() {
		return COMMAND_NAME;
	}

	@Override
	protected boolean apply(Content content) throws ApsSystemException {
		boolean performed = true;
		Collection<String> groups = this.getItemProperties();
		if (null == groups || groups.isEmpty()) {
			this.getTracer().traceError(content.getId(), ApsCommandErrorCode.PARAMS_NOT_VALID);
			performed = false;
		} else {
			// TODO assicurarsi che il remove non abbia impatto su contenuti in cache (o meglio ancora scrivere un'operazione che faccia un restore)
			content.getGroups().removeAll(groups);// Preliminar REMOVE, useful for check
			performed = this.checkContentUtilizers(content) && this.checkContentReferences(content);
			if (performed) {
				this.getApplier().saveContent(content);
				this.getTracer().traceSuccess(content.getId());
			} else {
				this.getTracer().traceError(content.getId(), ApsCommandErrorCode.NOT_APPLICABLE);
			}
		}
		return performed;
	}

}
