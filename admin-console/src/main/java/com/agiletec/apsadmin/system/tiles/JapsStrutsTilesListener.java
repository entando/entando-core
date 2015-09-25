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
