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
package com.agiletec.plugins.jacms.aps.system.services.content.model.extraAttribute.util;

import java.util.ArrayList;
import java.util.List;

import com.agiletec.plugins.jacms.aps.system.services.content.model.SymbolicLink;

/**
 * Classe di utilita' per gli attributi ipertesto.
 * @author E.Santoboni
 */
public class HypertextAttributeUtil {
	
	/**
	 * Restituisce la lista di Link Simbolici relativi ai link
	 * inseriti nel testo specificato.
	 * @param text Il testo da analizzare.
	 * @return La lista di link simbolici inserito internalmente al testo.
	 */
	public static List<SymbolicLink> getSymbolicLinksOnText(String text) {
		List<SymbolicLink> links = new ArrayList<SymbolicLink>();
		if (null != text && text.length() > 0) {
			String temp = text;
			String START_HREF = "href=\"" + SymbolicLink.SYMBOLIC_DEST_PREFIX;
			String END_HREF = SymbolicLink.SYMBOLIC_DEST_POSTFIX + "\"";
			while (temp.indexOf(START_HREF) > 0) {
				int start = temp.indexOf(START_HREF);
				int end = temp.indexOf(END_HREF);
				if (start < end) {
					String symbLinkString = temp.substring(start+6, end+2);
					SymbolicLink symbLink = new SymbolicLink();
					boolean isValidLink = symbLink.setSymbolicDestination(symbLinkString);
					if (isValidLink) {
						links.add(symbLink);
					}
				}
				if (end > 0) {
					temp = temp.substring(end+2, temp.length()-1);
				} else {
					temp = temp.substring(start+6, temp.length()-1);
				}
			}
		}
		return links;
	}
	
	public static int getIndexCutPoint(String text, int percent) {
		if (text.length() < 10) return 0;
		String textToSearch = "<p>";
		String textControl = "</p>";
		int[] indexes = getIndexes(text, textToSearch);
		int[] indexesControl = getIndexes(text, textControl);
		boolean ok = areIndexesRight(indexes, indexesControl);
		if (!ok) {
			return 0;
		} else {
			if (indexes.length == 0) return 0;
			int index = 0;
			if (indexes.length == 1) {
				index = indexes[0];
			} else {
				int approxPoint = (text.length() * percent) / 100;
				index = calculate(approxPoint, indexes);
			}
			int indexSerched = index + textToSearch.length();
			return indexSerched;
		}
	}
	
	private static int[] getIndexes(String master, String textToSearch) {
		int[] indexes = new int[0];
		int lastIndex = 0;
		while (master.indexOf(textToSearch, lastIndex)>-1) {
			int index = master.indexOf(textToSearch, lastIndex);
			lastIndex = index+1;
			indexes = addIndex(indexes, index);
		}
		return indexes;
	}
	
	private static int[] addIndex(int[] indexes, int index) {
		int len = indexes.length;
		int[] newChildren = new int[len + 1];
		for(int i=0; i < len; i++){
			newChildren[i] = indexes[i];
		}
		newChildren[len] = index;
		indexes = newChildren;
		return newChildren;
	}
	
	private static boolean areIndexesRight(int[] indexes, int[] indexesControl) {
		if (indexes.length != indexesControl.length) return false;
		for(int i=0; i < indexes.length; i++){
			if (indexes[i]>= indexesControl[i] || 
					(i>0 && indexes[i]<= indexesControl[i-1])) return false;
		}
		return true;
	}
	
	private static int calculate(int approxPoint, int[] indexes) {
		int index = -1;
		for(int i=0; i < indexes.length; i++){
			int a = Math.abs(approxPoint-index);
			int b = Math.abs(approxPoint-indexes[i]);
			if (i==0 || a>b) {
				index = indexes[i];
			}
		}
		return index;
	}
	
}
