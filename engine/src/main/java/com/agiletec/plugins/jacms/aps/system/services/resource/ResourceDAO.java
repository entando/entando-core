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
package com.agiletec.plugins.jacms.aps.system.services.resource;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agiletec.aps.system.common.AbstractSearcherDAO;
import com.agiletec.aps.system.common.FieldSearchFilter;
import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.aps.system.services.category.Category;
import com.agiletec.plugins.jacms.aps.system.services.resource.model.ResourceInterface;
import com.agiletec.plugins.jacms.aps.system.services.resource.model.ResourceRecordVO;

/**
 * Data Access Object per gli oggetti risorsa.
 * @author E.Santoboni - W.Ambu
 */
public class ResourceDAO extends AbstractSearcherDAO implements IResourceDAO {
	
	private static final Logger _logger =  LoggerFactory.getLogger(ResourceDAO.class);
	
	/**
	 * Carica una risorsa nel db. 
	 * @param resource La risorsa da caricare nel db.
	 */
	@Override
	public void addResource(ResourceInterface resource) {
		Connection conn = null;
		try {
			conn = this.getConnection();
			conn.setAutoCommit(false);
			this.executeAddResource(resource, conn);
			conn.commit();
		} catch (Throwable t) {
			this.executeRollback(conn);
			_logger.error("Error adding resource",  t);
			throw new RuntimeException("Error adding resource", t);
			//processDaoException(t, "Error adding risorsa", "addResource");
		} finally {
			closeConnection(conn);
		}
	}
	
	protected void executeAddResource(ResourceInterface resource, Connection conn) throws ApsSystemException {
		this.addResourceRecord(resource, conn);
		this.addCategoryRelationsRecord(resource, conn);
	}
    
    protected void addResourceRecord(ResourceInterface resource, Connection conn) throws ApsSystemException {
    	PreparedStatement stat = null;
        try {
        	stat = conn.prepareStatement(ADD_RESOURCE);
			stat.setString(1, resource.getId());
			stat.setString(2, resource.getType());
			stat.setString(3, resource.getDescr());
			stat.setString(4, resource.getMainGroup());
			stat.setString(5, resource.getXML());
			stat.setString(6, resource.getMasterFileName());
			if (null != resource.getCreationDate()) {
				stat.setDate(7, new java.sql.Date(resource.getCreationDate().getTime()));
			} else {
				stat.setDate(7, new java.sql.Date(new java.util.Date().getTime()));
			}
			stat.executeUpdate();
        } catch (Throwable t) {
            _logger.error("Error adding resource record",  t);
			throw new RuntimeException("Error adding resource record", t);
			//processDaoException(t, "Errore in aggiunta record risorsa", "addResourceRecord");
        } finally {
            closeDaoResources(null, stat);
        }
    }
	
	/**
	 * Aggiorna una risorsa nel database.
	 * @param resource La risorsa da aggiornare nel db.
	 */
    @Override
	public void updateResource(ResourceInterface resource) {
		Connection conn = null;
		try {
			conn = this.getConnection();
			conn.setAutoCommit(false);
			this.executeUpdateResource(resource, conn);
			conn.commit();
		} catch (Throwable t) {
			this.executeRollback(conn);
			_logger.error("Error updating resource",  t);
			throw new RuntimeException("Error updating resource", t);
			//processDaoException(t, "Error updating resource", "updateResource");
		} finally {
			closeConnection(conn);
		}
	}
	
	protected void executeUpdateResource(ResourceInterface resource, Connection conn) throws ApsSystemException {
		this.deleteRecordsById(resource.getId(), DELETE_RESOURCE_REL_RECORD, conn);
		this.updateResourceRecord(resource, conn);
		this.addCategoryRelationsRecord(resource, conn);
	}
    
    protected void updateResourceRecord(ResourceInterface resource, Connection conn) throws ApsSystemException {
    	PreparedStatement stat = null;
        try {
        	stat = conn.prepareStatement(UPDATE_RESOURCE);
			stat.setString(1, resource.getType());
			stat.setString(2, resource.getDescr());
			stat.setString(3, resource.getMainGroup());
			stat.setString(4, resource.getXML());
			stat.setString(5, resource.getMasterFileName());
			if (null != resource.getLastModified()) {
				stat.setDate(6, new java.sql.Date(resource.getLastModified().getTime()));
			} else {
				stat.setDate(6, new java.sql.Date(new java.util.Date().getTime()));
			}
			stat.setString(7, resource.getId());
			stat.executeUpdate();
        } catch (Throwable t) {
            _logger.error("Error updating resource record",  t);
			throw new RuntimeException("Error updating resource record", t);
			//processDaoException(t, "Errore in aggiornamento record risorsa", "updateResourceRecord");
        } finally {
            closeDaoResources(null, stat);
        }
    }
	
