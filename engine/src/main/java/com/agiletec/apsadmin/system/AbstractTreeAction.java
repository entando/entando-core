/*
*
* Copyright 2013 Entando S.r.l. (http://www.entando.com) All rights reserved.
*
* This file is part of Entando software.
* Entando is a free software;
* You can redistribute it and/or modify it
* under the terms of the GNU General Public License (GPL) as published by the Free Software Foundation; version 2.
* 
* See the file License for the specific language governing permissions   
* and limitations under the License
* 
* 
* 
* Copyright 2013 Entando S.r.l. (http://www.entando.com) All rights reserved.
*
*/
package com.agiletec.apsadmin.system;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agiletec.aps.system.common.tree.ITreeNode;

/**
 * Abstract Action class which handles tree. 
 * @author E.Santoboni
 */
public abstract class AbstractTreeAction extends BaseAction implements ITreeAction {

	private static final Logger _logger = LoggerFactory.getLogger(AbstractTreeAction.class);
	
	@Override
	public String buildTree() {
		Set<String> targets = this.getTreeNodesToOpen();
		try {
			String marker = this.getTreeNodeActionMarkerCode();
			if (null != marker) {
				if (null != marker && marker.equalsIgnoreCase(ACTION_MARKER_OPEN)) {
					targets = this.getTreeHelper().checkTargetNodes(this.getTargetNode(), targets, this.getNodeGroupCodes());
				} else if (null != marker && marker.equalsIgnoreCase(ACTION_MARKER_CLOSE)) {
					targets = this.getTreeHelper().checkTargetNodesOnClosing(this.getTargetNode(), targets, this.getNodeGroupCodes());
				}
			}
			this.setTreeNodesToOpen(targets);
		} catch (Throwable t) {
			_logger.error("error in buildTree", t);
			//ApsSystemUtils.logThrowable(t, this, "buildTree");
			return FAILURE;
		}
		return SUCCESS;
	}
	
	@Override
	public ITreeNode getShowableTree() {
		ITreeNode node = null;
		try {
			ITreeNode allowedTree = this.getAllowedTreeRootNode();
			node = this.getTreeHelper().getShowableTree(this.getTreeNodesToOpen(), allowedTree, this.getNodeGroupCodes());
		} catch (Throwable t) {
			_logger.error("error in getShowableTree", t);
			//ApsSystemUtils.logThrowable(t, this, "getShowableTree");
		}
		return node;
	}
	
	@Override
	public ITreeNode getAllowedTreeRootNode() {
		ITreeNode node = null;
		try {
			node = this.getTreeHelper().getAllowedTreeRoot(this.getNodeGroupCodes());
		} catch (Throwable t) {
			_logger.error("error in getAllowedTreeRootNode", t);
			//ApsSystemUtils.logThrowable(t, this, "getAllowedTreeRootNode");
		}
		return node;
	}
	
	/**
	 * Return the allowed codes of the group of the nodes to manage.
	 * This method has to be extended if the helper manage tree nodes with authority.
	 * @return The allowed group codes.
	 */
	protected Collection<String> getNodeGroupCodes() {
		return null;
	}
	
	public String getTargetNode() {
		return _targetNode;
	}
	public void setTargetNode(String targetNode) {
		this._targetNode = targetNode;
	}
	
	public Set<String> getTreeNodesToOpen() {
		return _treeNodesToOpen;
	}
	public void setTreeNodesToOpen(Set<String> treeNodesToOpen) {
		this._treeNodesToOpen = treeNodesToOpen;
	}
	
	public String getTreeNodeActionMarkerCode() {
		return _treeNodeActionMarkerCode;
	}
	public void setTreeNodeActionMarkerCode(String treeNodeActionMarkerCode) {
		this._treeNodeActionMarkerCode = treeNodeActionMarkerCode;
	}
	
	protected ITreeNodeBaseActionHelper getTreeHelper() {
		return _treeHelper;
	}
	public void setTreeHelper(ITreeNodeBaseActionHelper treeHelper) {
		this._treeHelper = treeHelper;
	}
	
	private String _targetNode;
	private Set<String> _treeNodesToOpen = new HashSet<String>();
	
	private String _treeNodeActionMarkerCode;
	
	private ITreeNodeBaseActionHelper _treeHelper;
	
}