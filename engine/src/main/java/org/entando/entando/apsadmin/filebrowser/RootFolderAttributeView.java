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
package org.entando.entando.apsadmin.filebrowser;

import org.entando.entando.aps.system.services.storage.BasicFileAttributeView;

/**
 * @author E.Santoboni
 */
public class RootFolderAttributeView extends BasicFileAttributeView {
	
	protected RootFolderAttributeView(String name, boolean isProtected) {
		this.setName(name);
		this.setDirectory(true);
		this.setProtectedFolder(isProtected);
	}
	
	public boolean isProtectedFolder() {
		return _protectedFolder;
	}
	protected void setProtectedFolder(boolean protectedFolder) {
		this._protectedFolder = protectedFolder;
	}
	
	private boolean _protectedFolder;
	
}
