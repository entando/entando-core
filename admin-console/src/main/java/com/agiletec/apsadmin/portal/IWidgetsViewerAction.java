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