	/**
	 * Cancella una risorsa dal db.
	 * @param id L'identificativo della risorsa da cancellare.
	 */
    @Override
	public void deleteResource(String id) {
		Connection conn = null;
		try {
			conn = this.getConnection();
			conn.setAutoCommit(false);
			this.executeDeleteResource(id, conn);
			conn.commit();
		} catch (Throwable t) {
			this.executeRollback(conn);
			_logger.error("Error deleting resource {}", id,  t);
			throw new RuntimeException("Error deleting resource " + id, t);
			//processDaoException(t, "Error deleting resource", "deleteResource");
		} finally {
			this.closeConnection(conn);
		}
	}
	
	protected void executeDeleteResource(String resourceId, Connection conn) throws ApsSystemException {
		PreparedStatement stat = null;
		try {
			this.deleteRecordsById(resourceId, DELETE_RESOURCE_REL_RECORD, conn);
			this.deleteRecordsById(resourceId, DELETE_CONTENTS_REFERENCE, conn);
			stat = conn.prepareStatement(DELETE_RESOURCE);
			stat.setString(1, resourceId);
			stat.executeUpdate();
		} catch (Throwable t) {
			_logger.error("Error deleting resource {}", resourceId,  t);
			throw new RuntimeException("Error deleting resource " + resourceId, t);
			//processDaoException(t, "Error deleting resource", "executeDeleteResource");
		} finally {
			closeDaoResources(null, stat);
		}
	}
	
	/**
	 * Carica una lista di identificativi di risorse 
	 * in base al tipo, ad una parola chiave e dalla categoria della risorsa. 
	 * @param type Tipo di risorsa da cercare.
	 * @param text Testo immesso per il raffronto con la descrizione della risorsa. null o 
	 * stringa vuota nel caso non si voglia ricercare le risorse per parola chiave. 
	 * @param categoryCode Il codice della categoria delle risorse. null o 
	 * stringa vuota nel caso non si voglia ricercare le risorse per categoria.
	 * @param groupCodes I codici dei gruppi utenti consentiti tramite il quale 
	 * filtrare le risorse. Nel caso che la collezione di codici sia nulla o vuota, 
	 * non verrà eseguito la selezione per gruppi.
	 * @return La lista di identificativi di risorse.
	 */
    @Override
	public List<String> searchResourcesId(String type, String text, String categoryCode, Collection<String> groupCodes) {
    	return this.searchResourcesId(type, text, null, categoryCode, groupCodes);
	}
    
    @Override
	public List<String> searchResourcesId(String type, String text, String filename, String categoryCode, Collection<String> groupCodes) {
    	FieldSearchFilter[] filters = this.createFilters(type, text, filename, groupCodes);
		return this.searchResourcesId(filters, categoryCode, groupCodes);
	}
    
    private FieldSearchFilter[] createFilters(String type, String text, String filename, Collection<String> groupCodes) {
    	FieldSearchFilter[] filters = new FieldSearchFilter[0];
    	if (null != type && type.trim().length() > 0) {
    		FieldSearchFilter filterToAdd = new FieldSearchFilter(IResourceManager.RESOURCE_TYPE_FILTER_KEY, type, false);
    		filters = super.addFilter(filters, filterToAdd);
    	}
    	if (null != text && text.trim().length() > 0) {
    		FieldSearchFilter filterToAdd = new FieldSearchFilter(IResourceManager.RESOURCE_DESCR_FILTER_KEY, text, true);
    		filters = super.addFilter(filters, filterToAdd);
    	}
    	if (null != filename && filename.trim().length() > 0) {
    		FieldSearchFilter filterToAdd = new FieldSearchFilter(IResourceManager.RESOURCE_FILENAME_FILTER_KEY, filename, true);
    		filters = super.addFilter(filters, filterToAdd);
    	}
    	if (groupCodes != null && groupCodes.size() > 0) {
    		List<String> allowedValues = new ArrayList<String>();
    		allowedValues.addAll(groupCodes);
    		FieldSearchFilter filterToAdd = new FieldSearchFilter(IResourceManager.RESOURCE_MAIN_GROUP_FILTER_KEY, allowedValues, false);
    		filters = super.addFilter(filters, filterToAdd);
    	}
    	if (filters.length == 0) {
    		return null;
    	}
    	return filters;
    }
    
