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
package com.agiletec.aps.system.common.entity.parse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import com.agiletec.aps.system.common.entity.ApsEntityManager;
import com.agiletec.aps.system.common.entity.model.IApsEntity;
import com.agiletec.aps.system.common.entity.model.attribute.AttributeInterface;
import com.agiletec.aps.system.common.entity.parse.attribute.AttributeHandlerInterface;
import com.agiletec.aps.system.services.category.Category;
import com.agiletec.aps.system.services.category.ICategoryManager;

/**
 * This class supports the parsing of the XML string that represents an Entity.
 * This "handler" class is used by the managers which manage the ApsEntities.
 * This class is utilized by default in the definition of the Spring abstract service {@link ApsEntityManager}.
 * This definition must be substituted in the declaration of those services which make use, extending it,
 * of the ApsEntityManager to support a customized entity. This entity must implement the IApsEntity
 * interface. 
 * @author M.Diana - E.Santoboni
 */
public class EntityHandler extends DefaultHandler {

	private static final Logger _logger = LoggerFactory.getLogger(EntityHandler.class);
	
    /**
     * Handler initialization.
     * @param entity The Entity Prototype to fill with data. 
     * @param xmlAttributeRootElementName The name of the root XML attribute.
     * @param categoryManager The category manager suitable for the Entity Type. 
     */
    public void initHandler(IApsEntity entity, String xmlAttributeRootElementName,
            ICategoryManager categoryManager) {
        this.reset();
        this._currentEntity = entity;
        this._xmlAttributeRootElementName = xmlAttributeRootElementName;
        this._categoryManager = categoryManager;
    }
    
    public EntityHandler getHandlerPrototype() {
        EntityHandler handler = null;
        try {
            Class handlerClass = Class.forName(this.getClass().getName());
            handler = (EntityHandler) handlerClass.newInstance();
        } catch (Exception e) {
            throw new RuntimeException("Error while cloning the entity handler: class " + this.getClass().getName(), e);
        }
        return handler;
    }
    
	@Override
    public void characters(char[] buf, int offset, int length) throws SAXException {
        String s = new String(buf, offset, length);
        if (_textBuffer == null) {
            _textBuffer = new StringBuffer(s);
        } else {
            _textBuffer.append(s);
        }
    }
    
