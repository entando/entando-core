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
package com.agiletec.apsadmin.portal.helper;

import com.agiletec.aps.system.common.tree.ITreeNode;
import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.aps.system.services.baseconfig.ConfigInterface;
import com.agiletec.aps.system.services.group.Group;
import com.agiletec.aps.system.services.lang.Lang;
import com.agiletec.aps.system.services.page.IPage;
import com.agiletec.aps.system.services.page.IPageManager;
import com.agiletec.aps.system.services.page.PageUtilizer;
import com.agiletec.aps.system.services.role.Permission;
import com.agiletec.aps.util.ApsWebApplicationUtils;
import com.agiletec.apsadmin.portal.AbstractPortalAction;
import com.agiletec.apsadmin.system.TreeNodeBaseActionHelper;
import com.agiletec.apsadmin.system.TreeNodeWrapper;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.servlet.http.HttpServletRequest;

import org.entando.entando.aps.system.services.actionlog.model.ActivityStreamInfo;
import org.entando.entando.apsadmin.portal.node.PageTreeNodeWrapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Main abstract hepler for pages handling
 *
 * @author E.Santoboni -S.Puddu
 */
public abstract class AbstractPageActionHelper extends TreeNodeBaseActionHelper implements IPageActionHelper {

	private static final Logger _logger = LoggerFactory.getLogger(AbstractPageActionHelper.class);

	protected abstract IPage getPage(String pageCode);

	@Override
	protected abstract IPage getRoot();

	@Override
	public Map getReferencingObjects(IPage page, HttpServletRequest request) throws ApsSystemException {
		Map<String, List> references = new HashMap<String, List>();
		try {
			String[] defNames = ApsWebApplicationUtils.getWebApplicationContext(request).getBeanNamesForType(PageUtilizer.class);
			for (int i = 0; i < defNames.length; i++) {
				Object service = null;
				try {
					service = ApsWebApplicationUtils.getWebApplicationContext(request).getBean(defNames[i]);
				} catch (Throwable t) {
					_logger.error("error in hasReferencingObjects", t);
					service = null;
				}
				if (service != null) {
					PageUtilizer pageUtilizer = (PageUtilizer) service;
					List utilizers = pageUtilizer.getPageUtilizers(page.getCode());
					if (utilizers != null && !utilizers.isEmpty()) {
						references.put(pageUtilizer.getName() + "Utilizers", utilizers);
					}
				}
			}
		} catch (Throwable t) {
			throw new ApsSystemException("Error extracting Referencing Objects", t);
		}
		return references;
	}

	@Override
	public ITreeNode getAllowedTreeRoot(Collection<String> groupCodes) throws ApsSystemException {
		return this.getAllowedTreeRoot(groupCodes, false);
	}

	@Override
	public ITreeNode getAllowedTreeRoot(Collection<String> userGroupCodes, boolean alsoFreeViewPages) throws ApsSystemException {
		PageTreeNodeWrapper root = null;
		IPage pageRoot = (IPage) this.getRoot();
		if (userGroupCodes.contains(Group.FREE_GROUP_NAME) || userGroupCodes.contains(Group.ADMINS_GROUP_NAME)
				|| (alsoFreeViewPages && null != pageRoot.getExtraGroups() && pageRoot.getExtraGroups().contains(Group.FREE_GROUP_NAME))) {
			root = new PageTreeNodeWrapper(this.getRoot());
		} else {
			root = this.getVirtualRoot();
		}
		this.addTreeWrapper(root, pageRoot, userGroupCodes, alsoFreeViewPages);
		return root;
	}

	private void addTreeWrapper(PageTreeNodeWrapper currentNode, IPage currentTreeNode, Collection<String> userGroupCodes, boolean alsoFreeViewPages) {
		IPage[] children = currentTreeNode.getChildren();
		for (int i = 0; i < children.length; i++) {
			IPage newCurrentTreeNode = children[i];
			if (this.isPageAllowed(newCurrentTreeNode, userGroupCodes, alsoFreeViewPages)) {
				PageTreeNodeWrapper newNode = new PageTreeNodeWrapper(newCurrentTreeNode);
				currentNode.addChild(newNode);
				this.addTreeWrapper(newNode, newCurrentTreeNode, userGroupCodes, alsoFreeViewPages);
			} else {
				this.addTreeWrapper(currentNode, newCurrentTreeNode, userGroupCodes, alsoFreeViewPages);
			}
		}
	}

