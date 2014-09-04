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
package com.agiletec.plugins.jacms.apsadmin.portal.specialwidget.listviewer;

/**
 * Interfaccia base per le action delegate alla creazione dei filtri 
 * per la configurazione della showlet erogatore avanzato lista contenuti.
 * @author E.Santoboni
 */
public interface IContentListFilterAction {
	
	public String newFilter();
	
	public String setFilterType();
	
	public String setFilterOption();
	
	public String saveFilter();
	
	public static final String METADATA_KEY_PREFIX = "keyFilter_";
	
	public static final int METADATA_FILTER_TYPE = 0;
	public static final int TEXT_ATTRIBUTE_FILTER_TYPE = 1;
	public static final int NUMBER_ATTRIBUTE_FILTER_TYPE = 2;
	public static final int BOOLEAN_ATTRIBUTE_FILTER_TYPE = 3;
	public static final int DATE_ATTRIBUTE_FILTER_TYPE = 4;
	
	public static final int VALUE_FILTER_OPTION = 1;
	public static final int RANGE_FILTER_OPTION = 2;
	public static final int PRESENCE_FILTER_OPTION = 3;
	public static final int ABSENCE_FILTER_OPTION = 4;
	
	public static final int NO_DATE_FILTER = 1;
	public static final int CURRENT_DATE_FILTER = 2;
	public static final int INSERTED_DATE_FILTER = 3;
	
}