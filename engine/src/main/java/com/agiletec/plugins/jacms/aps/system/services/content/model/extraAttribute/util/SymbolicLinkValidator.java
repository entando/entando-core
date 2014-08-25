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
package com.agiletec.plugins.jacms.aps.system.services.content.model.extraAttribute.util;

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
 * Classe di utilità per la validazione degli attributi in cui negli elementi compositivi
 * vi può essere in link rappresentato dal proprio link simbolico.
 * @author E.Santoboni
 */
public class SymbolicLinkValidator {

    public SymbolicLinkValidator(IContentManager contentManager, IPageManager pageManager) {
        this.setContentManager(contentManager);
        this.setPageManager(pageManager);
    }

    /**
     * Analizza un link simbolico ne verifica la correttezza e restituisce
     * un intero rappresentante il codice dell'eventuale errore riscontrato.
     * In caso di link a pagina ed a contenuto controlla
     * la validità dell'elemento referenziato.
     * @param symbLink Il link simbolico da verificare.
     * @param content Il contenuto corrente in fase di verifica.
     * @return Il codice di errore.
     */
    public String scan(SymbolicLink symbLink, Content content) {
        String errorCode = null;
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

    protected String checkPageDest(SymbolicLink symbLink, Content content) {
        String pageCode = symbLink.getPageDest();
        IPage page = this.getPageManager().getPage(pageCode);
        if (null == page) {
            return ICmsAttributeErrorCodes.INVALID_PAGE;
        } else {
            if (this.isVoidPage(page)) {
                return ICmsAttributeErrorCodes.VOID_PAGE;
            } else {
                String pageGroup = page.getGroup();
                if (!Group.FREE_GROUP_NAME.equals(pageGroup)
                        && (page.getExtraGroups() == null || !page.getExtraGroups().contains(Group.FREE_GROUP_NAME))) {
                    //Bisogna controllare che tutti i gruppi abilitati possano accedere alla pagina lincata.
                    List<String> linkingContentGroups = new ArrayList<String>();
                    linkingContentGroups.add(content.getMainGroup());
                    linkingContentGroups.addAll(content.getGroups());
                    for (int i = 0; i < linkingContentGroups.size(); i++) {
                        String groupName = linkingContentGroups.get(i);
                        if (!groupName.equals(pageGroup) && !groupName.equals(Group.ADMINS_GROUP_NAME)) {
                            //TODO NON DICE QUALE è IL GRUPPO INVALIDO
                            return ICmsAttributeErrorCodes.INVALID_PAGE_GROUPS;
                        }
                    }
                }
            }
        }
        return null;
    }

    protected String checkContentDest(SymbolicLink symbLink, Content content) {
        Content linkedContent = null;
        try {
            linkedContent = this.getContentManager().loadContent(symbLink.getContentDest(), true);
        } catch (Throwable e) {
            throw new RuntimeException("Errore in caricamento contenuto " + symbLink.getContentDest(), e);
        }
        if (null == linkedContent) {
            return ICmsAttributeErrorCodes.INVALID_CONTENT;
        }
        if (!Group.FREE_GROUP_NAME.equals(linkedContent.getMainGroup()) && !linkedContent.getGroups().contains(Group.FREE_GROUP_NAME)) {
            //Bisogna controllare che tutti i gruppi abilitati possano accedere al contenuto lincata.
            List<String> linkingContentGroups = new ArrayList<String>();
            linkingContentGroups.add(content.getMainGroup());
            linkingContentGroups.addAll(content.getGroups());
            for (int i = 0; i < linkingContentGroups.size(); i++) {
                String groupName = linkingContentGroups.get(i);
                if (!groupName.equals(linkedContent.getMainGroup()) && !linkedContent.getGroups().contains(groupName)) {
                    //TODO NON DICE QUALE è IL GRUPPO INVALIDO
                    return ICmsAttributeErrorCodes.INVALID_CONTENT_GROUPS;
                }
            }
        }
        return null;
    }

    protected String checkContentOnPageDest(SymbolicLink symbLink, Content content) {
        String errorCode = this.checkContentDest(symbLink, content);
        if (errorCode == null) {
            errorCode = this.checkPageDest(symbLink, content);
        }
        return errorCode;
    }

    /**
     * Metodo di servizio: verifica che la pagina abbia dei widget configurate.
     * Restituisce true nel caso tutti i frame siano vuoti, false in caso che
     * anche un frame sia occupato da una widget.
     * @param page La pagina da controllare.
     * @return true nel caso tutti i frame siano vuoti, false in caso
     * che anche un frame sia occupato da una widget.
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
    protected void setContentManager(IContentManager contentManager) {
        this._contentManager = contentManager;
    }

    protected IPageManager getPageManager() {
        return _pageManager;
    }
    protected void setPageManager(IPageManager pageManager) {
        this._pageManager = pageManager;
    }

    private IContentManager _contentManager;
    private IPageManager _pageManager;

}