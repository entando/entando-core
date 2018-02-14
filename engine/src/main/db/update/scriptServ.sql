CREATE TABLE dataobjects
(
  dataid character varying(16) NOT NULL,
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
  CONSTRAINT dataobjects_pkey PRIMARY KEY (dataid )
);

CREATE TABLE dataobjectsearch
(
  dataid character varying(16) NOT NULL,
  attrname character varying(30) NOT NULL,
  textvalue character varying(255),
  datevalue timestamp without time zone,
  numvalue integer,
  langcode character varying(3),
  CONSTRAINT dataobjectsearch_contid_fkey FOREIGN KEY (dataid)
      REFERENCES dataobjects (dataid) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
);

CREATE TABLE dataobjectrelations
(
  dataid character varying(16) NOT NULL,
  refcategory character varying(30),
  refgroup character varying(20)
);

CREATE TABLE dataobjectattributeroles
(
  dataid character varying(16) NOT NULL,
  attrname character varying(30) NOT NULL,
  rolename character varying(50) NOT NULL,
  CONSTRAINT dataobattrroles_contid_fkey FOREIGN KEY (dataid)
      REFERENCES dataobjects (dataid) MATCH SIMPLE
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

ALTER TABLE actionlogcommentrecords RENAME comment TO commenttext;

