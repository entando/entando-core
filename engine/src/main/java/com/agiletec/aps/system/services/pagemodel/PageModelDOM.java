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
package com.agiletec.aps.system.services.pagemodel;

import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.aps.system.services.page.Widget;
import com.agiletec.aps.util.ApsProperties;
import com.agiletec.aps.util.ApsPropertiesDOM;

import java.io.StringReader;
import java.util.Iterator;
import java.util.List;

import org.entando.entando.aps.system.services.widgettype.IWidgetTypeManager;
import org.entando.entando.aps.system.services.widgettype.WidgetType;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Helper class for xml descriptor of page model.
 * @author E.Santoboni
 */
public class PageModelDOM {
	
	private static final Logger _logger = LoggerFactory.getLogger(PageModelDOM.class);
	
	/**
	 * Constructor of the dom. This constructor has to be used 
	 * for create a page model from xml.
	 * @param xmlText The xml tring to parse.
	 * @param widgetTypeManager The manager of widget types.
	 * @throws ApsSystemException in case of errors.
	 */
	public PageModelDOM(String xmlText, IWidgetTypeManager widgetTypeManager) throws ApsSystemException {
		this.decodeDOM(xmlText);
		this.buildFrames(widgetTypeManager);
	}
	
	public PageModelDOM(PageModel pageModel) throws ApsSystemException {
		this._doc = new Document();
		Element root = new Element("frames");
		this._doc.setRootElement(root);
		Frame[] frames = pageModel.getConfiguration();
		for (int i = 0; i < frames.length; i++) {
			Frame frame = frames[i];
			Element frameElement = new Element(TAB_FRAME);
			frameElement.setAttribute(ATTRIBUTE_POS, String.valueOf(i));
			if (frame.isMainFrame()) {
				frameElement.setAttribute(ATTRIBUTE_MAIN, "true");
			}
			Element descrElement = new Element(TAB_DESCR);
			descrElement.setText(frame.getDescription());
			frameElement.addContent(descrElement);
			Widget defaultWidget = frame.getDefaultWidget();
			if (null != defaultWidget) {
				Element defaultWidgetElement = new Element(TAB_DEFAULT_WIDGET);
				defaultWidgetElement.setAttribute(ATTRIBUTE_CODE, defaultWidget.getType().getCode());
				ApsProperties properties = defaultWidget.getConfig();
				if (null != properties && !properties.isEmpty()) {
					ApsPropertiesDOM propertiesDom = new ApsPropertiesDOM();
					propertiesDom.buildJDOM(properties);
					Element widgetConfigElement = propertiesDom.getRootElement();
					defaultWidgetElement.addContent(widgetConfigElement);
				}
				frameElement.addContent(defaultWidgetElement);
			}
			root.addContent(frameElement);
		}
	}
	
	private void decodeDOM(String xmlText) throws ApsSystemException {
		SAXBuilder builder = new SAXBuilder();
		builder.setValidation(false);
		StringReader reader = new StringReader(xmlText);
		try {
			_doc = builder.build(reader);
		} catch (Throwable t) {
			_logger.error("Error parsing the page model XML: {}", xmlText, t);
			throw new ApsSystemException("Error parsing the page model XML", t);
		}
	}
	
	private ApsProperties buildProperties(Element propertiesElement) {
		List<Element> propertyElements = propertiesElement.getChildren(TAB_PROPERTY);
		if (null == propertyElements || propertyElements.isEmpty()) {
			return null;
		}
		ApsProperties prop = new ApsProperties();
		Iterator<Element> propertyElementsIter = propertyElements.iterator();
		while (propertyElementsIter.hasNext()) {
			Element property = (Element) propertyElementsIter.next();
			prop.put(property.getAttributeValue(ATTRIBUTE_KEY), property.getText().trim());
		}
		return prop;
	}
	
