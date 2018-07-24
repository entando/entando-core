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

import javax.servlet.http.HttpSession;

import com.agiletec.apsadmin.system.BaseAction;
import com.agiletec.plugins.jacms.aps.system.services.content.model.Content;
import com.agiletec.plugins.jacms.aps.system.services.content.model.SymbolicLink;
import com.agiletec.plugins.jacms.apsadmin.content.ContentActionConstants;
import com.agiletec.plugins.jacms.apsadmin.content.attribute.action.link.helper.ILinkAttributeActionHelper;
import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * Classe action delegata alla gestione dei link esterni nelle 
 * operazioni sugli attributi tipo Link.
 * @author E.Santoboni
 */
public class UrlLinkAction extends BaseAction {
	
	/**
	 * Gestisce la richiesta di associazione di un link esterno.
	 * @return Il codice del risultato.
	 */
	public String joinUrlLink() {
		String[] destinations = {this.getUrl(), null, null};
		this.buildEntryContentAnchorDest();
		this.getLinkAttributeHelper().joinLink(destinations, SymbolicLink.URL_TYPE, createPropertiesMap(),  this.getRequest());
		return SUCCESS;
	}


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
	
	public Content getContent() {
		return (Content) this.getRequest().getSession()
				.getAttribute(ContentActionConstants.SESSION_PARAM_NAME_CURRENT_CONTENT_PREXIX + this.getContentOnSessionMarker());
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
	
	public String getUrl() {
		return _url;
	}

	public void setUrl(String url) {
		this._url = url;
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

	public void setLinkAttributeHRefLang(String linkAttributeHRrefLang) {
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
	
	private String _url;
	
	private String _entryContentAnchorDest;
	
	private ILinkAttributeActionHelper _linkAttributeHelper;

	private String linkAttributeRel;

	private String linkAttributeTarget;

	private String linkAttributeHRefLang;

}