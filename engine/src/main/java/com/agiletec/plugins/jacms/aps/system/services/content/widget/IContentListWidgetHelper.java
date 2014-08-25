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
package com.agiletec.plugins.jacms.aps.system.services.content.widget;

import java.util.List;

import com.agiletec.aps.system.RequestContext;
import com.agiletec.aps.system.common.entity.helper.IEntityFilterBean;
import com.agiletec.aps.system.common.entity.model.EntitySearchFilter;
import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.plugins.jacms.aps.system.services.content.IContentManager;
import com.agiletec.plugins.jacms.aps.system.services.content.helper.IContentListFilterBean;

/**
 * Interfaccia base per l'implementazione del bean helper della showlet di erogatore lista contenuti.
 * La classe è a servizio sia delle funzioni dell'area di amministrazione che di front-end.
 * @author E.Santoboni
 */
public interface IContentListWidgetHelper extends com.agiletec.plugins.jacms.aps.system.services.content.helper.IContentListHelper {
	
	/*
	 * Ricerca la lista dei contenuti in base alla posizione della showlet nella pagina.
	 * @param listName Il nome identificativo della lista.
	 * @param reqCtx Il contesto della richiesta.
	 * @return La lista di identificativi di contenuto.
	 * @throws Throwable In caso di errore.
	 */
	//public List<String> searchInCache(String listName, RequestContext reqCtx) throws Throwable;
	
	/**
	 * Restituisce la lista di identificativi di contenuto in base ai parametri di ricerca.
	 * I parametri utilizzati per la ricerca, per ciascuno di essi vengono estratti con questo 
	 * ordine di importanza: hanno la precedenza i parametri specificati all'intrno del tag jsp, 
	 * nel caso uno di essi sia nullo esso viene ricercato nei parametri di configurazione 
	 * della showlet.
	 * @param bean Il contenitore delle informazioni base sulla interrogazione da eseguire.
	 * @param reqCtx Il contesto della richiesta.
	 * @return La lista di identificativi di contenuto in base ai parametri di ricerca.
	 * @throws Throwable In caso di errore.
	 */
	public List<String> getContentsId(IContentListTagBean bean, RequestContext reqCtx) throws Throwable;
	
	/**
	 * Restituisce l'insieme dei filtri in base al parametro di configurazione della showlet detentore dei filtri.
	 * Il parametro è nella forma di:
	 * (key=KEY;value=VALUE;attributeFilter=TRUE|FALSE;start=START;end=END;like=TRUE|FALSE)+..<OTHER_FILTERS>
	 * @param contentType Il tipo di contenuto al quale i filtri vanno applicati.
	 * @param filtersShowletParam Il parametro della showlet nella forma corretta detentore dei filtri.
	 * @param reqCtx Il contesto della richiesta.
	 * @return L'insieme dei filtri dato dall'interpretazione del parametro.
	 */
	public EntitySearchFilter[] getFilters(String contentType, String filtersShowletParam, RequestContext reqCtx);
	
	/**
	 * Costruisce e restituisce un filtro in base ai parametri specificati.
	 * Il metodo è a servizio del sottoTag ContentListFilterTag di ContentListTag.
	 * @param contentType Il Tipo di Contenuto corrispondente al filtro da costruire.
	 * @param bean Il contenitore delle informazioni sul filtro da costruire.
	 * @param reqCtx Il contesto della richiesta corrente.
	 * @return Il nuovo filtro costruito in base ai parametri specificati.
	 * @deprecated From Entando 3.0 version 3.0.1. Use getFilter(String, IEntityFilterBean, RequestContext) method
	 */
	public EntitySearchFilter getFilter(String contentType, IContentListFilterBean bean, RequestContext reqCtx);
	
	public EntitySearchFilter getFilter(String contentType, IEntityFilterBean bean, RequestContext reqCtx);
	
	/**
	 * @deprecated From Entando 3.0 version 3.0.1. Use getUserFilterOption(String, IEntityFilterBean, RequestContext) method
	 */
	public UserFilterOptionBean getUserFilterOption(String contentType, IContentListFilterBean bean, RequestContext reqCtx);
	
	public UserFilterOptionBean getUserFilterOption(String contentType, IEntityFilterBean bean, RequestContext reqCtx);
	
