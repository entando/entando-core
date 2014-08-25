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
package com.agiletec.plugins.jacms.apsadmin.content;

import java.util.List;
import java.util.Map;

import com.agiletec.aps.system.common.entity.model.EntitySearchFilter;
import com.agiletec.aps.system.common.entity.model.attribute.BooleanAttribute;
import com.agiletec.aps.system.common.entity.model.attribute.MonoListAttribute;
import com.agiletec.aps.system.services.group.Group;
import com.agiletec.aps.system.services.page.IPage;
import com.agiletec.apsadmin.system.ApsAdminSystemConstants;
import com.agiletec.apsadmin.system.BaseAction;
import com.agiletec.plugins.jacms.aps.system.services.content.IContentManager;
import com.agiletec.plugins.jacms.aps.system.services.content.model.Content;
import com.agiletec.plugins.jacms.aps.system.services.content.model.SymbolicLink;
import com.agiletec.plugins.jacms.aps.system.services.content.model.extraAttribute.LinkAttribute;
import com.agiletec.plugins.jacms.apsadmin.content.util.AbstractBaseTestContentAction;
import com.opensymphony.xwork2.Action;
import com.opensymphony.xwork2.ActionSupport;

/**
 * @author E.Santoboni
 */
public class TestContentAction extends AbstractBaseTestContentAction {

	public void testEditForAdminUser() throws Throwable {
		this.testSuccesfullEdit("ART1", "admin");
		this.testSuccesfullEdit("RAH101", "admin");
		this.testSuccesfullEdit("EVN103", "admin");
	}

	public void testEditForCustomerUser() throws Throwable {
		String username = "editorCustomers";
		this.testFailureEdit("ART1", username);
		this.testSuccesfullEdit("RAH101", username);
		this.testFailureEdit("EVN103", username);
	}

	public void testEditForCoachUser() throws Throwable {
		String username = "editorCoach";
		this.testFailureEdit("ART1", username);//Contenuto Non autorizzato
		this.testSuccesfullEdit("RAH101", username);
		this.testSuccesfullEdit("EVN103", username);
	}

	private void testSuccesfullEdit(String contentId, String currentUserName) throws Throwable {
		Content content = this.getContentManager().loadContent(contentId, false);
		String result = this.executeEdit(contentId, currentUserName);
		assertEquals(Action.SUCCESS, result);
		try {
			String contentSessionMarker = AbstractContentAction.buildContentOnSessionMarker(content, ApsAdminSystemConstants.EDIT);
			Content currentContent = this.getContentOnEdit(contentSessionMarker);
    		assertEquals(content.getId(), currentContent.getId());
    		assertEquals(content.getTypeCode(), currentContent.getTypeCode());
    		assertEquals(content.getDescr(), currentContent.getDescr());
    		assertEquals(content.getMainGroup(), currentContent.getMainGroup());
    	} catch (Throwable t) {
            throw t;
        }
	}

	private void testFailureEdit(String contentId, String currentUserName) throws Throwable {
		String result = this.executeEdit(contentId, currentUserName);
		assertEquals(BaseAction.USER_NOT_ALLOWED, result);
	}

