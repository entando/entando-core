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

import com.agiletec.aps.BaseTestCase;

/**
 * @version 1.0
 * @author W.Ambu
 */
public class TestHtmlHandler extends BaseTestCase {
	
    public void testGetParsedText() {
        String textToParse = "<title> This is the<b>first</b></title><body><b>this is</b>the next</body>";
        HtmlHandler htmlHandler = new HtmlHandler();
        String resultText = htmlHandler.getParsedText(textToParse);
        assertEquals("  This is the first    this is the next ", resultText);
    }
    
}

