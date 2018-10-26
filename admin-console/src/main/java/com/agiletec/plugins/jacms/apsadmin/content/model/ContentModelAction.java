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
package com.agiletec.plugins.jacms.apsadmin.content.model;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agiletec.aps.system.common.entity.model.attribute.AbstractAttribute;
import com.agiletec.aps.system.common.entity.model.attribute.AttributeInterface;
import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.aps.system.services.page.IPage;
import com.agiletec.aps.system.services.page.IPageManager;
import com.agiletec.aps.system.services.page.Widget;
import com.agiletec.apsadmin.system.ApsAdminSystemConstants;
import com.agiletec.apsadmin.system.BaseAction;
import com.agiletec.plugins.jacms.aps.system.services.content.IContentManager;
import com.agiletec.plugins.jacms.aps.system.services.content.model.Content;
import com.agiletec.plugins.jacms.aps.system.services.content.model.ContentRecordVO;
import com.agiletec.plugins.jacms.aps.system.services.content.model.SmallContentType;
import com.agiletec.plugins.jacms.aps.system.services.contentmodel.ContentModel;
import com.agiletec.plugins.jacms.aps.system.services.contentmodel.ContentModelReference;
import com.agiletec.plugins.jacms.aps.system.services.contentmodel.IContentModelManager;
import java.util.HashMap;

/**
 * Classe action delegata alle operazioni sugli oggetti modelli di contenuti.
 *
 * @author E.Santoboni
 */
public class ContentModelAction extends BaseAction implements IContentModelAction {

    private static final Logger _logger = LoggerFactory.getLogger(ContentModelAction.class);

    @Override
    public void validate() {
        super.validate();
        if (this.getStrutsAction() == ApsAdminSystemConstants.ADD) {
            this.checkModelId();
        }
    }

    private void checkModelId() {
        if (null == this.getModelId()) {
            return;
        }
        ContentModel dummyModel = this.getContentModelManager().getContentModel(this.getModelId());
        if (dummyModel != null) {
            this.addFieldError("modelId", this.getText("error.contentModel.modelId.alreadyPresent"));
        }
        SmallContentType utilizer = this.getContentModelManager().getDefaultUtilizer(this.getModelId());
        if (null != utilizer && !utilizer.getCode().equals(this.getContentType())) {
            String[] args = {this.getModelId().toString(), utilizer.getDescr()};
            this.addFieldError("modelId", this.getText("error.contentModel.modelId.wrongUtilizer", args));
        }
    }

    @Override
    public String newModel() {
        this.setStrutsAction(ApsAdminSystemConstants.ADD);
        return SUCCESS;
    }

    @Override
    public String edit() {
        this.setStrutsAction(ApsAdminSystemConstants.EDIT);
        try {
            long modelId = this.getModelId().longValue();
            ContentModel model = this.getContentModelManager().getContentModel(modelId);
            this.setFormValues(model);
        } catch (Throwable t) {
            _logger.error("error in edit", t);
            //ApsSystemUtils.logThrowable(t, this, "edit");
            return FAILURE;
        }
        return SUCCESS;
    }

    @Override
    public String save() {
        try {
            ContentModel model = this.getBeanFromForm();
            if (this.getStrutsAction() == ApsAdminSystemConstants.ADD) {
                this.getContentModelManager().addContentModel(model);
            } else {
                this.getContentModelManager().updateContentModel(model);
            }
        } catch (Throwable t) {
            _logger.error("error in save", t);
            //ApsSystemUtils.logThrowable(t, this, "save");
            return FAILURE;
        }
        return SUCCESS;
    }

    private String checkDelete() {
        String check = null;
        long modelId = this.getModelId().longValue();

        List<ContentModelReference> references = this.getContentModelManager().getContentModelReferences(modelId);
        if (!references.isEmpty()) {
            // sort by page code, status (draft first) and then widget position
            Collections.sort(references, (ref1, ref2) -> {
                int pageCompare = ref1.getPageCode().compareTo(ref2.getPageCode());
                if (pageCompare == 0) {
                    int statusCompare = Boolean.compare(ref1.isOnline(), ref2.isOnline());
                    if (statusCompare == 0) {
                        return Integer.compare(ref1.getWidgetPosition(), ref2.getWidgetPosition());
                    }
                    return statusCompare;
                }
                return pageCompare;
            });

            // build page maps (used for displaying information on the table inside JSP)
            this._onlineReferencedPages = new HashMap<>();
            this._draftReferencedPages = new HashMap<>();
            for (ContentModelReference reference : references) {
                String pageCode = reference.getPageCode();
                if (reference.isOnline()) {
                    if (!this._onlineReferencedPages.containsKey(pageCode)) {
                        this._onlineReferencedPages.put(pageCode, this._pageManager.getOnlinePage(pageCode));
                    }
                } else {
                    if (!this._draftReferencedPages.containsKey(pageCode)) {
                        this._draftReferencedPages.put(pageCode, this._pageManager.getDraftPage(pageCode));
                    }
                }
            }

            this.setContentModelReferences(references);
            check = "references";
        }

        return check;
    }

