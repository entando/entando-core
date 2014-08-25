/*
*
* Copyright 2014 Entando S.r.l. (http://www.entando.com) All rights reserved.
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
* Copyright 2014 Entando S.r.l. (http://www.entando.com) All rights reserved.
*
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
