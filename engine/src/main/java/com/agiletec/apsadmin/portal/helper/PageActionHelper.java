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
package com.agiletec.apsadmin.portal.helper;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.entando.entando.aps.system.services.actionlog.model.ActivityStreamInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agiletec.aps.system.common.tree.ITreeNode;
import com.agiletec.aps.system.common.tree.TreeNode;
import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.aps.system.services.baseconfig.ConfigInterface;
import com.agiletec.aps.system.services.group.Group;
import com.agiletec.aps.system.services.lang.Lang;
import com.agiletec.aps.system.services.page.IPage;
import com.agiletec.aps.system.services.page.IPageManager;
import com.agiletec.aps.system.services.page.PageUtilizer;
import com.agiletec.aps.system.services.role.Permission;
import com.agiletec.aps.system.services.user.UserDetails;
import com.agiletec.aps.util.ApsWebApplicationUtils;
import com.agiletec.apsadmin.portal.AbstractPortalAction;
import com.agiletec.apsadmin.system.TreeNodeBaseActionHelper;

/**
 * Classe Helper per la gestione pagine.
 * @author E.Santoboni
 */
public class PageActionHelper extends TreeNodeBaseActionHelper implements IPageActionHelper {

	private static final Logger _logger = LoggerFactory.getLogger(PageActionHelper.class);
	
	@Override
	public List<Group> getAllowedGroups(UserDetails currentUser) {
		return super.getAllowedGroups(currentUser);
	}
	
	@Override
	public Map getReferencingObjects(IPage page, HttpServletRequest request) throws ApsSystemException {
    	Map<String, List> references = new HashMap<String, List>();
    	try {
    		String[] defNames = ApsWebApplicationUtils.getWebApplicationContext(request).getBeanNamesForType(PageUtilizer.class);
			for (int i=0; i<defNames.length; i++) {
				Object service = null;
				try {
					service = ApsWebApplicationUtils.getWebApplicationContext(request).getBean(defNames[i]);
				} catch (Throwable t) {
					_logger.error("error in hasReferencingObjects", t);
					//ApsSystemUtils.logThrowable(t, this, "hasReferencingObjects");
					service = null;
				}
				if (service != null) {
					PageUtilizer pageUtilizer = (PageUtilizer) service;
					List utilizers = pageUtilizer.getPageUtilizers(page.getCode());
					if (utilizers != null && !utilizers.isEmpty()) {
						references.put(pageUtilizer.getName()+"Utilizers", utilizers);
					}
				}
			}
    	} catch (Throwable t) {
    		throw new ApsSystemException("Error extracting Referencing Objects", t);
    	}
    	return references;
    }
	
	@Override
	@Deprecated (/** from jAPS 2.0 version jAPS 2.1 */)
	public ITreeNode getAllowedTreeRoot(UserDetails user) throws ApsSystemException {
		return this.getAllowedTreeRoot(user, false);
	}
	
	@Override
	public ITreeNode getAllowedTreeRoot(UserDetails user, boolean alsoFreeViewPages) throws ApsSystemException {
		List<String> groupCodes = new ArrayList<String>();
		List<Group> groups = this.getAllowedGroups(user);
		for (int i=0; i<groups.size(); i++) {
			groupCodes.add(groups.get(i).getName());
		}
		return this.getAllowedTreeRoot(groupCodes, alsoFreeViewPages);
	}
	
	@Override
	public ITreeNode getAllowedTreeRoot(Collection<String> groupCodes) throws ApsSystemException {
		return this.getAllowedTreeRoot(groupCodes, false);
	}
	
	@Override
	public ITreeNode getAllowedTreeRoot(Collection<String> groupCodes, boolean alsoFreeViewPages) throws ApsSystemException {
		ITreeNode root = null;
		if (groupCodes.contains(Group.ADMINS_GROUP_NAME)) {
			root = this.getRoot();
		} else {
			IPage pageRoot = (IPage) this.getRoot();
			if (groupCodes.contains(Group.FREE_GROUP_NAME) 
					|| (alsoFreeViewPages && null != pageRoot.getExtraGroups() && pageRoot.getExtraGroups().contains(Group.FREE_GROUP_NAME))) {
				root = new TreeNode();
				this.fillTreeNode((TreeNode) root, null, this.getRoot());
			} else {
				root = this.getVirtualRoot();
			}
			this.addTreeWrapper((TreeNode) root, null, pageRoot, groupCodes, alsoFreeViewPages);
		}
		return root;
	}
	
	private void addTreeWrapper(TreeNode currentNode, TreeNode parent, IPage currentTreeNode, Collection<String> groupCodes, boolean alsoFreeViewPages) {
		IPage[] children = currentTreeNode.getChildren();
		for (int i=0; i<children.length; i++) {
			IPage newCurrentTreeNode = children[i];
			if (this.isPageAllowed(newCurrentTreeNode, groupCodes, alsoFreeViewPages)) {
				TreeNode newNode = new TreeNode();
				this.fillTreeNode(newNode, currentNode, newCurrentTreeNode);
				currentNode.addChild(newNode);
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
		TreeNode virtualRoot = new TreeNode();
		virtualRoot.setCode(AbstractPortalAction.VIRTUAL_ROOT_CODE);
		List<Lang> langs = this.getLangManager().getLangs();
		for (int i=0; i<langs.size(); i++) {
			Lang lang = langs.get(i);
			virtualRoot.setTitle(lang.getCode(), "ROOT");
		}
		return virtualRoot;
	}
	
	@Override
	protected void buildCheckNodes(ITreeNode treeNode, Set<String> checkNodes, Collection<String> groupCodes) {
		checkNodes.add(treeNode.getCode());
		ITreeNode parent = treeNode.getParent();
		IPage page = this.getPageManager().getPage(parent.getCode());
		if (!this.isPageAllowed(page, groupCodes, false)) {
			checkNodes.add(AbstractPortalAction.VIRTUAL_ROOT_CODE);
			return;
		}
		if (parent != null && parent.getParent() != null && 
				!parent.getCode().equals(treeNode.getCode())) {
			this.buildCheckNodes(parent, checkNodes, groupCodes);
		}
	}
	
	private boolean isPageAllowed(IPage page, Collection<String> groupCodes, boolean alsoFreeViewPages) {
		if (page == null) return false;
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
		IPage page = this.getPageManager().getPage(code);
		return this.isPageAllowed(page, groupCodes, false);
	}
	
	@Override
	protected ITreeNode getTreeNode(String code) {
		if (AbstractPortalAction.VIRTUAL_ROOT_CODE.equals(code)) {
			return this.getVirtualRoot();
		}
		return this.getPageManager().getPage(code);
	}
	
	@Override
	protected ITreeNode getRoot() {
		return (ITreeNode) this.getPageManager().getRoot();
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
