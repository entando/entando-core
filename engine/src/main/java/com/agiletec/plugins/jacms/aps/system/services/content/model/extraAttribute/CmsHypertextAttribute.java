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
package com.agiletec.plugins.jacms.aps.system.services.content.model.extraAttribute;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agiletec.aps.system.SystemConstants;
import com.agiletec.aps.system.common.entity.model.AttributeFieldError;
import com.agiletec.aps.system.common.entity.model.AttributeTracer;
import com.agiletec.aps.system.common.entity.model.attribute.HypertextAttribute;
import com.agiletec.aps.system.services.lang.ILangManager;
import com.agiletec.aps.system.services.lang.Lang;
import com.agiletec.aps.system.services.page.IPageManager;
import com.agiletec.plugins.jacms.aps.system.JacmsSystemConstants;
import com.agiletec.plugins.jacms.aps.system.services.content.IContentManager;
import com.agiletec.plugins.jacms.aps.system.services.content.model.CmsAttributeReference;
import com.agiletec.plugins.jacms.aps.system.services.content.model.Content;
import com.agiletec.plugins.jacms.aps.system.services.content.model.SymbolicLink;
import com.agiletec.plugins.jacms.aps.system.services.content.model.extraAttribute.util.HypertextAttributeUtil;
import com.agiletec.plugins.jacms.aps.system.services.content.model.extraAttribute.util.SymbolicLinkValidator;

/**
 * Rappresenta una informazione di tipo "ipertesto" specifico per il cms.
 * @author W.Ambu - E.Santoboni
 */
public class CmsHypertextAttribute extends HypertextAttribute implements IReferenceableAttribute {

	private static final Logger _logger = LoggerFactory.getLogger(CmsHypertextAttribute.class);
	
    /**
     * Restituisce il testo con modificato con eliminate 
     * l'apertura del primo paragrafo e la chiusura dell'ultimo.
     * @return Il testo modificato.
     */
    public String getTextPLess() {
        String masterText = this.getText();
        String text = masterText.replaceFirst("<p>", "");
        StringBuffer sbuffer = new StringBuffer(text);
        int lastIndexOfP = sbuffer.lastIndexOf("</p>");
        if (lastIndexOfP != -1) {
            sbuffer.replace(lastIndexOfP, lastIndexOfP + 4, "");
        }
        return sbuffer.toString();
    }

    /**
     * Restituisce la porzione di testo totale antecedente ad una 
     * eventuale immagine da inserire internamente all'ipertesto.
     * Il testo viene ricavato dal testo principale la cui fine è corrispondente 
     * all'inizio del paragrafo (apertura inclusa) più vicino al punto 
     * del testo completo ricavato dalla percentuale specificata.
     * @param percent La percentuale, rispetto all'inizio del testo, 
     * rispetto al quale ricavare il punto di taglio.
     * @return La porzione di testo totale antecedente ad una eventuale immagine.
     */
    public String getTextBeforeImage(int percent) {
        String text = super.getText();
        int cutPoint = HypertextAttributeUtil.getIndexCutPoint(text, percent);
        String textBefore = text.substring(0, cutPoint);
        return textBefore;
    }

    /**
     * Restituisce la porzione di testo totale successivo ad una 
     * eventuale immagine da inserire internamente all'ipertesto.
     * Il testo viene ricavato dal testo principale il cui inizio è corrispondente 
     * all'inizio del paragrafo (apertura esclusa) più vicino al punto 
     * del testo completo ricavato dalla percentuale specificata.
     * @param percent La percentuale, rispetto all'inizio del testo, 
     * rispetto al quale ricavare il punto di taglio.
     * @return La porzione di testo totale successiva ad una eventuale immagine.
     */
    public String getTextAfterImage(int percent) {
        String text = super.getText();
        int cutPoint = HypertextAttributeUtil.getIndexCutPoint(text, percent);
        String textAfter = text.substring(cutPoint);
        return textAfter;
    }

