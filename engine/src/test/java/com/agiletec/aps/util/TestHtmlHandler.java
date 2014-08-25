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

