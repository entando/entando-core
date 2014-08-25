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
package com.agiletec.aps.system.services.page.widget;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agiletec.aps.system.RequestContext;
import com.agiletec.aps.system.SystemConstants;
import com.agiletec.aps.system.services.authorization.IAuthorizationManager;
import com.agiletec.aps.system.services.page.IPage;
import com.agiletec.aps.system.services.page.IPageManager;
import com.agiletec.aps.system.services.user.UserDetails;

/**
 * Classe di supporto per l'interpretazione delle espressioni che specificano sottoinsiemi
 * di pagine. Utilizzata dal tag per la generazione di navigatori; il risultato 
 * dell'interpretazione è una lista di oggetti NavigatorTarget, che wrappano
 * pagine del portale e possono essere utilizzati dai sub-tag. Le espressioni per
 * la specificazione delle pagine da selezionare possono essere assolute o relative o miste,
 * in riferimento alla pagina visualizzata (pagina corrente) e sono definite in base
 * alla struttura ad albero delle pagine del portale (ogni pagina è un nodo).<BR>
 * Le <b>espressioni</b> supportate seguono la seguente sintassi:<br>
 * <code>expr1+expr2+ ... +exprn</code><br>
 * dove ogni espressione rappresenta un insieme di pagine e il segno + è l'operatore
 * di unione di insiemi.<br>
 * Ogni espressione in forma di stringa ha la forma:<br>
 * <code>page_spec</code><br>
 * oppure:<br>
 * <code>page_spec.operator</code><br>
 * dove <code>page_spec</code> è una funzione di selezione di una singola pagina e
 * <code>.operator</code> è un operatore che seleziona un sottoinsieme di pagine correlate alla
 * pagina cui è applicato.<br><br>
 * Solo nel caso dell'operatore "Subtree" gli oggetti NavigatorTarget restituiti
 * hanno associato un valore significativo del livello nel sottoalbero; in tutti gli altri
 * casi il livello ha valore zero.
 * @author M.Diana
 */
public class NavigatorParser implements INavigatorParser {

	private static final Logger _logger = LoggerFactory.getLogger(NavigatorParser.class);
	
	@Override
	public List<NavigatorExpression> getExpressions(String spec) {
		List<NavigatorExpression> navExpressions = new ArrayList<NavigatorExpression>();
		if (null != spec && spec.length() > 0) {
			String[] expressions = spec.split("\\+");
			for (int i=0; i<expressions.length; i++) {
				String expression = expressions[i].trim();
				if (expression.length() > 0) {
					NavigatorExpression navExpression = new NavigatorExpression(expression);
					navExpressions.add(navExpression);
				}
			}
		}
		return navExpressions;
	}
	
	@Override
	public String getSpec(List<NavigatorExpression> expressions) {
		if (null == expressions || expressions.size()==0) return "";
		StringBuffer buffer = new StringBuffer();
		boolean first = true;
		Iterator<NavigatorExpression> iter = expressions.iterator();
		while (iter.hasNext()) {
			NavigatorExpression navigatorExpression = iter.next();
			if (!first) buffer.append(" ").append(EXPRESSION_SEPARATOR).append(" ");
			buffer.append(navigatorExpression.toString());
			first = false;
		}
		return buffer.toString();
	}
	
	/**
	 * Crea e restituisce una lista di oggetti NavigatorTarget, che wrappano 
	 * pagine del portale e possono essere utilizzati dai sub-tag.
	 * @param spec L'espressione usata la specificazione delle pagine 
	 * da selezionare; possono essere assolute o relative o miste.
	 * @param reqCtx Il contesto della richiesta corrente.
	 * @return La lista di oggetti NavigatorTarget.
	 */
	@Override
	public List<NavigatorTarget> parseSpec(String spec, RequestContext reqCtx) {
		IPage currentPage = (IPage) reqCtx.getExtraParam(SystemConstants.EXTRAPAR_CURRENT_PAGE);
		UserDetails currentUser = (UserDetails) reqCtx.getRequest().getSession().getAttribute(SystemConstants.SESSIONPARAM_CURRENT_USER);
		return this.parseSpec(spec, currentPage, currentUser);
	}
	
	/**
	 * Crea e restituisce una lista di oggetti NavigatorTarget, che wrappano 
	 * pagine del portale e possono essere utilizzati dai sub-tag.
	 * @param spec L'espressione usata la specificazione delle pagine 
	 * da selezionare; possono essere assolute o relative o miste.
	 * @param currentPage La pagina corrente dove il tag è inserito.
	 * @param currentUser L'utente corrente.
	 * @return La lista di oggetti NavigatorTarget.
	 */
	@Override
	public List<NavigatorTarget> parseSpec(String spec, IPage currentPage, UserDetails currentUser) {
		List<NavigatorTarget> targets = new ArrayList<NavigatorTarget>();
		if (null != spec && spec.length() > 0) {
			String[] expressions = spec.split("\\"+EXPRESSION_SEPARATOR);
			for (int i=0; i<expressions.length; i++) {
				String expression = expressions[i].trim();
				if (expression.length() > 0) {
					NavigatorExpression navExpression = new NavigatorExpression(expression);
					targets = this.parseSubSpec(navExpression, currentPage, targets, currentUser);
				}
			}
		}
		return targets;
	}
	
