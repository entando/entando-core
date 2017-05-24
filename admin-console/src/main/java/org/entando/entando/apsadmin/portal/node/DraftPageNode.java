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
package org.entando.entando.apsadmin.portal.node;

import com.agiletec.aps.system.common.tree.TreeNode;
import com.agiletec.aps.system.services.page.IPage;
import com.agiletec.aps.system.services.page.PageMetadata;
import com.agiletec.aps.util.ApsProperties;

public class DraftPageNode extends TreeNode {

	public DraftPageNode(IPage entity) {
		this.setEntity(entity);
	}

	@Override
	public ApsProperties getTitles() {
		PageMetadata metadata = this.getPageMetadata();
		if (null != metadata) {
			return metadata.getTitles();
		}
		return null;
	}

	protected PageMetadata getPageMetadata() {
		PageMetadata metadata = this.getEntity().getMetadata();
		return metadata;
	}

	@Override
	public boolean isChildOf(String nodeCode) {
		return this.getEntity().isChildOf(nodeCode);
	}

	public IPage getEntity() {
		return entity;
	}

	public void setEntity(IPage entity) {
		this.entity = entity;
	}

	private IPage entity;

}
