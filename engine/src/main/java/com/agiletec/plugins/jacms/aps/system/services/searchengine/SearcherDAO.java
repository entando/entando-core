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

import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.aps.system.services.category.Category;
import com.agiletec.aps.system.services.group.Group;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.lucene.document.DateTools;
import org.apache.lucene.document.Document;
import org.apache.lucene.facet.params.CategoryListParams;
import org.apache.lucene.facet.params.FacetIndexingParams;
import org.apache.lucene.facet.params.FacetSearchParams;
import org.apache.lucene.facet.search.CountFacetRequest;
import org.apache.lucene.facet.search.DrillDownQuery;
import org.apache.lucene.facet.search.FacetRequest;
import org.apache.lucene.facet.search.FacetResult;
import org.apache.lucene.facet.search.FacetResultNode;
import org.apache.lucene.facet.search.FacetsAggregator;
import org.apache.lucene.facet.search.FacetsCollector;
import org.apache.lucene.facet.taxonomy.CategoryPath;
import org.apache.lucene.facet.taxonomy.TaxonomyReader;
import org.apache.lucene.facet.taxonomy.directory.DirectoryTaxonomyReader;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.BooleanClause;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.FieldComparator;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.MatchAllDocsQuery;
import org.apache.lucene.search.MultiCollector;
import org.apache.lucene.search.NumericRangeQuery;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.TermRangeQuery;
import org.apache.lucene.search.TopDocsCollector;
import org.apache.lucene.search.TopScoreDocCollector;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.store.SimpleFSDirectory;
import org.apache.lucene.util.BytesRef;
import org.entando.entando.aps.system.services.searchengine.SearchEngineFilter;

/**
 * Data Access Object dedita alle operazioni di ricerca 
 * ad uso del motore di ricerca interno.
 * @author E.Santoboni
 */
public class SearcherDAO implements ISearcherDAO {
	
	/**
	 * Inizializzazione del searcher.
	 * @param dir La cartella locale contenitore dei dati persistenti.
	 * @param taxoDir La cartella locale delle tassonomie
	 * @throws ApsSystemException In caso di errore
	 */
	@Override
	public void init(File dir, File taxoDir) throws ApsSystemException {
		this._indexDir = dir;
		this._taxoDir = taxoDir;
	}
	
	private IndexSearcher getSearcher() throws IOException {
		FSDirectory directory = new SimpleFSDirectory(_indexDir);
		IndexReader reader = DirectoryReader.open(directory);
		IndexSearcher searcher = new IndexSearcher(reader);
		return searcher;
	}
	
	private TaxonomyReader getTaxoReader() throws IOException {
		FSDirectory directory = new SimpleFSDirectory(this._taxoDir);
		TaxonomyReader taxoReader = new DirectoryTaxonomyReader(directory);
		return taxoReader;
	}
	
	private void releaseResources(IndexSearcher searcher, TaxonomyReader taxoReader) throws ApsSystemException {
		try {
			if (searcher != null) {
				searcher.getIndexReader().close();
				//searcher.close();
			}
		} catch (IOException e) {
			throw new ApsSystemException("Error closing searcher", e);
		}
		try {
			if (taxoReader != null) {
				taxoReader.close();
			}
		} catch (IOException e) {
			throw new ApsSystemException("Error closing TaxonomyReader", e);
		}
	}
	
	/*
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
	//@Override
	/*
	public List<String> searchContentsId(String langCode, String word, 
			Collection<String> allowedGroups) throws ApsSystemException {
		Properties termsProperty = new Properties();
		termsProperty.setProperty(langCode, word);
		return this.searchContentsId(termsProperty, null, allowedGroups);
	}
	*/
	@Override
	public List<String> searchContentsId(SearchEngineFilter[] filters, 
			Collection<Category> categories, Collection<String> allowedGroups) throws ApsSystemException {
		List<String> contentsId = new ArrayList<String>();
		IndexSearcher searcher = null;
		TaxonomyReader taxoReader = null;
    	try {
			taxoReader = this.getTaxoReader();
    		searcher = this.getSearcher();
    		Query query = null;
			if ((null == filters || filters.length == 0) 
					//&& (null == categories || categories.isEmpty())
					&& (allowedGroups != null && allowedGroups.contains(Group.ADMINS_GROUP_NAME))) {
				query = new MatchAllDocsQuery();
			} else {
				query = this.createQuery(filters, allowedGroups);
			}
           	int maxSearchLength = 1000;
			List<FacetRequest> facetRequests = new ArrayList<FacetRequest>();
			
			if (null != categories && !categories.isEmpty()) {
				Iterator<Category> iter = categories.iterator();
				while (iter.hasNext()) {
					Category category = iter.next();
					CategoryPath categoryPath = new CategoryPath(category.getPathArray());
					facetRequests.add(new CountFacetRequest(categoryPath, maxSearchLength));
				}
			} else {
				facetRequests.add(new CountFacetRequest(new CategoryPath("/"), maxSearchLength));
			}
			
			FacetSearchParams fsp = new FacetSearchParams(facetRequests);
			TopDocsCollector tdc = TopScoreDocCollector.create(maxSearchLength, true);
			FacetsCollector fc = FacetsCollector.create(fsp, searcher.getIndexReader(), taxoReader);
    		
			//TopDocs topDocs = searcher.search(query, null, maxSearchLength);
			searcher.search(query, MultiCollector.wrap(tdc, fc));
			for (FacetsCollector.MatchingDocs doc : fc.getMatchingDocs()) {
				System.out.println(" - " + doc.toString() + " - ");
			}
			
			for (FacetResult fres : fc.getFacetResults()) {
				FacetResultNode root = fres.getFacetResultNode();
				System.out.println(root.label + " - " + root.value);
				for (FacetResultNode cat : root.subResults) {
					System.out.println("" + cat.label.components[0] + " (" + cat.value + ")");
				}
			}
			
			//CategoryListParams clp = new CategoryListParams();
			//FacetIndexingParams fip = new FacetIndexingParams(null);
			//DrillDownQuery ddq = new DrillDownQuery(fip, query);
			
			for (ScoreDoc scoreDoc: tdc.topDocs().scoreDocs) {
				Document doc = searcher.getIndexReader().document(scoreDoc.doc);
				contentsId.add(doc.get(IIndexerDAO.CONTENT_ID_FIELD_NAME));
				System.out.println("ID " + doc.get("id") + " - score " + scoreDoc.score);
			}
			
    	} catch (IOException e) {
    		throw new ApsSystemException("Errore in estrazione " +
    				"documento in base ad indice", e);
    	} finally {
    		this.releaseResources(searcher, taxoReader);
    	}
    	return contentsId;
    }
    
