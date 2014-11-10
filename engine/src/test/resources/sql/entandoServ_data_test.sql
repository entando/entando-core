INSERT INTO authgroups (groupname, descr) VALUES ('administrators', 'Amministratori');
INSERT INTO authgroups (groupname, descr) VALUES ('coach', 'Coach');
INSERT INTO authgroups (groupname, descr) VALUES ('customers', 'Customers');
INSERT INTO authgroups (groupname, descr) VALUES ('free', 'Accesso Libero');
INSERT INTO authgroups (groupname, descr) VALUES ('helpdesk', 'Helpdesk');
INSERT INTO authgroups (groupname, descr) VALUES ('management', 'Management');




INSERT INTO authroles (rolename, descr) VALUES ('admin', 'Tutte le funzioni');
INSERT INTO authroles (rolename, descr) VALUES ('editor', 'Gestore di Contenuti e Risorse');
INSERT INTO authroles (rolename, descr) VALUES ('supervisor', 'Supervisore di Contenuti');
INSERT INTO authroles (rolename, descr) VALUES ('pageManager', 'Gestore di Pagine');




INSERT INTO authpermissions (permissionname, descr) VALUES ('managePages', 'Operazioni su Pagine');
INSERT INTO authpermissions (permissionname, descr) VALUES ('enterBackend', 'Accesso all''Area di Amministrazione');
INSERT INTO authpermissions (permissionname, descr) VALUES ('manageResources', 'Operazioni su Risorse');
INSERT INTO authpermissions (permissionname, descr) VALUES ('editContents', 'Redazione di Contenuti');
INSERT INTO authpermissions (permissionname, descr) VALUES ('validateContents', 'Supervisione di Contenuti');
INSERT INTO authpermissions (permissionname, descr) VALUES ('superuser', 'Tutte le funzioni');
INSERT INTO authpermissions (permissionname, descr) VALUES ('manageCategories', 'Operazioni su Categorie');




INSERT INTO authrolepermissions (rolename, permissionname) VALUES ('admin', 'superuser');
INSERT INTO authrolepermissions (rolename, permissionname) VALUES ('pageManager', 'enterBackend');
INSERT INTO authrolepermissions (rolename, permissionname) VALUES ('editor', 'enterBackend');
INSERT INTO authrolepermissions (rolename, permissionname) VALUES ('supervisor', 'enterBackend');
INSERT INTO authrolepermissions (rolename, permissionname) VALUES ('pageManager', 'managePages');
INSERT INTO authrolepermissions (rolename, permissionname) VALUES ('supervisor', 'editContents');
INSERT INTO authrolepermissions (rolename, permissionname) VALUES ('editor', 'editContents');
INSERT INTO authrolepermissions (rolename, permissionname) VALUES ('supervisor', 'validateContents');
INSERT INTO authrolepermissions (rolename, permissionname) VALUES ('editor', 'manageResources');




INSERT INTO authusergrouprole (username, groupname, rolename) VALUES ('pageManagerCoach', 'coach', 'pageManager');
INSERT INTO authusergrouprole (username, groupname, rolename) VALUES ('pageManagerCustomers', 'customers', 'pageManager');
INSERT INTO authusergrouprole (username, groupname, rolename) VALUES ('supervisorCoach', 'coach', 'supervisor');
INSERT INTO authusergrouprole (username, groupname, rolename) VALUES ('supervisorCustomers', 'customers', 'supervisor');
INSERT INTO authusergrouprole (username, groupname, rolename) VALUES ('editorCoach', 'coach', 'editor');
INSERT INTO authusergrouprole (username, groupname, rolename) VALUES ('editorCustomers', 'customers', 'editor');
INSERT INTO authusergrouprole (username, groupname, rolename) VALUES ('supervisorCoach', 'customers', 'supervisor');
INSERT INTO authusergrouprole (username, groupname, rolename) VALUES ('editorCoach', 'customers', 'editor');
INSERT INTO authusergrouprole (username, groupname, rolename) VALUES ('mainEditor', 'administrators', 'editor');
INSERT INTO authusergrouprole (username, groupname, rolename) VALUES ('pageManagerCoach', 'customers', 'pageManager');
INSERT INTO authusergrouprole (username, groupname, rolename) VALUES ('admin', 'administrators', 'admin');




