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
package com.agiletec.plugins.jacms.apsadmin.content.attribute.action.resource;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.agiletec.aps.system.SystemConstants;
import com.agiletec.aps.system.common.entity.model.attribute.AttributeInterface;
import com.agiletec.aps.system.common.entity.model.attribute.CompositeAttribute;
import com.agiletec.aps.system.common.entity.model.attribute.MonoListAttribute;
import com.agiletec.aps.system.services.lang.ILangManager;
import com.agiletec.aps.util.ApsWebApplicationUtils;
import com.agiletec.plugins.jacms.aps.system.services.content.model.Content;
import com.agiletec.plugins.jacms.aps.system.services.content.model.extraAttribute.AbstractResourceAttribute;
import com.agiletec.plugins.jacms.aps.system.services.content.model.extraAttribute.ResourceAttributeInterface;
import com.agiletec.plugins.jacms.aps.system.services.resource.model.ResourceInterface;
import com.agiletec.plugins.jacms.apsadmin.content.ContentActionConstants;

/**
 * Classe helper base per le action delegata 
 * alla gestione delle operazione sugli attributi risorsa.
 * @author E.Santoboni
 */
public class ResourceAttributeActionHelper {
	
	public static void initSessionParams(IResourceAttributeAction action, HttpServletRequest request) {
		HttpSession session = request.getSession();
		if (null != action.getParentAttributeName()) {
			session.setAttribute(ATTRIBUTE_NAME_SESSION_PARAM, action.getParentAttributeName());
			session.setAttribute(INCLUDED_ELEMENT_NAME_SESSION_PARAM, action.getAttributeName());
		} else {
			session.setAttribute(ATTRIBUTE_NAME_SESSION_PARAM, action.getAttributeName());
		}
		if (action.getElementIndex()>=0) {
			session.setAttribute(LIST_ELEMENT_INDEX_SESSION_PARAM, new Integer(action.getElementIndex()));
		}
		session.setAttribute(RESOURCE_TYPE_CODE_SESSION_PARAM, action.getResourceTypeCode());
		session.setAttribute(RESOURCE_LANG_CODE_SESSION_PARAM, action.getResourceLangCode());
	}
	
	/**
	 * Associa la risorsa all'attributo del contenuto o all'elemento dell'attributo lista
	 * o all'elemento dell'attributo Composito (sia semplice che in lista).
	 */
	public static void joinResource(ResourceInterface resource, HttpServletRequest request) {
		HttpSession session = request.getSession();
		Content currentContent = ResourceAttributeActionHelper.getContent(request);
		String attributeName = (String) session.getAttribute(ATTRIBUTE_NAME_SESSION_PARAM);
		AttributeInterface attribute = (AttributeInterface) currentContent.getAttribute(attributeName);
		joinResource(attribute, resource, session);
		removeSessionParams(session);
	}
	
	/**
	 * Associa la risorsa all'attributo del contenuto o all'elemento dell'attributo lista
	 * o all'elemento dell'attributo Composito (sia semplice che in lista).
	 */
	private static void joinResource(AttributeInterface attribute, ResourceInterface resource, HttpSession session) {
		if (attribute instanceof CompositeAttribute) {
			String includedAttributeName = (String) session.getAttribute(INCLUDED_ELEMENT_NAME_SESSION_PARAM);
			AttributeInterface includedAttribute = ((CompositeAttribute) attribute).getAttribute(includedAttributeName);
			joinResource(includedAttribute, resource, session);
		} else if (attribute instanceof ResourceAttributeInterface) {
			String langCode = (String) session.getAttribute(RESOURCE_LANG_CODE_SESSION_PARAM);
			langCode = (langCode!=null && !"".equals(langCode)) ? langCode : null;
			((ResourceAttributeInterface) attribute).setResource(resource, langCode);
		} else if (attribute instanceof MonoListAttribute) {
			int elementIndex = ((Integer) session.getAttribute(LIST_ELEMENT_INDEX_SESSION_PARAM)).intValue();
			AttributeInterface attributeElement = ((MonoListAttribute) attribute).getAttribute(elementIndex);
			joinResource(attributeElement, resource, session);
		}
	}
	
