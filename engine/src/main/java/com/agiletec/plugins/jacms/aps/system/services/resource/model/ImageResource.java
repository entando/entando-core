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
package com.agiletec.plugins.jacms.aps.system.services.resource.model;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Iterator;
import java.util.Map;

import javax.swing.ImageIcon;

import org.im4java.core.ConvertCmd;
import org.im4java.core.IMOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.plugins.jacms.aps.system.services.resource.model.imageresizer.IImageResizer;
import com.agiletec.plugins.jacms.aps.system.services.resource.model.util.IImageDimensionReader;
import org.apache.commons.io.FilenameUtils;

/**
 * Classe rappresentante una risorsa Image.
 *
 * @author W.Ambu - E.Santoboni
 */
public class ImageResource extends AbstractMultiInstanceResource {

    private IImageDimensionReader imageDimensionReader;
    private Map<String, String> imageResizerClasses;
    private boolean imageMagickEnabled;
    private boolean imageMagickWindows;
    private String imageMagickPath;

    private static final Logger _logger = LoggerFactory.getLogger(ImageResource.class);

    /**
     * Restituisce il path dell'immagine (relativa al size immesso). La stringa
     * restituita è comprensiva del folder della risorsa e del nome del file
     * dell'istanza richiesta.
     *
     * @param size Il size dell'istanza.
     * @return Il path dell'immagine.
     */
    public String getImagePath(String size) {
        ResourceInstance instance = (ResourceInstance) this.getInstances().get(size);
        return this.getUrlPath(instance);
    }

    @Override
    public InputStream getResourceStream() {
        return this.getResourceStream(0, null);
    }

    @Override
    public InputStream getResourceStream(int size, String langCode) {
        ResourceInstance instance = (ResourceInstance) this.getInstances().get(String.valueOf(size));
        String subPath = super.getDiskSubFolder() + instance.getFileName();
        try {
            return this.getStorageManager().getStream(subPath, this.isProtectedResource());
        } catch (Throwable t) {
            _logger.error("Error on extracting file", t);
            throw new RuntimeException("Error on extracting file", t);
        }
    }

    @Override
    public ResourceInterface getResourcePrototype() {
        ImageResource resource = (ImageResource) super.getResourcePrototype();
        resource.setImageDimensionReader(this.getImageDimensionReader());
        resource.setImageResizerClasses(this.getImageResizerClasses());
        resource.setImageMagickEnabled(this.isImageMagickEnabled());
        resource.setImageMagickWindows(this.isImageMagickWindows());
        resource.setImageMagickPath(this.getImageMagickPath());
        return resource;
    }

    /**
     * Aggiunge un'istanza alla risorsa, indicizzandola in base al size
     * dell'istanza sulla mappa delle istanze.
     *
     * @param instance L'istanza da aggiungere alla risorsa.
     */
    @Override
    public void addInstance(ResourceInstance instance) {
        String key = String.valueOf(instance.getSize());
        this.getInstances().put(key, instance);
    }

    @Override
    @Deprecated
    public String getInstanceFileName(String masterFileName, int size, String langCode) {
        return getNewInstanceFileName(masterFileName, size, langCode);
    }

    @Override
    public ResourceInstance getInstance(int size, String langCode) {
        return (ResourceInstance) this.getInstances().get(String.valueOf(size));
    }

    @Override
    public ResourceInstance getDefaultInstance() {
        return this.getInstance(0, null);
    }

