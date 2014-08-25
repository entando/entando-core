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
package com.agiletec.apsadmin.portal.specialwidget.navigator;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agiletec.aps.system.services.page.IPage;
import com.agiletec.aps.system.services.page.Widget;
import com.agiletec.aps.system.services.page.widget.INavigatorParser;
import com.agiletec.aps.system.services.page.widget.NavigatorExpression;
import com.agiletec.apsadmin.portal.specialwidget.SimpleWidgetConfigAction;

/**
 * Classe action per la gestione della configurazione dei widget tipo Navigatore.
 * @author E.Santoboni
 */
public class NavigatorWidgetConfigAction extends SimpleWidgetConfigAction implements INavigatorWidgetConfigAction {

	private static final Logger _logger = LoggerFactory.getLogger(NavigatorWidgetConfigAction.class);
	
	@Override
	public void validate() {
		super.validate();
		if (this.getActionErrors().size()>0 || this.getFieldErrors().size()>0) {
			super.extractInitConfig();
			this.createExpressions(this.getNavSpec());
		}
	}

	@Override
	public String save() {
		if (StringUtils.isBlank(this.getNavSpec())) {
			this.addActionError(this.getText("error.widget.configNavigator.noExpressions"));
			super.extractInitConfig();
			this.createExpressions(this.getNavSpec());
			return INPUT;
		}
		return super.save();
	}

	@Override
	protected String extractInitConfig() {
		try {
			String result = super.extractInitConfig();
			if (!result.equals(SUCCESS)) return result;
			Widget widget = this.getWidget();
			String navSpec = null;
			if (null != widget.getConfig()) {
				navSpec = widget.getConfig().getProperty("navSpec");
			}
			this.createExpressions(navSpec);
			this.setNavSpec(navSpec);
		} catch (Throwable t) {
			_logger.error("error in extractInitConfig", t);
			//ApsSystemUtils.logThrowable(t, this, "extractInitConfig");
			return FAILURE;
		}
		return SUCCESS;
	}

	@Override
	public String addExpression() {
		try {
			NavigatorExpression newExpression = this.createNewExpression();
			List<NavigatorExpression> expressions = this.getNavigatorParser().getExpressions(this.getNavSpec());
			expressions.add(newExpression);
			this.createNavigatorParams(expressions);
		} catch (Throwable t) {
			_logger.error("error in addExpression", t);
			//ApsSystemUtils.logThrowable(t, this, "addExpression");
			return FAILURE;
		}
		return SUCCESS;
	}

	/**
	 * Crea una nuova espressione in base ai parametri della richiesta corrente.
	 * @return La nuova espressione.
	 */
	protected NavigatorExpression createNewExpression() {
		NavigatorExpression navExpression = new NavigatorExpression();
		navExpression.setSpecId(this.getSpecId());
		navExpression.setSpecAbsLevel(this.getSpecAbsLevel());
		navExpression.setSpecCode(this.getSpecCode());
		navExpression.setSpecSuperLevel(this.getSpecSuperLevel());
		if (this.getOperatorId() > 0) {
			navExpression.setOperatorId(this.getOperatorId());
			navExpression.setOperatorSubtreeLevel(this.getOperatorSubtreeLevel());
		}
		return navExpression;
	}

	@Override
	public String moveExpression() {
		try {
			String navSpec = this.getNavSpec();
			List<NavigatorExpression> expressions = this.getNavigatorParser().getExpressions(navSpec);
			this.executeMoveExpression(expressions);
			this.createNavigatorParams(expressions);
		} catch (Throwable t) {
			_logger.error("error in moveExpression", t);
			//ApsSystemUtils.logThrowable(t, this, "moveExpression");
			return FAILURE;
		}
		return SUCCESS;
	}

	/**
	 * Esegue il movimento richiesto sulla lista di espressioni specificata.
	 * Il tipo di movimento richiesto viene ricavato in base ai parametri della richiesta corrente.
	 * @param expressions La lista di espressioni nel quale eseguire il movimento richiesto.
	 */
	protected void executeMoveExpression(List<NavigatorExpression> expressions) {
		int elementIndex = this.getExpressionIndex();
		if (elementIndex < 0 || elementIndex >= expressions.size()) return;
		String movement = this.getMovement();
		if (!(elementIndex==0 && movement.equals(INavigatorWidgetConfigAction.MOVEMENT_UP_CODE)) &&
				!(elementIndex == expressions.size()-1 && movement.equals(INavigatorWidgetConfigAction.MOVEMENT_DOWN_CODE))) {
			NavigatorExpression elementToMove = expressions.get(elementIndex);
			expressions.remove(elementIndex);
			if (movement.equals(INavigatorWidgetConfigAction.MOVEMENT_UP_CODE)) {
				expressions.add(elementIndex-1, elementToMove);
			}
			if (movement.equals(INavigatorWidgetConfigAction.MOVEMENT_DOWN_CODE)) {
				expressions.add(elementIndex+1, elementToMove);
			}
		}
	}

