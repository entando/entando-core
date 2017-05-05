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
package com.agiletec.aps.system.services.page;

import java.util.List;

import com.agiletec.aps.system.exception.ApsSystemException;

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
	 * @param page La pagina in cui settare il widget.
	 * @param widget il widget da settare.
	 * @param pos La posizione della pagina su cui settare il widget.
	 */
	public void joinWidget(IPage page, Widget widget, int pos);
	

	
	/**
	 * Rimuove una widget nella pagina specificata.
	 * @param pageCode Il codice della pagina nel quale rimuovere il widget.
	 * @param pos La posizione dal liberare.
	 */
	public void removeWidget(IPage page, int pos);
	
	/**
	 * Move a page under a a new parent node
	 * @param currentPage page to move
	 * @param newParent new parent
	 */
	public void movePage(IPage currentPage, IPage newParent);
	
	/**
	 * Set a page as online, copying the current draft configuration.
	 * @param pageCode The code of the page to set as online
	 */
	public void setPageOnline(String pageCode);
	
	/**
	 * Set a page as offline, removing the current online configuration.
	 * @param pageCode The code of the page to set as offline
	 */
	public void setPageOffline(String pageCode);
	
}