	/**
	 * Metodo a servizio della costruzione dell'albero delle pagine. Nel caso
	 * che l'utente corrente non sia abilitato alla visualizzazione del nodo
	 * root, fornisce un nodo "virtuale" nel quale inserire gli eventuali nodi
	 * visibili.
	 *
	 * @return Il nodo root virtuale.
	 */
	private PageTreeNodeWrapper getVirtualRoot() {
		PageTreeNodeWrapper virtualRoot = new PageTreeNodeWrapper();
		virtualRoot.setCode(AbstractPortalAction.VIRTUAL_ROOT_CODE);
		List<Lang> langs = this.getLangManager().getLangs();
		for (int i = 0; i < langs.size(); i++) {
			Lang lang = langs.get(i);
			virtualRoot.setTitle(lang.getCode(), "ROOT");
		}
		return virtualRoot;
	}

	@Override
	protected void buildCheckNodes(ITreeNode treeNode, Set<String> nodesToShow, Collection<String> groupCodes) {
		nodesToShow.add(treeNode.getCode());
		ITreeNode parent = treeNode.getParent();
		if (parent == null) {
			return;
		}
		IPage page = this.getPage(parent.getCode());
		if (!this.isPageAllowed(page, groupCodes, false)) {
			nodesToShow.add(AbstractPortalAction.VIRTUAL_ROOT_CODE);
			return;
		}
		if (parent.getParent() != null
				&& !parent.getCode().equals(treeNode.getCode())) {
			this.buildCheckNodes(parent, nodesToShow, groupCodes);
		}
	}

	protected boolean isPageAllowed(IPage page, Collection<String> groupCodes, boolean alsoFreeViewPages) {
		if (page == null) {
			return false;
		}
		String pageGroup = page.getGroup();
		Collection<String> extraGroups = page.getExtraGroups();
		boolean isAuth = (groupCodes.contains(pageGroup) || groupCodes.contains(Group.ADMINS_GROUP_NAME))
				|| (alsoFreeViewPages && null != extraGroups && extraGroups.contains(Group.FREE_GROUP_NAME));
		return isAuth;
	}

	@Override
	protected boolean isNodeAllowed(String code, Collection<String> groupCodes) {
		if (null != code && code.equals(AbstractPortalAction.VIRTUAL_ROOT_CODE)) {
			return true;
		}
		IPage page = this.getPage(code);
		return this.isPageAllowed(page, groupCodes, false);
	}

	@Override
	protected ITreeNode getTreeNode(String code) {
		if (AbstractPortalAction.VIRTUAL_ROOT_CODE.equals(code)) {
			return this.getVirtualRoot();
		}
		return this.getPage(code);
	}

	@Override
	public ActivityStreamInfo createActivityStreamInfo(IPage page,
			int strutsAction, boolean addLink, String entryPageAction) {
		ActivityStreamInfo asi = this.createBaseActivityStreamInfo(page, strutsAction, addLink);
		if (addLink) {
			asi.setLinkNamespace("/do/Page");
			asi.setLinkActionName(entryPageAction);
			asi.addLinkParameter("selectedNode", page.getCode());
		}
		return asi;
	}

	@Override
	public ActivityStreamInfo createConfigFrameActivityStreamInfo(IPage page,
			int framePos, int strutsAction, boolean addLink) {
		ActivityStreamInfo asi = this.createBaseActivityStreamInfo(page, strutsAction, addLink);
		if (addLink) {
			asi.setLinkNamespace("/do/Page");
			asi.setLinkActionName("editFrame");
			asi.addLinkParameter("pageCode", page.getCode());
			asi.addLinkParameter("frame", String.valueOf(framePos));
		}
		return asi;
	}

	private ActivityStreamInfo createBaseActivityStreamInfo(IPage page, int strutsAction, boolean addLink) {
		ActivityStreamInfo asi = new ActivityStreamInfo();
		asi.setActionType(strutsAction);
		asi.setObjectTitles(page.getTitles());
		List<String> groupCodes = new ArrayList<String>();
		groupCodes.add(page.getGroup());
		if (null != page.getExtraGroups()) {
			groupCodes.addAll(page.getExtraGroups());
		}
		asi.setGroups(groupCodes);
		if (addLink) {
			asi.setLinkAuthGroup(page.getGroup());
			asi.setLinkAuthPermission(Permission.MANAGE_PAGES);
		}
		return asi;
	}

	@Override
	protected TreeNodeWrapper buildWrapper(ITreeNode treeNode) {
		if (treeNode instanceof IPage) {
			return new PageTreeNodeWrapper((IPage) treeNode);
		} else {
			if (AbstractPortalAction.VIRTUAL_ROOT_CODE.equals(treeNode.getCode())) {
				return this.getVirtualRoot();
			}
			IPage page = this.getPage(treeNode.getCode());
			return new PageTreeNodeWrapper(page);
		}
	}

	protected IPageManager getPageManager() {
		return _pageManager;
	}

	public void setPageManager(IPageManager pageManager) {
		this._pageManager = pageManager;
	}

	protected ConfigInterface getConfigService() {
		return _configService;
	}

	public void setConfigService(ConfigInterface configService) {
		this._configService = configService;
	}

	private IPageManager _pageManager;
	private ConfigInterface _configService;

}