	public void testValidate_1() throws Throwable {
		String insertedDescr = "XXX Prova Validazione XXX";
		String contentTypeCode = "ART";
		Content prototype = this.getContentManager().createContentType(contentTypeCode);
		String contentOnSessionMarker = AbstractContentAction.buildContentOnSessionMarker(prototype, ApsAdminSystemConstants.ADD);
		try {
			String result = this.executeCreateNewVoid(contentTypeCode,
					insertedDescr, Content.STATUS_DRAFT, Group.FREE_GROUP_NAME, "admin");
			assertEquals(Action.SUCCESS, result);
			Content contentOnSession = this.getContentOnEdit(contentOnSessionMarker);
			assertNotNull(contentOnSession);
			assertEquals(insertedDescr, contentOnSession.getDescr());

			this.initContentAction("/do/jacms/Content", "save", contentOnSessionMarker);
			result = this.executeAction();
			assertEquals(Action.INPUT, result);

			Map<String, List<String>> fieldErrors = this.getAction().getFieldErrors();
			assertEquals(1, fieldErrors.size());
			List<String> titleFieldErrors = fieldErrors.get("Text:it_Titolo");
			assertNotNull(titleFieldErrors);
			assertEquals(1, titleFieldErrors.size());//Verifica obbligatorietà attributo "Titolo"

			String monolistAttributeName = "Autori";
			contentOnSession = this.getContentOnEdit(contentOnSessionMarker);
			MonoListAttribute monolist = (MonoListAttribute) contentOnSession.getAttribute(monolistAttributeName);
			assertEquals(0, monolist.getAttributes().size());
			monolist.addAttribute();
			assertEquals(1, monolist.getAttributes().size());

			this.initContentAction("/do/jacms/Content", "save", contentOnSessionMarker);
			this.addParameter("mainGroup", Group.FREE_GROUP_NAME);
			result = this.executeAction();
			assertEquals(Action.INPUT, result);

			fieldErrors = this.getAction().getFieldErrors();
			assertEquals(2, fieldErrors.size());

			titleFieldErrors = fieldErrors.get("Text:it_Titolo");
			assertNotNull(titleFieldErrors);
			assertEquals(1, titleFieldErrors.size());//Verifica obbligatorietà attributo "Titolo"

			List<String> autoriFieldErrors = fieldErrors.get("Monolist:Monotext:Autori_0");
			assertNotNull(autoriFieldErrors);
			assertEquals(1, autoriFieldErrors.size());//Verifica non valido elemento 1 in attributo lista "Autori"
		} catch (Throwable t) {
			throw t;
		} finally {
			this.removeTestContent(insertedDescr);
		}
	}

	public void testValidate_2() throws Throwable {
		String insertedDescr = "XXX Prova Validazione XXX";
		try {
			Content contentForTest = this.getContentManager().loadContent("EVN191", true);
			String contentOnSessionMarker = AbstractContentAction.buildContentOnSessionMarker(contentForTest, ApsAdminSystemConstants.EDIT);
			contentForTest.setId(null);
			contentForTest.setDescr(insertedDescr);
			contentForTest.setMainGroup("coach");//Valorizzo il gruppo proprietario

			contentForTest.getGroups().add("customers");

			//AGGIUNGO LINK SU PAGINA COACH
			MonoListAttribute linksCorrelati = (MonoListAttribute) contentForTest.getAttribute("LinkCorrelati");
			LinkAttribute linkAttribute = (LinkAttribute) linksCorrelati.addAttribute();
			linkAttribute.setText("Descrizione link", "it");
			SymbolicLink symbolicLink = new SymbolicLink();
			symbolicLink.setDestinationToContent("EVN103");//Contenuto di coach
			linkAttribute.setSymbolicLink(symbolicLink);

			this.getRequest().getSession().setAttribute(ContentActionConstants.SESSION_PARAM_NAME_CURRENT_CONTENT_PREXIX + contentOnSessionMarker, contentForTest);

			this.initContentAction("/do/jacms/Content", "save", contentOnSessionMarker);
			this.setUserOnSession("admin");
			String result = this.executeAction();
			assertEquals(Action.INPUT, result);

			ActionSupport action = this.getAction();
			assertEquals(1, action.getFieldErrors().size());
			assertEquals(1, action.getFieldErrors().get("Monolist:Link:LinkCorrelati_0").size());

		} catch (Throwable t) {
			throw t;
		} finally {
			this.removeTestContent(insertedDescr);
		}
	}

	public void testValidate_3() throws Throwable { // Description maxlength
		String contentTypeCode = "ART";
		String contentOnSessionMarker = this.extractSessionMarker(contentTypeCode, ApsAdminSystemConstants.ADD);
		String marker = "__DESCR_TEST__";
		String insertedDescr = marker;
		while (insertedDescr.length() < 300) {
			insertedDescr += marker;
		}
		try {
			String result = this.executeCreateNewVoid(contentTypeCode, "descr",
					Content.STATUS_DRAFT, Group.FREE_GROUP_NAME, "admin");
			assertEquals(Action.SUCCESS, result);
			Content contentOnSession = this.getContentOnEdit(contentOnSessionMarker);
			assertNotNull(contentOnSession);

			this.initContentAction("/do/jacms/Content", "save", contentOnSessionMarker);
			this.setUserOnSession("admin");
			this.addParameter("descr", insertedDescr);
			result = this.executeAction();
			assertEquals(Action.INPUT, result);

			ActionSupport action = this.getAction();
			Map<String, List<String>> fieldErros = action.getFieldErrors();
			assertEquals(2, fieldErros.size());

			List<String> descrFieldsErrors = fieldErros.get("descr");
			assertEquals(1, descrFieldsErrors.size());
			List<String> titleFieldsErrors = fieldErros.get("Text:it_Titolo");
			assertEquals(1, titleFieldsErrors.size());
		} catch (Throwable t) {
			throw t;
		} finally {
			this.removeTestContent(insertedDescr);
		}
	}

