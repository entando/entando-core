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
package org.entando.entando.plugins.jacms.aps.system.services.api.model;

import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.aps.system.services.category.Category;
import com.agiletec.aps.system.services.category.ICategoryManager;
import com.agiletec.plugins.jacms.aps.system.services.resource.model.BaseResourceDataBean;
import com.agiletec.plugins.jacms.aps.system.services.resource.model.ResourceInterface;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import java.io.*;
import java.net.FileNameMap;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * @author E.Santoboni
 */
@XmlRootElement(name = "resource")
@XmlType(propOrder = {"id", "typeCode", "description", "mainGroup", "fileName", "categories", "base64"})
public class JAXBResource {

	private static final Logger _logger =  LoggerFactory.getLogger(JAXBResource.class);
	
	public JAXBResource() {}

	public JAXBResource(ResourceInterface resource) throws ApsSystemException {
		try {
			this.setDescription(resource.getDescription());
			this.setFileName(resource.getMasterFileName());
			this.setId(resource.getId());
			this.setMainGroup(resource.getMainGroup());
			this.setTypeCode(resource.getType());
			List<Category> resourceCategories = resource.getCategories();
			if (null != resourceCategories && !resourceCategories.isEmpty()) {
				List<String> categories = new ArrayList<>();
				for (int i = 0; i < resourceCategories.size(); i++) {
					Category category = resourceCategories.get(i);
					if (null != category) {
						categories.add(category.getCode());
					}
				}
				this.setCategories(categories);
			}
			InputStream stream = resource.getResourceStream();
			if (null != stream) {
				File tempFile = this.createTempFile(new Random().nextInt(100) + resource.getMasterFileName(), stream);
				byte[] bytes = this.fileToByteArray(tempFile);
				this.setBase64(bytes);
				boolean deleted = tempFile.delete();

				if(!deleted) {
					_logger.warn("Failed to delete temp file {} ",tempFile.getAbsolutePath());
				}


			}
		} catch (IOException t) {
			_logger.error("Error creating jaxb resource", t);
			throw new ApsSystemException("Error creating jaxb resource", t);
		}
	}
	
	protected File createTempFile(String filename, InputStream is) throws IOException {
		String tempDir = System.getProperty("java.io.tmpdir");
		String filePath = tempDir + File.separator + filename;
		FileOutputStream outStream = null;
		try {
			byte[] buffer = new byte[1024];
			int length = -1;
			outStream = new FileOutputStream(filePath);
			while ((length = is.read(buffer)) != -1) {
				outStream.write(buffer, 0, length);
				outStream.flush();
			}
		} catch (IOException t) {
			_logger.error("Error on saving temporary file", t);
			throw t;
		} finally {
			if (null != outStream) {
				outStream.close();
			}
			if (null != is) {
				is.close();
			}
		}
		return new File(filePath);
	}
	
	private byte[] fileToByteArray(File file) throws IOException {
		FileInputStream fis = null;
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		try {
			fis = new FileInputStream(file);
			byte[] buf = new byte[1024];
			for (int readNum; (readNum = fis.read(buf)) != -1;) {
				bos.write(buf, 0, readNum);
			}
		} catch (IOException ex) {
			_logger.error("Error creating byte array", ex);
			throw ex;
		} finally {
			if (null != fis) {
				fis.close();
			}
		}
		return bos.toByteArray();
	}
	
	public BaseResourceDataBean createBataBean(ICategoryManager categoryManager) throws Throwable {
		BaseResourceDataBean bean = new BaseResourceDataBean();
		if (null != this.getCategories()) {
			List<Category> categories = new ArrayList<>();
			for (int i = 0; i < this.getCategories().size(); i++) {
				String categoryCode = this.getCategories().get(i);
				Category category = categoryManager.getCategory(categoryCode);
				if (null != category) {
					categories.add(category);
				}
			}
			bean.setCategories(categories);
		}
		bean.setDescr(this.getDescription());
		bean.setFileName(this.getFileName());
		bean.setMainGroup(this.getMainGroup());
		FileNameMap fileNameMap = URLConnection.getFileNameMap();
		String mimeType = fileNameMap.getContentTypeFor(this.getFileName());
		bean.setMimeType(mimeType);
		bean.setResourceType(this.getTypeCode());
		bean.setResourceId(this.getId());
		if (null != this.getBase64()) {
			File file = this.byteArrayToFile();
			bean.setFile(file);
		}
		return bean;
	}
	
	private File byteArrayToFile() throws IOException {
		String tempDir = System.getProperty("java.io.tmpdir");
		File file = new File(tempDir + File.separator + this.getFileName());
		InputStream inputStream = null;
		OutputStream out = null;
		try {
			inputStream = new ByteArrayInputStream(this.getBase64());
			out = new FileOutputStream(file);
			byte buf[] = new byte[1024];
			int len;
			while ((len = inputStream.read(buf)) > 0) {
				out.write(buf, 0, len);
			}
		} catch (IOException ex) {
			_logger.error("Error creating file from byte array", ex);
			throw ex;
		} finally {
			if (null != out) {
				out.close();
			}
			if (null != inputStream) {
				inputStream.close();
			}
		}
		return file;
	}

	@XmlElement(name = "id", required = true)
	public String getId() {
		return _id;
	}
	public void setId(String id) {
		this._id = id;
	}

	@XmlElement(name = "typeCode", required = true)
	public String getTypeCode() {
		return _typeCode;
	}
	public void setTypeCode(String typeCode) {
		this._typeCode = typeCode;
	}

	@XmlElement(name = "description", required = true)
	public String getDescription() {
		return _description;
	}
	public void setDescription(String description) {
		this._description = description;
	}

	@XmlElement(name = "mainGroup", required = true)
	public String getMainGroup() {
		return _mainGroup;
	}
	public void setMainGroup(String mainGroup) {
		this._mainGroup = mainGroup;
	}

	@XmlElement(name = "fileName", required = true)
	public String getFileName() {
		return _fileName;
	}
	public void setFileName(String fileName) {
		this._fileName = fileName;
	}

	@XmlElement(name = "category", required = true)
    @XmlElementWrapper(name = "categories")
	public List<String> getCategories() {
		return _categories;
	}
	public void setCategories(List<String> categories) {
		this._categories = categories;
	}

	@XmlElement(name = "base64", required = true)
	public byte[] getBase64() {
		return _base64;
	}
	public void setBase64(byte[] base64) {
		this._base64 = base64;
	}

	private String _id;
	private String _typeCode;
	private String _description;
	private String _mainGroup;
	private String _fileName;
	private List<String> _categories;
	private byte[] _base64;
	
}