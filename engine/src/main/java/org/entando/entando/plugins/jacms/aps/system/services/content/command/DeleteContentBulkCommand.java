package org.entando.entando.plugins.jacms.aps.system.services.content.command;

import org.entando.entando.aps.system.common.command.constants.ApsCommandErrorCode;
import org.entando.entando.plugins.jacms.aps.system.services.content.command.common.BaseContentBulkCommand;
import org.entando.entando.plugins.jacms.aps.system.services.content.command.common.ContentBulkCommandContext;

import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.plugins.jacms.aps.system.services.content.model.Content;

public class DeleteContentBulkCommand extends BaseContentBulkCommand<ContentBulkCommandContext> {

	public static String BEAN_NAME = "jacmsDeleteContentBulkCommand";

	@Override
	protected boolean apply(Content content) throws ApsSystemException {
		if (content.isOnLine()) {
			this.getTracer().traceError(content.getId(), ApsCommandErrorCode.NOT_APPLICABLE);
			return false;
		} else {
			this.getApplier().deleteContent(content);
			return true;
		}
	}

}