	@Override
    public List<String> searchResourcesId(FieldSearchFilter[] filters, String categoryCode, Collection<String> groupCodes) {
		Connection conn = null;
		List<String> resources = new ArrayList<String>();
		PreparedStatement stat = null;
		ResultSet res = null;
		try {
			conn = this.getConnection();
			stat = this.buildStatement(filters, categoryCode, groupCodes, conn);
			res = stat.executeQuery();
			this.flowResult(resources, filters, res);
		} catch (Throwable t) {
			_logger.error("Error loading resources id",  t);
			throw new RuntimeException("Error loading resources id", t);
			//processDaoException(t, "Error loading resources id", "searchResourcesId");
		} finally {
			closeDaoResources(res, stat, conn);
		}
		return resources;
	}
    
    private PreparedStatement buildStatement(FieldSearchFilter[] filters, String categoryCode, Collection<String> groupCodes, Connection conn) {
		String query = this.createQueryString(filters, categoryCode, groupCodes);
		PreparedStatement stat = null;
		try {
			stat = conn.prepareStatement(query);
			int index = 0;
			index = this.addMetadataFieldFilterStatementBlock(filters, index, stat);
			if (null != categoryCode && categoryCode.trim().length() > 0) {
				stat.setString(++index, categoryCode);
			}
		} catch (Throwable t) {
			_logger.error("Error while creating the statement",  t);
			throw new RuntimeException("Error while creating the statement", t);
			//processDaoException(t, "Error while creating the statement", "buildStatement");
		}
		return stat;
	}
    
    private String createQueryString(FieldSearchFilter[] filters, String categoryCode, Collection<String> groupCodes) {
		StringBuffer query = this.createBaseQueryBlock(filters, false, categoryCode);
		boolean hasAppendWhereClause = this.appendMetadataFieldFilterQueryBlocks(filters, query, false);
		if (null != categoryCode && categoryCode.trim().length() > 0) {
			hasAppendWhereClause = this.verifyWhereClauseAppend(query, hasAppendWhereClause);
			query.append("resourcerelations.refcategory = ? ");
		}
		query.append("ORDER BY resources.descr ");
		return query.toString();
	}
    
    private StringBuffer createBaseQueryBlock(FieldSearchFilter[] filters, boolean selectAll, String categoryCode) {
		StringBuffer query = super.createBaseQueryBlock(filters, selectAll);
		if (null != categoryCode && categoryCode.trim().length() > 0) {
			query.append("INNER JOIN resourcerelations ON resources.resid = resourcerelations.resid ");
		}
		return query;
	}
    
	/**
	 * Carica un record di risorse in funzione dell'idRisorsa. Questo record è 
	 * necessario per l'estrazione della risorse in oggetto tipo AbstractResource 
	 * da parte del ResourceManager. 
	 * @param id L'identificativo della risorsa.
	 * @return Il record della risorsa.
	 */
	@Override
	public ResourceRecordVO loadResourceVo(String id) {
		Connection conn = null;
		ResourceRecordVO resourceVo = null;
		PreparedStatement stat = null;
		ResultSet res = null;
		try {
			conn = this.getConnection();
			stat = conn.prepareStatement(LOAD_RESOURCE_VO);
			stat.setString(1, id);
			res = stat.executeQuery();
			if (res.next()) {
				resourceVo = new ResourceRecordVO();
				resourceVo.setId(id);
				resourceVo.setResourceType(res.getString(1));
				resourceVo.setDescr(res.getString(2));
				resourceVo.setMainGroup(res.getString(3));
				resourceVo.setXml(res.getString(4));
				resourceVo.setMasterFileName(res.getString(5));
				resourceVo.setCreationDate(res.getDate(6));
				resourceVo.setLastModified(res.getDate(7));
			}
		} catch (Throwable t) {
			_logger.error("Errore loading resource {}", id,  t);
			throw new RuntimeException("Errore loading resource" + id, t);
			//processDaoException(t, "Errore in caricamento risorsa", "loadResourceVo");
		} finally {
			closeDaoResources(res, stat, conn);
		}
		return resourceVo;
	}
	