    /**
     * Restituisce la porzione di testo totale interposto tra due 
     * eventuali immagini da inserire internamente all'ipertesto.
     * Il testo viene ricavato dal testo principale il cui inizio è corrispondente 
     * all'inizio del paragrafo (apertura esclusa) più vicino al punto 
     * del testo completo ricavato dalla percentuale start specificata, e la cui fine 
     * è corrispondente all'inizio del paragrafo (apertura inclusa) più vicina al punto 
     * del testo completo ricavato dalla percentuale percentEnd specificata.
     * @param percentStart La percentuale, rispetto all'inizio del testo, 
     * rispetto al quale ricavare il punto di taglio iniziale.
     * @param percentEnd La percentuale, rispetto all'inizio del testo, 
     * rispetto al quale ricavare il punto di taglio finale.
     * @return La porzione di testo totale interposto tra due eventuali immagini.
     */
    public String getTextByRange(int percentStart, int percentEnd) {
        String text = super.getText();
        int firstCutPoint = HypertextAttributeUtil.getIndexCutPoint(text, percentStart);
        int endCutPoint = HypertextAttributeUtil.getIndexCutPoint(text, percentEnd);
        String textByRange = text.substring(firstCutPoint, endCutPoint);
        return textByRange;
    }
    
    public List<CmsAttributeReference> getReferences(List<Lang> systemLangs) {
        List<CmsAttributeReference> refs = new ArrayList<CmsAttributeReference>();
        for (int i = 0; i < systemLangs.size(); i++) {
            Lang lang = systemLangs.get(i);
            String text = this.getTextMap().get(lang.getCode());
            List<SymbolicLink> links = HypertextAttributeUtil.getSymbolicLinksOnText(text);
            if (null != links && !links.isEmpty()) {
                for (int j = 0; j < links.size(); j++) {
                    SymbolicLink symbLink = links.get(j);
                    if (symbLink.getDestType() != SymbolicLink.URL_TYPE) {
                        CmsAttributeReference ref = new CmsAttributeReference(symbLink.getPageDest(),
                                symbLink.getContentDest(), null);
                        refs.add(ref);
                    }
                }
            }
        }
        return refs;
    }
    
    public List<AttributeFieldError> validate(AttributeTracer tracer) {
        List<AttributeFieldError> errors = super.validate(tracer);
        try {
            ILangManager langManager = (ILangManager) this.getBeanFactory().getBean(SystemConstants.LANGUAGE_MANAGER, ILangManager.class);
            List<Lang> langs = langManager.getLangs();
            for (int i = 0; i < langs.size(); i++) {
                Lang lang = langs.get(i);
                AttributeTracer textTracer = (AttributeTracer) tracer.clone();
                textTracer.setLang(lang);
                String text = this.getTextMap().get(lang.getCode());
                if (null == text) continue;
                List<SymbolicLink> links = HypertextAttributeUtil.getSymbolicLinksOnText(text);
                if (null != links && !links.isEmpty()) {
                    IContentManager contentManager = (IContentManager) this.getBeanFactory().getBean(JacmsSystemConstants.CONTENT_MANAGER, IContentManager.class);
                    IPageManager pageManager = (IPageManager) this.getBeanFactory().getBean(SystemConstants.PAGE_MANAGER, IPageManager.class);
                    SymbolicLinkValidator sler = new SymbolicLinkValidator(contentManager, pageManager);
                    for (int j = 0; j < links.size(); j++) {
                        SymbolicLink symbLink = links.get(j);
                        String linkErrorCode = sler.scan(symbLink, (Content) this.getParentEntity());
                        if (null != linkErrorCode) {
                            AttributeFieldError error = new AttributeFieldError(this, linkErrorCode, textTracer);
                            error.setMessage("Invalid link - page " + symbLink.getPageDest() 
                                + " - content " + symbLink.getContentDest() + " - Error code " + linkErrorCode);
                            errors.add(error);
                        }
                    }
                }
            }
        } catch (Throwable t) {
        	_logger.error("Error validating Attribute '{}'", this.getName(), t);
            //ApsSystemUtils.logThrowable(t, this, "validate", "Error validating Attribute '" + this.getName() + "'");
            throw new RuntimeException("Error validating Attribute '" + this.getName() + "'", t);
        }
        return errors;
    }
    
}