INSERT INTO authusers (username, passwd, registrationdate, lastaccess, lastpasswordchange, active) VALUES ('supervisorCoach', 'supervisorCoach', '2008-09-25 00:00:00', '2009-01-30 00:00:00', NULL, 1);
INSERT INTO authusers (username, passwd, registrationdate, lastaccess, lastpasswordchange, active) VALUES ('mainEditor', 'mainEditor', '2008-09-25 00:00:00', '2009-01-30 00:00:00', NULL, 1);
INSERT INTO authusers (username, passwd, registrationdate, lastaccess, lastpasswordchange, active) VALUES ('pageManagerCoach', 'pageManagerCoach', '2008-09-25 00:00:00', '2009-01-30 00:00:00', NULL, 1);
INSERT INTO authusers (username, passwd, registrationdate, lastaccess, lastpasswordchange, active) VALUES ('supervisorCustomers', 'supervisorCustomers', '2008-09-25 00:00:00', '2009-01-30 00:00:00', NULL, 1);
INSERT INTO authusers (username, passwd, registrationdate, lastaccess, lastpasswordchange, active) VALUES ('pageManagerCustomers', 'pageManagerCustomers', '2008-09-25 00:00:00', '2009-01-30 00:00:00', NULL, 1);
INSERT INTO authusers (username, passwd, registrationdate, lastaccess, lastpasswordchange, active) VALUES ('editorCustomers', 'editorCustomers', '2008-09-25 00:00:00', '2009-07-02 00:00:00', NULL, 1);
INSERT INTO authusers (username, passwd, registrationdate, lastaccess, lastpasswordchange, active) VALUES ('editorCoach', 'editorCoach', '2008-09-25 00:00:00', '2009-07-02 00:00:00', NULL, 1);
INSERT INTO authusers (username, passwd, registrationdate, lastaccess, lastpasswordchange, active) VALUES ('admin', 'admin', '2008-09-25 00:00:00', '2009-12-16 00:00:00', NULL, 1);