	/*
	 * We test, among other things the CheckBox attribute
	 */
	public void testValidate_4() throws Throwable {
		String contentTypeCode = "RAH";
		String contentOnSessionMarker = this.extractSessionMarker(contentTypeCode, ApsAdminSystemConstants.ADD);
		String insertedDescr = "XXX Prova Validazione XXX";
		try {
			String result = this.executeCreateNewVoid(contentTypeCode, "descr",
					Content.STATUS_DRAFT, Group.FREE_GROUP_NAME, "admin");
			assertEquals(Action.SUCCESS, result);
			Content contentOnSession = this.getContentOnEdit(contentOnSessionMarker);
			assertNotNull(contentOnSession);

			this.initContentAction("/do/jacms/Content", "save", contentOnSessionMarker);
			this.setUserOnSession("admin");
			this.addParameter("descr", insertedDescr);
			this.addParameter("Monotext:email", "wrongEmailAddress");
			this.addParameter("Number:Numero", "wrongNumber");
			this.addParameter("CheckBox:Checkbox", "true");
			result = this.executeAction();
			assertEquals(Action.INPUT, result);

			ActionSupport action = this.getAction();
			Map<String, List<String>> fieldErros = action.getFieldErrors();
			assertEquals(2, fieldErros.size());
			List<String> emailFieldErrors = fieldErros.get("Monotext:email");
			assertEquals(1, emailFieldErrors.size());
			List<String> numberFieldErrors = fieldErros.get("Number:Numero");
			assertEquals(1, numberFieldErrors.size());

			Content content = this.getContentOnEdit(contentOnSessionMarker);
			assertNotNull(content);
			assertTrue(content.getAttributeMap().containsKey("Checkbox"));
			BooleanAttribute attribute = (BooleanAttribute) content.getAttribute("Checkbox");
			assertNotNull(attribute);
			assertEquals("CheckBox", attribute.getType());
			assertEquals(Boolean.TRUE, attribute.getValue());

			this.initContentAction("/do/jacms/Content", "save", contentOnSessionMarker);
			this.setUserOnSession("admin");
			this.addParameter("mainGroup", Group.FREE_GROUP_NAME);
			this.addParameter("descr", insertedDescr);
			this.addParameter("Monotext:email", "wrongEmailAddress");
			this.addParameter("Number:Numero", "wrongNumber");
			// LEAVING the Checkbox parameter will result in the checkbox attribute being later evaluated as 'false'
			result = this.executeAction();
			assertEquals(Action.INPUT, result);

			content = this.getContentOnEdit(contentOnSessionMarker);
			assertNotNull(content);
			assertTrue(content.getAttributeMap().containsKey("Checkbox"));
			attribute = (BooleanAttribute) content.getAttribute("Checkbox");
			assertNotNull(attribute);
			assertEquals("CheckBox", attribute.getType());
			assertEquals(Boolean.FALSE, attribute.getValue());

		} catch (Throwable t) {
			throw t;
		} finally {
			this.removeTestContent(insertedDescr);
		}
	}

