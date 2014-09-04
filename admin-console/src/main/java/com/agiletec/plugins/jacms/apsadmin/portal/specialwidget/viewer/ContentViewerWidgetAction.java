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
package com.agiletec.plugins.jacms.apsadmin.portal.specialwidget.viewer;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agiletec.aps.system.services.page.IPage;
import com.agiletec.apsadmin.portal.specialwidget.SimpleWidgetConfigAction;
import com.agiletec.plugins.jacms.aps.system.services.content.IContentManager;
import com.agiletec.plugins.jacms.aps.system.services.content.model.Content;
import com.agiletec.plugins.jacms.aps.system.services.content.model.ContentRecordVO;
import com.agiletec.plugins.jacms.aps.system.services.contentmodel.ContentModel;
import com.agiletec.plugins.jacms.aps.system.services.contentmodel.IContentModelManager;
import com.agiletec.plugins.jacms.apsadmin.util.CmsPageActionUtil;

/**
 * Action per la gestione della configurazione della showlet erogatore contenuto singolo.
 * @author E.Santoboni
 */
public class ContentViewerWidgetAction extends SimpleWidgetConfigAction implements IContentViewerWidgetAction {

	private static final Logger _logger = LoggerFactory.getLogger(ContentViewerWidgetAction.class);
	
	@Override
	public void validate() {
		super.validate();
		if (this.getFieldErrors().size()==0) {
			try {
				Content publishingContent = this.getContentManager().loadContent(this.getContentId(), true);
				if (null == publishingContent) {
					this.addFieldError("contentId", this.getText("error.widget.viewer.nullContent"));
				} else {
					IPage currentPage = this.getCurrentPage();
					if (!CmsPageActionUtil.isContentPublishableOnPage(publishingContent, currentPage)) {
						List<String> pageGroups = new ArrayList<String>();
						pageGroups.add(currentPage.getGroup());
						if (null != currentPage.getExtraGroups()) {
							pageGroups.addAll(currentPage.getExtraGroups());
						}
						this.addFieldError("contentId", this.getText("error.widget.viewer.invalidContent", new String[]{pageGroups.toString()}));
					}
				}
			} catch (Throwable t) {
				_logger.error("Error validating content {}", this.getContentId(), t);
				//ApsSystemUtils.logThrowable(t, this, "validate", "Errore in validazione contenuto con id " + this.getContentId());
				throw new RuntimeException("Errore in validazione contenuto con id " + this.getContentId(), t);
			}
		}
		if (this.getFieldErrors().size()>0) {
			try {
				this.createValuedShowlet();
			} catch (Throwable t) {
				_logger.error("error creating new widget", t);
				//ApsSystemUtils.logThrowable(t, this, "validate", "Errore in creazione showlet valorizzata");
				throw new RuntimeException("Errore in creazione showlet valorizzata", t);
			}
		}
	}
	
	@Override
	public String joinContent() {
		try {
			this.createValuedShowlet();
		} catch (Throwable t) {
			_logger.error("error in joinContent", t);
			//ApsSystemUtils.logThrowable(t, this, "joinContent");
			throw new RuntimeException("Errore in associazione contenuto", t);
		}
		return SUCCESS;
	}
	
	/**
	 * Restituisce il contenuto vo in base all'identificativo.
	 * @param contentId L'identificativo del contenuto.
	 * @return Il contenuto vo cercato.
	 */
	public ContentRecordVO getContentVo(String contentId) {
		ContentRecordVO contentVo = null;
		try {
			contentVo = this.getContentManager().loadContentVO(contentId);
		} catch (Throwable t) {
			_logger.error("error in getContentVo for content {}", contentId, t);
			//ApsSystemUtils.logThrowable(t, this, "getContentVo");
			throw new RuntimeException("Errore in caricamento contenuto vo", t);
		}
		return contentVo;
	}
	
	/**
	 * Restituisce la lista di Modelli di Contenuto compatibili con il contenuto specificato.
	 * @param contentId Il contenuto cui restituire i modelli compatibili.
	 * @return La lista di Modelli di Contenuto compatibili con il contenuto specificato.
	 */
	public List<ContentModel> getModelsForContent(String contentId) {
		if (null == contentId) return new ArrayList<ContentModel>();
		String typeCode = contentId.substring(0, 3);
		return this.getContentModelManager().getModelsForContentType(typeCode);
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
	
	private IContentModelManager _contentModelManager;
	private IContentManager _contentManager;
	
	private String _contentId;
	private String _modelId;
	
}