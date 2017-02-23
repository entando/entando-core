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




INSERT INTO pagemodels (code, descr, frames, plugincode) VALUES ('home', 'Modello home page', '<frames>
	<frame pos="0"><descr>Box sinistra alto</descr></frame>
	<frame pos="1"><descr>Box sinistra basso</descr></frame>
	<frame pos="2" main="true"><descr>Box centrale 1</descr></frame>
	<frame pos="3"><descr>Box centrale 2</descr></frame>
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


INSERT INTO widgetconfig (pagecode, framepos, widgetcode, config) VALUES ('pagina_1', 2, 'leftmenu', '<?xml version="1.0" encoding="UTF-8"?>
<properties>
<property key="navSpec">abs(1).subtree(2)</property>
</properties>

');
INSERT INTO widgetconfig (pagecode, framepos, widgetcode, config) VALUES ('contentview', 1, 'login_form', NULL);
INSERT INTO widgetconfig (pagecode, framepos, widgetcode, config) VALUES ('pagina_2', 2, 'formAction', '<?xml version="1.0" encoding="UTF-8"?>
<properties>
<property key="actionPath">/do/login</property>
</properties>

');


INSERT INTO widgetconfig_draft (pagecode, framepos, widgetcode, config) VALUES ('pagina_1', 2, 'leftmenu', '<?xml version="1.0" encoding="UTF-8"?>
<properties>
<property key="navSpec">abs(1).subtree(2)</property>
</properties>

');
INSERT INTO widgetconfig_draft (pagecode, framepos, widgetcode, config) VALUES ('contentview', 1, 'login_form', NULL);
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
</properties>

');
INSERT INTO widgetconfig_draft (pagecode, framepos, widgetcode, config) VALUES ('pagina_draft', 2, 'formAction', '<?xml version="1.0" encoding="UTF-8"?>
<properties>
<property key="actionPath">/do/login</property>
</properties>

');


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



INSERT INTO uniquekeys (id, keyvalue) VALUES (1, 200);
