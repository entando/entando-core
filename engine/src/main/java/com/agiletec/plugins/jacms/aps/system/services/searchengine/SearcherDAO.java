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

import com.agiletec.aps.system.common.tree.ITreeNode;
import org.entando.entando.aps.system.services.searchengine.FacetedContentsResult;
import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.aps.system.services.group.Group;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.lucene.document.DateTools;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexNotFoundException;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.BooleanClause;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.MatchAllDocsQuery;
import org.apache.lucene.search.PhraseQuery;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.TermRangeQuery;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.store.SimpleFSDirectory;
import org.apache.lucene.util.BytesRef;
import org.entando.entando.aps.system.services.searchengine.SearchEngineFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Data Access Object dedita alle operazioni di ricerca 
 * ad uso del motore di ricerca interno.
 * @author E.Santoboni
 */
public class SearcherDAO implements ISearcherDAO {
	
	private static final Logger _logger = LoggerFactory.getLogger(SearcherDAO.class);
	
	/**
	 * Inizializzazione del searcher.
	 * @param dir La cartella locale contenitore dei dati persistenti.
	 * @throws ApsSystemException In caso di errore
	 */
	@Override
	public void init(File dir) throws ApsSystemException {
		this._indexDir = dir;
	}
	
	private IndexSearcher getSearcher() throws IOException {
		FSDirectory directory = new SimpleFSDirectory(_indexDir);
		IndexReader reader = DirectoryReader.open(directory);
		IndexSearcher searcher = new IndexSearcher(reader);
		return searcher;
	}
	
	private void releaseResources(IndexSearcher searcher) throws ApsSystemException {
		try {
			if (searcher != null) {
				searcher.getIndexReader().close();
			}
		} catch (IOException e) {
			throw new ApsSystemException("Error closing searcher", e);
		}
	}
	
	@Override
	public FacetedContentsResult searchFacetedContents(SearchEngineFilter[] filters, 
			Collection<ITreeNode> categories, Collection<String> allowedGroups) throws ApsSystemException {
		return searchContents(filters, categories, allowedGroups, true);
	}
	
	/**
     * Ricerca una lista di identificativi di contenuto in base 
     * ai filtri immessi.
     * @param filters i filtri da applicare alla ricerca.
	 * @param categories Le categorie da applicare alla ricerca.
     * @param allowedGroups I gruppi autorizzati alla visualizzazione. Nel caso 
     * che la collezione sia nulla o vuota, la ricerca sarà effettuata su contenuti 
     * referenziati con il gruppo "Ad accesso libero". Nel caso che nella collezione 
     * sia presente il gruppo degli "Amministratori", la ricerca produrrà un'insieme 
     * di identificativi di contenuto non filtrati per gruppo.
     * @return La lista di identificativi contenuto.
     * @throws ApsSystemException
     */
	@Override
	public List<String> searchContentsId(SearchEngineFilter[] filters, 
			Collection<ITreeNode> categories, Collection<String> allowedGroups) throws ApsSystemException {
		return this.searchContents(filters, categories, allowedGroups, false).getContentsId();
    }
	
	protected FacetedContentsResult searchContents(SearchEngineFilter[] filters, 
			Collection<ITreeNode> categories, Collection<String> allowedGroups, boolean faceted) throws ApsSystemException {
		FacetedContentsResult result = new FacetedContentsResult();
		List<String> contentsId = new ArrayList<String>();
		IndexSearcher searcher = null;
    	try {
    		searcher = this.getSearcher();
    		Query query = null;
			if ((null == filters || filters.length == 0) 
					&& (null == categories || categories.isEmpty())
					&& (allowedGroups != null && allowedGroups.contains(Group.ADMINS_GROUP_NAME))) {
				query = new MatchAllDocsQuery();
			} else {
				query = this.createQuery(filters, categories, allowedGroups);
			}
			TopDocs topDocs = searcher.search(query, null, 1000);
			ScoreDoc[] scoreDocs = topDocs.scoreDocs;
			Map<String, Integer> occurrences = new HashMap<String, Integer>();
			if (scoreDocs.length > 0) {
    			for (int index=0; index<scoreDocs.length; index++) {
					Document doc = searcher.doc(scoreDocs[index].doc);
    				contentsId.add(doc.get(IIndexerDAO.CONTENT_ID_FIELD_NAME));
					if (faceted) {
						Set<String> codes = new HashSet<String>();
						String[] categoryPaths = doc.getValues(IIndexerDAO.CONTENT_CATEGORY_FIELD_NAME);
						for (int i = 0; i < categoryPaths.length; i++) {
							String categoryPath = categoryPaths[i];
							String[] paths = categoryPath.split(IIndexerDAO.CONTENT_CATEGORY_SEPARATOR);
							codes.addAll(Arrays.asList(paths));
						}
						Iterator<String> iter = codes.iterator();
						while (iter.hasNext()) {
							String code = iter.next();
							Integer value = occurrences.get(code);
							if (null == value) {
								value = 0;
							}
							occurrences.put(code, (value+1));
						}
					}
    			}
    		}
			result.setOccurrences(occurrences);
			result.setContentsId(contentsId);
    	} catch (IndexNotFoundException inf) {
			_logger.error("no index was found in the Directory", inf);
    	} catch (Throwable t) {
			_logger.error("Error extracting documents", t);
    		throw new ApsSystemException("Error extracting documents", t);
    	} finally {
    		this.releaseResources(searcher);
    	}
    	return result;
    }
	