	private void buildFrames(IWidgetTypeManager widgetTypeManager) throws ApsSystemException {
		List<Element> frameElements = this._doc.getRootElement().getChildren(TAB_FRAME);
		if (null != frameElements && frameElements.size() > 0) {
			int framesNumber = frameElements.size();
			//_frames = new String[framesNumber];
			//_defaultWidget = new Widget[framesNumber];
			this._configuration = new Frame[framesNumber];
			this._existMainFrame = false;
			for (int i = 0; i < frameElements.size(); i++) {
				Element frameElement = frameElements.get(i);
				int pos = Integer.parseInt(frameElement.getAttributeValue(ATTRIBUTE_POS));
				if (pos >= framesNumber) {
					throw new ApsSystemException("The position '" + pos + "' exceeds the number of frames defined in the page model");
				}
				Frame frame = new Frame();
				frame.setPos(pos);
				String main = frameElement.getAttributeValue(ATTRIBUTE_MAIN);
				if (null != main && main.equals("true")) {
					_existMainFrame = true;
					_mainFrame = pos;
					frame.setMainFrame(true);
				}
				Element frameDescrElement = frameElement.getChild(TAB_DESCR);
				if (null != frameDescrElement) {
					//_frames[pos] = frameDescrElement.getText();
					frame.setDescription(frameDescrElement.getText());
				}
				Element defaultWidgetElement = frameElement.getChild(TAB_DEFAULT_WIDGET);
				//to guaranted compatibility with previsous version of Entamdo 3.3.1 *** Start Block
				if (null == defaultWidgetElement) {
					defaultWidgetElement = frameElement.getChild("defaultShowlet");
				}
				//to guaranted compatibility with previsous version of Entamdo 3.3.1 *** End Block
				if (null != defaultWidgetElement) {
					this.buildDefaultWidget(frame, defaultWidgetElement, pos, widgetTypeManager);
				}
				this._configuration[pos] = frame;
			}
		} else {
			this._configuration = new Frame[0];
		}
	}
	
	private void buildDefaultWidget(Frame frame, Element defaultWidgetElement, int pos, IWidgetTypeManager widgetTypeManager) {
		Widget widget = new Widget();
		String widgetCode = defaultWidgetElement.getAttributeValue(ATTRIBUTE_CODE);
		WidgetType type = widgetTypeManager.getWidgetType(widgetCode);
		if (null == type) {
			_logger.error("Unknown code of the default widget - '{}'", widgetCode);
			return;
		}
		widget.setType(type);
		Element propertiesElement = defaultWidgetElement.getChild(TAB_PROPERTIES);
		if (null != propertiesElement) {
			ApsProperties prop = this.buildProperties(propertiesElement);
			widget.setConfig(prop);
		}// else {
		//	widget.setConfig(new ApsProperties());
		//}
		frame.setDefaultWidget(widget);
	}
	/*
	private void buildDefaultWidget(Element defaultWidgetElement, int pos, IWidgetTypeManager widgetTypeManager) {
		Widget widget = new Widget();
		String widgetCode = defaultWidgetElement.getAttributeValue(ATTRIBUTE_CODE);
		WidgetType type = widgetTypeManager.getWidgetType(widgetCode);
		if (null == type) {
			_logger.error("Unknown code of the default widget - '{}'", widgetCode);
			return;
		}
		widget.setType(type);
		Element propertiesElement = defaultWidgetElement.getChild(TAB_PROPERTIES);
		if (null != propertiesElement) {
			ApsProperties prop = this.buildProperties(propertiesElement);
			widget.setConfig(prop);
		}// else {
		//	widget.setConfig(new ApsProperties());
		//}
		_defaultWidget[pos] = widget;
	}
	*/
	/*
	 * Restituisce l'insieme ordinato delle descrizioni dei "frames"
	 * del modello.  
	 * @return L'insieme delle descrizioni dei "frames"
	 */
	//public String[] getFrames() {
	//	return this._frames;
	//}
	
	/**
	 * La posizione del frame principale, se esiste;
	 * vale -1 se non esiste;
	 * @return La posizione del frame principale.
	 */
	public int getMainFrame() {
		if (_existMainFrame) {
			return this._mainFrame;
		} else {
			return -1;
		}
	}
	
	public String getXMLDocument(){
		XMLOutputter out = new XMLOutputter();
		Format format = Format.getPrettyFormat();
		format.setIndent("\t");
		out.setFormat(format);
		return out.outputString(this._doc);
	}
	
	/*
	 * @deprecated Use {@link #getDefaultWidget()} instead
	 */
	//public Widget[] getDefaultShowlet() {
	//	return getDefaultWidget();
	//}
	/*
	 * Restituisce la configurazione dei widget di default.
	 * @return Il widget di default.
	 */
	//public Widget[] getDefaultWidget() {
	//	return this._defaultWidget;
	//}
	public Frame[] getConfiguration() {
		return _configuration;
	}
	
	private Document _doc;
	private final String TAB_FRAME = "frame";
	private final String ATTRIBUTE_POS = "pos";
	private final String ATTRIBUTE_MAIN = "main";
	private final String TAB_DESCR = "descr";
	private final String TAB_DEFAULT_WIDGET = "defaultWidget";
	private final String ATTRIBUTE_CODE = "code";
	private final String TAB_PROPERTIES = "properties";
	private final String TAB_PROPERTY = "property";
	private final String ATTRIBUTE_KEY = "key";
	private boolean _existMainFrame;
	private int _mainFrame;
	//private String[] _frames;
	//private Widget[] _defaultWidget;
	private Frame[] _configuration;
	
}
