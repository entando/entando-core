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
package org.entando.entando.aps.system.services.api.model;

import java.io.IOException;
import java.io.Writer;

import com.sun.xml.bind.marshaller.CharacterEscapeHandler;

/**
 * @author Frédéric Barmes - E.Santoboni
 */
public class CDataCharacterEscapeHandler implements CharacterEscapeHandler {
	
	/**
	 * @param ch The array of characters.
	 * @param start The starting position.
	 * @param length The number of characters to use.
	 * @param isAttVal true if this is an attribute value literal.
	 */
	@Override
	public void escape(char[] ch, int start, int length, boolean isAttVal, Writer writer) throws IOException {
		if (CDataAdapter.isCdata(new String(ch))) {
			writer.write( ch, start, length );
		} else {
			this.useStandardEscape(ch, start, length, isAttVal, writer);
		}
	}
	
	private void useStandardEscape(char[] ch, int start, int length, boolean isAttVal, Writer writer) throws IOException {
		CharacterEscapeHandler escapeHandler = StandardEscapeHandler.getInstance();
		escapeHandler.escape(ch, start, length, isAttVal, writer);
	}
	
	/**
	 * A standard XML character escape handler
	 * @author fbarmes
	 */
	private static final class StandardEscapeHandler implements CharacterEscapeHandler {
		
		public static final StandardEscapeHandler getInstance() {
			if (_instance == null) {
				_instance = new StandardEscapeHandler();
			}
			return _instance;
		}
		
		private StandardEscapeHandler() {
			super();
		}
		
		@Override
		public void escape(char[] ch, int start, int length, boolean isAttVal, Writer out) throws IOException {
			int limit = start + length;
			for (int i = start; i < limit; i++) {
				char c = ch[i];
				if (c == '&' || c == '<' || c == '>' || (c == '\"' && isAttVal)
						|| (c == '\'' && isAttVal)) {
					if (i != start) {
						out.write(ch, start, i - start);
					}
					start = i + 1;
					switch (ch[i]) {
					case '&':
						out.write("&amp;");
						break;
					case '<':
						out.write("&lt;");
						break;
					case '>':
						out.write("&gt;");
						break;
					case '\"':
						out.write("&quot;");
						break;
					case '\'':
						out.write("&apos;");
						break;
					}
				}
			}
			if (start != limit) {
				out.write(ch, start, limit - start);
			}
		}
		
		private static StandardEscapeHandler _instance;
		
	}
	
}
