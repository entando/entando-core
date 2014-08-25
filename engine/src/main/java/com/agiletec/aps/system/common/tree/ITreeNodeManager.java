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
package com.agiletec.aps.system.common.tree;

/**
 * Interface for Tree Node Manager.
 * @author E.Santoboni
 */
public interface ITreeNodeManager {
	
	/**
	 * Return the root node.
	 * @return The root node.
	 */
	public ITreeNode getRoot();
	
	/**
	 * Return a Node by a code.
	 * @param code The code or the node to return.
	 * @return The node.
	 */
	public ITreeNode getNode(String code);
	
}
