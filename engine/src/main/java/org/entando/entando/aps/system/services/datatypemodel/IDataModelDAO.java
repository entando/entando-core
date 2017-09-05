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
package org.entando.entando.aps.system.services.datatypemodel;

import java.util.Map;

/**
 * Interfaccia base per i Data Access Object degli oggetti modello datatype
 */
public interface IDataModelDAO {

	public Map<Long, DataModel> loadDataModels();

	public void addDataModel(DataModel model);

	public void deleteDataModel(DataModel model);

	public void updateDataModel(DataModel model);

}
