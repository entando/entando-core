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
package com.agiletec.plugins.jacms.apsadmin.resource;

import com.agiletec.aps.system.common.FieldSearchFilter;
import com.agiletec.aps.system.common.entity.model.EntitySearchFilter;
import com.agiletec.aps.system.services.category.Category;
import com.agiletec.aps.system.services.group.Group;
import com.agiletec.plugins.jacms.aps.system.services.resource.IResourceManager;
import com.agiletec.plugins.jacms.aps.system.services.resource.model.ImageResourceDimension;
import com.agiletec.plugins.jacms.aps.system.services.resource.model.util.IImageDimensionReader;
import com.agiletec.plugins.jacms.apsadmin.util.ResourceIconUtil;

import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Classe Action delegata alla gestione delle operazioni di ricerca risorse.
 *
 * @author E.Santoboni
 */
public class ResourceFinderAction extends AbstractResourceAction {

    private static final Logger logger = LoggerFactory.getLogger(ResourceFinderAction.class);

    private String text;
    private String fileName;
    private String ownerGroupName;
    private String categoryCode;
    private ResourceIconUtil resourceIconUtil;
    private IImageDimensionReader imageDimensionManager;
    private boolean openCollapsed = false;

    private String lastOrder;
    private String lastGroupBy;
    private String groupBy;

    /**
     * Restituisce la lista di identificativi delle risorse che soddisfano i
     * parametri di ricerca immessi.
     *
     * @return La lista di identificativi di risorse.
     * @throws Throwable In caso di errore.
     */
    public List<String> getResources() throws Throwable {
        List<String> resourceIds = null;
        try {
            List<String> userGroups = this.getGroupCodesForSearch();
            List<String> groupCodesForSearch = (userGroups.contains(Group.ADMINS_GROUP_NAME)) ? null : userGroups;
            resourceIds = this.getResourceManager().searchResourcesId(this.createSearchFilters(), this.getCategoryCode(), groupCodesForSearch);
        } catch (Throwable t) {
            logger.error("error in getResources", t);
            throw t;
        }
        return resourceIds;
    }

    protected FieldSearchFilter[] createSearchFilters() {
        FieldSearchFilter typeCodeFilter = new FieldSearchFilter(IResourceManager.RESOURCE_TYPE_FILTER_KEY, this.getResourceTypeCode(), false);
        FieldSearchFilter[] filters = {typeCodeFilter};
        if (!StringUtils.isBlank(this.getOwnerGroupName())) {
            FieldSearchFilter groupFilter = new FieldSearchFilter(IResourceManager.RESOURCE_MAIN_GROUP_FILTER_KEY, this.getOwnerGroupName(), false);
            filters = ArrayUtils.add(filters, groupFilter);
        }
        if (!StringUtils.isBlank(this.getText())) {
            FieldSearchFilter textFilter = new FieldSearchFilter(IResourceManager.RESOURCE_DESCR_FILTER_KEY, this.getText(), true);
            filters = ArrayUtils.add(filters, textFilter);
        }
        if (!StringUtils.isBlank(this.getFileName())) {
            FieldSearchFilter filenameFilter = new FieldSearchFilter(IResourceManager.RESOURCE_FILENAME_FILTER_KEY, this.getFileName(), true);
            filters = ArrayUtils.add(filters, filenameFilter);
        }
        filters = ArrayUtils.add(filters, this.getOrderFilter());
        return filters;
    }

    protected EntitySearchFilter getOrderFilter() {
        String groupBy = this.getGroupBy();
        String order = "ASC";
        if (!StringUtils.isBlank(this.getLastGroupBy())
                && this.getLastGroupBy().equals(this.getGroupBy())
                && !StringUtils.isBlank(this.getLastOrder())) {
            order = (this.getLastOrder().equals("ASC")) ? "DESC" : "ASC";
        }
        String key = null;
        if (StringUtils.isBlank(groupBy) || groupBy.equals("descr")) {
            key = IResourceManager.RESOURCE_DESCR_FILTER_KEY;
        } else if (groupBy.equals("lastModified")) {
            key = IResourceManager.RESOURCE_MODIFY_DATE_FILTER_KEY;
        } else if (groupBy.equals("created")) {
            key = IResourceManager.RESOURCE_CREATION_DATE_FILTER_KEY;
        } else {
            key = IResourceManager.RESOURCE_DESCR_FILTER_KEY;
        }
        EntitySearchFilter filter = new EntitySearchFilter(key, false);
        if (StringUtils.isBlank(order)) {
            filter.setOrder(EntitySearchFilter.ASC_ORDER);
        } else {
            filter.setOrder(order);
        }
        return filter;
    }

