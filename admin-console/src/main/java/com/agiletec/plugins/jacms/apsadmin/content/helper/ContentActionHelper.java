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
package com.agiletec.plugins.jacms.apsadmin.content.helper;

import com.agiletec.aps.system.common.entity.model.EntitySearchFilter;
import com.agiletec.aps.system.common.entity.model.IApsEntity;
import com.agiletec.aps.system.common.entity.model.attribute.ITextAttribute;
import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.aps.system.services.group.Group;
import com.agiletec.aps.system.services.lang.Lang;
import com.agiletec.aps.system.services.page.IPage;
import com.agiletec.aps.system.services.role.Permission;
import com.agiletec.aps.system.services.user.UserDetails;
import com.agiletec.aps.util.ApsWebApplicationUtils;
import com.agiletec.apsadmin.system.entity.EntityActionHelper;
import com.agiletec.plugins.jacms.aps.system.JacmsSystemConstants;
import com.agiletec.plugins.jacms.aps.system.services.content.ContentUtilizer;
import com.agiletec.plugins.jacms.aps.system.services.content.IContentManager;
import com.agiletec.plugins.jacms.aps.system.services.content.helper.IContentAuthorizationHelper;
import com.agiletec.plugins.jacms.aps.system.services.content.model.Content;
import com.agiletec.plugins.jacms.apsadmin.content.ContentActionConstants;
import com.agiletec.plugins.jacms.apsadmin.util.CmsPageActionUtil;
import com.opensymphony.xwork2.ActionSupport;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.apache.struts2.ServletActionContext;
import org.entando.entando.aps.system.services.actionlog.model.ActivityStreamInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Classe Helper della ContentAction.
 * @author E.Santoboni
 */
public class ContentActionHelper extends EntityActionHelper implements IContentActionHelper {

	private static final Logger _logger = LoggerFactory.getLogger(ContentActionHelper.class);
	
	@Override
	public void updateContent(IApsEntity entity, boolean updateMainGroup, HttpServletRequest request) {
		this.updateEntity(entity, request);
		if (null != entity && updateMainGroup) {
			Content content = (Content) entity;
			if (null == content.getId() && StringUtils.isEmpty(content.getMainGroup())) {
				String mainGroup = request.getParameter("mainGroup");
				if (mainGroup != null) {
					content.setMainGroup(mainGroup);
				}
			}
		}
	}
	
	@Override
	public void updateEntity(IApsEntity entity, HttpServletRequest request) {
		Content content = (Content) entity;
		try {
            if (null != content) {
            	String descr = request.getParameter("descr");
            	if (descr != null) content.setDescription(descr.trim());
            	String status = request.getParameter("status");
            	if (status != null) content.setStatus(status);
				if (null == content.getId()) {
	            	String mainGroup = request.getParameter("mainGroup");
	            	if (mainGroup != null) {
						request.getSession().setAttribute(ContentActionConstants.SESSION_PARAM_NAME_CURRENT_CONTENT_GROUP, mainGroup);
					}
	            }
	            super.updateEntity(content, request);
				String description = content.getDescription();
				if (null == description || description.trim().length() == 0) {
					ITextAttribute titleAttribute = (ITextAttribute) content.getAttributeByRole(JacmsSystemConstants.ATTRIBUTE_ROLE_TITLE);
					if (null != titleAttribute && StringUtils.isNotEmpty(titleAttribute.getText())) {
						content.setDescription(titleAttribute.getText());
					}
				}
            }
        } catch (Throwable t) {
        	_logger.error("ContentActionHelper - updateContent", t);
        	throw new RuntimeException("Error updating Content", t);
        }
	}
	
