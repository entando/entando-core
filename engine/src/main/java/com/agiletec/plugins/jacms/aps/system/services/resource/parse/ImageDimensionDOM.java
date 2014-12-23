/*
 * Copyright 2013-Present Entando Corporation (http://www.entando.com) All rights reserved.
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
package com.agiletec.plugins.jacms.aps.system.services.resource.parse;

import java.io.StringReader;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agiletec.aps.system.ApsSystemUtils;
import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.plugins.jacms.aps.system.services.resource.model.ImageResourceDimension;
import com.agiletec.plugins.jacms.aps.system.services.resource.model.imageresizer.PNGImageResizer;

/**
 * Questa classe opera per caricare le diverse dimensioni di resize
 * delle immagini che compongono le risorse immagini.
 * Il risultato è una mappa delle previste dimensioni di resize.
 * @author E.Santoboni
 */
public class ImageDimensionDOM {

	private static final Logger _logger = LoggerFactory.getLogger(ImageDimensionDOM.class);
	
	/**
	 * Costruttore della classe.
	 * @param xmlText La stringa xml da interpretare.
	 * @throws ApsSystemException In caso di errore
	 * nell'interpretazione dell'xml di configurazione.
	 */
	public ImageDimensionDOM(String xmlText) throws ApsSystemException {
		this.decodeDOM(xmlText);
	}

	/**
	 * Restitusce la mappa delle dimensioni di resize previste.
	 * @return La mappa delle dimensioni di resize previste.
	 */
	public Map<Integer, ImageResourceDimension> getDimensions() {
		Map<Integer, ImageResourceDimension> dimensions = new HashMap<Integer, ImageResourceDimension>();
		List<Element> dimensionElements = _doc.getRootElement().getChildren(TAB_DIMENSION);
		if (null != dimensionElements && dimensionElements.size() > 0) {
			Iterator<Element> dimensionElementsIter = dimensionElements.iterator();
			while (dimensionElementsIter.hasNext()) {
				Element currentElement = (Element) dimensionElementsIter.next();
				ImageResourceDimension dimension = new ImageResourceDimension();
				Element idElement = currentElement.getChild(TAB_ID);
				if (null != idElement) {
					String id = idElement.getText();
					dimension.setIdDim(Integer.parseInt(id));
				}
				Element dimxElement = currentElement.getChild(TAB_DIMX);
				if (null != dimxElement) {
					String dimx = dimxElement.getText();
					dimension.setDimx(Integer.parseInt(dimx));
				}
				Element dimyElement = currentElement.getChild(TAB_DIMY);
				if (null != dimyElement) {
					String dimy = dimyElement.getText();
					dimension.setDimy(Integer.parseInt(dimy));
				}
				dimensions.put(new Integer(dimension.getIdDim()), dimension);
				_logger.debug("Definita dimensione di resize: {}", dimension.getIdDim());
			}
		}
		return dimensions;
	}

	private void decodeDOM(String xmlText) throws ApsSystemException {
		SAXBuilder builder = new SAXBuilder();
		builder.setValidation(false);
		StringReader reader = new StringReader(xmlText);
		try {
			_doc = builder.build(reader);
		} catch (Throwable t) {
			_logger.error("Error parsing xml. {}", xmlText, t);
			throw new ApsSystemException("Errore nel parsing della configurazione Dimensioni di resize", t);
		}
	}

	private Document _doc;
	private final String TAB_DIMENSION = "Dimension";
	private final String TAB_ID = "id";
	private final String TAB_DIMX = "dimx";
	private final String TAB_DIMY = "dimy";

}
