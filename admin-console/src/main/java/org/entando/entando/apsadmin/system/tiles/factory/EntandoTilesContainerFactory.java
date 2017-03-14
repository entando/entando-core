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
package org.entando.entando.apsadmin.system.tiles.factory;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletContext;

import org.apache.tiles.TilesContainer;
import org.apache.tiles.TilesException;
import org.apache.tiles.factory.TilesContainerFactoryException;

import org.entando.entando.apsadmin.system.tiles.EntandoStrutsTilesListener;
import org.entando.entando.apsadmin.system.tiles.impl.EntandoBasicTilesContainer;

/**
 * @see org.entando.entando.apsadmin.system.tiles.EntandoStrutsTilesListener
 * @author zuanni G.Cocco
 */
public class EntandoTilesContainerFactory /*extends TilesContainerFactory*/ {
	/*
	private static final Map<String, String> DEFAULTS = new HashMap<String, String>();

	static {
	        DEFAULTS.put(CONTAINER_FACTORY_INIT_PARAM, EntandoTilesContainerFactory.class.getName());
	}
	
	public static EntandoTilesContainerFactory getFactory(Object context) throws TilesException {
		return getMyFactory(context, DEFAULTS);
	}

	public static EntandoTilesContainerFactory getMyFactory(Object context, Map<String, String> defaults) throws TilesException {
		Map<String, String> configuration = new HashMap<String, String>(defaults);
		configuration.putAll(getInitParameterMap(context));
		EntandoTilesContainerFactory factory =
			(EntandoTilesContainerFactory) EntandoTilesContainerFactory.createFactory(configuration,
					CONTAINER_FACTORY_INIT_PARAM);
		factory.setDefaultConfiguration(defaults);
		return factory;
	}
	
	@Override
	public TilesContainer createTilesContainer(Object context) throws TilesException {
		ServletContext servletContext = (ServletContext) context;
		EntandoBasicTilesContainer container = new EntandoBasicTilesContainer();
		//container.setServletContext(servletContext);
		this.initializeContainer(context, container);
		return container;
	}
	*/
}
