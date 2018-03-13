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
package com.agiletec;

import javax.servlet.ServletContext;

import org.apache.commons.dbcp.BasicDataSource;
import org.entando.entando.TestEntandoJndiUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.AbstractApplicationContext;
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
        TestEntandoJndiUtils.setupJndi();
		XmlWebApplicationContext applicationContext = new XmlWebApplicationContext();
		applicationContext.setConfigLocations(this.getSpringConfigFilePaths());
		applicationContext.setServletContext(srvCtx);
		applicationContext.refresh();
		return applicationContext;
	}
	
	/**
	 * Restituisce l'insieme dei file di configurazione dei bean definiti nel sistema.
	 * Il metodo và esteso nel caso si inseriscano file di configurazioni esterni al Core ed ai Plugin.
	 * @return L'insieme dei file di configurazione definiti nel sistema.
	 */
	protected String[] getSpringConfigFilePaths() {
    	String[] filePaths = new String[6];
        filePaths[0] = "classpath:spring/testpropertyPlaceholder.xml";
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