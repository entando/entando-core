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
package com.agiletec.aps.system.common.entity.parse;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;

import com.agiletec.aps.system.SystemConstants;
import com.agiletec.aps.system.common.entity.ApsEntityManager;
import com.agiletec.aps.system.common.entity.loader.ExtraAttributeLoader;
import com.agiletec.aps.system.common.entity.model.IApsEntity;
import com.agiletec.aps.system.common.entity.model.SmallEntityType;
import com.agiletec.aps.system.common.entity.model.attribute.AbstractComplexAttribute;
import com.agiletec.aps.system.common.entity.model.attribute.AttributeInterface;
import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.aps.system.services.lang.ILangManager;
import java.io.IOException;
import org.apache.commons.lang3.StringUtils;
import org.jdom.JDOMException;

/**
 * This class parses the XML that defines the Entity Types as obtained from the
 * configuration file. This class serves the managers that handle the
 * ApsEntities. This class is the default used in the definition of the Spring
 * bean that defines the base manager {@link ApsEntityManager} of the entities.
 * This declaration of this class must be substituted in the definition of all
 * the services which extend the ApsEntityManager, if a customized DOM class is
 * going to interpret a customized entity. Such entity must have its own
 * implementation in a class that extends ApsEntity. A note about the source
 * code: the naming conflict between the content "Attribute" and the "Attribute"
 * found in the HXML tags makes the distinction between the two difficult.
 * Please pay attention to the correct interpretation of the name of variables
 * and the private methods.
 *
 * @author M.Diana - E.Santoboni
 */
public class EntityTypeDOM implements IEntityTypeDOM, BeanFactoryAware {

	private static final Logger _logger = LoggerFactory.getLogger(EntityTypeDOM.class);

	private Map<String, AttributeInterface> _attributeTypes;

	private String _entityManagerName;
	private BeanFactory _beanFactory;

	@Override
	public List<SmallEntityType> extractSmallEntityTypes(String xml) throws ApsSystemException {
		List<SmallEntityType> list = new ArrayList<>();
		Document document = this.decodeDOM(xml);
		List<Element> entityElements = document.getRootElement().getChildren();
		for (int i = 0; i < entityElements.size(); i++) {
			Element entityElem = entityElements.get(i);
			String typeCode = this.extractXmlAttribute(entityElem, "typecode", true);
			String typeDescr = this.extractXmlAttribute(entityElem, "typedescr", true);
			list.add(new SmallEntityType(typeCode, typeDescr));
		}
		return list;
	}

	@Override
	public Map<String, IApsEntity> extractEntityTypes(String xml, Class entityClass,
			IApsEntityDOM entityDom, String entityManagerName) throws ApsSystemException {
		Document document = this.initDom(xml, entityManagerName);
		return this.doParsing(document, entityClass, entityDom);
	}

	@Override
	public IApsEntity extractEntityType(String typeCode, String xml, Class entityClass,
			IApsEntityDOM entityDom, String entityManagerName) throws ApsSystemException {
		Document document = this.initDom(xml, entityManagerName);
		return this.doParsing(typeCode, document, entityClass, entityDom);
	}

	protected Document initDom(String xml, String entityManagerName) throws ApsSystemException {
		if (null != entityManagerName) {
			ExtraAttributeLoader loader = new ExtraAttributeLoader();
			Map<String, AttributeInterface> extraAttributes = loader.extractAttributes(this.getBeanFactory(), entityManagerName);
			if (null != extraAttributes) {
				this.getAttributeTypes().putAll(extraAttributes);
			}
		}
		this.setEntityManagerName(entityManagerName);
		return this.decodeDOM(xml);
	}

	@Override
	public String getXml(Map<String, IApsEntity> entityTypes) throws ApsSystemException {
		XMLOutputter out = new XMLOutputter();
		Document document = new Document();
		try {
			Element rootElement = new Element(this.getEntityTypesRootElementName());
			document.setRootElement(rootElement);
			List<String> entityTypeCodes = new ArrayList<>(entityTypes.keySet());
			Collections.sort(entityTypeCodes);
			for (String entityTypeCode : entityTypeCodes) {
				IApsEntity currentEntityType = entityTypes.get(entityTypeCode);
				Element entityTypeElement = this.createTypeElement(currentEntityType);
				rootElement.addContent(entityTypeElement);
			}
			Format format = Format.getPrettyFormat();
			format.setIndent("\t");
			out.setFormat(format);
		} catch (Throwable t) {
			_logger.error("Error building xml", t);
			throw new ApsSystemException("Error building xml", t);
		}
		return out.outputString(document);
	}

