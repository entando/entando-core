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
package org.entando.entando.aps.system.services.widgettype;

import com.agiletec.aps.util.ApsProperties;
import java.util.Map;

/**
 * Interfaccia base per Data Access Object dei tipi di widget (WidgetType).
 *
 * @author E.Santoboni
 */
public interface IWidgetTypeDAO {

	/**
	 * Return the map of the widget types
	 *
	 * @return The map of the widget types
	 */
	public Map<String, WidgetType> loadWidgetTypes();

	public void addWidgetType(WidgetType widgetType);

	/**
	 * Delete a widget type.
	 *
	 * @param widgetTypeCode The code of widget type to delete
	 */
	public void deleteWidgetType(String widgetTypeCode);

	public void updateWidgetType(String showletTypeCode, ApsProperties titles, ApsProperties defaultConfig, String mainGroup);

}
