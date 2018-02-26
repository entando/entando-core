package org.entando.entando.web.widget.model;

import java.util.Date;
import java.util.List;
import java.util.Map;

public class WidgetRequest {

    private String code;
    private String name;
    private int used;
    private Map<String, String> titles;

    private String group;
    private String customerUi;
    private String defaultUi;
    private Date createdAt;
    private Date updatedAt;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getUsed() {
        return used;
    }

    public void setUsed(int used) {
        this.used = used;
    }

    public  Map<String, String>getTitles() {
        return titles;
    }

    public void setTitles( Map<String, String> titles) {
        this.titles = titles;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public String getCustomerUi() {
        return customerUi;
    }

    public void setCustomerUi(String customerUi) {
        this.customerUi = customerUi;
    }

    public String getDefaultUi() {
        return defaultUi;
    }

    public void setDefaultUi(String defaultUi) {
        this.defaultUi = defaultUi;
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
}
