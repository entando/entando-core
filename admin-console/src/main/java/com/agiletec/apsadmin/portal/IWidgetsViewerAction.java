/*
 * Copyright 2013-Present Entando Corporation (http://www.entando.com) All rights reserved.
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
package com.agiletec.apsadmin.portal;

/**
 * @author E.Santoboni
 */
public interface IWidgetsViewerAction {
	
	/**
	 * @deprecated Use {@link #viewWidgets()} instead
	 */
	public String viewShowlets();

	/**
	 * Show the widget catalog.
	 * @return The code describing the result of the operation.
	 */
	public String viewWidgets();
	
	/**
	 * @deprecated Use {@link #viewWidgetUtilizers()} instead
	 */
	public String viewShowletUtilizers();

	/**
	 * Show the list of pages where a single widget is published.
	 * @return The code describing the result of the operation.
	 */
	public String viewWidgetUtilizers();
	
}
