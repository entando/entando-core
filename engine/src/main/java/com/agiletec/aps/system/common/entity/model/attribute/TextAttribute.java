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
package com.agiletec.aps.system.common.entity.model.attribute;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.jdom.Element;

import com.agiletec.aps.system.common.entity.model.AttributeSearchInfo;
import com.agiletec.aps.system.services.lang.Lang;

/**
 * This class implements the Text Attribute. It can support multiple languages.
 * @author M.Diana
 */
public class TextAttribute extends AbstractTextAttribute {
    
    /**
     * Initialize the map of the texts.
     */
    public TextAttribute() {
        this._textMap = new HashMap<String, String>();
    }
    
    @Override
    public boolean isMultilingual() {
        return true;
    }

    /**
     * Return the text field to index.
     * @return The text field to index.
     */
    @Override
    public String getIndexeableFieldValue() {
        return this.getText();
    }

    /**
     * Return the associated text in the current language (set using the 'setLangCode' method)
     * or the default language if the former is not available.
     * @return The requested text.
     */
    @Override
    public String getText() {
        String text = (String) this.getTextMap().get(this.getRenderingLang());
        if (text == null) {
            text = (String) this.getTextMap().get(this.getDefaultLangCode());
            if (text == null) {
                text = "";
            }
        }
        return text;
    }

    @Override
    public String getTextForLang(String langCode) {
        return (String) this.getTextMap().get(langCode);
    }

    /**
     * Set up the associated text in the given language.
     * @param text The text string to set up.
     * @param langCode The code of the native language of the given text. 
     */
    @Override
    public void setText(String text, String langCode) {
        this.getTextMap().put(langCode, text);
    }

    @Override
    public boolean needToConvertSpecialCharacter() {
        return true;
    }

    /**
     * Return the Map containing all the versions available of the associated text, one per
     * language.
     * @return A map indexed by the language code.
     */
    public Map<String, String> getTextMap() {
        return _textMap;
    }

    /**
     * Set up a map containing all the versions available of the associated text, one per language.
     * This will overwrite, and possibly delete, all the previous values of the attribute.
     * @param textMap A map indexed by the language code.
     */
    public void setTextMap(Map<String, String> textMap) {
        this._textMap = textMap;
    }

    @Override
    public boolean isSearchableOptionSupported() {
        return true;
    }

    @Override
    public List<AttributeSearchInfo> getSearchInfos(List<Lang> systemLangs) {
        List<AttributeSearchInfo> infos = null;
        if (null != this.getTextMap() && this.getTextMap().size() > 0) {
            infos = new ArrayList<AttributeSearchInfo>();
            for (int i = 0; i < systemLangs.size(); i++) {
                Lang lang = systemLangs.get(i);
                String text = this.getTextMap().get(lang.getCode());
                if (null == text) {
                    text = this.getTextMap().get(this.getDefaultLangCode());
                }
                if (null != text) {
                    AttributeSearchInfo info = null;
                    if (text.length() >= 255) {
                        info = new AttributeSearchInfo(text.substring(0, 254), null, null, lang.getCode());
                    } else {
                        info = new AttributeSearchInfo(text, null, null, lang.getCode());
                    }
                    infos.add(info);
                }
            }
        }
        return infos;
    }
	
    @Override
    public Element getJDOMElement() {
		Element attributeElement = this.createRootElement("attribute");
        this.addTextElements(attributeElement);
        return attributeElement;
    }
    
    /**
     * Add the elements, related to the texts inserted in the attribute, needed in order
     * to prepare the element to finally insert in the XML of the entity.
     * @param attributeElement The element to complete.
     */
    protected void addTextElements(Element attributeElement) {
        if (null == this.getTextMap()) {
            return;
        }
        Iterator<String> langIter = this.getTextMap().keySet().iterator();
        while (langIter.hasNext()) {
            String currentLangCode = langIter.next();
            String text = this.getTextMap().get(currentLangCode);
            if (null != text && text.trim().length() > 0) {
                Element textElement = new Element("text");
                textElement.setAttribute("lang", currentLangCode);
                textElement.setText(text.trim());
                attributeElement.addContent(textElement);
            }
        }
    }
    
    public Object getValue() {
        return this.getTextMap();
    }
    
    protected Object getJAXBValue(String langCode) {
        if (null == langCode) {
            langCode = this.getDefaultLangCode();
        }
        String text = this.getTextForLang(langCode);
        if (null == text || text.trim().length() == 0) {
            text = this.getTextForLang(this.getDefaultLangCode());
        }
        return text;
    }
    
    public void valueFrom(DefaultJAXBAttribute jaxbAttribute) {
        super.valueFrom(jaxbAttribute);
        Object value = jaxbAttribute.getValue();
        if (null == value) return;
        this.getTextMap().put(this.getDefaultLangCode(), value.toString());
    }
    
    public Status getStatus() {
        String text = this.getTextMap().get(this.getDefaultLangCode());
        if (null != text && text.trim().length() > 0) {
            return Status.VALUED;
        }
        return Status.EMPTY;
    }
    
    private Map<String, String> _textMap;
    
}