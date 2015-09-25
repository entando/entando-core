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
package org.entando.entando.plugins.jacms.apsadmin.portal.specialwidget.rowcontentlist;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agiletec.aps.system.services.lang.Lang;
import com.agiletec.aps.system.services.page.IPage;
import com.agiletec.aps.util.ApsProperties;
import com.agiletec.apsadmin.portal.specialwidget.SimpleWidgetConfigAction;
import com.agiletec.plugins.jacms.aps.system.services.content.IContentManager;
import com.agiletec.plugins.jacms.aps.system.services.content.model.ContentRecordVO;
import com.agiletec.plugins.jacms.aps.system.services.content.widget.IContentListWidgetHelper;
import com.agiletec.plugins.jacms.aps.system.services.contentmodel.ContentModel;
import com.agiletec.plugins.jacms.aps.system.services.contentmodel.IContentModelManager;
import org.apache.commons.lang3.StringUtils;

import org.entando.entando.plugins.jacms.aps.system.services.content.widget.RowContentListHelper;

/**
 * @author E.Santoboni
 */
public class RowContentListViewerWidgetAction extends SimpleWidgetConfigAction {

	private static final Logger _logger = LoggerFactory.getLogger(RowContentListViewerWidgetAction.class);
	
	@Override
	public void validate() {
		super.validate();
		try {
			if (this.getActionErrors().size()>0 || this.getFieldErrors().size()>0) {
				this.setShowlet(super.createNewShowlet());
				return;
			}
			this.createValuedShowlet();
			this.validateTitle();
			this.validateLink();
		} catch (Throwable t) {
			_logger.error("Error validating row list viewer", t);
		}
	}

	protected void validateTitle() {
		String titleParamPrefix = IContentListWidgetHelper.WIDGET_PARAM_TITLE + "_";
		if (this.isMultilanguageParamValued(titleParamPrefix)) {
			Lang defaultLang = this.getLangManager().getDefaultLang();
			String defaultTitleParam = titleParamPrefix + defaultLang.getCode();
			String defaultTitle = this.getWidget().getConfig().getProperty(defaultTitleParam);
			if (defaultTitle == null || defaultTitle.length() == 0) {
				String[] args = {defaultLang.getDescr()};
				this.addFieldError(defaultTitleParam, this.getText("error.widget.listViewer.defaultLangTitle.required", args));
			}
		}
	}
	
	protected void validateLink() {
		String pageLink = this.getWidget().getConfig().getProperty(IContentListWidgetHelper.WIDGET_PARAM_PAGE_LINK);
		boolean existsPageLink = pageLink != null && this.getPage(pageLink) != null;
		String linkDescrParamPrefix = IContentListWidgetHelper.WIDGET_PARAM_PAGE_LINK_DESCR + "_";
		if (existsPageLink || this.isMultilanguageParamValued(linkDescrParamPrefix)) {
			if (!existsPageLink) {
				this.addFieldError(IContentListWidgetHelper.WIDGET_PARAM_PAGE_LINK, this.getText("error.widget.listViewer.pageLink.required"));
			}
			Lang defaultLang = this.getLangManager().getDefaultLang();
			String defaultLinkDescrParam = linkDescrParamPrefix + defaultLang.getCode();
			String defaultLinkDescr = this.getWidget().getConfig().getProperty(defaultLinkDescrParam);
			if (defaultLinkDescr == null || defaultLinkDescr.length() == 0) {
				String[] args = {defaultLang.getDescr()};
				this.addFieldError(defaultLinkDescrParam, this.getText("error.widget.listViewer.defaultLangLink.required", args));
			}
		}
	}
	
	private boolean isMultilanguageParamValued(String prefix) {
		ApsProperties config = this.getWidget().getConfig();
		if (null == config) return false;
		for (int i = 0; i < this.getLangs().size(); i++) {
			Lang lang = this.getLangs().get(i);
			String paramValue = config.getProperty(prefix+lang.getCode());
			if (null != paramValue && paramValue.trim().length() > 0) return true;
		}
		return false;
	}
	
	@Override
	public String init() {
		try {
			super.init();
			ApsProperties config = this.getWidget().getConfig();
			if (null == config) return SUCCESS;
			this.extractContentProperties(config);
		} catch (Throwable t) {
			_logger.error("error in init", t);
			return FAILURE;
		}
		return SUCCESS;
	}
	
	private void extractContentProperties(ApsProperties config) {
		if (null == config) return;
		String contents = config.getProperty("contents");
		List<Properties> properties = RowContentListHelper.fromParameterToContents(contents);
		this.setContentsProperties(properties);
	}
	
	public String moveContent() {
		return this.moveRemoveContent(true);
	}
	
	public String removeContent() {
		return this.moveRemoveContent(false);
	}
	
