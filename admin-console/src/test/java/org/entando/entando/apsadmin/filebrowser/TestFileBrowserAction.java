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

import java.io.ByteArrayInputStream;
import java.io.File;
import java.util.Collection;

import org.entando.entando.aps.system.services.storage.BasicFileAttributeView;
import org.entando.entando.aps.system.services.storage.IStorageManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agiletec.aps.system.SystemConstants;
import com.agiletec.apsadmin.ApsAdminBaseTestCase;
import com.opensymphony.xwork2.Action;

/**
 * @author E.Santoboni
 */
public class TestFileBrowserAction extends ApsAdminBaseTestCase {
	
	private static final Logger _logger = LoggerFactory.getLogger(TestFileBrowserAction.class);
	
	@Override
	protected void setUp() throws Exception {
		super.setUp();
		this.init();
	}
	
	public void testBrowseFileSystemWithUserNotAllowed() throws Throwable {
		String result = this.executeList("developersConf", null, null);
		assertEquals("apslogin", result);
	}
	
	public void testBrowseFileSystem_1() throws Throwable {
		String result = this.executeList("admin", null, null);
		assertEquals(Action.SUCCESS, result);
		FileBrowserAction action = (FileBrowserAction) super.getAction();
		BasicFileAttributeView[] fileAttributes = action.getFilesAttributes();
		assertNotNull(fileAttributes);
		assertEquals(2, fileAttributes.length);
		for (int i = 0; i < fileAttributes.length; i++) {
			BasicFileAttributeView bfav = fileAttributes[i];
			assertTrue(bfav instanceof RootFolderAttributeView);
			assertTrue(bfav.isDirectory());
			if (i == 0) {
				assertEquals("public", bfav.getName());
			} else {
				assertEquals("protected", bfav.getName());
			}
		}
	}
	
	public void testBrowseFileSystem_2() throws Throwable {
		String result = this.executeList("admin", null, false);
		assertEquals(Action.SUCCESS, result);
		FileBrowserAction action = (FileBrowserAction) super.getAction();
		BasicFileAttributeView[] fileAttributes = action.getFilesAttributes();
		assertNotNull(fileAttributes);
		boolean containsConf = false;
		boolean prevDirectory = true;
		String prevName = null;
		for (int i = 0; i < fileAttributes.length; i++) {
			BasicFileAttributeView bfav = fileAttributes[i];
			if (!prevDirectory && bfav.isDirectory()) {
				fail();
			}
			if (bfav.isDirectory() && bfav.getName().equals("conf")) {
				containsConf = true;
			}
			if ((bfav.isDirectory() == prevDirectory) && null != prevName) {
				assertTrue(bfav.getName().compareTo(prevName) > 0);
			}
			prevName = bfav.getName();
			prevDirectory = bfav.isDirectory();
		}
		assertTrue(containsConf);
	}
	
	public void testBrowseFileSystem_3() throws Throwable {
		String result = this.executeList("admin", "conf" + File.separator, false);
		assertEquals(Action.SUCCESS, result);
		FileBrowserAction action = (FileBrowserAction) super.getAction();
		BasicFileAttributeView[] fileAttributes = action.getFilesAttributes();
		assertEquals(2, fileAttributes.length);
		int dirCounter = 0;
		int fileCounter = 0;
		for (int i = 0; i < fileAttributes.length; i++) {
			BasicFileAttributeView bfav = fileAttributes[i];
			if (bfav.isDirectory()) {
				dirCounter++;
			} else {
				fileCounter++;
			}
		}
		assertEquals(0, dirCounter);
		assertEquals(2, fileCounter);
	}
	