	@Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        this._textBuffer = null;
        try {
            if (this.isIntoAttributes()) {
                this.startAttribute(attributes, qName);
            } else {
                if (qName.equals(this._xmlAttributeRootElementName)) {
                    this.startEntity(attributes, qName);
                } else if (qName.equals("descr")) {
                    this.startDescr(attributes, qName);
                } else if (qName.equals("groups")) {
                    this.startGroups(attributes, qName);
                } else if (qName.equals("group")) {
                    this.startGroup(attributes, qName);
                } else if (qName.equals("categories")) {
                    this.startCategories(attributes, qName);
                } else if (qName.equals("category")) {
                    this.startCategory(attributes, qName);
                } else if (qName.equals("attributes")) {
                    startAttributes(attributes, qName);
                } else {
                    this.startEntityElement(uri, localName, qName, attributes);
                }
            }
        } catch (Throwable t) {
            _logger.error("error in startElement", t);
            throw new SAXException(t.getMessage(), new Exception(t));
        }
    }

    /**
     * Initialize the entity. This method is triggered by the startElement event
     * on the root element in the XML defining the entity.
     * This method must be extended when defining particular operations to perform
     * on customized entities.
     * @param attributes The Attribute Elements.
     * @param qName The name of the XML attribute.
     * @throws SAXException If errors are detected while parsing the XML string.
     */
    protected void startEntity(Attributes attributes, String qName) throws SAXException {
        String id = this.extractXmlAttribute(attributes, "id", qName, true);
        this.getCurrentEntity().setId(id);
        String typecode = this.extractXmlAttribute(attributes, "typecode", qName, true);
        this.getCurrentEntity().setTypeCode(typecode);
    }

    /**
     * This method performs the operations not handled in the base handler.
     * This method is triggered by the startElement event on those elements
     * not recognized in the base handler.
     * By default this method does not perform any action.
     * This method must be extended to perform specific operations that depend
     * on the structure of the Entity Type.
     * @param uri The URI.
     * @param localName The localName.
     * @param qName The name of the XML attribute.
     * @param attributes The attributes of the element.
     * @throws SAXException If errors are detected during the parsing process.
     */
    protected void startEntityElement(String uri, String localName,
            String qName, Attributes attributes) throws SAXException {
        //default: do nothing
    }
    
	@Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        try {
            if (this.isIntoAttributes()) {
                if (qName.equals("attributes")) {
                    this.endAttributes();
                } else {
                    this.endAttribute(qName);
                }
            } else {
                if (qName.equals(this._xmlAttributeRootElementName)) {
                    this.endEntity();
                } else if (qName.equals("descr")) {
                    this.endDescr();
                } else if (qName.equals("groups")) {
                    this.endGroups();
                } else if (qName.equals("group")) {
                    this.endGroup();
                } else if (qName.equals("categories")) {
                    this.endCategories();
                } else if (qName.equals("category")) {
                    this.endCategory();
                } else {
                    this.endEntityElement(uri, localName, qName);
                }
            }
        } catch (Throwable t) {
        	_logger.error("error in endElement", t);
            throw new SAXException(t.getMessage(), new Exception(t));
        }
    }

    /**
     * This is the last step in the interpretation process of the XML entity.
     * This method is triggered by the endElement event on the root element of the XML 
     * string.
     * By default this method does not perform any action.
     * This method must be extended to perform specific operations that depend
     * on the structure of the Entity Type.
     */
    private void endEntity() {
        return; // nothing to do
    }

    /**
     * Perform the operations for those attributes not handled in the base handler.
     * This method is triggered by the endElement event on elements unknown to the
     * base handler.
     * By default this method does not perform any action.
     * This method must be extended to perform specific operations that depend
     * on the structure of the Entity Type.
     * @param uri The URI.
     * @param localName The localName.
     * @param qName The name of the XML attribute.
     * @throws SAXException If errors are detected while parsing the XML code 
     */
    protected void endEntityElement(String uri, String localName, String qName) throws SAXException {
        // default: nothing to do
    }
    
    private void startAttributes(Attributes attributes, String qName) throws SAXException {
        this.setIntoAttributes(true);
    }
    
    private void endAttributes() {
        this.setIntoAttributes(false);
    }
    
    private void startDescr(Attributes attributes, String qName) throws SAXException {
        return; // nothing to do
    }
    
    private void endDescr() {
        if (null != this._textBuffer) {
            this._currentEntity.setDescr(this._textBuffer.toString());
        }
    }
    
    private void startGroups(Attributes attributes, String qName) throws SAXException {
        String mainGroup = extractXmlAttribute(attributes, "mainGroup", qName, false);
        this._currentEntity.setMainGroup(mainGroup);
    }
    
    private void endGroups() {
        return; // nothing to do
    }
    
    private void startGroup(Attributes attributes, String qName) throws SAXException {
        String groupName = extractXmlAttribute(attributes, "name", qName, false);
        this._currentEntity.addGroup(groupName);
    }

    private void endGroup() {
        return; // nothing to do
    }
    
    private void startCategories(Attributes attributes, String qName) throws SAXException {
        return; // nothing to do
    }
    
    private void endCategories() {
        return; // nothing to do
    }
    
    private void startCategory(Attributes attributes, String qName) throws SAXException {
        if (this._categoryManager != null) {
            String categoryCode = extractXmlAttribute(attributes, "id", qName, true);
            Category category = this._categoryManager.getCategory(categoryCode);
            if (null != category) {
                this._currentEntity.addCategory(category);
            }
        }
    }
    
    private void endCategory() {
        return; // nothing to do
    }
    
    private void startAttribute(Attributes attributes, String qName) throws SAXException {
        if (null == this._currentAttr) {
            String attributeName = this.extractXmlAttribute(attributes, "name", qName, false);
            if (null != attributeName) {
				String attributeType = this.extractXmlAttribute(attributes, "attributetype", qName, false);
                this._currentAttr = (AttributeInterface) this._currentEntity.getAttribute(attributeName);
                if (null != this._currentAttr && this._currentAttr.getType().equals(attributeType)) {
                    this._parserModule = this._currentAttr.getHandler();
                    this._parserModule.setCurrentAttr(this._currentAttr);
                }
            }
        }
        if (null != _parserModule) {
            this._parserModule.startAttribute(attributes, qName);
        }
    }

    private void endAttribute(String qName) {
        if (null != this._parserModule) {
            this._parserModule.endAttribute(qName, this._textBuffer);
            if (this._parserModule.isEndAttribute(qName)) {
                this._parserModule = null;
                this._currentAttr = null;
            }
        }
    }

    /**
     * Recupera in modo controllato un attributo di un tag xml dall'insieme
     * degli attributi.
     * @param attrs Attributi del tag xml.
     * @param attributeName Nome dell'attributo richiesto.
     * @param qName Nome del tag xml.
     * @param required Se true, l'attributo è considerato obbligatorio.
     * @return Il valore dell'attributo richiesto.
     * @throws SAXException Nel caso l'attributo sia dichiarato obbligatorio e
     * risulti assente.
     */
    protected String extractXmlAttribute(Attributes attrs, String attributeName,
            String qName, boolean required) throws SAXException {
        int index = attrs.getIndex(attributeName);
        String value = attrs.getValue(index);
        if (required && value == null) {
            throw new SAXException("Xml Attribute '" + attributeName + "' not found in tag <" + qName + ">");
        }
        return value;
    }

    /**
     * Verifica se, nell'istante in cui il metodo è richiamato, ci si trova 
     * all'interno della definizione della lista di attributi componenti l'entità.
     * @return True se ci si trova all'interno della definizione della lista di attributi 
     * componenti l'entità, false in caso contrario.
     */
    protected boolean isIntoAttributes() {
        return _intoAttributes;
    }

    /**
     * Setta se, nell'istante in cui il metodo è richiamato, ci si trova 
     * all'interno della definizione della lista di attributi componenti l'entità.
     * @param intoAttributes True se ci si trova all'interno della definizione della 
     * lista di attributi componenti l'entità, false in caso contrario.
     */
    protected void setIntoAttributes(boolean intoAttributes) {
        this._intoAttributes = intoAttributes;
    }

    /**
     * Resituisce il buffer relativo al testo incluso.
     * @return Il buffer relativo al testo incluso.
     */
    protected StringBuffer getTextBuffer() {
        return this._textBuffer;
    }

    /**
     * Restituisce l'entità gestita dall'handler.
     * @return L'entità gestita dall'handler.
     */
    protected IApsEntity getCurrentEntity() {
        return _currentEntity;
    }

    /**
     * Effettua il reset delle variabili di istanza dell'handler.
     * Il metodo viene richiamato in fase di inizializzazione degli 
     * elementi dell'handler, prima di effettuare la scanzìsione del documento.
     */
    protected void reset() {
        this._categoryManager = null;
        this._currentEntity = null;
        this._intoAttributes = false;
        this._parserModule = null;
        this._textBuffer = null;
        this._xmlAttributeRootElementName = null;
        this._currentAttr = null;
    }
    
    private boolean _intoAttributes;
    private IApsEntity _currentEntity;
    private AttributeHandlerInterface _parserModule;
    private StringBuffer _textBuffer;
    private AttributeInterface _currentAttr;
    private ICategoryManager _categoryManager;
    private String _xmlAttributeRootElementName;
    
}
