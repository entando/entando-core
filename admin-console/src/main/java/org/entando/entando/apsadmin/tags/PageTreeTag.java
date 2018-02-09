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
package org.entando.entando.apsadmin.tags;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import javax.servlet.jsp.JspException;

import org.apache.struts2.views.jsp.StrutsBodyTagSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agiletec.aps.system.common.tree.ITreeNode;
import com.agiletec.aps.util.ApsWebApplicationUtils;
import com.agiletec.apsadmin.portal.helper.IPageActionHelper;
import com.agiletec.apsadmin.system.ITreeNodeBaseActionHelper;
import com.opensymphony.xwork2.util.ValueStack;

public class PageTreeTag extends StrutsBodyTagSupport {
	
	private static final Logger _logger = LoggerFactory.getLogger(PageTreeTag.class);
	
	@Override
	public int doStartTag() throws JspException {
		try {
			IPageActionHelper helper = this.getHelper(this.isOnline());
			ITreeNode root = this.getAllowedTreeRootNode(helper);
			ValueStack stack = this.getStack();
			stack.getContext().put(this.getVar(), root);
            stack.setValue("#attr['" + this.getVar() + "']", root, false);
		} catch (Throwable t) {
        	_logger.error("error in doStartTag", t);
            throw new JspException("Error during tag initialization", t);
		}
		return super.doStartTag();
	}
	
	@Override
	public void release() {
		super.release();
		this.setVar(null);
		this.setOnline(false);
		this.setOnDemand(false);
		this.setOpen(null);
		this.setTargetNode(null);
		this.setTreeNodesToOpen(null);
		this.setAllowedGroups(null);
	}
	
	protected IPageActionHelper getHelper(boolean online) {
		String beanName = online ? "pageActionHelperOnline" : "pageActionHelperDraft";
		IPageActionHelper helper = (IPageActionHelper) ApsWebApplicationUtils.getBean(beanName, this.pageContext);
		return helper;
	}
	
	public Set<String> getTargetNodes(Collection<String> allowedGroups, ITreeNodeBaseActionHelper helper) {
		Set<String> targets = this.convertCollection(this.getTreeNodesToOpen());
		try {
			String targetNode = this.getTargetNode();
			Boolean open = this.getOpen();
			if (open != null && !open) {
				targets = helper.checkTargetNodesOnClosing(targetNode, targets, allowedGroups);
			} else {
				targets = helper.checkTargetNodes(targetNode, targets, allowedGroups);
			}
		} catch (Throwable t) {
			_logger.error("error in buildTree", t);
		}
		return targets;
	}
	
	public ITreeNode getAllowedTreeRootNode(ITreeNodeBaseActionHelper helper) {
		ITreeNode root = null;
		try {
			root = helper.getAllowedTreeRoot(this.getAllowedGroups());
			if (this.isOnDemand()) {
				Collection<String> allowedGroups = this.convertCollection(this.getAllowedGroups());
				root = helper.getShowableTree(this.getTargetNodes(allowedGroups, helper), root, allowedGroups);
			}
		} catch (Throwable t) {
			_logger.error("error in getAllowedTreeRootNode", t);
		}
		return root;
	}
	
	private Set<String> convertCollection(Collection items) {
		Set<String> itemsSet = new HashSet<String>();
		if (items != null) {
			for (Object current : items) {
				itemsSet.add(current.toString());
			}
		}
		return itemsSet;
	}
	
	/**
	 * Return the name used to reference the required object (or one of its property) pushed into the Value Stack.
	 * @return The name used to reference the required object.
	 */
	protected String getVar() {
		return _var;
	}
	
	/**
	 * Set the name used to reference the required object (or one of its property) pushed into the Value Stack.
	 * @param var The name of the variable
	 */
	public void setVar(String var) {
		this._var = var;
	}
	
	public boolean isOnline() {
		return _online;
	}
	public void setOnline(boolean online) {
		this._online = online;
	}
	
	public boolean isOnDemand() {
		return _onDemand;
	}
	public void setOnDemand(boolean onDemand) {
		this._onDemand = onDemand;
	}
	
	public Boolean getOpen() {
		return _open;
	}
	public void setOpen(Boolean open) {
		this._open = open;
	}
	
	public String getTargetNode() {
		return _targetNode;
	}
	public void setTargetNode(String targetNode) {
		this._targetNode = targetNode;
	}
	
	public Collection getTreeNodesToOpen() {
		return _treeNodesToOpen;
	}
	public void setTreeNodesToOpen(Collection treeNodesToOpen) {
		this._treeNodesToOpen = treeNodesToOpen;
	}
	
	public Collection getAllowedGroups() {
		return _allowedGroups;
	}
	public void setAllowedGroups(Collection allowedGroups) {
		this._allowedGroups = allowedGroups;
	}
	
	private String _var;
	private boolean _online = false;
	private boolean _onDemand = false;
	private Boolean _open;
	private String _targetNode;
	private Collection _treeNodesToOpen;
	private Collection _allowedGroups;
	
}
