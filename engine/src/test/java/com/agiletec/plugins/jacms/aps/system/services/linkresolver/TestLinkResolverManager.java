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
package com.agiletec.plugins.jacms.aps.system.services.linkresolver;

import com.agiletec.aps.BaseTestCase;
import com.agiletec.aps.system.RequestContext;
import com.agiletec.aps.system.SystemConstants;
import com.agiletec.aps.system.services.lang.Lang;
import com.agiletec.plugins.jacms.aps.system.JacmsSystemConstants;
import com.agiletec.plugins.jacms.aps.system.services.content.model.SymbolicLink;

/**
 * @author W.Ambu - M.Diana
 */
public class TestLinkResolverManager extends BaseTestCase {
	
	@Override
	public void setUp() throws Exception {
		try {
			super.setUp();
			_reqCtx = this.getRequestContext();
			Lang lang = new Lang();
			lang.setCode("it");
			lang.setDescr("italiano");
			_reqCtx.addExtraParam(SystemConstants.EXTRAPAR_CURRENT_LANG, lang);
			this.init();
		} catch (Throwable t) {
			throw new Exception(t);
		}
	}
	
	public void testResolveNoLinks(){
		String text = "";
		String resolvedText;
		resolvedText = _resolver.resolveLinks(text, null, _reqCtx);
		assertEquals(text, resolvedText);		
		text = "Qui non c'è alcun link";
		resolvedText = _resolver.resolveLinks(text, null, _reqCtx);
		assertEquals(text, resolvedText);
		text = text + SymbolicLink.SYMBOLIC_DEST_POSTFIX + text + SymbolicLink.SYMBOLIC_DEST_PREFIX + text;
		resolvedText = _resolver.resolveLinks(text, null, _reqCtx);
		assertEquals(text, resolvedText);
	}
	
	public void testResolvePageLink(){
		SymbolicLink link = new SymbolicLink();
		link.setDestinationToPage("primapagina");
		String text = "Qui c'è un link: '" + link.getSymbolicDestination() + "'; fine";		
		String expected = "Qui c'è un link: '/Entando/it/primapagina.page'; fine";
		String resolvedText = _resolver.resolveLinks(text, null, _reqCtx);
		assertEquals(expected, resolvedText);
	}
	
	public void testResolveURLLink() {
		SymbolicLink link = new SymbolicLink();
		link.setDestinationToUrl("http://www.google.it");
		String text = "Qui c'è un link: '" + link.getSymbolicDestination() + "'; fine";
		String expected = "Qui c'è un link: 'http://www.google.it'; fine";
		String resolvedText = _resolver.resolveLinks(text, null, _reqCtx);
		assertEquals(expected, resolvedText);
	}
	
	public void testResolveContentOnPageLink(){
		SymbolicLink link = new SymbolicLink();
		link.setDestinationToContentOnPage("ART1", "homepage");
		String text = "Qui c'è un link: '" + link.getSymbolicDestination() + "'; fine";
		String expected = "Qui c'è un link: '/Entando/it/homepage.page?contentId=ART1'; fine";
		String resolvedText = _resolver.resolveLinks(text, null, _reqCtx);
		assertEquals(expected, resolvedText);
	}
	
	public void testResolveContentLink(){
		SymbolicLink link = new SymbolicLink();
		link.setDestinationToContent("ART1");
		String text = "Qui c'è un link: '" + link.getSymbolicDestination() + "'; fine";
		String expected = "Qui c'è un link: '/Entando/it/homepage.page'; fine";
		String resolvedText = _resolver.resolveLinks(text, null, _reqCtx);
		assertEquals(expected, resolvedText);
	}
	
	public void testResolveWithNoise(){
		SymbolicLink link = new SymbolicLink();
		link.setDestinationToContentOnPage("ART1", "homepage");
		String text = "Trabocchetto: " + SymbolicLink.SYMBOLIC_DEST_PREFIX
			+ " - Qui c'è un link: ''" + link.getSymbolicDestination() + "'; "
			+ "altro Trabocchetto: " + SymbolicLink.SYMBOLIC_DEST_POSTFIX + " fine";
		String expected = "Trabocchetto: " + SymbolicLink.SYMBOLIC_DEST_PREFIX
			+" - Qui c'è un link: ''/Entando/it/homepage.page?contentId=ART1'; "
			+ "altro Trabocchetto: " + SymbolicLink.SYMBOLIC_DEST_POSTFIX + " fine";
		String resolvedText = _resolver.resolveLinks(text, null, _reqCtx);
		assertEquals(expected, resolvedText);
		
		text = text + text;
		expected = expected + expected;
		resolvedText = _resolver.resolveLinks(text, null, _reqCtx);
		assertEquals(expected, resolvedText);
	}
	
