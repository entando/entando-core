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

import java.util.List;

import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.aps.util.ApsProperties;

/**
 * Interfaccia base per i Servizi gestori dei tipi di widget (WidgetType)
 * definiti nel sistema.
 *
 * @author E.Santoboni
 */
public interface IWidgetTypeManager {

	/**
	 * Restituisce la definizione di un tipo di widget in base al codice.
	 *
	 * @param widgetTypeCode Il codice univoco del tipo
	 * @return La definizione del tipo di widget
	 * @deprecated Use {@link #getWidgetType(String)} instead
	 */
	public WidgetType getShowletType(String widgetTypeCode);

	/**
	 * Restituisce la definizione di un tipo di widget in base al codice.
	 *
	 * @param widgetTypeCode Il codice univoco del tipo
	 * @return La definizione del tipo di widget
	 */
	public WidgetType getWidgetType(String widgetTypeCode);

	/**
	 * Restituisce la lista completa (ordinata per descrizione) dei tipi di
	 * widget.
	 *
	 * @return la lista completa dei widget (ordinata per la descrizione del
	 * tipo)
	 * @deprecated Use {@link #getWidgetTypes()} instead
	 */
	public List<WidgetType> getShowletTypes();

	/**
	 * Restituisce la lista completa (ordinata per descrizione) dei tipi di
	 * widget.
	 *
	 * @return la lista completa dei widget (ordinata per la descrizione del
	 * tipo) disponibili in oggetti WidgetType.
	 */
	public List<WidgetType> getWidgetTypes();

	/**
	 * Add a widget type into the catalogue
	 *
	 * @param widgetType The type to add
	 * @throws ApsSystemException in case of error
	 * @deprecated Use {@link #addWidgetType(WidgetType)} instead
	 */
	public void addShowletType(WidgetType widgetType) throws ApsSystemException;

	/**
	 * Add a widget type into the catalogue
	 *
	 * @param widgetType The type to add
	 * @throws ApsSystemException in case of error
	 */
	public void addWidgetType(WidgetType widgetType) throws ApsSystemException;

	/**
	 * Delete a widget type from the catalogue
	 *
	 * @param widgetTypeCode The code of the type to delete
	 * @throws ApsSystemException in case of error
	 * @deprecated Use {@link #deleteWidgetType(String)} instead
	 */
	public void deleteShowletType(String widgetTypeCode) throws ApsSystemException;

	/**
	 * Delete a widget type from the catalogue
	 *
	 * @param widgetTypeCode The code of the type to delete
	 * @throws ApsSystemException in case of error
	 */
	public void deleteWidgetType(String widgetTypeCode) throws ApsSystemException;

	@Deprecated
	public void updateShowletType(String widgetTypeCode, ApsProperties titles, ApsProperties defaultConfig) throws ApsSystemException;

	/**
	 * Update a Widget type on the catalogue.
	 *
	 * @param widgetTypeCode The code of the widget type to update.
	 * @param titles The titles of the widget type to update.
	 * @param defaultConfig The configuration of the widget type to update.
	 * @param mainGroup The main group of the widget type to update.
	 * @throws ApsSystemException in case of error
	 * @deprecated Use
	 * {@link #updateWidgetType(String,ApsProperties,ApsProperties,String)}
	 * instead
	 */
	public void updateShowletType(String widgetTypeCode, ApsProperties titles, ApsProperties defaultConfig, String mainGroup) throws ApsSystemException;

	/**
	 * Update a Widget type on the catalogue.
	 *
	 * @param widgetTypeCode The code of the widget type to update.
	 * @param titles The titles of the widget type to update.
	 * @param defaultConfig The configuration of the widget type to update.
	 * @param mainGroup The main group of the widget type to update.
	 * @throws ApsSystemException in case of error
	 */
	public void updateWidgetType(String widgetTypeCode, ApsProperties titles, ApsProperties defaultConfig, String mainGroup) throws ApsSystemException;

}
