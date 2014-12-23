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
package com.agiletec.apsadmin.portal.specialwidget;

import com.agiletec.aps.system.services.page.Widget;

/**
 * Basic interface for the action classes which configure the widget with parameters. 
 * @author E.Santoboni
 */
public interface ISimpleWidgetConfigAction {
	
	/**
	 * Initialize the interface used for configuration management.
	 * @return The code resulting from the operation.
	 */
	public String init();
	
	/**
	 * Save the configuration of the current showlet.
	 * @return The result code.
	 */
	public String save();
	
	/** 
	 * @return The showlet currently on edit.
	 * @deprecated Use {@link #getWidget()} instead
	 */
	public Widget getShowlet();

	/**
	 * Return the configuration of the showlet currently on edit.   
	 * @return The showlet currently on edit.
	 */
	public Widget getWidget();
	
}
