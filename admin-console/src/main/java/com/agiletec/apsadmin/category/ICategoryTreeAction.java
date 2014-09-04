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
