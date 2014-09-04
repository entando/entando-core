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
 * This interface is specific for those actions which handle the configuration of a single page.
 * @author E.Santoboni
 */
public interface IPageConfigAction {
	
	/**
	 * Configure the frames of portal page.
	 * @return The code describing the result of the operation.
	 */
	public String configure();
	
	/**
	 * Associate a showlet to a frame of the page on edit.
	 * @return The result code
	 * @deprecated Use {@link #joinWidget()} instead
	 */
	public String joinShowlet();

	/**
	 * Associate a widget to a frame of the page on edit.
	 * @return The result code
	 */
	public String joinWidget();

	/**
	 * Remove a showlet from those defined in the current page.
	 * @return The result code
	 * @deprecated use trashShowlet
	 */
	public String removeShowlet() ;
	
	/**
	 * @deprecated Use {@link #trashWidget()} instead
	 */
	public String trashShowlet() ;

	/**
	 * Executes the specific action to trash a widget from a page. This does NOT perform any operation.
	 * @return The result code
	 */
	public String trashWidget() ;
	
	/**
	 * @deprecated Use {@link #deleteWidget()} instead
	 */
	public String deleteShowlet() ;

	/**
	 * Forces the deletion of a widget from a page.
	 * @return The result code
	 */
	public String deleteWidget() ;
	
	/**
	 * Start the configuration of a single page frame. 
	 * @return The result code
	 */
	public String editFrame();
	
}
