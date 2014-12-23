/*
 * Copyright 2013-Present Entando Corporation (http://www.entando.com) All rights reserved.
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
