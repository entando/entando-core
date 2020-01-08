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

import com.agiletec.aps.system.common.tree.ITreeNode;
import com.agiletec.aps.system.common.tree.ITreeNodeManager;
import com.agiletec.aps.system.services.page.IPage;
import com.agiletec.apsadmin.system.TreeNodeWrapper;

public class PageTreeNodeWrapper extends TreeNodeWrapper {

	public PageTreeNodeWrapper() {
		super();
	}

	public PageTreeNodeWrapper(IPage page, ITreeNode parent) {
		super(page, parent);
		this._origin = page;
	}

	public PageTreeNodeWrapper(IPage page, ITreeNode parent, String currentLang, ITreeNodeManager treeNodeManager) {
		super(page, parent, currentLang, treeNodeManager);
		this._origin = page;
	}

	@Deprecated
	public IPage getEntity() {
		return this.getOrigin();
	}

	public IPage getOrigin() {
		return _origin;
	}

	public void setOrigin(IPage origin) {
		this._origin = origin;
	}

	private IPage _origin;

}
