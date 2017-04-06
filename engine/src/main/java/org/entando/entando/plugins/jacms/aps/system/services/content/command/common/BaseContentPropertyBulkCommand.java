package org.entando.entando.plugins.jacms.aps.system.services.content.command.common;

import java.util.Collection;

public abstract class BaseContentPropertyBulkCommand<P> extends BaseContentBulkCommand<ContentPropertyBulkCommandContext<P>> {

	public Collection<P> getItemProperties() {
		return this.getContext().getItemProperties();
	}

}