    public String changeOrder() throws Exception {
        try {
            if (null == this.getGroupBy()) {
                return SUCCESS;
            }
            if (this.getGroupBy().equals(this.getLastGroupBy())) {
                boolean condition = (null == this.getLastOrder()
                        || this.getLastOrder().equals(EntitySearchFilter.ASC_ORDER));
                String order = (condition ? EntitySearchFilter.DESC_ORDER : EntitySearchFilter.ASC_ORDER);
                this.setLastOrder(order);
            } else {
                this.setLastOrder(EntitySearchFilter.DESC_ORDER);
            }
            this.setLastGroupBy(this.getGroupBy());
        } catch (Throwable t) {
            logger.error("error in changeOrder", t);
            throw new RuntimeException("error in changeOrder", t);
        }
        return this.execute();
    }

    public String getIconFile(String filename) {
        String extension = this.getFileExtension(filename);
        return this.getResourceIconUtil().getIconByExtension(extension);
    }

    public String getFileExtension(String filename) {
        return filename.substring(filename.lastIndexOf('.') + 1).trim();
    }

    public String getMimetype(String filename) {
        return URLConnection.guessContentTypeFromName(filename);
    }

    public List<ImageResourceDimension> getImageDimensions() {
        Map<Integer, ImageResourceDimension> master = this.getImageDimensionManager().getImageDimensions();
        List<ImageResourceDimension> dimensions = new ArrayList<ImageResourceDimension>(master.values());
        BeanComparator comparator = new BeanComparator("dimx");
        Collections.sort(dimensions, comparator);
        return dimensions;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getOwnerGroupName() {
        return ownerGroupName;
    }

    public void setOwnerGroupName(String ownerGroupName) {
        this.ownerGroupName = ownerGroupName;
    }

    public String getCategoryCode() {
        if (this.categoryCode != null && this.getCategoryManager().getRoot().getCode().equals(this.categoryCode)) {
            this.categoryCode = null;
        }
        return categoryCode;
    }

    public void setCategoryCode(String categoryCode) {
        this.categoryCode = categoryCode;
    }

    protected ResourceIconUtil getResourceIconUtil() {
        return resourceIconUtil;
    }

    public void setResourceIconUtil(ResourceIconUtil resourceIconUtil) {
        this.resourceIconUtil = resourceIconUtil;
    }

    protected IImageDimensionReader getImageDimensionManager() {
        return imageDimensionManager;
    }

    public void setImageDimensionManager(IImageDimensionReader imageDimensionManager) {
        this.imageDimensionManager = imageDimensionManager;
    }

    public boolean isOpenCollapsed() {
        boolean hasFilterByCat = false;
        if (null != this.getCategoryCode()) {
            Category category = this.getCategoryManager().getCategory(this.getCategoryCode());
            hasFilterByCat = (null != category && !category.isRoot());
        }
        return (this.openCollapsed || hasFilterByCat
                || !StringUtils.isBlank(this.getFileName())
                || !StringUtils.isBlank(this.getOwnerGroupName()));
    }

    public void setOpenCollapsed(boolean openCollapsed) {
        this.openCollapsed = openCollapsed;
    }

    protected List<String> getGroupCodesForSearch() {
        return super.getActualAllowedGroupCodes();
    }

    public String getLastOrder() {
        return lastOrder;
    }

    public void setLastOrder(String order) {
        this.lastOrder = order;
    }

    public String getLastGroupBy() {
        return lastGroupBy;
    }

    public void setLastGroupBy(String lastGroupBy) {
        this.lastGroupBy = lastGroupBy;
    }

    public String getGroupBy() {
        return groupBy;
    }

    public void setGroupBy(String groupBy) {
        this.groupBy = groupBy;
    }

}
