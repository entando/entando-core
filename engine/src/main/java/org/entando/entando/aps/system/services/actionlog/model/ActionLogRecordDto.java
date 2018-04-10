package org.entando.entando.aps.system.services.actionlog.model;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

public class ActionLogRecordDto {

    private int id;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createdAt;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updatedAt;
    private String username;
    private String namespace;
    private String actionName;
    private String parameters;

    public ActionLogRecordDto() {}

    public ActionLogRecordDto(ActionLogRecord src) {
        this.setActionName(src.getActionName());
        this.setCreatedAt(src.getActionDate());
        this.setId(src.getId());
        this.setNamespace(src.getNamespace());
        this.setParameters(src.getParameters());
        this.setUpdatedAt(src.getUpdateDate());
        this.setUsername(src.getUsername());
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getNamespace() {
        return namespace;
    }

    public void setNamespace(String namespace) {
        this.namespace = namespace;
    }

    public String getActionName() {
        return actionName;
    }

    public void setActionName(String actionName) {
        this.actionName = actionName;
    }

    public String getParameters() {
        return parameters;
    }

    public void setParameters(String parameters) {
        this.parameters = parameters;
    }

}
