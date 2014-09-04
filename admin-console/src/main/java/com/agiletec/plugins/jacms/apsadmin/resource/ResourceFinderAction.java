/*
*
* Copyright 2013 Entando S.r.l. (http://www.entando.com) All rights reserved.
*
* This file is part of Entando software.
* Entando is a free software;
* You can redistribute it and/or modify it
* under the terms of the GNU General Public License (GPL) as published by the Free Software Foundation; version 2.
* 
* See the file License for the specific language governing permissions   
* and limitations under the License
* 
* 
* 
* Copyright 2013 Entando S.r.l. (http://www.entando.com) All rights reserved.
*
*/
package com.agiletec.plugins.jacms.apsadmin.resource;

import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.BeanComparator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agiletec.plugins.jacms.aps.system.services.resource.model.ImageResourceDimension;
import com.agiletec.plugins.jacms.aps.system.services.resource.model.util.IImageDimensionReader;
import com.agiletec.plugins.jacms.apsadmin.util.ResourceIconUtil;

/**
 * Classe Action delegata alla gestione delle operazioni di ricerca risorse.
 * @author E.Santoboni
 */
public class ResourceFinderAction extends AbstractResourceAction implements IResourceFinderAction {
 
	private static final Logger _logger = LoggerFactory.getLogger(ResourceFinderAction.class);
	
    public List<String> getResources() throws Throwable {
        List<String> resources = null;
        try {
            resources = this.getResourceActionHelper().searchResources(this.getResourceTypeCode(),
                    this.getText(), this.getOwnerGroupName(), this.getFileName(), this.getCategoryCode(), this.getCurrentUser());
        } catch (Throwable t) {
        	_logger.error("error in getResources", t);
            //ApsSystemUtils.logThrowable(t, this, "getResources");
            throw t;
        }
        return resources;
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