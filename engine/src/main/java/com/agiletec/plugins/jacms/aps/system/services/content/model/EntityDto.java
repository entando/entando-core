package com.agiletec.plugins.jacms.aps.system.services.content.model;

import java.util.List;
import java.util.Set;

import com.agiletec.aps.system.common.entity.model.attribute.AttributeInterface;

public class EntityDto  {

    private String id;
    private String typeCode;
    private String typeDescription;
    private String description;
    private String mainGroup;
    private Set<String> groups;
    private List<AttributeInterface> attributeList;
    private List<String> categories;
    private String renderingLang;
    private String defaultLang;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTypeCode() {
        return typeCode;
    }

    public void setTypeCode(String typeCode) {
        this.typeCode = typeCode;
    }

    public String getTypeDescription() {
        return typeDescription;
    }

    public void setTypeDescription(String typeDescription) {
        this.typeDescription = typeDescription;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getMainGroup() {
        return mainGroup;
    }

    public void setMainGroup(String mainGroup) {
        this.mainGroup = mainGroup;
    }

    public Set<String> getGroups() {
        return groups;
    }

    public void setGroups(Set<String> groups) {
        this.groups = groups;
    }

    public List<AttributeInterface> getAttributeList() {
        return attributeList;
    }

    public void setAttributeList(List<AttributeInterface> attributeList) {
        this.attributeList = attributeList;
    }

    public List<String> getCategories() {
        return categories;
    }

    public void setCategories(List<String> categories) {
        this.categories = categories;
    }

    public String getRenderingLang() {
        return renderingLang;
    }

    public void setRenderingLang(String renderingLang) {
        this.renderingLang = renderingLang;
    }

    public String getDefaultLang() {
        return defaultLang;
    }

    public void setDefaultLang(String defaultLang) {
        this.defaultLang = defaultLang;
    }

}
