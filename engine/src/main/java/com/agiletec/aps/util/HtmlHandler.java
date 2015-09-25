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
package com.agiletec.aps.util;

/**
 * 
 * @author W.Ambu
 */
public class HtmlHandler {

	/**
	 * Extracts text without html tags
	 * @param textToParse
	 * @return the text value of the node
	 */
	public String getParsedText(String textToParse) {
		String parsedText;
		parsedText = textToParse.replaceAll("<[^<>]+>", " ");
		return parsedText;
	}

}

