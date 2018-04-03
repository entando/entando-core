package com.agiletec.plugins.jacms.aps.system.services.contentmodel.model;

public class ContentModelDto {

    private Long id;
    private String contentType;
    private String descr;
    private String contentShape;
    private String stylesheet;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public String getDescr() {
        return descr;
    }

    public void setDescr(String descr) {
        this.descr = descr;
    }

    public String getContentShape() {
        return contentShape;
    }

    public void setContentShape(String contentShape) {
        this.contentShape = contentShape;
    }

    public String getStylesheet() {
        return stylesheet;
    }

    public void setStylesheet(String stylesheet) {
        this.stylesheet = stylesheet;
    }
}
