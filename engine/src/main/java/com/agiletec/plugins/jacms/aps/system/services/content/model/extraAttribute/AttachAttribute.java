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
package com.agiletec.plugins.jacms.aps.system.services.content.model.extraAttribute;

import com.agiletec.plugins.jacms.aps.system.services.resource.model.AttachResource;
import com.agiletec.plugins.jacms.aps.system.services.resource.model.ResourceInterface;
import java.io.IOException;
import java.io.InputStream;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.AutoDetectParser;
import org.apache.tika.sax.BodyContentHandler;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;

/**
 * Rappresenta un attributo di entità di tipo attachment. L'attachment e il
 * testo associato possono essere diversi per ciascun lingua.
 *
 * @author M.Diana - S.Didaci - E.Santoboni
 */
public class AttachAttribute extends AbstractResourceAttribute {

	private static final Logger _logger = LoggerFactory.getLogger(AttachAttribute.class);

	/**
	 * Restituisce il path URL dell'attachment.
	 *
	 * @return Il path dell'attachment.
	 */
	public String getAttachPath() {
		String attachPath = "";
		ResourceInterface res = this.getResource();
		if (null != res) {
			attachPath = ((AttachResource) res).getAttachPath();
			attachPath = this.appendContentReference(attachPath);
		}
		return attachPath;
	}

	@Override
	protected String getDefaultPath() {
		return this.getAttachPath();
	}

	@Override
	public String getIndexeableFieldValue() {
		StringBuilder buffer = new StringBuilder();
		if (null != super.getIndexeableFieldValue()) {
			buffer.append(super.getIndexeableFieldValue());
		}
		String extraValue = null;
		ResourceInterface resource = this.getResource();
		if (resource != null) {
			InputStream is = ((AttachResource) resource).getResourceStream();
			if (null != is) {
				AutoDetectParser parser = new AutoDetectParser();
				BodyContentHandler handler = new BodyContentHandler(-1);
				Metadata metadata = new Metadata();
				try {
					parser.parse(is, handler, metadata);
					extraValue = handler.toString();
				} catch (Throwable t) {
					_logger.error("Error while processing the parsing", t);
				} finally {
					try {
						is.close();
					} catch (IOException ex) {
						_logger.error("Error closing stream", ex);
					}
				}
			}
		}
		if (null != extraValue) {
			buffer.append(" ").append(extraValue);
		}
		return buffer.toString();
	}

}
