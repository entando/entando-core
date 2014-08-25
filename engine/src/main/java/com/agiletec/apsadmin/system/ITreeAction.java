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

import com.agiletec.aps.system.common.tree.ITreeNode;

/**
 * Interface of the Action class which handles tree. 
 * @author E.Santoboni
 */
public interface ITreeAction {
	
	/**
	 * Build the tree.
	 * This action method allows to set the collection of the "selected node" to build the showable tree.
	 * @return The result code.
	 */
	public String buildTree();
	
	/**
	 * Open a tree node.
	 * This action method allows to set the collection of the "selected node" to build the showable tree.
	 * @return The result code.
	 */
	//public String openTreeNode();
	
	/**
	 * Close a tree node.
	 * This action method allows to set the collection of the "selected node" to build the showable tree.
	 * @return The result code.
	 */
	//public String closeTreeNode();
	
	/** 
	 * Returns the root node of the tree that is granted access.
	 * The node may be the effective root of the tree or a virtual node which contains only
	 * some qualified nodes.
	 * @return The root node of the tree which the current user is granted to access.
	 */
	public ITreeNode getAllowedTreeRootNode();
	
	/**
	 * Return the root of the showable tree.
	 * The tree il build by the current "selected nodes".
	 * @return The root of the showable tree.
	 */
	public ITreeNode getShowableTree();
	
	public static final String ACTION_MARKER_OPEN = "open";
	public static final String ACTION_MARKER_CLOSE = "close";
	
}