	private Query createQuery(SearchEngineFilter[] filters, Collection<String> allowedGroups) {
		BooleanQuery mainQuery = new BooleanQuery();
		if (filters != null && filters.length > 0) {
			for (int i = 0; i < filters.length; i++) {
				SearchEngineFilter filter = filters[i];
				BooleanQuery fieldQuery = this.createQuery(filter);
				mainQuery.add(fieldQuery, BooleanClause.Occur.MUST);
			}
		}
		if (allowedGroups == null) {
			allowedGroups = new HashSet<String>();
		}
		if (!allowedGroups.contains(Group.ADMINS_GROUP_NAME)) {
			if (!allowedGroups.contains(Group.FREE_GROUP_NAME)) {
				allowedGroups.add(Group.FREE_GROUP_NAME);
			}
			BooleanQuery groupsQuery = new BooleanQuery();
			Iterator<String> iterGroups = allowedGroups.iterator();
			while (iterGroups.hasNext()) {
				String group = iterGroups.next();
				TermQuery groupQuery = new TermQuery(new Term(IIndexerDAO.CONTENT_GROUP_FIELD_NAME, group.toLowerCase()));
				//NOTE: search lower case....
				groupsQuery.add(groupQuery, BooleanClause.Occur.SHOULD);
			}
			mainQuery.add(groupsQuery, BooleanClause.Occur.MUST);
		}
		return mainQuery;
	}
    
	private BooleanQuery createQuery(SearchEngineFilter filter) {
		BooleanQuery fieldQuery = new BooleanQuery();
		Object value = filter.getValue();
		if (null != value) {
			if (value instanceof String) {
				SearchEngineFilter.TextSearchOption option = filter.getTextSearchOption();
				String stringValue = value.toString();
				if (null == option || !option.equals(SearchEngineFilter.TextSearchOption.EXACT)) {
					BooleanClause.Occur bc = BooleanClause.Occur.SHOULD;
					if (null != option) {
						if (option.equals(SearchEngineFilter.TextSearchOption.ALL_WORDS)) {
							bc = BooleanClause.Occur.MUST;
						} else if (option.equals(SearchEngineFilter.TextSearchOption.ANY_WORD)) {
							bc = BooleanClause.Occur.MUST_NOT;
						}
					}
					String[] values = stringValue.split("\\s+");
					for (int i = 0; i < values.length; i++) {
						TermQuery term = new TermQuery(new Term(filter.getKey(), values[i].toLowerCase()));
						//NOTE: search lower case....
						fieldQuery.add(term, bc);
					}
				} else {
					TermQuery term = new TermQuery(new Term(filter.getKey(), stringValue.toLowerCase()));
					//NOTE: search lower case....
					fieldQuery.add(term, BooleanClause.Occur.MUST);
				}
			} else if (value instanceof Date) {
				String toString = DateTools.timeToString(((Date) value).getTime(), DateTools.Resolution.MINUTE);
				TermQuery term = new TermQuery(new Term(filter.getKey(), toString));
				fieldQuery.add(term, BooleanClause.Occur.MUST);
			} else if (value instanceof Number) {
				TermQuery term = new TermQuery(new Term(filter.getKey(), value.toString()));
				fieldQuery.add(term, BooleanClause.Occur.MUST);
			}
		} else {
			if (filter.getStart() instanceof Number || filter.getEnd() instanceof Number) {
				//.............................. TODO
			} else {
				String start = null;
				String end = null;
				if (filter.getStart() instanceof Date || filter.getEnd() instanceof Date) {
					if (null != filter.getStart()) {
						start = DateTools.timeToString(((Date) filter.getStart()).getTime(), DateTools.Resolution.MINUTE);
					}
					if (null != filter.getEnd()) {
						end = DateTools.timeToString(((Date) filter.getEnd()).getTime(), DateTools.Resolution.MINUTE);
					}
				} else {
					start = (null != filter.getStart()) ? filter.getStart().toString().toLowerCase() : null;
					end = (null != filter.getEnd()) ? filter.getEnd().toString().toLowerCase() : null;
				}
				BytesRef byteStart = (null != start) ? new BytesRef(start.getBytes()) : null;
				BytesRef byteEnd = (null != end) ? new BytesRef(end.getBytes()) : null;
				TermRangeQuery range = new TermRangeQuery(filter.getKey(), byteStart, byteEnd, true, true);
				fieldQuery.add(range, BooleanClause.Occur.MUST);
			}
		}
		return fieldQuery;
	}
	
	@Override
    public void close() {
    	// nothing to do
    }
    
    private File _indexDir;
    private File _taxoDir;
	
}