    @Override
    public String trash() {
        try {
            String check = this.checkDelete();
            if (null != check) {
                return check;
            }
            long modelId = this.getModelId().longValue();
            ContentModel model = this.getContentModelManager().getContentModel(modelId);
            if (null != model) {
                this.setDescription(model.getDescription());
                this.setContentType(model.getContentType());
            } else {
                return "noModel";
            }
        } catch (Throwable t) {
            _logger.error("error in trash", t);
            //ApsSystemUtils.logThrowable(t, this, "trash");
            return FAILURE;
        }
        return SUCCESS;
    }

    @Override
    public String delete() {
        try {
            String check = this.checkDelete();
            if (null != check) {
                return check;
            }
            long modelId = this.getModelId().longValue();
            ContentModel model = this.getContentModelManager().getContentModel(modelId);
            this.getContentModelManager().removeContentModel(model);
        } catch (Throwable t) {
            _logger.error("error in delete", t);
            //ApsSystemUtils.logThrowable(t, this, "delete");
            return FAILURE;
        }
        return SUCCESS;
    }

    public List<SmallContentType> getSmallContentTypes() {
        return this.getContentManager().getSmallContentTypes();
    }

    public Map<String, SmallContentType> getSmallContentTypesMap() {
        return this.getContentManager().getSmallContentTypesMap();
    }

    public SmallContentType getSmallContentType(String typeCode) {
        return this.getContentManager().getSmallContentTypesMap().get(typeCode);
    }

    public ContentModel getContentModel(long modelId) {
        return this.getContentModelManager().getContentModel(modelId);
    }

    private ContentModel getBeanFromForm() {
        ContentModel contentModel = new ContentModel();
        contentModel.setId(this.getModelId());
        contentModel.setContentShape(this.getContentShape());
        contentModel.setContentType(this.getContentType());
        contentModel.setDescription(this.getDescription());
        if (null != this.getStylesheet() && this.getStylesheet().trim().length() > 0) {
            contentModel.setStylesheet(this.getStylesheet());
        }
        if (getStrutsAction() == ApsAdminSystemConstants.EDIT) {
            contentModel.setId(new Long(this.getModelId()).longValue());
        }
        return contentModel;
    }

    private void setFormValues(ContentModel model) {
        this.setModelId(new Integer(String.valueOf(model.getId())));
        this.setDescription(model.getDescription());
        this.setContentType(model.getContentType());
        this.setContentShape(model.getContentShape());
        this.setStylesheet(model.getStylesheet());
    }

    /**
     * Restituisce il contenuto vo in base all'identificativo. Metodo a servizio
     * dell'interfaccia di visualizzazione contenuti in lista.
     *
     * @param contentId L'identificativo del contenuto.
     * @return Il contenuto vo cercato.
     */
    public ContentRecordVO getContentVo(String contentId) {
        ContentRecordVO contentVo = null;
        try {
            contentVo = this.getContentManager().loadContentVO(contentId);
        } catch (Throwable t) {
            _logger.error("error loading getContentVo for {}", contentId, t);
            //ApsSystemUtils.logThrowable(t, this, "getContentVo");
            throw new RuntimeException("Errore in caricamento contenuto vo", t);
        }
        return contentVo;
    }

    /* Used by JSP page */
    public IPage getPage(ContentModelReference reference) {
        if (reference.isOnline()) {
            return _onlineReferencedPages.get(reference.getPageCode());
        } else {
            return _draftReferencedPages.get(reference.getPageCode());
        }
    }

    /* Used by JSP page */
    public Widget getWidget(ContentModelReference reference) {
        return getPage(reference).getWidgets()[reference.getWidgetPosition()];
    }

