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
 * Basic interface for the Data Access Objects for the 'data' objects.
 *
 * @author M.Diana - E.Santoboni - S.Didaci
 */
public interface IDataObjectDAO extends IEntityDAO {

    public void insertDataObject(DataObject data);

    public void reloadDataObjectReferences(DataObject data);

    public void removeDataObject(DataObject data);

    public void updateDataObject(DataObject data, boolean updateDate);

    public DataObjectsStatus loadDataObjectsStatus();

    public List<String> getCategoryUtilizers(String categoryCode);

    public List<String> getGroupUtilizers(String groupName);

}
