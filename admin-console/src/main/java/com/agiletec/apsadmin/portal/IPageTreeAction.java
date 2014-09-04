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
package com.agiletec.apsadmin.portal;

import com.agiletec.aps.system.common.tree.ITreeNode;
import com.agiletec.aps.system.services.page.IPage;
import com.agiletec.apsadmin.system.ITreeAction;

/**
 * This is the interface for those action which manage the tree of pages.
 * @author E.Santoboni
 */
public interface IPageTreeAction extends ITreeAction {
	
	/**
	 * Move a page upward with respect to its siblings.
	 * @return The result code.
	 */
	public String moveUp();
	
	/**
	 * Move a page downward with respect to its siblings.
	 * @return The result code.
	 */
	public String moveDown() ;
	
	/**
	 * Copy a page of the tree.
	 * @return The result code.
	 */
	public String copy();
	
	/**
	 * Return the root of the tree of pages.
	 * @return The root of the page tree.
	 * @deprecated As of version 2.0.5 use {@link #getTreeRootNode()}
	 */
	public IPage getRoot();
	
	/**
	 * Return the root node if the page tree depending on the privileges of the current user.
	 * The root can be either the effective root or a 'virtual' node whose leaves are the pages
	 * which the current user is allowed to access. 
	 * @return The root of the page tree.
	 * @deprecated As of version 2.2 use {@link #getAllowedTreeRootNode()}
	 */
	public ITreeNode getTreeRootNode();
	
}
