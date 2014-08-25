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
package com.agiletec;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import java.util.Properties;

import javax.servlet.ServletContext;

import org.apache.commons.dbcp.BasicDataSource;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.mock.jndi.SimpleNamingContextBuilder;
import org.springframework.web.context.support.XmlWebApplicationContext;

/**
 * Classe di utilità per i test.
 * Fornisce la lista di file di configurazione di spring e il contesto.
 * La classe và estesa nel caso si intenda modificare od aggiungere 
 * file di configurazione di spring esterni.
 * @author W.Ambu - E.Santoboni
 */
public class ConfigTestUtils {
	
	/**
	 * Crea e restituisce il Contesto dell'Applicazione.
	 * @param srvCtx Il Contesto della Servlet.
	 * @return Il Contesto dell'Applicazione.
	 */
	public ApplicationContext createApplicationContext(ServletContext srvCtx) {
		this.createNamingContext();
		XmlWebApplicationContext applicationContext = new XmlWebApplicationContext();
		applicationContext.setConfigLocations(this.getSpringConfigFilePaths());
		applicationContext.setServletContext(srvCtx);
		applicationContext.refresh();
		return applicationContext;
	}
	
	protected SimpleNamingContextBuilder createNamingContext() {
		SimpleNamingContextBuilder builder = null;
		try {
			builder = SimpleNamingContextBuilder.emptyActivatedContextBuilder();
			InputStream in = new FileInputStream("target/test/conf/contextTestParams.properties");
			Properties testConfig = new Properties();
			testConfig.load(in);
			in.close();
			
			builder.bind("java:comp/env/logName", testConfig.getProperty("logName"));
			builder.bind("java:comp/env/logFilePrefix", testConfig.getProperty("logFilePrefix"));
			builder.bind("java:comp/env/logLevel", testConfig.getProperty("logLevel"));
			builder.bind("java:comp/env/logFileSize", testConfig.getProperty("logFileSize"));
			builder.bind("java:comp/env/logFilesCount", testConfig.getProperty("logFilesCount"));
			
			builder.bind("java:comp/env/configVersion", testConfig.getProperty("configVersion"));
			
			builder.bind("java:comp/env/applicationBaseURL", testConfig.getProperty("applicationBaseURL"));
			builder.bind("java:comp/env/resourceRootURL", testConfig.getProperty("resourceRootURL"));
			builder.bind("java:comp/env/protectedResourceRootURL", testConfig.getProperty("protectedResourceRootURL"));
			builder.bind("java:comp/env/resourceDiskRootFolder", testConfig.getProperty("resourceDiskRootFolder"));
			builder.bind("java:comp/env/protectedResourceDiskRootFolder", testConfig.getProperty("protectedResourceDiskRootFolder"));
			
			builder.bind("java:comp/env/indexDiskRootFolder", testConfig.getProperty("indexDiskRootFolder"));
			
			Iterator<Entry<Object, Object>> configIter = testConfig.entrySet().iterator();
			while (configIter.hasNext()) {
				Entry<Object, Object> entry = configIter.next();
				builder.bind("java:comp/env/" + (String) entry.getKey(), (String) entry.getValue()); 
			}
			
			this.createDatasources(builder, testConfig);
		} catch (Throwable t) {
			throw new RuntimeException("Error on creation naming context", t);
		}
		return builder;
	}
	
	private void createDatasources(SimpleNamingContextBuilder builder, Properties testConfig) {
		List<String> dsNameControlKeys = new ArrayList<String>();
		Enumeration<Object> keysEnum = testConfig.keys();
		while (keysEnum.hasMoreElements()) {
			String key = (String) keysEnum.nextElement();
			if (key.startsWith("jdbc.")) {
				String[] controlKeys = key.split("\\.");
				String dsNameControlKey = controlKeys[1];
				if (!dsNameControlKeys.contains(dsNameControlKey)) {
					this.createDatasource(dsNameControlKey, builder, testConfig);
					dsNameControlKeys.add(dsNameControlKey);
				}
			}
		}
	}
	
	private void createDatasource(String dsNameControlKey, SimpleNamingContextBuilder builder, Properties testConfig) {
		String beanName = testConfig.getProperty("jdbc."+dsNameControlKey+".beanName");
		try {
			String className = testConfig.getProperty("jdbc."+dsNameControlKey+".driverClassName");
			String url = testConfig.getProperty("jdbc."+dsNameControlKey+".url");
			String username = testConfig.getProperty("jdbc."+dsNameControlKey+".username");
			String password = testConfig.getProperty("jdbc."+dsNameControlKey+".password");
			Class.forName(className);
			BasicDataSource ds = new BasicDataSource();
			ds.setUrl(url);
			ds.setUsername(username);
			ds.setPassword(password);
			ds.setMaxActive(8);
			ds.setMaxIdle(4);
			ds.setDriverClassName(className);
			builder.bind("java:comp/env/jdbc/" + beanName, ds);
		} catch (Throwable t) {
			throw new RuntimeException("Error on creation datasource '" + beanName + "'", t);
		}
	}
	
	/**
	 * Restituisce l'insieme dei file di configurazione dei bean definiti nel sistema.
	 * Il metodo và esteso nel caso si inseriscano file di configurazioni esterni al Core ed ai Plugin.
	 * @return L'insieme dei file di configurazione definiti nel sistema.
	 */
	protected String[] getSpringConfigFilePaths() {
    	String[] filePaths = new String[6];
		filePaths[0] = "classpath:spring/propertyPlaceholder.xml";
		filePaths[1] = "classpath:spring/baseSystemConfig.xml";
		filePaths[2] = "classpath*:spring/aps/**/**.xml";
		filePaths[3] = "classpath*:spring/apsadmin/**/**.xml";
		filePaths[4] = "classpath*:spring/plugins/**/aps/**/**.xml";
		filePaths[5] = "classpath*:spring/plugins/**/apsadmin/**/**.xml";
		return filePaths;
    }
	
	public void destroyContext(ApplicationContext applicationContext) throws Exception {
		if (applicationContext instanceof AbstractApplicationContext) {
			((AbstractApplicationContext) applicationContext).destroy();
		}
	}
    
	/**
	 * Effettua la chiusura dei datasource definiti nel contesto.
	 * @param applicationContext Il contesto dell'applicazione.
	 * @throws Exception In caso di errore nel recupero o chiusura dei DataSource.
	 */
	public void closeDataSources(ApplicationContext applicationContext) throws Exception {
		String[] dataSourceNames = applicationContext.getBeanNamesForType(BasicDataSource.class);
		for (int i=0; i<dataSourceNames.length; i++) {
			BasicDataSource dataSource = (BasicDataSource) applicationContext.getBean(dataSourceNames[i]);
			if (null != dataSource) {
				dataSource.close();
			}
		}
	}
	
}