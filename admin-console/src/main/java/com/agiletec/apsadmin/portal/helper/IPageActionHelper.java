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
package com.agiletec.apsadmin.portal.helper;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.entando.entando.aps.system.services.actionlog.model.ActivityStreamInfo;

import com.agiletec.aps.system.common.tree.ITreeNode;
import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.aps.system.services.group.Group;
import com.agiletec.aps.system.services.page.IPage;
import com.agiletec.aps.system.services.user.UserDetails;
import com.agiletec.apsadmin.system.ITreeNodeBaseActionHelper;

/**
 * Interface for the helper classes handling the portal pages.
 * @author E.Santoboni
 */
public interface IPageActionHelper extends ITreeNodeBaseActionHelper {
	
	public Map getReferencingObjects(IPage page, HttpServletRequest request) throws ApsSystemException;
	
	public List<Group> getAllowedGroups(UserDetails currentUser);
	
	/**
	 * Return the root node of the page tree respecting the given user.
	 * @param user The user.
	 * @param alsoFreeViewPages Specify if include the pages with the 'free' groups on the extra groups.
	 * @return The root of the page tree.
	 * @throws ApsSystemException In case of error
	 */
	public ITreeNode getAllowedTreeRoot(UserDetails user, boolean alsoFreeViewPages) throws ApsSystemException;
	
	/**
	 * Return the root node of the page tree respecting the given permissions. 
	 * @param groupCodes The groups list used when building the page tree.
	 * @param alsoFreeViewPages Indicate if include also only free view pages
	 * @return The root of the page tree
	 * @throws ApsSystemException In case of error
	 */
	public ITreeNode getAllowedTreeRoot(Collection<String> groupCodes, boolean alsoFreeViewPages) throws ApsSystemException;
	
	public ActivityStreamInfo createActivityStreamInfo(IPage page, int strutsAction, boolean addLink, String entryAction);
	
	public ActivityStreamInfo createConfigFrameActivityStreamInfo(IPage page, int framePos, int strutsAction, boolean addLink);
	
}