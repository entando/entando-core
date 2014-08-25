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

import java.io.StringReader;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agiletec.aps.util.DateConverter;

/**
 * @author E.Santoboni
 */
public class SystemInstallationReport {

	private static final Logger _logger = LoggerFactory.getLogger(SystemInstallationReport.class);
	
	private SystemInstallationReport(Status status) {
		this.setStatus(status);
		this.setCreation(new Date());
	}
	
	public SystemInstallationReport(String xmlText) {
		if (null == xmlText || xmlText.trim().length() == 0) {
			this.setStatus(Status.PORTING);
			return;
		}
		SAXBuilder builder = new SAXBuilder();
		builder.setValidation(false);
		StringReader reader = new StringReader(xmlText);
		try {
			Document doc = builder.build(reader);
			Element rootElement = doc.getRootElement();
			String statusString = rootElement.getAttributeValue(STATUS_ATTRIBUTE);
			if (null != statusString && statusString.trim().length() > 0) {
				SystemInstallationReport.Status status = Enum.valueOf(SystemInstallationReport.Status.class, statusString.toUpperCase());
				this.setStatus(status);
			}
			Element creationElement = rootElement.getChild(CREATION_ELEMENT);
			if (null != creationElement) {
				Date date = DateConverter.parseDate(creationElement.getText(), DATE_FORMAT);
				this.setCreation(date);
			}
			Element lastUpdateElement = rootElement.getChild(LAST_UPDATE_ELEMENT);
			if (null != lastUpdateElement) {
				Date date = DateConverter.parseDate(lastUpdateElement.getText(), DATE_FORMAT);
				this.setLastUpdate(date);
			}
			Element componentsElement = rootElement.getChild(COMPONENTS_ELEMENT);
			if (null == componentsElement) return;
			List<Element> elements = componentsElement.getChildren(COMPONENT_ELEMENT);
			for (int i = 0; i < elements.size(); i++) {
				Element element = elements.get(i);
				ComponentInstallationReport report = new ComponentInstallationReport(element);
				this.getReports().add(report);
			}
		} catch (Throwable t) {
			_logger.error("Error parsing Report. xml: {} ", xmlText, t);
			throw new RuntimeException("Error detected while parsing the XML", t);
		}
	}
	
	public static boolean isSafeStatus(Status status) {
		if (null == status) {
			return false;
		}
		return (status.equals(SystemInstallationReport.Status.OK) || 
				status.equals(SystemInstallationReport.Status.PORTING) || 
				status.equals(SystemInstallationReport.Status.RESTORE) || 
				status.equals(SystemInstallationReport.Status.NOT_AVAILABLE) || 
				status.equals(SystemInstallationReport.Status.SKIPPED));
	}
	
	public static SystemInstallationReport getInstance() {
		return new SystemInstallationReport(Status.INIT);
	}
	
	public static SystemInstallationReport getPortingInstance() {
		return new SystemInstallationReport(Status.PORTING);
	}
	
	public ComponentInstallationReport addReport(String componentCode) {
		ComponentInstallationReport report = ComponentInstallationReport.getInstance(componentCode);
		this.getReports().add(report);
		this.setUpdated();
		return report;
	}
	
	public void addReport(ComponentInstallationReport report) {
		this.getReports().add(report);
	}
	
	public String toXml() {
		Document doc = new Document();
		Element rootElement = new Element(ROOT_ELEMENT);
		Status status = Status.OK;
		for (int i = 0; i < this.getReports().size(); i++) {
			ComponentInstallationReport componentReport = this.getReports().get(i);
			if (!componentReport.getStatus().equals(Status.OK)) {
				status = componentReport.getStatus();
				break;
			} 
		}
		rootElement.setAttribute(STATUS_ATTRIBUTE, status.toString());
		
		Element creationElement = new Element(CREATION_ELEMENT);
		creationElement.setText(DateConverter.getFormattedDate(this.getCreation(), DATE_FORMAT));
		rootElement.addContent(creationElement);
		
		Element lastUpdateElement = new Element(LAST_UPDATE_ELEMENT);
		lastUpdateElement.setText(DateConverter.getFormattedDate(this.getLastUpdate(), DATE_FORMAT));
		rootElement.addContent(lastUpdateElement);
		
		Element componentsElement = new Element(COMPONENTS_ELEMENT);
		rootElement.addContent(componentsElement);
		for (int i = 0; i < this.getReports().size(); i++) {
			ComponentInstallationReport singleReport = this.getReports().get(i);
			Element componentElement = singleReport.toJdomElement();
			componentsElement.addContent(componentElement);
		}
		doc.setRootElement(rootElement);
		XMLOutputter out = new XMLOutputter();
		Format format = Format.getPrettyFormat();
		format.setIndent("\t");
		out.setFormat(format);
		return out.outputString(doc);
	}
	
	public ComponentInstallationReport getComponentReport(String componentCode, boolean addIfNotExist) {
		for (int i = 0; i < this.getReports().size(); i++) {
			ComponentInstallationReport singleReport = this.getReports().get(i);
			if (singleReport.getComponentCode().equals(componentCode)) {
				return singleReport;
			}
		}
		if (addIfNotExist) {
			return this.addReport(componentCode);
		}
		return null;
	}
	
	public Date getCreation() {
		return _creation;
	}
	protected void setCreation(Date creation) {
		this._creation = creation;
	}
	
	public Date getLastUpdate() {
		return _lastUpdate;
	}
	protected void setLastUpdate(Date lastUpdate) {
		this._lastUpdate = lastUpdate;
	}
	
	public Status getStatus() {
		return _status;
	}
	public void setStatus(Status status) {
		this._status = status;
	}
	
	public boolean isUpdated() {
		return _updated;
	}
	public void setUpdated() {
		this.setLastUpdate(new Date());
		this._updated = true;
	}
	
	public List<ComponentInstallationReport> getReports() {
		return _reports;
	}
	
	private Date _creation;
	private Date _lastUpdate;
	private Status _status;
	private boolean _updated;
	private List<ComponentInstallationReport> _reports = new ArrayList<ComponentInstallationReport>();
	
	public enum Status {OK, PORTING, SKIPPED, RESTORE, INCOMPLETE, NOT_AVAILABLE, INIT}
	
	protected static final String ROOT_ELEMENT = "reports";
	protected static final String CREATION_ELEMENT = "creation";
	protected static final String LAST_UPDATE_ELEMENT = "lastupdate";
	protected static final String COMPONENTS_ELEMENT = "components";
	protected static final String COMPONENT_ELEMENT = "component";
	protected static final String CODE_ATTRIBUTE = "code";
	protected static final String COMPONENT_POST_PROCESS_ELEMENT = "postProcess";
	protected static final String NAME_ATTRIBUTE = "name";
	protected static final String DATE_ATTRIBUTE = "date";
	protected static final String DATA_ELEMENT = "data";
	protected static final String SCHEMA_ELEMENT = "schema";
	protected static final String STATUS_ATTRIBUTE = "status";
	protected static final String DATASOURCE_ELEMENT = "datasource";
	protected static final String TABLE_ELEMENT = "table";
	
	protected static final String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";
	
}
