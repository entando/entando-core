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
package com.agiletec.plugins.jacms.apsadmin.content.attribute.action.link;

import com.agiletec.aps.system.services.group.Group;
import com.agiletec.aps.system.services.page.IPage;
import com.agiletec.aps.util.ApsWebApplicationUtils;
import com.agiletec.apsadmin.portal.PageTreeAction;
import com.agiletec.plugins.jacms.aps.system.JacmsSystemConstants;
import com.agiletec.plugins.jacms.aps.system.services.content.IContentManager;
import com.agiletec.plugins.jacms.aps.system.services.content.model.Content;
import com.agiletec.plugins.jacms.aps.system.services.content.model.ContentRecordVO;
import com.agiletec.plugins.jacms.aps.system.services.content.model.SymbolicLink;
import com.agiletec.plugins.jacms.apsadmin.content.ContentActionConstants;
import com.agiletec.plugins.jacms.apsadmin.content.attribute.action.link.helper.ILinkAttributeActionHelper;

import java.util.*;

import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Classe action delegata alla gestione dei link a pagina nelle 
 * operazioni sugli attributi tipo Link.
 * @author E.Santoboni
 */
public class PageLinkAction extends PageTreeAction {
	
	private static final Logger _logger = LoggerFactory.getLogger(PageLinkAction.class);


	@Override
	public String execute() throws Exception {
		String result= super.execute();
		if (result.equals(SUCCESS)) {
			Map<String, String> properties = (Map<String, String>) this.getRequest().getSession().getAttribute(ILinkAttributeActionHelper.LINK_PROPERTIES_MAP_SESSION_PARAM);
			if (null != properties) {
				this.linkAttributeRel = properties.get("rel");
				this.linkAttributeTarget = properties.get("target");
				this.linkAttributeHRefLang = properties.get("hreflang");
			}
		}
		return result;
	}

	@Override
	public void validate() {
		super.validate();
		if (this.getFieldErrors().isEmpty()) {
			IPage linkedPage = this.getPageManager().getOnlinePage(this.getSelectedNode());
			if (null == linkedPage) {
				this.addFieldError("selectedNode", this.getText("error.content.link.pageNotExist"));
			}
		}
	}
	
	/**
	 * Gestisce la richiesta di associazione di un link a pagina.
	 * In questo metodo vine anche gestita la richiesta di link di contenuto su pagina. 
	 * @return Il codice del risultato.
	 */
	public String joinLink() {
		int destType = this.getLinkType();
		String[] destinations = {null, this.getContentId(), this.getSelectedNode()};
		this.buildEntryContentAnchorDest();
		this.getLinkAttributeHelper().joinLink(destinations, destType, createPropertiesMap(),this.getRequest());
		return SUCCESS;
	}

	private Map<String,String> createPropertiesMap(){
		Map<String,String> properties = new HashMap<>();
		if (StringUtils.isNotBlank(getLinkAttributeRel())) {
			properties.put("rel", getLinkAttributeRel());
		}
		if (StringUtils.isNotBlank(getLinkAttributeTarget())) {
			properties.put("target", getLinkAttributeTarget());
		}
		if (StringUtils.isNotBlank(getLinkAttributeHRefLang())) {
			properties.put("hrefLang",getLinkAttributeHRefLang());
		}
		return properties;
	}

	private void buildEntryContentAnchorDest() {
		HttpSession session = this.getRequest().getSession();
		String anchorDest = this.getLinkAttributeHelper().buildEntryContentAnchorDest(session);
		this.setEntryContentAnchorDest(anchorDest);
	}

	@Override
	protected Collection<String> getNodeGroupCodes() {
		Set<String> groupCodes = new HashSet<String>();
		groupCodes.add(Group.FREE_GROUP_NAME);
		Content currentContent = this.getContent();
		if (null != currentContent.getMainGroup()) {
			groupCodes.add(currentContent.getMainGroup());
		}
		return groupCodes;
	}
	
	public Content getContent() {
		return (Content) this.getRequest().getSession()
				.getAttribute(ContentActionConstants.SESSION_PARAM_NAME_CURRENT_CONTENT_PREXIX + this.getContentOnSessionMarker());
	}
	
	public ContentRecordVO getContentVo(String contentId) {
		ContentRecordVO contentVo = null;
		try {
			IContentManager contentManager = (IContentManager) ApsWebApplicationUtils.getBean(JacmsSystemConstants.CONTENT_MANAGER, this.getRequest());
			contentVo = contentManager.loadContentVO(contentId);
		} catch (Throwable t) {
			_logger.error("error in getContentVo for content {}", contentId, t);
			throw new RuntimeException("Errore in caricamento contenuto vo", t);
		}
		return contentVo;
	}
	
	public String getContentOnSessionMarker() {
		return _contentOnSessionMarker;
	}
	public void setContentOnSessionMarker(String contentOnSessionMarker) {
		this._contentOnSessionMarker = contentOnSessionMarker;
	}
	
	public SymbolicLink getSymbolicLink() {
		return (SymbolicLink) this.getRequest().getSession().getAttribute(ILinkAttributeActionHelper.SYMBOLIC_LINK_SESSION_PARAM);
	}
	
	public String getContentId() {
		return _contentId;
	}
	public void setContentId(String contentId) {
		this._contentId = contentId;
	}
	
	public int getLinkType() {
		return _linkType;
	}
	public void setLinkType(int linkType) {
		this._linkType = linkType;
	}

	public String getLinkAttributeRel() {
		return linkAttributeRel;
	}

	public void setLinkAttributeRel(String linkAttributeRel) {
		this.linkAttributeRel = linkAttributeRel;
	}

	public String getLinkAttributeTarget() {
		return linkAttributeTarget;
	}

	public void setLinkAttributeTarget(String linkAttributeTarget) {
		this.linkAttributeTarget = linkAttributeTarget;
	}

	public String getLinkAttributeHRefLang() {
		return linkAttributeHRefLang;
	}

	public void setLinkAttributeHRrefLang(String linkAttributeHRrefLang) {
		this.linkAttributeHRefLang = linkAttributeHRrefLang;
	}
	public String getEntryContentAnchorDest() {
		if (null == this._entryContentAnchorDest) {
			this.buildEntryContentAnchorDest();
		}
		return _entryContentAnchorDest;
	}
	protected void setEntryContentAnchorDest(String entryContentAnchorDest) {
		this._entryContentAnchorDest = entryContentAnchorDest;
	}
	
	/**
	 * Restituisce la classe helper della gestione degli attributi di tipo Link.
	 * @return La classe helper degli attributi di tipo Link.
	 */
	protected ILinkAttributeActionHelper getLinkAttributeHelper() {
		return _linkAttributeHelper;
	}
	/**
	 * Setta la classe helper della gestione degli attributi di tipo Link.
	 * @param linkAttributeHelper La classe helper degli attributi di tipo Link.
	 */
	public void setLinkAttributeHelper(ILinkAttributeActionHelper linkAttributeHelper) {
		this._linkAttributeHelper = linkAttributeHelper;
	}
	
	private String _contentOnSessionMarker;
	
	private String _contentId;
	private int _linkType;
	
	private String _entryContentAnchorDest;
	
	private ILinkAttributeActionHelper _linkAttributeHelper;

	private String linkAttributeRel;

	private String linkAttributeTarget;

	private String linkAttributeHRefLang;

}