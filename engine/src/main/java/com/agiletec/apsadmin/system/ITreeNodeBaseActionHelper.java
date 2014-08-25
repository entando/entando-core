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
package com.agiletec.apsadmin.system;

import java.util.Collection;
import java.util.Set;

import com.agiletec.aps.system.common.tree.ITreeNode;
import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.aps.system.services.user.UserDetails;

/**
 * Interfaccia base delle classi helper che gestiscono le operazioni su oggetti alberi.
 * @author E.Santoboni
 */
public interface ITreeNodeBaseActionHelper {
	
	/**
	 * Costruisce il codice univoco di un nodo in base ai parametri specificato.
	 * Il metodo:
	 * 1) elimina i caratteri non compresi tra "a" e "z", tra "0" e "9".
	 * 2) taglia (se necessario) la stringa secondo la lunghezza massima immessa.
	 * 3) verifica se esistono entità con il codice ricavato (ed in tal caso appende il suffisso "_<numero>" fino a che non trova un codice univoco).
	 * @param title Il titolo del nuovo nodo.
	 * @param baseDefaultCode Un codice nodo di default.
	 * @param maxLength La lunghezza massima del codice.
	 * @return Il codice univoco univoco ricavato.
	 * @throws ApsSystemException In caso di errore.
	 */
	public String buildCode(String title, String baseDefaultCode, int maxLength) throws ApsSystemException;
	
	/**
	 * Restituisce il nodo root dell'albero abilitato all'utente specificato.
	 * @param user L'utente da cui ricavare il l'albero autorizzato.
	 * @return Il nodo root dell'albero autorizzato.
	 * @throws ApsSystemException In caso di errore.
	 * @deprecated from jAPS 2.0 version jAPS 2.1
	 */
	public ITreeNode getAllowedTreeRoot(UserDetails user) throws ApsSystemException;
	
	public ITreeNode getAllowedTreeRoot(Collection<String> groupCodes) throws ApsSystemException;
	
	/**
	 * Check and return the nodes to use to build the showable tree.
	 * @param nodeToOpen The selected node to open.
	 * @param lastOpenedNodes The last opened nodes.
	 * @param groupCodes The groups with whom check permissions.
	 * @return The new opened tree nodes to use to build the showable tree.
	 * @throws ApsSystemException In case of error.
	 */
	public Set<String> checkTargetNodes(String nodeToOpen, Set<String> lastOpenedNodes, Collection<String> groupCodes) throws ApsSystemException;
	
	/**
	 * Check and return the nodes to use to build the showable tree.
	 * @param nodeToClose The node to close
	 * @param lastOpenedNodes The last opened nodes.
	 * @param groupCodes The groups with whom check permissions.
	 * @return The new opened tree nodes to use to build the showable tree.
	 */
	public Set<String> checkTargetNodesOnClosing(String nodeToClose, Set<String> lastOpenedNodes, Collection<String> groupCodes) throws ApsSystemException;
	
	/**
	 * Return the root node of the showable tree.
	 * @param targetNodes The tree nodes to open
	 * @param fullTree The root node of full tree.
	 * @param groupCodes The groups with whom check permissions.
	 * @return The root node to use to build the showble tree.
	 * @throws ApsSystemException in case of error.
	 */
	public TreeNodeWrapper getShowableTree(Set<String> treeNodesToOpen, ITreeNode fullTree, Collection<String> groupCodes) throws ApsSystemException;
	
}
