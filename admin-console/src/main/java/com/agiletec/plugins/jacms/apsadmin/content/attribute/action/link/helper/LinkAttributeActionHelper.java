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
package com.agiletec.plugins.jacms.apsadmin.content.attribute.action.link.helper;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.agiletec.aps.system.common.entity.model.attribute.AttributeInterface;
import com.agiletec.aps.system.common.entity.model.attribute.CompositeAttribute;
import com.agiletec.aps.system.common.entity.model.attribute.MonoListAttribute;
import com.agiletec.plugins.jacms.aps.system.services.content.model.Content;
import com.agiletec.plugins.jacms.aps.system.services.content.model.SymbolicLink;
import com.agiletec.plugins.jacms.aps.system.services.content.model.extraAttribute.LinkAttribute;
import com.agiletec.plugins.jacms.apsadmin.content.ContentActionConstants;
import com.agiletec.plugins.jacms.apsadmin.content.attribute.action.link.ILinkAttributeAction;

/**
 * Classe helper base per le action delegata 
 * alla gestione delle operazione sugli attributi link.
 * @author E.Santoboni
 */
public class LinkAttributeActionHelper implements ILinkAttributeActionHelper {
	
	@Override
	public void initSessionParams(ILinkAttributeAction action, HttpServletRequest request) {
		AttributeInterface attribute = null;
		HttpSession session = request.getSession();
		if (null != action.getParentAttributeName()) {
			attribute = (AttributeInterface) getContent(request).getAttribute(action.getParentAttributeName());
			session.setAttribute(ATTRIBUTE_NAME_SESSION_PARAM, action.getParentAttributeName());
			session.setAttribute(INCLUDED_ELEMENT_NAME_SESSION_PARAM, action.getAttributeName());
		} else {
			attribute = (AttributeInterface) getContent(request).getAttribute(action.getAttributeName());
			session.setAttribute(ATTRIBUTE_NAME_SESSION_PARAM, action.getAttributeName());
		}
		if (action.getElementIndex()>=0) {
			session.setAttribute(LIST_ELEMENT_INDEX_SESSION_PARAM, new Integer(action.getElementIndex()));
		}
		session.setAttribute(LINK_LANG_CODE_SESSION_PARAM, action.getLangCode());
		LinkAttribute linkAttribute = (LinkAttribute) getLinkAttribute(attribute, request);
		session.setAttribute(SYMBOLIC_LINK_SESSION_PARAM, linkAttribute.getSymbolicLink());
	}
	
	@Override
	public void joinLink(String[] destinations, int destType, HttpServletRequest request) {
		HttpSession session = request.getSession();
		Content currentContent = getContent(request);
		String attributeName = (String) session.getAttribute(ATTRIBUTE_NAME_SESSION_PARAM);
		AttributeInterface attribute = (AttributeInterface) currentContent.getAttribute(attributeName);
		joinLink(attribute, destinations, destType, request);
		removeSessionParams(session);
	}
	
	@Override
	public void removeLink(HttpServletRequest request) {
		HttpSession session = request.getSession();
		Content currentContent = getContent(request);
		String attributeName = (String) session.getAttribute(ATTRIBUTE_NAME_SESSION_PARAM);
		AttributeInterface attribute = (AttributeInterface) currentContent.getAttribute(attributeName);
		removeLink(attribute, request);
		removeSessionParams(session);
	}
	
	@Override
	public void removeSessionParams(HttpSession session) {
		session.removeAttribute(ATTRIBUTE_NAME_SESSION_PARAM);
		session.removeAttribute(LINK_LANG_CODE_SESSION_PARAM);
		session.removeAttribute(LIST_ELEMENT_INDEX_SESSION_PARAM);
		session.removeAttribute(INCLUDED_ELEMENT_NAME_SESSION_PARAM);
		session.removeAttribute(SYMBOLIC_LINK_SESSION_PARAM);
	}
	
	@Override
	public String buildEntryContentAnchorDest(HttpSession session) {
		StringBuilder buffer = new StringBuilder("contentedit_");
		buffer.append(session.getAttribute(LINK_LANG_CODE_SESSION_PARAM));
		buffer.append("_" + session.getAttribute(ATTRIBUTE_NAME_SESSION_PARAM));
		return buffer.toString();
	}
	
