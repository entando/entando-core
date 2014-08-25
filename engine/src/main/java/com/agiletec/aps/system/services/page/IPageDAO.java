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
package com.agiletec.aps.system.services.page;

import java.util.List;

/**
 * Basic interface for the Data Acces Objects for the 'Page' objects
 * @author M.Diana - E.Santoboni
 */
public interface IPageDAO {

	/**
	 * Load a sorted list of the pages and the configuration of the widgets
	 * @return the list of pages
	 */
	public List<IPage> loadPages();

	/**
	 * Insert a new page.
	 * @param page The new page to insert.
	 */
	public void addPage(IPage page);

	/**
	 * Delete the page identified by the given code.
	 * @param page The page to delete.
	 */
	public void deletePage(IPage page);

	/**
	 * Updates a page record in the database.
	 * @param page The page to update
	 */
	public void updatePage(IPage page);

	/**
	 * Updates the position for the page movement
	 * @param pageDown The page to move downwards
	 * @param pageUp The page to move upwards
	 */
	public void updatePosition(IPage pageDown, IPage pageUp);

	public void updateWidgetPosition(String pageCode, Integer frameToMove, Integer destFrame);
	
	/**
	 * Setta il widget (comprensiva della sua configurazione) nella pagina e nel frame specificato.
	 * Nel caso che la posizione specificata sia già occupata, il widget corrente
	 * sarà sostituita da quella specificata.
	 * @param pageCode Il codice della pagina in cui settare il widget.
	 * @param widget il widget da settare.
	 * @param pos La posizione della pagina su cui settare il widget.
	 * @deprecated Use {@link #joinWidget(String,Widget,int)} instead
	 */
	public void joinShowlet(String pageCode, Widget widget, int pos);

	/**
	 * Setta il widget (comprensiva della sua configurazione) nella pagina e nel frame specificato.
	 * Nel caso che la posizione specificata sia già occupata, il widget corrente
	 * sarà sostituita da quella specificata.
	 * @param pageCode Il codice della pagina in cui settare il widget.
	 * @param widget il widget da settare.
	 * @param pos La posizione della pagina su cui settare il widget.
	 */
	public void joinWidget(String pageCode, Widget widget, int pos);

	/**
	 * Rimuove una widget nella pagina specificata.
	 * @param pageCode Il codice della pagina nel quale rimuovere il widget.
	 * @param pos La posizione dal liberare.
	 * @deprecated Use {@link #removeWidget(String,int)} instead
	 */
	public void removeShowlet(String pageCode, int pos);

	/**
	 * Rimuove una widget nella pagina specificata.
	 * @param pageCode Il codice della pagina nel quale rimuovere il widget.
	 * @param pos La posizione dal liberare.
	 */
	public void removeWidget(String pageCode, int pos);

}