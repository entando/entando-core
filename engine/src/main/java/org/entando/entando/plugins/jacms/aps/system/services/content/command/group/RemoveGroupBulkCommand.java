package org.entando.entando.plugins.jacms.aps.system.services.content.command.group;

import java.util.Collection;

import org.entando.entando.aps.system.common.command.constants.CommandErrorCode;
import org.entando.entando.aps.system.common.command.constants.CommandWarningCode;
import org.entando.entando.aps.system.common.command.tracer.BulkCommandTracer;

import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.aps.system.services.lang.ILangManager;
import com.agiletec.aps.system.services.page.IPageManager;
import com.agiletec.plugins.jacms.aps.system.services.content.ContentUtilizer;
import com.agiletec.plugins.jacms.aps.system.services.content.IContentManager;
import com.agiletec.plugins.jacms.aps.system.services.content.model.Content;

public class RemoveGroupBulkCommand extends BaseContentGroupBulkCommand {

	public RemoveGroupBulkCommand(Collection<String> items, String group, IContentManager manager, 
			ContentUtilizer[] contentUtilizers, ILangManager langManager, IPageManager pageManager, BulkCommandTracer<String> tracer) {
		super(items, group, manager, contentUtilizers, langManager, pageManager, tracer);
	}

	@Override
	protected boolean apply(Content content) throws ApsSystemException {
		boolean performed = true;
		String group = this.getItemProperty();
		if (null == group) {
			this.getTracer().traceError(content.getId(), CommandErrorCode.PARAMS_NOT_VALID);
			performed = false;
		} else if (content.getGroups()==null || !content.getGroups().contains(group)) {
			this.getTracer().traceWarning(content.getId(), CommandWarningCode.NOT_NECESSARY);
		} else {
			content.getGroups().remove(group);
			performed = this.checkContentUtilizers(content) && this.checkContentReferences(content);
			if (performed) {
				this.getApplier().saveContent(content);
				this.getTracer().traceSuccess(content.getId());
			}
		}
		return performed;
	}

}
