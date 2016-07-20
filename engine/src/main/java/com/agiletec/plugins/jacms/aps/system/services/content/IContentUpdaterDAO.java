package com.agiletec.plugins.jacms.aps.system.services.content;

import com.agiletec.plugins.jacms.aps.system.services.content.model.Content;

/**
 * @author eu
 * EVOLUZIONE DEL CORE - AGGIUNTA FIRST EDITOR e funzioni aggiornamento referenze
 */
public interface IContentUpdaterDAO {
	
	public void reloadWorkContentCategoryReferences(Content content);
	
	public void reloadPublicContentCategoryReferences(Content content);
	
}
