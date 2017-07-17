ALTER TABLE contents ADD COLUMN firsteditor character varying(40);



##	------	------	------	------	------	porting script for pages

CREATE TABLE pages_metadata_online
(
  code character varying(30) NOT NULL,
  modelcode character varying(40) NOT NULL,
  titles text NOT NULL,
  showinmenu smallint NOT NULL,
  extraconfig text,
  updatedat timestamp without time zone NOT NULL,
  CONSTRAINT pages_metadata_pkey PRIMARY KEY (code),
  CONSTRAINT pages_metadata_code_fkey FOREIGN KEY (code)
      REFERENCES pages (code) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
);

CREATE TABLE pages_metadata_draft
(
  code character varying(30) NOT NULL,
  modelcode character varying(40) NOT NULL,
  titles text NOT NULL,
  showinmenu smallint NOT NULL,
  extraconfig text,
  updatedat timestamp without time zone NOT NULL,
  CONSTRAINT pages_metadata_draft_pkey PRIMARY KEY (code),
  CONSTRAINT pages_metadata_draft_code_fkey FOREIGN KEY (code)
      REFERENCES pages (code) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
);

insert into pages_metadata_draft select code, modelcode, titles,showinmenu, extraconfig, current_timestamp from pages;
insert into pages_metadata_online select code, modelcode, titles,showinmenu, extraconfig, current_timestamp from pages;




ALTER TABLE pages  DROP COLUMN showinmenu;
ALTER TABLE pages  DROP COLUMN extraconfig;
ALTER TABLE pages  DROP COLUMN modelcode;
ALTER TABLE pages  DROP COLUMN titles;



CREATE TABLE widgetconfig_draft
(
  pagecode character varying(30) NOT NULL,
  framepos integer NOT NULL,
  widgetcode character varying(40) NOT NULL,
  config text,
  CONSTRAINT widgetconfig_draft_pkey PRIMARY KEY (pagecode, framepos),
  CONSTRAINT widgetconfig_draft_pagecode_fkey FOREIGN KEY (pagecode)
      REFERENCES pages (code) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT widgetconfig_draft_widgetcode_fkey FOREIGN KEY (widgetcode)
      REFERENCES widgetcatalog (code) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
)
WITH (
  OIDS=FALSE
);


insert into widgetconfig_draft select * from widgetconfig;


ALTER TABLE pages_metadata_draft
  ADD CONSTRAINT pages_metadata_draft_modelcode_fk FOREIGN KEY (modelcode)
      REFERENCES pagemodels (code) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION;


ALTER TABLE pages_metadata_online
  ADD CONSTRAINT pages_metadata_draft_modelcode_fk FOREIGN KEY (modelcode)
      REFERENCES pagemodels (code) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION;


##	------	------	------	------	------	porting script for pages end

