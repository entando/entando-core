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
package org.entando.entando.apsadmin.system.tiles;

import javax.servlet.ServletContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.struts2.tiles.StrutsTilesInitializer;
import org.apache.tiles.factory.AbstractTilesContainerFactory;
import org.apache.tiles.request.ApplicationContext;

/**
 * @see org.entando.entando.apsadmin.system.tiles.EntandoStrutsTilesListener
 * @author E.Santoboni
 */
public class EntandoStrutsTilesInitializer extends StrutsTilesInitializer {
	
	private static final Logger LOG = LogManager.getLogger(EntandoStrutsTilesInitializer.class);
	private ServletContext _servletContext;
	
	protected EntandoStrutsTilesInitializer(ServletContext servletContext) {
		this._servletContext = servletContext;
	}
	
	@Override
    protected AbstractTilesContainerFactory createContainerFactory(ApplicationContext context) {
        LOG.trace("Creating dedicated Struts factory to create Tiles container");
        return new EntandoStrutsTilesContainerFactory(this._servletContext);
    }
	
}