	private List<NavigatorTarget> parseSubSpec(NavigatorExpression navExpression, IPage page, List<NavigatorTarget> targets, UserDetails user) {
		int specId = navExpression.getSpecId();
		IPage basePage = null;
		if (specId == NavigatorExpression.SPEC_CURRENT_PAGE_ID) {
			basePage = page;
		} else if (specId == NavigatorExpression.SPEC_PARENT_PAGE_ID) {
			int limit = 0;
			do {
				basePage = page.getParent();
				limit++;
			} while((!basePage.isShowable() || !this.isUserAllowed(user, basePage)) && limit < 20);
		} else if (specId == NavigatorExpression.SPEC_SUPER_ID) {
			if (navExpression.getSpecSuperLevel() < 0) {
				throw new RuntimeException("Level 'SUPER' not specified : Page " + page.getCode());
			}
			basePage = page;
			for (int i = 1; i <= navExpression.getSpecSuperLevel(); i++) {
				basePage = basePage.getParent();
			}
			int limit = 0;
			while ((!basePage.isShowable() || !this.isUserAllowed(user, basePage)) && limit < 20) {
				basePage = basePage.getParent();
				limit++;
			}
		} else if (specId == NavigatorExpression.SPEC_ABS_ID) {
			basePage = page;
			if (navExpression.getSpecAbsLevel() < 0) {
				throw new RuntimeException("Level 'ABS' not specified : Page " + page.getCode());
			}
			int absLevel = navExpression.getSpecAbsLevel();
			List<IPage> candidates = new ArrayList<IPage>();
			int limit = 0;
			if (basePage.isShowable() && isUserAllowed(user, basePage)) {
				candidates.add(0, basePage);
			}
			while (basePage != basePage.getParent() && limit < 20) {
				basePage = basePage.getParent();
				if (basePage.isShowable() && isUserAllowed(user, basePage)) {
					candidates.add(0, basePage);
				}
				limit ++;
			}
			if (absLevel >= candidates.size()) {
				absLevel = candidates.size() - 1;
			}
			basePage = (IPage) candidates.get(absLevel);
		} else if (specId == NavigatorExpression.SPEC_PAGE_ID) {
			if (null == navExpression.getSpecCode()) {
				throw new RuntimeException("Page Code not specified : Page " + page.getCode());
			}
			String code = navExpression.getSpecCode();
			IPage basePageTemp = this.getPageManager().getPage(code);
			if (null == basePageTemp) {
				_logger.error("Invalid Page Specification (null): Code {} - Page: {}", code, page.getCode());
				return targets;
			}
			if (basePageTemp.isShowable() && this.isUserAllowed(user, basePageTemp)) {
				basePage = basePageTemp;
			}
		}
		if (null == basePage) {
			return targets;
		}
		targets = this.processBasePage(navExpression, basePage, targets, user);
		return targets;
	}
	
	private List<NavigatorTarget> processBasePage(NavigatorExpression navExpression, IPage basePage, 
			List<NavigatorTarget> targets, UserDetails user) {
		int operatorId = navExpression.getOperatorId();
		if (operatorId < 0) {
			targets.add(new NavigatorTarget(basePage, 0));
		} else {
			if (operatorId == NavigatorExpression.OPERATOR_CHILDREN_ID) {
				IPage children[] = basePage.getChildren();
				for(int i = 0; i < children.length; i++) {
					if(children[i].isShowable() && isUserAllowed(user, children[i])) {
						targets.add(new NavigatorTarget(children[i], 0));
					}
				}
			} else if (operatorId == NavigatorExpression.OPERATOR_PATH_ID) {
				IPage page = basePage;
				int index = targets.size();
				int limit = 0;
				if (page.isShowable() && this.isUserAllowed(user, page)) {
					targets.add(index, new NavigatorTarget(page, 0));
				}
				while (!page.isRoot() && limit < 20) {
					page = page.getParent();
					if(page.isShowable() && this.isUserAllowed(user, page)) {
						targets.add(index, new NavigatorTarget(page, 0));
					}
					limit++;
				}
			} else if (operatorId == NavigatorExpression.OPERATOR_SUBTREE_ID) {
				int depth = navExpression.getOperatorSubtreeLevel();
				if (depth < 0) {
					throw new RuntimeException("Operator level 'SUBTREE' not specified");
				}
				targets = this.putSubTree(basePage, 0, depth, targets, user);
			} else {
				targets = null;
			}
		}
		return targets;
	}
	
	private List<NavigatorTarget> putSubTree(IPage page, int level, int depth, List<NavigatorTarget> targets, UserDetails currentUser) {
		if (page.isShowable() && this.isUserAllowed(currentUser, page)) {
			targets.add(new NavigatorTarget(page, level));
			if (level < depth) {
				IPage[] children = page.getChildren();
				for(int i = 0; i < children.length; i++) {
					targets = this.putSubTree(children[i], level + 1, depth, targets, currentUser);
				}
			}
		}
		return targets;
	}
	
	private boolean isUserAllowed(UserDetails user, IPage page) {
		return this.getAuthManager().isAuth(user, page);
	}
	
	protected IPageManager getPageManager() {
		return _pageManager;
	}
	public void setPageManager(IPageManager pageManager) {
		this._pageManager = pageManager;
	}
	
	protected IAuthorizationManager getAuthManager() {
		return _authManager;
	}
	public void setAuthManager(IAuthorizationManager authManager) {
		this._authManager = authManager;
	}
	
	private IPageManager _pageManager;
	private IAuthorizationManager _authManager;
	
}