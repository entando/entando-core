package com.agiletec.plugins.jacms.aps.system.services.content.model;

import java.util.Date;
import java.util.stream.Collectors;

public class ContentDto extends EntityDto {

    private String status;
    private boolean onLine;
    private String viewPage;
    private String listModel;
    private String defaultModel;

    private Date created;
    private Date lastModified;

    private String version;
    private String firstEditor;
    private String lastEditor;

    public ContentDto() {}

    public ContentDto(Content src) {

        this.setId(src.getId());
        this.setTypeCode(src.getTypeCode());
        this.setTypeDescription(src.getTypeDescription());
        this.setDescription(src.getDescription());
        this.setMainGroup(src.getMainGroup());
        this.setGroups(src.getGroups());
        //this.setAttributeList(src.getAttributeList());

        if (null != src.getCategories()) {
            this.setCategories(src.getCategories().stream().map(i -> i.getCode()).collect(Collectors.toList()));
        }

        //this.setRenderingLang(src.getRenderingLang());
        //this.setDefaultLang(src.getDefaultLang());

        this.setStatus(src.getStatus());
        this.setOnLine(src.isOnLine());
        this.setViewPage(src.getViewPage());
        this.setListModel(src.getListModel());
        this.setDefaultModel(src.getDefaultModel());

        this.setCreated(src.getCreated());
        this.setLastModified(src.getLastModified());

        this.setVersion(src.getVersion());
        this.setFirstEditor(src.getFirstEditor());
        this.setLastEditor(src.getLastEditor());

    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public boolean isOnLine() {
        return onLine;
    }

    public void setOnLine(boolean onLine) {
        this.onLine = onLine;
    }

    public String getViewPage() {
        return viewPage;
    }

    public void setViewPage(String viewPage) {
        this.viewPage = viewPage;
    }

    public String getListModel() {
        return listModel;
    }

    public void setListModel(String listModel) {
        this.listModel = listModel;
    }

    public String getDefaultModel() {
        return defaultModel;
    }

    public void setDefaultModel(String defaultModel) {
        this.defaultModel = defaultModel;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public Date getLastModified() {
        return lastModified;
    }

    public void setLastModified(Date lastModified) {
        this.lastModified = lastModified;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getFirstEditor() {
        return firstEditor;
    }

    public void setFirstEditor(String firstEditor) {
        this.firstEditor = firstEditor;
    }

    public String getLastEditor() {
        return lastEditor;
    }

    public void setLastEditor(String lastEditor) {
        this.lastEditor = lastEditor;
    }

}