    /* Used by JSP page */
    public String getWidgetTitle(ContentModelReference reference, String langCode) {
        return getWidget(reference).getType().getTitles().getProperty(langCode);
    }

    public Content getContentPrototype() {
        if (null == this.getContentType()) {
            return null;
        }
        return (Content) this.getContentManager().getEntityPrototype(this.getContentType());
    }

    public List<String> getAllowedAttributeMethods(Content prototype, String attributeName) {
        List<String> methods = new ArrayList<String>();
        try {
            AttributeInterface attribute = (AttributeInterface) prototype.getAttribute(attributeName);
            if (null == attribute) {
                throw new ApsSystemException("Null Attribute '" + attributeName + "' for Content Type '"
                        + prototype.getTypeCode() + "' - '" + prototype.getTypeDescr());
            }
            String methodsString = this.getAllowedPublicAttributeMethods().getProperty(attribute.getType());
            if (null != methodsString) {
                String[] methodsArray = methodsString.split(";");
                methods = Arrays.asList(methodsArray);
            } else {
                BeanInfo beanInfo = Introspector.getBeanInfo(attribute.getClass(), AbstractAttribute.class);
                PropertyDescriptor[] prDescrs = beanInfo.getPropertyDescriptors();
                for (int i = 0; i < prDescrs.length; i++) {
                    PropertyDescriptor propertyDescriptor = prDescrs[i];
                    if (null != propertyDescriptor.getReadMethod()) {
                        methods.add(propertyDescriptor.getDisplayName());
                    }
                }
            }
        } catch (Throwable t) {
            _logger.error("error in getAllowedAttributeMethods", t);
            //ApsSystemUtils.logThrowable(t, this, "getAllowedAttributeMethods");
        }
        return methods;
    }

    public int getStrutsAction() {
        return _strutsAction;
    }
    public void setStrutsAction(int strutsAction) {
        this._strutsAction = strutsAction;
    }

    public Integer getModelId() {
        return _modelId;
    }
    public void setModelId(Integer modelId) {
        this._modelId = modelId;
    }

    public String getContentType() {
        return _contentType;
    }
    public void setContentType(String contentType) {
        this._contentType = contentType;
    }

    public String getDescription() {
        return _description;
    }
    public void setDescription(String description) {
        this._description = description;
    }

    public String getContentShape() {
        return _contentShape;
    }
    public void setContentShape(String contentShape) {
        this._contentShape = contentShape;
    }

    public String getStylesheet() {
        return _stylesheet;
    }
    public void setStylesheet(String stylesheet) {
        this._stylesheet = stylesheet;
    }

    public List<String> getAllowedPublicContentMethods() {
        return _allowedPublicContentMethods;
    }
    public void setAllowedPublicContentMethods(List<String> allowedPublicContentMethods) {
        this._allowedPublicContentMethods = allowedPublicContentMethods;
    }

    public Properties getAllowedPublicAttributeMethods() {
        return _allowedPublicAttributeMethods;
    }
    public void setAllowedPublicAttributeMethods(Properties allowedPublicAttributeMethods) {
        this._allowedPublicAttributeMethods = allowedPublicAttributeMethods;
    }

    public List<ContentModelReference> getContentModelReferences() {
        return _contentModelReferences;
    }
    protected void setContentModelReferences(List<ContentModelReference> contentModelReferences) {
        this._contentModelReferences = contentModelReferences;
    }

    protected IContentModelManager getContentModelManager() {
        return _contentModelManager;
    }
    public void setContentModelManager(IContentModelManager contentModelManager) {
        this._contentModelManager = contentModelManager;
    }

    protected IContentManager getContentManager() {
        return _contentManager;
    }
    public void setContentManager(IContentManager contentManager) {
        this._contentManager = contentManager;
    }

    public IPageManager getPageManager() {
        return _pageManager;
    }
    public void setPageManager(IPageManager _pageManager) {
        this._pageManager = _pageManager;
    }

    private int _strutsAction;
    private Integer _modelId;
    private String _contentType;
    private String _description;
    private String _contentShape;
    private String _stylesheet;

    private List<String> _allowedPublicContentMethods;
    private Properties _allowedPublicAttributeMethods;

    private List<ContentModelReference> _contentModelReferences;
    private Map<String, IPage> _draftReferencedPages;
    private Map<String, IPage> _onlineReferencedPages;

    private IContentModelManager _contentModelManager;
    private IContentManager _contentManager;
    private IPageManager _pageManager;

}