    @Override
    public void saveResourceInstances(ResourceDataBean bean) throws ApsSystemException {
        try {
            String masterImageFileName = getNewInstanceFileName(bean.getFileName(), 0, null);
            String subPath = this.getDiskSubFolder() + masterImageFileName;
            this.getStorageManager().deleteFile(subPath, this.isProtectedResource());
            File tempMasterFile = this.saveTempFile("temp_" + masterImageFileName, bean.getInputStream());
            ResourceInstance instance = new ResourceInstance();
            instance.setSize(0);
            instance.setFileName(masterImageFileName);
            String mimeType = bean.getMimeType();
            instance.setMimeType(mimeType);
            instance.setFileLength(bean.getFileSize() + " Kb");
            this.addInstance(instance);
            this.saveResizedInstances(bean, tempMasterFile.getAbsolutePath());
            this.getStorageManager().saveFile(subPath,
                    this.isProtectedResource(), new FileInputStream(tempMasterFile));
            boolean deleted = tempMasterFile.delete();
            if (!deleted) {
                _logger.warn("Failed to delete temp file {}", tempMasterFile);
            }
        } catch (Throwable t) {
            _logger.error("Error saving image resource instances", t);
            throw new ApsSystemException("Error saving image resource instances", t);
        }
    }

    @Override
    public void reloadResourceInstances() throws ApsSystemException {
        try {
            ResourceInstance masterInstance = this.getInstance(0, null);
            String masterImageFileName = masterInstance.getFileName();
            String subPath = this.getDiskSubFolder() + masterImageFileName;
            InputStream masterImageIs = this.getStorageManager().getStream(subPath, this.isProtectedResource());
            File tempMasterFile = this.saveTempFile("temp_" + masterImageFileName, masterImageIs);
            BaseResourceDataBean bean = new BaseResourceDataBean(tempMasterFile);
            int index = masterImageFileName.lastIndexOf("_d0.");
            String masterFileName = masterImageFileName.substring(0, index) + masterImageFileName.substring(index + 3);
            bean.setFileName(masterFileName);
            bean.setMimeType(masterInstance.getMimeType());
            this.saveResizedInstances(bean, tempMasterFile.getAbsolutePath());
            boolean deleted = tempMasterFile.delete();
            if (!deleted) {
                _logger.warn("Failed to delete temp file {}", tempMasterFile);
            }
        } catch (Throwable t) {
            _logger.error("Error reloading image resource instances", t);
            throw new ApsSystemException("Error reloading image resource instances", t);
        }
    }

    private void saveResizedInstances(ResourceDataBean bean, String masterFilePath) throws ApsSystemException {
        try {
            String extension = FilenameUtils.getExtension(bean.getFileName());
            if (extension.equalsIgnoreCase("svg")) {
                return;
            }
            Map<Integer, ImageResourceDimension> dimensions = this.getImageDimensionReader().getImageDimensions();
            Iterator<ImageResourceDimension> iterDimensions = dimensions.values().iterator();
            while (iterDimensions.hasNext()) {
                ImageResourceDimension dimension = iterDimensions.next();
                //Is the system use ImageMagick?
                if (!this.isImageMagickEnabled()) {
                    ImageIcon imageIcon = new ImageIcon(masterFilePath);
                    this.saveResizedImage(bean, imageIcon, dimension);
                } else {
                    this.saveResizedImage(bean, dimension);
                }
            }
        } catch (Throwable t) {
            _logger.error("Error saving resized image resource instances", t);
            throw new ApsSystemException("Error saving resized image resource instances", t);
        }
    }

