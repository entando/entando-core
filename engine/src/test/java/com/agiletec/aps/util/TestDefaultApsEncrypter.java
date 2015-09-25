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
