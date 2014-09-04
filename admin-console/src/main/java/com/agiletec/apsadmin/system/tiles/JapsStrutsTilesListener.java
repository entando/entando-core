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
package com.agiletec.apsadmin.system.tiles;

import org.entando.entando.apsadmin.system.tiles.EntandoStrutsTilesListener;

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
 * @deprecated use {@link EntandoStrutsTilesListener}
 */
public class JapsStrutsTilesListener extends EntandoStrutsTilesListener {
	
}
