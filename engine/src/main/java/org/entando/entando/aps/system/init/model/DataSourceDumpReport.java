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
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.BeanComparator;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agiletec.aps.util.DateConverter;

import com.agiletec.aps.system.ApsSystemUtils;
import com.agiletec.aps.util.DateConverter;

/**
 * @author E.Santoboni
 */
public class DataSourceDumpReport {

	private static final Logger _logger = LoggerFactory.getLogger(DataSourceDumpReport.class);
	
	public DataSourceDumpReport(SystemInstallationReport installationReport) {
		List<ComponentInstallationReport> componentInstallationReports = installationReport.getReports();
		for (int i = 0; i < componentInstallationReports.size(); i++) {
			ComponentInstallationReport component = componentInstallationReports.get(i);
			this.addComponentHistory(component);
		}
	}
	
	public DataSourceDumpReport(String xmlText) {
		if (null == xmlText || xmlText.trim().length() == 0) {
			return;
		}
		SAXBuilder builder = new SAXBuilder();
		builder.setValidation(false);
		StringReader reader = new StringReader(xmlText);
		try {
			Document doc = builder.build(reader);
			Element rootElement = doc.getRootElement();
			Element dateElement = rootElement.getChild(DATE_ELEMENT);
			if (null != dateElement) {
				Date date = DateConverter.parseDate(dateElement.getText(), DATE_FORMAT);
				this.setDate(date);
			}
			Element subfolderElement = rootElement.getChild(SUBFOLDER_NAME_ELEMENT);
			if (null != subfolderElement) {
				this.setSubFolderName(subfolderElement.getText());
			}
			
			Element requiredTimeElement = rootElement.getChild(REQUIRED_TIME_ELEMENT);
			if (null != requiredTimeElement) {
				this.setRequiredTime(Long.valueOf(requiredTimeElement.getText()));
			}
			
			Element componentsElement = rootElement.getChild(COMPONENTS_HISTORY_ELEMENT);
			if (null != componentsElement) {
				List<Element> componentElements = componentsElement.getChildren();
				for (int i = 0; i < componentElements.size(); i++) {
					Element componentElement = componentElements.get(i);
					ComponentInstallationReport componentHistory = new ComponentInstallationReport(componentElement);
					this.addComponentHistory(componentHistory);
				}
			}
			List<Element> elements = rootElement.getChildren(DATASOURCE_ELEMENT);
			for (int i = 0; i < elements.size(); i++) {
				Element dataSourceElement = elements.get(i);
				String dataSourceName = dataSourceElement.getAttributeValue(NAME_ATTRIBUTE);
				List<Element> tableElements = dataSourceElement.getChildren();
				for (int j = 0; j < tableElements.size(); j++) {
					Element tableElement = tableElements.get(j);
					TableDumpReport tableDumpReport = new TableDumpReport(tableElement);
					this.addTableReport(dataSourceName, tableDumpReport);
				}
			}
		} catch (Throwable t) {
			_logger.error("Error parsing Report. xml:{} ", xmlText, t);
			throw new RuntimeException("Error detected while parsing the XML", t);
		}
	}
	