	public void testValidate_5() throws Throwable {
		String contentTypeCode = "RAH";
		String contentOnSessionMarker = this.extractSessionMarker(contentTypeCode, ApsAdminSystemConstants.ADD);
		String insertedDescr = "XXX Prova Validazione XXX";
		String shortTitle = "short";
		String longTitle = "Titolo che supera la lunghezza massima di cento caratteri; " +
			"Ripeto, Titolo che supera la lunghezza massima di cento caratteri";
		try {
			String result = this.executeCreateNewVoid(contentTypeCode, "descr",
					Content.STATUS_DRAFT, Group.FREE_GROUP_NAME, "admin");
			assertEquals(Action.SUCCESS, result);
			Content contentOnSession = this.getContentOnEdit(contentOnSessionMarker);
			assertNotNull(contentOnSession);

			this.initContentAction("/do/jacms/Content", "save", contentOnSessionMarker);
			this.setUserOnSession("admin");
			this.addParameter("descr", insertedDescr);
			this.addParameter("Text:it_Titolo", shortTitle);

			result = this.executeAction();
			assertEquals(Action.INPUT, result);

			ActionSupport action = this.getAction();
			Map<String, List<String>> fieldErros = action.getFieldErrors();

			assertEquals(1, fieldErros.size());

			List<String> titleItFieldErrors = fieldErros.get("Text:it_Titolo");
			assertEquals(1, titleItFieldErrors.size());

			this.initContentAction("/do/jacms/Content", "save", contentOnSessionMarker);
			this.setUserOnSession("admin");
			this.addParameter("mainGroup", Group.FREE_GROUP_NAME);
			this.addParameter("descr", insertedDescr);
			this.addParameter("Text:it_Titolo", longTitle);

			result = this.executeAction();
			assertEquals(Action.INPUT, result);

			action = this.getAction();
			fieldErros = action.getFieldErrors();
			assertEquals(1, fieldErros.size());
			titleItFieldErrors = fieldErros.get("Text:it_Titolo");
			assertEquals(1, titleItFieldErrors.size());

			this.initContentAction("/do/jacms/Content", "save", contentOnSessionMarker);
			this.setUserOnSession("admin");
			this.addParameter("mainGroup", Group.FREE_GROUP_NAME);
			this.addParameter("descr", insertedDescr);
			this.addParameter("Text:it_Titolo", "Right Title length");
			this.addParameter("Text:en_Titolo", longTitle);

			result = this.executeAction();
			assertEquals(Action.INPUT, result);

			action = this.getAction();
			fieldErros = action.getFieldErrors();
			assertEquals(1, fieldErros.size());
			titleItFieldErrors = fieldErros.get("Text:en_Titolo");
			assertEquals(1, titleItFieldErrors.size());

		} catch (Throwable t) {
			throw t;
		} finally {
			this.removeTestContent(insertedDescr);
		}
	}

	public void testValidate_6() throws Throwable {
		String contentId = "ART112";
		String contentOnSessionMarker = this.extractSessionMarker(contentId, ApsAdminSystemConstants.EDIT);
		try {
			this.initAction("/do/jacms/Content", "edit");
			this.setUserOnSession("admin");
			this.addParameter("contentId", contentId);
			String result = this.executeAction();
			assertEquals(Action.SUCCESS, result);
			Content contentOnEdit = this.getContentOnEdit(contentOnSessionMarker);
			assertNotNull(contentOnEdit);

			assertEquals("coach", contentOnEdit.getMainGroup());
			assertEquals(2, contentOnEdit.getGroups().size());
			assertTrue(contentOnEdit.getGroups().contains("customers"));
			assertTrue(contentOnEdit.getGroups().contains("helpdesk"));

			this.initContentAction("/do/jacms/Content", "removeGroup", contentOnSessionMarker);
			this.addParameter("extraGroupName", "customers");
			this.addParameter("descr", contentOnEdit.getDescr());
			result = this.executeAction();
			assertEquals(Action.SUCCESS, result);
			contentOnEdit = (Content) this.getRequest().getSession().getAttribute(ContentActionConstants.SESSION_PARAM_NAME_CURRENT_CONTENT_PREXIX + contentOnSessionMarker);
			assertEquals(1, contentOnEdit.getGroups().size());
		} catch (Throwable t) {
			throw t;
		}
		Content mainContent = this.getContentManager().loadContent(contentId, true);
		try {
			this.initContentAction("/do/jacms/Content", "save", contentOnSessionMarker);
			this.addParameter("descr", mainContent.getDescr());
			String result = this.executeAction();
			assertEquals(Action.INPUT, result);
			ActionSupport action = this.getAction();
			assertEquals(1, action.getFieldErrors().size());
			assertEquals(1, action.getFieldErrors().get("mainGroup").size());
		} catch (Throwable t) {
			this.getContentManager().insertOnLineContent(mainContent);
			throw t;
		}
	}

