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
package org.entando.entando.aps.system.init.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.jdom.Element;

/**
 * @author E.Santoboni
 */
public class DataSourceInstallationReport extends AbstractReport {

	protected DataSourceInstallationReport() {
	}

	protected DataSourceInstallationReport(Element element) {
		List<Element> databaseElements = element.getChildren(SystemInstallationReport.DATASOURCE_ELEMENT);
		for (int i = 0; i < databaseElements.size(); i++) {
			Element databaseElement = databaseElements.get(i);
			String dbName = databaseElement.getAttributeValue(SystemInstallationReport.NAME_ATTRIBUTE);
			String dbStatusString = databaseElement.getAttributeValue(SystemInstallationReport.STATUS_ATTRIBUTE);
			SystemInstallationReport.Status dbStatus = Enum.valueOf(SystemInstallationReport.Status.class, dbStatusString.toUpperCase());
			this.getDatabaseStatus().put(dbName, dbStatus);
			List<String> tables = new ArrayList<String>();
			List<Element> databaseTableElements = databaseElement.getChildren(SystemInstallationReport.TABLE_ELEMENT);
			for (int j = 0; j < databaseTableElements.size(); j++) {
				Element databaseTableElement = databaseTableElements.get(j);
				tables.add(databaseTableElement.getAttributeValue(SystemInstallationReport.NAME_ATTRIBUTE));
			}
			this.getDataSourceTables().put(dbName, tables);
		}
	}

	protected Element toJdomElement() {
		Element element = new Element(SystemInstallationReport.SCHEMA_ELEMENT);
		element.setAttribute(SystemInstallationReport.STATUS_ATTRIBUTE, this.getStatus().toString());
		Iterator<String> nameIter = this.getDatabaseStatus().keySet().iterator();
		while (nameIter.hasNext()) {
			String dbName = nameIter.next();
			Element dbElement = new Element(SystemInstallationReport.DATASOURCE_ELEMENT);
			dbElement.setAttribute(SystemInstallationReport.NAME_ATTRIBUTE, dbName);
			dbElement.setAttribute(SystemInstallationReport.STATUS_ATTRIBUTE, this.getDatabaseStatus().get(dbName).toString());
			element.addContent(dbElement);
			List<String> tables = this.getDataSourceTables().get(dbName);
			if (null == tables) {
				continue;
			}
			for (int i = 0; i < tables.size(); i++) {
				String table = tables.get(i);
				Element tableElement = new Element(SystemInstallationReport.TABLE_ELEMENT);
				tableElement.setAttribute(SystemInstallationReport.NAME_ATTRIBUTE, table);
				dbElement.addContent(tableElement);
			}
		}
		return element;
	}

	public Map<String, List<String>> getDataSourceTables() {
		return _dataSourceTables;
	}
	private Map<String, List<String>> _dataSourceTables = new HashMap<String, List<String>>();
}