	@Override
	public String getXml(IApsEntity entityType) throws ApsSystemException {
		XMLOutputter out = new XMLOutputter();
		Document document = new Document();
		try {
			Element entityTypeElement = this.createTypeElement(entityType);
			document.setRootElement(entityTypeElement);
			Format format = Format.getPrettyFormat();
			format.setIndent("\t");
			out.setFormat(format);
		} catch (Throwable t) {
			_logger.error("Error building xml", t);
			throw new ApsSystemException("Error building xml", t);
		}
		return out.outputString(document);
	}

	@Override
	public IApsEntity extractEntityType(String entityTypeXml, Class entityClass,
			IApsEntityDOM entityDom, String entityManagerName) throws ApsSystemException {
		try {
			Document document = this.decodeDOM(entityTypeXml);
			return this.doParsing(document.getRootElement(), entityClass, entityDom);
		} catch (Throwable t) {
			_logger.error("Error extracting single entity type from xml {}", entityTypeXml, t);
			throw new ApsSystemException("Error extracting single entity type", t);
		}
	}

	protected Element createTypeElement(IApsEntity currentEntityType) {
		Element typeElement = this.createRootTypeElement(currentEntityType);
		Element attributesElement = new Element("attributes");
		typeElement.addContent(attributesElement);
		List<AttributeInterface> attributes = currentEntityType.getAttributeList();
		for (AttributeInterface attribute : attributes) {
			Element configElement = attribute.getJDOMConfigElement();
			attributesElement.addContent(configElement);
		}
		return typeElement;
	}

	protected Element createRootTypeElement(IApsEntity currentEntityType) {
		Element typeElement = new Element(this.getEntityTypeRootElementName());
		typeElement.setAttribute("typecode", currentEntityType.getTypeCode());
		typeElement.setAttribute("typedescr", currentEntityType.getTypeDescription());
		return typeElement;
	}

	private Document decodeDOM(String xmlText) throws ApsSystemException {
		Document doc = null;
		SAXBuilder builder = new SAXBuilder();
		builder.setValidation(false);
		StringReader reader = new StringReader(xmlText);
		try {
			doc = builder.build(reader);
		} catch (JDOMException | IOException ex) {
			throw new ApsSystemException("Error while parsing: " + ex.getMessage(), ex);
		}
		return doc;
	}

	/**
	 * Parse the XML element and return the map of Entity Types . This method
	 * must be extended to implement particular operations that apply to the
	 * specific structure of (an eventually customized) entity class that must
	 * be handled by the native Entity manager. That class must implement the
	 * IApsEntity interface.
	 *
	 * @param document The DOM document.
	 * @param entityClass The class that maps the Entity Type.
	 * @param entityDom L'elemento xml della definizione del singolo tipo di
	 * entità.
	 * @return The map of the Entity Types
	 * @throws ApsSystemException In case of error
	 */
	protected Map<String, IApsEntity> doParsing(Document document, Class entityClass, IApsEntityDOM entityDom) throws ApsSystemException {
		Map<String, IApsEntity> entityTypes = new HashMap<>();
		List<Element> contentElements = document.getRootElement().getChildren();
		for (int i = 0; i < contentElements.size(); i++) {
			Element currentContentElem = contentElements.get(i);
			IApsEntity entity = this.doParsing(currentContentElem, entityClass, entityDom);
			entityTypes.put(entity.getTypeCode(), entity);
		}
		return entityTypes;
	}

	protected IApsEntity doParsing(String typeCode, Document document, Class entityClass, IApsEntityDOM entityDom) throws ApsSystemException {
		if (StringUtils.isEmpty(typeCode)) {
			return null;
		}
		List<Element> entityElements = document.getRootElement().getChildren();
		for (Element currentEntityElem : entityElements) {
			String extractedTypeCode = this.extractXmlAttribute(currentEntityElem, "typecode", true);
			if (!typeCode.equals(extractedTypeCode)) {
				continue;
			}
			return this.doParsing(currentEntityElem, entityClass, entityDom);
		}
		return null;
	}

	protected IApsEntity doParsing(Element element, Class entityClass, IApsEntityDOM entityDom) throws ApsSystemException {
		IApsEntity entity = null;
		try {
			entity = this.createEntityType(element, entityClass);
			entity.setEntityDOM(entityDom);
			this.fillEntityType(entity, element);
			entity.setDefaultLang(this.getLangManager().getDefaultLang().getCode());
			_logger.debug("Entity Type '{}' defined", entity.getTypeCode());
		} catch (Throwable t) {
			_logger.error("Error extracting entity type", t);
			throw new ApsSystemException("Configuration error of the Entity Type detected", t);
		}
		return entity;
	}