	public void testValidateAddTextFile() throws Throwable {
		String path = "conf" + File.separator;
		try {
			String result = this.executeAddTextFile("developersConf", path, "filename", "css", "content", false);
			assertEquals("apslogin", result);
			
			result = this.executeAddTextFile("admin", path, "", "", "content", false);
			assertEquals(Action.INPUT, result);
			assertEquals(2, this.getAction().getFieldErrors().size());
			
			result = this.executeAddTextFile("admin", path, "filename", "", "", false);
			assertEquals(Action.INPUT, result);
			assertEquals(1, this.getAction().getFieldErrors().size());
			
			result = this.executeAddTextFile("admin", path, "filename", "exe", "content", false);
			assertEquals(Action.INPUT, result);
			assertEquals(1, this.getAction().getFieldErrors().size());
			
		} catch (Throwable t) {
			throw t;
		}
	}
	
	public void testAddTextFile() throws Throwable {
		String path = "conf" + File.separator;
		String filename = "test_filename_1";
		String extension = "css";
		String fullPath = path + filename + "." + extension;
		String text = "This is the content";
		try {
			String result = this.executeAddTextFile("admin", path, filename, extension, text, false);
			//FileBrowserAction action = (FileBrowserAction) this.getAction();
			assertEquals(Action.SUCCESS, result);
			assertTrue(this._localStorageManager.exists(fullPath, false));
			
			result = this.executeAddTextFile("admin", path, filename, extension, text, false);
			assertEquals(Action.INPUT, result);
			assertEquals(1, this.getAction().getFieldErrors().size());
			assertEquals(1, this.getAction().getFieldErrors().get("filename").size());
			
			String extractedText = this._localStorageManager.readFile(fullPath, false);
			assertEquals(text, extractedText);
			this._localStorageManager.deleteFile(fullPath, false);
			assertFalse(this._localStorageManager.exists(fullPath, false));
		} catch (Throwable t) {
			this._localStorageManager.deleteFile(fullPath, false);
			throw t;
		}
	}

	public void testAddTextFileWithErrors() throws Throwable {
		String path = "conf" + File.separator;
		String filename = "test_filename_1";
		String extension = "css";
		String fullPath = path + filename + "." + extension;
		String text = "This is the content";
		try {
			
			String filename1 = filename + "è";
			fullPath = path + filename1 + "." + extension;
			String result = this.executeAddTextFile("admin", path, filename1, extension, text, false);
			assertEquals(Action.INPUT, result);
			FileBrowserAction action = (FileBrowserAction) this.getAction();
			Collection<String> actionErrors = action.getActionErrors();
			assertEquals(1, actionErrors.size());
			this._localStorageManager.deleteFile(fullPath, false);

			String filename2 = "../" + filename;
			fullPath = path + filename2 + "." + extension;
			result = this.executeAddTextFile("admin", path, filename2, extension, text, false);
			assertEquals(Action.INPUT, result);
			action = (FileBrowserAction) this.getAction();
			actionErrors = action.getActionErrors();
			assertEquals(1, actionErrors.size());
			this._localStorageManager.deleteFile(fullPath, false);

			path = "../" + path;
			fullPath = path + filename + "." + extension;
			result = this.executeAddTextFile("admin", path, filename, extension, text, false);
			assertEquals(Action.INPUT, result);
			action = (FileBrowserAction) this.getAction();
			actionErrors = action.getActionErrors();
			assertEquals(1, actionErrors.size());
			this._localStorageManager.deleteFile(fullPath, false);

			
		} catch (Throwable t) {
			this._localStorageManager.deleteFile(fullPath, false);
			throw t;
		}
	}
	
	
	public void testDeleteFile() throws Throwable {
		String path = "conf" + File.separator;
		String filename = "test_filename_2";
		String extension = "css";
		String fullFilename = filename + "." + extension;
		String fullPath = path + fullFilename;
		String text = "This is the content";
		try {
			assertFalse(this._localStorageManager.exists(fullPath, false));
			this._localStorageManager.saveFile(fullPath, false, new ByteArrayInputStream(text.getBytes()));
			assertTrue(this._localStorageManager.exists(fullPath, false));
			String result = this.executeDeleteFile("admin", path, fullFilename, true, false);
			assertEquals(Action.SUCCESS, result);
			assertFalse(this._localStorageManager.exists(fullPath, false));
		} catch (Throwable t) {
			this._localStorageManager.deleteFile(fullPath, false);
			throw t;
		}
	}
	