INSERT INTO authuserprofiles (username, profiletype, profilexml, publicprofile) VALUES ('editorCustomers', 'PFL', '<?xml version="1.0" encoding="UTF-8"?>
<profile id="editorCustomers" typecode="PFL" typedescr="Default user profile"><descr /><groups /><categories />
	<attributes>
		<attribute name="fullname" attributetype="Monotext"><monotext>Sean Red</monotext></attribute>
		<attribute name="birthdate" attributetype="Date"><date>19520521</date></attribute>
		<attribute name="email" attributetype="Monotext"><monotext>sean.red@mailinator.com</monotext></attribute>
		<attribute name="language" attributetype="Monotext"><monotext>it</monotext></attribute>
		<attribute name="boolean1" attributetype="Boolean"><boolean>false</boolean></attribute>
		<attribute name="boolean2" attributetype="Boolean"><boolean>false</boolean></attribute>
	</attributes>
</profile>', 0);
INSERT INTO authuserprofiles (username, profiletype, profilexml, publicprofile) VALUES ('mainEditor', 'PFL', '<?xml version="1.0" encoding="UTF-8"?>
<profile id="mainEditor" typecode="PFL" typedescr="Default user profile"><descr /><groups /><categories />
	<attributes>
		<attribute name="fullname" attributetype="Monotext"><monotext>Amanda Chedwase</monotext></attribute>
		<attribute name="birthdate" attributetype="Date"><date>19471124</date></attribute>
		<attribute name="email" attributetype="Monotext"><monotext>amanda.chedwase@mailinator.com</monotext></attribute>
		<attribute name="language" attributetype="Monotext"><monotext>it</monotext></attribute>
		<attribute name="boolean1" attributetype="Boolean"><boolean>false</boolean></attribute>
		<attribute name="boolean2" attributetype="Boolean"><boolean>false</boolean></attribute>
	</attributes>
</profile>', 0);
INSERT INTO authuserprofiles (username, profiletype, profilexml, publicprofile) VALUES ('pageManagerCoach', 'PFL', '<?xml version="1.0" encoding="UTF-8"?>
<profile id="pageManagerCoach" typecode="PFL" typedescr="Default user profile"><descr /><groups /><categories />
	<attributes>
		<attribute name="fullname" attributetype="Monotext"><monotext>Raimond Stevenson</monotext></attribute>
		<attribute name="birthdate" attributetype="Date"><date>20000904</date></attribute>
		<attribute name="email" attributetype="Monotext"><monotext>raimond.stevenson@mailinator.com</monotext></attribute>
		<attribute name="language" attributetype="Monotext"><monotext>it</monotext></attribute>
		<attribute name="boolean1" attributetype="Boolean"><boolean>false</boolean></attribute>
		<attribute name="boolean2" attributetype="Boolean"><boolean>false</boolean></attribute>
	</attributes>
</profile>', 0);
INSERT INTO authuserprofiles (username, profiletype, profilexml, publicprofile) VALUES ('editorCoach', 'PFL', '<?xml version="1.0" encoding="UTF-8"?>
<profile id="editorCoach" typecode="PFL" typedescr="Default user profile"><descr /><groups /><categories />
	<attributes>
		<attribute name="fullname" attributetype="Monotext"><monotext>Rick Bobonsky</monotext></attribute>
		<attribute name="email" attributetype="Monotext"><monotext>rick.bobonsky@mailinator.com</monotext></attribute>
		<attribute name="birthdate" attributetype="Date"><date>19450301</date></attribute>
		<attribute name="language" attributetype="Monotext"><monotext>it</monotext></attribute>
		<attribute name="boolean1" attributetype="Boolean"><boolean>false</boolean></attribute>
		<attribute name="boolean2" attributetype="Boolean"><boolean>false</boolean></attribute>
	</attributes>
</profile>', 0);

INSERT INTO authuserprofilesearch (username, attrname, textvalue, datevalue, numvalue, langcode) VALUES ('editorCoach', 'fullname', 'Rick Bobonsky', NULL, NULL, NULL);
INSERT INTO authuserprofilesearch (username, attrname, textvalue, datevalue, numvalue, langcode) VALUES ('editorCoach', 'email', 'rick.bobonsky@mailinator.com', NULL, NULL, NULL);
INSERT INTO authuserprofilesearch (username, attrname, textvalue, datevalue, numvalue, langcode) VALUES ('editorCoach', 'birthdate', NULL, '1945-03-01 00:00:00', NULL, NULL);
INSERT INTO authuserprofilesearch (username, attrname, textvalue, datevalue, numvalue, langcode) VALUES ('editorCoach', 'boolean1', 'false', NULL, NULL, NULL);
INSERT INTO authuserprofilesearch (username, attrname, textvalue, datevalue, numvalue, langcode) VALUES ('editorCoach', 'boolean2', 'false', NULL, NULL, NULL);
INSERT INTO authuserprofilesearch (username, attrname, textvalue, datevalue, numvalue, langcode) VALUES ('editorCustomers', 'fullname', 'Sean Red', NULL, NULL, NULL);
INSERT INTO authuserprofilesearch (username, attrname, textvalue, datevalue, numvalue, langcode) VALUES ('editorCustomers', 'email', 'sean.red@mailinator.com', NULL, NULL, NULL);
INSERT INTO authuserprofilesearch (username, attrname, textvalue, datevalue, numvalue, langcode) VALUES ('editorCustomers', 'birthdate', NULL, '1952-05-21 00:00:00', NULL, NULL);
INSERT INTO authuserprofilesearch (username, attrname, textvalue, datevalue, numvalue, langcode) VALUES ('editorCustomers', 'boolean1', 'false', NULL, NULL, NULL);
INSERT INTO authuserprofilesearch (username, attrname, textvalue, datevalue, numvalue, langcode) VALUES ('editorCustomers', 'boolean2', 'false', NULL, NULL, NULL);
INSERT INTO authuserprofilesearch (username, attrname, textvalue, datevalue, numvalue, langcode) VALUES ('mainEditor', 'fullname', 'Amanda Chedwase', NULL, NULL, NULL);
INSERT INTO authuserprofilesearch (username, attrname, textvalue, datevalue, numvalue, langcode) VALUES ('mainEditor', 'email', 'amanda.chedwase@mailinator.com', NULL, NULL, NULL);
INSERT INTO authuserprofilesearch (username, attrname, textvalue, datevalue, numvalue, langcode) VALUES ('mainEditor', 'birthdate', NULL, '1947-11-24 00:00:00', NULL, NULL);
INSERT INTO authuserprofilesearch (username, attrname, textvalue, datevalue, numvalue, langcode) VALUES ('mainEditor', 'boolean1', 'false', NULL, NULL, NULL);
INSERT INTO authuserprofilesearch (username, attrname, textvalue, datevalue, numvalue, langcode) VALUES ('mainEditor', 'boolean2', 'false', NULL, NULL, NULL);
INSERT INTO authuserprofilesearch (username, attrname, textvalue, datevalue, numvalue, langcode) VALUES ('pageManagerCoach', 'fullname', 'Raimond Stevenson', NULL, NULL, NULL);
INSERT INTO authuserprofilesearch (username, attrname, textvalue, datevalue, numvalue, langcode) VALUES ('pageManagerCoach', 'email', 'raimond.stevenson@mailinator.com', NULL, NULL, NULL);
INSERT INTO authuserprofilesearch (username, attrname, textvalue, datevalue, numvalue, langcode) VALUES ('pageManagerCoach', 'birthdate', NULL, '2000-09-04 00:00:00', NULL, NULL);
INSERT INTO authuserprofilesearch (username, attrname, textvalue, datevalue, numvalue, langcode) VALUES ('pageManagerCoach', 'boolean1', 'false', NULL, NULL, NULL);
INSERT INTO authuserprofilesearch (username, attrname, textvalue, datevalue, numvalue, langcode) VALUES ('pageManagerCoach', 'boolean2', 'false', NULL, NULL, NULL);




INSERT INTO authuserprofileattrroles (username, attrname, rolename) VALUES ('editorCoach', 'fullname', 'userprofile:fullname');
INSERT INTO authuserprofileattrroles (username, attrname, rolename) VALUES ('editorCustomers', 'fullname', 'userprofile:fullname');
INSERT INTO authuserprofileattrroles (username, attrname, rolename) VALUES ('mainEditor', 'fullname', 'userprofile:fullname');
INSERT INTO authuserprofileattrroles (username, attrname, rolename) VALUES ('pageManagerCoach', 'fullname', 'userprofile:fullname');
INSERT INTO authuserprofileattrroles (username, attrname, rolename) VALUES ('editorCoach', 'email', 'userprofile:email');
INSERT INTO authuserprofileattrroles (username, attrname, rolename) VALUES ('editorCustomers', 'email', 'userprofile:email');
INSERT INTO authuserprofileattrroles (username, attrname, rolename) VALUES ('mainEditor', 'email', 'userprofile:email');
INSERT INTO authuserprofileattrroles (username, attrname, rolename) VALUES ('pageManagerCoach', 'email', 'userprofile:email');

