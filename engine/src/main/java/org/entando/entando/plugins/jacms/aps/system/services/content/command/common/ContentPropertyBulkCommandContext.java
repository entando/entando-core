package org.entando.entando.plugins.jacms.aps.system.services.content.command.common;

import java.util.Collection;

import org.entando.entando.aps.system.common.command.tracer.BulkCommandTracer;

import com.agiletec.aps.system.services.user.UserDetails;

public class ContentPropertyBulkCommandContext<P> extends ContentBulkCommandContext {

	public ContentPropertyBulkCommandContext(Collection<String> items, Collection<P> itemProperties, UserDetails currentUser, BulkCommandTracer<String> tracer) {
		super(items, currentUser, tracer);
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
