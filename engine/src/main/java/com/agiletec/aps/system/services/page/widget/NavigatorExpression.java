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

/**
 * Oggetto di utilità per la configurazione dei widget navigatore.
 * Ogni espressione in forma di stringa ha la forma:<br>
 * <code>page_spec</code><br>
 * oppure:<br>
 * <code>page_spec.operator</code><br>
 * dove <code>page_spec</code> è una funzione di selezione di una singola pagina e
 * <code>.operator</code> è un operatore che seleziona un sottoinsieme di pagine correlate alla
 * pagina cui è applicato.<br><br>
 * Gli specificatori di pagina sono:<ul> 
 * <li><code>current</code>&nbsp;(pagina corrente)</li>
 * <li><code>parent</code><br>&nbsp;(nodo di cui la pagina corrente è figlia)</li>
 * <li><code>super(n)</code>&nbsp;(nodo progenitore della pagina corrente a distanza n; 
 * super(1) equivale a parent)</li> 
 * <li><code>abs(n)</code>&nbsp;(nodo progenitore della pagina corrente a distanza n dalla
 * pagina radice)</li>  
 * <li><code>code(s)</code>&nbsp;(la pagina che ha codice s; s è una stringa senza virgolette)</li>
 * </ul><br>
 * Gli operatori sono:<ul> 
 * <li><code>.children</code>&nbsp;(tutti i nodi figli della pagina cui è applicato)</li> 
 * <li><code>.path</code>&nbsp;(tutti i nodi del percorso dalla pagina radice alla pagina 
 * cui è applicato)</li> 
 * <li><code>.subtree(n)</code>&nbsp;(sottoalbero a partire dalla pagina cui è applicato
 * e fino a gli n livelli sottostanti; subtree(0) comprende la sola pagina cui è applicato)</li>
 * </ul><br>
 * @author M.Diana - E.Santoboni
 */
public class NavigatorExpression {

	/**
	 * Costruttore base.
	 */
	public NavigatorExpression() {}

	/**
	 * Costruttore interpretatore dei una espressione.
	 * Consente l'interpretazione della struttura della singola espressione (componente 
	 * la stringa che identifica la totalità delle pagine da erogare nel menù).
	 * @param expression La espressione da interpretare.
	 */
	public NavigatorExpression(String expression) {
		String[] components = expression.split("\\.");
		this.buildPageSpecFields(components);
		this.buildOperatorsFields(components);
	}
	
	protected void buildPageSpecFields(String[] components) {
		String pageSpec = components[0].trim();
		if (pageSpec.startsWith(SPEC_CURRENT_PAGE_CODE)) {
			this.setSpecId(SPEC_CURRENT_PAGE_ID);
		} else if (pageSpec.startsWith(SPEC_PARENT_PAGE_CODE)) {
			this.setSpecId(SPEC_PARENT_PAGE_ID);
		} else {
			String param = this.extractParam(pageSpec);
			if (pageSpec.startsWith(SPEC_SUPER_CODE)) {
				this.setSpecId(SPEC_SUPER_ID);
				this.setSpecSuperLevel(Integer.parseInt(param));
			} else if (pageSpec.startsWith(SPEC_ABS_CODE)) {
				this.setSpecId(SPEC_ABS_ID);
				this.setSpecAbsLevel(Integer.parseInt(param));
			} else if (pageSpec.startsWith(SPEC_PAGE_CODE)) {
				this.setSpecId(SPEC_PAGE_ID);
				this.setSpecCode(param);
			} else {
				throw new RuntimeException("Unknown page specification:" + pageSpec);
			}
		}
	}
	
	protected void buildOperatorsFields(String[] components) {
		if (components.length > 1) {
			String operator = components[1].trim();
			if (operator.startsWith(OPERATOR_CHILDREN_CODE)) {
				this.setOperatorId(OPERATOR_CHILDREN_ID);
			} else if (operator.startsWith(OPERATOR_PATH_CODE)) {
				this.setOperatorId(OPERATOR_PATH_ID);
			} else if (operator.startsWith(OPERATOR_SUBTREE_CODE)) {
				this.setOperatorId(OPERATOR_SUBTREE_ID);
				String param = this.extractParam(operator);
				this.setOperatorSubtreeLevel(Integer.parseInt(param));
			} else {
				throw new RuntimeException("Unknown operator: " + operator);
			}
		}
	}
	
	private String extractParam(String pageSpec){
		String param = null;
		int p1 = pageSpec.indexOf('(');
		int p2 = pageSpec.indexOf(')');
		if(p1 >= 0 && p2 > p1) {
			param = pageSpec.substring(p1 + 1, p2);
		}
		return param;
	}
	
	/**
	 * Crea la stringa rappresentazione dell'espressione. 
	 */
	public String toString() {
		StringBuffer buffer = new StringBuffer();
		this.createSpecSection(buffer);
		this.createOperatorSection(buffer);
		return buffer.toString();
	}

