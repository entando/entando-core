package org.entando.entando.plugins.jacms.aps.system.services.content.command.group;

import java.util.Collection;

import org.entando.entando.aps.system.common.command.constants.ApsCommandErrorCode;
import org.entando.entando.aps.system.common.command.tracer.BulkCommandTracer;
import org.springframework.web.context.WebApplicationContext;

import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.plugins.jacms.aps.system.services.content.IContentManager;
import com.agiletec.plugins.jacms.aps.system.services.content.model.Content;

public class JoinGroupBulkCommand extends BaseContentGroupBulkCommand {

	public JoinGroupBulkCommand(Collection<String> items, Collection<String> groups, 
			IContentManager manager, BulkCommandTracer<String> tracer, WebApplicationContext wax) {
		super(items, groups, manager, tracer, wax);
	}

	@Override
	protected boolean apply(Content content) throws ApsSystemException {
		boolean performed = true;
		Collection<String> groups = this.getItemProperties();
		if (null == groups || groups.isEmpty()) {
			this.getTracer().traceError(content.getId(), ApsCommandErrorCode.PARAMS_NOT_VALID);
			performed = false;
//		} else if (group.equals(content.getMainGroup()) || (content.getGroups()!=null && content.getGroups().contains(group))) {
//			this.getTracer().traceWarning(content.getId(), CommandWarningCode.NOT_NECESSARY);
		} else {
			// TODO assicurarsi che l'add non abbia impatto su contenuti in cache (o meglio ancora scrivere un'operazione che faccia un restore)
			content.getGroups().addAll(groups);// Preliminar ADD, useful for check
			performed = this.checkContentUtilizers(content) && this.checkContentReferences(content);
			if (performed) {
				this.getApplier().saveContent(content);
				this.getTracer().traceSuccess(content.getId());
			}
		}
		return performed;
	}

}