	public String toXml() {
		try {
			Document doc = new Document();
			Element rootElement = new Element(ROOT_ELEMENT);
			
			this.addElement(DATE_ELEMENT, DateConverter.getFormattedDate(this.getDate(), DATE_FORMAT), rootElement);
			this.addElement(REQUIRED_TIME_ELEMENT, String.valueOf(this.getRequiredTime()), rootElement);
			this.addElement(SUBFOLDER_NAME_ELEMENT, this.getSubFolderName(), rootElement);
			
			Element components = new Element(COMPONENTS_HISTORY_ELEMENT);
			rootElement.addContent(components);
			List<ComponentInstallationReport> componentsHistory = this.getComponentsHistory();
			for (int i = 0; i < componentsHistory.size(); i++) {
				ComponentInstallationReport componentHistory = componentsHistory.get(i);
				Element element = componentHistory.toJdomElement();
				components.addContent(element);
			}
			
			List<String> dataSourceNames = new ArrayList<String>();
			dataSourceNames.addAll(this.getDataSourcesReports().keySet());
			for (int i = 0; i < dataSourceNames.size(); i++) {
				String dataSourceName = dataSourceNames.get(i);
				Element dataSourceElement = new Element(DATASOURCE_ELEMENT);
				rootElement.addContent(dataSourceElement);
				dataSourceElement.setAttribute(NAME_ATTRIBUTE, dataSourceName);
				List<TableDumpReport> tableReports = this.getDataSourcesReports().get(dataSourceName);
				BeanComparator comparator = new BeanComparator("tableName");
				Collections.sort(tableReports, comparator);
				for (int j = 0; j < tableReports.size(); j++) {
					TableDumpReport tableDumpReport = tableReports.get(j);
					dataSourceElement.addContent(tableDumpReport.toJdomElement());
				}
			}
			doc.setRootElement(rootElement);
			XMLOutputter out = new XMLOutputter();
			Format format = Format.getPrettyFormat();
			format.setIndent("\t");
			out.setFormat(format);
			return out.outputString(doc);
		} catch (Throwable t) {
			_logger.error("Error creating XML", t);
			//ApsSystemUtils.logThrowable(t, this, "toXml");
			throw new RuntimeException("Error creating XML", t);
		}
	}
	
	private void addElement(String name, String text, Element parent) {
		Element element = new Element(name);
		element.setText(text);
		parent.addContent(element);
	}
	
	public Date getDate() {
		return _date;
	}
	public void setDate(Date date) {
		this._date = date;
	}
	
	public String getSubFolderName() {
		return _subFolderName;
	}
	public void setSubFolderName(String subFolderName) {
		this._subFolderName = subFolderName;
	}
	
	public void addTableReport(String dataSourceName, TableDumpReport tableDumpReport) {
		List<TableDumpReport> dataSourceReports = this.getDataSourcesReports().get(dataSourceName);
		if (null == dataSourceReports) {
			dataSourceReports = new ArrayList<TableDumpReport>();
			this.getDataSourcesReports().put(dataSourceName, dataSourceReports);
		}
		dataSourceReports.add(tableDumpReport);
	}
	
	public List<String> getDataSourceNames() {
		List<String> list = new ArrayList<String>();
		if (null != this.getDataSourcesReports()) {
			list.addAll(this.getDataSourcesReports().keySet());
		}
		Collections.sort(list);
		return list;
	}
	
	public Map<String, List<TableDumpReport>> getDataSourcesReports() {
		return _dataSourcesReports;
	}
	public void setDataSourcesReports(Map<String, List<TableDumpReport>> dataSourcesReports) {
		this._dataSourcesReports = dataSourcesReports;
	}
	
	protected void addComponentHistory(ComponentInstallationReport component) {
		this._componentsHistory.add(component);
	} 
	
	public List<ComponentInstallationReport> getComponentsHistory() {
		BeanComparator comparator = new BeanComparator("date");
		Collections.sort(this._componentsHistory, comparator);
		return _componentsHistory;
	}
	public void setComponentsHistory(List<ComponentInstallationReport> componentsHistory) {
		this._componentsHistory = componentsHistory;
	}
	
	public long getRequiredTime() {
		return _requiredTime;
	}
	public void setRequiredTime(long requiredTime) {
		this._requiredTime = requiredTime;
	}
	
	private Date _date;
	private String _subFolderName;
	private Map<String, List<TableDumpReport>> _dataSourcesReports = new HashMap<String, List<TableDumpReport>>();
	private List<ComponentInstallationReport> _componentsHistory = new ArrayList<ComponentInstallationReport>();
	private long _requiredTime;
	
	private static final String ROOT_ELEMENT = "backup";
	private static final String DATE_ELEMENT = "date";
	private static final String SUBFOLDER_NAME_ELEMENT = "subfolder";
	private static final String REQUIRED_TIME_ELEMENT = "requiredTime";
	private static final String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";
	private static final String DATASOURCE_ELEMENT = "datasource";
	private static final String NAME_ATTRIBUTE = "name";
	
	private static final String COMPONENTS_HISTORY_ELEMENT = "componentsHistory";
	
}
