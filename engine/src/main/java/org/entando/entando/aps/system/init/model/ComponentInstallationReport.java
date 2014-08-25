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

import java.util.Date;

import org.entando.entando.aps.system.init.model.SystemInstallationReport.Status;
import org.jdom.Element;

import com.agiletec.aps.util.DateConverter;

/**
 * @author E.Santoboni
 */
public class ComponentInstallationReport {
	
	private ComponentInstallationReport() {}
	
	protected ComponentInstallationReport(Element element) {
		String componentCode = element.getAttributeValue(SystemInstallationReport.CODE_ATTRIBUTE);
		this.setComponentCode(componentCode);
		String dateString = element.getAttributeValue(SystemInstallationReport.DATE_ATTRIBUTE);
		Date date = DateConverter.parseDate(dateString, SystemInstallationReport.DATE_FORMAT);
		this.setDate(date);
		Element schemaElement = element.getChild(SystemInstallationReport.SCHEMA_ELEMENT);
		if (null != schemaElement) {
			this.setDataSourceReport(new DataSourceInstallationReport(schemaElement));
		}
		Element dataElement = element.getChild(SystemInstallationReport.DATA_ELEMENT);
		if (null != dataElement) {
			this.setDataReport(new DataInstallationReport(dataElement));
		}
		Element postProcessElement = element.getChild(SystemInstallationReport.COMPONENT_POST_PROCESS_ELEMENT);
		if (null != postProcessElement) {
			String postProcessStatusString = postProcessElement.getAttributeValue(SystemInstallationReport.STATUS_ATTRIBUTE);
			if (null != postProcessStatusString) {
				SystemInstallationReport.Status postProcessStatus = 
						Enum.valueOf(SystemInstallationReport.Status.class, postProcessStatusString.toUpperCase());
				this.setPostProcessStatus(postProcessStatus);
			}
		}
	}
	
	public static ComponentInstallationReport getInstance(String componentCode) {
		ComponentInstallationReport report = new ComponentInstallationReport();
		report.setDate(new Date());
		report.setComponentCode(componentCode);
		report.setDataSourceReport(new DataSourceInstallationReport());
		report.setDataReport(new DataInstallationReport());
		return report;
	}
	
	protected Element toJdomElement() {
		Element element = new Element(SystemInstallationReport.COMPONENT_ELEMENT);
		element.setAttribute(SystemInstallationReport.CODE_ATTRIBUTE, this.getComponentCode());
		String dateString = DateConverter.getFormattedDate(this.getDate(), SystemInstallationReport.DATE_FORMAT);
		element.setAttribute(SystemInstallationReport.DATE_ATTRIBUTE, dateString);
		if (null != this.getStatus()) {
			element.setAttribute(SystemInstallationReport.STATUS_ATTRIBUTE, this.getStatus().toString());
		}
		Element schemaElement = this.getDataSourceReport().toJdomElement();
		element.addContent(schemaElement);
		Element dataElement = this.getDataReport().toJdomElement();
		element.addContent(dataElement);
		if (null != this.getPostProcessStatus()) {
			Element postProcessElement = new Element(SystemInstallationReport.COMPONENT_POST_PROCESS_ELEMENT);
			postProcessElement.setAttribute(SystemInstallationReport.STATUS_ATTRIBUTE, this.getPostProcessStatus().toString());
			element.addContent(postProcessElement);
		}
		return element;
	}
	
	public SystemInstallationReport.Status getStatus() {
		SystemInstallationReport.Status schemaStatus = this.getDataSourceReport().getStatus();
		SystemInstallationReport.Status dataStatus = this.getDataReport().getStatus();
		boolean isSchemaStatusSafe = SystemInstallationReport.isSafeStatus(schemaStatus);
		boolean isDataStatusSafe = SystemInstallationReport.isSafeStatus(dataStatus);
		SystemInstallationReport.Status postProcessStatus = this.getPostProcessStatus();
		boolean isPostProcessStatusSafe = SystemInstallationReport.isSafeStatus(postProcessStatus);
		if (!isSchemaStatusSafe || !isDataStatusSafe || 
				(!isPostProcessStatusSafe && !postProcessStatus.equals(SystemInstallationReport.Status.INIT))) {
			return SystemInstallationReport.Status.INCOMPLETE;
		} else if (isSchemaStatusSafe && isDataStatusSafe && isPostProcessStatusSafe) {
			return SystemInstallationReport.Status.OK;
		} else {
			return SystemInstallationReport.Status.INIT;
		}
	}
	
	public boolean isPostProcessExecutionRequired() {
		SystemInstallationReport.Status dataSourceStatus = this.getDataSourceReport().getStatus();
		SystemInstallationReport.Status dataStatus = this.getDataReport().getStatus();
		SystemInstallationReport.Status ok = SystemInstallationReport.Status.OK;
		return (dataSourceStatus.equals(ok) && dataStatus.equals(ok) && !this.getDataReport().isDataAlreadyPresent());
	}
	
	public String getComponentCode() {
		return _componentCode;
	}
	public void setComponentCode(String componentCode) {
		this._componentCode = componentCode;
	}
	
	public Date getDate() {
		return _date;
	}
	protected void setDate(Date date) {
		this._date = date;
	}
	
	public Status getPostProcessStatus() {
		if ("entandoCore".equals(this.getComponentCode())) {
			return Status.NOT_AVAILABLE;
		}
		return _postProcessStatus;
	}
	public void setPostProcessStatus(Status postProcessStatus) {
		this._postProcessStatus = postProcessStatus;
	}
	
	public DataSourceInstallationReport getDataSourceReport() {
		return _dataSourceReport;
	}
	private void setDataSourceReport(DataSourceInstallationReport schemaReport) {
		this._dataSourceReport = schemaReport;
	}
	
	public DataInstallationReport getDataReport() {
		return _dataReport;
	}
	private void setDataReport(DataInstallationReport dataReport) {
		this._dataReport = dataReport;
	}
	
	private String _componentCode;
	private Date _date;
	private SystemInstallationReport.Status _postProcessStatus = SystemInstallationReport.Status.INIT;
	
	private DataSourceInstallationReport _dataSourceReport;
	private DataInstallationReport _dataReport;
	
}
