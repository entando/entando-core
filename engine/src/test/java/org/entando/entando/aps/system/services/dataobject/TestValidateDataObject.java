/*
 * Copyright 2015-Present Entando Inc. (http://www.entando.com) All rights reserved.
 *
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 */
package org.entando.entando.aps.system.services.dataobject;

import java.util.List;

import com.agiletec.aps.BaseTestCase;
import com.agiletec.aps.system.SystemConstants;
import com.agiletec.aps.system.common.entity.model.FieldError;
import com.agiletec.aps.system.common.entity.model.attribute.ITextAttribute;
import com.agiletec.aps.system.common.entity.model.attribute.MonoListAttribute;
import com.agiletec.aps.system.services.group.Group;
import com.agiletec.aps.system.services.group.IGroupManager;
import org.entando.entando.aps.system.services.dataobject.model.DataObject;

/**
 * @author E.Santoboni
 */
public class TestValidateDataObject extends BaseTestCase {

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		this.init();
	}

	public void testValidate_1() throws Throwable {
		String insertedDescr = "XXX Prova Validazione XXX";
		try {
			DataObject content = this.createNewVoid("ART", insertedDescr, DataObject.STATUS_DRAFT, Group.FREE_GROUP_NAME, "admin");
			List<FieldError> errors = content.validate(this._groupManager);
			assertNotNull(errors);
			assertEquals(1, errors.size());
			FieldError error = errors.get(0);
			assertEquals("Text:it_Titolo", error.getFieldCode());//Verifica obbligatorietà attributo "Titolo"
			assertEquals(FieldError.MANDATORY, error.getErrorCode());

			String monolistAttributeName = "Autori";
			MonoListAttribute monolist = (MonoListAttribute) content.getAttribute(monolistAttributeName);
			monolist.addAttribute();
			assertEquals(1, monolist.getAttributes().size());

			errors = content.validate(this._groupManager);
			assertEquals(2, errors.size());
			error = errors.get(0);
			assertEquals("Text:it_Titolo", error.getFieldCode());//Verifica obbligatorietà attributo "Titolo"
			assertEquals(FieldError.MANDATORY, error.getErrorCode());

			error = errors.get(1);
			assertEquals("Monolist:Monotext:Autori_0", error.getFieldCode());//Verifica non valido elemento 1 in attributo lista "Autori"
			assertEquals(FieldError.INVALID, error.getErrorCode());
		} catch (Throwable t) {
			throw t;
		}
	}

	public void testValidate_2() throws Throwable {
		try {
			DataObject content = this.createNewVoid("RAH", "descr", DataObject.STATUS_DRAFT, Group.FREE_GROUP_NAME, "admin");
			ITextAttribute emailAttribute = (ITextAttribute) content.getAttribute("email");
			emailAttribute.setText("wrongEmailAddress", null);

			List<FieldError> errors = content.validate(this._groupManager);
			assertEquals(1, errors.size());
			FieldError error = errors.get(0);
			assertEquals("Monotext:email", error.getFieldCode());
			assertEquals(FieldError.INVALID_FORMAT, error.getErrorCode());
		} catch (Throwable t) {
			throw t;
		}
	}

	public void testValidate_4() throws Throwable {
		String shortTitle = "short";
		String longTitle = "Titolo che supera la lunghezza massima di cento caratteri; "
				+ "Ripeto, Titolo che supera la lunghezza massima di cento caratteri";
		try {
			DataObject content = this.createNewVoid("RAH", "descr", DataObject.STATUS_DRAFT, Group.FREE_GROUP_NAME, "admin");

			ITextAttribute textAttribute = (ITextAttribute) content.getAttribute("Titolo");
			textAttribute.setText(shortTitle, "it");

			List<FieldError> errors = content.validate(this._groupManager);
			assertEquals(1, errors.size());
			FieldError error = errors.get(0);
			assertEquals("Text:it_Titolo", error.getFieldCode());
			assertEquals(FieldError.INVALID_MIN_LENGTH, error.getErrorCode());

			textAttribute.setText(longTitle, "it");
			errors = content.validate(this._groupManager);
			assertEquals(1, errors.size());
			error = errors.get(0);
			assertEquals("Text:it_Titolo", error.getFieldCode());
			assertEquals(FieldError.INVALID_MAX_LENGTH, error.getErrorCode());

			textAttribute.setText(shortTitle, "en");
			errors = content.validate(this._groupManager);
			assertEquals(2, errors.size());
			error = errors.get(0);
			assertEquals("Text:it_Titolo", error.getFieldCode());
			assertEquals(FieldError.INVALID_MAX_LENGTH, error.getErrorCode());
			error = errors.get(1);
			assertEquals("Text:en_Titolo", error.getFieldCode());
			assertEquals(FieldError.INVALID_MIN_LENGTH, error.getErrorCode());

		} catch (Throwable t) {
			throw t;
		}
	}

	private DataObject createNewVoid(String contentType, String insertedDescr, String status, String mainGroup, String lastEditor) {
		DataObject content = this._contentManager.createContentType(contentType);
		content.setDescription(insertedDescr);
		content.setStatus(status);
		content.setMainGroup(mainGroup);
		content.setLastEditor(lastEditor);
		return content;
	}

	private void init() throws Exception {
		try {
			this._contentManager = (IContentManager) this.getService("DataObjectManager");
			this._groupManager = (IGroupManager) this.getService(SystemConstants.GROUP_MANAGER);
		} catch (Throwable t) {
			throw new Exception(t);
		}
	}

	private IContentManager _contentManager = null;
	private IGroupManager _groupManager = null;

}
