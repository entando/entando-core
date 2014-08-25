/*
*
* Copyright 2014 Entando S.r.l. (http://www.entando.com) All rights reserved.
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
* Copyright 2014 Entando S.r.l. (http://www.entando.com) All rights reserved.
*
*/
package org.entando.entando.apsadmin.system.tiles;

import javax.servlet.ServletContext;

import org.apache.struts2.tiles.StrutsTilesListener;
import org.apache.tiles.TilesContainer;
import org.apache.tiles.TilesException;

import org.entando.entando.apsadmin.system.tiles.factory.EntandoTilesContainerFactory;

/**
 * Listener for loading during init struts2 tiles configuration files.
 * Accepting for web.xml context param org.apache.tiles.impl.BasicTilesContainer.DEFINITIONS_CONFIG
 * coma separared entries with jolly characters **.
 * 
 *  <listener>
 *	  <listener-class>org.entando.entando.apsadmin.system.tiles.EntandoStrutsTilesListener</listener-class>
 *  </listener>
 * 
 * 
 * @see org.apache.struts2.tiles.StrutsTilesListener
 * @version 1.0
 * @author zuanni G.Cocco
 */
public class EntandoStrutsTilesListener extends StrutsTilesListener {
	
	@Override
	protected TilesContainer createContainer(ServletContext context) throws TilesException {
		EntandoTilesContainerFactory factory = EntandoTilesContainerFactory.getFactory(context);
		return factory.createContainer(context);
	}
	
}