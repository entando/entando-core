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
package com.agiletec.plugins.jacms.apsadmin.content.attribute.manager.util;

import java.util.ArrayList;
import java.util.List;

import com.agiletec.aps.system.services.group.Group;
import com.agiletec.aps.system.services.page.IPage;
import com.agiletec.aps.system.services.page.IPageManager;
import com.agiletec.aps.system.services.page.Widget;
import com.agiletec.plugins.jacms.aps.system.services.content.IContentManager;
import com.agiletec.plugins.jacms.aps.system.services.content.model.Content;
import com.agiletec.plugins.jacms.aps.system.services.content.model.SymbolicLink;

/**
 * Classe di utilità per i manager degli attributi in cui negli elementi compositivi 
 * vi può essere in link rappresentato dal proprio link simbolico.
 * @author E.Santoboni
 * @deprecated Moved validation into general validation of link and hypertext attribute
 */
public class SymbolicLinkErrorMessenger implements ISymbolicLinkErrorMessenger {
	
	@Override
	public int scan(SymbolicLink symbLink, Content content) {
		int errorCode = MESSAGE_CODE_NO_ERROR;
		if (symbLink != null) {
			switch (symbLink.getDestType()) {
			case SymbolicLink.URL_TYPE:
				break;
			case SymbolicLink.PAGE_TYPE:
				errorCode = this.checkPageDest(symbLink, content);
				break;
			case SymbolicLink.CONTENT_TYPE:
				errorCode = this.checkContentDest(symbLink, content);
				break;
			case SymbolicLink.CONTENT_ON_PAGE_TYPE:
				errorCode = this.checkContentOnPageDest(symbLink, content);
				break;
			}
		}
		return errorCode;
	}
	
	protected int checkPageDest(SymbolicLink symbLink, Content content) {
		String pageCode = symbLink.getPageDest();
		IPage page = this.getPageManager().getPage(pageCode);
		if (null == page) {
			return MESSAGE_CODE_INVALID_PAGE;
		} else {
			if (this.isVoidPage(page)) {
				return MESSAGE_CODE_VOID_PAGE;
			} else {
				String pageGroup = page.getGroup();
				if (!Group.FREE_GROUP_NAME.equals(pageGroup) 
						&& (page.getExtraGroups() == null || !page.getExtraGroups().contains(Group.FREE_GROUP_NAME)) ) {
					//Bisogna controllare che tutti i gruppi abilitati possano accedere alla pagina lincata.
					List<String> linkingContentGroups = new ArrayList<String>();
					linkingContentGroups.add(content.getMainGroup());
					linkingContentGroups.addAll(content.getGroups());
					for (int i=0; i<linkingContentGroups.size(); i++) {
						String groupName = linkingContentGroups.get(i);
						if (!groupName.equals(pageGroup) && !groupName.equals(Group.ADMINS_GROUP_NAME)) {
							//TODO NON DICE QUALE è IL GRUPPO INVALIDO
							return MESSAGE_CODE_INVALID_PAGE_GROUPS;
						}
					}
				}
			}
		}
		return MESSAGE_CODE_NO_ERROR;
	}
	
	protected int checkContentDest(SymbolicLink symbLink, Content content) {
		Content linkedContent = null;
		try {
			linkedContent = this.getContentManager().loadContent(symbLink.getContentDest(), true);
		} catch (Throwable e) {
			throw new RuntimeException("Errore in caricamento contenuto " + symbLink.getContentDest(), e);
		}
		if (null == linkedContent) {
			return MESSAGE_CODE_INVALID_CONTENT;
		}
		if (!Group.FREE_GROUP_NAME.equals(linkedContent.getMainGroup()) && !linkedContent.getGroups().contains(Group.FREE_GROUP_NAME)) {
			//Bisogna controllare che tutti i gruppi abilitati possano accedere al contenuto lincata.
			List<String> linkingContentGroups = new ArrayList<String>();
			linkingContentGroups.add(content.getMainGroup());
			linkingContentGroups.addAll(content.getGroups());
			for (int i=0; i<linkingContentGroups.size(); i++) {
				String groupName = linkingContentGroups.get(i);
				if (!groupName.equals(linkedContent.getMainGroup()) && !linkedContent.getGroups().contains(groupName)) {
					//TODO NON DICE QUALE è IL GRUPPO INVALIDO
					return MESSAGE_CODE_INVALID_CONTENT_GROUPS;
				}
			}
		}
		return MESSAGE_CODE_NO_ERROR;
	}
	
	protected int checkContentOnPageDest(SymbolicLink symbLink, Content content) {
		int errorCode = this.checkContentDest(symbLink, content);
		if (errorCode == MESSAGE_CODE_NO_ERROR) {
			errorCode = this.checkPageDest(symbLink, content);
		}
		return errorCode;
	}
	
	/**
	 * Metodo di servizio: verifica che la pagina abbia dei widget configurate.
	 * Restituisce true nel caso tutti i frame siano vuoti, false in caso che 
	 * anche un frame sia occupato da una showlet.
	 * @param page La pagina da controllare.
	 * @return true nel caso tutti i frame siano vuoti, false in caso 
	 * che anche un frame sia occupato da una showlet.
	 */
	protected boolean isVoidPage(IPage page) {
		Widget[] widgets = page.getWidgets();
		for (int i = 0; i < widgets.length; i++) {
			if (null != widgets[i]) {
				return false;
			}
		}
		return true;
	}
	
	protected IContentManager getContentManager() {
		return _contentManager;
	}
	public void setContentManager(IContentManager contentManager) {
		this._contentManager = contentManager;
	}
	
	protected IPageManager getPageManager() {
		return _pageManager;
	}
	public void setPageManager(IPageManager pageManager) {
		this._pageManager = pageManager;
	}
	
	private IContentManager _contentManager;
	private IPageManager _pageManager;
	
}