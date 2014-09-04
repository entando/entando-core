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
