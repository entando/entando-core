DROP TABLE authuserroles;
DROP TABLE authusergroups;

CREATE TABLE authusergrouprole
(
  username character varying(40) NOT NULL,
  groupname character varying(20) NOT NULL,
  rolename character varying(20),
  CONSTRAINT authusergrouprole_grn_fkey FOREIGN KEY (groupname)
      REFERENCES authgroups (groupname),
  CONSTRAINT authusergrouprole_rln_fkey FOREIGN KEY (rolename)
      REFERENCES authroles (rolename)
);

INSERT INTO authusergrouprole (username, groupname, rolename) VALUES ('admin', 'administrators', 'admin');