	protected AttributeInterface getLinkAttribute(AttributeInterface attribute, HttpServletRequest request) {
		HttpSession session = request.getSession();
		if (attribute instanceof CompositeAttribute) {
			String includedAttributeName = (String) session.getAttribute(INCLUDED_ELEMENT_NAME_SESSION_PARAM);
			AttributeInterface includedAttribute = ((CompositeAttribute) attribute).getAttribute(includedAttributeName);
			return getLinkAttribute(includedAttribute, request);
		} else if (attribute instanceof MonoListAttribute) {
			Integer elementIndex = (Integer) session.getAttribute(LIST_ELEMENT_INDEX_SESSION_PARAM);
			AttributeInterface attributeElement = ((MonoListAttribute) attribute).getAttribute(elementIndex.intValue());
			return getLinkAttribute(attributeElement, request);
		} else if (attribute instanceof LinkAttribute) {
			return attribute;
		} else {
			throw new RuntimeException("Caso non gestito : Atttributo tipo " + attribute.getClass());
		}
	}
	
	protected void joinLink(AttributeInterface attribute, String[] destinations, int destType, HttpServletRequest request) {
		HttpSession session = request.getSession();
		if (attribute instanceof CompositeAttribute) {
			String includedAttributeName = (String) session.getAttribute(INCLUDED_ELEMENT_NAME_SESSION_PARAM);
			AttributeInterface includedAttribute = ((CompositeAttribute) attribute).getAttribute(includedAttributeName);
			updateLink(includedAttribute, destinations, destType);
		} else if (attribute instanceof MonoListAttribute) {
			Integer elementIndex = (Integer) session.getAttribute(LIST_ELEMENT_INDEX_SESSION_PARAM);
			AttributeInterface attributeElement = ((MonoListAttribute) attribute).getAttribute(elementIndex.intValue());
			joinLink(attributeElement, destinations, destType, request);
		} else if (attribute instanceof LinkAttribute) {
			updateLink(attribute, destinations, destType);
		}
	}
	
	protected void updateLink(AttributeInterface currentAttribute, String[] destinations, int destType) {
		if (destinations.length!=3) {
			throw new RuntimeException("Destinazioni non riconosciute");
		}
    	SymbolicLink symbolicLink = new SymbolicLink();
        switch (destType) {
        case (SymbolicLink.CONTENT_TYPE):
            symbolicLink.setDestinationToContent(destinations[1]);
            break;
        case (SymbolicLink.CONTENT_ON_PAGE_TYPE):
            symbolicLink.setDestinationToContentOnPage(destinations[1], destinations[2]);
            break;
        case SymbolicLink.PAGE_TYPE:
            symbolicLink.setDestinationToPage(destinations[2]);
            break;
        case SymbolicLink.URL_TYPE:
        	symbolicLink.setDestinationToUrl(destinations[0]);
            break;
        default:
            symbolicLink.setDestinationToContent("");
            break;
        }
        ((LinkAttribute) currentAttribute).setSymbolicLink(symbolicLink);
    }
	
	protected void removeLink(AttributeInterface attribute, HttpServletRequest request) {
		HttpSession session = request.getSession();
		if (attribute instanceof CompositeAttribute) {
			String includedAttributeName = (String) session.getAttribute(INCLUDED_ELEMENT_NAME_SESSION_PARAM);
			AttributeInterface includedAttribute = ((CompositeAttribute) attribute).getAttribute(includedAttributeName);
			removeLink(includedAttribute, request);
		} else if (attribute instanceof LinkAttribute) {
			((LinkAttribute) attribute).setSymbolicLink(null);
			((LinkAttribute) attribute).getTextMap().clear();
		} else if (attribute instanceof MonoListAttribute) {
			Integer elementIndex = (Integer) session.getAttribute(LIST_ELEMENT_INDEX_SESSION_PARAM);
			AttributeInterface attributeElement = ((MonoListAttribute) attribute).getAttribute(elementIndex.intValue());
			removeLink(attributeElement, request);
		}
	}
	
	/**
	 * Restituisce il contenuto in sesione.
	 * @return Il contenuto in sesione.
	 */
	protected Content getContent(HttpServletRequest request) {
		String contentOnSessionMarker = (String) request.getAttribute("contentOnSessionMarker");
		if (null == contentOnSessionMarker || contentOnSessionMarker.trim().length() == 0) {
			contentOnSessionMarker = request.getParameter("contentOnSessionMarker");
		}
		return (Content) request.getSession()
				.getAttribute(ContentActionConstants.SESSION_PARAM_NAME_CURRENT_CONTENT_PREXIX + contentOnSessionMarker);
	}
	
}