	public void testTrash() throws Throwable {
		String path = "conf" + File.separator;
		String filename = "test_filename_2";
		String extension = "css";
		String fullFilename = filename + "." + extension;
		String fullPath = path + fullFilename;
		String text = "This is the content";
		this._localStorageManager.deleteFile(fullPath, false);
		try {
			assertFalse(this._localStorageManager.exists(fullPath, false));
			this._localStorageManager.saveFile(fullPath, false, new ByteArrayInputStream(text.getBytes()));
			assertTrue(this._localStorageManager.exists(fullPath, false));
			
			String result = this.executeTrashFile("admin", path, fullFilename, false);
			assertEquals(Action.SUCCESS, result);

			String path2 = path + "../../";
			result = this.executeTrashFile("admin", path2, fullFilename, false);
			assertEquals(Action.INPUT, result);
			FileBrowserAction action = (FileBrowserAction) this.getAction();
			Collection<String> actionErrors = action.getActionErrors();
			assertEquals(1, actionErrors.size());
			System.out.println(actionErrors);
			
			fullFilename = "../conf/" + fullFilename;
			result = this.executeTrashFile("admin", path, fullFilename, false);
			assertEquals(Action.INPUT, result);
			action = (FileBrowserAction) this.getAction();
			actionErrors = action.getActionErrors();
			assertEquals(1, actionErrors.size());
			System.out.println(actionErrors);
			
		} catch (Throwable t) {
			this._localStorageManager.deleteFile(fullPath, false);
			throw t;
		}
		
		this._localStorageManager.deleteFile(fullPath, false);
	}
	
	private String executeList(String currentUser, String path, Boolean isProtected) throws Throwable {
		this.setUserOnSession(currentUser);
		this.initAction("/do/FileBrowser", "list");
		this.addParameter("currentPath", path);
		if (null != isProtected) {
			this.addParameter("protectedFolder", isProtected.toString());
		}
		return this.executeAction();
	}
	
	private String executeAddTextFile(String currentUser, String currentPath, 
			String filename, String extension, String content, Boolean isProtected) throws Throwable {
		this.setUserOnSession(currentUser);
		this.initAction("/do/FileBrowser", "save");
		this.addParameter("currentPath", currentPath);
		this.addParameter("filename", filename);
		this.addParameter("textFileExtension", extension);
		this.addParameter("fileText", content);
		this.addParameter("strutsAction", FileBrowserAction.ADD_NEW_FILE);
		if (null != isProtected) {
			this.addParameter("protectedFolder", isProtected.toString());
		}
		return this.executeAction();
	}
	
	private String executeDeleteFile(String currentUser, String currentPath, 
			String filename, boolean deleteFile, Boolean isProtected) throws Throwable {
		this.setUserOnSession(currentUser);
		this.initAction("/do/FileBrowser", "delete");
		this.addParameter("currentPath", currentPath);
		this.addParameter("filename", filename);
		this.addParameter("deleteFile", new Boolean(deleteFile).toString());
		if (null != isProtected) {
			this.addParameter("protectedFolder", isProtected.toString());
		}
		return this.executeAction();
	}

	private String executeTrashFile(String currentUser, String currentPath, String filename, Boolean isProtected) throws Throwable {
		this.setUserOnSession(currentUser);
		this.initAction("/do/FileBrowser", "trash");
		this.addParameter("currentPath", currentPath);
		this.addParameter("filename", filename);
		if (null != isProtected) {
			this.addParameter("protectedFolder", isProtected.toString());
		}
		return this.executeAction();
	}
	
	private void init() throws Exception {
		try {
			this._localStorageManager = (IStorageManager) this.getApplicationContext().getBean(SystemConstants.STORAGE_MANAGER);
		} catch (Throwable t) {
			_logger.error("error on init", t);
		}
	}
	
	private IStorageManager _localStorageManager;
	
}
