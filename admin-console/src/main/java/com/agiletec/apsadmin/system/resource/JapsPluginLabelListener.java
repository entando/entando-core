/*
 * Copyright 2013-Present Entando Corporation (http://www.entando.com) All rights reserved.
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
package com.agiletec.apsadmin.system.resource;

import org.entando.entando.apsadmin.system.resource.EntandoPluginLabelListener;

/**
 * This listener is used to add the properties of the plugins menu to the default resource bundles.
 * Note: this listener distinguishes between plugins under development and standard ones, that is,
 * those plugins found in the WEB-INF/classes/ directory and those normally installed in the
 * WEB-INF/lib/ in the form of JAR files.
 * Apart the 'plugins' contained in the package, we rely on the 'apsadmin' being present soon
 * after the plugin name in the URL to distinguish between Entando plugins and the others eventually
 * contained in the system libraries.
 * @author M. Minnai
 * @deprecated use {@link EntandoPluginLabelListener}
 */
public class JapsPluginLabelListener extends EntandoPluginLabelListener {
	
}