	/**
	 * Restituisce il parametro da inserire nella configurazione della showlet.
	 * Il parametro è nella forma di:
	 * (key=KEY;value=VALUE;attributeFilter=TRUE|FALSE;start=START;end=END;like=TRUE|FALSE)+..<OTHER_FILTERS>
	 * @param filters I filtri tramite il quale ricavare il parametro.
	 * @return Il parametro da inserire nella configurazione della showlet.
	 * @deprecated From Entando 2.0 version 2.4.1. Use getFilterParam(EntitySearchFilter[]) method
	 */
	public String getShowletParam(EntitySearchFilter[] filters);
	
	/**
	 * Return tle list of the front-end user filter options configured into showlet parameters.
	 * @param bean The container of the base informations.
	 * @param reqCtx The request context.
	 * @return The list of the filter options.
	 * @throws ApsSystemException in case of error.
	 */
	public List<UserFilterOptionBean> getConfiguredUserFilters(IContentListTagBean bean, RequestContext reqCtx) throws ApsSystemException;
	
	public static final String[] allowedMetadataFilterKeys = 
		{IContentManager.ENTITY_TYPE_CODE_FILTER_KEY, IContentManager.CONTENT_DESCR_FILTER_KEY, IContentManager.CONTENT_STATUS_FILTER_KEY, 
		IContentManager.CONTENT_CREATION_DATE_FILTER_KEY, IContentManager.CONTENT_MODIFY_DATE_FILTER_KEY, IContentManager.CONTENT_ONLINE_FILTER_KEY};
	
	public static final String[] allowedMetadataUserFilterOptionKeys = 
		{UserFilterOptionBean.KEY_CATEGORY, UserFilterOptionBean.KEY_FULLTEXT};
	
	public static final String WIDGET_PARAM_CONTENT_TYPE = "contentType";
	public static final String WIDGET_PARAM_USER_FILTERS = "userFilters";
	public static final String WIDGET_PARAM_CATEGORIES = "categories";
	public static final String WIDGET_PARAM_OR_CLAUSE_CATEGORY_FILTER = "orClauseCategoryFilter";
	public static final String WIDGET_PARAM_FILTERS = "filters";
	public static final String WIDGET_PARAM_TITLE = "title";
	public static final String WIDGET_PARAM_PAGE_LINK = "pageLink";
	public static final String WIDGET_PARAM_PAGE_LINK_DESCR = "linkDescr";
	
	/**
	 * @deprecated Use {@link #WIDGET_PARAM_CONTENT_TYPE} instead
	 */
	public static final String SHOWLET_PARAM_CONTENT_TYPE = WIDGET_PARAM_CONTENT_TYPE;
	/**
	 * @deprecated Use {@link #WIDGET_PARAM_USER_FILTERS} instead
	 */
	public static final String SHOWLET_PARAM_USER_FILTERS = WIDGET_PARAM_USER_FILTERS;
	
	@Deprecated(/** to maintain compatibility with versions prior to 2.2.0.1 */)
	public static final String SHOWLET_PARAM_CATEGORY = "category";
	/**
	 * @deprecated Use {@link #WIDGET_PARAM_CATEGORIES} instead
	 */
	public static final String SHOWLET_PARAM_CATEGORIES = WIDGET_PARAM_CATEGORIES;
	/**
	 * @deprecated Use {@link #WIDGET_PARAM_OR_CLAUSE_CATEGORY_FILTER} instead
	 */
	public static final String SHOWLET_PARAM_OR_CLAUSE_CATEGORY_FILTER = WIDGET_PARAM_OR_CLAUSE_CATEGORY_FILTER;
	/**
	 * @deprecated Use {@link #WIDGET_PARAM_FILTERS} instead
	 */
	public static final String SHOWLET_PARAM_FILTERS = WIDGET_PARAM_FILTERS;
	/**
	 * @deprecated Use {@link #WIDGET_PARAM_TITLE} instead
	 */
	public static final String SHOWLET_PARAM_TITLE = WIDGET_PARAM_TITLE;
	/**
	 * @deprecated Use {@link #WIDGET_PARAM_PAGE_LINK} instead
	 */
	public static final String SHOWLET_PARAM_PAGE_LINK = WIDGET_PARAM_PAGE_LINK;
	/**
	 * @deprecated Use {@link #WIDGET_PARAM_PAGE_LINK_DESCR} instead
	 */
	public static final String SHOWLET_PARAM_PAGE_LINK_DESCR = WIDGET_PARAM_PAGE_LINK_DESCR;
	
}