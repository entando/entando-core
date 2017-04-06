package org.entando.entando.plugins.jacms.aps.system.services.content.command;

import java.util.List;
import java.util.Map;

import org.entando.entando.aps.system.common.command.constants.ApsCommandErrorCode;
import org.entando.entando.plugins.jacms.aps.system.services.content.command.common.BaseContentBulkCommand;
import org.entando.entando.plugins.jacms.aps.system.services.content.command.common.ContentBulkCommandContext;
import org.entando.entando.plugins.jacms.aps.system.services.content.helper.IContentHelper;

import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.plugins.jacms.aps.system.services.content.ContentUtilizer;
import com.agiletec.plugins.jacms.aps.system.services.content.model.Content;

public class RemoveOnlineContentBulkCommand extends BaseContentBulkCommand<ContentBulkCommandContext> {

	public static String BEAN_NAME = "jacmsRemoveOnlineContentBulkCommand";

	@Override
	protected boolean apply(Content content) throws ApsSystemException {
		if (!this.validateContent(content)) {
			this.getTracer().traceError(content.getId(), ApsCommandErrorCode.NOT_APPLICABLE);
			return false;
		} else {
			this.getApplier().removeOnLineContent(content);
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
		this._contentUtilizers = this.getContentHelper().getContentUtilizers();
	}

	protected List<ContentUtilizer> getContentUtilizers() {
		return _contentUtilizers;
	}
	protected void setContentUtilizers(List<ContentUtilizer> contentUtilizers) {
		this._contentUtilizers = contentUtilizers;
	}

	private IContentHelper _contentHelper;
	private List<ContentUtilizer> _contentUtilizers;

}
