/*
 * Copyright 2013-Present Entando Inc. (http://www.entando.com) All rights reserved.
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
package org.entando.entando.aps.system.services.controller.executor;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;

/**
 * @author E.Santoboni
 */
public class BufferedHttpResponseWrapper extends HttpServletResponseWrapper {
	
	public BufferedHttpResponseWrapper(HttpServletResponse response) {
		super(response);
		this._baos = new ByteArrayOutputStream();
		this._writer = new PrintWriter(this._baos);
	}
	
	@Override
	public PrintWriter getWriter() throws IOException {
		return this._writer;
	}

	public String getOutput() {
		this._writer.flush();
		this._writer.close();
		return this._baos.toString();
	}
	
	PrintWriter _writer = null;
	ByteArrayOutputStream _baos = null;
	
}