	public void testJoinRemoveCategory() throws Throwable {
		String contentId = "ART1";
		String contentOnSessionMarker = this.extractSessionMarker(contentId, ApsAdminSystemConstants.EDIT);

		this.executeEdit(contentId, "admin");
		String categoryCodeToAdd = "cat1";
		String categoryFieldName = "categoryCode";
		Content contentOnSession = this.getContentOnEdit(contentOnSessionMarker);
		assertNotNull(contentOnSession);
		assertEquals(0, contentOnSession.getCategories().size());

		this.initContentAction("/do/jacms/Content", "joinCategory", contentOnSessionMarker);
		this.addParameter("contentOnSessionMarker", contentOnSessionMarker);
		this.addParameter(categoryFieldName, categoryCodeToAdd);
		String result = this.executeAction();
		assertEquals(Action.SUCCESS, result);
		contentOnSession = this.getContentOnEdit(contentOnSessionMarker);
		assertNotNull(contentOnSession);
		assertEquals(1, contentOnSession.getCategories().size());

		//tentativo aggiunta categoria duplicata
		this.initContentAction("/do/jacms/Content", "joinCategory", contentOnSessionMarker);
		this.addParameter("contentOnSessionMarker", contentOnSessionMarker);
		this.addParameter(categoryFieldName, categoryCodeToAdd);
		result = this.executeAction();
		assertEquals(Action.SUCCESS, result);
		contentOnSession = this.getContentOnEdit(contentOnSessionMarker);
		assertNotNull(contentOnSession);
		assertEquals(1, contentOnSession.getCategories().size());

		//tentativo aggiunta categoria inesistente
		this.initContentAction("/do/jacms/Content", "joinCategory", contentOnSessionMarker);
		this.addParameter("contentOnSessionMarker", contentOnSessionMarker);
		this.addParameter(categoryFieldName, "wrongCategoryCode");
		result = this.executeAction();
		assertEquals(Action.SUCCESS, result);
		contentOnSession = this.getContentOnEdit(contentOnSessionMarker);
		assertNotNull(contentOnSession);
		assertEquals(1, contentOnSession.getCategories().size());

		this.initContentAction("/do/jacms/Content", "removeCategory", contentOnSessionMarker);
		this.addParameter("contentOnSessionMarker", contentOnSessionMarker);
		this.addParameter(categoryFieldName, categoryCodeToAdd);
		result = this.executeAction();
		assertEquals(Action.SUCCESS, result);
		contentOnSession = this.getContentOnEdit(contentOnSessionMarker);
		assertNotNull(contentOnSession);
		assertEquals(0, contentOnSession.getCategories().size());
	}

	public void testJoinRemoveGroup() throws Throwable {
		String contentId = "ART1";
		String contentOnSessionMarker = this.extractSessionMarker(contentId, ApsAdminSystemConstants.EDIT);

		this.executeEdit(contentId, "admin");
		String groupToAdd = "coach";
		String extraGroupFieldName = "extraGroupName";
		Content contentOnSession = this.getContentOnEdit(contentOnSessionMarker);
		assertNotNull(contentOnSession);
		assertEquals(0, contentOnSession.getGroups().size());

		this.initContentAction("/do/jacms/Content", "joinGroup", contentOnSessionMarker);
		this.addParameter(extraGroupFieldName, groupToAdd);
		String result = this.executeAction();
		assertEquals(Action.SUCCESS, result);
		contentOnSession = this.getContentOnEdit(contentOnSessionMarker);
		assertNotNull(contentOnSession);
		assertEquals(1, contentOnSession.getGroups().size());

		//tentativo aggiunta gruppo duplicata
		this.initContentAction("/do/jacms/Content", "joinGroup", contentOnSessionMarker);
		this.addParameter(extraGroupFieldName, groupToAdd);
		result = this.executeAction();
		assertEquals(Action.SUCCESS, result);
		contentOnSession = this.getContentOnEdit(contentOnSessionMarker);
		assertNotNull(contentOnSession);
		assertEquals(1, contentOnSession.getGroups().size());

		//tentativo aggiunta gruppo inesistente
		this.initContentAction("/do/jacms/Content", "joinGroup", contentOnSessionMarker);
		this.addParameter("contentOnSessionMarker", contentOnSessionMarker);
		this.addParameter(extraGroupFieldName, "wrongGroupCode");
		result = this.executeAction();
		assertEquals(Action.SUCCESS, result);
		contentOnSession = this.getContentOnEdit(contentOnSessionMarker);
		assertNotNull(contentOnSession);
		assertEquals(1, contentOnSession.getGroups().size());

		this.initContentAction("/do/jacms/Content", "removeGroup", contentOnSessionMarker);
		this.addParameter(extraGroupFieldName, groupToAdd);
		result = this.executeAction();
		assertEquals(Action.SUCCESS, result);
		contentOnSession = this.getContentOnEdit(contentOnSessionMarker);
		assertNotNull(contentOnSession);
		assertEquals(0, contentOnSession.getGroups().size());
	}