	/**
	 * Map the structure of an Entity Type with the attributes defined in its
	 * XML configuration item. This method must be extended to implement
	 * particular operations that apply to the specific structure of (an
	 * eventually customized) entity class that must be handled by the native
	 * Entity manager. That class must implement the IApsEntity interface.
	 *
	 * @param entityType The entity type to map.
	 * @param currentContentElem The XML that configures the Entity Type.
	 * @throws ApsSystemException If errors are detected during the parsing
	 * process.
	 */
	protected void fillEntityType(IApsEntity entityType, Element currentContentElem) throws ApsSystemException {
		try {
			if (null == currentContentElem.getChild("attributes")) {
				return;
			}
			List<Element> attributeElements = currentContentElem.getChild("attributes").getChildren();
			for (Element currentAttrElem : attributeElements) {
				AttributeInterface attribute = this.createAttribute(currentAttrElem);
				attribute.setParentEntity(entityType);
				entityType.addAttribute(attribute);
				_logger.debug("The Attribute {} of type {} was successfully inserted in the Entity Type {}", attribute.getName(), attribute.getType(), entityType.getTypeCode());
			}
		} catch (Throwable t) {
			throw new ApsSystemException("Configuration error of the Entity Type " + entityType.getTypeCode() + " detected", t);
		}
	}

	/**
	 * Instantiate and initialize an Entity Type starting from the raw
	 * configuration data.
	 *
	 * @param entityElem The element of the Entity Type to initialize.
	 * @param entityClass The class of the Entity Type.
	 * @return The initialized Entity Type.
	 * @throws ApsSystemException If parsing errors are detected.
	 */
	protected IApsEntity createEntityType(Element entityElem, Class entityClass) throws ApsSystemException {
		try {
			IApsEntity entity = (IApsEntity) entityClass.newInstance();
			entity.setId(null);
			String typeCode = this.extractXmlAttribute(entityElem, "typecode", true);
			entity.setTypeCode(typeCode);
			String typeDescr = this.extractXmlAttribute(entityElem, "typedescr", true);
			entity.setTypeDescription(typeDescr);
			return entity;
		} catch (InstantiationException | IllegalAccessException | ApsSystemException t) {
			throw new ApsSystemException("Error detected while creating a new entity", t);
		}
	}

	/**
	 * Generate an attribute to insert in an Entity Type. The attribute is
	 * obtained cloning one of the previously defined elements.
	 *
	 * @param attributeElem The element of the Attribute Type to generate.
	 * @return The built attribute.
	 * @throws ApsSystemException If parsing errors are detected.
	 */
	private AttributeInterface createAttribute(Element attributeElem) throws ApsSystemException {
		String typeCode = this.extractXmlAttribute(attributeElem, "attributetype", true);
		AttributeInterface attr = (AttributeInterface) getAttributeTypes().get(typeCode);
		if (null == attr) {
			throw new ApsSystemException("Wrong Attribute Type: " + typeCode + ", "
					+ "found in the tag <" + attributeElem.getName() + ">");
		}
		attr = (AttributeInterface) attr.getAttributePrototype();
		attr.setAttributeConfig(attributeElem);
		if (!attr.isSimple()) {
			((AbstractComplexAttribute) attr).setComplexAttributeConfig(attributeElem, this.getAttributeTypes());
		}
		return attr;
	}

	/**
	 * Extract from a XML tag the attribute matching the given criteria.
	 *
	 * @param currElement The element where to extract the value of the
	 * attribute from.
	 * @param attributeName The name of the requested attribute.
	 * @param required Distinguish between mandatory and optional attributes.
	 * @return The value of the requested attribute.
	 * @throws ApsSystemException When a mandatory attribute is not found.
	 */
	protected String extractXmlAttribute(Element currElement, String attributeName,
			boolean required) throws ApsSystemException {
		String value = currElement.getAttributeValue(attributeName);
		if (required && value == null) {
			throw new ApsSystemException("Attribute '" + attributeName + "' not found in the tag <" + currElement.getName() + ">");
		}
		return value;
	}

	/**
	 * Prepare the map with the Attribute Types. The map is indexed by the code
	 * of the Attribute Type. The Attributes are utilized (as elementary
	 * "bricks") to build the structure of the Entity Types.
	 *
	 * @param attributeTypes The map containing the Attribute Types indexed by
	 * the type code.
	 */
	@Override
	public void setAttributeTypes(Map<String, AttributeInterface> attributeTypes) {
		this._attributeTypes = attributeTypes;
	}

	@Override
	public Map<String, AttributeInterface> getAttributeTypes() {
		return this._attributeTypes;
	}

	protected ILangManager getLangManager() {
		return (ILangManager) this.getBeanFactory().getBean(SystemConstants.LANGUAGE_MANAGER, ILangManager.class);
	}

	protected String getEntityManagerName() {
		return _entityManagerName;
	}

	protected void setEntityManagerName(String entityManagerName) {
		this._entityManagerName = entityManagerName;
	}

	protected BeanFactory getBeanFactory() {
		return this._beanFactory;
	}

	@Override
	public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
		this._beanFactory = beanFactory;
	}

	protected String getEntityTypesRootElementName() {
		return "entitytypes";
	}

	protected String getEntityTypeRootElementName() {
		return "entitytype";
	}

}
