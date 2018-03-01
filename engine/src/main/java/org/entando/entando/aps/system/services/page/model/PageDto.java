/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.entando.entando.aps.system.services.page.model;

import com.agiletec.aps.system.services.page.IPage;
import com.agiletec.aps.util.ApsProperties;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 *
 * @author paddeo
 */
public class PageDto {

    private String code;
    private String status;
    private boolean displayedInMenu;
    private String pageModel;
    private String charset;
    private String contentType;
    private String parentCode;
    private boolean seo;
    private List<TitleDto> titles = new ArrayList<>();
    private String ownerGroup;
    private List<String> joinGroups = new ArrayList<>();
    private int position;

    public PageDto() {
    }

    public PageDto(IPage page) {
        this.setCode(page.getCode());
        this.setStatus(page.isOnline() ? "online" : "draft");
        this.setDisplayedInMenu(page.isShowable());
        this.setPageModel(page.getModel().getCode());
        this.setCharset(page.getCharset());
        this.setContentType(page.getMimeType());
        this.setParentCode(page.getParentCode());
        this.setSeo(page.isUseExtraTitles());
        Optional<ApsProperties> apsTitles = Optional.ofNullable(page.getTitles());
        apsTitles.ifPresent(values -> values.keySet().forEach((lang) -> {
            this.titles.add(new TitleDto((String) lang, (String) values.get(lang)));
        }));
        this.setOwnerGroup(page.getGroup());
        Optional<Set<String>> groups = Optional.ofNullable(page.getExtraGroups());
        groups.ifPresent(values -> values.forEach((group) -> {
            this.joinGroups.add(group);
        }));
        this.setPosition(page.getPosition());
    }

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

    public List<TitleDto> getTitles() {
        return titles;
    }

    public void setTitles(List<TitleDto> titles) {
        this.titles = titles;
    }

    public void addTitle(TitleDto title) {
        this.titles.add(title);
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

    public void addJoinGroup(String joinGroup) {
        this.joinGroups.add(joinGroup);
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public static String getEntityFieldName(String dtoFieldName) {
        switch (dtoFieldName) {
            case "code":
                return "code";
            case "name":
                return "descr";
            default:
                return dtoFieldName;
        }
    }

}
