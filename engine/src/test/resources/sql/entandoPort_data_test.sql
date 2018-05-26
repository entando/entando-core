INSERT INTO categories (catcode, parentcode, titles) VALUES ('home', 'home', '<?xml version="1.0" encoding="UTF-8"?>
<properties>
<property key="it">Home</property>
</properties>
');
INSERT INTO categories (catcode, parentcode, titles) VALUES ('cat1', 'home', '<?xml version="1.0" encoding="UTF-8"?>
<properties>
<property key="en">Animal</property>
<property key="it">Animali</property>
</properties>

');
INSERT INTO categories (catcode, parentcode, titles) VALUES ('evento', 'home', '<?xml version="1.0" encoding="UTF-8"?>
<properties>
<property key="en">Event</property>
<property key="it">Evento</property>
</properties>

');
INSERT INTO categories (catcode, parentcode, titles) VALUES ('resource_root', 'home', '<?xml version="1.0" encoding="UTF-8"?>
<properties>
<property key="it">Root Risorse</property>
</properties>

');
INSERT INTO categories (catcode, parentcode, titles) VALUES ('Attach', 'resource_root', '<?xml version="1.0" encoding="UTF-8"?>
<properties>
<property key="it">Attach</property>
</properties>

');
INSERT INTO categories (catcode, parentcode, titles) VALUES ('Image', 'resource_root', '<?xml version="1.0" encoding="UTF-8"?>
<properties>
<property key="it">Image</property>
</properties>

');
INSERT INTO categories (catcode, parentcode, titles) VALUES ('general', 'home', '<?xml version="1.0" encoding="UTF-8"?>
<properties>
<property key="en">General</property>
<property key="it">Generale</property>
</properties>

');
INSERT INTO categories (catcode, parentcode, titles) VALUES ('general_cat1', 'general', '<?xml version="1.0" encoding="UTF-8"?>
<properties>
<property key="en">Category 1</property>
<property key="it">Categoria 1</property>
</properties>

');
INSERT INTO categories (catcode, parentcode, titles) VALUES ('general_cat2', 'general', '<?xml version="1.0" encoding="UTF-8"?>
<properties>
<property key="en">Category 2</property>
<property key="it">Categoria 2</property>
</properties>

');
INSERT INTO categories (catcode, parentcode, titles) VALUES ('resCat2', 'Image', '<?xml version="1.0" encoding="UTF-8"?>
<properties>
<property key="en">Images Resource Category 2</property>
<property key="it">Categoria Risorsa Immagine 2</property>
</properties>

');
INSERT INTO categories (catcode, parentcode, titles) VALUES ('resCat1', 'Image', '<?xml version="1.0" encoding="UTF-8"?>
<properties>
<property key="en">Images Resource Category 1</property>
<property key="it">Categoria Risorse Immagine 1</property>
</properties>

');
INSERT INTO categories (catcode, parentcode, titles) VALUES ('general_cat3', 'general', '<?xml version="1.0" encoding="UTF-8"?>
<properties>
<property key="en">Category 3</property>
<property key="it">Categoria 3</property>
</properties>

');




INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('PAGE', 'en', 'page');
INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('PAGE', 'it', 'pagina');
INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('PAGE_MODEL', 'en', 'page model');
INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('PAGE_MODEL', 'it', 'modello pagina');
INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('PAGE_TITLE', 'en', 'page title');
INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('PAGE_TITLE', 'it', 'titolo pagina');
INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('userprofile_PFL_fullname', 'it', 'fullname');
INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('userprofile_PFL_email', 'it', 'email');
INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('userprofile_PFL_birthdate', 'it', 'birthdate');
INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('userprofile_PFL_language', 'it', 'language');
INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('userprofile_PFL_boolean1', 'it', 'boolean 1');
INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('userprofile_PFL_boolean2', 'it', 'boolean 2');
INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('LABEL_WITH_PARAMS', 'it', 'Benvenuto ${name} ${surname} (${username} - ${name}.${surname})');
INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('LABEL_WITH_PARAMS', 'en', 'Welcome ${surname} ${name} (${username} - ${name}.${surname})');




INSERT INTO pagemodels (code, descr, frames, plugincode) VALUES ('home', 'Modello home page', '<frames>
	<frame pos="0"><descr>Box sinistra alto</descr></frame>
	<frame pos="1"><descr>Box sinistra basso</descr></frame>
	<frame pos="2"><descr>Box centrale 1</descr></frame>
	<frame pos="3" main="true"><descr>Box centrale 2</descr></frame>
	<frame pos="4"><descr>Box destra alto</descr></frame>
	<frame pos="5"><descr>Box destra basso</descr></frame>
</frames>', NULL);
INSERT INTO pagemodels (code, descr, frames, plugincode) VALUES ('service', 'Modello pagine di servizio', '<frames>
	<frame pos="0"><descr>Navigazione orizzontale</descr></frame>
	<frame pos="1"><descr>Lingue</descr></frame>
	<frame pos="2"><descr>Navigazione verticale sinistra</descr></frame>
	<frame pos="3" main="true"><descr>Area principale</descr></frame>
</frames>', NULL);
INSERT INTO pagemodels (code, descr, frames, plugincode) VALUES ('internal', 'Internal Page', '<frames>
	<frame pos="0">
		<descr>Choose Language</descr>
	</frame>
	<frame pos="1">
		<descr>Search Form</descr>
	</frame>
	<frame pos="2">
		<descr>Breadcrumbs</descr>
	</frame>
	<frame pos="3">
		<descr>First Column: Box 1</descr>
		<defaultWidget code="leftmenu">
			<properties>
				<property key="navSpec">code(homepage).subtree(1)</property>
			</properties>
		</defaultWidget>
	</frame>
	<frame pos="4">
		<descr>First Column: Box 2</descr>
	</frame>
	<frame pos="5" main="true">
		<descr>Main Column: Box 1</descr>
	</frame>
	<frame pos="6">
		<descr>Main Column: Box 2</descr>
	</frame>
	<frame pos="7">
		<descr>Third Column: Box 1</descr>
	</frame>
	<frame pos="8">
		<descr>Third Column: Box 2</descr>
	</frame>
</frames>', NULL);

INSERT INTO pages (code, parentcode, pos, groupcode) VALUES ('service', 'homepage', 1, 'free');
INSERT INTO pages (code, parentcode, pos, groupcode) VALUES ('primapagina', 'service', 1, 'free');
INSERT INTO pages (code, parentcode, pos, groupcode) VALUES ('notfound', 'service', 2, 'free');
INSERT INTO pages (code, parentcode, pos, groupcode) VALUES ('login', 'service', 3, 'free');
INSERT INTO pages (code, parentcode, pos, groupcode) VALUES ('homepage', 'homepage', -1, 'free');
INSERT INTO pages (code, parentcode, pos, groupcode) VALUES ('errorpage', 'service', 5, 'free');
INSERT INTO pages (code, parentcode, pos, groupcode) VALUES ('customers_page', 'homepage', 5, 'customers');
INSERT INTO pages (code, parentcode, pos, groupcode) VALUES ('coach_page', 'homepage', 4, 'coach');
INSERT INTO pages (code, parentcode, pos, groupcode) VALUES ('administrators_page', 'homepage', 6, 'administrators');
INSERT INTO pages (code, parentcode, pos, groupcode) VALUES ('customer_subpage_2', 'customers_page', 2, 'customers');
INSERT INTO pages (code, parentcode, pos, groupcode) VALUES ('pagina_12', 'pagina_1', 2, 'free');
INSERT INTO pages (code, parentcode, pos, groupcode) VALUES ('pagina_11', 'pagina_1', 1, 'free');
INSERT INTO pages (code, parentcode, pos, groupcode) VALUES ('customer_subpage_1', 'customers_page', 1, 'customers');
INSERT INTO pages (code, parentcode, pos, groupcode) VALUES ('pagina_1', 'homepage', 2, 'free');
INSERT INTO pages (code, parentcode, pos, groupcode) VALUES ('dataObjectview', 'service', 6, 'free');
INSERT INTO pages (code, parentcode, pos, groupcode) VALUES ('contentview', 'service', 4, 'free');
INSERT INTO pages (code, parentcode, pos, groupcode) VALUES ('pagina_2', 'homepage', 3, 'free');
INSERT INTO pages (code, parentcode, pos, groupcode) VALUES ('pagina_draft', 'homepage', 7, 'free');

INSERT INTO pages_metadata_online (code, titles, modelcode, showinmenu, extraconfig, updatedat) VALUES ('service', '<?xml version="1.0" encoding="UTF-8"?>
<properties>
<property key="it">Nodo pagine di servizio</property>
</properties>
', 'service', 0, NULL, '2017-02-17 13:06:24');
INSERT INTO pages_metadata_online (code, titles, modelcode, showinmenu, extraconfig, updatedat) VALUES ('primapagina', '<?xml version="1.0" encoding="UTF-8"?>
<properties>
<property key="it">Nodo pagine di servizio</property>
</properties>', 'service', 0, NULL, '2017-02-17 13:06:24');
INSERT INTO pages_metadata_online (code, titles, modelcode, showinmenu, extraconfig, updatedat) VALUES ('notfound', '<?xml version="1.0" encoding="UTF-8"?>
<properties>
<property key="it">Pagina non trovata</property>
</properties>', 'service', 1, NULL, '2017-02-17 13:06:24');
INSERT INTO pages_metadata_online (code, titles, modelcode, showinmenu, extraconfig, updatedat) VALUES ('login', '<?xml version="1.0" encoding="UTF-8"?>
<properties>
<property key="it">Pagina di login</property>
</properties>', 'service', 1, NULL, '2017-02-17 13:06:24');
INSERT INTO pages_metadata_online (code, titles, modelcode, showinmenu, extraconfig, updatedat) VALUES ('homepage', '<?xml version="1.0" encoding="UTF-8"?>
<properties>
<property key="en">Start Page</property>
<property key="it">Pagina iniziale</property>
</properties>', 'home', 1, NULL, '2017-02-17 13:06:24');
INSERT INTO pages_metadata_online (code, titles, modelcode, showinmenu, extraconfig, updatedat) VALUES ('errorpage', '<?xml version="1.0" encoding="UTF-8"?>
<properties>
<property key="it">Pagina di errore</property>
</properties>', 'service', 1, NULL, '2017-02-17 13:06:24');
INSERT INTO pages_metadata_online (code, titles, modelcode, showinmenu, extraconfig, updatedat) VALUES ('customers_page', '<?xml version="1.0" encoding="UTF-8"?>
<properties>
<property key="en">Customers Page</property>
<property key="it">Pagina gruppo Customers</property>
</properties>', 'home', 1, NULL, '2017-02-17 13:06:24');
INSERT INTO pages_metadata_online (code, titles, modelcode, showinmenu, extraconfig, updatedat) VALUES ('coach_page', '<?xml version="1.0" encoding="UTF-8"?>
<properties>
<property key="en">Coach Page</property>
<property key="it">Pagina gruppo Coach</property>
</properties>', 'home', 1, NULL, '2017-02-17 13:06:24');
INSERT INTO pages_metadata_online (code, titles, modelcode, showinmenu, extraconfig, updatedat) VALUES ('administrators_page', '<?xml version="1.0" encoding="UTF-8"?>
<properties>
<property key="en">Administrators Page</property>
<property key="it">Pagina gruppo Amministratori</property>
</properties>', 'home', 1, NULL, '2017-02-17 13:06:24');
INSERT INTO pages_metadata_online (code, titles, modelcode, showinmenu, extraconfig, updatedat) VALUES ('customer_subpage_2', '<?xml version="1.0" encoding="UTF-8"?>
<properties>
<property key="en">Customer SubPage 2</property>
<property key="it">Customer SubPage 2</property>
</properties>', 'home', 0, '<?xml version="1.0" encoding="UTF-8"?>
<config />

', '2017-02-17 13:06:24');
INSERT INTO pages_metadata_online (code, titles, modelcode, showinmenu, extraconfig, updatedat) VALUES ('pagina_12', '<?xml version="1.0" encoding="UTF-8"?>
<properties>
<property key="en">Page 1-2</property>
<property key="it">Pagina 1-2</property>
</properties>', 'home', 1, NULL, '2017-02-17 13:06:24');
INSERT INTO pages_metadata_online (code, titles, modelcode, showinmenu, extraconfig, updatedat) VALUES ('pagina_11', '<?xml version="1.0" encoding="UTF-8"?>
<properties>
<property key="en">Page 1-1</property>
<property key="it">Pagina 1-1</property>
</properties>', 'home', 1, NULL, '2017-02-17 13:06:24');
INSERT INTO pages_metadata_online (code, titles, modelcode, showinmenu, extraconfig, updatedat) VALUES ('customer_subpage_1', '<?xml version="1.0" encoding="UTF-8"?>
<properties>
<property key="en">Customer SubPage 1</property>
<property key="it">Customer SubPage 1</property>
</properties>', 'home', 0, '<?xml version="1.0" encoding="UTF-8"?>
<config>
  <useextratitles>false</useextratitles>
  <extragroups>
    <group name="coach" />
  </extragroups>
</config>

', '2017-02-17 13:06:24');
INSERT INTO pages_metadata_online (code, titles, modelcode, showinmenu, extraconfig, updatedat) VALUES ('pagina_1', '<?xml version="1.0" encoding="UTF-8"?>
<properties>
<property key="en">Page 1</property>
<property key="it">Pagina 1</property>
</properties>', 'home', 1, '<?xml version="1.0" encoding="UTF-8"?>
<config>
  <useextratitles>false</useextratitles>
</config>

', '2017-02-17 13:06:24');
INSERT INTO pages_metadata_online (code, titles, modelcode, showinmenu, extraconfig, updatedat) VALUES ('dataObjectview', '<?xml version="1.0" encoding="UTF-8"?>
<properties>
<property key="en">DataObject Publishing</property>
<property key="it">Publicazione DataObject</property>
</properties>', 'home', 1, '<?xml version="1.0" encoding="UTF-8"?>
<config>
  <useextratitles>false</useextratitles>
</config>

', '2017-02-17 13:06:24');
INSERT INTO pages_metadata_online (code, titles, modelcode, showinmenu, extraconfig, updatedat) VALUES ('contentview', '<?xml version="1.0" encoding="UTF-8"?>
<properties>
<property key="en">Content Publishing</property>
<property key="it">Publicazione Contenuto</property>
</properties>', 'home', 1, '<?xml version="1.0" encoding="UTF-8"?>
<config>
  <useextratitles>false</useextratitles>
</config>

', '2017-02-17 13:06:24');
INSERT INTO pages_metadata_online (code, titles, modelcode, showinmenu, extraconfig, updatedat) VALUES ('pagina_2', '<?xml version="1.0" encoding="UTF-8"?>
<properties>
<property key="en">Page 2</property>
<property key="it">Pagina 2</property>
</properties>', 'home', 1, '<?xml version="1.0" encoding="UTF-8"?>
<config>
  <useextratitles>false</useextratitles>
</config>

', '2017-02-17 13:06:24');

INSERT INTO pages_metadata_draft (code, titles, modelcode, showinmenu, extraconfig, updatedat) VALUES ('service', '<?xml version="1.0" encoding="UTF-8"?>
<properties>
<property key="it">Nodo pagine di servizio</property>
</properties>
', 'service', 0, NULL, '2017-02-17 13:06:24');
INSERT INTO pages_metadata_draft (code, titles, modelcode, showinmenu, extraconfig, updatedat) VALUES ('primapagina', '<?xml version="1.0" encoding="UTF-8"?>
<properties>
<property key="it">Nodo pagine di servizio</property>
</properties>', 'service', 0, NULL, '2017-02-17 13:06:24');
INSERT INTO pages_metadata_draft (code, titles, modelcode, showinmenu, extraconfig, updatedat) VALUES ('notfound', '<?xml version="1.0" encoding="UTF-8"?>
<properties>
<property key="it">Pagina non trovata</property>
</properties>', 'service', 1, NULL, '2017-02-17 13:06:24');
INSERT INTO pages_metadata_draft (code, titles, modelcode, showinmenu, extraconfig, updatedat) VALUES ('login', '<?xml version="1.0" encoding="UTF-8"?>
<properties>
<property key="it">Pagina di login</property>
</properties>', 'service', 1, NULL, '2017-02-17 13:06:24');
INSERT INTO pages_metadata_draft (code, titles, modelcode, showinmenu, extraconfig, updatedat) VALUES ('homepage', '<?xml version="1.0" encoding="UTF-8"?>
<properties>
<property key="en">Start Page</property>
<property key="it">Pagina iniziale</property>
</properties>', 'home', 1, NULL, '2017-02-17 13:06:24');
INSERT INTO pages_metadata_draft (code, titles, modelcode, showinmenu, extraconfig, updatedat) VALUES ('errorpage', '<?xml version="1.0" encoding="UTF-8"?>
<properties>
<property key="it">Pagina di errore</property>
</properties>', 'service', 1, NULL, '2017-02-17 13:06:24');
INSERT INTO pages_metadata_draft (code, titles, modelcode, showinmenu, extraconfig, updatedat) VALUES ('customers_page', '<?xml version="1.0" encoding="UTF-8"?>
<properties>
<property key="en">Customers Page</property>
<property key="it">Pagina gruppo Customers</property>
</properties>', 'home', 1, NULL, '2017-02-17 13:06:24');
INSERT INTO pages_metadata_draft (code, titles, modelcode, showinmenu, extraconfig, updatedat) VALUES ('coach_page', '<?xml version="1.0" encoding="UTF-8"?>
<properties>
<property key="en">Coach Page</property>
<property key="it">Pagina gruppo Coach</property>
</properties>', 'home', 1, NULL, '2017-02-17 13:06:24');
INSERT INTO pages_metadata_draft (code, titles, modelcode, showinmenu, extraconfig, updatedat) VALUES ('administrators_page', '<?xml version="1.0" encoding="UTF-8"?>
<properties>
<property key="en">Administrators Page</property>
<property key="it">Pagina gruppo Amministratori</property>
</properties>', 'home', 1, NULL, '2017-02-17 13:06:24');
INSERT INTO pages_metadata_draft (code, titles, modelcode, showinmenu, extraconfig, updatedat) VALUES ('customer_subpage_2', '<?xml version="1.0" encoding="UTF-8"?>
<properties>
<property key="en">Customer SubPage 2</property>
<property key="it">Customer SubPage 2</property>
</properties>', 'home', 0, '<?xml version="1.0" encoding="UTF-8"?>
<config />

', '2017-02-17 13:06:24');
INSERT INTO pages_metadata_draft (code, titles, modelcode, showinmenu, extraconfig, updatedat) VALUES ('pagina_12', '<?xml version="1.0" encoding="UTF-8"?>
<properties>
<property key="en">Page 1-2</property>
<property key="it">Pagina 1-2</property>
</properties>', 'home', 1, NULL, '2017-02-17 13:06:24');
INSERT INTO pages_metadata_draft (code, titles, modelcode, showinmenu, extraconfig, updatedat) VALUES ('pagina_11', '<?xml version="1.0" encoding="UTF-8"?>
<properties>
<property key="en">Page 1-1</property>
<property key="it">Pagina 1-1</property>
</properties>', 'home', 1, NULL, '2017-02-17 13:06:24');
INSERT INTO pages_metadata_draft (code, titles, modelcode, showinmenu, extraconfig, updatedat) VALUES ('customer_subpage_1', '<?xml version="1.0" encoding="UTF-8"?>
<properties>
<property key="en">Customer SubPage 1</property>
<property key="it">Customer SubPage 1</property>
</properties>', 'home', 0, '<?xml version="1.0" encoding="UTF-8"?>
<config>
  <useextratitles>false</useextratitles>
  <extragroups>
    <group name="coach" />
  </extragroups>
</config>

', '2017-02-17 13:06:24');
INSERT INTO pages_metadata_draft (code, titles, modelcode, showinmenu, extraconfig, updatedat) VALUES ('pagina_1', '<?xml version="1.0" encoding="UTF-8"?>
<properties>
<property key="en">Page 1</property>
<property key="it">Pagina 1</property>
</properties>', 'home', 1, '<?xml version="1.0" encoding="UTF-8"?>
<config>
  <useextratitles>false</useextratitles>
</config>

', '2017-02-17 13:06:24');
INSERT INTO pages_metadata_draft (code, titles, modelcode, showinmenu, extraconfig, updatedat) VALUES ('dataObjectview', '<?xml version="1.0" encoding="UTF-8"?>
<properties>
<property key="en">DataObject Publishing</property>
<property key="it">Publicazione DataObject</property>
</properties>', 'home', 1, '<?xml version="1.0" encoding="UTF-8"?>
<config>
  <useextratitles>false</useextratitles>
</config>

', '2017-02-17 13:06:24');
INSERT INTO pages_metadata_draft (code, titles, modelcode, showinmenu, extraconfig, updatedat) VALUES ('contentview', '<?xml version="1.0" encoding="UTF-8"?>
<properties>
<property key="en">Content Publishing</property>
<property key="it">Publicazione Contenuto</property>
</properties>', 'home', 1, '<?xml version="1.0" encoding="UTF-8"?>
<config>
  <useextratitles>false</useextratitles>
</config>

', '2017-02-17 13:06:24');
INSERT INTO pages_metadata_draft (code, titles, modelcode, showinmenu, extraconfig, updatedat) VALUES ('pagina_2', '<?xml version="1.0" encoding="UTF-8"?>
<properties>
<property key="en">Page 2</property>
<property key="it">Pagina 2</property>
</properties>', 'home', 1, '<?xml version="1.0" encoding="UTF-8"?>
<config>
  <useextratitles>false</useextratitles>
</config>

', '2017-02-17 13:06:24');
INSERT INTO pages_metadata_draft (code, titles, modelcode, showinmenu, extraconfig, updatedat) VALUES ('pagina_draft', '<?xml version="1.0" encoding="UTF-8"?>
<properties>
<property key="en">Page Draft</property>
<property key="it">Pagina Draft</property>
</properties>', 'home', 0, '<?xml version="1.0" encoding="UTF-8"?>
<config>
  <useextratitles>true</useextratitles>
</config>

', '2017-02-17 13:06:24');


INSERT INTO widgetcatalog (code, titles, parameters, plugincode, parenttypecode, defaultconfig, locked, maingroup) VALUES ('login_form', '<?xml version="1.0" encoding="UTF-8"?>
<properties>
<property key="en">Login Widget</property>
<property key="it">Widget di Login</property>
</properties>', NULL, NULL, NULL, NULL, 1, NULL);
INSERT INTO widgetcatalog (code, titles, parameters, plugincode, parenttypecode, defaultconfig, locked, maingroup) VALUES ('messages_system', '<?xml version="1.0" encoding="UTF-8"?>
<properties>
<property key="en">System Messages</property>
<property key="it">Messaggi di Sistema</property>
</properties>', NULL, NULL, NULL, NULL, 1, NULL);
INSERT INTO widgetcatalog (code, titles, parameters, plugincode, parenttypecode, defaultconfig, locked, maingroup) VALUES ('formAction', '<?xml version="1.0" encoding="UTF-8"?>
<properties>
<property key="en">Internal Servlet</property>
<property key="it">Invocazione di una Servlet Interna</property>
</properties>', '<config>
	<parameter name="actionPath">
		Path relativo di una action o una Jsp
	</parameter>
	<action name="configSimpleParameter"/>
</config>', NULL, NULL, NULL, 1, NULL);
INSERT INTO widgetcatalog (code, titles, parameters, plugincode, parenttypecode, defaultconfig, locked, maingroup) VALUES ('leftmenu', '<?xml version="1.0" encoding="UTF-8"?>
<properties>
<property key="en">Vertical Navigation Menu</property>
<property key="it">Menu di navigazione verticale</property>
</properties>', '<config>
	<parameter name="navSpec">Rules for the Page List auto-generation</parameter>
	<action name="navigatorConfig" />
</config>', NULL, NULL, NULL, 1, NULL);
INSERT INTO widgetcatalog (code, titles, parameters, plugincode, parenttypecode, defaultconfig, locked, maingroup) VALUES ('entando_apis', '<?xml version="1.0" encoding="UTF-8"?>
<properties>
<property key="en">APIs</property>
<property key="it">APIs</property>
</properties>
', NULL, NULL, 'formAction', '<?xml version="1.0" encoding="UTF-8"?>
<properties>
<property key="actionPath">/ExtStr2/do/Front/Api/Resource/list.action</property>
</properties>
', 1, 'free');
INSERT INTO widgetcatalog (code, titles, parameters, plugincode, parenttypecode, defaultconfig, locked, maingroup) VALUES ('logic_type', '<?xml version="1.0" encoding="UTF-8"?>
<properties>
<property key="en">Logic type for test</property>
<property key="it">Tipo logico per test</property>
</properties>', NULL, NULL, 'leftmenu', '<?xml version="1.0" encoding="UTF-8"?>
<properties>
<property key="navSpec">code(homepage)</property>
</properties>', 0, NULL);




INSERT INTO guifragment (code, widgettypecode, plugincode, gui, defaultgui, locked) VALUES ('login_form', 'login_form', NULL, NULL, '<#assign wp=JspTaglibs["/aps-core"]>
<h1><@wp.i18n key="RESERVED_AREA" /></h1>
<#if (Session.currentUser.username != "guest") >
	<p><@wp.i18n key="WELCOME" />, <em>${Session.currentUser}</em>!</p>
	<#if (Session.currentUser.entandoUser) >
	<table class="table table-condensed">
		<tr>
			<th><@wp.i18n key="USER_DATE_CREATION" /></th>
			<th><@wp.i18n key="USER_DATE_ACCESS_LAST" /></th>
			<th><@wp.i18n key="USER_DATE_PASSWORD_CHANGE_LAST" /></th>
		</tr>
		<tr>
			<td>${Session.currentUser.creationDate?default("-")}</td>
			<td>${Session.currentUser.lastAccess?default("-")}</td>
			<td>${Session.currentUser.lastPasswordChange?default("-")}</td>
		</tr>
	</table>
		<#if (!Session.currentUser.credentialsNotExpired) >
		<div class="alert alert-block">
			<p><@wp.i18n key="USER_STATUS_EXPIRED_PASSWORD" />: <a href="<@wp.info key="systemParam" paramName="applicationBaseURL" />do/editPassword.action"><@wp.i18n key="USER_STATUS_EXPIRED_PASSWORD_CHANGE" /></a></p>
		</div>
		</#if>
	</#if>
	<@wp.ifauthorized permission="enterBackend">
	<div class="btn-group">
		<a href="<@wp.info key="systemParam" paramName="applicationBaseURL" />do/main.action?request_locale=<@wp.info key="currentLang" />" class="btn"><@wp.i18n key="ADMINISTRATION" /></a>
	</div>
	</@wp.ifauthorized>
	<p class="pull-right"><a href="<@wp.info key="systemParam" paramName="applicationBaseURL" />do/logout.action" class="btn"><@wp.i18n key="LOGOUT" /></a></p>
	<@wp.pageWithWidget widgetTypeCode="userprofile_editCurrentUser" var="userprofileEditingPageVar" listResult=false />
	<#if (userprofileEditingPageVar??) >
	<p><a href="<@wp.url page="${userprofileEditingPageVar.code}" />" ><@wp.i18n key="userprofile_CONFIGURATION" /></a></p>
	</#if>
<#else>
	<#if (accountExpired?? && accountExpired == true) >
	<div class="alert alert-block alert-error">
		<p><@wp.i18n key="USER_STATUS_EXPIRED" /></p>
	</div>
	</#if>
	<#if (wrongAccountCredential?? && wrongAccountCredential == true) >
	<div class="alert alert-block alert-error">
		<p><@wp.i18n key="USER_STATUS_CREDENTIALS_INVALID" /></p>
	</div>
	</#if>
	<form action="<@wp.url/>" method="post" class="form-horizontal margin-medium-top">
		<#if (RequestParameters.returnUrl??) >
		<input type="hidden" name="returnUrl" value="${RequestParameters.returnUrl}" />
		</#if>
		<div class="control-group">
			<label for="username" class="control-label"><@wp.i18n key="USERNAME" /></label>
			<div class="controls">
				<input id="username" type="text" name="username" class="input-xlarge" />
			</div>
		</div>
		<div class="control-group">
			<label for="password" class="control-label"><@wp.i18n key="PASSWORD" /></label>
			<div class="controls">
				<input id="password" type="password" name="password" class="input-xlarge" />
			</div>
		</div>
		<div class="form-actions">
			<input type="submit" value="<@wp.i18n key="SIGNIN" />" class="btn btn-primary" />
		</div>
	</form>
</#if>', 1);




INSERT INTO widgetconfig (pagecode, framepos, widgetcode, config) VALUES ('pagina_1', 2, 'leftmenu', '<?xml version="1.0" encoding="UTF-8"?>
<properties>
<property key="navSpec">abs(1).subtree(2)</property>
</properties>

');
INSERT INTO widgetconfig (pagecode, framepos, widgetcode, config) VALUES ('dataObjectview', 1, 'login_form', NULL);
INSERT INTO widgetconfig (pagecode, framepos, widgetcode, config) VALUES ('contentview', 1, 'login_form', NULL);
INSERT INTO widgetconfig (pagecode, framepos, widgetcode, config) VALUES ('contentview', 2, 'formAction', '<?xml version="1.0" encoding="UTF-8"?>
<properties>
<property key="actionPath">/do/login</property>
</properties>');
INSERT INTO widgetconfig (pagecode, framepos, widgetcode, config) VALUES ('pagina_2', 2, 'formAction', '<?xml version="1.0" encoding="UTF-8"?>
<properties>
<property key="actionPath">/do/login</property>
</properties>');
INSERT INTO widgetconfig (pagecode, framepos, widgetcode, config) VALUES ('coach_page', 2, 'formAction', '<?xml version="1.0" encoding="UTF-8"?>
<properties>
<property key="actionPath">/do/login</property>
</properties>');


INSERT INTO widgetconfig_draft (pagecode, framepos, widgetcode, config) VALUES ('pagina_1', 2, 'leftmenu', '<?xml version="1.0" encoding="UTF-8"?>
<properties>
<property key="navSpec">abs(1).subtree(2)</property>
</properties>

');
INSERT INTO widgetconfig_draft (pagecode, framepos, widgetcode, config) VALUES ('dataObjectview', 1, 'login_form', NULL);
INSERT INTO widgetconfig_draft (pagecode, framepos, widgetcode, config) VALUES ('contentview', 1, 'login_form', NULL);
INSERT INTO widgetconfig_draft (pagecode, framepos, widgetcode, config) VALUES ('contentview', 2, 'formAction', '<?xml version="1.0" encoding="UTF-8"?>
<properties>
<property key="actionPath">/do/login</property>
</properties>');
INSERT INTO widgetconfig_draft (pagecode, framepos, widgetcode, config) VALUES ('pagina_2', 0, 'leftmenu', '<?xml version="1.0" encoding="UTF-8"?>
<properties>
<property key="navSpec">abs(1).subtree(2)</property>
</properties>

');
INSERT INTO widgetconfig_draft (pagecode, framepos, widgetcode, config) VALUES ('pagina_2', 2, 'formAction', '<?xml version="1.0" encoding="UTF-8"?>
<properties>
<property key="actionPath">/do/login</property>
</properties>

');
INSERT INTO widgetconfig_draft (pagecode, framepos, widgetcode, config) VALUES ('pagina_draft', 1, 'leftmenu', '<?xml version="1.0" encoding="UTF-8"?>
<properties>
<property key="navSpec">abs(1).subtree(2)</property>
</properties>');
INSERT INTO widgetconfig_draft (pagecode, framepos, widgetcode, config) VALUES ('pagina_draft', 2, 'formAction', '<?xml version="1.0" encoding="UTF-8"?>
<properties>
<property key="actionPath">/do/login</property>
</properties>');
INSERT INTO widgetconfig_draft (pagecode, framepos, widgetcode, config) VALUES ('coach_page', 2, 'formAction', '<?xml version="1.0" encoding="UTF-8"?>
<properties>
<property key="actionPath">/do/login</property>
</properties>');


INSERT INTO sysconfig (version, item, descr, config) VALUES ('test', 'langs', 'Definition of the system languages', '<?xml version="1.0" encoding="UTF-8"?>
<Langs>
  <Lang>
    <code>it</code>
    <descr>Italiano</descr>
    <default>true</default>
  </Lang>
  <Lang>
    <code>en</code>
    <descr>English</descr>
  </Lang>
</Langs>

');
INSERT INTO sysconfig (version, item, descr, config) VALUES ('test', 'params', 'Configuration params. Tags other than "Param" are ignored', '<?xml version="1.0" encoding="UTF-8"?>
<Params>
	<Param name="urlStyle">classic</Param>
	<Param name="hypertextEditor">fckeditor</Param>
	<Param name="treeStyle_page">classic</Param>
	<Param name="treeStyle_category">classic</Param>
	<Param name="startLangFromBrowser">false</Param>
	<Param name="firstTimeMessages">true</Param>
	<Param name="baseUrl">static</Param>
	<Param name="baseUrlContext">true</Param>
	<Param name="useJsessionId">true</Param>
	<Param name="editEmptyFragmentEnabled">false</Param>
	<SpecialPages>
		<Param name="notFoundPageCode">notfound</Param>
		<Param name="homePageCode">homepage</Param>
		<Param name="errorPageCode">errorpage</Param>
		<Param name="loginPageCode">login</Param>
	</SpecialPages>
	<ExtendendPrivacyModule>
		<Param name="extendedPrivacyModuleEnabled">false</Param>
		<Param name="maxMonthsSinceLastAccess">6</Param>
		<Param name="maxMonthsSinceLastPasswordChange">3</Param>
	</ExtendendPrivacyModule>
</Params>');
INSERT INTO sysconfig (version, item, descr, config) VALUES ('test', 'userProfileTypes', 'User Profile Types Definitions', '<profiletypes>
	<profiletype typecode="PFL" typedescr="Default user profile" >
		<attributes>
			<attribute name="fullname" attributetype="Monotext" searchable="true" >
				<validations>
					<required>true</required>
				</validations>
				<roles>
					<role>userprofile:fullname</role>
				</roles>
			</attribute>
			<attribute name="email" attributetype="Monotext" searchable="true" >
				<validations>
					<required>true</required>
					<regexp><![CDATA[.+@.+.[a-z]+]]></regexp>
				</validations>
				<roles>
					<role>userprofile:email</role>
				</roles>
			</attribute>
			<attribute name="birthdate" attributetype="Date" required="true" searchable="true"/>
			<attribute name="language" attributetype="Monotext" required="true"/>
			<attribute name="boolean1" attributetype="Boolean" searchable="true"/>
			<attribute name="boolean2" attributetype="Boolean" searchable="true"/>
		</attributes>
	</profiletype>
</profiletypes>');
INSERT INTO sysconfig (version, item, descr, config) VALUES ('test', 'dataTypeDefinitions', 'Definition of the Data Types', '<?xml version="1.0" encoding="UTF-8"?>
<datatypes>
	<datatype typecode="ALL" typedescr="DataObject type with all attribute types" viewpage="announcements_read" listmodel="**NULL**" defaultmodel="**NULL**">
		<attributes>
			<attribute name="Boolean" attributetype="Boolean" />
			<attribute name="CheckBox" attributetype="CheckBox" />
			<attribute name="Date" attributetype="Date" />
			<attribute name="Date2" attributetype="Date">
				<validations>
					<rangestart attribute="Date" />
					<rangeend>25/11/2020</rangeend>
				</validations>
			</attribute>
			<attribute name="Enumerator" attributetype="Enumerator" separator=","><![CDATA[a,b,c]]></attribute>
			<attribute name="EnumeratorMap" attributetype="EnumeratorMap" separator=";"><![CDATA[01=Value 1;02=Value 2;03=Value 3]]></attribute>
			<attribute name="Hypertext" attributetype="Hypertext" />
			<attribute name="Longtext" attributetype="Longtext" />
			<attribute name="Monotext" attributetype="Monotext" />
			<attribute name="Monotext2" attributetype="Monotext">
				<validations>
					<minlength>15</minlength>
					<maxlength>30</maxlength>
					<regexp><![CDATA[.+@.+.[a-z]+]]></regexp>
				</validations>
			</attribute>
			<attribute name="Number" attributetype="Number" />
			<attribute name="Number2" attributetype="Number">
				<validations>
					<rangestart>50</rangestart>
					<rangeend>300</rangeend>
				</validations>
			</attribute>
			<attribute name="Text" attributetype="Text">
				<roles>
					<role>dataObject:title</role>
				</roles>
			</attribute>
			<attribute name="Text2" attributetype="Text">
				<validations>
					<minlength>15</minlength>
					<maxlength>30</maxlength>
					<regexp><![CDATA[.+@.+.[a-z]+]]></regexp>
				</validations>
			</attribute>
			<attribute name="ThreeState" attributetype="ThreeState" />
			<attribute name="Composite" attributetype="Composite">
				<attributes>
					<attribute name="Boolean" attributetype="Boolean" />
					<attribute name="CheckBox" attributetype="CheckBox" />
					<attribute name="Date" attributetype="Date">
						<validations>
							<rangestart attribute="Date" />
							<rangeend>10/10/2030</rangeend>
						</validations>
					</attribute>
					<attribute name="Enumerator" attributetype="Enumerator" separator="," />
					<attribute name="Hypertext" attributetype="Hypertext" />
					<attribute name="Longtext" attributetype="Longtext" />
					<attribute name="Monotext" attributetype="Monotext" />
					<attribute name="Number" attributetype="Number">
						<validations>
							<expression evalOnValuedAttribute="true">
								<ognlexpression><![CDATA[#entity.getAttribute(''Number'').value == null || (#entity.getAttribute(''Number'').value != null && value > #entity.getAttribute(''Number'').value)]]></ognlexpression>
								<errormessage><![CDATA[Value has to be upper then ''Number'' attribute]]></errormessage>
								<helpmessage><![CDATA[If ''Number'' valued attribute, Value has to be upper]]></helpmessage>
							</expression>
						</validations>
					</attribute>
					<attribute name="Text" attributetype="Text" />
					<attribute name="ThreeState" attributetype="ThreeState" />
				</attributes>
			</attribute>
			<list name="ListBoolea" attributetype="List">
				<nestedtype>
					<attribute name="ListBoolea" attributetype="Boolean" />
				</nestedtype>
			</list>
			<list name="ListCheck" attributetype="List">
				<nestedtype>
					<attribute name="ListCheck" attributetype="CheckBox" />
				</nestedtype>
			</list>
			<list name="ListDate" attributetype="List">
				<nestedtype>
					<attribute name="ListDate" attributetype="Date" />
				</nestedtype>
			</list>
			<list name="ListEnum" attributetype="List">
				<nestedtype>
					<attribute name="ListEnum" attributetype="Enumerator" separator=","><![CDATA[a,b,c]]></attribute>
				</nestedtype>
			</list>
			<list name="ListMonot" attributetype="List">
				<nestedtype>
					<attribute name="ListMonot" attributetype="Monotext" />
				</nestedtype>
			</list>
			<list name="ListNumber" attributetype="List">
				<nestedtype>
					<attribute name="ListNumber" attributetype="Number" />
				</nestedtype>
			</list>
			<list name="List3Stat" attributetype="List">
				<nestedtype>
					<attribute name="List3Stat" attributetype="ThreeState" />
				</nestedtype>
			</list>
			<list name="MonoLBool" attributetype="Monolist">
				<nestedtype>
					<attribute name="MonoLBool" attributetype="Boolean" />
				</nestedtype>
			</list>
			<list name="MonoLChec" attributetype="Monolist">
				<nestedtype>
					<attribute name="MonoLChec" attributetype="CheckBox" />
				</nestedtype>
			</list>
			<list name="MonoLCom" attributetype="Monolist">
				<nestedtype>
					<attribute name="MonoLCom" attributetype="Composite">
						<attributes>
							<attribute name="Date" attributetype="Date">
								<validations>
									<rangestart>10/10/1971</rangestart>
									<rangeend attribute="Date" />
								</validations>
							</attribute>
							<attribute name="Enumerator" attributetype="Enumerator" separator=","><![CDATA[a,b,c]]></attribute>
							<attribute name="EnumeratorMapTris" attributetype="EnumeratorMap" separator=";"><![CDATA[01=Value 1 Tris;02=Value 2 Tris;03=Value 3 Tris]]></attribute>
							<attribute name="Hypertext" attributetype="Hypertext" />
							<attribute name="Longtext" attributetype="Longtext" />
							<attribute name="Monotext" attributetype="Monotext" />
							<attribute name="Number" attributetype="Number">
								<validations>
									<rangestart>25</rangestart>
									<rangeend attribute="Number" />
								</validations>
							</attribute>
							<attribute name="Text" attributetype="Text" />
						</attributes>
					</attribute>
				</nestedtype>
			</list>
			<list name="MonoLCom2" attributetype="Monolist">
				<nestedtype>
					<attribute name="MonoLCom2" attributetype="Composite">
						<attributes>
							<attribute name="Boolean" attributetype="Boolean" />
							<attribute name="CheckBox" attributetype="CheckBox" />
							<attribute name="ThreeState" attributetype="ThreeState" />
						</attributes>
					</attribute>
				</nestedtype>
			</list>
			<list name="MonoLDate" attributetype="Monolist">
				<nestedtype>
					<attribute name="MonoLDate" attributetype="Date" />
				</nestedtype>
			</list>
			<list name="MonoLEnum" attributetype="Monolist">
				<nestedtype>
					<attribute name="MonoLEnum" attributetype="Enumerator" separator=","><![CDATA[a,b,c]]></attribute>
				</nestedtype>
			</list>
			<list name="MonoLHyper" attributetype="Monolist">
				<nestedtype>
					<attribute name="MonoLHyper" attributetype="Hypertext" />
				</nestedtype>
			</list>
			<list name="MonoLLong" attributetype="Monolist">
				<nestedtype>
					<attribute name="MonoLLong" attributetype="Longtext" />
				</nestedtype>
			</list>
			<list name="MonoLMonot" attributetype="Monolist">
				<nestedtype>
					<attribute name="MonoLMonot" attributetype="Monotext" />
				</nestedtype>
			</list>
			<list name="MonoLNumb" attributetype="Monolist">
				<nestedtype>
					<attribute name="MonoLNumb" attributetype="Number" />
				</nestedtype>
			</list>
			<list name="MonoLText" attributetype="Monolist">
				<nestedtype>
					<attribute name="MonoLText" attributetype="Text" />
				</nestedtype>
			</list>
			<list name="MonoL3stat" attributetype="Monolist">
				<nestedtype>
					<attribute name="MonoL3stat" attributetype="ThreeState" />
				</nestedtype>
			</list>
			<attribute name="EnumeratorMapBis" attributetype="EnumeratorMap" separator=";"><![CDATA[01=Value 1 Bis;02=Value 2 Bis;03=Value 3 Bis]]></attribute>
			<attribute name="MARKER" attributetype="Monotext">
				<validations>
					<required>true</required>
				</validations>
			</attribute>
		</attributes>
	</datatype>
	<datatype typecode="ART" typedescr="Articolo rassegna stampa" viewpage="dataObjectview" listmodel="11" defaultmodel="1">
		<attributes>
			<attribute name="Titolo" attributetype="Text" indexingtype="TEXT">
				<validations>
					<required>true</required>
				</validations>
				<roles>
					<role>dataObject:title</role>
				</roles>
			</attribute>
			<list name="Autori" attributetype="Monolist">
				<nestedtype>
					<attribute name="Autori" attributetype="Monotext" />
				</nestedtype>
			</list>
			<attribute name="CorpoTesto" attributetype="Hypertext" indexingtype="text" />
			<attribute name="Data" attributetype="Date" searchable="true" />
			<attribute name="Numero" attributetype="Number" searchable="true" />
		</attributes>
	</datatype>
	<datatype typecode="EVN" typedescr="Evento" viewpage="dataObjectview" listmodel="51" defaultmodel="5">
		<attributes>
			<attribute name="Titolo" attributetype="Text" searchable="true" indexingtype="TEXT">
				<validations />
				<roles>
					<role>dataObject:title</role>
				</roles>
			</attribute>
			<attribute name="CorpoTesto" attributetype="Hypertext" indexingtype="text" />
			<attribute name="DataInizio" attributetype="Date" searchable="true" />
			<attribute name="DataFine" attributetype="Date" searchable="true" />
		</attributes>
	</datatype>
	<datatype typecode="RAH" typedescr="Tipo_Semplice" viewpage="dataObjectview" listmodel="126" defaultmodel="457">
		<attributes>
			<attribute name="Titolo" attributetype="Text" indexingtype="text">
				<validations>
					<minlength>10</minlength>
					<maxlength>100</maxlength>
				</validations>
			</attribute>
			<attribute name="CorpoTesto" attributetype="Hypertext" indexingtype="text" />
			<attribute name="email" attributetype="Monotext">
				<validations>
					<regexp><![CDATA[.+@.+.[a-z]+]]></regexp>
				</validations>
			</attribute>
			<attribute name="Numero" attributetype="Number" />
			<attribute name="Checkbox" attributetype="CheckBox" />
		</attributes>
	</datatype>
</datatypes>
');
INSERT INTO sysconfig (version, item, descr, config) VALUES ('test', 'dataobjectsubdir', 'Name of the sub-directory containing dataobject indexing files', 'index');



INSERT INTO uniquekeys (id, keyvalue) VALUES (1, 200);
