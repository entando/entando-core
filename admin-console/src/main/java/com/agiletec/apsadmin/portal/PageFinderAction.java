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
package com.agiletec.apsadmin.portal;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.commons.beanutils.BeanComparator;
import org.entando.entando.apsadmin.portal.rs.model.PageJO;
import org.entando.entando.apsadmin.portal.rs.model.PagesStatusResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agiletec.aps.system.common.tree.ITreeNode;
import com.agiletec.aps.system.services.group.Group;
import com.agiletec.aps.system.services.page.IPage;
import com.agiletec.aps.system.services.page.PageUtils;
import com.agiletec.aps.system.services.page.PagesStatus;
import com.agiletec.aps.system.services.user.UserDetails;
import com.agiletec.apsadmin.portal.helper.IPageActionHelper;
import com.agiletec.apsadmin.system.ITreeAction;
import com.agiletec.apsadmin.system.ITreeNodeBaseActionHelper;
import org.entando.entando.apsadmin.portal.node.PageTreeNodeWrapper;

/**
 * This action class contains all the elements currently use to perform searches
 * across the portal pages.
 *
 * @author M.E. Minnai
 */
public class PageFinderAction extends AbstractPortalAction implements ITreeAction {

	private static final Logger _logger = LoggerFactory.getLogger(PageFinderAction.class);
	private static final int DEFAULT_LASTUPDATE_RESPONSE_SIZE = 5;

	public PagesStatusResponse getPagesStatusResponse() {
		PagesStatusResponse response = null;
		try {
			PagesStatus pagesStatus = this.getPageManager().getPagesStatus();
			response = new PagesStatusResponse(pagesStatus);
		} catch (Throwable t) {
			_logger.error("Error loading pagesStatus", t);
			throw new RuntimeException("Error loading pagesStatus", t);
		}
		return response;
	}

	public String getLastUpdated() {
		if (this.getLastUpdateResponseSize() < 1) {
			this.setLastUpdateResponseSize(DEFAULT_LASTUPDATE_RESPONSE_SIZE);
		}
		return SUCCESS;
	}

	public List<PageJO> getLastUpdatePagesResponse() {
		List<PageJO> response = null;
		try {
			List<IPage> pages = this.getPageManager().loadLastUpdatedPages(this.getLastUpdateResponseSize());
			if (null != pages && !pages.isEmpty()) {
				response = new ArrayList<PageJO>();
				Iterator<IPage> it = pages.iterator();
				while (it.hasNext()) {
					IPage page = it.next();
					PageJO pageJO = this.buildLastUpdateResponseItem(page);
					response.add(pageJO);
				}
			}
		} catch (Throwable t) {
			_logger.error("Error loading pagesStatus", t);
			throw new RuntimeException("Error loading pagesStatus", t);
		}
		return response;
	}

	protected PageJO buildLastUpdateResponseItem(IPage page) {
		PageJO pageJO = new PageJO();
		pageJO.setCode(page.getCode());
		pageJO.setRoot(page.isRoot());
		pageJO.setOnline(page.isOnline());
		pageJO.setChanged(page.isChanged());
		pageJO.setParentCode(page.getParentCode());
		pageJO.setGroup(page.getGroup());
		pageJO.setDraftMetadata(page.getMetadata());
		return pageJO;
	}

	public List<IPage> searchPages() {
		List<IPage> result = null;
		try {
			List<String> allowedGroupCodes = this.getAllowedGroupCodes();
			result = this.getPageManager().searchPages(this.getPageCodeToken(), allowedGroupCodes);
			BeanComparator comparator = new BeanComparator("code");
			Collections.sort(result, comparator);
			if (result != null) {
				if (result.size() > 0) {
					this.setFirstNode(result.get(0));
				}
				Collection<String> resultCodes = new ArrayList<>();
				for (IPage page : result) {
					resultCodes.add(page.getCode());
				}
				this.setResultCodes(resultCodes);
			}
		} catch (Throwable t) {
			_logger.error("Error on searching pages", t);
			throw new RuntimeException("Error on searching pages", t);
		}
		return result;
	}

	@Deprecated
	public List<IPage> getPagesFound() {
		return this.searchPages();
	}

	private List<String> getAllowedGroupCodes() {
		List<String> allowedGroups = new ArrayList<String>();
		UserDetails currentUser = this.getCurrentUser();
		List<Group> userGroups = this.getAuthorizationManager().getUserGroups(currentUser);
		Iterator<Group> iter = userGroups.iterator();
		while (iter.hasNext()) {
			Group group = iter.next();
			allowedGroups.add(group.getName());
		}
		return allowedGroups;
	}