    /**
     * Redim images using im4Java
     *
     * @param bean
     * @param dimension
     * @throws ApsSystemException
     */
    private void saveResizedImage(ResourceDataBean bean, ImageResourceDimension dimension) throws ApsSystemException {
        if (dimension.getIdDim() == 0) {
            //salta l'elemento con id zero che non va ridimensionato
            return;
        }
        String imageName = getNewInstanceFileName(bean.getFileName(), dimension.getIdDim(), null);
        String subPath = super.getDiskSubFolder() + imageName;
        try {
            this.getStorageManager().deleteFile(subPath, this.isProtectedResource());
            ResourceInstance resizedInstance = new ResourceInstance();
            resizedInstance.setSize(dimension.getIdDim());
            resizedInstance.setFileName(imageName);
            resizedInstance.setMimeType(bean.getMimeType());
            String tempFilePath = System.getProperty("java.io.tmpdir") + File.separator + "temp_" + imageName;
            File tempFile = new File(tempFilePath);
            long realLength = tempFile.length() / 1000;
            resizedInstance.setFileLength(String.valueOf(realLength) + " Kb");
            this.addInstance(resizedInstance);
            // create command
            ConvertCmd convertCmd = new ConvertCmd();
            //Is it a windows system?
            if (this.isImageMagickWindows()) {
                //yes so configure the path where ImagicMagick is installed
                convertCmd.setSearchPath(this.getImageMagickPath());
            }
            // create the operation, add images and operators/options
            IMOperation imOper = new IMOperation();
            imOper.addImage();
            imOper.resize(dimension.getDimx(), dimension.getDimy());
            imOper.addImage();
            convertCmd.run(imOper, bean.getFile().getAbsolutePath(), tempFile.getAbsolutePath());
            this.getStorageManager().saveFile(subPath, this.isProtectedResource(), new FileInputStream(tempFile));
            boolean deleted = tempFile.delete();

            if (!deleted) {
                _logger.warn("Failed to delete temp file {}", tempFile);
            }
        } catch (Throwable t) {
            _logger.error("Error creating resource file instance '{}'", subPath, t);
            throw new ApsSystemException("Error creating resource file instance '" + subPath + "'", t);
        }
    }

    private void saveResizedImage(ResourceDataBean bean,
            ImageIcon imageIcon, ImageResourceDimension dimension) throws ApsSystemException {
        if (dimension.getIdDim() == 0) {
            //salta l'elemento con id zero che non va ridimensionato
            return;
        }
        String imageName = this.getNewInstanceFileName(bean.getFileName(), dimension.getIdDim(), null);
        String subPath = super.getDiskSubFolder() + imageName;
        try {
            this.getStorageManager().deleteFile(subPath, this.isProtectedResource());
            IImageResizer resizer = this.getImageResizer(subPath);
            ResourceInstance resizedInstance
                    = resizer.saveResizedImage(subPath, this.isProtectedResource(), imageIcon, dimension);
            this.addInstance(resizedInstance);
        } catch (Throwable t) {
            _logger.error("Error creating resource file instance '{}'", subPath, t);
            throw new ApsSystemException("Error creating resource file instance '" + subPath + "'", t);
        }
    }

    private IImageResizer getImageResizer(String filePath) {
        String extension = FilenameUtils.getExtension(filePath);
        String resizerClassName = this.getImageResizerClasses().get(extension);
        if (null == resizerClassName) {
            resizerClassName = this.getImageResizerClasses().get("DEFAULT_RESIZER");
        }
        try {
            Class resizerClass = Class.forName(resizerClassName);
            IImageResizer resizer = (IImageResizer) resizerClass.newInstance();
            resizer.setStorageManager(this.getStorageManager());
            return resizer;
        } catch (Throwable t) {
            throw new RuntimeException("Errore in creazione resizer da classe '"
                    + resizerClassName + "' per immagine tipo '" + extension + "'", t);
        }
    }

    protected IImageDimensionReader getImageDimensionReader() {
        return imageDimensionReader;
    }

    public void setImageDimensionReader(IImageDimensionReader imageDimensionReader) {
        this.imageDimensionReader = imageDimensionReader;
    }

    protected Map<String, String> getImageResizerClasses() {
        return imageResizerClasses;
    }

    public void setImageResizerClasses(Map<String, String> imageResizerClasses) {
        this.imageResizerClasses = imageResizerClasses;
    }

    public void setImageMagickEnabled(boolean imageMagickEnabled) {
        this.imageMagickEnabled = imageMagickEnabled;
    }

    protected boolean isImageMagickEnabled() {
        return imageMagickEnabled;
    }

    public void setImageMagickWindows(boolean imageMagickWindows) {
        this.imageMagickWindows = imageMagickWindows;
    }

    protected boolean isImageMagickWindows() {
        return imageMagickWindows;
    }

    public void setImageMagickPath(String imageMagickPath) {
        this.imageMagickPath = imageMagickPath;
    }

    protected String getImageMagickPath() {
        return imageMagickPath;
    }

}