	@Override
	public void scanEntity(IApsEntity entity, ActionSupport action) {
		Content content = (Content) entity;
		if (null == content) {
    		_logger.error("Null Content");
    		return;
    	}
		String descr = content.getDescription();
    	if (descr == null || descr.length() == 0) {
			action.addFieldError("descr", action.getText("error.content.descr.required"));
		} else {
			int maxLength = 250;
			if (descr.length() > maxLength) {
				String[] args = {String.valueOf(maxLength)};
				action.addFieldError("descr", action.getText("error.content.descr.wrongMaxLength", args));
			}
			if (!descr.matches("([^\"])+")) {
				action.addFieldError("descr", action.getText("error.content.descr.wrongCharacters"));
			}
		}
		if (null == content.getId() && (content.getMainGroup() == null || content.getMainGroup().length() == 0)) {
			action.addFieldError("mainGroup", action.getText("error.content.mainGroup.required"));
		}
		try {
			this.scanReferences(content, action);
			super.scanEntity(content, action);
		} catch (Throwable t) {
			_logger.error("ContentActionHelper - scanEntity", t);
			throw new RuntimeException("Error checking entity", t);
		}
	}
    
	@Override
	public EntitySearchFilter getOrderFilter(String groupBy, String order) {
		String key = null;
		if (null == groupBy || groupBy.trim().length()== 0 || groupBy.equals("lastModified")) {
			key = IContentManager.CONTENT_MODIFY_DATE_FILTER_KEY;
		} else if (groupBy.equals("code")) {
			key = IContentManager.ENTITY_ID_FILTER_KEY;
		} else if (groupBy.equals("descr")) {
			key = IContentManager.CONTENT_DESCR_FILTER_KEY;
		} else if (groupBy.equals("created")) {
			key = IContentManager.CONTENT_CREATION_DATE_FILTER_KEY;
		} else throw new RuntimeException("Invalid Filter '" + groupBy + "'");
		EntitySearchFilter filter = new EntitySearchFilter(key, false);
		if (null == order || order.trim().length() == 0) {
			filter.setOrder(EntitySearchFilter.DESC_ORDER);
		} else {
			filter.setOrder(order);
		}
		return filter;
	}
	
	/**
     * Verifica che l'utente corrente possegga 
     * i diritti di accesso al contenuto selezionato.
     * @param content Il contenuto.
     * @param currentUser Il contenuto corrente.
     * @return True nel caso che l'utente corrente abbia i permessi 
     * di lettura/scrittura sul contenuto, false in caso contrario.
     */
	@Override
	public boolean isUserAllowed(Content content, UserDetails currentUser) {
		try {
			return this.getContentAuthorizationHelper().isAuthToEdit(currentUser, content);
		} catch (Throwable t) {
			_logger.error("Error checking user authority", t);
			throw new RuntimeException("Error checking user authority", t);
		}
	}
	
	@Override
	public Map getReferencingObjects(Content content, HttpServletRequest request) throws ApsSystemException {
    	Map<String, List> references = new HashMap<String, List>();
    	try {
    		String[] defNames = ApsWebApplicationUtils.getWebApplicationContext(request).getBeanNamesForType(ContentUtilizer.class);
			for (int i=0; i<defNames.length; i++) {
				Object service = null;
				try {
					service = ApsWebApplicationUtils.getWebApplicationContext(request).getBean(defNames[i]);
				} catch (Throwable t) {
					_logger.error("error loading ReferencingObject ", t);
					service = null;
				}
				if (service != null) {
					ContentUtilizer contentUtilizer = (ContentUtilizer) service;
					List utilizers = contentUtilizer.getContentUtilizers(content.getId());
					if (utilizers != null && !utilizers.isEmpty()) {
						references.put(contentUtilizer.getName()+"Utilizers", utilizers);
					}
				}
			}
    	} catch (Throwable t) {
			_logger.error("Error extracting referencing object", t);
    		throw new ApsSystemException("Error in hasReferencingObject method", t);
    	}
    	return references;
    }
	