	protected Query createQuery(SearchEngineFilter[] filters, 
			Collection<ITreeNode> categories, Collection<String> allowedGroups) {
		BooleanQuery mainQuery = new BooleanQuery();
		if (filters != null && filters.length > 0) {
			for (int i = 0; i < filters.length; i++) {
				SearchEngineFilter filter = filters[i];
				Query fieldQuery = this.createQuery(filter);
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
				TermQuery groupQuery = new TermQuery(new Term(IIndexerDAO.CONTENT_GROUP_FIELD_NAME, group));
				groupsQuery.add(groupQuery, BooleanClause.Occur.SHOULD);
			}
			mainQuery.add(groupsQuery, BooleanClause.Occur.MUST);
		}
		if (null != categories && !categories.isEmpty()) {
			BooleanQuery categoriesQuery = new BooleanQuery();
			Iterator<ITreeNode> cateIter = categories.iterator();
			while (cateIter.hasNext()) {
				ITreeNode category = cateIter.next();
				String path = category.getPath(IIndexerDAO.CONTENT_CATEGORY_SEPARATOR, false);
				TermQuery categoryQuery = new TermQuery(new Term(IIndexerDAO.CONTENT_CATEGORY_FIELD_NAME, path));
				categoriesQuery.add(categoryQuery, BooleanClause.Occur.MUST);
			}
			mainQuery.add(categoriesQuery, BooleanClause.Occur.MUST);
		}
		return mainQuery;
	}
    
	private Query createQuery(SearchEngineFilter filter) {
		BooleanQuery fieldQuery = new BooleanQuery();
		String key = filter.getKey();
		String attachmentKey = key + IIndexerDAO.ATTACHMENT_FIELD_SUFFIX;
		Object value = filter.getValue();
		if (null != value) {
			if (value instanceof String) {
				SearchEngineFilter.TextSearchOption option = filter.getTextSearchOption();
				if (null == option) {
					option = SearchEngineFilter.TextSearchOption.AT_LEAST_ONE_WORD;
				}
				String stringValue = value.toString();
				String[] values = stringValue.split("\\s+");
				if (!option.equals(SearchEngineFilter.TextSearchOption.EXACT)) {
					BooleanClause.Occur bc = BooleanClause.Occur.SHOULD;
					if (option.equals(SearchEngineFilter.TextSearchOption.ALL_WORDS)) {
						bc = BooleanClause.Occur.MUST;
					} else if (option.equals(SearchEngineFilter.TextSearchOption.ANY_WORD)) {
						bc = BooleanClause.Occur.MUST_NOT;
					}
					for (int i = 0; i < values.length; i++) {
						TermQuery term = new TermQuery(new Term(key, values[i].toLowerCase()));
						//NOTE: search lower case....
						if (filter.isIncludeAttachments()) {
							BooleanQuery compositeQuery = new BooleanQuery();
							compositeQuery.add(term, BooleanClause.Occur.SHOULD);
							TermQuery termAttachment = new TermQuery(new Term(attachmentKey, values[i].toLowerCase()));
							compositeQuery.add(termAttachment, BooleanClause.Occur.SHOULD);
							fieldQuery.add(compositeQuery, bc);
						} else {
							fieldQuery.add(term, bc);
						}
					}
				} else {
					PhraseQuery phraseQuery = new PhraseQuery();
					for (int i = 0; i < values.length; i++) {
						//NOTE: search lower case....
						phraseQuery.add(new Term(key, values[i].toLowerCase()));
					}
					if (filter.isIncludeAttachments()) {
						fieldQuery.add(phraseQuery, BooleanClause.Occur.SHOULD);
						PhraseQuery phraseQuery2 = new PhraseQuery();
						for (int i = 0; i < values.length; i++) {
							//NOTE: search lower case....
							phraseQuery2.add(new Term(attachmentKey, values[i].toLowerCase()));
						}
						fieldQuery.add(phraseQuery2, BooleanClause.Occur.SHOULD);
					} else {
						return phraseQuery;
					}
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
    
}