	public void testSaveNewContent() throws Throwable {
		String contentId = "ART1";
		Content master = this.getContentManager().loadContent(contentId, false);
		String contentOnSessionMarker = AbstractContentAction.buildContentOnSessionMarker(master, ApsAdminSystemConstants.ADD);
		master.setId(null);
		String descr = "Contenuto di prova per testSave";
		try {
			this.getRequest().getSession().setAttribute(ContentActionConstants.SESSION_PARAM_NAME_CURRENT_CONTENT_PREXIX + contentOnSessionMarker, master);
			this.initContentAction("/do/jacms/Content", "save", contentOnSessionMarker);
			this.setUserOnSession("admin");
			this.addParameter("Text:it_Titolo", "");
			this.addParameter("descr", "");
			String result = this.executeAction();
			assertEquals(Action.INPUT, result);
			Map<String, List<String>> fieldErrors = this.getAction().getFieldErrors();
			assertEquals(2, fieldErrors.size());
			assertEquals(1, fieldErrors.get("descr").size());
			assertEquals(1, fieldErrors.get("Text:it_Titolo").size());

			this.initContentAction("/do/jacms/Content", "save", contentOnSessionMarker);
			this.addParameter("Text:it_Titolo", descr);
			result = this.executeAction();
			assertEquals(Action.SUCCESS, result);
		} catch (Throwable t) {
			throw t;
		} finally {
			this.removeTestContent(descr);
		}
	}

	public void testSaveContentWithPageReference() throws Throwable {
		String contentId = "ART111";
		this.executeEdit(contentId, "admin");
		Content master = this.getContentManager().loadContent(contentId, false);
		String contentOnSessionMarker = AbstractContentAction.buildContentOnSessionMarker(master, ApsAdminSystemConstants.EDIT);
		String groupToRemove = "customers";
		try {
			Content contentOnSession = this.getContentOnEdit(contentOnSessionMarker);
			assertNotNull(contentOnSession);
			assertEquals(2, contentOnSession.getGroups().size());
			assertTrue(contentOnSession.getGroups().contains(groupToRemove));

			this.initContentAction("/do/jacms/Content", "removeGroup", contentOnSessionMarker);
			this.addParameter("extraGroupName", groupToRemove);
			String result = this.executeAction();
			assertEquals(Action.SUCCESS, result);

			contentOnSession = this.getContentOnEdit(contentOnSessionMarker);
			assertNotNull(contentOnSession);
			assertEquals(1, contentOnSession.getGroups().size());
			assertFalse(contentOnSession.getGroups().contains(groupToRemove));

			this.initContentAction("/do/jacms/Content", "save", contentOnSessionMarker);
			this.addParameter("descr", master.getDescr());
			this.addParameter("mainGroup", master.getMainGroup());
			this.addParameter("descr", master.getDescr());
			result = this.executeAction();
			assertEquals(Action.INPUT, result);

			ActionSupport action = this.getAction();
			Map<String, List<String>> fieldErrors = action.getFieldErrors();
			assertEquals(1, fieldErrors.size());
			List<String> mainGroupFieldErrors = (List<String>) fieldErrors.get("mainGroup");
			assertEquals(2, mainGroupFieldErrors.size());
		} catch (Throwable t) {
			this.getContentManager().saveContent(master);
			throw t;
		}
	}

	private void removeTestContent(String descr) throws Throwable {
		EntitySearchFilter filter1 = new EntitySearchFilter(IContentManager.CONTENT_MODIFY_DATE_FILTER_KEY, false);
		filter1.setOrder(EntitySearchFilter.DESC_ORDER);
		EntitySearchFilter filter2 = new EntitySearchFilter(IContentManager.CONTENT_DESCR_FILTER_KEY, false, descr, false);
		EntitySearchFilter[] filters = {filter1, filter2};
		List<String> contentsId = this.getContentManager().searchId(filters);
		assertNotNull(contentsId);
		if (contentsId.size() == 1) {
			String id = contentsId.get(0);
			Content extractContent = this.getContentManager().loadContent(id, false);
			this.getContentManager().deleteContent(extractContent);
		}
	}

