package org.entando.entando.plugins.jacms.aps.system.services.content.command.common;

import java.util.Collection;

import org.entando.entando.aps.system.common.command.tracer.BulkCommandTracer;

import com.agiletec.plugins.jacms.aps.system.services.content.IContentManager;

public abstract class BaseContentPropertyBulkCommand<P> extends BaseContentBulkCommand {

	public BaseContentPropertyBulkCommand(Collection<String> items, Collection<P> itemProperties, IContentManager manager, BulkCommandTracer<String> tracer) {
		super(items, manager, tracer);
		this.setItemProperties(itemProperties);
	}

	public Collection<P> getItemProperties() {
		return _itemProperties;
	}
	protected void setItemProperties(Collection<P> itemProperties) {
		this._itemProperties = itemProperties;
	}

	private Collection<P> _itemProperties;

}
