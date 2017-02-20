ALTER TABLE contents ADD COLUMN firsteditor character varying(40);



##	------	------	------	------	------	porting script for pages

CREATE TABLE pages_metadata_online
(
  code character varying(30) NOT NULL,
  modelcode character varying(40) NOT NULL,
  titles text NOT NULL,
  showinmenu smallint NOT NULL,
  extraconfig text,
  lastmodified timestamp without time zone,
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
  lastmodified timestamp without time zone,
  CONSTRAINT pages_metadata_draft_pkey PRIMARY KEY (code),
  CONSTRAINT pages_metadata_draft_code_fkey FOREIGN KEY (code)
      REFERENCES pages (code) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
);

insert into pages_metadata_draft select code, modelcode, titles,showinmenu, extraconfig from pages;
insert into pages_metadata_online select code, modelcode, titles,showinmenu, extraconfig from pages;



ALTER TABLE pages  DROP COLUMN showinmenu;
ALTER TABLE pages  DROP COLUMN extraconfig;
ALTER TABLE pages  DROP COLUMN modelcode;
ALTER TABLE pages  DROP COLUMN titles;



##	------	------	------	------	------	porting script for pages end

