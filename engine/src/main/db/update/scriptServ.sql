CREATE TABLE dataobjects
(
  contentid character varying(16) NOT NULL,
  datatype character varying(30) NOT NULL,
  descr character varying(255) NOT NULL,
  status character varying(12) NOT NULL,
  workxml text NOT NULL,
  created character varying(20),
  lastmodified character varying(20),
  onlinexml text,
  maingroup character varying(20) NOT NULL,
  currentversion character varying(7) NOT NULL,
  lasteditor character varying(40),
  firsteditor character varying(40),
  CONSTRAINT dataobjects_pkey PRIMARY KEY (contentid )
);

CREATE TABLE dataobjectsearch
(
  contentid character varying(16) NOT NULL,
  attrname character varying(30) NOT NULL,
  textvalue character varying(255),
  datevalue timestamp without time zone,
  numvalue integer,
  langcode character varying(3),
  CONSTRAINT dataobjectsearch_contid_fkey FOREIGN KEY (contentid)
      REFERENCES dataobjects (contentid) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
);

CREATE TABLE dataobjectrelations
(
  contentid character varying(16) NOT NULL,
  refcategory character varying(30),
  refgroup character varying(20)
);

CREATE TABLE dataobjectattributeroles
(
  contentid character varying(16) NOT NULL,
  attrname character varying(30) NOT NULL,
  rolename character varying(50) NOT NULL,
  CONSTRAINT dataobattrroles_contid_fkey FOREIGN KEY (contentid)
      REFERENCES dataobjects (contentid) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
);

CREATE TABLE workdataobjectattributeroles
(
  contentid character varying(16) NOT NULL,
  attrname character varying(30) NOT NULL,
  rolename character varying(50) NOT NULL,
  CONSTRAINT workdatobjattrroles_id_fkey FOREIGN KEY (contentid)
      REFERENCES dataobjects (contentid) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
);

CREATE TABLE workdataobjectrelations
(
  contentid character varying(16) NOT NULL,
  refcategory character varying(30),
  CONSTRAINT workdataobjrels_id_fkey FOREIGN KEY (contentid)
      REFERENCES dataobjects (contentid) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
);

CREATE TABLE workdataobjectsearch
(
  contentid character varying(16) NOT NULL,
  attrname character varying(30) NOT NULL,
  textvalue character varying(255),
  datevalue timestamp without time zone,
  numvalue integer,
  langcode character varying(3),
  CONSTRAINT workdataobjsear_contid_fkey FOREIGN KEY (contentid)
      REFERENCES dataobjects (contentid) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
);

CREATE TABLE dataobjectmodels
(
  modelid integer NOT NULL,
  datatype character varying(30) NOT NULL,
  descr character varying(50) NOT NULL,
  model text,
  stylesheet character varying(50),
  CONSTRAINT dataobjectmodels_pkey PRIMARY KEY (modelid )
);



