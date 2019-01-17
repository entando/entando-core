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
package com.agiletec.plugins.jacms.aps.system.services.searchengine;

import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.aps.system.services.baseconfig.ConfigInterface;
import com.agiletec.aps.system.services.lang.ILangManager;
import com.agiletec.plugins.jacms.aps.system.JacmsSystemConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;

/**
 * Classe factory degli elementi ad uso del SearchEngine.
 *
 * @author E.Santoboni
 */
public class SearchEngineDAOFactory implements ISearchEngineDAOFactory {

    private static final Logger _logger = LoggerFactory.getLogger(SearchEngineDAOFactory.class);

    private String indexerClassName;
    private String searcherClassName;

    private String indexDiskRootFolder;
    private String subDirectory;

    private ConfigInterface configManager;
    private ILangManager langManager;

    @Override
    public void init() throws Exception {
        this.subDirectory = this.getConfigManager().getConfigItem(JacmsSystemConstants.CONFIG_ITEM_CONTENT_INDEX_SUB_DIR);
        if (this.subDirectory == null) {
            throw new ApsSystemException("Item configurazione assente: " + JacmsSystemConstants.CONFIG_ITEM_CONTENT_INDEX_SUB_DIR);
        }
    }

    @Override
    public boolean checkCurrentSubfolder() throws ApsSystemException {
        String currentSubDir = this.getConfigManager().getConfigItem(JacmsSystemConstants.CONFIG_ITEM_CONTENT_INDEX_SUB_DIR);
        boolean check = currentSubDir.equals(this.subDirectory);
        if (!check) {
            _logger.info("Actual subfolder {} - Current subfolder {}", this.subDirectory, currentSubDir);
        }
        return check;
    }

    @Override
    public IIndexerDAO getIndexer() throws ApsSystemException {
        return this.getIndexer(this.subDirectory);
    }

    @Override
    public ISearcherDAO getSearcher() throws ApsSystemException {
        return this.getSearcher(this.subDirectory);
    }

    @Override
    public IIndexerDAO getIndexer(String subDir) throws ApsSystemException {
        IIndexerDAO indexerDao = null;
        try {
            Class indexerClass = Class.forName(this.getIndexerClassName());
            indexerDao = (IIndexerDAO) indexerClass.newInstance();
            indexerDao.setLangManager(this.getLangManager());
            indexerDao.init(this.getDirectory(subDir));
        } catch (Throwable t) {
            _logger.error("Error getting indexer", t);
            throw new ApsSystemException("Error creating new indexer", t);
        }
        return indexerDao;
    }

    @Override
    public ISearcherDAO getSearcher(String subDir) throws ApsSystemException {
        ISearcherDAO searcherDao = null;
        try {
            Class searcherClass = Class.forName(this.getSearcherClassName());
            searcherDao = (ISearcherDAO) searcherClass.newInstance();
            searcherDao.init(this.getDirectory(subDir));
        } catch (Throwable t) {
            _logger.error("Error creating new searcher", t);
            throw new ApsSystemException("Error creating new searcher", t);
        }
        return searcherDao;
    }

    @Override
    public void updateSubDir(String newSubDirectory) throws ApsSystemException {
        this.getConfigManager().updateConfigItem(JacmsSystemConstants.CONFIG_ITEM_CONTENT_INDEX_SUB_DIR, newSubDirectory);
        String oldDir = subDirectory;
        this.subDirectory = newSubDirectory;
        this.deleteSubDirectory(oldDir);
    }

    private File getDirectory(String subDirectory) throws ApsSystemException {
        String dirName = this.getIndexDiskRootFolder();
        if (!dirName.endsWith("/")) {
            dirName += "/";
        }
        dirName += "cmscontents/" + subDirectory;
        _logger.debug("Index Directory: {}", dirName);
        File dir = new File(dirName);
        if (!dir.exists() || !dir.isDirectory()) {
            dir.mkdirs();
            _logger.debug("Index Directory created");
        }
        if (!dir.canRead() || !dir.canWrite()) {
            throw new ApsSystemException(dirName + " does not have r/w rights");
        }
        return dir;
    }

    @Override
    public void deleteSubDirectory(String subDirectory) {
        String dirName = this.getIndexDiskRootFolder();
        if (!dirName.endsWith("/") || !dirName.endsWith(File.separator)) {
            dirName += File.separator;
        }
        dirName += subDirectory;
        File dir = new File(dirName);
        if (dir.exists() || dir.isDirectory()) {
            String[] filesName = dir.list();
            for (int i = 0; i < filesName.length; i++) {
                File fileToDelete = new File(dirName + File.separator + filesName[i]);
                boolean fileDeleted = fileToDelete.delete();
                if (!fileDeleted) {
                    _logger.warn("Failed to delete file {} ", fileToDelete.getAbsolutePath());
                }
            }
            boolean deleted = dir.delete();
            if (!deleted) {
                _logger.warn("Failed to delete file {}", deleted);
            } else {
                _logger.debug("Deleted subfolder {}", subDirectory);
            }
        }
    }

    public String getIndexerClassName() {
        return indexerClassName;
    }

    public void setIndexerClassName(String indexerClassName) {
        this.indexerClassName = indexerClassName;
    }

    public String getSearcherClassName() {
        return searcherClassName;
    }

    public void setSearcherClassName(String searcherClassName) {
        this.searcherClassName = searcherClassName;
    }

    protected String getIndexDiskRootFolder() {
        return indexDiskRootFolder;
    }

    public void setIndexDiskRootFolder(String indexDiskRootFolder) {
        this.indexDiskRootFolder = indexDiskRootFolder;
    }

    protected ConfigInterface getConfigManager() {
        return configManager;
    }

    public void setConfigManager(ConfigInterface configService) {
        this.configManager = configService;
    }

    protected ILangManager getLangManager() {
        return langManager;
    }

    public void setLangManager(ILangManager langManager) {
        this.langManager = langManager;
    }

}
