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
package com.agiletec.aps.system.common.tree;

import java.io.Serializable;

import com.agiletec.aps.util.ApsProperties;

/**
 * A node of a tree. The node is the basic information a tree and contains all
 * the minimum information necessary for its definition.
 *
 * @author E.Santoboni
 */
public class TreeNode implements ITreeNode, Serializable {

	@Override
	public String getCode() {
		return _code;
	}

	public void setCode(String code) {
		this._code = code;
	}

	@Override
	public boolean isRoot() {
		return (null == this.getParent() || this.getCode().equals(this.getParent().getCode()));
	}

	@Override
	public ITreeNode getParent() {
		return _parent;
	}

	public void setParent(ITreeNode parent) {
		this._parent = parent;
	}

	/**
	 * Return the group code this node belongs to
	 *
	 * @return the authorization group
	 */
	@Override
	public String getGroup() {
		return _group;
	}

	/**
	 * Set the group code of this node
	 *
	 * @param group The group to assign this node to
	 */
	public void setGroup(String group) {
		this._group = group;
	}

	@Override
	public String[] getChildrenCodes() {
		return _childrenCodes;
	}

	public void setChildrenCodes(String[] childrenCodes) {
		this._childrenCodes = childrenCodes;
	}

	/**
	 * Adds a node to nodes in a lower level. The new node is inserted in the
	 * final position.
	 *
	 * @param treeNode The node to add.
	 */
	public void addChildCode(String treeNode) {
		int len = this._childrenCodes.length;
		String[] newChildren = new String[len + 1];
		for (int i = 0; i < len; i++) {
			newChildren[i] = this._childrenCodes[i];
		}
		newChildren[len] = treeNode;
		this._childrenCodes = newChildren;
	}

	@Override
	public int getPosition() {
		return _position;
	}

	protected void setPosition(int position) {
		this._position = position;
	}

	@Override
	public ApsProperties getTitles() {
		return _titles;
	}

	/**
	 * Set the titles of the node.
	 *
	 * @param titles A set of properties with the titles, where the keys are the
	 * codes of language.
	 */
	public void setTitles(ApsProperties titles) {
		this._titles = titles;
	}

	@Override
	public void setTitle(String langCode, String title) {
		this.getTitles().setProperty(langCode, title);
	}

	@Override
	public String getTitle(String langCode) {
		return this.getTitles().getProperty(langCode);
	}

	@Override
	public String getFullTitle(String langCode) {
		return this.getFullTitle(langCode, " / ");
	}

	@Override
	public String getFullTitle(String langCode, String separator) {
		return this.getFullTitle(langCode, separator, false);
	}

	@Override
	public String getShortFullTitle(String langCode) {
		return this.getShortFullTitle(langCode, " / ");
	}

	@Override
	public String getShortFullTitle(String langCode, String separator) {
		return this.getFullTitle(langCode, separator, true);
	}

	/**
	 * Create a full title of the code of the current page. The full title (breadcrumb) 
	 * consists of the set of page titles (from the characters ".." if shortTitle = true) 
	 * starting from the root up to the current page, and it is created by moving 
	 * backwards in the page structure starting from the node current.
	 * @param langCode The language code to extract the title of each node
	 * @param separator The separator between the nodes
	 * @param shortTitle defines whether to return the full title in the form "short" (each title of the relative replaced with "..")
	 * @return The required full title.
	 */
	protected String getFullTitle(String langCode, String separator, boolean shortTitle) {
		String title = this.getTitles().getProperty(langCode);
		if (null == title) {
			title = this.getCode();
		}
		if (this.isRoot()) {
			return title;
		}
		ITreeNode parent = this.getParent();
		while (parent != null && parent.getParent() != null) {
			String parentTitle = "..";
			if (!shortTitle) {
				parentTitle = parent.getTitles().getProperty(langCode);
				if (null == parentTitle) {
					parentTitle = parent.getCode();
				}
			}
			title = parentTitle + separator + title;
			if (parent.isRoot()) {
				return title;
			}
			parent = parent.getParent();
		}
		return title;
	}

	/**
	 * Returns the path of the single node. 
	 * The path is composed by node codes (separated by "/") starting 
	 * from the root up to the current node
	 * @return the path of the single node.
	 */
	@Override
	public String getPath() {
		return this.getPath("/", true);
	}

	/**
	 * Returns the path of the single node. 
	 * The path is composed by node codes (separated by the given separator) starting 
	 * from the root up to the current node
	 * @param separator The separator to use to divide the node codes
	 * @param addRoot if true, the path starts with the code of the root node
	 * @return the required path.
	 */
	@Override
	public String getPath(String separator, boolean addRoot) {
		String[] pathArray = this.getPathArray(addRoot);
		StringBuilder path = new StringBuilder();
		for (int i = 0; i < pathArray.length; i++) {
			String code = pathArray[i];
			if (i > 0) {
				path.append(separator);
			}
			path.append(code);
		}
		return path.toString();
	}

	/**
	 * Returns the path of the single node. The separator between the node will
	 * be '/' and the path contains the root node.
	 *
	 * @return the path of the single node.
	 */
	@Override
	public String[] getPathArray() {
		return this.getPathArray(true);
	}

	/**
	 * Returns the path array of the current node. The array in composed 
	 * by node codes from the root up to the current node
	 * @param addRoot if true, the array starts with the code of the root node
	 * @return the required path array of the single node.
	 */
	@Override
	public String[] getPathArray(boolean addRoot) {
		String[] pathArray = new String[0];
		if (this.isRoot() && !addRoot) {
			return pathArray;
		}
		pathArray = this.addSubPath(pathArray, this.getCode());
		if (this.isRoot()) {
			return pathArray;
		}
		ITreeNode parent = this.getParent();
		while (parent != null) {
			if (parent.isRoot() && !addRoot) {
				return pathArray;
			}
			pathArray = this.addSubPath(pathArray, parent.getCode());
			if (parent.isRoot()) {
				return pathArray;
			}
			parent = parent.getParent();
		}
		return pathArray;
	}

	private String[] addSubPath(String[] pathArray, String subPath) {
		int len = pathArray.length;
		String[] newPath = new String[len + 1];
		newPath[0] = subPath;
		for (int i = 0; i < len; i++) {
			newPath[i + 1] = pathArray[i];
		}
		return newPath;
	}

	@Override
	public boolean isChildOf(String nodeCode) {
		return this.isChildOf(this, nodeCode);
	}

	private boolean isChildOf(ITreeNode node, String nodeCode) {
		if (node.getCode().equals(nodeCode)) {
			return true;
		} else {
			ITreeNode parent = node.getParent();
			if (parent != null && !parent.getCode().equals(node.getCode())) {
				return this.isChildOf(parent, nodeCode);
			} else {
				return false;
			}
		}
	}

	@Override
	public String toString() {
		return "Node: " + this.getCode();
	}

	private String _code;

	private ITreeNode _parent;

	private String _group;

	private String[] _childrenCodes = new String[0];

	private int _position = -1;

	private ApsProperties _titles = new ApsProperties();

}