	protected static void removeSessionParams(HttpSession session) {
		session.removeAttribute(ATTRIBUTE_NAME_SESSION_PARAM);
		session.removeAttribute(RESOURCE_TYPE_CODE_SESSION_PARAM);
		session.removeAttribute(RESOURCE_LANG_CODE_SESSION_PARAM);
		session.removeAttribute(LIST_ELEMENT_INDEX_SESSION_PARAM);
		session.removeAttribute(INCLUDED_ELEMENT_NAME_SESSION_PARAM);
	}
	
	protected static String buildEntryContentAnchorDest(HttpSession session) {
		StringBuilder buffer = new StringBuilder("contentedit_");
		buffer.append(session.getAttribute(ResourceAttributeActionHelper.RESOURCE_LANG_CODE_SESSION_PARAM));
		buffer.append("_" + session.getAttribute(ResourceAttributeActionHelper.ATTRIBUTE_NAME_SESSION_PARAM));
		return buffer.toString();
	}
	
	/**
	 * Restituisce il contenuto in sesione.
	 * @return Il contenuto in sesione.
	 */
	public static Content getContent(HttpServletRequest request) {
		String contentOnSessionMarker = (String) request.getAttribute("contentOnSessionMarker");
		if (null == contentOnSessionMarker || contentOnSessionMarker.trim().length() == 0) {
			contentOnSessionMarker = request.getParameter("contentOnSessionMarker");
		}
		return (Content) request.getSession()
				.getAttribute(ContentActionConstants.SESSION_PARAM_NAME_CURRENT_CONTENT_PREXIX + contentOnSessionMarker);
	}
	
	public static void removeResource(HttpServletRequest request) {
		HttpSession session = request.getSession();
		String attributeName = (String) session.getAttribute(ATTRIBUTE_NAME_SESSION_PARAM);
		AttributeInterface attribute = (AttributeInterface) getContent(request).getAttribute(attributeName);
		removeResource(attribute, request);
		removeSessionParams(session);
	}
	
	private static void removeResource(AttributeInterface attribute, HttpServletRequest request) {
		HttpSession session = request.getSession();
		if (attribute instanceof CompositeAttribute) {
			String includedAttributeName = (String) session.getAttribute(INCLUDED_ELEMENT_NAME_SESSION_PARAM);
			AttributeInterface includedAttribute = ((CompositeAttribute) attribute).getAttribute(includedAttributeName);
			removeResource(includedAttribute, request);
		} else if (attribute instanceof AbstractResourceAttribute) {
			ILangManager langManager = (ILangManager) ApsWebApplicationUtils.getBean(SystemConstants.LANGUAGE_MANAGER, request);
			String langCode = (String) session.getAttribute(RESOURCE_LANG_CODE_SESSION_PARAM);
			AbstractResourceAttribute resourceAttribute = (AbstractResourceAttribute) attribute;
			if (langCode == null || 
					langCode.length()==0 || 
					langManager.getDefaultLang().getCode().equals(langCode)) {
				resourceAttribute.getResources().clear();
				resourceAttribute.getTextMap().clear();
			} else {
				resourceAttribute.setResource(null, langCode);
				resourceAttribute.setText(null, langCode);
			}
		} else if (attribute instanceof MonoListAttribute) {
			int elementIndex = ((Integer) session.getAttribute(LIST_ELEMENT_INDEX_SESSION_PARAM)).intValue();
			AttributeInterface attributeElement = ((MonoListAttribute) attribute).getAttribute(elementIndex);
			removeResource(attributeElement, request);
		}
	}
	
	public static final String ATTRIBUTE_NAME_SESSION_PARAM = "contentAttributeName";
	public static final String RESOURCE_TYPE_CODE_SESSION_PARAM = "resourceTypeCode";
	public static final String RESOURCE_LANG_CODE_SESSION_PARAM = "resourceLangCode";
	public static final String LIST_ELEMENT_INDEX_SESSION_PARAM = "listElementIndex";
	public static final String INCLUDED_ELEMENT_NAME_SESSION_PARAM = "includedElementName";
	
}
