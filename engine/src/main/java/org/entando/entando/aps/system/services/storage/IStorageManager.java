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
package org.entando.entando.aps.system.services.storage;

import java.io.InputStream;
import java.io.Serializable;

import com.agiletec.aps.system.exception.ApsSystemException;

/**
 * @author E.Santoboni
 */
public interface IStorageManager extends Serializable {
	
	public void saveFile(String subPath, boolean isProtectedResource, InputStream is) throws ApsSystemException;
	
	public boolean deleteFile(String subPath, boolean isProtectedResource) throws ApsSystemException;
	
	public void createDirectory(String subPath, boolean isProtectedResource) throws ApsSystemException;
	
	public void deleteDirectory(String subPath, boolean isProtectedResource) throws ApsSystemException;
	
	public InputStream getStream(String subPath, boolean isProtectedResource) throws ApsSystemException;
	
	public String getResourceUrl(String subPath, boolean isProtectedResource);
	
	public boolean exists(String subPath, boolean isProtectedResource) throws ApsSystemException;
	
	public BasicFileAttributeView getAttributes(String subPath, boolean isProtectedResource) throws ApsSystemException;
	
	public String[] list(String subPath, boolean isProtectedResource) throws ApsSystemException;
	
	public String[] listDirectory(String subPath, boolean isProtectedResource) throws ApsSystemException;
	
	public String[] listFile(String subPath, boolean isProtectedResource) throws ApsSystemException;
	
	public BasicFileAttributeView[] listAttributes(String subPath, boolean isProtectedResource) throws ApsSystemException;
	
	public BasicFileAttributeView[] listDirectoryAttributes(String subPath, boolean isProtectedResource) throws ApsSystemException;
	
	public BasicFileAttributeView[] listFileAttributes(String subPath, boolean isProtectedResource) throws ApsSystemException;
	
	public String readFile(String subPath, boolean isProtectedResource) throws ApsSystemException;
	
	public void editFile(String subPath, boolean isProtectedResource, InputStream is) throws ApsSystemException;
	
}
