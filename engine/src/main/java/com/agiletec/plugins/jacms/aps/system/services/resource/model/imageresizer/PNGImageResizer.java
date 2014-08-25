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

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.HeadlessException;
import java.awt.Image;
import java.awt.Transparency;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.PixelGrabber;
import java.io.File;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.plugins.jacms.aps.system.services.resource.model.ImageResourceDimension;

/**
 * Classe delegata al redimensionameno e salvataggio di file tipo immagine di tipo PNG.
 * @author E.Santoboni
 */
public class PNGImageResizer extends AbstractImageResizer {

	private static final Logger _logger = LoggerFactory.getLogger(PNGImageResizer.class);
	
	@Override
	@Deprecated
	public void saveResizedImage(ImageIcon imageIcon, String filePath, ImageResourceDimension dimension) throws ApsSystemException {
		BufferedImage imageResized = this.getResizedImage(imageIcon, dimension.getDimx(), dimension.getDimy());
		try {
			File file = new File(filePath);
	        ImageIO.write(imageResized, this.getFileExtension(filePath), file);
		} catch (Throwable t) {
			String msg = this.getClass().getName() + ": saveResizedImage: " + t.toString();
			_logger.error(" Error in saveResizedImage",t);
			throw new ApsSystemException(msg, t);
		}
	}
	
	/**
	 * Crea e restituisce un'immagine in base all'immagine master ed alle dimensioni massime consentite.
	 * L'immagine risultante sarà un'immagine rispettante le proporzioni dell'immagine sorgente.
	 * @param imageIcon L'immagine sorgente.
	 * @param dimensioneX la dimensione orizzontale massima.
	 * @param dimensioneY La dimensione verticale massima.
	 * @return L'immagine risultante.
	 * @throws ApsSystemException In caso di errore.
	 */
	@Override
	protected BufferedImage getResizedImage(ImageIcon imageIcon, int dimensioneX, int dimensioneY) throws ApsSystemException {
    	Image image = imageIcon.getImage();
    	BufferedImage bi = this.toBufferedImage(image);
    	double scale = this.computeScale(image.getWidth(null), image.getHeight(null), dimensioneX, dimensioneY);
		int scaledW = (int) (scale * image.getWidth(null));
		int scaledH = (int) (scale * image.getHeight(null));
		BufferedImage biRes = new BufferedImage(bi.getColorModel(), 
        		bi.getColorModel().createCompatibleWritableRaster(scaledW, scaledH), 
        		bi.isAlphaPremultiplied() , null);
        AffineTransform tx = new AffineTransform();
		tx.scale(scale, scale);
        Graphics2D bufImageGraphics = biRes.createGraphics();
        bufImageGraphics.drawImage(image, tx, null);
        return biRes;
	}
	
	protected BufferedImage toBufferedImage(Image image) throws ApsSystemException {
		if (image instanceof BufferedImage) {
			return (BufferedImage) image;
		}
		// This code ensures that all the pixels in the image are loaded
		image = new ImageIcon(image).getImage();
		// Determine if the image has transparent pixels.
		boolean hasAlpha = this.hasAlpha(image);
		// Create a buffered image with a format that's compatible with the
		// screen
		BufferedImage bimage = null;
		GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
		try {
			// Determine the type of transparency of the new buffered image
			int transparency = Transparency.OPAQUE;
			if (hasAlpha) {
				transparency = Transparency.BITMASK;
			}
			// Create the buffered image
			GraphicsDevice gs = ge.getDefaultScreenDevice();
			GraphicsConfiguration gc = gs.getDefaultConfiguration();
			bimage = gc.createCompatibleImage(image.getWidth(null), image.getHeight(null), transparency);
		} catch (HeadlessException e) {
			throw new ApsSystemException("The system does not have a screen", e);
		}
		if (bimage == null) {
			// Create a buffered image using the default color model
			int type = BufferedImage.TYPE_INT_RGB;
			if (hasAlpha) {
				type = BufferedImage.TYPE_INT_ARGB;
			}
			bimage = new BufferedImage(image.getWidth(null), 
					image.getHeight(null), type);
		}
		// Copy image to buffered image
		Graphics graphics = bimage.createGraphics();
		// Paint the image onto the buffered image
		graphics.drawImage(image, 0, 0, null);
		graphics.dispose();
		return bimage;
	}
	
	protected boolean hasAlpha(Image image) throws ApsSystemException {
        // If buffered image, the color model is readily available
        if (image instanceof BufferedImage) {
            BufferedImage bimage = (BufferedImage)image;
            return bimage.getColorModel().hasAlpha();
        }
        // Use a pixel grabber to retrieve the image's color model;
        // grabbing a single pixel is usually sufficient
        PixelGrabber pg = new PixelGrabber(image, 0, 0, 1, 1, false);
        try {
            pg.grabPixels();
        } catch (InterruptedException e) {
        	throw new ApsSystemException("Error grabbing a single pixel", e);
        }
        // Get the image's color model
        ColorModel cm = pg.getColorModel();
        return cm.hasAlpha();
	}
	
}
