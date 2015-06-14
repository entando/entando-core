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
	 * @param taxoDir La cartella locale delle tassonomie
	 * @throws ApsSystemException In caso di errore
	 */
	@Override
	public void init(File dir, File taxoDir) throws ApsSystemException {
		try {
			this._dir = FSDirectory.open(dir);
			this._taxoDir = FSDirectory.open(taxoDir);
		} catch (Throwable t) {
			_logger.error("Error creating directory", t);
			throw new ApsSystemException("Error creating directory", t);
		}
		_logger.debug("Indexer: search engine index ok.");
	}
	
	@Override
	public synchronized void add(IApsEntity entity) throws ApsSystemException {
		IndexWriter writer = null;
		TaxonomyWriter taxoWriter = null;
		try {
			writer = new IndexWriter(this._dir, this.getIndexWriterConfig());
			taxoWriter = new DirectoryTaxonomyWriter(this._taxoDir);
            Document document = this.createDocument(entity, taxoWriter);
            writer.addDocument(document);
        } catch (Throwable t) {
        	_logger.error("Errore saving entity {}", entity.getId(), t);
        	throw new ApsSystemException("Error saving entity", t);
        } finally {
			if (null != taxoWriter) {
				try {
					taxoWriter.close();
				} catch (IOException ex) {
					_logger.error("Error closing TaxonomyWriter", ex);
				}
			}
			if (null != writer) {
				try {
					writer.close();
				} catch (IOException ex) {
					_logger.error("Error closing IndexWriter", ex);
				}
			}
		}
	}
	
    /**
     * Crea un oggetto Document pronto per l'indicizzazione da un oggetto Content.
     * @param entity Il contenuto dal quale ricavare il Document.
     * @return L'oggetto Document ricavato dal contenuto.
     * @throws ApsSystemException In caso di errore
     */
    private Document createDocument(IApsEntity entity, TaxonomyWriter taxoWriter) throws ApsSystemException {
        Document document = new Document();
        document.add(new StringField(CONTENT_ID_FIELD_NAME, 
				entity.getId(), Field.Store.YES));
        document.add(new StringField(CONTENT_GROUP_FIELD_NAME, 
				entity.getMainGroup(), Field.Store.YES));
        Iterator<String> iterGroups = entity.getGroups().iterator();
        while (iterGroups.hasNext()) {
        	String groupName = (String) iterGroups.next();
        	document.add(new StringField(CONTENT_GROUP_FIELD_NAME, 
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
				FacetFields facetFields = new FacetFields(taxoWriter);
				List<CategoryPath> cats = new ArrayList<CategoryPath>();
				for (int i = 0; i < categories.size(); i++) {
					Category category = categories.get(i);
					CategoryPath cp = new CategoryPath(category.getPathArray());
					cats.add(cp);
					document.add(new StringField(CONTENT_CATEGORY_FIELD_NAME, 
							category.getPath(CONTENT_CATEGORY_SEPARATOR), Field.Store.YES));
				}
				facetFields.addFields(document, cats);
			}
        } catch (Throwable t) {
			_logger.error("Error creating document", t);
            throw new ApsSystemException("Error creating document", t);
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
            	document.add(new TextField(lang.getCode(), valueToIndex, Field.Store.NO));
            }
            if (null != indexingType && 
            		IndexableAttributeInterface.INDEXING_TYPE_TEXT.equalsIgnoreCase(indexingType)) {
            	document.add(new TextField(lang.getCode(), valueToIndex, Field.Store.YES));
            }
        }
    }
	
    /**
     * Cancella un documento.
     * @param name Il nome del campo Field da utilizzare per recupero del documento.
     * @param value La chiave mediante il quale è stato indicizzato il documento.
     * @throws ApsSystemException In caso di errore
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
        return new StandardAnalyzer(Version.LUCENE_46);
    }
	
	private IndexWriterConfig getIndexWriterConfig() {
		return new IndexWriterConfig(Version.LUCENE_46, this.getAnalyzer());
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