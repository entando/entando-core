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

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agiletec.aps.system.common.tree.ITreeNode;
import com.agiletec.aps.system.common.tree.TreeNode;
import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.aps.system.services.group.Group;
import com.agiletec.aps.system.services.lang.Lang;
import com.agiletec.aps.system.services.page.IPage;
import com.agiletec.aps.system.services.page.Page;
import com.agiletec.aps.util.ApsProperties;
import com.agiletec.apsadmin.portal.AbstractPortalAction;
import com.agiletec.apsadmin.system.DraftPageNode;

/**
 * Classe Helper per la gestione pagine.
 * @author E.Santoboni
 */
public class PageActionHelperDraft extends AbstractPageActionHelper {

	private static final Logger _logger = LoggerFactory.getLogger(PageActionHelperDraft.class);
	@Override
	public ITreeNode getAllowedTreeRoot(Collection<String> groupCodes, boolean alsoFreeViewPages) throws ApsSystemException {
		DraftPageNode root = null;
		//        if (groupCodes.contains(Group.ADMINS_GROUP_NAME)) {
		//            //XXX FIXME
		//        	//root = this.getRoot();
		//        } else {
		IPage pageRoot = (IPage) this.getRoot();
		if (groupCodes.contains(Group.FREE_GROUP_NAME)
				|| (alsoFreeViewPages && null != pageRoot.getExtraGroups() && pageRoot.getExtraGroups().contains(Group.FREE_GROUP_NAME))) {
			root = new DraftPageNode(this.getRoot());
			this.fillTreeNode((TreeNode) root, null, this.getRoot());
		} else {
			root = (DraftPageNode) this.getVirtualRoot();
		}
		this.addTreeWrapper(root, null, pageRoot, groupCodes, alsoFreeViewPages);
		// }
		return root;
	}

	private void addTreeWrapper(TreeNode currentNode, TreeNode parent, IPage currentTreeNode, Collection<String> groupCodes, boolean alsoFreeViewPages) {

		IPage[] children = currentTreeNode.getAllChildren();
		for (int i = 0; i < children.length; i++) {
			IPage newCurrentTreeNode = children[i];
			if (this.isPageAllowed(newCurrentTreeNode, groupCodes, alsoFreeViewPages)) {
				TreeNode newNode = new TreeNode();
				this.fillTreeNode(newNode, currentNode, newCurrentTreeNode);
				currentNode.addChild(new DraftPageNode(newCurrentTreeNode));
				this.addTreeWrapper(newNode, currentNode, newCurrentTreeNode, groupCodes, alsoFreeViewPages);
			} else {
				this.addTreeWrapper(currentNode, currentNode, newCurrentTreeNode, groupCodes, alsoFreeViewPages);
			}
		}
	}

	/**
	 * Metodo a servizio della costruzione dell'albero delle pagine. 
	 * Nel caso che l'utente corrente non sia abilitato alla visualizzazione del nodo 
	 * root, fornisce un nodo "virtuale" nel quale inserire gli eventuali nodi visibili.
	 * @return Il nodo root virtuale.
	 */
	private TreeNode getVirtualRoot() {
		Page virtualRoot = new Page();
		virtualRoot.setCode(AbstractPortalAction.VIRTUAL_ROOT_CODE);
		List<Lang> langs = this.getLangManager().getLangs();
		for (int i = 0; i < langs.size(); i++) {
			Lang lang = langs.get(i);
			virtualRoot.setTitle(lang.getCode(), "ROOT");
		}
		return new DraftPageNode(virtualRoot);
	}

	protected void fillTreeNode(TreeNode nodeToValue, TreeNode parent, ITreeNode realNode) {
		nodeToValue.setCode(realNode.getCode());
		if (null == parent) {
			nodeToValue.setParent(nodeToValue);
		} else {
			nodeToValue.setParent(parent);
		}

		ApsProperties titles = ((IPage)realNode).getDraftMetadata().getTitles();
		Set<Object> codes = titles.keySet();
		Iterator<Object> iterKey = codes.iterator();
		while (iterKey.hasNext()) {
			String key = (String) iterKey.next();
			String title = titles.getProperty(key);
			nodeToValue.getTitles().put(key, title);
		}
	}

}
