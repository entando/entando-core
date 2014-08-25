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

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.plugins.jacms.aps.system.services.resource.model.ImageResourceDimension;

/**
 * Classe di default delegata al redimensionameno e salvataggio di file tipo immagine.
 * @author E.Santoboni
 */
public class DefaultImageResizer extends AbstractImageResizer {

	private static final Logger _logger = LoggerFactory.getLogger(DefaultImageResizer.class);
	
	@Override
	@Deprecated
	public void saveResizedImage(ImageIcon imageIcon, String filePath, ImageResourceDimension dimension) throws ApsSystemException {
		BufferedImage outImage = this.getResizedImage(imageIcon, dimension.getDimx(), dimension.getDimy());
		try {
			File file = new File(filePath);
	        ImageIO.write(outImage, this.getFileExtension(filePath), file);
		} catch (Throwable t) {
			_logger.error("Error creating resized Image", t);
			//String msg = "Error creating resigned Image";
			//ApsSystemUtils.logThrowable(t, this, "saveImageResized", msg);
			throw new ApsSystemException("Error creating resized Image", t);
		}
	}
	
	@Override
	protected BufferedImage getResizedImage(ImageIcon imageIcon, int dimensionX, int dimensionY) throws ApsSystemException {
		try {
			Image image = imageIcon.getImage();
			double scale = this.computeScale(image.getWidth(null), image.getHeight(null), dimensionX, dimensionY);
			int scaledW = (int) (scale * image.getWidth(null));
			int scaledH = (int) (scale * image.getHeight(null));
			BufferedImage outImage = new BufferedImage(scaledW, scaledH, BufferedImage.TYPE_INT_RGB);
			AffineTransform tx = new AffineTransform();
			tx.scale(scale, scale);
			Graphics2D g2d = outImage.createGraphics();
			g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
			g2d.setRenderingHint(RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_QUALITY);
			g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
			g2d.drawImage(image, tx, null);
			g2d.dispose();
			return outImage;
		} catch (Throwable t) {
			_logger.error("Error creating resized Image", t);
			//ApsSystemUtils.logThrowable(t, this, "getResizedImage", msg);
			throw new ApsSystemException("Error creating resized Image", t);
		}
	}
	
}
