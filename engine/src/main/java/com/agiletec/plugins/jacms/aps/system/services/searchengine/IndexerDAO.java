/*
 * Copyright 2013-Present Entando Corporation (http://www.entando.com) All rights reserved.
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

import com.agiletec.aps.system.common.entity.model.IApsEntity;
import com.agiletec.aps.system.common.entity.model.attribute.AttributeInterface;
import com.agiletec.aps.system.common.searchengine.IndexableAttributeInterface;
import com.agiletec.aps.system.common.util.EntityAttributeIterator;
import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.aps.system.services.category.Category;
import com.agiletec.aps.system.services.lang.ILangManager;
import com.agiletec.aps.system.services.lang.Lang;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.facet.index.FacetFields;
import org.apache.lucene.facet.taxonomy.CategoryPath;
import org.apache.lucene.facet.taxonomy.TaxonomyWriter;
import org.apache.lucene.facet.taxonomy.directory.DirectoryTaxonomyWriter;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.Term;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Data Access Object dedita alla indicizzazione di documenti.
 * @author E.Santoboni
 */
public class IndexerDAO implements IIndexerDAO {

	private static final Logger _logger = LoggerFactory.getLogger(IndexerDAO.class);
	
	/**
	 * Inizializzazione dell'indicizzatore.
	 * @param dir La cartella locale contenitore dei dati persistenti.
	 * @throws ApsSystemException
	 */
	@Override
	public void init(File dir, File taxoDir) throws ApsSystemException {
		try {
			this._dir = FSDirectory.open(dir);
			this._taxoDir = FSDirectory.open(taxoDir);
			//IndexWriter writer = new IndexWriter(this._dir, this.getIndexWriterConfig());
			//writer.close();
		} catch (Throwable t) {
			_logger.error("Error creating directory", t);
			throw new ApsSystemException("Error creating directory", t);
		}
		_logger.debug("Indexer: search engine index ok.");
	}
	
	@Override
	public void add(IApsEntity entity) throws ApsSystemException {
		try {
            Document document = this.createDocument(entity);
            this.add(document);
        } catch (ApsSystemException e) {
        	_logger.error("Errore saving entity {}", entity.getId(), e);
        	throw e;
        }
	}
	
	/**
	 * Aggiunge un documento nel db del motore di ricerca.
     * @param document Il documento da aggiungere.
	 * @throws ApsSystemException In caso di errori in accesso al db.
	 */
    private synchronized void add(Document document) throws ApsSystemException {
        try {
            IndexWriter writer = new IndexWriter(this._dir, this.getIndexWriterConfig());
			writer.addDocument(document);
            writer.close();
        } catch (IOException e) {
			_logger.error("Error adding document", e);
            throw new ApsSystemException("Error adding document", e);
        }
    }
    
    /**
     * Crea un oggetto Document pronto per l'indicizzazione da un oggetto Content.
     * @param entity Il contenuto dal quale ricavare il Document.
     * @return L'oggetto Document ricavato dal contenuto.
     * @throws ApsSystemException In caso di errore
     */
    private Document createDocument(IApsEntity entity) throws ApsSystemException {
        Document document = new Document();
        document.add(new StringField(CONTENT_ID_FIELD_NAME, 
				entity.getId(), Field.Store.YES));
        document.add(new TextField(CONTENT_GROUP_FIELD_NAME, 
				entity.getMainGroup(), Field.Store.YES));
        Iterator<String> iterGroups = entity.getGroups().iterator();
        while (iterGroups.hasNext()) {
        	String groupName = (String) iterGroups.next();
        	document.add(new TextField(CONTENT_GROUP_FIELD_NAME, 
					groupName, Field.Store.YES));
        }
        try {
        	EntityAttributeIterator attributesIter = new EntityAttributeIterator(entity);
        	while (attributesIter.hasNext()) {
                AttributeInterface currentAttribute = (AttributeInterface) attributesIter.next();
                List<Lang> langs = this.getLangManager().getLangs();
            	for (int i=0; i<langs.size(); i++) {
            		Lang currentLang = (Lang) langs.get(i);
            		this.indexAttribute(document, currentAttribute, currentLang);
            	}
            }
			List<Category> categories = entity.getCategories();
			if (null != categories && !categories.isEmpty()) {
				TaxonomyWriter taxoWriter = new DirectoryTaxonomyWriter(this._taxoDir);
				FacetFields facetFields = new FacetFields(taxoWriter);
				List<CategoryPath> cats = new ArrayList<CategoryPath>();
				for (int i = 0; i < categories.size(); i++) {
					Category category = categories.get(i);
					cats.add(new CategoryPath(category.getPath()));
				}
				facetFields.addFields(document, cats);
				taxoWriter.close();
			}
        } catch (Exception e) {
			_logger.error("Error creating document", e);
            throw new ApsSystemException("Error creating document", e);
        }
        return document;
    }
    
    private void indexAttribute(Document document,
            AttributeInterface attribute, Lang lang) throws ApsSystemException {
    	attribute.setRenderingLang(lang.getCode());
        if (attribute instanceof IndexableAttributeInterface) {
            String valueToIndex = ((IndexableAttributeInterface) attribute).getIndexeableFieldValue();
            String indexingType = attribute.getIndexingType();
            if (null != indexingType && 
            		IndexableAttributeInterface.INDEXING_TYPE_UNSTORED.equalsIgnoreCase(indexingType)) {
            	document.add(new StringField(lang.getCode(), valueToIndex, Field.Store.NO));
            }
            if (null != indexingType && 
            		IndexableAttributeInterface.INDEXING_TYPE_TEXT.equalsIgnoreCase(indexingType)) {
            	document.add(new TextField(lang.getCode(), valueToIndex, Field.Store.YES));
            }
        }
    }

    /**
     * Cancella un documento in base alla chiave (di nome "id") 
     * mediante il quale è stato indicizzato.
     * Nel caso della cancellazione di un contenuto il nome del campo 
     * da utilizzare sarà "id" mentre il valore sarà l'identificativo del contenuto.
     * @param name Il nome del campo Field da utilizzare per recupero del documento.
     * @param value La chiave mediante il quale 
     * è stato indicizzato il documento.
     * @throws ApsSystemException
     */
    @Override
	public synchronized void delete(String name, String value) throws ApsSystemException {
        try {
            IndexWriter writer = new IndexWriter(this._dir, this.getIndexWriterConfig());
            writer.deleteDocuments(new Term(name, value));
            writer.close();
        } catch (IOException e) {
			_logger.error("Error deleting document", e);
            throw new ApsSystemException("Error deleting document", e);
        }
    }
    
    @Override
	public void close() {
    	// nothing to do
    }
    
    private Analyzer getAnalyzer() {
        return new StandardAnalyzer(Version.LUCENE_42);
    }
	
	private IndexWriterConfig getIndexWriterConfig() {
		return new IndexWriterConfig(Version.LUCENE_42, this.getAnalyzer());
	}
    
	protected ILangManager getLangManager() {
		return _langManager;
	}
	@Override
	public void setLangManager(ILangManager langManager) {
		this._langManager = langManager;
	}
	
    private Directory _dir;
    private Directory _taxoDir;
	
    private ILangManager _langManager;
    
}