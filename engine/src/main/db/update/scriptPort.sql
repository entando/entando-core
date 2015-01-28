ALTER TABLE widgetconfig DROP COLUMN publishedcontent;

UPDATE sysconfig SET config = replace(config, 'jpuserprofile:surname', 'userprofile:surname') WHERE item = 'userProfileTypes';
UPDATE sysconfig SET config = replace(config, 'jpuserprofile:name', 'userprofile:name') WHERE item = 'userProfileTypes';