	/**
	 * Metodo di servizio.
	 * Aggiunge un record nella tabella resourcerelations 
	 * per ogni categoria della risorsa.
	 * @param resource La risorsa del quale referenziare le categorie.
	 * @param conn La connessione con il db.
	 * @throws ApsSystemException
	 */
	protected void addCategoryRelationsRecord(ResourceInterface resource, Connection conn) 
			throws ApsSystemException {
		if (resource.getCategories().size()>0) {
			PreparedStatement stat = null;
			try {
				Set<String> codes = this.getCategoryCodes(resource);
				stat = conn.prepareStatement(ADD_RESOURCE_REL_RECORD);
				Iterator<String> codeIter = codes.iterator();
				while (codeIter.hasNext()) {
					String code = (String) codeIter.next();
					stat.setString(1, resource.getId());
					stat.setString(2, code);
					stat.addBatch();
					stat.clearParameters();
				}
				stat.executeBatch();
			} catch (Throwable t) {
				_logger.error("Error adding resourcerelations record for {}",  resource.getId(),  t);
			throw new RuntimeException("Error adding resourcerelations record for " + resource.getId(), t);
			//processDaoException(t, "Errore in aggiunta record tabella resourcerelations", "addCategoryRelationsRecord");
			} finally {
				closeDaoResources(null, stat);
			}
		}
	}
	
	/**
	 * Restituisce la lista di codici di categorie associate ad una risorsa.
	 * La risorsa viene sempre referenziata con la categoria "root" della 
	 * tipologia relativa (che corrisponde al codice della tipologia). 
	 * @param resource La risorsa da inserire o da modificare.
	 * @return Il set di codici di categorie.
	 */
	private Set<String> getCategoryCodes(ResourceInterface resource) {
		Set<String> codes = new HashSet<String>();
		Iterator<Category> categoryIter = resource.getCategories().iterator();
		while (categoryIter.hasNext()) {
			Category category = (Category) categoryIter.next();
			this.addCategoryCode(resource, category, codes);
		}
		return codes;
	}
	
	private void addCategoryCode(ResourceInterface resource, Category category, Set<String> codes) {
		if (category.getCode().equals(category.getParent().getCode())) return;
		codes.add(category.getCode());
		Category parentCategory = (Category) category.getParent();
		if (null != parentCategory) {
			this.addCategoryCode(resource, parentCategory, codes);
		}
	}
	
	protected void deleteRecordsById(String resourceId, String query, Connection conn) {
		PreparedStatement stat = null;
		try {
			stat = conn.prepareStatement(query);
			stat.setString(1, resourceId);
			stat.executeUpdate();
		} catch (Throwable t) {
			_logger.error("Error deleting resource records for resource {}", resourceId,  t);
			throw new RuntimeException("Error deleting resource records for resource " + resourceId, t);
			//processDaoException(t, "Error deleting resource records by id " + resourceId, "deleteRecordsById");
		} finally {
			closeDaoResources(null, stat);
		}
	}
	
	@Override
	protected String getMasterTableName() {
		return "resources";
	}
	
	@Override
	protected String getMasterTableIdFieldName() {
		return "resid";
	}
	
	@Override
	protected String getTableFieldName(String metadataFieldKey) {
		return metadataFieldKey;
	}
	
	@Override
	protected boolean isForceCaseInsensitiveLikeSearch() {
		return true;
	}
	
	private final String LOAD_RESOURCE_VO =
		"SELECT restype, descr, maingroup, resourcexml, masterfilename, creationdate, lastmodified FROM resources WHERE resid = ? ";
	
	private final String ADD_RESOURCE =
		"INSERT INTO resources (resid, restype, descr, maingroup, resourcexml, masterfilename, creationdate) "
			+ "VALUES ( ? , ? , ? , ? , ? , ? , ? )";
	
	private final String UPDATE_RESOURCE =
		"UPDATE resources SET restype = ? , descr = ? , maingroup = ? , resourcexml = ? , masterfilename = ? , lastmodified = ? WHERE resid = ? ";
	
	private final String DELETE_CONTENTS_REFERENCE =
		"DELETE FROM contentrelations WHERE refresource = ? ";
	
	private final String DELETE_RESOURCE =
		"DELETE FROM resources WHERE resid = ? ";
	
	private final String ADD_RESOURCE_REL_RECORD =
		"INSERT INTO resourcerelations (resid, refcategory) VALUES ( ? , ? )";
	
	private final String DELETE_RESOURCE_REL_RECORD =
		"DELETE FROM resourcerelations WHERE resid = ? ";
	
}