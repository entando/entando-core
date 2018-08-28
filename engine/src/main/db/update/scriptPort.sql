-- EN-1748
ALTER TABLE pages_metadata_draft ADD COLUMN groupcode character varying(30);
ALTER TABLE pages_metadata_online ADD COLUMN groupcode character varying(30);
UPDATE pages_metadata_online SET groupcode = (SELECT pages.groupcode FROM pages WHERE pages_metadata_online.code = pages.code);
UPDATE pages_metadata_draft SET groupcode = (SELECT pages.groupcode FROM pages WHERE pages_metadata_draft.code = pages.code);
ALTER TABLE pages DROP groupcode;

-- EN-1858
INSERT INTO sysconfig (version, item, descr, config) VALUES ('production', 'jacms_resourceMetadataMapping', 'Mapping between resource Metadata and resource attribute fields', '<mapping>
    <field key="alt"></field>
    <field key="description"></field>
    <field key="legend"></field>
    <field key="title"></field>
</mapping>');
