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
package com.agiletec.plugins.jacms.aps.system.services.searchengine;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriter.MaxFieldLength;
import org.apache.lucene.index.Term;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agiletec.aps.system.common.entity.model.IApsEntity;
import com.agiletec.aps.system.common.entity.model.attribute.AttributeInterface;
import com.agiletec.aps.system.common.searchengine.IndexableAttributeInterface;
import com.agiletec.aps.system.common.util.EntityAttributeIterator;
import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.aps.system.services.lang.ILangManager;
import com.agiletec.aps.system.services.lang.Lang;

/**
 * Data Access Object dedita alla indicizzazione di documenti.
 * @author W.Ambu
 */
public class IndexerDAO implements IIndexerDAO {

	private static final Logger _logger = LoggerFactory.getLogger(IndexerDAO.class);
	
	/**
	 * Inizializzazione dell'indicizzatore.
	 * @param dir La cartella locale contenitore dei dati persistenti.
	 * @param newIndex true se è una nuova indicizzazione (ed in tal caso 
	 * cancella tutte le precedenti indicizzazioni), false in caso contrario.
	 * @throws ApsSystemException
	 */
	@Override
	public void init(File dir, boolean newIndex) throws ApsSystemException {
		try {
			this._dir = FSDirectory.open(dir);
			boolean indexExists = IndexReader.indexExists(this._dir);
			IndexWriter writer = new IndexWriter(_dir, getAnalyzer(), !indexExists, new MaxFieldLength(IndexWriter.DEFAULT_MAX_FIELD_LENGTH));
			writer.close();
		} catch (Throwable t) {
			throw new ApsSystemException("Errore in creazione directory", t);
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
        	//ApsSystemUtils.logThrowable(e, this, "addContentToIndex", "Errore in aggiunta di un contenuto");
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
            IndexWriter writer = new IndexWriter(_dir, this.getAnalyzer(), false, new MaxFieldLength(IndexWriter.DEFAULT_MAX_FIELD_LENGTH));
            writer.addDocument(document);
            writer.optimize();
            writer.close();
        } catch (IOException e) {
            throw new ApsSystemException(
                    "Errore nell'aggiunta di un documento", e);
        }
    }
    
    /**
     * Crea un oggetto Document pronto per l'indicizzazione da un oggetto Content.
     * @param entity Il contenuto dal quale ricavare il Document.
     * @return L'oggetto Document ricavato dal contenuto.
     * @throws ApsSystemException
     */
    private Document createDocument(IApsEntity entity) throws ApsSystemException {
        Document document = new Document();
        document.add(new Field(CONTENT_ID_FIELD_NAME, entity.getId(), 
        		Field.Store.YES, Field.Index.NOT_ANALYZED));
        
        document.add(new Field(CONTENT_GROUP_FIELD_NAME, entity.getMainGroup(), 
    			Field.Store.YES, Field.Index.ANALYZED));
        
        Iterator<String> iterGroups = entity.getGroups().iterator();
        while (iterGroups.hasNext()) {
        	String groupName = (String) iterGroups.next();
        	document.add(new Field(CONTENT_GROUP_FIELD_NAME, groupName, 
        			Field.Store.YES, Field.Index.ANALYZED));
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
        } catch (Exception e) {
            throw new ApsSystemException(
                    "Errore nella creazione del Document da indicizzare", e);
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
            	document.add(new Field(lang.getCode(), valueToIndex, 
            			Field.Store.NO, Field.Index.ANALYZED));
            }
            if (null != indexingType && 
            		IndexableAttributeInterface.INDEXING_TYPE_TEXT.equalsIgnoreCase(indexingType)) {
            	document.add(new Field(lang.getCode(), valueToIndex, 
            			Field.Store.YES, Field.Index.ANALYZED));
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
        IndexReader reader = null;
        try {
            reader = IndexReader.open(this._dir, false);
            reader.deleteDocuments(new Term(name, value));
            reader.close();
            IndexWriter writer = new IndexWriter(this._dir, this.getAnalyzer(), false, new MaxFieldLength(IndexWriter.DEFAULT_MAX_FIELD_LENGTH));
            writer.optimize();
            writer.close();
        } catch (IOException e) {
            throw new ApsSystemException(
                    "Errore nella cancellazione di un indice", e);
        }
    }
    
    @Override
	public void close() {
    	// nothing to do
    }
    
    private Analyzer getAnalyzer() {
        return new StandardAnalyzer(Version.LUCENE_30);
    }
    
	protected ILangManager getLangManager() {
		return _langManager;
	}
	@Override
	public void setLangManager(ILangManager langManager) {
		this._langManager = langManager;
	}
	
    private Directory _dir;
    
    private ILangManager _langManager;
    
}