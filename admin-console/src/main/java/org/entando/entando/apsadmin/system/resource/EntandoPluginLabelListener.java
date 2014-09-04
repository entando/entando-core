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
package org.entando.entando.apsadmin.system.resource;

import java.io.InputStream;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarInputStream;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.opensymphony.xwork2.util.LocalizedTextUtil;

/**
 * This listener is used to add the properties of the plugins menu to the default resource bundles.
 * Note: this listener distinguishes between plugins under development and standard ones, that is,
 * those plugins found in the WEB-INF/classes/ directory and those normally installed in the
 * WEB-INF/lib/ in the form of JAR files.
 * Apart the 'plugins' contained in the package, we rely on the 'apsadmin' being present soon
 * after the plugin name in the URL to distinguish between Entando plugins and the others eventually
 * contained in the system libraries.
 *
 * @author M. Minnai
 */
public class EntandoPluginLabelListener implements ServletContextListener {

	private static final Logger _logger = LoggerFactory.getLogger(EntandoPluginLabelListener.class);
	
	@Override
	public void contextInitialized(ServletContextEvent event) {
		Set<String> classPlugins = this.discoverClasses(TOMCAT_CLASSES, event);
		Set<String> jaredPlugins = this.discoverJars(TOMCAT_LIB, event);
		Iterator<String> itr = classPlugins.iterator();
		while (itr.hasNext()) {
			String cur = itr.next();
			_logger.debug("Trying to load resources under development @ {}", cur);
			LocalizedTextUtil.addDefaultResourceBundle(cur+this.PLUGIN_RESOURCE_NAME);
		}
		itr = jaredPlugins.iterator();
		while (itr.hasNext()) {
			String cur = itr.next();
			_logger.debug("Trying to load resources @{}", cur);
			LocalizedTextUtil.addDefaultResourceBundle(cur+this.PLUGIN_RESOURCE_NAME);
		}
		_logger.info("EntandoPluginLabelListener summary: {} plugin detected ({} under development)", (classPlugins.size()+jaredPlugins.size()), classPlugins.size());
	}

	/**
	 * Discover the directories holding plugins within the classpath
	 * @param path the path where to start the search from
	 * @param event the servlet context event
	 */
	private Set<String> discoverClasses(String path, ServletContextEvent event) {
		Set<String> plugins = new HashSet<String>();
		if (null == path || null == event) {
			return plugins;
		}
		Set<String> directory = event.getServletContext().getResourcePaths(path);
		if (null != directory && !directory.isEmpty()) {
			Iterator<String> itr = directory.iterator();
			while (itr.hasNext()) {
				String currentDirectory=itr.next();
				boolean skip = false;
				// AVOID USELESS LOOPS IF POSSIBLE
				Iterator<String> exclude = _plugin_exclusion_directories.iterator();
				while (exclude.hasNext()) {
					String currentDirectoryExcluded = exclude.next();
					if (currentDirectory.contains(currentDirectoryExcluded)
							&& !currentDirectory.contains(PLUGIN_DIRECTORY)) {
						skip = true;
						break;
					}
				}
				if (skip) continue;
				if (currentDirectory.contains(PLUGIN_DIRECTORY)
						&& currentDirectory.endsWith(PLUGIN_APSADMIN_PATH)) {
					currentDirectory = currentDirectory.replaceFirst(TOMCAT_CLASSES, "");
					plugins.add(currentDirectory);
				} else {
					plugins.addAll(discoverClasses(currentDirectory, event));
				}
			}
		}
		return plugins;
	}

	private Set<String> discoverJars(String path, ServletContextEvent event) {
		Set<String> plugins = new HashSet<String>();
		Set<String> directory = event.getServletContext().getResourcePaths(path);
		Iterator<String> itr = directory.iterator();
		// ITERATE OVER PATHS
		while (null != itr && itr.hasNext()) {
			String currentJar = itr.next();
			InputStream is=event.getServletContext().getResourceAsStream(currentJar);
			plugins.addAll(discoverJarPlugin(is));
		}
		return plugins;
	}

	/**
	 * Fetch all the entries in the given (jar) input stream and look for the
	 * plugin directory.
	 * @param is the input stream to analyse
	 */
	private Set<String> discoverJarPlugin(InputStream is) {
		Set<String> plugins = new HashSet<String>();
		if (null==is) return plugins;
		JarEntry je = null;
		try {
			JarInputStream jis = new JarInputStream(is);
			do {
				je = jis.getNextJarEntry();
				if (null != je) {
					String URL=je.toString();
					if (URL.contains(PLUGIN_DIRECTORY)
							&& URL.endsWith(PLUGIN_APSADMIN_PATH)) {
						plugins.add(URL);
					}
				}
			} while (je != null);
		} catch (Throwable t) {
			_logger.error("error in discoverJarPlugin", t);
			//ApsSystemUtils.logThrowable(t, this, "discoverJarPlugin");
		}
		return plugins;
	}

	@Override
	public void contextDestroyed(ServletContextEvent arg0) {
		// nothing to do
	}

	/**
	 * This contains all the directories to exclude from the recursive search when
	 * PLUGIN_DIRECTORY does NOT exist in the URL or path
	 */
	private List<String> _plugin_exclusion_directories = Arrays.asList("/test/",
			"/aps/",
			"/apsadmin/");

	/**
	 * Path within the plugin where the global properties are stored.
	 */
	private final String PLUGIN_APSADMIN_PATH = "/apsadmin/";

	/**
	 * Path to the global properties file within the plugin package
	 */
	private final String PLUGIN_RESOURCE_NAME = "global-messages";

	/**
	 * This is the directory where plugins are searched
	 */
	private final String PLUGIN_DIRECTORY = "plugins";

	/**
	 * The URL of Tomcat classes
	 */
	private String TOMCAT_CLASSES = "/WEB-INF/classes/";

	/**
	 * The URL of the Tomcat shared lib directory
	 */
	private String TOMCAT_LIB = "/WEB-INF/lib/";
}
