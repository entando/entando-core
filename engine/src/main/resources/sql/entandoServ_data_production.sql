INSERT INTO authgroups (groupname, descr) VALUES ('administrators', 'Administrators');
INSERT INTO authgroups (groupname, descr) VALUES ('free', 'Free Access');




INSERT INTO authpermissions (permissionname, descr) VALUES ('superuser', 'All functions');
INSERT INTO authpermissions (permissionname, descr) VALUES ('validateContents', 'Supervision of contents');
INSERT INTO authpermissions (permissionname, descr) VALUES ('manageResources', 'Operations on Resources');
INSERT INTO authpermissions (permissionname, descr) VALUES ('managePages', 'Operations on Pages');
INSERT INTO authpermissions (permissionname, descr) VALUES ('enterBackend', 'Access to Administration Area');
INSERT INTO authpermissions (permissionname, descr) VALUES ('manageCategories', 'Operations on Categories');
INSERT INTO authpermissions (permissionname, descr) VALUES ('editContents', 'Content Editing');
INSERT INTO authpermissions (permissionname, descr) VALUES ('viewUsers', 'View Users and Profiles');
INSERT INTO authpermissions (permissionname, descr) VALUES ('editUsers', 'User Editing');
INSERT INTO authpermissions (permissionname, descr) VALUES ('editUserProfile', 'User Profile Editing');




INSERT INTO authroles (rolename, descr) VALUES ('admin', 'Administrator');




INSERT INTO authrolepermissions (rolename, permissionname) VALUES ('admin', 'superuser');




INSERT INTO authusergrouprole (username, groupname, rolename) VALUES ('admin', 'administrators', 'admin');




INSERT INTO authusers (username, passwd, registrationdate, lastaccess, lastpasswordchange, active) VALUES ('admin', 'adminadmin', '2008-10-10 00:00:00', '2011-02-05 00:00:00', NULL, 1);



