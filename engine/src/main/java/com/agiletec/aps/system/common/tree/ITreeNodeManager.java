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
