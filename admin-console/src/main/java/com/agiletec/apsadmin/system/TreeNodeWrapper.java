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
import com.agiletec.aps.system.common.tree.ITreeNodeManager;
import com.agiletec.aps.system.common.tree.TreeNode;

/**
 * @author E.Santoboni
 */
public class TreeNodeWrapper extends TreeNode {

	protected TreeNodeWrapper() {
	}

	public TreeNodeWrapper(ITreeNode node, ITreeNode parent) {
		this.setCode(node.getCode());
		this.setParentCode(node.getParentCode());
		Set<Object> codes = node.getTitles().keySet();
		Iterator<Object> iterKey = codes.iterator();
		while (iterKey.hasNext()) {
			String key = (String) iterKey.next();
			String title = node.getTitles().getProperty(key);
			this.getTitles().put(key, title);
		}
		this._empty = (null == node.getChildrenCodes() || node.getChildrenCodes().length == 0);
		this.setParent(parent);
	}

	public TreeNodeWrapper(ITreeNode tree, ITreeNode parent, String currentLang, ITreeNodeManager treeNodeManager) {
		this(tree, parent);
		this.setTitle(tree.getTitle(currentLang));
		this.setFullTitle(tree.getFullTitle(currentLang, treeNodeManager));
		this.setShortFullTitle(tree.getShortFullTitle(currentLang, treeNodeManager));
	}

	public boolean isOpen() {
		return _open;
	}

	public void setOpen(boolean open) {
		this._open = open;
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

    public ITreeNode getParent() {
        return parent;
    }

    public void setParent(ITreeNode parent) {
        this.parent = parent;
    }

	public ITreeNode[] getChildren() {
		return _children;
	}

	public void setChildren(ITreeNode[] children) {
		this._children = children;
	}

	public void addChild(ITreeNode treeNode) {
		int len = this._children.length;
		ITreeNode[] newChildren = new ITreeNode[len + 1];
		for (int i = 0; i < len; i++) {
			newChildren[i] = this._children[i];
		}
		newChildren[len] = treeNode;
		this._children = newChildren;
	}

	private boolean _empty;
	private boolean _open;

	private String _title;
	private String _fullTitle;
	private String _shortFullTitle;
    
    private ITreeNode parent;

	private ITreeNode[] _children = new ITreeNode[0];

}
