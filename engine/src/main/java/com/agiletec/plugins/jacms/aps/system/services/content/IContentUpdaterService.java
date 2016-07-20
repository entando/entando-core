package com.agiletec.plugins.jacms.aps.system.services.content;

import java.util.Set;

import com.agiletec.aps.system.exception.ApsSystemException;

/**
 * @author eu
 * EVOLUZIONE DEL CORE - AGGIUNTA FIRST EDITOR e funzioni aggiornamento referenze
 */
public interface IContentUpdaterService {
	
	public void reloadCategoryReferences(String categoryCode);
	
	public Set<String> getContentsId(String categoryCode) throws ApsSystemException;
	
}