INSERT INTO authuserprofiles (username, profiletype, profilexml, publicprofile) VALUES ('admin', 'PFL', '<?xml version="1.0" encoding="UTF-8"?>
<profile id="admin" typecode="PFL" typedescr="Default user profile"><descr /><groups /><categories /><attributes><attribute name="fullname" attributetype="Monotext" /><attribute name="email" attributetype="Monotext" /></attributes></profile>
', 0);



INSERT INTO api_oauth_consumers (consumerkey, consumersecret, name, description, callbackurl, scope, authorizedgranttypes, expirationdate, issueddate) VALUES ('appbuilder', '{bcrypt}$2a$10$axXuJXKHzgdmwQzBR3wvh.oSiiJp6On1pHxJgaBmwkRXnXqYqjhkK', 'Entando AppBuiler', 'Default Entando AppBuiler Consumer', NULL, 'read,write,trust', 'password,authorization_code,refresh_token,implicit', '2028-10-10 00:00:00', '2008-01-01 00:00:00');



INSERT INTO actionlogrecords (id, username, actiondate, updatedate, namespace, actionname, parameters, activitystreaminfo) VALUES (1, 'admin', '2013-09-27 10:58:38', '2013-09-27 10:58:38', '/do/Page', 'save', 'selectedNode=service
model=service
strutsAction=1
extraGroupName=free
charset=
parentPageCode=service
defaultShowlet=true
copyPageCode=
langit=Accedi
groupSelectLock=false
langen=Sign In
group=free
mimeType=
pageCode=sign_in
', '<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<activityStreamInfo>
    <objectTitles>
        <entry>
            <key xsi:type="xs:string" xmlns:xs="http://www.w3.org/2001/XMLSchema" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance">en</key>
            <value xsi:type="xs:string" xmlns:xs="http://www.w3.org/2001/XMLSchema" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance">Sign In</value>
        </entry>
        <entry>
            <key xsi:type="xs:string" xmlns:xs="http://www.w3.org/2001/XMLSchema" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance">it</key>
            <value xsi:type="xs:string" xmlns:xs="http://www.w3.org/2001/XMLSchema" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance">Accedi</value>
        </entry>
    </objectTitles>
    <groups>
        <group>free</group>
    </groups>
    <actionType>1</actionType>
    <linkNamespace>/do/Page</linkNamespace>
    <linkActionName>edit</linkActionName>
    <linkParameters>
        <entry>
            <key xsi:type="xs:string" xmlns:xs="http://www.w3.org/2001/XMLSchema" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance">selectedNode</key>
            <value xsi:type="xs:string" xmlns:xs="http://www.w3.org/2001/XMLSchema" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance">sign_in</value>
        </entry>
    </linkParameters>
    <linkAuthPermission>managePages</linkAuthPermission>
    <linkAuthGroup>free</linkAuthGroup>
</activityStreamInfo>
');

INSERT INTO actionlogrecords (id, username, actiondate, updatedate, namespace, actionname, parameters, activitystreaminfo) VALUES (2, 'admin', '2013-09-27 11:00:12', '2013-09-27 11:00:12', '/do/Page', 'save', 'selectedNode=service
model=service
strutsAction=1
extraGroupName=free
charset=
parentPageCode=service
defaultShowlet=true
copyPageCode=
langit=Pagina non trovata
groupSelectLock=false
langen=Page not found
group=free
mimeType=
pageCode=notfound
', '<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<activityStreamInfo>
    <objectTitles>
        <entry>
            <key xsi:type="xs:string" xmlns:xs="http://www.w3.org/2001/XMLSchema" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance">en</key>
            <value xsi:type="xs:string" xmlns:xs="http://www.w3.org/2001/XMLSchema" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance">Page not found</value>
        </entry>
        <entry>
            <key xsi:type="xs:string" xmlns:xs="http://www.w3.org/2001/XMLSchema" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance">it</key>
            <value xsi:type="xs:string" xmlns:xs="http://www.w3.org/2001/XMLSchema" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance">Pagina non trovata</value>
        </entry>
    </objectTitles>
    <groups>
        <group>free</group>
    </groups>
    <actionType>1</actionType>
    <linkNamespace>/do/Page</linkNamespace>
    <linkActionName>edit</linkActionName>
    <linkParameters>
        <entry>
            <key xsi:type="xs:string" xmlns:xs="http://www.w3.org/2001/XMLSchema" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance">selectedNode</key>
            <value xsi:type="xs:string" xmlns:xs="http://www.w3.org/2001/XMLSchema" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance">notfound</value>
        </entry>
    </linkParameters>
    <linkAuthPermission>managePages</linkAuthPermission>
    <linkAuthGroup>free</linkAuthGroup>
</activityStreamInfo>
');

INSERT INTO actionlogrecords (id, username, actiondate, updatedate, namespace, actionname, parameters, activitystreaminfo) VALUES (3, 'admin', '2013-09-27 11:00:12', '2013-09-27 11:00:12', '/do/Page', 'save', 'selectedNode=service
model=service
strutsAction=1
extraGroupName=free
charset=
parentPageCode=service
defaultShowlet=true
copyPageCode=
langit=Errore di Sistema
groupSelectLock=false
langen=System Error
group=free
mimeType=
pageCode=errorpage
', '<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<activityStreamInfo>
    <objectTitles>
        <entry>
            <key xsi:type="xs:string" xmlns:xs="http://www.w3.org/2001/XMLSchema" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance">en</key>
            <value xsi:type="xs:string" xmlns:xs="http://www.w3.org/2001/XMLSchema" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance">System Error</value>
        </entry>
        <entry>
            <key xsi:type="xs:string" xmlns:xs="http://www.w3.org/2001/XMLSchema" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance">it</key>
            <value xsi:type="xs:string" xmlns:xs="http://www.w3.org/2001/XMLSchema" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance">Errore di Sistema</value>
        </entry>
    </objectTitles>
    <groups>
        <group>free</group>
    </groups>
    <actionType>1</actionType>
    <linkNamespace>/do/Page</linkNamespace>
    <linkActionName>edit</linkActionName>
    <linkParameters>
        <entry>
            <key xsi:type="xs:string" xmlns:xs="http://www.w3.org/2001/XMLSchema" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance">selectedNode</key>
            <value xsi:type="xs:string" xmlns:xs="http://www.w3.org/2001/XMLSchema" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance">errorpage</value>
        </entry>
    </linkParameters>
    <linkAuthPermission>managePages</linkAuthPermission>
    <linkAuthGroup>free</linkAuthGroup>
</activityStreamInfo>
');

INSERT INTO actionlogrelations (recordid, refgroup) VALUES (1, 'free');
INSERT INTO actionlogrelations (recordid, refgroup) VALUES (2, 'free');
INSERT INTO actionlogrelations (recordid, refgroup) VALUES (3, 'free');
