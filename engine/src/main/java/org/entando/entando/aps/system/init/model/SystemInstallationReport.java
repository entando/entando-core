/*
 * Copyright 2015-Present Entando S.r.l. (http://www.entando.com) All rights reserved.
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
import java.io.Serializable;

/**
 * @author E.Santoboni
 */
public class SystemInstallationReport implements Serializable {

    private static final Logger logger = LoggerFactory.getLogger(SystemInstallationReport.class);

    public enum Status {
        OK, PORTING, SKIPPED, RESTORE, INCOMPLETE, NOT_AVAILABLE, INIT, UNINSTALLED
    }

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

    private Date creation;
    private Date lastUpdate;
    private Status status;
    private boolean updated;
    private List<ComponentInstallationReport> reports = new ArrayList<>();

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
            if (null == componentsElement) {
                return;
            }
            List<Element> elements = componentsElement.getChildren(COMPONENT_ELEMENT);
            for (int i = 0; i < elements.size(); i++) {
                Element element = elements.get(i);
                ComponentInstallationReport report = new ComponentInstallationReport(element);
                this.getReports().add(report);
            }
        } catch (Throwable t) {
            logger.error("Error parsing Report. xml: {} ", xmlText, t);
            throw new RuntimeException("Error detected while parsing the XML", t);
        }
    }

    public static boolean isSafeStatus(Status status) {
        if (null == status) {
            return false;
        }
        return (status.equals(SystemInstallationReport.Status.OK)
                || status.equals(SystemInstallationReport.Status.PORTING)
                || status.equals(SystemInstallationReport.Status.RESTORE)
                || status.equals(SystemInstallationReport.Status.NOT_AVAILABLE)
                || status.equals(SystemInstallationReport.Status.SKIPPED)
                || status.equals(SystemInstallationReport.Status.UNINSTALLED));
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
        for (ComponentInstallationReport componentReport : this.getReports()) {
            if (!componentReport.getStatus().equals(Status.OK)
                    && !componentReport.getStatus().equals(Status.UNINSTALLED)) {
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
        for (ComponentInstallationReport singleReport : this.getReports()) {
            if (singleReport.getComponentCode().equals(componentCode)) {
                return singleReport;
            }
        }
        if (addIfNotExist) {
            return this.addReport(componentCode);
        }
        return null;
    }

    public boolean removeComponentReport(String componentCode) {
        for (ComponentInstallationReport singleReport : this.getReports()) {
            if (singleReport.getComponentCode().equals(componentCode)) {
                this.getReports().remove(singleReport);
                return true;
            }
        }
        return false;
    }

    public Date getCreation() {
        return creation;
    }

    protected void setCreation(Date creation) {
        this.creation = creation;
    }

    public Date getLastUpdate() {
        return lastUpdate;
    }

    protected void setLastUpdate(Date lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public boolean isUpdated() {
        return updated;
    }

    public void setUpdated() {
        this.setLastUpdate(new Date());
        this.updated = true;
    }

    public List<ComponentInstallationReport> getReports() {
        return reports;
    }

}
