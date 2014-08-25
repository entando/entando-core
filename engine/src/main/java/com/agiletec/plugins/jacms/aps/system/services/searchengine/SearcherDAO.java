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
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;

import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.aps.system.services.group.Group;

/**
 * Data Access Object dedita alle operazioni di ricerca 
 * ad uso del motore di ricerca interno.
 * @author W.Ambu
 */
public class SearcherDAO implements ISearcherDAO {
	
	/**
	 * Inizializzazione del searcher.
	 * @param dir La cartella locale contenitore dei dati persistenti.
	 */
	@Override
	public void init(File dir) {
		this._indexDir = dir;
	}
	
	private IndexSearcher getSearcher() throws IOException {
		IndexReader reader = IndexReader.open(FSDirectory.open(this._indexDir), true);
		IndexSearcher searcher = new IndexSearcher(reader);
		return searcher;
	}
	
	private void releaseSearcher(IndexSearcher searcher) throws ApsSystemException {
		try {
			if (searcher != null) {
				searcher.getIndexReader().close();
				searcher.close();
			}
		} catch (IOException e) {
			throw new ApsSystemException("Errore in chiusura searcher", e);
		}
	}
	
    /**
     * Ricerca una lista di identificativi di contenuto in base 
     * al codice della lingua corrente ed alla parola immessa.
     * @param langCode Il codice della lingua corrente.
     * @param word La parola in base al quale fare la ricerca. Nel caso 
     * venissero inserite stringhe di ricerca del tipo "Venice Amsterdam" 
     * viene considerato come se fosse "Venice OR Amsterdam".
     * @param allowedGroups I gruppi autorizzati alla visualizzazione. Nel caso 
     * che la collezione sia nulla o vuota, la ricerca sarà effettuata su contenuti 
     * referenziati con il gruppo "Ad accesso libero". Nel caso che nella collezione 
     * sia presente il gruppo degli "Amministratori", la ricerca produrrà un'insieme 
     * di identificativi di contenuto non filtrati per gruppo.
     * @return La lista di identificativi contenuto.
     * @throws ApsSystemException
     */
    public List<String> searchContentsId(String langCode, 
    		String word, Collection<String> allowedGroups) throws ApsSystemException {
    	List<String> contentsId = new ArrayList<String>();
    	IndexSearcher searcher = null;
    	try {
    		searcher = this.getSearcher();
    		QueryParser parser = new QueryParser(Version.LUCENE_30, langCode, this.getAnalyzer());
    		String queryString = this.createQueryString(langCode, word, allowedGroups);
        	Query query = parser.parse(queryString);
           	int maxSearchLength = 1000;
    		TopDocs topDocs = searcher.search(query, null, maxSearchLength);
    		ScoreDoc[] scoreDoc = topDocs.scoreDocs;
    		if (scoreDoc.length > 0) {
    			for (int index=0; index<scoreDoc.length; index++) {
    				ScoreDoc sDoc = scoreDoc[index];
    				Document doc = searcher.doc(sDoc.doc);
    				contentsId.add(doc.get(IIndexerDAO.CONTENT_ID_FIELD_NAME));
    			}
    		}
    	} catch (IOException e) {
    		throw new ApsSystemException("Errore in estrazione " +
    				"documento in base ad indice", e);
    	} catch (ParseException e) {
    		throw new ApsSystemException("Errore parsing nella ricerca", e);
    	} finally {
    		this.releaseSearcher(searcher);
    	}
    	return contentsId;
    }
    
	private String createQueryString(String langCode, String word, Collection<String> allowedGroups) {
		String queryString = langCode + ":'" + word + "'";
		if (allowedGroups == null) {
			allowedGroups = new HashSet<String>();
		}
		if (!allowedGroups.contains(Group.ADMINS_GROUP_NAME)) {
			if (!allowedGroups.contains(Group.FREE_GROUP_NAME)) {
				allowedGroups.add(Group.FREE_GROUP_NAME);
			}
			queryString += " AND (";
			boolean isFirstGroup = true;
			Iterator<String> iterGroups = allowedGroups.iterator();
			while (iterGroups.hasNext()) {
				String group = iterGroups.next();
				if (!isFirstGroup) queryString += " OR ";
				queryString += IIndexerDAO.CONTENT_GROUP_FIELD_NAME + ":" + group;
				isFirstGroup = false;
			}
			queryString += ")";
		}
		return queryString;
	}
    
    private Analyzer getAnalyzer() {
        return new StandardAnalyzer(Version.LUCENE_30);
    }

    public void close() {
    	// nothing to do
    }
    
    private File _indexDir;
    
}