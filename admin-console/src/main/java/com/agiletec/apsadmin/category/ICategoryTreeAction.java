/*
 * Copyright 2013-Present Entando Inc. (http://www.entando.com) All rights reserved.
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
package com.agiletec.apsadmin.category;

import com.agiletec.aps.system.common.tree.ITreeNode;
import com.agiletec.aps.system.services.category.Category;

/**
 * Basic interface for the classes which handle the category trees.
 * @author E.Santoboni
 */
public interface ICategoryTreeAction {
	
	/**
	 * Return the root category of the tree. 
	 * @return The root category of the tree.
	 * @deprecated
	 */
	public Category getRoot();
	
	/** 
	 * Return the root node of the categories tree which the current user is granted to access. 
	 * The node may be the effective root of the category tree or a virtual node which contains only
	 * some qualified category.
	 *  
	 * @return The root node of the categories tree which the current user is granted to access.
	 */
	public ITreeNode getTreeRootNode();
	
}