	public void testSuspendReferencedContent() throws Throwable {
		String contentId = "ART1";
		Content master = this.getContentManager().loadContent(contentId, false);
		String contentOnSessionMarker = AbstractContentAction.buildContentOnSessionMarker(master, ApsAdminSystemConstants.EDIT);
		try {
			this.executeEdit(contentId, "admin");
			Content contentOnSession = this.getContentOnEdit(contentOnSessionMarker);
			assertNotNull(contentOnSession);
			this.initContentAction("/do/jacms/Content", "suspend", contentOnSessionMarker);
			String result = this.executeAction();
			assertEquals("references", result);
			ContentAction action = (ContentAction) this.getAction();
			List contentUtilizers = (List) action.getReferences().get("jacmsContentManagerUtilizers");
			assertEquals(2, contentUtilizers.size());
			List pegeUtilizers = (List) action.getReferences().get("PageManagerUtilizers");
			assertEquals(1, pegeUtilizers.size());
			IPage pageUtilizer = (IPage) pegeUtilizers.get(0);
			assertEquals("homepage", pageUtilizer.getCode());
		} catch (Throwable t) {
			this.getContentManager().insertOnLineContent(master);
			throw t;
		}
	}

	public void testRedirectFindImageResource() throws Throwable {
		String contentId = "ART1";
		this.executeEdit(contentId, "admin");
		String contentOnSessionMarker = this.extractSessionMarker(contentId, ApsAdminSystemConstants.EDIT);
		this.initContentAction("/do/jacms/Content", "chooseResource", contentOnSessionMarker);
		this.addParameter("attributeName", "Foto");
		this.addParameter("resourceTypeCode", "Image");
		this.addParameter("resourceLangCode", "it");
		String result = this.executeAction();
		assertEquals(Action.SUCCESS, result);
	}

	public void testFailureCopyPaste_1() throws Throwable {
		String contentId = "ART100";//Contenuto inesistente
		boolean publicVersion = false;
		String result = this.executeCopyPaste(contentId, publicVersion, "admin");
		assertEquals(BaseAction.FAILURE, result);
	}

	public void testFailureCopyPaste_2() throws Throwable {
		String contentId = "ART179";//Contenuto non pubblico
		boolean publicVersion = true;
		String result = this.executeCopyPaste(contentId, publicVersion, "admin");
		assertEquals(BaseAction.FAILURE, result);
	}

	public void testExecuteCopyPaste_1() throws Throwable {
		String contentId = "ART1";//Contenuto pubblico
		boolean publicVersion = true;
		this.executeSuccessfulCopyPaste(contentId, publicVersion, "admin");
	}

	public void testExecuteCopyPaste_2() throws Throwable {
		String contentId = "ART179";//Contenuto non pubblico
		boolean publicVersion = false;
		this.executeSuccessfulCopyPaste(contentId, publicVersion, "admin");
	}

	private void executeSuccessfulCopyPaste(String contentId, boolean publicVersion, String currentUserName) throws Throwable {
		Content content = this.getContentManager().loadContent(contentId, publicVersion);
		String contentOnSessionMarker = AbstractContentAction.buildContentOnSessionMarker(content, ApsAdminSystemConstants.PASTE);
		String result = this.executeCopyPaste(contentId, publicVersion, currentUserName);
		assertEquals(Action.SUCCESS, result);
		Content onEdit = this.getContentOnEdit(contentOnSessionMarker);
		assertNull(onEdit.getId());
    	assertEquals(content.getTypeCode(), onEdit.getTypeCode());
    	assertEquals(content.getMainGroup(), onEdit.getMainGroup());
    	assertTrue(onEdit.getDescr().indexOf(content.getDescr())>-1);
	}

	protected String executeCopyPaste(String contentId, boolean copyPublicVersion, String currentUserName) throws Throwable {
		this.initAction("/do/jacms/Content", "copyPaste");
		this.setUserOnSession(currentUserName);
		this.addParameter("contentId", contentId);
		this.addParameter("copyPublicVersion", String.valueOf(copyPublicVersion));
		return this.executeAction();
	}

}