	/**
	 * Crea la porzione di espressione relativa allo specificatore.
	 * @param buffer Il buffer iniziale.
	 */
	protected void createSpecSection(StringBuffer buffer) {
		int specId = this.getSpecId();
		switch (specId) {
		case SPEC_CURRENT_PAGE_ID:
			buffer.append(SPEC_CURRENT_PAGE_CODE);
			break;
		case SPEC_PARENT_PAGE_ID:
			buffer.append(SPEC_PARENT_PAGE_CODE);
			break;
		case NavigatorExpression.SPEC_ABS_ID:
			if (this.getSpecAbsLevel()<0) throw new RuntimeException("level 'ABS' not specified");
			buffer.append(SPEC_ABS_CODE).append("(").append(this.getSpecAbsLevel()).append(")");
			break;
		case NavigatorExpression.SPEC_SUPER_ID:
			if (this.getSpecSuperLevel()<0) throw new RuntimeException(" Level 'SUPER' not specified");
			buffer.append(SPEC_SUPER_CODE).append("(").append(this.getSpecSuperLevel()).append(")");
			break;
		case NavigatorExpression.SPEC_PAGE_ID:
			if (null == this.getSpecCode()) throw new RuntimeException("Page Code not specified");
			buffer.append(SPEC_PAGE_CODE).append("(").append(this.getSpecCode()).append(")");
			break;
		default:
			throw new RuntimeException("Specification identifier '" + specId+"' not recognized");
		}
	}
	
	/**
	 * Crea la porzione di espressione relativa all'operatore.
	 * @param buffer Il buffer iniziale.
	 */
	protected void createOperatorSection(StringBuffer buffer) {
		int operId = this.getOperatorId();
		if (operId > 0) {
			buffer.append(".");
			switch (operId) {
			case OPERATOR_CHILDREN_ID:
				buffer.append(OPERATOR_CHILDREN_CODE);
				break;
			case OPERATOR_PATH_ID:
				buffer.append(OPERATOR_PATH_CODE);
				break;
			case OPERATOR_SUBTREE_ID:
				if (this.getOperatorSubtreeLevel()<0) throw new RuntimeException("Livello Operatore 'SUBTREE' non Specificato");
				buffer.append(OPERATOR_SUBTREE_CODE).append("(").append(this.getOperatorSubtreeLevel()).append(")");
				break;
			default:
				throw new RuntimeException("Operator Identifier not recognized: id " + operId);
			}
		}
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
	public void setOperatorSubtreeLevel(int operatorSubtreeLevel) {
		this._operatorSubtreeLevel = operatorSubtreeLevel;
	}

	/**
	 * Identificativo specificatore pagina: 
	 * pagina corrente.
	 */
	public static final int SPEC_CURRENT_PAGE_ID = 1;

	public static final String SPEC_CURRENT_PAGE_CODE = "current";

	/**
	 * Identificativo specificatore pagina: 
	 * nodo di cui la pagina corrente è figlia.
	 */
	public static final int SPEC_PARENT_PAGE_ID = 2;

	public static final String SPEC_PARENT_PAGE_CODE = "parent";

	/**
	 * Identificativo specificatore pagina: 
	 * nodo progenitore della pagina corrente a distanza n.
	 */
	public static final int SPEC_SUPER_ID = 3;

	public static final String SPEC_SUPER_CODE = "super";

	/**
	 * Identificativo specificatore pagina: 
	 * nodo progenitore della pagina corrente a distanza n dalla pagina radice.
	 */
	public static final int SPEC_ABS_ID = 4;

	public static final String SPEC_ABS_CODE = "abs";

	/**
	 * Identificativo specificatore pagina: 
	 * una pagina di cui viene specificato il codice.
	 */
	public static final int SPEC_PAGE_ID = 5;

	public static final String SPEC_PAGE_CODE = "code";

	/**
	 * Identificativo operatore: 
	 * tutti i nodi figli della pagina cui è applicato.
	 */
	public static final int OPERATOR_CHILDREN_ID = 1;

	public static final String OPERATOR_CHILDREN_CODE = "children";

	/**
	 * Identificativo operatore: 
	 * tutti i nodi del percorso dalla pagina radice 
	 * alla pagina cui è applicato.
	 */
	public static final int OPERATOR_PATH_ID = 2;

	public static final String OPERATOR_PATH_CODE = "path";

	/**
	 * Identificativo operatore: 
	 * sottoalbero a partire dalla pagina cui è applicato 
	 * e fino a gli n livelli sottostanti.
	 */
	public static final int OPERATOR_SUBTREE_ID = 3;

	public static final String OPERATOR_SUBTREE_CODE = "subtree";

	private int _specId;
	private int _specSuperLevel = -1;
	private int _specAbsLevel = -1;
	private String _specCode;

	private int _operatorId = -1;
	private int _operatorSubtreeLevel = -1;

}
