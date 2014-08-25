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
package com.agiletec.aps.system.services.page;

import java.util.Set;

import com.agiletec.aps.system.common.tree.ITreeNode;
import com.agiletec.aps.system.services.pagemodel.PageModel;

/**
 * This class describes a pages of the portal.
 * @author E.Santoboni
 */
public interface IPage extends ITreeNode {

	/**
	 * Return the model of the associated page
	 * @return The page model
	 */
	public PageModel getModel();
	
	/**
	 * Return the set of codes of the additional groups authorized to view the page in the front-end. 
	 * @return The set of codes belonging to the additional group authorized to access the page in the front-end.
	 */
	public Set<String> getExtraGroups();
	
	/**
	 * Add a group (code) authorized to view/access the page in the Front-end.
	 * @param groupName The group to add.
	 */
	public void addExtraGroup(String groupName);
	
	/**
	 * Remove a group (code) authorized to view/access the page in the Front-end.
	 * @param groupName The group to remove.
	 */
	public void removeExtraGroup(String groupName);
	
	/**
	 * WARNING: this method is reserved to the page manager service only.
	 * Return the code of the father of this page. This methods exists only to
	 * simplify the loading of the pages structure, it cannot be used in any other 
	 * circumstance.
	 * @return the code of the higher level page
	 */
	public String getParentCode();
	
	/**
	 * Return the sorted group of the children of the current page, that is the 
	 * pages belonging to the lower level
	 * @return the sorted group of the children
	 */
	@Override
	public IPage[] getChildren();

	/**
	 * Return the parent of the current page. 
	 * If the current page is the root, the root page itself is returned
	 * @return The father of the current page
	 */
	@Override
	public IPage getParent();
	
	/**
	 * This returns a boolean values indicating whether the page is
	 * displayed in the menus or similar.
	 * @return true if the page must be shown in the menu, false otherwise. 
	 */
	public boolean isShowable();
	
	/**
	 * This returns a boolean values indicating whether the page use the 
	 * extra titles extracted from Request Context parameter EXTRAPAR_EXTRA_PAGE_TITLES.
	 * @return true if the page must use the extra titles, false otherwise. 
	 */
	public boolean isUseExtraTitles();
	
	/**
	 * Return the widgets configured in this page.
	 * @return all the widgets of the current page
	 * @deprecated Use {@link #getWidgets()} instead
	 */
	public Widget[] getShowlets();

	/**
	 * Return the widgets configured in this page.
	 * @return all the widgets of the current page
	 */
	public Widget[] getWidgets();
	
	/**
	 * Return the mimetype configured for this page.
	 * @return the mimetype configured for this page.
	 */
	public String getMimeType();
	
	/**
	 * Return the charset configured for this page.
	 * @return the charset configured for this page.
	 */
	public String getCharset();
	
}