package org.entando.entando.plugins.jacms.aps.system.services.content.command.common;

import java.util.Collection;

import org.entando.entando.aps.system.common.command.BaseBulkCommand;
import org.entando.entando.aps.system.common.command.constants.CommandErrorCode;
import org.entando.entando.aps.system.common.command.tracer.BulkCommandTracer;

import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.plugins.jacms.aps.system.services.content.IContentManager;
import com.agiletec.plugins.jacms.aps.system.services.content.model.Content;

public abstract class BaseContentBulkCommand<P> extends BaseBulkCommand<String, IContentManager> {

	public BaseContentBulkCommand(Collection<String> items, Collection<P> itemProperties, IContentManager manager, BulkCommandTracer<String> tracer) {
		super(items, manager, tracer);
		this.setItemProperties(itemProperties);
	}

	@Override
	protected boolean apply(String item) throws ApsSystemException {
		boolean performed = false;
		Content content = this.getContent(item);
		if (content == null) {
			this.getTracer().traceError(item, CommandErrorCode.NOT_FOUND);
		} else {
			performed = this.apply(content);
		}
		return performed;
	}

	protected abstract boolean apply(Content content) throws ApsSystemException;

	protected Content getContent(String id) throws ApsSystemException {
		return this.getApplier().loadContent(id, false);
	}

	public Collection<P> getItemProperties() {
		return _itemProperties;
	}
	protected void setItemProperties(Collection<P> itemProperties) {
		this._itemProperties = itemProperties;
	}

	private Collection<P> _itemProperties;

}
