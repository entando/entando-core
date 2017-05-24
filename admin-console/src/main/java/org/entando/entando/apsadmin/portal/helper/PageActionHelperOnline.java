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
package org.entando.entando.apsadmin.portal.helper;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.entando.entando.apsadmin.portal.node.DraftPageNode;
import org.entando.entando.apsadmin.portal.node.OnlinePageNode;
import org.entando.entando.apsadmin.portal.node.PageOnlineTreeNodeWrapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agiletec.aps.system.common.tree.ITreeNode;
import com.agiletec.aps.system.common.tree.TreeNode;
import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.aps.system.services.group.Group;
import com.agiletec.aps.system.services.page.IPage;
import com.agiletec.aps.system.services.page.PageMetadata;
import com.agiletec.apsadmin.portal.helper.AbstractPageActionHelper;
import com.agiletec.apsadmin.system.TreeNodeWrapper;

/**
 *
 */
public class PageActionHelperOnline extends AbstractPageActionHelper {

	private static final Logger _logger = LoggerFactory.getLogger(PageActionHelperOnline.class);

	@Override
	protected IPage getPage(String pageCode) {
		return this.getPageManager().getOnlinePage(pageCode);
	}

	@Override
	protected PageMetadata getPageMetadata(IPage page) {
		return page.getMetadata();
	}

	@Override
	protected TreeNode createNodeInstance(IPage page) {
		return new OnlinePageNode(page);
	}

	@Override
	protected boolean isPageAllowed(IPage page, Collection<String> groupCodes, boolean alsoFreeViewPages) {
		return page != null && page.isOnline() && super.isPageAllowed(page, groupCodes, alsoFreeViewPages);
	}

	@Override
	public ITreeNode getAllowedTreeRoot(Collection<String> groupCodes, boolean alsoFreeViewPages) throws ApsSystemException {
		TreeNode root = null;
		IPage pageRoot = (IPage) this.getRoot();
		PageMetadata metadata = this.getPageMetadata(pageRoot);
		if (metadata != null && (groupCodes.contains(Group.ADMINS_GROUP_NAME) || groupCodes.contains(Group.FREE_GROUP_NAME)
				|| (alsoFreeViewPages && null != metadata.getExtraGroups() && metadata.getExtraGroups().contains(Group.FREE_GROUP_NAME)))) {
			root = this.createNodeInstance(pageRoot);
			this.fillTreeNode(root, null, pageRoot);
		} else {
			root = this.getVirtualRoot();
		}
		this.addTreeWrapper(root, null, pageRoot, groupCodes, alsoFreeViewPages);
		return root;
	}

	@Override
	public TreeNodeWrapper getShowableTree(Set<String> treeNodesToOpen, ITreeNode fullTree, Collection<String> groupCodes) throws ApsSystemException {
		if (null == treeNodesToOpen || treeNodesToOpen.isEmpty()) {
			_logger.warn("No selected nodes");
			return new PageOnlineTreeNodeWrapper(fullTree);
		}
		TreeNodeWrapper root = null;
		try {
			Set<String> checkNodes = new HashSet<String>();
			this.buildCheckNodes(treeNodesToOpen, checkNodes, groupCodes);
			root = new PageOnlineTreeNodeWrapper(((DraftPageNode) fullTree));
			root.setParent(root);
			this.builShowableTree(root, null, fullTree, checkNodes);
		} catch (Throwable t) {
			_logger.error("Error creating showable tree", t);
			throw new ApsSystemException("Error creating showable tree - draft", t);
		}
		return root;
	}

	private void buildCheckNodes(Set<String> treeNodesToOpen, Set<String> checkNodes, Collection<String> groupCodes) {
		if (null == treeNodesToOpen) {
			return;
		}
		Iterator<String> iter = treeNodesToOpen.iterator();
		while (iter.hasNext()) {
			String targetNode = (String) iter.next();
			ITreeNode treeNode = this.getTreeNode(targetNode);
			if (null != treeNode) {
				this.buildCheckNodes(treeNode, checkNodes, groupCodes);
			}
		}
	}

	private void builShowableTree(TreeNodeWrapper currentNode, TreeNodeWrapper parent, ITreeNode currentTreeNode, Set<String> checkNodes) {
		if (checkNodes.contains(currentNode.getCode())) {
			currentNode.setOpen(true);
			ITreeNode[] children = currentTreeNode.getChildren();
			for (int i = 0; i < children.length; i++) {
				ITreeNode newCurrentTreeNode = children[i];
				TreeNodeWrapper newNode = new PageOnlineTreeNodeWrapper(((DraftPageNode) newCurrentTreeNode));
				newNode.setParent(currentNode);
				currentNode.addChild(newNode);
				this.builShowableTree(newNode, currentNode, newCurrentTreeNode, checkNodes);
			}
		}
	}

}