	/**
	 * Controlla le referenziazioni di un contenuto. Verifica la referenziazione di un contenuto con altri contenuti o pagine nel caso 
	 * di operazioni di ripubblicazione di contenuti non del gruppo ad accesso libero.
	 * L'operazione si rende necessaria per ovviare a casi nel cui il contenuto, di un particolare gruppo, sia stato
	 * pubblicato precedentemente in una pagina o referenziato in un'altro contenuto grazie alla associazione di questo con 
	 * altri gruppi abilitati alla visualizzazione. Il controllo evidenzia quali devono essere i gruppi al quale il contenuto 
	 * deve essere necessariamente associato (ed il perchè) per salvaguardare le precedenti relazioni. 
	 * @param content Il contenuto da analizzare.
	 * @param action L'action da valorizzare con i messaggi di errore.
	 * @throws ApsSystemException In caso di errore.
	 */
	@Override
	public void scanReferences(Content content, ActionSupport action) throws ApsSystemException {
		if (!Group.FREE_GROUP_NAME.equals(content.getMainGroup()) && !content.getGroups().contains(Group.FREE_GROUP_NAME)) {
			HttpServletRequest request = ServletActionContext.getRequest();
			try {
				String[] defNames = ApsWebApplicationUtils.getWebApplicationContext(request).getBeanNamesForType(ContentUtilizer.class);
				for (int i=0; i<defNames.length; i++) {
					Object service = null;
					try {
						service = ApsWebApplicationUtils.getWebApplicationContext(request).getBean(defNames[i]);
					} catch (Throwable t) {
						_logger.error("error loading ReferencingObject ", t);
						service = null;
					}
					if (service != null) {
						ContentUtilizer contentUtilizer = (ContentUtilizer) service;
						List<Object> utilizers = contentUtilizer.getContentUtilizers(content.getId());
						if (null == utilizers) {
							continue;
						}
						Lang lang = this.getLangManager().getDefaultLang();
						for (int j = 0; j < utilizers.size(); j++) {
							Object object = utilizers.get(j);
							if (service instanceof IContentManager && object instanceof String) { //Content ID
								Content refContent = this.getContentManager().loadContent(object.toString(), true);
								if (!content.getMainGroup().equals(refContent.getMainGroup()) && 
										!content.getGroups().contains(refContent.getMainGroup())) {
									String[] args = {this.getGroupManager().getGroup(refContent.getMainGroup()).getDescription(), object.toString()+" '"+refContent.getDescription()+"'"};
									action.addFieldError("mainGroup", action.getText("error.content.referencedContent.wrongGroups", args));
								}
							} else if (object instanceof IPage) { //Content ID
								IPage page = (IPage) object;
								if (!CmsPageActionUtil.isContentPublishableOnPage(content, page)) {
									List<String> pageGroups = new ArrayList<String>();
									pageGroups.add(page.getGroup());
									if (null != page.getExtraGroups()) {
										pageGroups.addAll(page.getExtraGroups());
									}
									String[] args = {pageGroups.toString(), page.getTitle(lang.getCode())};
									action.addFieldError("mainGroup", action.getText("error.content.referencedPage.wrongGroups", args));
								}
							}
						}
					}
				}
			} catch (Throwable t) {
				throw new ApsSystemException("Error in hasReferencingObject method", t);
			}
		}
	}
	
	@Override
	public ActivityStreamInfo createActivityStreamInfo(Content content, int strutsAction, boolean addLink) {
		ActivityStreamInfo asi = new ActivityStreamInfo();
		asi.setActionType(strutsAction);
		Lang defaultLang = this.getLangManager().getDefaultLang();
		Properties titles = new Properties();
		titles.setProperty(defaultLang.getCode(), (null != content.getDescription()) ? content.getDescription() : "-");
		asi.setObjectTitles(titles);
		if (addLink) {
			asi.setLinkNamespace("/do/jacms/Content");
			asi.setLinkActionName("edit");
			asi.addLinkParameter("contentId", content.getId());
			asi.setLinkAuthGroup(content.getMainGroup());
			asi.setLinkAuthPermission(Permission.CONTENT_EDITOR);
		}
		List<String> groupCodes = new ArrayList<String>();
		if (null != content.getMainGroup()) {
			groupCodes.addAll(content.getGroups());
		}
		groupCodes.add(content.getMainGroup());
		asi.setGroups(groupCodes);
		return asi;
	}
	
	protected IContentManager getContentManager() {
		return _contentManager;
	}
	public void setContentManager(IContentManager contentManager) {
		this._contentManager = contentManager;
	}
	
	protected IContentAuthorizationHelper getContentAuthorizationHelper() {
		return _contentAuthorizationHelper;
	}
	public void setContentAuthorizationHelper(IContentAuthorizationHelper contentAuthorizationHelper) {
		this._contentAuthorizationHelper = contentAuthorizationHelper;
	}
	
	private IContentManager _contentManager;
	
	private IContentAuthorizationHelper _contentAuthorizationHelper;
	
}
