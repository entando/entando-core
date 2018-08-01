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
package com.agiletec.plugins.jacms.aps.system.services.content.model.extraAttribute.util;

import java.util.Set;

import com.agiletec.aps.system.services.group.Group;
import com.agiletec.aps.system.services.page.IPage;
import com.agiletec.aps.system.services.page.IPageManager;
import com.agiletec.aps.system.services.page.Widget;
import com.agiletec.plugins.jacms.aps.system.services.content.IContentManager;
import com.agiletec.plugins.jacms.aps.system.services.content.model.Content;
import com.agiletec.plugins.jacms.aps.system.services.content.model.SymbolicLink;
import java.util.HashSet;
import org.apache.commons.collections4.CollectionUtils;

/**
 * Classe di utilità per la validazione degli attributi in cui negli elementi
 * compositivi vi può essere in link rappresentato dal proprio link simbolico.
 *
 * @author E.Santoboni
 */
public class SymbolicLinkValidator {

    public SymbolicLinkValidator(IContentManager contentManager, IPageManager pageManager) {
        this.setContentManager(contentManager);
        this.setPageManager(pageManager);
    }

    /**
     * Analizza un link simbolico ne verifica la correttezza e restituisce un
     * intero rappresentante il codice dell'eventuale errore riscontrato. In
     * caso di link a pagina ed a contenuto controlla la validità dell'elemento
     * referenziato.
     *
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
        IPage page = this.getPageManager().getOnlinePage(pageCode);
        if (null == page) {
            return ICmsAttributeErrorCodes.INVALID_PAGE;
        } else if (this.isVoidPage(page)) {
            return ICmsAttributeErrorCodes.VOID_PAGE;
        } else {
            Set<String> pageGroups = new HashSet<>();
            pageGroups.add(page.getGroup());
            Set<String> extraGroups = page.getMetadata().getExtraGroups();
            if (null != extraGroups) {
                pageGroups.addAll(extraGroups);
            }
            if (pageGroups.contains(Group.FREE_GROUP_NAME)) {
                return null;
            }
            Set<String> linkingContentGroups = this.extractContentGroups(content);
            boolean check = CollectionUtils.containsAll(pageGroups, linkingContentGroups);
            if (!check) {
                return ICmsAttributeErrorCodes.INVALID_PAGE_GROUPS;
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
        Set<String> linkedContentGroups = this.extractContentGroups(linkedContent);
        if (linkedContentGroups.contains(Group.FREE_GROUP_NAME)) {
            return null;
        }
        Set<String> linkingContentGroups = this.extractContentGroups(content);
        boolean check = CollectionUtils.containsAll(linkedContentGroups, linkingContentGroups);
        if (!check) {
            return ICmsAttributeErrorCodes.INVALID_CONTENT_GROUPS;
        }
        return null;
    }

    private Set<String> extractContentGroups(Content content) {
        Set<String> groups = new HashSet<>();
        groups.add(content.getMainGroup());
        groups.addAll(content.getGroups());
        return groups;
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
     *
     * @param page La pagina da controllare.
     * @return true nel caso tutti i frame siano vuoti, false in caso che anche
     * un frame sia occupato da una widget.
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
