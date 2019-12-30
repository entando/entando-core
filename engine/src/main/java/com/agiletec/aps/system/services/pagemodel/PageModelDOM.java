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
package com.agiletec.aps.system.services.pagemodel;

import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.aps.system.services.page.Widget;
import com.agiletec.aps.util.ApsProperties;
import com.agiletec.aps.util.ApsPropertiesDOM;

import java.io.StringReader;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
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
			
			Element sketchElement = this.buildSketchXML(frame);
			if (null != sketchElement) {
				frameElement.addContent(sketchElement);
			}
			
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
	
	private Element buildSketchXML(Frame frame) {
		FrameSketch sketch = frame.getSketch();
		if (null != sketch) {
			Element sketchElement = new Element(TAB_SKETCH);
			sketchElement.setAttribute(ATTRIBUTE_X1, String.valueOf(sketch.getX1()));
			sketchElement.setAttribute(ATTRIBUTE_Y1, String.valueOf(sketch.getY1()));
			sketchElement.setAttribute(ATTRIBUTE_X2, String.valueOf(sketch.getX2()));
			sketchElement.setAttribute(ATTRIBUTE_Y2, String.valueOf(sketch.getY2()));
			return sketchElement;
		}
		return null;
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
				FrameSketch frameSketch = this.buildFrameSketch(frameElement);
				if (null != frameSketch) {
					frame.setSketch(frameSketch);
				} else {
					this.applyDefaultFrameSketch(frame);
				}
				
				this._configuration[pos] = frame;
			}
		} else {
			this._configuration = new Frame[0];
		}
	}
	
	
	/**
	 * the framework behavior when, reading from db, no sketch info is found.
	 * this implementations do nothing
	 * @param frame
	 */
	protected void applyDefaultFrameSketch(Frame frame) {
		// do nothing
	}

	private FrameSketch buildFrameSketch(Element frameElement) {
		Element frameSketchElement = frameElement.getChild(TAB_SKETCH);
		if (null == frameSketchElement) {
			return null;
		}
		
		String x1Val = frameSketchElement.getAttributeValue(ATTRIBUTE_X1);
		String y1Val = frameSketchElement.getAttributeValue(ATTRIBUTE_Y1);
		String x2Val = frameSketchElement.getAttributeValue(ATTRIBUTE_X2);
		String y2Val = frameSketchElement.getAttributeValue(ATTRIBUTE_Y2);
		
		int x1 = StringUtils.isNumeric(x1Val) ? new Integer(x1Val) : 0;
		int y1 = StringUtils.isNumeric(x1Val) ? new Integer(y1Val) : 0;
		int x2 = StringUtils.isNumeric(x1Val) ? new Integer(x2Val) : 0;
		int y2 = StringUtils.isNumeric(x1Val) ? new Integer(y2Val) : 0;
        
		FrameSketch frameSketch = new FrameSketch();
		frameSketch.setCoords(x1, y1, x2, y2);
		return frameSketch;
	}

	private void buildDefaultWidget(Frame frame, Element defaultWidgetElement, int pos, IWidgetTypeManager widgetTypeManager) {
		Widget widget = new Widget();
		String widgetCode = defaultWidgetElement.getAttributeValue(ATTRIBUTE_CODE);
		WidgetType type = widgetTypeManager.getWidgetType(widgetCode);
		if (null == type) {
			_logger.warn("Unknown code of the default widget - '{}'", widgetCode);
			return;
		}
		widget.setType(type);
		Element propertiesElement = defaultWidgetElement.getChild(TAB_PROPERTIES);
		if (null != propertiesElement) {
			ApsProperties prop = this.buildProperties(propertiesElement);
			widget.setConfig(prop);
		}
		frame.setDefaultWidget(widget);
	}
	
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
	
	private final String TAB_SKETCH = "sketch";
	private final String ATTRIBUTE_X1 = "x1";
	private final String ATTRIBUTE_Y1 = "y1";
	private final String ATTRIBUTE_X2 = "x2";
	private final String ATTRIBUTE_Y2 = "y2";
	
	
	private boolean _existMainFrame;
	private int _mainFrame;
	private Frame[] _configuration;
	
}