	@Override
	public String removeExpression() {
		try {
			String navSpec = this.getNavSpec();
			List<NavigatorExpression> expressions = this.getNavigatorParser().getExpressions(navSpec);
			int elementIndex = this.getExpressionIndex();
			if (elementIndex >= 0 && elementIndex < expressions.size()) {
				expressions.remove(elementIndex);
			}
			this.createNavigatorParams(expressions);
		} catch (Throwable t) {
			_logger.error("error in removeExpression", t);
			//ApsSystemUtils.logThrowable(t, this, "removeExpression");
			return FAILURE;
		}
		return SUCCESS;
	}

	/**
	 * Crea e valorizza l'action corrente in base ai parametri ricavati da una lista di espressioni.
	 * @param expressions La lista di espressioni tramite il quale valorizzare l'action corrente.
	 * @throws Throwable Il caso di errore.
	 */
	protected void createNavigatorParams(List<NavigatorExpression> expressions) throws Throwable {
		Widget prototype = this.createNewShowlet();
		//COSTRUISCE SHOWLET PROTOTIPO VUOTA E SETTA SPECIFICATORE
		String navSpec = this.getNavigatorParser().getSpec(expressions);
		prototype.getConfig().setProperty("navSpec", navSpec);
		//COSTRUISCE LE NUOVE ESPRESSIONI IN BASE AL NUOVO PARAMETRO
		this.createExpressions(expressions);
		this.setShowlet(prototype);
		//SETTA LA SHOWLET COSTRUITA
		this.setNavSpec(navSpec);
	}

	public String getNavSpec() {
		return _navSpec;
	}
	public void setNavSpec(String navSpec) {
		this._navSpec = navSpec;
	}

	protected void createExpressions(String navSpec) {
		List<NavigatorExpression> expressions = this.getNavigatorParser().getExpressions(navSpec);
		this.createExpressions(expressions);
	}

	protected void createExpressions(List<NavigatorExpression> expressions) {
		if (this.getExpressions() == null) {
			this.setExpressions(new ArrayList<NavigatorExpression>());
		}
		this.getExpressions().addAll(expressions);
	}

	/**
	 * Restituisce una lista piatta delle pagine del portale.
	 * @return La lista delle pagine del portale.
	 */
	public List<IPage> getPages() {
		IPage root = this.getPageManager().getRoot();
		List<IPage> pages = new ArrayList<IPage>();
		this.addPages(root, pages);
		return pages;
	}

	private void addPages(IPage page, List<IPage> pages) {
		pages.add(page);
		IPage[] children = page.getChildren();
		for (int i=0; i<children.length; i++) {
			this.addPages(children[i], pages);
		}
	}

	@Override
	public List<NavigatorExpression> getExpressions() {
		return _expressions;
	}
	protected void setExpressions(List<NavigatorExpression> expressions) {
		this._expressions = expressions;
	}

	protected INavigatorParser getNavigatorParser() {
		return _navigatorParser;
	}
	public void setNavigatorParser(INavigatorParser navigatorParser) {
		this._navigatorParser = navigatorParser;
	}

	public int getOperatorId() {
		return _operatorId;
	}
	public void setOperatorId(int operatorId) {
		this._operatorId = operatorId;
	}

	public int getSpecId() {
		return _specId;
	}
	public void setSpecId(int specId) {
		this._specId = specId;
	}

	public int getSpecAbsLevel() {
		return _specAbsLevel;
	}
	public void setSpecAbsLevel(int specAbsLevel) {
		this._specAbsLevel = specAbsLevel;
	}

	public String getSpecCode() {
		return _specCode;
	}
	public void setSpecCode(String specCode) {
		this._specCode = specCode;
	}

	public int getSpecSuperLevel() {
		return _specSuperLevel;
	}
	public void setSpecSuperLevel(int specSuperLevel) {
		this._specSuperLevel = specSuperLevel;
	}

	public int getOperatorSubtreeLevel() {
		return _operatorSubtreeLevel;
	}
	public void setOperatorSubtreeLevel(int operatorLevel) {
		this._operatorSubtreeLevel = operatorLevel;
	}

	public String getMovement() {
		return _movement;
	}
	public void setMovement(String movement) {
		this._movement = movement;
	}

	public int getExpressionIndex() {
		return _expressionIndex;
	}
	public void setExpressionIndex(int expressionIndex) {
		this._expressionIndex = expressionIndex;
	}

	private String _navSpec;

	private List<NavigatorExpression> _expressions;
	private INavigatorParser _navigatorParser;

	private int _specId;
	private int _specSuperLevel = -1;
	private int _specAbsLevel = -1;
	private String _specCode;

	private int _operatorId = -1;
	private int _operatorSubtreeLevel = -1;

	private String _movement;
	private int _expressionIndex = -1;

}