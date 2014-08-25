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
package com.agiletec.plugins.jacms.aps.system.services.resource.model.imageresizer;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.net.URLConnection;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

import org.entando.entando.aps.system.services.storage.IStorageManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.plugins.jacms.aps.system.services.resource.model.ImageResourceDimension;
import com.agiletec.plugins.jacms.aps.system.services.resource.model.ResourceInstance;

/**
 * Classe astratta base a servizio delle classi delegate al redimensionameno e salvataggio di file tipo immagine.
 * @author E.Santoboni
 */
public abstract class AbstractImageResizer implements IImageResizer {

	private static final Logger _logger = LoggerFactory.getLogger(AbstractImageResizer.class);
	
	@Override
	public ResourceInstance saveResizedImage(String subPath, boolean isProtectedResource, 
			ImageIcon imageIcon, ImageResourceDimension dimension) throws ApsSystemException {
		ResourceInstance resizedInstance = new ResourceInstance();
		resizedInstance.setSize(dimension.getIdDim());
		BufferedImage outImage = this.getResizedImage(imageIcon, dimension.getDimx(), dimension.getDimy());
		String filename = subPath.substring(subPath.lastIndexOf("/") + 1);
		resizedInstance.setFileName(filename);
		String tempFilePath = System.getProperty("java.io.tmpdir") + File.separator + "temp_" + filename;
		try {
			File tempFile = new File(tempFilePath);
	        ImageIO.write(outImage, this.getFileExtension(tempFilePath), tempFile);
			this.getStorageManager().saveFile(subPath, isProtectedResource, new FileInputStream(tempFile));
			//resizedInstance.setMimeType(bean.getMimeType());
			long realLength = tempFile.length() / 1000;
			resizedInstance.setFileLength(String.valueOf(realLength) + " Kb");
			tempFile.delete();
		} catch (Throwable t) {
			_logger.error("Error creating resized Image", t);
			String msg = "Error creating resigned Image";
			//ApsSystemUtils.logThrowable(t, this, "saveImageResized", msg);
			throw new ApsSystemException(msg, t);
		}
		String mimeType = URLConnection.guessContentTypeFromName(filename);
		resizedInstance.setMimeType(mimeType);
		return resizedInstance;
	}
	
	protected abstract BufferedImage getResizedImage(ImageIcon imageIcon, int dimensionX, int dimensionY) throws ApsSystemException;
	
	/**
	 * Calcola il rapporto di scala sulla base della dimensione maggiore (tenuto conto
	 * del rapporto finale desiderato).
	 * Il fattore di scala restituito non sarà comunque superiore ad 1.
	 * @param width Dimensione attuale dell'immagine
	 * @param height Dimensione attuale dell'immagine
	 * @param finalWidth Dimensione finale dell'immagine
	 * @param finalHeight Dimensione finale dell'immagine
	 * @return Il fattore di scala da applicare all'immagine
	 */
	protected double computeScale(int width, int height, int finalWidth, int finalHeight) {
		double scale;
		if (((double) width / (double) height) >= ((double) finalWidth / (double) finalHeight)) {
			scale = (double) finalWidth / width;
		} else {
			scale = (double) finalHeight / height;
		}
		if (scale > 1) {
			scale = 1;
		}
		return scale;
	}
	
	protected String getFileExtension(String fileName) {
		return fileName.substring(fileName.lastIndexOf('.')+1).trim();
	}
	
	protected IStorageManager getStorageManager() {
		return _storageManager;
	}
	@Override
	public void setStorageManager(IStorageManager storageManager) {
		this._storageManager = storageManager;
	}
	
	private IStorageManager _storageManager;
	
}
