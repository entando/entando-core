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