	public void testResolveMix(){
		SymbolicLink link = new SymbolicLink();
		link.setDestinationToContentOnPage("ART1", "homepage");
		String one = link.getSymbolicDestination();
		link.setDestinationToPage("primapagina");
		String two = link.getSymbolicDestination();
		String text = "Trabocchetto: " + SymbolicLink.SYMBOLIC_DEST_PREFIX
			+ " - Qui c'è un link: ''" + one + "'; "
			+ "altro Trabocchetto: " + SymbolicLink.SYMBOLIC_DEST_POSTFIX 
			+ " " + two	+ " fine";
		String expected = "Trabocchetto: " + SymbolicLink.SYMBOLIC_DEST_PREFIX
			+" - Qui c'è un link: ''/Entando/it/homepage.page?contentId=ART1'; "
			+ "altro Trabocchetto: " + SymbolicLink.SYMBOLIC_DEST_POSTFIX 
			+ " /Entando/it/primapagina.page fine";
		String resolvedText = _resolver.resolveLinks(text, null, _reqCtx);
		assertEquals(expected, resolvedText);
		text = text + text;
		expected = expected + expected;
		resolvedText = _resolver.resolveLinks(text, null, _reqCtx);
		assertEquals(expected, resolvedText);
	}
	
	public void testResolveContentOnProtectedPageLink() throws Throwable {
		SymbolicLink link = new SymbolicLink();
		link.setDestinationToContent("ART187");
		String text = "Qui c'è un link: '" + link.getSymbolicDestination() + "'; fine";
		String expected = "Qui c'è un link: '/Entando/it/contentview.page?contentId=ART187'; fine";
		String resolvedText = _resolver.resolveLinks(text, null, _reqCtx);
		assertEquals(expected, resolvedText);
		
		this.setUserOnSession("editorCustomers");
		text = "Qui c'è un link: '" + link.getSymbolicDestination() + "'; fine";
		expected = "Qui c'è un link: '/Entando/it/contentview.page?contentId=ART187'; fine";
		resolvedText = _resolver.resolveLinks(text, null, _reqCtx);
		assertEquals(expected, resolvedText);
		
		this.setUserOnSession("editorCoach");
		text = "Qui c'è un link: '" + link.getSymbolicDestination() + "'; fine";
		expected = "Qui c'è un link: '/Entando/it/coach_page.page'; fine";
		resolvedText = _resolver.resolveLinks(text, null, _reqCtx);
		assertEquals(expected, resolvedText);
		
		this.setUserOnSession("admin");
		text = "Qui c'è un link: '" + link.getSymbolicDestination() + "'; fine";
		expected = "Qui c'è un link: '/Entando/it/coach_page.page'; fine";
		resolvedText = _resolver.resolveLinks(text, null, _reqCtx);
		assertEquals(expected, resolvedText);
	}
	
	public void testResolveResourceLink() throws Throwable {
		SymbolicLink link = new SymbolicLink();
		link.setDestinationToResource("44");
		String text = "The link is: '" + link.getSymbolicDestination() + "'; end";
		String expected = "The link is: '/Entando/resources/cms/images/lvback_d0.jpg'; end";
		String resolvedText = _resolver.resolveLinks(text, null, _reqCtx);
		assertEquals(expected, resolvedText);
		
		link = new SymbolicLink();
		link.setDestinationToResource("82");
		text = "The second resource link is: '" + link.getSymbolicDestination() + "'; end";
		expected = "The second resource link is: '/Entando/protected/82/0/def/ref/ART122/'; end";
		resolvedText = _resolver.resolveLinks(text, "ART122", _reqCtx);
		assertEquals(expected, resolvedText);
	}
	
	private void init() throws Exception {
    	try {
    		_resolver = (ILinkResolverManager) this.getService(JacmsSystemConstants.LINK_RESOLVER_MANAGER);
    	} catch (Throwable t) {
            throw new Exception(t);
        }
    }
	
	private RequestContext _reqCtx;
	
	private ILinkResolverManager _resolver;
	
}
