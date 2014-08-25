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

import java.util.Iterator;
import java.util.List;

import org.jdom.Element;

/**
 * @author E.Santoboni
 */
public class DataInstallationReport extends AbstractReport {
	
	protected DataInstallationReport() {}
	
	protected DataInstallationReport(Element element) {
		List<Element> databaseElements = element.getChildren(SystemInstallationReport.DATASOURCE_ELEMENT);
		for (int i = 0; i < databaseElements.size(); i++) {
			Element databaseElement = databaseElements.get(i);
			String dbName = databaseElement.getAttributeValue(SystemInstallationReport.NAME_ATTRIBUTE);
			String dbStatusString = databaseElement.getAttributeValue(SystemInstallationReport.STATUS_ATTRIBUTE);
			SystemInstallationReport.Status dbStatus = Enum.valueOf(SystemInstallationReport.Status.class, dbStatusString.toUpperCase());
			this.getDatabaseStatus().put(dbName, dbStatus);
		}
	}
	
	protected Element toJdomElement() {
		Element element = new Element(SystemInstallationReport.DATA_ELEMENT);
		element.setAttribute(SystemInstallationReport.STATUS_ATTRIBUTE, this.getStatus().toString());
		Iterator<String> nameIter = this.getDatabaseStatus().keySet().iterator();
		while (nameIter.hasNext()) {
			String dbName = nameIter.next();
			Element dbElement = new Element(SystemInstallationReport.DATASOURCE_ELEMENT);
			dbElement.setAttribute(SystemInstallationReport.NAME_ATTRIBUTE, dbName);
			dbElement.setAttribute(SystemInstallationReport.STATUS_ATTRIBUTE, this.getDatabaseStatus().get(dbName).toString());
			element.addContent(dbElement);
		}
		return element;
	}
	
	/**
	 * Check if the data is already present before the process of the component.
	 * The typical cases are data restore from dump and porting.
	 * @return true if the data is already present before the process of the component.
	 */
	public boolean isDataAlreadyPresent() {
		Iterator<SystemInstallationReport.Status> iter = super.getDatabaseStatus().values().iterator();
		while (iter.hasNext()) {
			SystemInstallationReport.Status status = iter.next();
			if (!status.equals(SystemInstallationReport.Status.PORTING) 
					&& !status.equals(SystemInstallationReport.Status.RESTORE)) {
				return false;
			}
		}
		return true;
	}
	
}