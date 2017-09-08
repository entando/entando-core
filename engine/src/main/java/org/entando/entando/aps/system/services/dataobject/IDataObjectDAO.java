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
package org.entando.entando.aps.system.services.dataobject;

import com.agiletec.aps.system.common.entity.IEntityDAO;
import java.util.List;
import org.entando.entando.aps.system.services.dataobject.model.DataObject;

/**
 * Basic interface for the Data Access Objects for the 'content' objects.
 *
 * @author M.Diana - E.Santoboni - S.Didaci
 */
public interface IDataObjectDAO extends IEntityDAO {

	/**
	 * Publish a content.
	 *
	 * @param content The content to publish.
	 */
	public void insertOnLineContent(DataObject content);

	/**
	 * Reload the references of a published content.
	 *
	 * @param content The published content.
	 */
	public void reloadPublicContentReferences(DataObject content);

	/**
	 * Reload the references of a content.
	 *
	 * @param content The content.
	 */
	public void reloadWorkContentReferences(DataObject content);

	/**
	 * Unpublish a content, preventing it from being displayed in the portal.
	 * Obviously the content itself is not deleted.
	 *
	 * @param content the content to unpublish.
	 */
	public void removeOnLineContent(DataObject content);

	public void updateContent(DataObject content, boolean updateDate);

	public DataObjectsStatus loadContentStatus();

	public List<String> getCategoryUtilizers(String categoryCode);

	public List<String> getGroupUtilizers(String groupName);

}
