/*
*
* Copyright 2013 Entando S.r.l. (http://www.entando.com) All rights reserved.
*
* This file is part of Entando software.
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
package com.agiletec.aps.tags.util;


/**
 * @version 1.0
 * @author E.Santoboni
 */
public class SmallCategory implements Comparable<SmallCategory> {

	public String getCode() {
		return _code;
	}
	public void setCode(String code) {
		this._code = code;
	}

	public String getTitle() {
		return _title;
	}
	public void setTitle(String title) {
		this._title = title;
	}

	public int compareTo(SmallCategory smallCat) {
		return _title.compareTo(smallCat.getTitle());
	}

	private String _code;
	private String _title;

}