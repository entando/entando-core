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
