/*
 * Copyright 2015-Present Entando Inc. (http://www.entando.com) All rights reserved.
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
package org.entando.entando.web.page.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

/**
 *
 * @author paddeo
 */
public class PageRequest {

    @Size(max = 30, message = "string.size.invalid")
    @NotNull(message = "page.code.notBlank")
    @Pattern(regexp = "[a-zA-Z0-9_]+", message="page.code.wrongCharacters")
    private String code;
    private String status;
    private boolean displayedInMenu;
    @NotNull(message = "page.pageModel.notBlank")
    private String pageModel;
    private String charset;
    private String contentType;
    @NotNull(message = "page.parent.notBlank")
    private String parentCode;
    private boolean seo;
    private Map<String, String> titles = new HashMap<>();
    @NotNull(message = "page.group.notBlank")
    private String ownerGroup;
    private List<String> joinGroups = new ArrayList<>();

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public boolean isDisplayedInMenu() {
        return displayedInMenu;
    }

    public void setDisplayedInMenu(boolean displayedInMenu) {
        this.displayedInMenu = displayedInMenu;
    }

    public String getPageModel() {
        return pageModel;
    }

    public void setPageModel(String pageModel) {
        this.pageModel = pageModel;
    }

    public String getCharset() {
        return charset;
    }

    public void setCharset(String charset) {
        this.charset = charset;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public String getParentCode() {
        return parentCode;
    }

    public void setParentCode(String parentCode) {
        this.parentCode = parentCode;
    }

    public boolean isSeo() {
        return seo;
    }

    public void setSeo(boolean seo) {
        this.seo = seo;
    }

    public Map<String, String> getTitles() {
        return titles;
    }

    public void setTitles(Map<String, String> titles) {
        this.titles = titles;
    }

    public String getOwnerGroup() {
        return ownerGroup;
    }

    public void setOwnerGroup(String ownerGroup) {
        this.ownerGroup = ownerGroup;
    }

    public List<String> getJoinGroups() {
        return joinGroups;
    }

    public void setJoinGroups(List<String> joinGroups) {
        this.joinGroups = joinGroups;
    }

    @Override
    public String toString() {
        return "PageRequest{" + "code=" + code + ", status=" + status + ", displayedInMenu=" + displayedInMenu + ", pageModel=" + pageModel + ", charset=" + charset + ", contentType=" + contentType + ", parentCode=" + parentCode + ", seo=" + seo + ", titles=" + titles + ", ownerGroup=" + ownerGroup + ", joinGroups=" + joinGroups + '}';
    }

}