	protected String moveRemoveContent(boolean move) {
		try {
			this.createValuedShowlet();
			ApsProperties config = this.getWidget().getConfig();
			if (null == config) return SUCCESS;
			this.extractContentProperties(config);
			List<Properties> contentProperties = this.getContentsProperties();
			int filterIndex = this.getElementIndex();
			if (move) {
				Properties element = contentProperties.get(filterIndex);
				if (this.getMovement().equalsIgnoreCase(MOVEMENT_UP_CODE)){
					if (filterIndex > 0) {
						contentProperties.remove(filterIndex);
						contentProperties.add(filterIndex -1, element);
					}
				} else if (this.getMovement().equalsIgnoreCase(MOVEMENT_DOWN_CODE)) {
					if (filterIndex < contentProperties.size() -1) {
						contentProperties.remove(filterIndex);
						contentProperties.add(filterIndex + 1, element);
					}
				}
			} else {
				contentProperties.remove(filterIndex);
			}
			String newWidgetParam = RowContentListHelper.fromContentsToParameter(contentProperties);
			this.getWidget().getConfig().setProperty("contents", newWidgetParam);
		} catch (Throwable t) {
			String marker = (move) ? "moving" : "removing";
			_logger.error("Error {} content", marker, t);
			return FAILURE;
		}
		return SUCCESS;
	}
	
	public String joinContent() {
		try {
			this.createValuedShowlet();
			ApsProperties config = this.getWidget().getConfig();
			this.extractContentProperties(config);
			ContentRecordVO contentVo = this.getContentVo(this.getContentId());
			if (null == contentVo || !contentVo.isOnLine()) {
				return INPUT;
			}
			List<Properties> contentProperties = this.getContentsProperties();
			Properties properties = new Properties();
			properties.put("contentId", this.getContentId());
			if (!StringUtils.isEmpty(this.getModelId())) {
				properties.put("modelId", this.getModelId());
			}
			contentProperties.add(properties);
			String newWidgetParam = RowContentListHelper.fromContentsToParameter(contentProperties);
			this.getWidget().getConfig().setProperty("contents", newWidgetParam);
		} catch (Throwable t) {
			_logger.error("Error joining content", t);
			return FAILURE;
		}
		return SUCCESS;
	}
	
	public List<IPage> getPages() {
		if (this._pages == null) {
			this._pages = new ArrayList<IPage>();
			IPage root = this.getPageManager().getRoot();
			this.addPages(root, this._pages);
		}
		return this._pages;
	}

	protected void addPages(IPage page, List<IPage> pages) {
		pages.add(page);
		IPage[] children = page.getChildren();
		for (int i=0; i<children.length; i++) {
			this.addPages(children[i], pages);
		}
	}
	
	public ContentRecordVO getContentVo(String contentId) {
		ContentRecordVO contentVo = null;
		try {
			contentVo = this.getContentManager().loadContentVO(contentId);
		} catch (Throwable t) {
			_logger.error("error in getContentVo for content {}", contentId, t);
			throw new RuntimeException("Errore in caricamento contenuto vo", t);
		}
		return contentVo;
	}
	
	public ContentModel getContentModel(String modelId) {
		ContentModel contentModel = null;
		try {
			Integer modelIdInteger = Integer.parseInt(modelId);
			contentModel = this.getContentModelManager().getContentModel(modelIdInteger);
		} catch (Throwable t) {
			_logger.error("error in getContentModel for content model {}", modelId, t);
			throw new RuntimeException("error in getContentModel", t);
		}
		return contentModel;
	}
	
	public List<ContentModel> getModelsForContent(String contentId) {
		if (null == contentId) return new ArrayList<ContentModel>();
		String typeCode = contentId.substring(0, 3);
		return this.getContentModelManager().getModelsForContentType(typeCode);
	}
	
	public List<Properties> getContentsProperties() {
		return _contentsProperties;
	}
	public void setContentsProperties(List<Properties> contentsProperties) {
		this._contentsProperties = contentsProperties;
	}
	
	public String getMaxElemForItem() {
		return _maxElemForItem;
	}
	public void setMaxElemForItem(String maxElemForItem) {
		this._maxElemForItem = maxElemForItem;
	}
	
	public int getElementIndex() {
		return _elementIndex;
	}
	public void setElementIndex(int elementIndex) {
		this._elementIndex = elementIndex;
	}
	
	public String getMovement() {
		return _movement;
	}
	public void setMovement(String movement) {
		this._movement = movement;
	}
	
	public String getContentId() {
		return _contentId;
	}
	public void setContentId(String contentId) {
		this._contentId = contentId;
	}
	
	public String getModelId() {
		return _modelId;
	}
	public void setModelId(String modelId) {
		this._modelId = modelId;
	}
	
	protected IContentManager getContentManager() {
		return _contentManager;
	}
	public void setContentManager(IContentManager contentManager) {
		this._contentManager = contentManager;
	}
	
	protected IContentModelManager getContentModelManager() {
		return _contentModelManager;
	}
	public void setContentModelManager(IContentModelManager contentModelManager) {
		this._contentModelManager = contentModelManager;
	}
	
	private List<Properties> _contentsProperties = new ArrayList<Properties>();
	
	private String _maxElemForItem;
	
	private int _elementIndex;
	private String _movement;
	
	private String _contentId;
	private String _modelId;
	
	private List<IPage> _pages;
	
	private IContentManager _contentManager;
	private IContentModelManager _contentModelManager;
	
	public static final String MOVEMENT_UP_CODE = "UP";
	public static final String MOVEMENT_DOWN_CODE = "DOWN";

}