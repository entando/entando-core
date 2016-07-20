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

import java.util.Iterator;
import java.util.Set;

import com.agiletec.aps.system.common.tree.ITreeNode;
import com.agiletec.aps.system.common.tree.TreeNode;

/**
 * @author E.Santoboni
 */
public class TreeNodeWrapper extends TreeNode {
	
	public TreeNodeWrapper(ITreeNode node) {
		this.setCode(node.getCode());
		Set<Object> codes = node.getTitles().keySet();
		Iterator<Object> iterKey = codes.iterator();
		while (iterKey.hasNext()) {
			String key = (String) iterKey.next();
			String title = node.getTitles().getProperty(key);
			this.getTitles().put(key, title);
		}
		this._empty = (null == node.getChildren() || node.getChildren().length == 0);
		this.setParent(node.getParent());
	}
	
	public TreeNodeWrapper(ITreeNode tree, String currentLang) {
		this.setCode(tree.getCode());
		ITreeNode parent = tree.getParent();
		if (null != parent) {
			super.setParent(parent);
		}
		this.setTitle(tree.getTitle(currentLang));
		this.setFullTitle(tree.getFullTitle(currentLang));
		this.setShortFullTitle(tree.getShortFullTitle(currentLang));
	}
	
	public boolean isOpen() {
		return _open;
	}
	public void setOpen(boolean open) {
		this._open = open;
	}
	
	public String getParentCode() {
		if (null != super.getParent()) {
			return super.getParent().getCode();
		}
		return null;
	}
	
	public String getTitle() {
		return _title;
	}
	public void setTitle(String title) {
		this._title = title;
	}
	
	public String getFullTitle() {
		return _fullTitle;
	}
	public void setFullTitle(String fullTitle) {
		this._fullTitle = fullTitle;
	}
	
	public String getShortFullTitle() {
		return _shortFullTitle;
	}
	public void setShortFullTitle(String shortFullTitle) {
		this._shortFullTitle = shortFullTitle;
	}
	
	public boolean isEmpty() {
		return _empty;
	}
	public void setEmpty(boolean empty) {
		this._empty = empty;
	}
	
	private boolean _empty;
	private boolean _open;
	
	private String _title;
	private String _fullTitle;
	private String _shortFullTitle;
	
}
