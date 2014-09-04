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
import java.util.Iterator;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agiletec.aps.system.common.tree.ITreeNode;
import com.agiletec.aps.system.common.tree.TreeNode;
import com.agiletec.aps.system.exception.ApsSystemException;

/**
 * Classe base per gli helper che gestiscono le operazioni su oggetti alberi.
 * @author E.Santoboni
 */
public abstract class TreeNodeBaseActionHelper extends BaseActionHelper implements ITreeNodeBaseActionHelper {

	private static final Logger _logger = LoggerFactory.getLogger(TreeNodeBaseActionHelper.class);
	
	/**
	 * Costruisce il codice univoco di un nodo in base ai parametri specificato.
	 * Il metodo:
	 * 1) elimina i caratteri non compresi tra "a" e "z", tra "0" e "9".
	 * 2) taglia (se necessario) la stringa secondo la lunghezza massima immessa.
	 * 3) verifica se esistono entità con il codice ricavato (ed in tal caso appende il suffisso "_<numero>" fino a che non trova un codice univoco).
	 * @param title Il titolo del nuovo nodo.
	 * @param baseDefaultCode Un codice nodo di default.
	 * @param maxLength La lunghezza massima del codice.
	 * @return Il codice univoco univoco ricavato.
	 * @throws ApsSystemException In caso di errore.
	 */
	@Override
	public String buildCode(String title, String baseDefaultCode, int maxLength) throws ApsSystemException {
		String uniqueCode = null;
		try {
			// punto 1
			uniqueCode = (null != title) ? purgeString(title) : baseDefaultCode;
			if (uniqueCode.length() == 0) {
				uniqueCode = baseDefaultCode;
			}
			// punto 2
			if (uniqueCode.length() > maxLength) {
				uniqueCode = uniqueCode.substring(0, maxLength);
				if (uniqueCode.charAt(uniqueCode.length()-1) == '_') {
					uniqueCode = uniqueCode.substring(0, uniqueCode.length()-1);
				}
			}
			//punto 3
			if (null != this.getTreeNode(uniqueCode)) {
				int index = 0;
				String currentCode = null;
				do {
					index++;
					currentCode = uniqueCode + "_" + index;
				} while (null != this.getTreeNode(currentCode));
				uniqueCode = currentCode;
			}
		} catch (Throwable t) {
			throw new ApsSystemException("Errore in creazione nuovo codice", t);
		}
		return uniqueCode;
	}
	
	@Override
	public Set<String> checkTargetNodes(String nodeToOpen, Set<String> lastOpenedNodes, Collection<String> groupCodes) throws ApsSystemException {
		Set<String> checkedTargetNodes = new HashSet<String>();
		try {
			if (null != nodeToOpen && this.checkNode(nodeToOpen, groupCodes)) {
				checkedTargetNodes.add(nodeToOpen);
			}
			if (null != lastOpenedNodes) {
				Iterator<String> iter = lastOpenedNodes.iterator();
				while (iter.hasNext()) {
					String code = (String) iter.next();
					if (null != code && this.checkNode(code, groupCodes)) {
						checkedTargetNodes.add(code);
					}
				}
			}
		} catch (Throwable t) {
			_logger.error("Error check target nodes", t);
			//ApsSystemUtils.logThrowable(t, this, "checkTargetNodes");
			throw new ApsSystemException("Error check target nodes", t);
		}
		return checkedTargetNodes;
	}
	
	@Override
	public Set<String> checkTargetNodesOnClosing(String nodeToCloseCode, Set<String> lastOpenedNodes, Collection<String> groupCodes) throws ApsSystemException {
		ITreeNode nodeToClose = this.getTreeNode(nodeToCloseCode);
		if (null == nodeToCloseCode || null == nodeToClose) {
			return this.checkTargetNodes(null, lastOpenedNodes, groupCodes);
		}
		Set<String> checkedTargetNodes = new HashSet<String>();
		try {
			if (nodeToClose.isRoot()) {
				return checkedTargetNodes;
			}
			if (null != lastOpenedNodes) {
				Iterator<String> iter = lastOpenedNodes.iterator();
				while (iter.hasNext()) {
					String code = (String) iter.next();
					if (null != code && this.checkNode(code, groupCodes) 
							&& !code.equals(nodeToCloseCode) && !this.getTreeNode(code).isChildOf(nodeToCloseCode)) {
						checkedTargetNodes.add(code);
					}
				}
			}
			if (null != nodeToClose 
					&& null != nodeToClose.getParent() 
					&& this.checkNode(nodeToClose.getParent().getCode(), groupCodes)) {
				checkedTargetNodes.add(nodeToClose.getParent().getCode());
			}
		} catch (Throwable t) {
			_logger.error("Error check target nodes on closing tree", t);
			//ApsSystemUtils.logThrowable(t, this, "checkTargetNodesOnClosing");
			throw new ApsSystemException("Error check target nodes on closing tree", t);
		}
		return checkedTargetNodes;
	}
	
