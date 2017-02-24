package com.agiletec.apsadmin.system;

import com.agiletec.aps.system.common.tree.TreeNode;
import com.agiletec.aps.util.ApsProperties;

public abstract class CustomTreeNode<T extends CustomTreeNodeInterface> extends TreeNode implements CustomTreeNodeInterface {

	public CustomTreeNode(T entity) {
		this.entity = entity;
	}

	
	@Override
	public String getCode() {
		return this.getEntity().getCode();
	}

	@Override
	public boolean isRoot() {
		return (null == this.getParent() || this.getCode().equals(this.getParent().getCode()));
	}

	
	@Override
	public CustomTreeNodeInterface getParent() {
		return entity.getParent();
	}

	@Override
	public CustomTreeNodeInterface[] getChildren() {
		return entity.getChildren();
	}

	@Override
	public int getPosition() {
		return entity.getPosition();
	}

	@Override
	public String getGroup() {
		return entity.getGroup();
	}

	@Override
	public ApsProperties getTitles() {
		return entity.getTitles();
	}

	@Override
	public String getTitle(String langCode) {
		return this.getTitles().getProperty(langCode);
	}

	@Override
	public void setTitle(String langCode, String title) {
		this.getTitles().setProperty(langCode, title);
	}

	@Override
	public String getFullTitle(String langCode) {
		return this.getFullTitle(langCode, " / ");
	}

	@Override
	public String getShortFullTitle(String langCode) {
		return this.getShortFullTitle(langCode, " / ");
	}

	@Override
	public String getShortFullTitle(String langCode, String separator) {
		return this.getFullTitle(langCode, separator, true);
	}

	@Override
	public String getFullTitle(String langCode, String separator) {
		return this.getFullTitle(langCode, separator, false);
	}

	@Override
	public String getPath() {
		return this.getPath("/", true);
	}
	
	@Override
	public String getPath(String separator, boolean addRoot) {
		String[] pathArray = this.getPathArray(addRoot);
		StringBuilder path = new StringBuilder();
		for (int i = 0; i < pathArray.length; i++) {
			String code = pathArray[i];
			if (i>0) path.append(separator);
			path.append(code);
		}
		return path.toString();
	}
	
	@Override
	public String[] getPathArray() {
		return this.getPathArray(true);
	}
	
	@Override
	public String[] getPathArray(boolean addRoot) {
		String[] pathArray = new String[0];
		if (this.isRoot() && !addRoot) return pathArray;
		pathArray = this.addSubPath(pathArray, this.getCode());
		if (this.isRoot()) return pathArray;
		CustomTreeNodeInterface parent = this.getParent();
		while (parent != null) {
			if (parent.isRoot() && !addRoot) return pathArray;
			pathArray = this.addSubPath(pathArray, parent.getCode());
			if (parent.isRoot()) return pathArray;
			parent = parent.getParent();
		}
		return pathArray;
	}
	
	@Override
	public boolean isChildOf(String nodeCode) {
		return this.isChildOf(this, nodeCode);
	}
	
	private boolean isChildOf(CustomTreeNodeInterface node, String nodeCode) {
		if (node.getCode().equals(nodeCode)) {
			return true;
		} else {
			CustomTreeNodeInterface parent = node.getParent();
			if (parent != null && !parent.getCode().equals(node.getCode())) {
				return this.isChildOf(parent, nodeCode);
			} else {
				return false;
			}
		}
	}

	
	private String getFullTitle(String langCode, String separator, boolean shortTitle) {
		String title = this.getTitles().getProperty(langCode);
		if (null == title) title = this.getCode();
		if (this.isRoot()) return title;
		CustomTreeNodeInterface parent = this.getParent();
		while (parent != null && parent.getParent() != null) {
			String parentTitle = "..";
			if (!shortTitle) {
				parentTitle = parent.getTitles().getProperty(langCode);
				if (null == parentTitle) parentTitle = parent.getCode();
			}
			title = parentTitle + separator + title;
			if (parent.isRoot()) return title;
			parent = parent.getParent();
		}
		return title;
	}

	private String[] addSubPath(String[] pathArray, String subPath) {
		int len = pathArray.length;
		String[] newPath = new String[len + 1];
		newPath[0] = subPath;
		for (int i=0; i < len; i++) {
			newPath[i+1] = pathArray[i];
		}
		return newPath;
	}
	
	public T getEntity() {
		return entity;
	}

	public void setEntity(T entity) {
		this.entity = entity;
	}


	public CustomTreeNodeInterface[] getNodeChildren() {
		return nodeChildren;
	}

	public void setNodeChildren(CustomTreeNodeInterface[] nodeChildren) {
		this.nodeChildren = nodeChildren;
	}

	private T entity;
	private CustomTreeNodeInterface[] nodeChildren = new CustomTreeNodeInterface[0];
}

