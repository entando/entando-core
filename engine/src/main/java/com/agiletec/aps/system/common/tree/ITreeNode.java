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

import java.io.Serializable;

import com.agiletec.aps.util.ApsProperties;

/**
 * Interface of a node of a tree. 
 * The node is the basic information a tree and contains all the 
 * minimum information necessary for its definition.
 * @author E.Santoboni
 */
public interface ITreeNode extends Serializable {
	
	/**
	 * Return the node code.
	 * @return The node code.
	 */
	public String getCode();
	
	/**
	 * Indicates whether the node is the root of the tree.
	 * @return true if the root, false otherwise.
	 */
	public boolean isRoot();
	
	/**
	 * Return the parent node.
	 * @return The parent node.
	 */
	public ITreeNode getParent();
	
	/**
	 * Returns the ordered set of nodes of lower level.
	 * @return The set of nodes of lower level.
	 */
	public ITreeNode[] getChildren();
	
	/**
	 * Returns the position of the node compared to the brothers nodes.
	 * @return The position of the node compared to the brothers nodes.
	 */
    public int getPosition();
    
	/**
	 * Return the group code this node belongs to
	 * @return The group code
	 */
	public String getGroup();
	
	/**
	 * Returns a properties with the titles of the node, where the keys are the codes of language.
	 * @return The node titles.
	 */
	public ApsProperties getTitles();
	
	/**
	 * Returns the title of the node in the specified language.
	 * @param langCode The code of the language.
	 * @return The title of the node.
	 */
	public String getTitle(String langCode);
	
	/**
	 * Set the title of the node in the specified language.
	 * @param langCode The code of the language.
	 * @param title The title of the node to set.
	 */
	public void setTitle(String langCode, String title);

	/**
	 * Returns the title (including the parent nodes) of the single node in the specified language.
	 * @param langCode The code of the language.
	 * @return The full title of the node.
	 */
	public String getFullTitle(String langCode);
	
	public String getShortFullTitle(String langCode);
	
	public String getShortFullTitle(String langCode, String separator);
	
	/**
	 * Returns the title (including the parent nodes) of the single node in the specified language.
	 * @param langCode The code of the language.
	 * @param separator The separator between the titles.
	 * @return The full title of the node.
	 */
	public String getFullTitle(String langCode, String separator);
	
	/**
	 * Indicates whether the node is child of the other specificated node.
	 * @return true if the node is child of the other node, false otherwise.
	 */
	public boolean isChildOf(String nodeCode);
	
}