	protected boolean checkNode(String nodeCode, Collection<String> groupCodes) {
		if (!this.isNodeAllowed(nodeCode, groupCodes)) {
			_logger.error("Node '{}' not allowed ", nodeCode);
			return false;
		}
		ITreeNode treeNode = this.getTreeNode(nodeCode);
		if (null == treeNode) {
			_logger.error("Node '{}' null", nodeCode);
			return false;
		}
		return true;
	}
	
	@Override
	public TreeNodeWrapper getShowableTree(Set<String> treeNodesToOpen, ITreeNode fullTree, Collection<String> groupCodes) throws ApsSystemException {
		if (null == treeNodesToOpen || treeNodesToOpen.isEmpty()) {
			_logger.warn("No selected nodes");
			return new TreeNodeWrapper(fullTree);
		}
		TreeNodeWrapper root = null;
		try {
			Set<String> checkNodes = new HashSet<String>();
			this.buildCheckNodes(treeNodesToOpen, checkNodes, groupCodes);
			root = new TreeNodeWrapper(fullTree);
			root.setParent(root);
			this.builShowableTree(root, null, fullTree, checkNodes);
		} catch (Throwable t) {
			_logger.error("Error creating showalble tree", t);
			//ApsSystemUtils.logThrowable(t, this, "getShowableTree");
			throw new ApsSystemException("Error creating showalble tree", t);
		}
		return root;
	}
	
	private void buildCheckNodes(Set<String> treeNodesToOpen, Set<String> checkNodes, Collection<String> groupCodes) {
		if (null == treeNodesToOpen) return;
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
			for (int i=0; i<children.length; i++) {
				ITreeNode newCurrentTreeNode = children[i];
				TreeNodeWrapper newNode = new TreeNodeWrapper(newCurrentTreeNode);
				newNode.setParent(currentNode);
				currentNode.addChild(newNode);
				this.builShowableTree(newNode, currentNode, newCurrentTreeNode, checkNodes);
			}
		}
	}
	
	protected void buildCheckNodes(ITreeNode treeNode, Set<String> checkNodes, Collection<String> groupCodes) {
		checkNodes.add(treeNode.getCode());
		ITreeNode parent = treeNode.getParent();
		if (parent != null && parent.getParent() != null && 
				!parent.getCode().equals(treeNode.getCode())) {
			this.buildCheckNodes(parent, checkNodes, groupCodes);
		}
	}
	
	protected abstract boolean isNodeAllowed(String code, Collection<String> groupCodes);
	
	/**
	 * Default implementation of the method.
	 * Build a root node cloning the returned tree from the helper.
	 */
	@Override
	public ITreeNode getAllowedTreeRoot(Collection<String> groupCodes) throws ApsSystemException {
		TreeNode root = new TreeNode();
		ITreeNode currentRoot = this.getRoot();
		this.fillTreeNode(root, root, currentRoot);
		this.addTreeWrapper(root, null, currentRoot);
		return root;
	}
	
	private void addTreeWrapper(TreeNode currentNode, TreeNode parent, ITreeNode currentTreeNode) {
		ITreeNode[] children = currentTreeNode.getChildren();
		for (int i=0; i<children.length; i++) {
			ITreeNode newCurrentTreeNode = children[i];
			TreeNode newNode = new TreeNode();
			this.fillTreeNode(newNode, currentNode, newCurrentTreeNode);
			currentNode.addChild(newNode);
			this.addTreeWrapper(newNode, currentNode, newCurrentTreeNode);
		}
	}
	
	/**
	 * Valorizza un nodo in base alle informazioni specificate..
	 * @param nodeToValue Il nodo da valorizzare.
	 * @param parent Il nodo parente.
	 * @param realNode Il nodo dal quela estrarre le info.
	 */
	protected void fillTreeNode(TreeNode nodeToValue, TreeNode parent, ITreeNode realNode) {
		nodeToValue.setCode(realNode.getCode());
		if (null == parent) {
			nodeToValue.setParent(nodeToValue);
		} else {
			nodeToValue.setParent(parent);
		}
		Set<Object> codes = realNode.getTitles().keySet();
		Iterator<Object> iterKey = codes.iterator();
		while (iterKey.hasNext()) {
			String key = (String) iterKey.next();
			String title = realNode.getTitles().getProperty(key);
			nodeToValue.getTitles().put(key, title);
		}
	}
	
	/**
	 * Return the root node of the managed tree.
	 * @return The root node.
	 */
	protected abstract ITreeNode getRoot();
	
	/**
	 * Return a node of the managed tree.
	 * @param code The code of the node to return.
	 * @return The required node.
	 */
	protected abstract ITreeNode getTreeNode(String code);
	
}