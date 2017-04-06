package org.entando.entando.plugins.jacms.aps.system.services.content.command;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.entando.entando.aps.system.common.command.constants.ApsCommandErrorCode;
import org.entando.entando.plugins.jacms.aps.system.services.content.command.common.BaseContentBulkCommand;
import org.entando.entando.plugins.jacms.aps.system.services.content.command.common.ContentBulkCommandContext;

import com.agiletec.aps.system.common.entity.model.FieldError;
import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.aps.system.services.group.IGroupManager;
import com.agiletec.plugins.jacms.aps.system.services.content.model.Content;

public class InsertOnlineContentBulkCommand extends BaseContentBulkCommand<ContentBulkCommandContext> {

	public static String BEAN_NAME = "jacmsInsertOnlineContentBulkCommand";

	@Override
	protected boolean apply(Content content) throws ApsSystemException {
		if (!this.validateContent(content)) {
			this.getTracer().traceError(content.getId(), ApsCommandErrorCode.NOT_APPLICABLE);
			return false;
		} else {
			this.getApplier().insertOnLineContent(content);
			return true;
		}
	}

	protected boolean validateContent(Content content) throws ApsSystemException {
		boolean valid = this.validateDescription(content) && this.checkContentUtilizers(content);
		if (valid) {
			List<FieldError> errors = content.validate(this.getGroupManager());
			valid = errors == null || errors.isEmpty();
		}
		return valid;
	}

	protected boolean validateDescription(Content content) {
		boolean valid = false;
		String descr = content.getDescription();
		int maxLength = 250;
		String regex = "([^\"])+";
    	if (StringUtils.isNotEmpty(descr) && descr.length() <= maxLength && descr.matches(regex)) {
    		valid = true;
		}
		return valid;
	}

	protected IGroupManager getGroupManager() {
		return _groupManager;
	}
	public void setGroupManager(IGroupManager groupManager) {
		this._groupManager = groupManager;
	}

	private IGroupManager _groupManager;

}
