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
package com.agiletec.plugins.jacms.aps.system.services.content;

import java.util.List;

import com.agiletec.aps.BaseTestCase;
import com.agiletec.aps.system.SystemConstants;
import com.agiletec.aps.system.common.entity.model.FieldError;
import com.agiletec.aps.system.common.entity.model.attribute.ITextAttribute;
import com.agiletec.aps.system.common.entity.model.attribute.MonoListAttribute;
import com.agiletec.aps.system.services.group.Group;
import com.agiletec.aps.system.services.group.IGroupManager;
import com.agiletec.plugins.jacms.aps.system.JacmsSystemConstants;
import com.agiletec.plugins.jacms.aps.system.services.content.model.Content;
import com.agiletec.plugins.jacms.aps.system.services.content.model.SymbolicLink;
import com.agiletec.plugins.jacms.aps.system.services.content.model.extraAttribute.LinkAttribute;
import com.agiletec.plugins.jacms.aps.system.services.content.model.extraAttribute.util.ICmsAttributeErrorCodes;

/**
 * @author E.Santoboni
 */
public class TestValidateContent extends BaseTestCase {
	
	@Override
    protected void setUp() throws Exception {
        super.setUp();
        this.init();
    }
    
    public void testValidate_1() throws Throwable {
        String insertedDescr = "XXX Prova Validazione XXX";
        try {
            Content content = this.createNewVoid("ART", insertedDescr, Content.STATUS_DRAFT, Group.FREE_GROUP_NAME, "admin");
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
            Content content = this._contentManager.loadContent("EVN191", true);
            content.setId(null);
            content.setMainGroup(Group.FREE_GROUP_NAME);//Valorizzo il gruppo proprietario
            content.getGroups().add("customers");
            
            MonoListAttribute linksCorrelati = (MonoListAttribute) content.getAttribute("LinkCorrelati");
            LinkAttribute linkAttribute = (LinkAttribute) linksCorrelati.addAttribute();
            
            List<FieldError> errors = content.validate(this._groupManager);
            assertNotNull(errors);
            assertEquals(1, errors.size());
            FieldError error = errors.get(0);
            assertEquals("Monolist:Link:LinkCorrelati_0", error.getFieldCode());//Verifica obbligatorietà attributo "Titolo"
            assertEquals(FieldError.INVALID, error.getErrorCode());
            
            //AGGIUNGO LINK SU PAGINA COACH
            linkAttribute.setText("Descrizione link", "it");
            SymbolicLink symbolicLink = new SymbolicLink();
            symbolicLink.setDestinationToContent("EVN103");//Contenuto di coach
            linkAttribute.setSymbolicLink(symbolicLink);
            
            errors = content.validate(this._groupManager);
            assertNotNull(errors);
            assertEquals(1, errors.size());
            error = errors.get(0);
            assertEquals("Monolist:Link:LinkCorrelati_0", error.getFieldCode());
            assertEquals(ICmsAttributeErrorCodes.INVALID_CONTENT_GROUPS, error.getErrorCode());
            
        } catch (Throwable t) {
            throw t;
        }
    }
    
    public void testValidate_3() throws Throwable {
        try {
            Content content = this.createNewVoid("RAH", "descr", Content.STATUS_DRAFT, Group.FREE_GROUP_NAME, "admin");
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
            Content content = this.createNewVoid("RAH", "descr", Content.STATUS_DRAFT, Group.FREE_GROUP_NAME, "admin");
            
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
    
    private Content createNewVoid(String contentType, String insertedDescr, String status, String mainGroup, String lastEditor) {
        Content content = this._contentManager.createContentType(contentType);
        content.setDescr(insertedDescr);
        content.setStatus(status);
        content.setMainGroup(mainGroup);
        content.setLastEditor(lastEditor);
        return content;
    }
    
    private void init() throws Exception {
        try {
            this._contentManager = (IContentManager) this.getService(JacmsSystemConstants.CONTENT_MANAGER);
            this._groupManager = (IGroupManager) this.getService(SystemConstants.GROUP_MANAGER);
        } catch (Throwable t) {
            throw new Exception(t);
        }
    }
    
    private IContentManager _contentManager = null;
    private IGroupManager _groupManager = null;
    
}
