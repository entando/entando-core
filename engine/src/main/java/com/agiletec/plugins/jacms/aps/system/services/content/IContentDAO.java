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
package com.agiletec.plugins.jacms.aps.system.services.content;

import java.util.List;

import com.agiletec.aps.system.common.entity.IEntityDAO;
import com.agiletec.plugins.jacms.aps.system.services.content.model.Content;

/**
 * Basic interface for the Data Access Objects for the 'content' objects. 
 * @author M.Diana - E.Santoboni - S.Didaci
 */
public interface IContentDAO extends IEntityDAO {
	
	/**
	 * Publish a content.
	 * @param content The content to publish.
	 */
	public void insertOnLineContent(Content content);
	
	/**
	 * Reload the references of a published content.
	 * @param content The published content.
	 */
	public void reloadPublicContentReferences(Content content);
	
	/**
	 * Reload the references of a content.
	 * @param content The content.
	 */
	public void reloadWorkContentReferences(Content content);
	
	/**
	 * Unpublish a content, preventing it from being displayed in the portal. Obviously
	 * the content itself is not deleted.
	 * @param content the content to unpublish.
	 */
	public void removeOnLineContent(Content content);
	
	/**
	 * Return the list of the contents IDs referenced by the specified group.
	 * @param groupName The name of the group.
	 * @return The list of the contents IDs referenced by the given group.
	 */
	public List<String> getGroupUtilizers(String groupName);
	
	/**
	 * Return the list of IDs of the contents which reference the specified page.
	 * @param pageCode The code of the page.
	 * @return The list of IDs of the contents which reference the specified page.
	 */
	public List<String> getPageUtilizers(String pageCode);
	
	/**
	 * Return the list of the content IDs which reference the specified content. 
	 * @param contentId The ID of the content.
	 * @return the list of the content IDs which reference the specified content.
	 */
	public List<String> getContentUtilizers(String contentId);
	
	/**
	 * Return the list of content IDs which reference the specified resource. 
	 * @param resourceId The id of the resource.
	 * @return The list of content IDs which reference the specified resource.
	 */
	public List<String> getResourceUtilizers(String resourceId);
	
	/**
	 * Return the list of the content ID which reference the specified category. 
	 * @param categoryCode The category code.
	 * @return The list of the content ID which reference the specified category.
	 */
	public List<String> getCategoryUtilizers(String categoryCode);
	
	public void updateContent(Content content, boolean updateDate);
	
}
