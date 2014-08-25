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


public class TestDefaultApsEncrypter extends BaseTestCase {

	/**
	 * We test the decryption method too, even if the implementation is not mandatory
	 * @throws Throwable
	 */
	public void testEncodeDecode() throws Throwable {
		String testString = "PointerOfBaseAction"; 
		String encoded = null;
		try {
			encoded = DefaultApsEncrypter.encryptString(testString);
			assertTrue(!testString.equals(encoded));
			assertEquals(testString, DefaultApsEncrypter.decrypt(encoded));
		} catch (Throwable t) {
			throw t;
		}
	}

}
