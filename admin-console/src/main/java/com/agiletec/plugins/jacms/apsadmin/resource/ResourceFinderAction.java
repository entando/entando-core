/*
 * Copyright 2013-Present Entando Inc. (http://www.entando.com) All rights reserved.
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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Classe Action delegata alla gestione delle operazioni di ricerca risorse.
 * @author E.Santoboni
 */
public class ResourceFinderAction extends AbstractResourceAction implements IResourceFinderAction {
 
	private static final Logger _logger = LoggerFactory.getLogger(ResourceFinderAction.class);
	
	@Override
    public List<String> getResources() throws Throwable {
        List<String> resourceIds = null;
        try {
			List<String> codesForSearch = this.getGroupCodesForSearch();
			resourceIds = this.getResourceManager().searchResourcesId(this.getResourceTypeCode(), 
					this.getText(), this.getFileName(), this.getCategoryCode(), codesForSearch);
		} catch (Throwable t) {
        	_logger.error("error in getResources", t);
            throw t;
        }
        return resourceIds;
    }
	
	protected List<String> getGroupCodesForSearch() {
		List<String> groupCodes = super.getActualAllowedGroupCodes();
		String ownerGroup = this.getOwnerGroupName();
		List<String> codesForSearch = new ArrayList<String>();
		if (StringUtils.isEmpty(ownerGroup)) {
			codesForSearch.addAll(groupCodes);
		} else {
			if (groupCodes.contains(ownerGroup)) {
				codesForSearch.add(ownerGroup);
			}
		}
		return codesForSearch;
	}
    
    public String getIconFile(String filename) {
		String extension = this.getFileExtension(filename);
        return this.getResourceIconUtil().getIconByExtension(extension);
    }
	
	public String getFileExtension(String filename) {
		return filename.substring(filename.lastIndexOf('.')+1).trim();
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
        return _text;
    }
    public void setText(String text) {
        this._text = text;
    }
    
    public String getFileName() {
        return _fileName;
    }
    public void setFileName(String fileName) {
        this._fileName = fileName;
    }
    
    public String getOwnerGroupName() {
        return _ownerGroupName;
    }
    public void setOwnerGroupName(String ownerGroupName) {
        this._ownerGroupName = ownerGroupName;
    }
    
    public String getCategoryCode() {
        if (this._categoryCode != null && this.getCategoryManager().getRoot().getCode().equals(this._categoryCode)) {
            this._categoryCode = null;
        }
        return _categoryCode;
    }
    public void setCategoryCode(String categoryCode) {
        this._categoryCode = categoryCode;
    }
    
    protected ResourceIconUtil getResourceIconUtil() {
        return _resourceIconUtil;
    }
    public void setResourceIconUtil(ResourceIconUtil resourceIconUtil) {
        this._resourceIconUtil = resourceIconUtil;
    }
    
    protected IImageDimensionReader getImageDimensionManager() {
        return _imageDimensionManager;
    }
    public void setImageDimensionManager(IImageDimensionReader imageDimensionManager) {
        this._imageDimensionManager = imageDimensionManager;
    }
    
    private String _text;
    private String _fileName;
    private String _ownerGroupName;
    private String _categoryCode;
    private ResourceIconUtil _resourceIconUtil;
    private IImageDimensionReader _imageDimensionManager;
    
}