	/**
	 * Set the token to be searched among the pages
	 *
	 * @param pageCodeToken The token to be searched among the pages
	 */
	public void setPageCodeToken(String pageCodeToken) {
		this._pageCodeToken = pageCodeToken;
	}

	/**
	 * Return the token to be searched among the pages
	 *
	 * @return The token to be searched among the pages
	 */
	public String getPageCodeToken() {
		return _pageCodeToken;
	}

	protected IPageActionHelper getPageActionHelper() {
		return _pageActionHelper;
	}

	public void setPageActionHelper(IPageActionHelper pageActionHelper) {
		this._pageActionHelper = pageActionHelper;
	}

	@Override
	public String buildTree() {
		Set<String> targets = this.getTreeNodesToOpen();
		try {
			this.searchPages();
			String marker = this.getTreeNodeActionMarkerCode();
			if (null != marker) {
				if (marker.equalsIgnoreCase(ACTION_MARKER_OPEN)) {
					targets = this.getTreeHelper().checkTargetNodes(this.getTargetNode(), targets, this.getActualAllowedGroupCodes());
				} else if (marker.equalsIgnoreCase(ACTION_MARKER_CLOSE)) {
					targets = this.getTreeHelper().checkTargetNodesOnClosing(this.getTargetNode(), targets, this.getActualAllowedGroupCodes());
				}
			}
			this.setTreeNodesToOpen(targets);
		} catch (Throwable t) {
			_logger.error("error in buildTree", t);
			return FAILURE;
		}
		return SUCCESS;
	}

	@Override
	public ITreeNode getShowableTree() {
		ITreeNode node = null;
		try {
			this.searchPages();
			ITreeNode allowedTree = this.getAllowedTreeRootNode();
			node = this.getTreeHelper().getShowableTree(this.getTreeNodesToOpen(), allowedTree, this.getActualAllowedGroupCodes());
		} catch (Throwable t) {
			_logger.error("error in getShowableTree", t);
		}
		return node;
	}

	@Override
	public ITreeNode getAllowedTreeRootNode() {
		PageTreeNodeWrapper root = null;
		try {
			if (null != this.getResultCodes() && this.getResultCodes().size() > 0) {
				IPage node = this.getPageManager().getDraftRoot();
				root = new PageTreeNodeWrapper(node);
				this.addTreeWrapper(root, node);
			}
		} catch (Throwable t) {
			_logger.error("error in getAllowedTreeRootNode", t);
		}
		return root;
	}

	private void addTreeWrapper(PageTreeNodeWrapper parent, IPage currentNode) {
		boolean isOnline = currentNode.isOnline();
		String[] children = currentNode.getChildrenCodes();
		if (null == children) {
			return;
		}
		for (int i = 0; i < children.length; i++) {
			IPage newCurrentNode = PageUtils.getPage(this.getPageManager(), isOnline, children[i]);
			if (null == newCurrentNode) {
				continue;
			}
			PageTreeNodeWrapper newNode = new PageTreeNodeWrapper(newCurrentNode);
			if (this.getResultCodes().contains(newCurrentNode.getCode())) {
				parent.addChildCode(newNode.getCode());
				this.addTreeWrapper(newNode, newCurrentNode);
			} else {
				this.addTreeWrapper(parent, newCurrentNode);
			}
		}
	}

	protected Collection<String> getResultCodes() {
		return _resultNodes;
	}

	protected void setResultCodes(Collection<String> result) {
		this._resultNodes = result;
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

	public String getCopyingPageCode() {
		return _copyingPageCode;
	}

	public ITreeNode getFirstNode() {
		return _firstNode;
	}

	public void setFirstNode(ITreeNode firstNode) {
		this._firstNode = firstNode;
	}

	public void setCopyingPageCode(String copyingPageCode) {
		this._copyingPageCode = copyingPageCode;
	}

	public ITreeNodeBaseActionHelper getTreeHelper() {
		return _treeHelper;
	}

	public void setTreeHelper(ITreeNodeBaseActionHelper treeHelper) {
		this._treeHelper = treeHelper;
	}

	public int getLastUpdateResponseSize() {
		return _lastUpdateResponseSize;
	}

	public void setLastUpdateResponseSize(int lastUpdateResponseSize) {
		this._lastUpdateResponseSize = lastUpdateResponseSize;
	}

	private String _pageCodeToken;
	private IPageActionHelper _pageActionHelper;

	private String _targetNode;
	private ITreeNode _firstNode;
	private Set<String> _treeNodesToOpen = new HashSet<String>();
	private Collection<String> _resultNodes;

	private String _treeNodeActionMarkerCode;

	private String _copyingPageCode;

	private ITreeNodeBaseActionHelper _treeHelper;
	private int _lastUpdateResponseSize;

}
