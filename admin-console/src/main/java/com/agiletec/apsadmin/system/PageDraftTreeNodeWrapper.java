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
package com.agiletec.apsadmin.system;

import com.agiletec.aps.system.common.tree.ITreeNode;

public class PageDraftTreeNodeWrapper extends TreeNodeWrapper {

	public PageDraftTreeNodeWrapper(ITreeNode node) {
		super(node);
		this.entity =  ((DraftPageNode)node).getEntity();
	}

	public PageDraftTreeNodeWrapper(ITreeNode tree, String currentLang) {
		super(tree, currentLang);
		this.entity =  ((DraftPageNode) tree).getEntity();
	}
	
	
	
	public Object getEntity() {
		return entity;
	}
	public void setEntity(Object entity) {
		this.entity = entity;
	}
	
	private Object entity;
}
