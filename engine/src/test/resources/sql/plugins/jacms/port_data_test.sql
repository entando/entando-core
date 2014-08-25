INSERT INTO contentmodels (modelid, contenttype, descr, model, stylesheet) VALUES (2, 'ART', 'per test rendering
', '$content.id;
#foreach ($autore in $content.Autori)
$autore.text;
#end
$content.Titolo.getText();
$content.VediAnche.text,$content.VediAnche.destination;
$content.Foto.text,$content.Foto.imagePath("1");
$content.Data.mediumDate;

', NULL);
INSERT INTO contentmodels (modelid, contenttype, descr, model, stylesheet) VALUES (3, 'ART', 'scheda di un articolo', '------ RENDERING CONTENUTO: id = $content.id; ---------
ATTRIBUTI:
  - AUTORI (Monolist-Monotext):
#foreach ($autore in $content.Autori)
         testo=$autore.text;
#end
  - TITOLO (Text): testo=$content.Titolo.getText();
  - VEDI ANCHE (Link): testo=$content.VediAnche.text, dest=$content.VediAnche.destination;
  - FOTO (Image): testo=$content.Foto.text, src(1)=$content.Foto.imagePath("1");
  - DATA (Date): data_media = $content.Data.mediumDate;
------ END ------

', NULL);
INSERT INTO contentmodels (modelid, contenttype, descr, model, stylesheet) VALUES (1, 'ART', 'Main Model', '#if ($content.Titolo.text != "")<h1 class="titolo">$content.Titolo.text</h1>#end
#if ($content.Data.longDate != "")<p>Data: $content.Data.longDate</p>#end
$content.CorpoTesto.getTextBeforeImage(0)
#if ( $content.Foto.imagePath("2") != "" )
<img class="left" src="$content.Foto.imagePath("2")" alt="$content.Foto.text" />
#end
$content.CorpoTesto.getTextAfterImage(0)
#if ($content.Numero.number)<p>Numero: $content.Numero.number</p>#end
#if ($content.Autori && $content.Autori.size() > 0)
<h2 class="titolo">Autori:</h2>
<ul title="Authors">
#foreach ($author in $content.Autori)
	<li>$author.text;</li>
#end
</ul>
#end
#if ($content.VediAnche.text != "")
<h2 class="titolo">Link:</h2>
<p>
<li><a href="$content.VediAnche.destination">$content.VediAnche.text</a></li>
</p>
#end', NULL);
INSERT INTO contentmodels (modelid, contenttype, descr, model, stylesheet) VALUES (11, 'ART', 'List Model', '#if ($content.Titolo.text != "")<h1 class="titolo">$content.Titolo.text</h1>#end
<a href="$content.contentLink">Details...</a>', NULL);




INSERT INTO contents (contentid, contenttype, descr, status, workxml, created, lastmodified, onlinexml, maingroup, currentversion, lasteditor) VALUES ('ART187', 'ART', 'una descrizione particolare', 'DRAFT', '<?xml version="1.0" encoding="UTF-8"?>
<content id="ART187" typecode="ART" typedescr="Articolo rassegna stampa"><descr>una descrizione particolare</descr><groups mainGroup="free" /><categories /><attributes><attribute name="Titolo" attributetype="Text" /><list attributetype="Monolist" name="Autori" nestedtype="Monotext" /><attribute name="VediAnche" attributetype="Link" /><attribute name="CorpoTesto" attributetype="Hypertext" /><attribute name="Foto" attributetype="Image" /><attribute name="Data" attributetype="Date" /><attribute name="Numero" attributetype="Number" /></attributes><status>DRAFT</status></content>
', '20051012164415', '20060622194219', '<?xml version="1.0" encoding="UTF-8"?>
<content id="ART187" typecode="ART" typedescr="Articolo rassegna stampa"><descr>una descrizione particolare</descr><groups mainGroup="free" /><categories /><attributes><attribute name="Titolo" attributetype="Text" /><list attributetype="Monolist" name="Autori" nestedtype="Monotext" /><attribute name="VediAnche" attributetype="Link" /><attribute name="CorpoTesto" attributetype="Hypertext" /><attribute name="Foto" attributetype="Image" /><attribute name="Data" attributetype="Date" /><attribute name="Numero" attributetype="Number" /></attributes><status>DRAFT</status></content>
', 'free', '1.0', 'admin');
INSERT INTO contents (contentid, contenttype, descr, status, workxml, created, lastmodified, onlinexml, maingroup, currentversion, lasteditor) VALUES ('EVN20', 'EVN', 'Mostra zootecnica', 'DRAFT', '<?xml version="1.0" encoding="UTF-8"?>
<content id="EVN20" typecode="EVN" typedescr="Evento"><descr>Mostra zootecnica</descr><groups mainGroup="free" /><categories /><attributes><attribute name="Titolo" attributetype="Text"><text lang="it">Mostra Zootecnica</text></attribute><attribute name="CorpoTesto" attributetype="Hypertext"><hypertext lang="it"><![CDATA[<p>Annuncio svolgimento mostra zootecnicaMostra</p>]]></hypertext></attribute><attribute name="DataInizio" attributetype="Date"><date>20060213</date></attribute><attribute name="DataFine" attributetype="Date"><date>20060220</date></attribute><attribute name="Foto" attributetype="Image" /><list attributetype="Monolist" name="LinkCorrelati" nestedtype="Link" /></attributes><status>DRAFT</status></content>
', '20080209100217', '20080209123357', '<?xml version="1.0" encoding="UTF-8"?>
<content id="EVN20" typecode="EVN" typedescr="Evento"><descr>Mostra zootecnica</descr><groups mainGroup="free" /><categories /><attributes><attribute name="Titolo" attributetype="Text"><text lang="it">Mostra Zootecnica</text></attribute><attribute name="CorpoTesto" attributetype="Hypertext"><hypertext lang="it"><![CDATA[<p>Annuncio svolgimento mostra zootecnicaMostra</p>]]></hypertext></attribute><attribute name="DataInizio" attributetype="Date"><date>20060213</date></attribute><attribute name="DataFine" attributetype="Date"><date>20060220</date></attribute><attribute name="Foto" attributetype="Image" /><list attributetype="Monolist" name="LinkCorrelati" nestedtype="Link" /></attributes><status>DRAFT</status></content>
', 'free', '1.0', 'admin');
INSERT INTO contents (contentid, contenttype, descr, status, workxml, created, lastmodified, onlinexml, maingroup, currentversion, lasteditor) VALUES ('EVN192', 'EVN', 'Evento 2', 'DRAFT', '<?xml version="1.0" encoding="UTF-8"?>
<content id="EVN192" typecode="EVN" typedescr="Evento"><descr>Evento 2</descr><groups mainGroup="free" /><categories><category id="evento" /><category id="general_cat1" /></categories><attributes><attribute name="Titolo" attributetype="Text"><text lang="it">Titolo B - Evento 2</text><text lang="en">Title B - Event 2</text></attribute><attribute name="CorpoTesto" attributetype="Hypertext"><hypertext lang="it"><![CDATA[<p>CorpoTesto evento 2</p>]]></hypertext></attribute><attribute name="DataInizio" attributetype="Date"><date>19990414</date></attribute><attribute name="DataFine" attributetype="Date"><date>19990614</date></attribute><attribute name="Foto" attributetype="Image" /><list attributetype="Monolist" name="LinkCorrelati" nestedtype="Link" /></attributes><status>DRAFT</status></content>
', '20060418142303', '20061221161202', '<?xml version="1.0" encoding="UTF-8"?>
<content id="EVN192" typecode="EVN" typedescr="Evento"><descr>Evento 2</descr><groups mainGroup="free" /><categories><category id="evento" /></categories><attributes><attribute name="Titolo" attributetype="Text"><text lang="it">Titolo B - Evento 2</text><text lang="en">Title B - Event 2</text></attribute><attribute name="CorpoTesto" attributetype="Hypertext"><hypertext lang="it"><![CDATA[<p>CorpoTesto evento 2</p>]]></hypertext></attribute><attribute name="DataInizio" attributetype="Date"><date>19990414</date></attribute><attribute name="DataFine" attributetype="Date"><date>19990614</date></attribute><attribute name="Foto" attributetype="Image" /><list attributetype="Monolist" name="LinkCorrelati" nestedtype="Link" /></attributes><status>DRAFT</status></content>
', 'free', '1.0', 'admin');
INSERT INTO contents (contentid, contenttype, descr, status, workxml, created, lastmodified, onlinexml, maingroup, currentversion, lasteditor) VALUES ('ART179', 'ART', 'una descrizione ON LINE', 'DRAFT', '<?xml version="1.0" encoding="UTF-8"?>
<content id="ART179" typecode="ART" typedescr="Articolo rassegna stampa"><descr>una descrizione ON LINE</descr><groups mainGroup="free" /><categories><category id="general_cat1" /><category id="general_cat2" /></categories><attributes><attribute name="Titolo" attributetype="Text" /><list attributetype="Monolist" name="Autori" nestedtype="Monotext" /><attribute name="VediAnche" attributetype="Link" /><attribute name="CorpoTesto" attributetype="Hypertext" /><attribute name="Foto" attributetype="Image" /><attribute name="Data" attributetype="Date"><date>20090716</date></attribute><attribute name="Numero" attributetype="Number" /></attributes><status>DRAFT</status></content>', '20051012105533', '20080210180714', NULL, 'free', '0.1', 'admin');
INSERT INTO contents (contentid, contenttype, descr, status, workxml, created, lastmodified, onlinexml, maingroup, currentversion, lasteditor) VALUES ('RAH1', 'RAH', 'Articolo', 'DRAFT', '<?xml version="1.0" encoding="UTF-8"?>
<content id="RAH1" typecode="RAH" typedescr="Tipo_Semplice"><descr>Articolo</descr><groups mainGroup="free" /><categories /><attributes><attribute name="Titolo" attributetype="Text"><text lang="it">Un bel titolo</text></attribute><attribute name="CorpoTesto" attributetype="Hypertext"><hypertext lang="it"><![CDATA[test test]]></hypertext></attribute><attribute name="Foto" attributetype="Image" /><attribute name="email" attributetype="Monotext" /><attribute name="Numero" attributetype="Number" /><attribute name="Correlati" attributetype="Link" /><attribute name="Allegati" attributetype="Attach"><resource resourcetype="Attach" id="7" lang="it" /><text lang="it">lop</text><text lang="en">linux</text></attribute><attribute name="Checkbox" attributetype="CheckBox" /></attributes><status>DRAFT</status></content>
', '20050503181212', '20061221161143', '<?xml version="1.0" encoding="UTF-8"?>
<content id="RAH1" typecode="RAH" typedescr="Tipo_Semplice"><descr>Articolo</descr><groups mainGroup="free" /><categories /><attributes><attribute name="Titolo" attributetype="Text"><text lang="it">Un bel titolo</text></attribute><attribute name="CorpoTesto" attributetype="Hypertext"><hypertext lang="it"><![CDATA[test test]]></hypertext></attribute><attribute name="Foto" attributetype="Image" /><attribute name="email" attributetype="Monotext" /><attribute name="Numero" attributetype="Number" /><attribute name="Correlati" attributetype="Link" /><attribute name="Allegati" attributetype="Attach"><resource resourcetype="Attach" id="7" lang="it" /><text lang="it">lop</text><text lang="en">linux</text></attribute><attribute name="Checkbox" attributetype="CheckBox" /></attributes><status>DRAFT</status></content>
', 'free', '1.0', 'admin');
INSERT INTO contents (contentid, contenttype, descr, status, workxml, created, lastmodified, onlinexml, maingroup, currentversion, lasteditor) VALUES ('ART1', 'ART', 'Articolo', 'DRAFT', '<?xml version="1.0" encoding="UTF-8"?>
<content id="ART1" typecode="ART" typedescr="Articolo rassegna stampa"><descr>Articolo</descr><groups mainGroup="free" /><categories /><attributes><attribute name="Titolo" attributetype="Text"><text lang="it">Il titolo</text><text lang="en">The title</text></attribute><list attributetype="Monolist" name="Autori" nestedtype="Monotext"><attribute name="Autori" attributetype="Monotext"><monotext>Pippo</monotext></attribute><attribute name="Autori" attributetype="Monotext"><monotext>Paperino</monotext></attribute><attribute name="Autori" attributetype="Monotext"><monotext>Pluto</monotext></attribute></list><attribute name="VediAnche" attributetype="Link"><link type="external"><urldest>http://www.spiderman.org</urldest></link><text lang="it">Spiderman</text></attribute><attribute name="CorpoTesto" attributetype="Hypertext" /><attribute name="Foto" attributetype="Image"><resource resourcetype="Image" id="44" lang="it" /><text lang="it">Image description</text></attribute><attribute name="Data" attributetype="Date"><date>20040310</date></attribute><attribute name="Numero" attributetype="Number" /></attributes><status>DRAFT</status></content>
', '20050503181212
', '20060622202051', '<?xml version="1.0" encoding="UTF-8"?>
<content id="ART1" typecode="ART" typedescr="Articolo rassegna stampa"><descr>Articolo</descr><groups mainGroup="free" /><categories /><attributes><attribute name="Titolo" attributetype="Text"><text lang="it">Il titolo</text><text lang="en">The title</text></attribute><list attributetype="Monolist" name="Autori" nestedtype="Monotext"><attribute name="Autori" attributetype="Monotext"><monotext>Pippo</monotext></attribute><attribute name="Autori" attributetype="Monotext"><monotext>Paperino</monotext></attribute><attribute name="Autori" attributetype="Monotext"><monotext>Pluto</monotext></attribute></list><attribute name="VediAnche" attributetype="Link"><link type="external"><urldest>http://www.spiderman.org</urldest></link><text lang="it">Spiderman</text></attribute><attribute name="CorpoTesto" attributetype="Hypertext" /><attribute name="Foto" attributetype="Image"><resource resourcetype="Image" id="44" lang="it" /><text lang="it">Image description</text></attribute><attribute name="Data" attributetype="Date"><date>20040310</date></attribute><attribute name="Numero" attributetype="Number" /></attributes><status>DRAFT</status></content>
', 'free', '1.0', 'admin');
INSERT INTO contents (contentid, contenttype, descr, status, workxml, created, lastmodified, onlinexml, maingroup, currentversion, lasteditor) VALUES ('EVN103', 'EVN', 'Contenuto 1 Coach', 'DRAFT', '<?xml version="1.0" encoding="UTF-8"?>
<content id="EVN103" typecode="EVN" typedescr="Evento"><descr>Contenuto 1 Coach</descr><groups mainGroup="coach" /><categories /><attributes><attribute name="Titolo" attributetype="Text"><text lang="it">Titolo Contenuto 1 Coach</text></attribute><attribute name="CorpoTesto" attributetype="Hypertext"><hypertext lang="it"><![CDATA[Corpo Testo Contenuto 1 Coach]]></hypertext></attribute><attribute name="DataInizio" attributetype="Date"><date>19990415</date></attribute><attribute name="DataFine" attributetype="Date"><date>20000414</date></attribute><attribute name="Foto" attributetype="Image" /><list attributetype="Monolist" name="LinkCorrelati" nestedtype="Link" /></attributes><status>DRAFT</status></content>
', '20061221165150', '20061223125859', '<?xml version="1.0" encoding="UTF-8"?>
<content id="EVN103" typecode="EVN" typedescr="Evento"><descr>Contenuto 1 Coach</descr><groups mainGroup="coach" /><categories /><attributes><attribute name="Titolo" attributetype="Text"><text lang="it">Titolo Contenuto 1 Coach</text></attribute><attribute name="CorpoTesto" attributetype="Hypertext"><hypertext lang="it"><![CDATA[Corpo Testo Contenuto 1 Coach]]></hypertext></attribute><attribute name="DataInizio" attributetype="Date"><date>19990415</date></attribute><attribute name="DataFine" attributetype="Date"><date>20000414</date></attribute><attribute name="Foto" attributetype="Image" /><list attributetype="Monolist" name="LinkCorrelati" nestedtype="Link" /></attributes><status>DRAFT</status></content>
', 'coach', '1.0', 'admin');
INSERT INTO contents (contentid, contenttype, descr, status, workxml, created, lastmodified, onlinexml, maingroup, currentversion, lasteditor) VALUES ('RAH101', 'RAH', 'Contenuto 1 Customers', 'DRAFT', '<?xml version="1.0" encoding="UTF-8"?>
<content id="RAH101" typecode="RAH" typedescr="Tipo_Semplice"><descr>Contenuto 1 Customers</descr><groups mainGroup="customers" /><categories /><attributes><attribute name="Titolo" attributetype="Text"><text lang="it">Titolo Contenuto 1 Customers</text></attribute><attribute name="CorpoTesto" attributetype="Hypertext"><hypertext lang="it"><![CDATA[CorpoTesto Contenuto 1 Customers]]></hypertext></attribute><attribute name="Foto" attributetype="Image" /><attribute name="email" attributetype="Monotext" /><attribute name="Numero" attributetype="Number" /><attribute name="Correlati" attributetype="Link" /><attribute name="Allegati" attributetype="Attach" /><attribute name="Checkbox" attributetype="CheckBox" /></attributes><status>DRAFT</status></content>
', '20061221164536', '20061221165755', '<?xml version="1.0" encoding="UTF-8"?>
<content id="RAH101" typecode="RAH" typedescr="Tipo_Semplice"><descr>Contenuto 1 Customers</descr><groups mainGroup="customers" /><categories /><attributes><attribute name="Titolo" attributetype="Text"><text lang="it">Titolo Contenuto 1 Customers</text></attribute><attribute name="CorpoTesto" attributetype="Hypertext"><hypertext lang="it"><![CDATA[CorpoTesto Contenuto 1 Customers]]></hypertext></attribute><attribute name="Foto" attributetype="Image" /><attribute name="email" attributetype="Monotext" /><attribute name="Numero" attributetype="Number" /><attribute name="Correlati" attributetype="Link" /><attribute name="Allegati" attributetype="Attach" /><attribute name="Checkbox" attributetype="CheckBox" /></attributes><status>DRAFT</status></content>
', 'customers', '1.0', 'admin');
INSERT INTO contents (contentid, contenttype, descr, status, workxml, created, lastmodified, onlinexml, maingroup, currentversion, lasteditor) VALUES ('ART104', 'ART', 'Contenuto 2 Coach', 'DRAFT', '<?xml version="1.0" encoding="UTF-8"?>
<content id="ART104" typecode="ART" typedescr="Articolo rassegna stampa"><descr>Contenuto 2 Coach</descr><groups mainGroup="coach" /><categories /><attributes><attribute name="Titolo" attributetype="Text"><text lang="it">Titolo Contenuto 2 Coach</text></attribute><list attributetype="Monolist" name="Autori" nestedtype="Monotext"><attribute name="Autori" attributetype="Monotext"><monotext>Walter</monotext></attribute><attribute name="Autori" attributetype="Monotext"><monotext>Marco</monotext></attribute><attribute name="Autori" attributetype="Monotext"><monotext>Eugenio</monotext></attribute><attribute name="Autori" attributetype="Monotext"><monotext>William</monotext></attribute></list><attribute name="VediAnche" attributetype="Link"><link type="external"><urldest>http://www.japsportal.org</urldest></link><text lang="it">Home jAPS</text></attribute><attribute name="CorpoTesto" attributetype="Hypertext"><hypertext lang="it"><![CDATA[Corpo Testo Contenuto&nbsp;2 Coach]]></hypertext></attribute><attribute name="Foto" attributetype="Image" /><attribute name="Data" attributetype="Date"><date>20070104</date></attribute><attribute name="Numero" attributetype="Number" /></attributes><status>DRAFT</status></content>
', '20061221165750', '20070103143539', '<?xml version="1.0" encoding="UTF-8"?>
<content id="ART104" typecode="ART" typedescr="Articolo rassegna stampa"><descr>Contenuto 2 Coach</descr><groups mainGroup="coach" /><categories /><attributes><attribute name="Titolo" attributetype="Text"><text lang="it">Titolo Contenuto 2 Coach</text></attribute><list attributetype="Monolist" name="Autori" nestedtype="Monotext"><attribute name="Autori" attributetype="Monotext"><monotext>Walter</monotext></attribute><attribute name="Autori" attributetype="Monotext"><monotext>Marco</monotext></attribute><attribute name="Autori" attributetype="Monotext"><monotext>Eugenio</monotext></attribute><attribute name="Autori" attributetype="Monotext"><monotext>William</monotext></attribute></list><attribute name="VediAnche" attributetype="Link"><link type="external"><urldest>http://www.japsportal.org</urldest></link><text lang="it">Home jAPS</text></attribute><attribute name="CorpoTesto" attributetype="Hypertext"><hypertext lang="it"><![CDATA[Corpo Testo Contenuto&nbsp;2 Coach]]></hypertext></attribute><attribute name="Foto" attributetype="Image" /><attribute name="Data" attributetype="Date"><date>20070104</date></attribute><attribute name="Numero" attributetype="Number" /></attributes><status>DRAFT</status></content>
', 'coach', '1.0', 'admin');
INSERT INTO contents (contentid, contenttype, descr, status, workxml, created, lastmodified, onlinexml, maingroup, currentversion, lasteditor) VALUES ('EVN23', 'EVN', 'Collezione Ingrao', 'DRAFT', '<?xml version="1.0" encoding="UTF-8"?>
<content id="EVN23" typecode="EVN" typedescr="Evento"><descr>Collezione Ingrao</descr><groups mainGroup="free" /><categories /><attributes><attribute name="Titolo" attributetype="Text"><text lang="it">Collezione Ingri</text></attribute><attribute name="CorpoTesto" attributetype="Hypertext"><hypertext lang="it"><![CDATA[Nei rinnovati spazi della Galleria Comunale d''Arte sono ospitate le opere pittoriche e scultoree, quelle che dell''intero lascito di Giovanni Ingri rientrano nel periodo moderno e contemporaneo.]]></hypertext></attribute><attribute name="DataInizio" attributetype="Date"><date>20080213</date></attribute><attribute name="DataFine" attributetype="Date"><date>20080222</date></attribute><attribute name="Foto" attributetype="Image" /><list attributetype="Monolist" name="LinkCorrelati" nestedtype="Link" /></attributes><status>DRAFT</status></content>
', '20080209100541', '20080209100546', '<?xml version="1.0" encoding="UTF-8"?>
<content id="EVN23" typecode="EVN" typedescr="Evento"><descr>Collezione Ingrao</descr><groups mainGroup="free" /><categories /><attributes><attribute name="Titolo" attributetype="Text"><text lang="it">Collezione Ingri</text></attribute><attribute name="CorpoTesto" attributetype="Hypertext"><hypertext lang="it"><![CDATA[Nei rinnovati spazi della Galleria Comunale d''Arte sono ospitate le opere pittoriche e scultoree, quelle che dell''intero lascito di Giovanni Ingri rientrano nel periodo moderno e contemporaneo.]]></hypertext></attribute><attribute name="DataInizio" attributetype="Date"><date>20080213</date></attribute><attribute name="DataFine" attributetype="Date"><date>20080222</date></attribute><attribute name="Foto" attributetype="Image" /><list attributetype="Monolist" name="LinkCorrelati" nestedtype="Link" /></attributes><status>DRAFT</status></content>
', 'free', '1.0', 'admin');
INSERT INTO contents (contentid, contenttype, descr, status, workxml, created, lastmodified, onlinexml, maingroup, currentversion, lasteditor) VALUES ('EVN24', 'EVN', 'Castello dei bambini', 'DRAFT', '<?xml version="1.0" encoding="UTF-8"?>
<content id="EVN24" typecode="EVN" typedescr="Evento"><descr>Castello dei bambini</descr><groups mainGroup="free" /><categories /><attributes><attribute name="Titolo" attributetype="Text"><text lang="it">Castello dei bambini</text></attribute><attribute name="CorpoTesto" attributetype="Hypertext"><hypertext lang="it"><![CDATA[<p>Descrizion evento Castello dei bambini</p>]]></hypertext></attribute><attribute name="DataInizio" attributetype="Date"><date>20090318</date></attribute><attribute name="DataFine" attributetype="Date"><date>20090326</date></attribute><attribute name="Foto" attributetype="Image" /><list attributetype="Monolist" name="LinkCorrelati" nestedtype="Link" /></attributes><status>DRAFT</status></content>
', '20080209100714', '20080209100719', '<?xml version="1.0" encoding="UTF-8"?>
<content id="EVN24" typecode="EVN" typedescr="Evento"><descr>Castello dei bambini</descr><groups mainGroup="free" /><categories /><attributes><attribute name="Titolo" attributetype="Text"><text lang="it">Castello dei bambini</text></attribute><attribute name="CorpoTesto" attributetype="Hypertext"><hypertext lang="it"><![CDATA[<p>Descrizion evento Castello dei bambini</p>]]></hypertext></attribute><attribute name="DataInizio" attributetype="Date"><date>20090318</date></attribute><attribute name="DataFine" attributetype="Date"><date>20090326</date></attribute><attribute name="Foto" attributetype="Image" /><list attributetype="Monolist" name="LinkCorrelati" nestedtype="Link" /></attributes><status>DRAFT</status></content>
', 'free', '1.0', 'admin');
INSERT INTO contents (contentid, contenttype, descr, status, workxml, created, lastmodified, onlinexml, maingroup, currentversion, lasteditor) VALUES ('EVN41', 'EVN', 'Mostra della ciliegia', 'DRAFT', '<?xml version="1.0" encoding="UTF-8"?>
<content id="EVN41" typecode="EVN" typedescr="Evento"><descr>Mostra della ciliegia</descr><groups mainGroup="coach" /><categories /><attributes><attribute name="Titolo" attributetype="Text"><text lang="it">Sagra della ciliegia</text></attribute><attribute name="CorpoTesto" attributetype="Hypertext"><hypertext lang="it"><![CDATA[<p>Sagra della ciliegia</p>]]></hypertext></attribute><attribute name="DataInizio" attributetype="Date"><date>20080106</date></attribute><attribute name="DataFine" attributetype="Date"><date>20080124</date></attribute><attribute name="Foto" attributetype="Image" /><list attributetype="Monolist" name="LinkCorrelati" nestedtype="Link" /></attributes><status>DRAFT</status></content>
', '20080209102901', '20080209102903', '<?xml version="1.0" encoding="UTF-8"?>
<content id="EVN41" typecode="EVN" typedescr="Evento"><descr>Mostra della ciliegia</descr><groups mainGroup="coach" /><categories /><attributes><attribute name="Titolo" attributetype="Text"><text lang="it">Sagra della ciliegia</text></attribute><attribute name="CorpoTesto" attributetype="Hypertext"><hypertext lang="it"><![CDATA[<p>Sagra della ciliegia</p>]]></hypertext></attribute><attribute name="DataInizio" attributetype="Date"><date>20080106</date></attribute><attribute name="DataFine" attributetype="Date"><date>20080124</date></attribute><attribute name="Foto" attributetype="Image" /><list attributetype="Monolist" name="LinkCorrelati" nestedtype="Link" /></attributes><status>DRAFT</status></content>
', 'coach', '1.0', 'admin');
INSERT INTO contents (contentid, contenttype, descr, status, workxml, created, lastmodified, onlinexml, maingroup, currentversion, lasteditor) VALUES ('EVN21', 'EVN', 'Sagra delle fragole', 'DRAFT', '<?xml version="1.0" encoding="UTF-8"?>
<content id="EVN21" typecode="EVN" typedescr="Evento"><descr>Sagra delle fragole</descr><groups mainGroup="free" /><categories /><attributes><attribute name="Titolo" attributetype="Text"><text lang="it">Mostra Delle Fragole</text></attribute><attribute name="CorpoTesto" attributetype="Hypertext"><hypertext lang="it"><![CDATA[<p>Mostre delle fragole</p>]]></hypertext></attribute><attribute name="DataInizio" attributetype="Date"><date>20060113</date></attribute><attribute name="DataFine" attributetype="Date"><date>20060304</date></attribute><attribute name="Foto" attributetype="Image" /><list attributetype="Monolist" name="LinkCorrelati" nestedtype="Link" /></attributes><status>DRAFT</status></content>
', '20080209123547', '20080209123637', '<?xml version="1.0" encoding="UTF-8"?>
<content id="EVN21" typecode="EVN" typedescr="Evento"><descr>Sagra delle fragole</descr><groups mainGroup="free" /><categories /><attributes><attribute name="Titolo" attributetype="Text"><text lang="it">Mostra Delle Fragole</text></attribute><attribute name="CorpoTesto" attributetype="Hypertext"><hypertext lang="it"><![CDATA[<p>Mostre delle fragole</p>]]></hypertext></attribute><attribute name="DataInizio" attributetype="Date"><date>20060113</date></attribute><attribute name="DataFine" attributetype="Date"><date>20060304</date></attribute><attribute name="Foto" attributetype="Image" /><list attributetype="Monolist" name="LinkCorrelati" nestedtype="Link" /></attributes><status>DRAFT</status></content>
', 'free', '1.0', 'admin');
INSERT INTO contents (contentid, contenttype, descr, status, workxml, created, lastmodified, onlinexml, maingroup, currentversion, lasteditor) VALUES ('EVN25', 'EVN', 'TEATRO DELLE MERAVIGLIE', 'DRAFT', '<?xml version="1.0" encoding="UTF-8"?>
<content id="EVN25" typecode="EVN" typedescr="Evento"><descr>TEATRO DELLE MERAVIGLIE</descr><groups mainGroup="coach"><group name="free" /></groups><categories /><attributes><attribute name="Titolo" attributetype="Text"><text lang="it">TEATRO DELLE MERAVIGLIE</text></attribute><attribute name="CorpoTesto" attributetype="Hypertext"><hypertext lang="it"><![CDATA[<p>TEATRO DELLE MERAVIGLIE  Laboratori Creativi</p>]]></hypertext></attribute><attribute name="DataInizio" attributetype="Date"><date>20071212</date></attribute><attribute name="DataFine" attributetype="Date"><date>20071222</date></attribute><attribute name="Foto" attributetype="Image" /><list attributetype="Monolist" name="LinkCorrelati" nestedtype="Link" /></attributes><status>DRAFT</status></content>
', '20080209100902', '20080209100915', '<?xml version="1.0" encoding="UTF-8"?>
<content id="EVN25" typecode="EVN" typedescr="Evento"><descr>TEATRO DELLE MERAVIGLIE</descr><groups mainGroup="coach"><group name="free" /></groups><categories /><attributes><attribute name="Titolo" attributetype="Text"><text lang="it">TEATRO DELLE MERAVIGLIE</text></attribute><attribute name="CorpoTesto" attributetype="Hypertext"><hypertext lang="it"><![CDATA[<p>TEATRO DELLE MERAVIGLIE  Laboratori Creativi</p>]]></hypertext></attribute><attribute name="DataInizio" attributetype="Date"><date>20071212</date></attribute><attribute name="DataFine" attributetype="Date"><date>20071222</date></attribute><attribute name="Foto" attributetype="Image" /><list attributetype="Monolist" name="LinkCorrelati" nestedtype="Link" /></attributes><status>DRAFT</status></content>
', 'coach', '1.0', 'admin');
INSERT INTO contents (contentid, contenttype, descr, status, workxml, created, lastmodified, onlinexml, maingroup, currentversion, lasteditor) VALUES ('EVN191', 'EVN', 'Evento 1', 'DRAFT', '<?xml version="1.0" encoding="UTF-8"?>
<content id="EVN191" typecode="EVN" typedescr="Evento"><descr>Evento 1</descr><groups mainGroup="free" /><categories /><attributes><attribute name="Titolo" attributetype="Text"><text lang="it">Titolo A - Evento 1</text><text lang="en">Title C - Event 1</text></attribute><attribute name="CorpoTesto" attributetype="Hypertext"><hypertext lang="it"><![CDATA[<p>CorpoTesto Evento 1</p>]]></hypertext></attribute><attribute name="DataInizio" attributetype="Date"><date>19960417</date></attribute><attribute name="DataFine" attributetype="Date"><date>19960617</date></attribute><attribute name="Foto" attributetype="Image" /><list attributetype="Monolist" name="LinkCorrelati" nestedtype="Link" /></attributes><status>DRAFT</status></content>
', '20060418142200', '20061221161157', '<?xml version="1.0" encoding="UTF-8"?>
<content id="EVN191" typecode="EVN" typedescr="Evento"><descr>Evento 1</descr><groups mainGroup="free" /><categories /><attributes><attribute name="Titolo" attributetype="Text"><text lang="it">Titolo A - Evento 1</text><text lang="en">Title C - Event 1</text></attribute><attribute name="CorpoTesto" attributetype="Hypertext"><hypertext lang="it"><![CDATA[<p>CorpoTesto Evento 1</p>]]></hypertext></attribute><attribute name="DataInizio" attributetype="Date"><date>19960417</date></attribute><attribute name="DataFine" attributetype="Date"><date>19960617</date></attribute><attribute name="Foto" attributetype="Image" /><list attributetype="Monolist" name="LinkCorrelati" nestedtype="Link" /></attributes><status>DRAFT</status></content>
', 'free', '1.0', 'admin');
INSERT INTO contents (contentid, contenttype, descr, status, workxml, created, lastmodified, onlinexml, maingroup, currentversion, lasteditor) VALUES ('EVN194', 'EVN', 'Evento 4', 'DRAFT', '<?xml version="1.0" encoding="UTF-8"?>
<content id="EVN194" typecode="EVN" typedescr="Evento"><descr>Evento 4</descr><groups mainGroup="free" /><categories /><attributes><attribute name="Titolo" attributetype="Text"><text lang="it">Titolo D - Evento 4</text><text lang="en">Title A - Event 4</text></attribute><attribute name="CorpoTesto" attributetype="Hypertext"><hypertext lang="it"><![CDATA[<p>CorpoTesto&nbsp;Evento 4</p>]]></hypertext></attribute><attribute name="DataInizio" attributetype="Date"><date>20220219</date></attribute><attribute name="DataFine" attributetype="Date"><date>20220419</date></attribute><attribute name="Foto" attributetype="Image" /><list attributetype="Monolist" name="LinkCorrelati" nestedtype="Link"><attribute name="LinkCorrelati" attributetype="Link"><link type="content"><contentdest>ART1</contentdest></link><text lang="it">Link 1</text></attribute><attribute name="LinkCorrelati" attributetype="Link"><link type="page"><pagedest>pagina_11</pagedest></link><text lang="it">Link 2</text></attribute></list></attributes><status>DRAFT</status></content>
', '20060418142507', '20061221161128', '<?xml version="1.0" encoding="UTF-8"?>
<content id="EVN194" typecode="EVN" typedescr="Evento"><descr>Evento 4</descr><groups mainGroup="free" /><categories /><attributes><attribute name="Titolo" attributetype="Text"><text lang="it">Titolo D - Evento 4</text><text lang="en">Title A - Event 4</text></attribute><attribute name="CorpoTesto" attributetype="Hypertext"><hypertext lang="it"><![CDATA[<p>CorpoTesto&nbsp;Evento 4</p>]]></hypertext></attribute><attribute name="DataInizio" attributetype="Date"><date>20220219</date></attribute><attribute name="DataFine" attributetype="Date"><date>20220419</date></attribute><attribute name="Foto" attributetype="Image" /><list attributetype="Monolist" name="LinkCorrelati" nestedtype="Link"><attribute name="LinkCorrelati" attributetype="Link"><link type="content"><contentdest>ART1</contentdest></link><text lang="it">Link 1</text></attribute><attribute name="LinkCorrelati" attributetype="Link"><link type="page"><pagedest>pagina_11</pagedest></link><text lang="it">Link 2</text></attribute></list></attributes><status>DRAFT</status></content>
', 'free', '1.0', 'admin');
INSERT INTO contents (contentid, contenttype, descr, status, workxml, created, lastmodified, onlinexml, maingroup, currentversion, lasteditor) VALUES ('ART112', 'ART', 'Contenuto 4 Coach', 'DRAFT', '<?xml version="1.0" encoding="UTF-8"?>
<content id="ART112" typecode="ART" typedescr="Articolo rassegna stampa"><descr>Contenuto 4 Coach</descr><groups mainGroup="coach"><group name="customers" /><group name="helpdesk" /></groups><categories><category id="general_cat2" /></categories><attributes><attribute name="Titolo" attributetype="Text"><text lang="it">Titolo Contenuto 4 Coach</text></attribute><list attributetype="Monolist" name="Autori" nestedtype="Monotext"><attribute name="Autori" attributetype="Monotext"><monotext>Walter</monotext></attribute><attribute name="Autori" attributetype="Monotext"><monotext>Marco</monotext></attribute><attribute name="Autori" attributetype="Monotext"><monotext>Eugenio</monotext></attribute><attribute name="Autori" attributetype="Monotext"><monotext>William</monotext></attribute></list><attribute name="VediAnche" attributetype="Link" /><attribute name="CorpoTesto" attributetype="Hypertext"><hypertext lang="it"><![CDATA[<p>
	Corpo Testo Contenuto 4 Coach</p>
]]></hypertext></attribute><attribute name="Foto" attributetype="Image" /><attribute name="Data" attributetype="Date"><date>20060213</date></attribute><attribute name="Numero" attributetype="Number" /></attributes><status>DRAFT</status><version>2.2</version><lastEditor>admin</lastEditor><created>20071216174627</created><lastModified>20120803224723</lastModified></content>
', '20071216174627', '20120803224723', '<?xml version="1.0" encoding="UTF-8"?>
<content id="ART112" typecode="ART" typedescr="Articolo rassegna stampa"><descr>Contenuto 4 Coach</descr><groups mainGroup="coach"><group name="customers" /><group name="helpdesk" /></groups><categories /><attributes><attribute name="Titolo" attributetype="Text"><text lang="it">Titolo Contenuto 4 Coach</text></attribute><list attributetype="Monolist" name="Autori" nestedtype="Monotext"><attribute name="Autori" attributetype="Monotext"><monotext>Walter</monotext></attribute><attribute name="Autori" attributetype="Monotext"><monotext>Marco</monotext></attribute><attribute name="Autori" attributetype="Monotext"><monotext>Eugenio</monotext></attribute><attribute name="Autori" attributetype="Monotext"><monotext>William</monotext></attribute></list><attribute name="VediAnche" attributetype="Link" /><attribute name="CorpoTesto" attributetype="Hypertext"><hypertext lang="it"><![CDATA[<p>
	Corpo Testo Contenuto 4 Coach</p>
]]></hypertext></attribute><attribute name="Foto" attributetype="Image" /><attribute name="Data" attributetype="Date"><date>20060213</date></attribute><attribute name="Numero" attributetype="Number" /></attributes><status>PUBLIC</status><version>2.0</version><lastEditor>admin</lastEditor><created>20071216174627</created><lastModified>20120803222829</lastModified></content>
', 'coach', '2.2', 'admin');
INSERT INTO contents (contentid, contenttype, descr, status, workxml, created, lastmodified, onlinexml, maingroup, currentversion, lasteditor) VALUES ('ART180', 'ART', 'una descrizione', 'READY', '<?xml version="1.0" encoding="UTF-8"?>
<content id="ART180" typecode="ART" typedescr="Articolo rassegna stampa"><descr>una descrizione</descr><groups mainGroup="free" /><categories><category id="cat1" /><category id="general_cat1" /></categories><attributes><attribute name="Titolo" attributetype="Text" /><list attributetype="Monolist" name="Autori" nestedtype="Monotext" /><attribute name="VediAnche" attributetype="Link" /><attribute name="CorpoTesto" attributetype="Hypertext" /><attribute name="Foto" attributetype="Image"><resource resourcetype="Image" id="44" lang="it" /><text lang="it">Descrizione foto</text></attribute><attribute name="Data" attributetype="Date" /><attribute name="Numero" attributetype="Number" /></attributes><status>READY</status></content>
', '20051012105757', '20061221161136', '<?xml version="1.0" encoding="UTF-8"?>
<content id="ART180" typecode="ART" typedescr="Articolo rassegna stampa"><descr>una descrizione</descr><groups mainGroup="free" /><categories><category id="cat1" /></categories><attributes><attribute name="Titolo" attributetype="Text" /><list attributetype="Monolist" name="Autori" nestedtype="Monotext" /><attribute name="VediAnche" attributetype="Link" /><attribute name="CorpoTesto" attributetype="Hypertext" /><attribute name="Foto" attributetype="Image"><resource resourcetype="Image" id="44" lang="it" /><text lang="it">Descrizione foto</text></attribute><attribute name="Data" attributetype="Date" /><attribute name="Numero" attributetype="Number" /></attributes><status>READY</status></content>
', 'free', '1.0', 'admin');
INSERT INTO contents (contentid, contenttype, descr, status, workxml, created, lastmodified, onlinexml, maingroup, currentversion, lasteditor) VALUES ('ART102', 'ART', 'Contenuto 2 Customers', 'DRAFT', '<?xml version="1.0" encoding="UTF-8"?>
<content id="ART102" typecode="ART" typedescr="Articolo rassegna stampa"><descr>Contenuto 2 Customers</descr><groups mainGroup="customers" /><categories><category id="general_cat1" /></categories><attributes><attribute name="Titolo" attributetype="Text"><text lang="it">Titolo Contenuto 2 Customers</text></attribute><list attributetype="Monolist" name="Autori" nestedtype="Monotext" /><attribute name="VediAnche" attributetype="Link"><link type="content"><contentdest>ART111</contentdest></link><text lang="it">Contenuto autorizzato Gruppo Coath</text></attribute><attribute name="CorpoTesto" attributetype="Hypertext"><hypertext lang="it"><![CDATA[<p>Corpo Testo Contenuto&nbsp;2 Customers</p>]]></hypertext></attribute><attribute name="Foto" attributetype="Image" /><attribute name="Data" attributetype="Date" /><attribute name="Numero" attributetype="Number" /></attributes><status>DRAFT</status></content>
', '20061221164629', '20071215174925', '<?xml version="1.0" encoding="UTF-8"?>
<content id="ART102" typecode="ART" typedescr="Articolo rassegna stampa"><descr>Contenuto 2 Customers</descr><groups mainGroup="customers" /><categories><category id="general_cat1" /></categories><attributes><attribute name="Titolo" attributetype="Text"><text lang="it">Titolo Contenuto 2 Customers</text></attribute><list attributetype="Monolist" name="Autori" nestedtype="Monotext" /><attribute name="VediAnche" attributetype="Link"><link type="content"><contentdest>ART111</contentdest></link><text lang="it">Contenuto autorizzato Gruppo Coath</text></attribute><attribute name="CorpoTesto" attributetype="Hypertext"><hypertext lang="it"><![CDATA[<p>Corpo Testo Contenuto&nbsp;2 Customers</p>]]></hypertext></attribute><attribute name="Foto" attributetype="Image" /><attribute name="Data" attributetype="Date" /><attribute name="Numero" attributetype="Number" /></attributes><status>DRAFT</status></content>
', 'customers', '1.0', 'admin');
INSERT INTO contents (contentid, contenttype, descr, status, workxml, created, lastmodified, onlinexml, maingroup, currentversion, lasteditor) VALUES ('EVN193', 'EVN', 'Evento 3', 'DRAFT', '<?xml version="1.0" encoding="UTF-8"?>
<content id="EVN193" typecode="EVN" typedescr="Evento"><descr>Evento 3</descr><groups mainGroup="free" /><categories><category id="evento" /><category id="general_cat2" /></categories><attributes><attribute name="Titolo" attributetype="Text"><text lang="it">Titolo C - Evento 3</text><text lang="en">Title D - Evento 3</text></attribute><attribute name="CorpoTesto" attributetype="Hypertext"><hypertext lang="it"><![CDATA[<p>CorpoTesto Evento 3</p>]]></hypertext></attribute><attribute name="DataInizio" attributetype="Date"><date>20170412</date></attribute><attribute name="DataFine" attributetype="Date"><date>20170912</date></attribute><attribute name="Foto" attributetype="Image" /><list attributetype="Monolist" name="LinkCorrelati" nestedtype="Link"><attribute name="LinkCorrelati" attributetype="Link"><link type="content"><contentdest>ART1</contentdest></link><text lang="it">Link 1</text></attribute><attribute name="LinkCorrelati" attributetype="Link"><link type="page"><pagedest>pagina_11</pagedest></link><text lang="it">Link 2</text></attribute></list></attributes><status>DRAFT</status></content>
', '20060418142409', '20061221161125', '<?xml version="1.0" encoding="UTF-8"?>
<content id="EVN193" typecode="EVN" typedescr="Evento"><descr>Evento 3</descr><groups mainGroup="free" /><categories><category id="evento" /></categories><attributes><attribute name="Titolo" attributetype="Text"><text lang="it">Titolo C - Evento 3</text><text lang="en">Title D - Evento 3</text></attribute><attribute name="CorpoTesto" attributetype="Hypertext"><hypertext lang="it"><![CDATA[<p>CorpoTesto Evento 3</p>]]></hypertext></attribute><attribute name="DataInizio" attributetype="Date"><date>20170412</date></attribute><attribute name="DataFine" attributetype="Date"><date>20170912</date></attribute><attribute name="Foto" attributetype="Image" /><list attributetype="Monolist" name="LinkCorrelati" nestedtype="Link"><attribute name="LinkCorrelati" attributetype="Link"><link type="content"><contentdest>ART1</contentdest></link><text lang="it">Link 1</text></attribute><attribute name="LinkCorrelati" attributetype="Link"><link type="page"><pagedest>pagina_11</pagedest></link><text lang="it">Link 2</text></attribute></list></attributes><status>DRAFT</status></content>
', 'free', '1.0', 'admin');
INSERT INTO contents (contentid, contenttype, descr, status, workxml, created, lastmodified, onlinexml, maingroup, currentversion, lasteditor) VALUES ('ART111', 'ART', 'Contenuto 3 Coach', 'PUBLIC', '<?xml version="1.0" encoding="UTF-8"?>
<content id="ART111" typecode="ART" typedescr="Articolo rassegna stampa"><descr>Contenuto 3 Coach</descr><groups mainGroup="coach"><group name="customers" /><group name="helpdesk" /></groups><categories><category id="general_cat1" /><category id="general_cat2" /></categories><attributes><attribute name="Titolo" attributetype="Text"><text lang="it">Titolo Contenuto 3 Coach</text></attribute><list attributetype="Monolist" name="Autori" nestedtype="Monotext"><attribute name="Autori" attributetype="Monotext"><monotext>Walter</monotext></attribute><attribute name="Autori" attributetype="Monotext"><monotext>Marco</monotext></attribute><attribute name="Autori" attributetype="Monotext"><monotext>Eugenio</monotext></attribute><attribute name="Autori" attributetype="Monotext"><monotext>William</monotext></attribute></list><attribute name="VediAnche" attributetype="Link" /><attribute name="CorpoTesto" attributetype="Hypertext"><hypertext lang="it"><![CDATA[<p>Corpo Testo Contenuto 3 Coach</p>
]]></hypertext></attribute><attribute name="Foto" attributetype="Image" /><attribute name="Data" attributetype="Date"><date>20061213</date></attribute><attribute name="Numero" attributetype="Number" /></attributes><status>PUBLIC</status><version>4.0</version><lastEditor>admin</lastEditor><created>20071215174627</created><lastModified>20120803222621</lastModified></content>
', '20071215174627', '20120803222621', '<?xml version="1.0" encoding="UTF-8"?>
<content id="ART111" typecode="ART" typedescr="Articolo rassegna stampa"><descr>Contenuto 3 Coach</descr><groups mainGroup="coach"><group name="customers" /><group name="helpdesk" /></groups><categories><category id="general_cat1" /><category id="general_cat2" /></categories><attributes><attribute name="Titolo" attributetype="Text"><text lang="it">Titolo Contenuto 3 Coach</text></attribute><list attributetype="Monolist" name="Autori" nestedtype="Monotext"><attribute name="Autori" attributetype="Monotext"><monotext>Walter</monotext></attribute><attribute name="Autori" attributetype="Monotext"><monotext>Marco</monotext></attribute><attribute name="Autori" attributetype="Monotext"><monotext>Eugenio</monotext></attribute><attribute name="Autori" attributetype="Monotext"><monotext>William</monotext></attribute></list><attribute name="VediAnche" attributetype="Link" /><attribute name="CorpoTesto" attributetype="Hypertext"><hypertext lang="it"><![CDATA[<p>Corpo Testo Contenuto 3 Coach</p>
]]></hypertext></attribute><attribute name="Foto" attributetype="Image" /><attribute name="Data" attributetype="Date"><date>20061213</date></attribute><attribute name="Numero" attributetype="Number" /></attributes><status>PUBLIC</status><version>4.0</version><lastEditor>admin</lastEditor><created>20071215174627</created><lastModified>20120803222621</lastModified></content>
', 'coach', '4.0', 'admin');
INSERT INTO contents (contentid, contenttype, descr, status, workxml, created, lastmodified, onlinexml, maingroup, currentversion, lasteditor) VALUES ('ART120', 'ART', 'Contenuto degli amministratori 1', 'PUBLIC', '<?xml version="1.0" encoding="UTF-8"?>
<content id="ART120" typecode="ART" typedescr="Articolo rassegna stampa"><descr>Contenuto degli amministratori 1</descr><groups mainGroup="administrators" /><categories><category id="general_cat2" /><category id="general_cat3" /></categories><attributes><attribute name="Titolo" attributetype="Text"><text lang="it">Titolo Contenuto degli "Amministratori"</text><text lang="en">Title of Administrator''s Content</text></attribute><list attributetype="Monolist" name="Autori" nestedtype="Monotext" /><attribute name="VediAnche" attributetype="Link"><link type="external"><urldest>http://www.japsportal.org</urldest></link><text lang="it">Pagina Iniziale jAPSPortal</text><text lang="en">jAPSPortal HomePage</text></attribute><attribute name="CorpoTesto" attributetype="Hypertext"><hypertext lang="it"><![CDATA[<p>
	Testo primo contenuto del gruppo degli Amministratori</p>
]]></hypertext><hypertext lang="en"><![CDATA[<p>
	Text of first Administrators Group&#39;s Content</p>
]]></hypertext></attribute><attribute name="Foto" attributetype="Image" /><attribute name="Data" attributetype="Date"><date>20090328</date></attribute><attribute name="Numero" attributetype="Number"><number>7</number></attribute></attributes><status>PUBLIC</status><version>1.0</version><lastEditor>admin</lastEditor><created>20080721125725</created><lastModified>20120803222703</lastModified></content>
', '20080721125725', '20120803222703', '<?xml version="1.0" encoding="UTF-8"?>
<content id="ART120" typecode="ART" typedescr="Articolo rassegna stampa"><descr>Contenuto degli amministratori 1</descr><groups mainGroup="administrators" /><categories><category id="general_cat2" /><category id="general_cat3" /></categories><attributes><attribute name="Titolo" attributetype="Text"><text lang="it">Titolo Contenuto degli "Amministratori"</text><text lang="en">Title of Administrator''s Content</text></attribute><list attributetype="Monolist" name="Autori" nestedtype="Monotext" /><attribute name="VediAnche" attributetype="Link"><link type="external"><urldest>http://www.japsportal.org</urldest></link><text lang="it">Pagina Iniziale jAPSPortal</text><text lang="en">jAPSPortal HomePage</text></attribute><attribute name="CorpoTesto" attributetype="Hypertext"><hypertext lang="it"><![CDATA[<p>
	Testo primo contenuto del gruppo degli Amministratori</p>
]]></hypertext><hypertext lang="en"><![CDATA[<p>
	Text of first Administrators Group&#39;s Content</p>
]]></hypertext></attribute><attribute name="Foto" attributetype="Image" /><attribute name="Data" attributetype="Date"><date>20090328</date></attribute><attribute name="Numero" attributetype="Number"><number>7</number></attribute></attributes><status>PUBLIC</status><version>1.0</version><lastEditor>admin</lastEditor><created>20080721125725</created><lastModified>20120803222703</lastModified></content>
', 'administrators', '1.0', 'admin');
INSERT INTO contents (contentid, contenttype, descr, status, workxml, created, lastmodified, onlinexml, maingroup, currentversion, lasteditor) VALUES ('ART121', 'ART', 'Contenuto degli amministratori 2', 'DRAFT', '<?xml version="1.0" encoding="UTF-8"?>
<content id="ART121" typecode="ART" typedescr="Articolo rassegna stampa"><descr>Contenuto degli amministratori 2</descr><groups mainGroup="administrators"><group name="free" /></groups><categories><category id="general_cat3" /></categories><attributes><attribute name="Titolo" attributetype="Text"><text lang="it">Titolo Contenuto degli "Amministratori" 2</text><text lang="en">Title of Administrator''s Content &lt;2&gt;</text></attribute><list attributetype="Monolist" name="Autori" nestedtype="Monotext" /><attribute name="VediAnche" attributetype="Link"><link type="external"><urldest>http://www.w3.org/</urldest></link><text lang="it">Pagina Iniziale W3C</text><text lang="en">World Wide Web Consortium - Web Standards</text></attribute><attribute name="CorpoTesto" attributetype="Hypertext"><hypertext lang="it"><![CDATA[<p>
	Testo secondo contenuto del gruppo degli Amministratori</p>
<p>
	Questo contenuto appartiene al Gruppo degli Amministratori ma dichiarato visibile agli utenti del gruppo del gruppo free.</p>
]]></hypertext><hypertext lang="en"><![CDATA[<p>
	Text of second Administrators Group&#39;s Content</p>
<p>
	This content belongs to the Administrators Group was declared visible to guest users.</p>
]]></hypertext></attribute><attribute name="Foto" attributetype="Image" /><attribute name="Data" attributetype="Date"><date>20090330</date></attribute><attribute name="Numero" attributetype="Number"><number>78</number></attribute></attributes><status>DRAFT</status><version>2.1</version><lastEditor>admin</lastEditor><created>20080721143834</created><lastModified>20120803222911</lastModified></content>
', '20080721143834', '20120803222911', '<?xml version="1.0" encoding="UTF-8"?>
<content id="ART121" typecode="ART" typedescr="Articolo rassegna stampa"><descr>Contenuto degli amministratori 2</descr><groups mainGroup="administrators"><group name="free" /></groups><categories /><attributes><attribute name="Titolo" attributetype="Text"><text lang="it">Titolo Contenuto degli "Amministratori" 2</text><text lang="en">Title of Administrator''s Content &lt;2&gt;</text></attribute><list attributetype="Monolist" name="Autori" nestedtype="Monotext" /><attribute name="VediAnche" attributetype="Link"><link type="external"><urldest>http://www.w3.org/</urldest></link><text lang="it">Pagina Iniziale W3C</text><text lang="en">World Wide Web Consortium - Web Standards</text></attribute><attribute name="CorpoTesto" attributetype="Hypertext"><hypertext lang="it"><![CDATA[<p>
	Testo secondo contenuto del gruppo degli Amministratori</p>
<p>
	Questo contenuto appartiene al Gruppo degli Amministratori ma dichiarato visibile agli utenti del gruppo del gruppo free.</p>
]]></hypertext><hypertext lang="en"><![CDATA[<p>
	Text of second Administrators Group&#39;s Content</p>
<p>
	This content belongs to the Administrators Group was declared visible to guest users.</p>
]]></hypertext></attribute><attribute name="Foto" attributetype="Image" /><attribute name="Data" attributetype="Date"><date>20090330</date></attribute><attribute name="Numero" attributetype="Number"><number>78</number></attribute></attributes><status>PUBLIC</status><version>2.0</version><lastEditor>admin</lastEditor><created>20080721143834</created><lastModified>20120803222859</lastModified></content>
', 'administrators', '2.1', 'admin');
INSERT INTO contents (contentid, contenttype, descr, status, workxml, created, lastmodified, onlinexml, maingroup, currentversion, lasteditor) VALUES ('ART122', 'ART', 'Contenuto degli amministratori 3', 'PUBLIC', '<?xml version="1.0" encoding="UTF-8"?>
<content id="ART122" typecode="ART" typedescr="Articolo rassegna stampa"><descr>Contenuto degli amministratori 3</descr><groups mainGroup="administrators"><group name="customers" /></groups><categories><category id="general_cat3" /></categories><attributes><attribute name="Titolo" attributetype="Text"><text lang="it">Titolo Contenuto degli "Amministratori" 3</text></attribute><list attributetype="Monolist" name="Autori" nestedtype="Monotext" /><attribute name="VediAnche" attributetype="Link" /><attribute name="CorpoTesto" attributetype="Hypertext"><hypertext lang="it"><![CDATA[<p>
	Testo terzo contenuto del gruppo degli Amministratori</p>
<p>
	Questo contenuto appartiene al Gruppo degli Amministratori ma dichiarato visibile agli utenti del gruppo customers.</p>
]]></hypertext><hypertext lang="en"><![CDATA[<p>
	Text of third Administrators Group&#39;s Content</p>
<p>
	This content belongs to the Administrators Group was declared visible to customers users.</p>
]]></hypertext></attribute><attribute name="Foto" attributetype="Image" /><attribute name="Data" attributetype="Date" /><attribute name="Numero" attributetype="Number" /></attributes><status>PUBLIC</status><version>1.0</version><lastEditor>admin</lastEditor><created>20080721143945</created><lastModified>20120803222932</lastModified></content>
', '20080721143945', '20120803222932', '<?xml version="1.0" encoding="UTF-8"?>
<content id="ART122" typecode="ART" typedescr="Articolo rassegna stampa"><descr>Contenuto degli amministratori 3</descr><groups mainGroup="administrators"><group name="customers" /></groups><categories><category id="general_cat3" /></categories><attributes><attribute name="Titolo" attributetype="Text"><text lang="it">Titolo Contenuto degli "Amministratori" 3</text></attribute><list attributetype="Monolist" name="Autori" nestedtype="Monotext" /><attribute name="VediAnche" attributetype="Link" /><attribute name="CorpoTesto" attributetype="Hypertext"><hypertext lang="it"><![CDATA[<p>
	Testo terzo contenuto del gruppo degli Amministratori</p>
<p>
	Questo contenuto appartiene al Gruppo degli Amministratori ma dichiarato visibile agli utenti del gruppo customers.</p>
]]></hypertext><hypertext lang="en"><![CDATA[<p>
	Text of third Administrators Group&#39;s Content</p>
<p>
	This content belongs to the Administrators Group was declared visible to customers users.</p>
]]></hypertext></attribute><attribute name="Foto" attributetype="Image" /><attribute name="Data" attributetype="Date" /><attribute name="Numero" attributetype="Number" /></attributes><status>PUBLIC</status><version>1.0</version><lastEditor>admin</lastEditor><created>20080721143945</created><lastModified>20120803222932</lastModified></content>
', 'administrators', '1.0', 'admin');




INSERT INTO resources (resid, restype, descr, maingroup, resourcexml, masterfilename) VALUES ('7', 'Attach', 'configurazione', 'free', '<?xml version="1.0" encoding="UTF-8"?>
<resource typecode="Attach" id="7"><descr>configurazione</descr><groups mainGroup="free" /><categories /><masterfile>configurazione.txt</masterfile><instance><size>0</size><filename>configurazione.txt</filename><mimetype>application/msword</mimetype><weight>55 Kb</weight></instance></resource>
', 'configurazione.txt');
INSERT INTO resources (resid, restype, descr, maingroup, resourcexml, masterfilename) VALUES ('82', 'Image', 'jAPS', 'customers', '<?xml version="1.0" encoding="UTF-8"?>
<resource typecode="Image" id="82"><descr>jAPS</descr><groups mainGroup="customers" /><categories /><masterfile>jAPS.jpg</masterfile><instance><size>3</size><filename>jAPS_d3.jpg</filename><mimetype>image/jpeg</mimetype><weight>2 Kb</weight></instance><instance><size>2</size><filename>jAPS_d2.jpg</filename><mimetype>image/jpeg</mimetype><weight>2 Kb</weight></instance><instance><size>1</size><filename>jAPS_d1.jpg</filename><mimetype>image/jpeg</mimetype><weight>1 Kb</weight></instance><instance><size>0</size><filename>jAPS_d0.jpg</filename><mimetype>image/jpeg</mimetype><weight>9 Kb</weight></instance></resource>
', 'jAPS.jpg');
INSERT INTO resources (resid, restype, descr, maingroup, resourcexml, masterfilename) VALUES ('22', 'Image', 'jAPS Team', 'free', '<?xml version="1.0" encoding="UTF-8"?>
<resource typecode="Image" id="22"><descr>jAPS Team</descr><groups mainGroup="free" /><categories /><masterfile>jAPSTeam.jpg</masterfile><instance><size>3</size><filename>jAPSTeam_d3.jpg</filename><mimetype>image/jpeg</mimetype><weight>2 Kb</weight></instance><instance><size>2</size><filename>jAPSTeam_d2.jpg</filename><mimetype>image/jpeg</mimetype><weight>2 Kb</weight></instance><instance><size>1</size><filename>jAPSTeam_d1.jpg</filename><mimetype>image/jpeg</mimetype><weight>1 Kb</weight></instance><instance><size>0</size><filename>jAPSTeam_d0.jpg</filename><mimetype>image/jpeg</mimetype><weight>9 Kb</weight></instance></resource>
', 'jAPSTeam.jpg');
INSERT INTO resources (resid, restype, descr, maingroup, resourcexml, masterfilename) VALUES ('44', 'Image', 'logo', 'free', '<?xml version="1.0" encoding="UTF-8"?>
<resource typecode="Image" id="44"><descr>logo</descr><groups mainGroup="free" /><categories><category id="resCat1" /></categories><masterfile>lvback.jpg</masterfile><instance><size>3</size><filename>lvback_d3.jpg</filename><mimetype>image/jpeg</mimetype><weight>4 Kb</weight></instance><instance><size>2</size><filename>lvback_d2.jpg</filename><mimetype>image/jpeg</mimetype><weight>4 Kb</weight></instance><instance><size>1</size><filename>lvback_d1.jpg</filename><mimetype>image/jpeg</mimetype><weight>2 Kb</weight></instance><instance><size>0</size><filename>lvback_d0.jpg</filename><mimetype>image/jpeg</mimetype><weight>7 Kb</weight></instance></resource>
', 'lvback.jpg');




INSERT INTO resourcerelations (resid, refcategory) VALUES ('44', 'resource_root');
INSERT INTO resourcerelations (resid, refcategory) VALUES ('44', 'Image');
INSERT INTO resourcerelations (resid, refcategory) VALUES ('44', 'resCat1');




INSERT INTO widgetcatalog (code, titles, parameters, plugincode, parenttypecode, defaultconfig, locked, maingroup) VALUES ('90_events', '<?xml version="1.0" encoding="UTF-8"?>
<properties>
<property key="en">Lista contenuti anni ''90</property>
<property key="it">Lista contenuti anni ''90</property>
</properties>', NULL, NULL, 'content_viewer_list', '<?xml version="1.0" encoding="UTF-8"?>
<properties>
<property key="maxElemForItem">10</property>
<property key="filters">(order=ASC;attributeFilter=true;end=31/12/1999;key=DataInizio;start=01/01/1990)</property>
<property key="contentType">EVN</property>
</properties>', 0, NULL);
INSERT INTO widgetcatalog (code, titles, parameters, plugincode, parenttypecode, defaultconfig, locked, maingroup) VALUES ('search_result', '<?xml version="1.0" encoding="UTF-8"?>
<properties>
<property key="en">Search - Search Result</property>
<property key="it">Ricerca - Risultati della Ricerca</property>
</properties>', NULL, 'jacms', NULL, NULL, 1, NULL);
INSERT INTO widgetcatalog (code, titles, parameters, plugincode, parenttypecode, defaultconfig, locked, maingroup) VALUES ('content_viewer', '<?xml version="1.0" encoding="UTF-8"?>
<properties>
<property key="en">Contents - Publish a Content</property>
<property key="it">Contenuti - Pubblica un Contenuto</property>
</properties>', '<config>
	<parameter name="contentId">
		Identificativo del Contenuto
	</parameter>
	<parameter name="modelId">
		Identificativo del Modello di Contenuto
	</parameter>
	<action name="viewerConfig"/>
</config>
', 'jacms', NULL, NULL, 1, NULL);
INSERT INTO widgetcatalog (code, titles, parameters, plugincode, parenttypecode, defaultconfig, locked, maingroup) VALUES ('content_viewer_list', '<?xml version="1.0" encoding="UTF-8"?>
<properties>
<property key="en">Contents - Publish a List of Contents</property>
<property key="it">Contenuti - Pubblica una Lista di Contenuti</property>
</properties>', '<config>
	<parameter name="contentType">Content Type (mandatory)</parameter>
	<parameter name="modelId">Content Model</parameter>
	<parameter name="userFilters">Front-End user filter options</parameter>
	<parameter name="category">Content Category **deprecated**</parameter>
	<parameter name="categories">Content Category codes (comma separeted)</parameter>
	<parameter name="orClauseCategoryFilter" />
	<parameter name="maxElemForItem">Contents for each page</parameter>
	<parameter name="filters" />
	<parameter name="title_{lang}">Widget Title in lang {lang}</parameter>
	<parameter name="pageLink">The code of the Page to link</parameter>
	<parameter name="linkDescr_{lang}">Link description in lang {lang}</parameter>
	<action name="listViewerConfig"/>
</config>', 'jacms', NULL, NULL, 1, NULL);
INSERT INTO widgetcatalog (code, titles, parameters, plugincode, parenttypecode, defaultconfig, locked, maingroup) VALUES ('logic_type', '<?xml version="1.0" encoding="UTF-8"?>
<properties>
<property key="en">Logic type for test</property>
<property key="it">Tipo logico per test</property>
</properties>', NULL, NULL, 'content_viewer_list', '<?xml version="1.0" encoding="UTF-8"?>
<properties>
<property key="maxElemForItem">10</property>
<property key="filters">(order=ASC;attributeFilter=true;end=31/12/1999;key=DataInizio;start=01/01/1990)</property>
<property key="contentType">EVN</property>
</properties>', 0, NULL);




INSERT INTO guifragment (code, widgettypecode, plugincode, gui, defaultgui, locked) VALUES ('content_viewer', 'content_viewer', 'jacms', NULL, '<#assign jacms=JspTaglibs["target/classes/META-INF/jacms-aps-core.tld"]>
<!-- relative tld path for test execution -->
<@jacms.content publishExtraTitle=true />', 1);




INSERT INTO widgetconfig (pagecode, framepos, widgetcode, config, publishedcontent) VALUES ('pagina_11', 2, 'content_viewer', '<?xml version="1.0" encoding="UTF-8"?>
<properties>
<property key="contentId">ART187</property>
</properties>
', 'ART187');
INSERT INTO widgetconfig (pagecode, framepos, widgetcode, config, publishedcontent) VALUES ('homepage', 2, 'content_viewer', '<?xml version="1.0" encoding="UTF-8"?>
<properties>
<property key="modelId">2</property>
<property key="contentId">ART1</property>
</properties>
', 'ART1');
INSERT INTO widgetconfig (pagecode, framepos, widgetcode, config, publishedcontent) VALUES ('coach_page', 2, 'content_viewer', '<?xml version="1.0" encoding="UTF-8"?>
<properties>
<property key="contentId">ART187</property>
</properties>
', 'ART187');
INSERT INTO widgetconfig (pagecode, framepos, widgetcode, config, publishedcontent) VALUES ('customers_page', 2, 'content_viewer', '<?xml version="1.0" encoding="UTF-8"?>
<properties>
<property key="contentId">ART111</property>
</properties>
', 'ART111');
INSERT INTO widgetconfig (pagecode, framepos, widgetcode, config, publishedcontent) VALUES ('homepage', 0, 'content_viewer_list', '<?xml version="1.0" encoding="UTF-8"?>
<properties>
<property key="maxElemForItem">5</property>
<property key="modelId">11</property>
<property key="contentType">NEW</property>
<property key="filters">(order=DESC;attributeFilter=true;likeOption=false;key=Date)+(order=ASC;attributeFilter=true;likeOption=false;key=Title)</property>
</properties>', NULL);
INSERT INTO widgetconfig (pagecode, framepos, widgetcode, config, publishedcontent) VALUES ('customer_subpage_2', 2, 'content_viewer', '<?xml version="1.0" encoding="UTF-8"?>
<properties>
<property key="contentId">ART112</property>
</properties>
', 'ART112');
INSERT INTO widgetconfig (pagecode, framepos, widgetcode, config, publishedcontent) VALUES ('contentview', 2, 'content_viewer', NULL, NULL);
INSERT INTO widgetconfig (pagecode, framepos, widgetcode, config, publishedcontent) VALUES ('pagina_2', 1, 'content_viewer', '<?xml version="1.0" encoding="UTF-8"?>
<properties>
<property key="contentId">ART187</property>
</properties>
', 'ART187');




INSERT INTO sysconfig (version, item, descr, config) VALUES ('test', 'contentTypes', 'Definition of the Content Types', '<?xml version="1.0" encoding="UTF-8"?>
<contenttypes>
	<contenttype typecode="ALL" typedescr="Content type with all attribute types" viewpage="announcements_read" listmodel="**NULL**" defaultmodel="**NULL**">
		<attributes>
			<attribute name="Attach" attributetype="Attach" />
			<attribute name="Boolean" attributetype="Boolean" />
			<attribute name="CheckBox" attributetype="CheckBox" />
			<attribute name="Date" attributetype="Date" />
			<attribute name="Date2" attributetype="Date">
				<validations>
					<rangestart attribute="Date" />
					<rangeend>25/11/2010</rangeend>
				</validations>
			</attribute>
			<attribute name="Enumerator" attributetype="Enumerator" separator=","><![CDATA[a,b,c]]></attribute>
			<attribute name="Hypertext" attributetype="Hypertext" />
			<attribute name="Image" attributetype="Image" />
			<attribute name="Link" attributetype="Link" />
			<attribute name="Longtext" attributetype="Longtext" />
			<attribute name="Monotext" attributetype="Monotext" />
			<attribute name="Monotext2" attributetype="Monotext">
				<validations>
					<minlength>15</minlength>
					<maxlength>30</maxlength>
					<regexp><![CDATA[.+@.+.[a-z]+]]></regexp>
				</validations>
			</attribute>
			<attribute name="Number" attributetype="Number" />
			<attribute name="Number2" attributetype="Number">
				<validations>
					<rangestart>50</rangestart>
					<rangeend>300</rangeend>
				</validations>
			</attribute>
			<attribute name="Text" attributetype="Text">
				<roles>
					<role>jacms:title</role>
				</roles>
			</attribute>
			<attribute name="Text2" attributetype="Text">
				<validations>
					<minlength>15</minlength>
					<maxlength>30</maxlength>
					<regexp><![CDATA[.+@.+.[a-z]+]]></regexp>
				</validations>
			</attribute>
			<attribute name="ThreeState" attributetype="ThreeState" />
			<attribute name="Composite" attributetype="Composite">
				<attributes>
					<attribute name="Attach" attributetype="Attach" />
					<attribute name="Boolean" attributetype="Boolean" />
					<attribute name="CheckBox" attributetype="CheckBox" />
					<attribute name="Date" attributetype="Date">
						<validations>
							<rangestart attribute="Date" />
							<rangeend>10/10/2010</rangeend>
						</validations>
					</attribute>
					<attribute name="Enumerator" attributetype="Enumerator" separator="," />
					<attribute name="Hypertext" attributetype="Hypertext" />
					<attribute name="Image" attributetype="Image" />
					<attribute name="Link" attributetype="Link" />
					<attribute name="Longtext" attributetype="Longtext" />
					<attribute name="Monotext" attributetype="Monotext" />
					<attribute name="Number" attributetype="Number">
						<validations>
							<expression evalOnValuedAttribute="true">
								<ognlexpression><![CDATA[#entity.getAttribute(''Number'').value == null || (#entity.getAttribute(''Number'').value != null && value > #entity.getAttribute(''Number'').value)]]></ognlexpression>
								<errormessage><![CDATA[Value has to be upper then ''Number'' attribute]]></errormessage>
								<helpmessage><![CDATA[If ''Number'' valued attribute, Value has to be upper]]></helpmessage>
							</expression>
						</validations>
					</attribute>
					<attribute name="Text" attributetype="Text" />
					<attribute name="ThreeState" attributetype="ThreeState" />
				</attributes>
			</attribute>
			<list name="ListBoolea" attributetype="List">
				<nestedtype>
					<attribute name="ListBoolea" attributetype="Boolean" />
				</nestedtype>
			</list>
			<list name="ListCheck" attributetype="List">
				<nestedtype>
					<attribute name="ListCheck" attributetype="CheckBox" />
				</nestedtype>
			</list>
			<list name="ListDate" attributetype="List">
				<nestedtype>
					<attribute name="ListDate" attributetype="Date" />
				</nestedtype>
			</list>
			<list name="ListEnum" attributetype="List">
				<nestedtype>
					<attribute name="ListEnum" attributetype="Enumerator" separator=","><![CDATA[a,b,c]]></attribute>
				</nestedtype>
			</list>
			<list name="ListMonot" attributetype="List">
				<nestedtype>
					<attribute name="ListMonot" attributetype="Monotext" />
				</nestedtype>
			</list>
			<list name="ListNumber" attributetype="List">
				<nestedtype>
					<attribute name="ListNumber" attributetype="Number" />
				</nestedtype>
			</list>
			<list name="List3Stat" attributetype="List">
				<nestedtype>
					<attribute name="List3Stat" attributetype="ThreeState" />
				</nestedtype>
			</list>
			<list name="MonoLAtta" attributetype="Monolist">
				<nestedtype>
					<attribute name="MonoLAtta" attributetype="Attach" />
				</nestedtype>
			</list>
			<list name="MonoLBool" attributetype="Monolist">
				<nestedtype>
					<attribute name="MonoLBool" attributetype="Boolean" />
				</nestedtype>
			</list>
			<list name="MonoLChec" attributetype="Monolist">
				<nestedtype>
					<attribute name="MonoLChec" attributetype="CheckBox" />
				</nestedtype>
			</list>
			<list name="MonoLCom" attributetype="Monolist">
				<nestedtype>
					<attribute name="MonoLCom" attributetype="Composite">
						<attributes>
							<attribute name="Attach" attributetype="Attach" />
							<attribute name="Date" attributetype="Date">
								<validations>
									<rangestart>10/10/1971</rangestart>
									<rangeend attribute="Date" />
								</validations>
							</attribute>
							<attribute name="Enumerator" attributetype="Enumerator" separator=","><![CDATA[a,b,c]]></attribute>
							<attribute name="Hypertext" attributetype="Hypertext" />
							<attribute name="Image" attributetype="Image" />
							<attribute name="Link" attributetype="Link" />
							<attribute name="Longtext" attributetype="Longtext" />
							<attribute name="Monotext" attributetype="Monotext" />
							<attribute name="Number" attributetype="Number">
								<validations>
									<rangestart>25</rangestart>
									<rangeend attribute="Number" />
								</validations>
							</attribute>
							<attribute name="Text" attributetype="Text" />
						</attributes>
					</attribute>
				</nestedtype>
			</list>
			<list name="MonoLCom2" attributetype="Monolist">
				<nestedtype>
					<attribute name="MonoLCom2" attributetype="Composite">
						<attributes>
							<attribute name="Boolean" attributetype="Boolean" />
							<attribute name="CheckBox" attributetype="CheckBox" />
							<attribute name="ThreeState" attributetype="ThreeState" />
						</attributes>
					</attribute>
				</nestedtype>
			</list>
			<list name="MonoLDate" attributetype="Monolist">
				<nestedtype>
					<attribute name="MonoLDate" attributetype="Date" />
				</nestedtype>
			</list>
			<list name="MonoLEnum" attributetype="Monolist">
				<nestedtype>
					<attribute name="MonoLEnum" attributetype="Enumerator" separator=","><![CDATA[a,b,c]]></attribute>
				</nestedtype>
			</list>
			<list name="MonoLHyper" attributetype="Monolist">
				<nestedtype>
					<attribute name="MonoLHyper" attributetype="Hypertext" />
				</nestedtype>
			</list>
			<list name="MonoLImage" attributetype="Monolist">
				<nestedtype>
					<attribute name="MonoLImage" attributetype="Image" />
				</nestedtype>
			</list>
			<list name="MonoLLink" attributetype="Monolist">
				<nestedtype>
					<attribute name="MonoLLink" attributetype="Link" />
				</nestedtype>
			</list>
			<list name="MonoLLong" attributetype="Monolist">
				<nestedtype>
					<attribute name="MonoLLong" attributetype="Longtext" />
				</nestedtype>
			</list>
			<list name="MonoLMonot" attributetype="Monolist">
				<nestedtype>
					<attribute name="MonoLMonot" attributetype="Monotext" />
				</nestedtype>
			</list>
			<list name="MonoLNumb" attributetype="Monolist">
				<nestedtype>
					<attribute name="MonoLNumb" attributetype="Number" />
				</nestedtype>
			</list>
			<list name="MonoLText" attributetype="Monolist">
				<nestedtype>
					<attribute name="MonoLText" attributetype="Text" />
				</nestedtype>
			</list>
			<list name="MonoL3stat" attributetype="Monolist">
				<nestedtype>
					<attribute name="MonoL3stat" attributetype="ThreeState" />
				</nestedtype>
			</list>
			<attribute name="MARKER" attributetype="Monotext">
				<validations>
					<required>true</required>
				</validations>
			</attribute>
		</attributes>
	</contenttype>
	<contenttype typecode="ART" typedescr="Articolo rassegna stampa" viewpage="contentview" listmodel="11" defaultmodel="1">
		<attributes>
			<attribute name="Titolo" attributetype="Text" indexingtype="TEXT">
				<validations>
					<required>true</required>
				</validations>
				<roles>
					<role>jacms:title</role>
				</roles>
			</attribute>
			<list name="Autori" attributetype="Monolist">
				<nestedtype>
					<attribute name="Autori" attributetype="Monotext" />
				</nestedtype>
			</list>
			<attribute name="VediAnche" attributetype="Link" />
			<attribute name="CorpoTesto" attributetype="Hypertext" indexingtype="text" />
			<attribute name="Foto" attributetype="Image" />
			<attribute name="Data" attributetype="Date" searchable="true" />
			<attribute name="Numero" attributetype="Number" searchable="true" />
		</attributes>
	</contenttype>
	<contenttype typecode="EVN" typedescr="Evento" viewpage="contentview" listmodel="51" defaultmodel="5">
		<attributes>
			<attribute name="Titolo" attributetype="Text" searchable="true" indexingtype="TEXT">
				<validations />
				<roles>
					<role>jacms:title</role>
				</roles>
			</attribute>
			<attribute name="CorpoTesto" attributetype="Hypertext" indexingtype="text" />
			<attribute name="DataInizio" attributetype="Date" searchable="true" />
			<attribute name="DataFine" attributetype="Date" searchable="true" />
			<attribute name="Foto" attributetype="Image" />
			<list name="LinkCorrelati" attributetype="Monolist">
				<nestedtype>
					<attribute name="LinkCorrelati" attributetype="Link" />
				</nestedtype>
			</list>
		</attributes>
	</contenttype>
	<contenttype typecode="RAH" typedescr="Tipo_Semplice" viewpage="contentview" listmodel="126" defaultmodel="457">
		<attributes>
			<attribute name="Titolo" attributetype="Text" indexingtype="text">
				<validations>
					<minlength>10</minlength>
					<maxlength>100</maxlength>
				</validations>
			</attribute>
			<attribute name="CorpoTesto" attributetype="Hypertext" indexingtype="text" />
			<attribute name="Foto" attributetype="Image" />
			<attribute name="email" attributetype="Monotext">
				<validations>
					<regexp><![CDATA[.+@.+.[a-z]+]]></regexp>
				</validations>
			</attribute>
			<attribute name="Numero" attributetype="Number" />
			<attribute name="Correlati" attributetype="Link" />
			<attribute name="Allegati" attributetype="Attach" />
			<attribute name="Checkbox" attributetype="CheckBox" />
		</attributes>
	</contenttype>
</contenttypes>
');
INSERT INTO sysconfig (version, item, descr, config) VALUES ('test', 'imageDimensions', 'Definition of the resized image dimensions', '<Dimensions>
	<Dimension>
		<id>1</id>
		<dimx>90</dimx>
		<dimy>90</dimy>
	</Dimension>
	<Dimension>
		<id>2</id>
		<dimx>130</dimx>
		<dimy>130</dimy>
	</Dimension>
	<Dimension>
		<id>3</id>
		<dimx>150</dimx>
		<dimy>150</dimy>
	</Dimension>
</Dimensions>
');
INSERT INTO sysconfig (version, item, descr, config) VALUES ('test', 'subIndexDir', 'Name of the sub-directory containing content indexing files', 'index');




INSERT INTO contentrelations (contentid, refpage, refcontent, refresource, refcategory, refgroup) VALUES ('ART122', NULL, NULL, NULL, 'general_cat3', NULL);
INSERT INTO contentrelations (contentid, refpage, refcontent, refresource, refcategory, refgroup) VALUES ('ART122', NULL, NULL, NULL, 'general', NULL);
INSERT INTO contentrelations (contentid, refpage, refcontent, refresource, refcategory, refgroup) VALUES ('ART122', NULL, NULL, NULL, NULL, 'administrators');
INSERT INTO contentrelations (contentid, refpage, refcontent, refresource, refcategory, refgroup) VALUES ('ART122', NULL, NULL, NULL, NULL, 'customers');
INSERT INTO contentrelations (contentid, refpage, refcontent, refresource, refcategory, refgroup) VALUES ('RAH101', NULL, NULL, NULL, NULL, 'customers');
INSERT INTO contentrelations (contentid, refpage, refcontent, refresource, refcategory, refgroup) VALUES ('RAH1', NULL, NULL, NULL, NULL, 'free');
INSERT INTO contentrelations (contentid, refpage, refcontent, refresource, refcategory, refgroup) VALUES ('RAH1', NULL, NULL, '7', NULL, NULL);
INSERT INTO contentrelations (contentid, refpage, refcontent, refresource, refcategory, refgroup) VALUES ('ART104', NULL, NULL, NULL, NULL, 'coach');
INSERT INTO contentrelations (contentid, refpage, refcontent, refresource, refcategory, refgroup) VALUES ('ART102', NULL, NULL, NULL, 'general', NULL);
INSERT INTO contentrelations (contentid, refpage, refcontent, refresource, refcategory, refgroup) VALUES ('ART102', NULL, NULL, NULL, 'general_cat1', NULL);
INSERT INTO contentrelations (contentid, refpage, refcontent, refresource, refcategory, refgroup) VALUES ('ART102', NULL, NULL, NULL, NULL, 'customers');
INSERT INTO contentrelations (contentid, refpage, refcontent, refresource, refcategory, refgroup) VALUES ('ART102', NULL, 'ART111', NULL, NULL, NULL);
INSERT INTO contentrelations (contentid, refpage, refcontent, refresource, refcategory, refgroup) VALUES ('ART180', NULL, NULL, NULL, 'cat1', NULL);
INSERT INTO contentrelations (contentid, refpage, refcontent, refresource, refcategory, refgroup) VALUES ('ART180', NULL, NULL, NULL, NULL, 'free');
INSERT INTO contentrelations (contentid, refpage, refcontent, refresource, refcategory, refgroup) VALUES ('ART180', NULL, NULL, '44', NULL, NULL);
INSERT INTO contentrelations (contentid, refpage, refcontent, refresource, refcategory, refgroup) VALUES ('ART1', NULL, NULL, NULL, NULL, 'free');
INSERT INTO contentrelations (contentid, refpage, refcontent, refresource, refcategory, refgroup) VALUES ('ART1', NULL, NULL, '44', NULL, NULL);
INSERT INTO contentrelations (contentid, refpage, refcontent, refresource, refcategory, refgroup) VALUES ('ART187', NULL, NULL, NULL, NULL, 'free');
INSERT INTO contentrelations (contentid, refpage, refcontent, refresource, refcategory, refgroup) VALUES ('EVN25', NULL, NULL, NULL, NULL, 'free');
INSERT INTO contentrelations (contentid, refpage, refcontent, refresource, refcategory, refgroup) VALUES ('EVN25', NULL, NULL, NULL, NULL, 'coach');
INSERT INTO contentrelations (contentid, refpage, refcontent, refresource, refcategory, refgroup) VALUES ('EVN41', NULL, NULL, NULL, NULL, 'coach');
INSERT INTO contentrelations (contentid, refpage, refcontent, refresource, refcategory, refgroup) VALUES ('EVN103', NULL, NULL, NULL, NULL, 'coach');
INSERT INTO contentrelations (contentid, refpage, refcontent, refresource, refcategory, refgroup) VALUES ('EVN193', NULL, NULL, NULL, 'evento', NULL);
INSERT INTO contentrelations (contentid, refpage, refcontent, refresource, refcategory, refgroup) VALUES ('EVN193', NULL, NULL, NULL, NULL, 'free');
INSERT INTO contentrelations (contentid, refpage, refcontent, refresource, refcategory, refgroup) VALUES ('EVN193', NULL, 'ART1', NULL, NULL, NULL);
INSERT INTO contentrelations (contentid, refpage, refcontent, refresource, refcategory, refgroup) VALUES ('EVN193', 'pagina_11', NULL, NULL, NULL, NULL);
INSERT INTO contentrelations (contentid, refpage, refcontent, refresource, refcategory, refgroup) VALUES ('EVN194', NULL, NULL, NULL, NULL, 'free');
INSERT INTO contentrelations (contentid, refpage, refcontent, refresource, refcategory, refgroup) VALUES ('EVN194', NULL, 'ART1', NULL, NULL, NULL);
INSERT INTO contentrelations (contentid, refpage, refcontent, refresource, refcategory, refgroup) VALUES ('EVN194', 'pagina_11', NULL, NULL, NULL, NULL);
INSERT INTO contentrelations (contentid, refpage, refcontent, refresource, refcategory, refgroup) VALUES ('EVN191', NULL, NULL, NULL, NULL, 'free');
INSERT INTO contentrelations (contentid, refpage, refcontent, refresource, refcategory, refgroup) VALUES ('EVN21', NULL, NULL, NULL, NULL, 'free');
INSERT INTO contentrelations (contentid, refpage, refcontent, refresource, refcategory, refgroup) VALUES ('EVN24', NULL, NULL, NULL, NULL, 'free');
INSERT INTO contentrelations (contentid, refpage, refcontent, refresource, refcategory, refgroup) VALUES ('EVN23', NULL, NULL, NULL, NULL, 'free');
INSERT INTO contentrelations (contentid, refpage, refcontent, refresource, refcategory, refgroup) VALUES ('EVN192', NULL, NULL, NULL, 'evento', NULL);
INSERT INTO contentrelations (contentid, refpage, refcontent, refresource, refcategory, refgroup) VALUES ('EVN192', NULL, NULL, NULL, NULL, 'free');
INSERT INTO contentrelations (contentid, refpage, refcontent, refresource, refcategory, refgroup) VALUES ('EVN20', NULL, NULL, NULL, NULL, 'free');
INSERT INTO contentrelations (contentid, refpage, refcontent, refresource, refcategory, refgroup) VALUES ('ART111', NULL, NULL, NULL, 'general', NULL);
INSERT INTO contentrelations (contentid, refpage, refcontent, refresource, refcategory, refgroup) VALUES ('ART111', NULL, NULL, NULL, 'general_cat1', NULL);
INSERT INTO contentrelations (contentid, refpage, refcontent, refresource, refcategory, refgroup) VALUES ('ART111', NULL, NULL, NULL, 'general_cat2', NULL);
INSERT INTO contentrelations (contentid, refpage, refcontent, refresource, refcategory, refgroup) VALUES ('ART111', NULL, NULL, NULL, NULL, 'coach');
INSERT INTO contentrelations (contentid, refpage, refcontent, refresource, refcategory, refgroup) VALUES ('ART111', NULL, NULL, NULL, NULL, 'customers');
INSERT INTO contentrelations (contentid, refpage, refcontent, refresource, refcategory, refgroup) VALUES ('ART111', NULL, NULL, NULL, NULL, 'helpdesk');
INSERT INTO contentrelations (contentid, refpage, refcontent, refresource, refcategory, refgroup) VALUES ('ART120', NULL, NULL, NULL, 'general_cat3', NULL);
INSERT INTO contentrelations (contentid, refpage, refcontent, refresource, refcategory, refgroup) VALUES ('ART120', NULL, NULL, NULL, 'general', NULL);
INSERT INTO contentrelations (contentid, refpage, refcontent, refresource, refcategory, refgroup) VALUES ('ART120', NULL, NULL, NULL, 'general_cat2', NULL);
INSERT INTO contentrelations (contentid, refpage, refcontent, refresource, refcategory, refgroup) VALUES ('ART120', NULL, NULL, NULL, NULL, 'administrators');
INSERT INTO contentrelations (contentid, refpage, refcontent, refresource, refcategory, refgroup) VALUES ('ART112', NULL, NULL, NULL, NULL, 'coach');
INSERT INTO contentrelations (contentid, refpage, refcontent, refresource, refcategory, refgroup) VALUES ('ART112', NULL, NULL, NULL, NULL, 'customers');
INSERT INTO contentrelations (contentid, refpage, refcontent, refresource, refcategory, refgroup) VALUES ('ART112', NULL, NULL, NULL, NULL, 'helpdesk');
INSERT INTO contentrelations (contentid, refpage, refcontent, refresource, refcategory, refgroup) VALUES ('ART121', NULL, NULL, NULL, NULL, 'free');
INSERT INTO contentrelations (contentid, refpage, refcontent, refresource, refcategory, refgroup) VALUES ('ART121', NULL, NULL, NULL, NULL, 'administrators');




INSERT INTO contentsearch (contentid, attrname, textvalue, datevalue, numvalue, langcode) VALUES ('ART112', 'Data', NULL, '2006-02-13 00:00:00', NULL, NULL);
INSERT INTO contentsearch (contentid, attrname, textvalue, datevalue, numvalue, langcode) VALUES ('ART121', 'Data', NULL, '2009-03-30 00:00:00', NULL, NULL);
INSERT INTO contentsearch (contentid, attrname, textvalue, datevalue, numvalue, langcode) VALUES ('ART121', 'Numero', NULL, NULL, 78, NULL);
INSERT INTO contentsearch (contentid, attrname, textvalue, datevalue, numvalue, langcode) VALUES ('ART104', 'Data', NULL, '2007-01-04 00:00:00', NULL, NULL);
INSERT INTO contentsearch (contentid, attrname, textvalue, datevalue, numvalue, langcode) VALUES ('ART1', 'Data', NULL, '2004-03-10 00:00:00', NULL, NULL);
INSERT INTO contentsearch (contentid, attrname, textvalue, datevalue, numvalue, langcode) VALUES ('EVN25', 'Titolo', 'TEATRO DELLE MERAVIGLIE', NULL, NULL, 'it');
INSERT INTO contentsearch (contentid, attrname, textvalue, datevalue, numvalue, langcode) VALUES ('EVN25', 'Titolo', 'TEATRO DELLE MERAVIGLIE', NULL, NULL, 'en');
INSERT INTO contentsearch (contentid, attrname, textvalue, datevalue, numvalue, langcode) VALUES ('EVN25', 'DataInizio', NULL, '2007-12-12 00:00:00', NULL, NULL);
INSERT INTO contentsearch (contentid, attrname, textvalue, datevalue, numvalue, langcode) VALUES ('EVN25', 'DataFine', NULL, '2007-12-22 00:00:00', NULL, NULL);
INSERT INTO contentsearch (contentid, attrname, textvalue, datevalue, numvalue, langcode) VALUES ('EVN41', 'Titolo', 'Sagra della ciliegia', NULL, NULL, 'it');
INSERT INTO contentsearch (contentid, attrname, textvalue, datevalue, numvalue, langcode) VALUES ('EVN41', 'Titolo', 'Sagra della ciliegia', NULL, NULL, 'en');
INSERT INTO contentsearch (contentid, attrname, textvalue, datevalue, numvalue, langcode) VALUES ('EVN41', 'DataInizio', NULL, '2008-01-06 00:00:00', NULL, NULL);
INSERT INTO contentsearch (contentid, attrname, textvalue, datevalue, numvalue, langcode) VALUES ('EVN41', 'DataFine', NULL, '2008-01-24 00:00:00', NULL, NULL);
INSERT INTO contentsearch (contentid, attrname, textvalue, datevalue, numvalue, langcode) VALUES ('EVN103', 'Titolo', 'Titolo Contenuto 1 Coach', NULL, NULL, 'it');
INSERT INTO contentsearch (contentid, attrname, textvalue, datevalue, numvalue, langcode) VALUES ('EVN103', 'Titolo', 'Titolo Contenuto 1 Coach', NULL, NULL, 'en');
INSERT INTO contentsearch (contentid, attrname, textvalue, datevalue, numvalue, langcode) VALUES ('EVN103', 'DataInizio', NULL, '1999-04-15 00:00:00', NULL, NULL);
INSERT INTO contentsearch (contentid, attrname, textvalue, datevalue, numvalue, langcode) VALUES ('EVN103', 'DataFine', NULL, '2000-04-14 00:00:00', NULL, NULL);
INSERT INTO contentsearch (contentid, attrname, textvalue, datevalue, numvalue, langcode) VALUES ('EVN193', 'Titolo', 'Titolo C - Evento 3', NULL, NULL, 'it');
INSERT INTO contentsearch (contentid, attrname, textvalue, datevalue, numvalue, langcode) VALUES ('EVN193', 'Titolo', 'Title D - Evento 3', NULL, NULL, 'en');
INSERT INTO contentsearch (contentid, attrname, textvalue, datevalue, numvalue, langcode) VALUES ('EVN193', 'DataInizio', NULL, '2017-04-12 00:00:00', NULL, NULL);
INSERT INTO contentsearch (contentid, attrname, textvalue, datevalue, numvalue, langcode) VALUES ('EVN193', 'DataFine', NULL, '2017-09-12 00:00:00', NULL, NULL);
INSERT INTO contentsearch (contentid, attrname, textvalue, datevalue, numvalue, langcode) VALUES ('EVN194', 'Titolo', 'Titolo D - Evento 4', NULL, NULL, 'it');
INSERT INTO contentsearch (contentid, attrname, textvalue, datevalue, numvalue, langcode) VALUES ('EVN194', 'Titolo', 'Title A - Event 4', NULL, NULL, 'en');
INSERT INTO contentsearch (contentid, attrname, textvalue, datevalue, numvalue, langcode) VALUES ('EVN194', 'DataInizio', NULL, '2022-02-19 00:00:00', NULL, NULL);
INSERT INTO contentsearch (contentid, attrname, textvalue, datevalue, numvalue, langcode) VALUES ('EVN194', 'DataFine', NULL, '2022-04-19 00:00:00', NULL, NULL);
INSERT INTO contentsearch (contentid, attrname, textvalue, datevalue, numvalue, langcode) VALUES ('EVN191', 'Titolo', 'Titolo A - Evento 1', NULL, NULL, 'it');
INSERT INTO contentsearch (contentid, attrname, textvalue, datevalue, numvalue, langcode) VALUES ('EVN191', 'Titolo', 'Title C - Event 1', NULL, NULL, 'en');
INSERT INTO contentsearch (contentid, attrname, textvalue, datevalue, numvalue, langcode) VALUES ('EVN191', 'DataInizio', NULL, '1996-04-17 00:00:00', NULL, NULL);
INSERT INTO contentsearch (contentid, attrname, textvalue, datevalue, numvalue, langcode) VALUES ('EVN191', 'DataFine', NULL, '1996-06-17 00:00:00', NULL, NULL);
INSERT INTO contentsearch (contentid, attrname, textvalue, datevalue, numvalue, langcode) VALUES ('EVN21', 'Titolo', 'Mostra Delle Fragole', NULL, NULL, 'it');
INSERT INTO contentsearch (contentid, attrname, textvalue, datevalue, numvalue, langcode) VALUES ('EVN21', 'Titolo', 'Mostra Delle Fragole', NULL, NULL, 'en');
INSERT INTO contentsearch (contentid, attrname, textvalue, datevalue, numvalue, langcode) VALUES ('EVN21', 'DataInizio', NULL, '2006-01-13 00:00:00', NULL, NULL);
INSERT INTO contentsearch (contentid, attrname, textvalue, datevalue, numvalue, langcode) VALUES ('EVN21', 'DataFine', NULL, '2006-03-04 00:00:00', NULL, NULL);
INSERT INTO contentsearch (contentid, attrname, textvalue, datevalue, numvalue, langcode) VALUES ('EVN24', 'Titolo', 'Castello dei bambini', NULL, NULL, 'it');
INSERT INTO contentsearch (contentid, attrname, textvalue, datevalue, numvalue, langcode) VALUES ('EVN24', 'Titolo', 'Castello dei bambini', NULL, NULL, 'en');
INSERT INTO contentsearch (contentid, attrname, textvalue, datevalue, numvalue, langcode) VALUES ('EVN24', 'DataInizio', NULL, '2009-03-18 00:00:00', NULL, NULL);
INSERT INTO contentsearch (contentid, attrname, textvalue, datevalue, numvalue, langcode) VALUES ('EVN24', 'DataFine', NULL, '2009-03-26 00:00:00', NULL, NULL);
INSERT INTO contentsearch (contentid, attrname, textvalue, datevalue, numvalue, langcode) VALUES ('EVN23', 'Titolo', 'Collezione Ingri', NULL, NULL, 'it');
INSERT INTO contentsearch (contentid, attrname, textvalue, datevalue, numvalue, langcode) VALUES ('EVN23', 'Titolo', 'Collezione Ingri', NULL, NULL, 'en');
INSERT INTO contentsearch (contentid, attrname, textvalue, datevalue, numvalue, langcode) VALUES ('EVN23', 'DataInizio', NULL, '2008-02-13 00:00:00', NULL, NULL);
INSERT INTO contentsearch (contentid, attrname, textvalue, datevalue, numvalue, langcode) VALUES ('EVN23', 'DataFine', NULL, '2008-02-22 00:00:00', NULL, NULL);
INSERT INTO contentsearch (contentid, attrname, textvalue, datevalue, numvalue, langcode) VALUES ('EVN192', 'Titolo', 'Titolo B - Evento 2', NULL, NULL, 'it');
INSERT INTO contentsearch (contentid, attrname, textvalue, datevalue, numvalue, langcode) VALUES ('EVN192', 'Titolo', 'Title B - Event 2', NULL, NULL, 'en');
INSERT INTO contentsearch (contentid, attrname, textvalue, datevalue, numvalue, langcode) VALUES ('EVN192', 'DataInizio', NULL, '1999-04-14 00:00:00', NULL, NULL);
INSERT INTO contentsearch (contentid, attrname, textvalue, datevalue, numvalue, langcode) VALUES ('EVN192', 'DataFine', NULL, '1999-06-14 00:00:00', NULL, NULL);
INSERT INTO contentsearch (contentid, attrname, textvalue, datevalue, numvalue, langcode) VALUES ('EVN20', 'Titolo', 'Mostra Zootecnica', NULL, NULL, 'it');
INSERT INTO contentsearch (contentid, attrname, textvalue, datevalue, numvalue, langcode) VALUES ('EVN20', 'Titolo', 'Mostra Zootecnica', NULL, NULL, 'en');
INSERT INTO contentsearch (contentid, attrname, textvalue, datevalue, numvalue, langcode) VALUES ('EVN20', 'DataInizio', NULL, '2006-02-13 00:00:00', NULL, NULL);
INSERT INTO contentsearch (contentid, attrname, textvalue, datevalue, numvalue, langcode) VALUES ('EVN20', 'DataFine', NULL, '2006-02-20 00:00:00', NULL, NULL);
INSERT INTO contentsearch (contentid, attrname, textvalue, datevalue, numvalue, langcode) VALUES ('ART111', 'Data', NULL, '2006-12-13 00:00:00', NULL, NULL);
INSERT INTO contentsearch (contentid, attrname, textvalue, datevalue, numvalue, langcode) VALUES ('ART120', 'Data', NULL, '2009-03-28 00:00:00', NULL, NULL);
INSERT INTO contentsearch (contentid, attrname, textvalue, datevalue, numvalue, langcode) VALUES ('ART120', 'Numero', NULL, NULL, 7, NULL);




INSERT INTO contentattributeroles (contentid, attrname, rolename) VALUES ('ART122', 'Titolo', 'jacms:title');
INSERT INTO contentattributeroles (contentid, attrname, rolename) VALUES ('ART121', 'Titolo', 'jacms:title');
INSERT INTO contentattributeroles (contentid, attrname, rolename) VALUES ('ART120', 'Titolo', 'jacms:title');
INSERT INTO contentattributeroles (contentid, attrname, rolename) VALUES ('ART111', 'Titolo', 'jacms:title');
INSERT INTO contentattributeroles (contentid, attrname, rolename) VALUES ('ART102', 'Titolo', 'jacms:title');
INSERT INTO contentattributeroles (contentid, attrname, rolename) VALUES ('ART180', 'Titolo', 'jacms:title');
INSERT INTO contentattributeroles (contentid, attrname, rolename) VALUES ('ART112', 'Titolo', 'jacms:title');
INSERT INTO contentattributeroles (contentid, attrname, rolename) VALUES ('ART104', 'Titolo', 'jacms:title');
INSERT INTO contentattributeroles (contentid, attrname, rolename) VALUES ('ART1', 'Titolo', 'jacms:title');
INSERT INTO contentattributeroles (contentid, attrname, rolename) VALUES ('ART187', 'Titolo', 'jacms:title');
INSERT INTO contentattributeroles (contentid, attrname, rolename) VALUES ('EVN193', 'Titolo', 'jacms:title');
INSERT INTO contentattributeroles (contentid, attrname, rolename) VALUES ('EVN194', 'Titolo', 'jacms:title');
INSERT INTO contentattributeroles (contentid, attrname, rolename) VALUES ('EVN191', 'Titolo', 'jacms:title');
INSERT INTO contentattributeroles (contentid, attrname, rolename) VALUES ('EVN25', 'Titolo', 'jacms:title');
INSERT INTO contentattributeroles (contentid, attrname, rolename) VALUES ('EVN21', 'Titolo', 'jacms:title');
INSERT INTO contentattributeroles (contentid, attrname, rolename) VALUES ('EVN41', 'Titolo', 'jacms:title');
INSERT INTO contentattributeroles (contentid, attrname, rolename) VALUES ('EVN24', 'Titolo', 'jacms:title');
INSERT INTO contentattributeroles (contentid, attrname, rolename) VALUES ('EVN23', 'Titolo', 'jacms:title');
INSERT INTO contentattributeroles (contentid, attrname, rolename) VALUES ('EVN103', 'Titolo', 'jacms:title');
INSERT INTO contentattributeroles (contentid, attrname, rolename) VALUES ('EVN192', 'Titolo', 'jacms:title');
INSERT INTO contentattributeroles (contentid, attrname, rolename) VALUES ('EVN20', 'Titolo', 'jacms:title');




INSERT INTO workcontentrelations (contentid, refcategory) VALUES ('ART102', 'general');
INSERT INTO workcontentrelations (contentid, refcategory) VALUES ('ART102', 'general_cat1');
INSERT INTO workcontentrelations (contentid, refcategory) VALUES ('ART180', 'cat1');
INSERT INTO workcontentrelations (contentid, refcategory) VALUES ('ART180', 'general');
INSERT INTO workcontentrelations (contentid, refcategory) VALUES ('ART180', 'general_cat1');
INSERT INTO workcontentrelations (contentid, refcategory) VALUES ('ART179', 'general');
INSERT INTO workcontentrelations (contentid, refcategory) VALUES ('ART179', 'general_cat1');
INSERT INTO workcontentrelations (contentid, refcategory) VALUES ('ART179', 'general_cat2');
INSERT INTO workcontentrelations (contentid, refcategory) VALUES ('EVN193', 'evento');
INSERT INTO workcontentrelations (contentid, refcategory) VALUES ('EVN193', 'general');
INSERT INTO workcontentrelations (contentid, refcategory) VALUES ('EVN193', 'general_cat2');
INSERT INTO workcontentrelations (contentid, refcategory) VALUES ('EVN192', 'evento');
INSERT INTO workcontentrelations (contentid, refcategory) VALUES ('EVN192', 'general');
INSERT INTO workcontentrelations (contentid, refcategory) VALUES ('EVN192', 'general_cat1');
INSERT INTO workcontentrelations (contentid, refcategory) VALUES ('ART111', 'general');
INSERT INTO workcontentrelations (contentid, refcategory) VALUES ('ART111', 'general_cat1');
INSERT INTO workcontentrelations (contentid, refcategory) VALUES ('ART111', 'general_cat2');
INSERT INTO workcontentrelations (contentid, refcategory) VALUES ('ART120', 'general_cat3');
INSERT INTO workcontentrelations (contentid, refcategory) VALUES ('ART120', 'general');
INSERT INTO workcontentrelations (contentid, refcategory) VALUES ('ART120', 'general_cat2');
INSERT INTO workcontentrelations (contentid, refcategory) VALUES ('ART121', 'general_cat3');
INSERT INTO workcontentrelations (contentid, refcategory) VALUES ('ART121', 'general');
INSERT INTO workcontentrelations (contentid, refcategory) VALUES ('ART122', 'general_cat3');
INSERT INTO workcontentrelations (contentid, refcategory) VALUES ('ART122', 'general');
INSERT INTO workcontentrelations (contentid, refcategory) VALUES ('ART112', 'general');
INSERT INTO workcontentrelations (contentid, refcategory) VALUES ('ART112', 'general_cat2');




INSERT INTO workcontentsearch (contentid, attrname, textvalue, datevalue, numvalue, langcode) VALUES ('ART111', 'Data', NULL, '2006-12-13 00:00:00', NULL, NULL);
INSERT INTO workcontentsearch (contentid, attrname, textvalue, datevalue, numvalue, langcode) VALUES ('ART120', 'Data', NULL, '2009-03-28 00:00:00', NULL, NULL);
INSERT INTO workcontentsearch (contentid, attrname, textvalue, datevalue, numvalue, langcode) VALUES ('ART120', 'Numero', NULL, NULL, 7, NULL);
INSERT INTO workcontentsearch (contentid, attrname, textvalue, datevalue, numvalue, langcode) VALUES ('ART121', 'Data', NULL, '2009-03-30 00:00:00', NULL, NULL);
INSERT INTO workcontentsearch (contentid, attrname, textvalue, datevalue, numvalue, langcode) VALUES ('ART121', 'Numero', NULL, NULL, 78, NULL);
INSERT INTO workcontentsearch (contentid, attrname, textvalue, datevalue, numvalue, langcode) VALUES ('ART112', 'Data', NULL, '2006-02-13 00:00:00', NULL, NULL);
INSERT INTO workcontentsearch (contentid, attrname, textvalue, datevalue, numvalue, langcode) VALUES ('ART104', 'Data', NULL, '2007-01-04 00:00:00', NULL, NULL);
INSERT INTO workcontentsearch (contentid, attrname, textvalue, datevalue, numvalue, langcode) VALUES ('ART1', 'Data', NULL, '2004-03-10 00:00:00', NULL, NULL);
INSERT INTO workcontentsearch (contentid, attrname, textvalue, datevalue, numvalue, langcode) VALUES ('ART179', 'Data', NULL, '2009-07-16 00:00:00', NULL, NULL);
INSERT INTO workcontentsearch (contentid, attrname, textvalue, datevalue, numvalue, langcode) VALUES ('EVN25', 'Titolo', 'TEATRO DELLE MERAVIGLIE', NULL, NULL, 'it');
INSERT INTO workcontentsearch (contentid, attrname, textvalue, datevalue, numvalue, langcode) VALUES ('EVN25', 'Titolo', 'TEATRO DELLE MERAVIGLIE', NULL, NULL, 'en');
INSERT INTO workcontentsearch (contentid, attrname, textvalue, datevalue, numvalue, langcode) VALUES ('EVN25', 'DataInizio', NULL, '2007-12-12 00:00:00', NULL, NULL);
INSERT INTO workcontentsearch (contentid, attrname, textvalue, datevalue, numvalue, langcode) VALUES ('EVN25', 'DataFine', NULL, '2007-12-22 00:00:00', NULL, NULL);
INSERT INTO workcontentsearch (contentid, attrname, textvalue, datevalue, numvalue, langcode) VALUES ('EVN41', 'Titolo', 'Sagra della ciliegia', NULL, NULL, 'it');
INSERT INTO workcontentsearch (contentid, attrname, textvalue, datevalue, numvalue, langcode) VALUES ('EVN41', 'Titolo', 'Sagra della ciliegia', NULL, NULL, 'en');
INSERT INTO workcontentsearch (contentid, attrname, textvalue, datevalue, numvalue, langcode) VALUES ('EVN41', 'DataInizio', NULL, '2008-01-06 00:00:00', NULL, NULL);
INSERT INTO workcontentsearch (contentid, attrname, textvalue, datevalue, numvalue, langcode) VALUES ('EVN41', 'DataFine', NULL, '2008-01-24 00:00:00', NULL, NULL);
INSERT INTO workcontentsearch (contentid, attrname, textvalue, datevalue, numvalue, langcode) VALUES ('EVN103', 'Titolo', 'Titolo Contenuto 1 Coach', NULL, NULL, 'it');
INSERT INTO workcontentsearch (contentid, attrname, textvalue, datevalue, numvalue, langcode) VALUES ('EVN103', 'Titolo', 'Titolo Contenuto 1 Coach', NULL, NULL, 'en');
INSERT INTO workcontentsearch (contentid, attrname, textvalue, datevalue, numvalue, langcode) VALUES ('EVN103', 'DataInizio', NULL, '1999-04-15 00:00:00', NULL, NULL);
INSERT INTO workcontentsearch (contentid, attrname, textvalue, datevalue, numvalue, langcode) VALUES ('EVN103', 'DataFine', NULL, '2000-04-14 00:00:00', NULL, NULL);
INSERT INTO workcontentsearch (contentid, attrname, textvalue, datevalue, numvalue, langcode) VALUES ('EVN193', 'Titolo', 'Titolo C - Evento 3', NULL, NULL, 'it');
INSERT INTO workcontentsearch (contentid, attrname, textvalue, datevalue, numvalue, langcode) VALUES ('EVN193', 'Titolo', 'Title D - Evento 3', NULL, NULL, 'en');
INSERT INTO workcontentsearch (contentid, attrname, textvalue, datevalue, numvalue, langcode) VALUES ('EVN193', 'DataInizio', NULL, '2017-04-12 00:00:00', NULL, NULL);
INSERT INTO workcontentsearch (contentid, attrname, textvalue, datevalue, numvalue, langcode) VALUES ('EVN193', 'DataFine', NULL, '2017-09-12 00:00:00', NULL, NULL);
INSERT INTO workcontentsearch (contentid, attrname, textvalue, datevalue, numvalue, langcode) VALUES ('EVN194', 'Titolo', 'Titolo D - Evento 4', NULL, NULL, 'it');
INSERT INTO workcontentsearch (contentid, attrname, textvalue, datevalue, numvalue, langcode) VALUES ('EVN194', 'Titolo', 'Title A - Event 4', NULL, NULL, 'en');
INSERT INTO workcontentsearch (contentid, attrname, textvalue, datevalue, numvalue, langcode) VALUES ('EVN194', 'DataInizio', NULL, '2022-02-19 00:00:00', NULL, NULL);
INSERT INTO workcontentsearch (contentid, attrname, textvalue, datevalue, numvalue, langcode) VALUES ('EVN194', 'DataFine', NULL, '2022-04-19 00:00:00', NULL, NULL);
INSERT INTO workcontentsearch (contentid, attrname, textvalue, datevalue, numvalue, langcode) VALUES ('EVN191', 'Titolo', 'Titolo A - Evento 1', NULL, NULL, 'it');
INSERT INTO workcontentsearch (contentid, attrname, textvalue, datevalue, numvalue, langcode) VALUES ('EVN191', 'Titolo', 'Title C - Event 1', NULL, NULL, 'en');
INSERT INTO workcontentsearch (contentid, attrname, textvalue, datevalue, numvalue, langcode) VALUES ('EVN191', 'DataInizio', NULL, '1996-04-17 00:00:00', NULL, NULL);
INSERT INTO workcontentsearch (contentid, attrname, textvalue, datevalue, numvalue, langcode) VALUES ('EVN191', 'DataFine', NULL, '1996-06-17 00:00:00', NULL, NULL);
INSERT INTO workcontentsearch (contentid, attrname, textvalue, datevalue, numvalue, langcode) VALUES ('EVN21', 'Titolo', 'Mostra Delle Fragole', NULL, NULL, 'it');
INSERT INTO workcontentsearch (contentid, attrname, textvalue, datevalue, numvalue, langcode) VALUES ('EVN21', 'Titolo', 'Mostra Delle Fragole', NULL, NULL, 'en');
INSERT INTO workcontentsearch (contentid, attrname, textvalue, datevalue, numvalue, langcode) VALUES ('EVN21', 'DataInizio', NULL, '2006-01-13 00:00:00', NULL, NULL);
INSERT INTO workcontentsearch (contentid, attrname, textvalue, datevalue, numvalue, langcode) VALUES ('EVN21', 'DataFine', NULL, '2006-03-04 00:00:00', NULL, NULL);
INSERT INTO workcontentsearch (contentid, attrname, textvalue, datevalue, numvalue, langcode) VALUES ('EVN24', 'Titolo', 'Castello dei bambini', NULL, NULL, 'it');
INSERT INTO workcontentsearch (contentid, attrname, textvalue, datevalue, numvalue, langcode) VALUES ('EVN24', 'Titolo', 'Castello dei bambini', NULL, NULL, 'en');
INSERT INTO workcontentsearch (contentid, attrname, textvalue, datevalue, numvalue, langcode) VALUES ('EVN24', 'DataInizio', NULL, '2009-03-18 00:00:00', NULL, NULL);
INSERT INTO workcontentsearch (contentid, attrname, textvalue, datevalue, numvalue, langcode) VALUES ('EVN24', 'DataFine', NULL, '2009-03-26 00:00:00', NULL, NULL);
INSERT INTO workcontentsearch (contentid, attrname, textvalue, datevalue, numvalue, langcode) VALUES ('EVN23', 'Titolo', 'Collezione Ingri', NULL, NULL, 'it');
INSERT INTO workcontentsearch (contentid, attrname, textvalue, datevalue, numvalue, langcode) VALUES ('EVN23', 'Titolo', 'Collezione Ingri', NULL, NULL, 'en');
INSERT INTO workcontentsearch (contentid, attrname, textvalue, datevalue, numvalue, langcode) VALUES ('EVN23', 'DataInizio', NULL, '2008-02-13 00:00:00', NULL, NULL);
INSERT INTO workcontentsearch (contentid, attrname, textvalue, datevalue, numvalue, langcode) VALUES ('EVN23', 'DataFine', NULL, '2008-02-22 00:00:00', NULL, NULL);
INSERT INTO workcontentsearch (contentid, attrname, textvalue, datevalue, numvalue, langcode) VALUES ('EVN192', 'Titolo', 'Titolo B - Evento 2', NULL, NULL, 'it');
INSERT INTO workcontentsearch (contentid, attrname, textvalue, datevalue, numvalue, langcode) VALUES ('EVN192', 'Titolo', 'Title B - Event 2', NULL, NULL, 'en');
INSERT INTO workcontentsearch (contentid, attrname, textvalue, datevalue, numvalue, langcode) VALUES ('EVN192', 'DataInizio', NULL, '1999-04-14 00:00:00', NULL, NULL);
INSERT INTO workcontentsearch (contentid, attrname, textvalue, datevalue, numvalue, langcode) VALUES ('EVN192', 'DataFine', NULL, '1999-06-14 00:00:00', NULL, NULL);
INSERT INTO workcontentsearch (contentid, attrname, textvalue, datevalue, numvalue, langcode) VALUES ('EVN20', 'Titolo', 'Mostra Zootecnica', NULL, NULL, 'it');
INSERT INTO workcontentsearch (contentid, attrname, textvalue, datevalue, numvalue, langcode) VALUES ('EVN20', 'Titolo', 'Mostra Zootecnica', NULL, NULL, 'en');
INSERT INTO workcontentsearch (contentid, attrname, textvalue, datevalue, numvalue, langcode) VALUES ('EVN20', 'DataInizio', NULL, '2006-02-13 00:00:00', NULL, NULL);
INSERT INTO workcontentsearch (contentid, attrname, textvalue, datevalue, numvalue, langcode) VALUES ('EVN20', 'DataFine', NULL, '2006-02-20 00:00:00', NULL, NULL);




INSERT INTO workcontentattributeroles (contentid, attrname, rolename) VALUES ('ART122', 'Titolo', 'jacms:title');
INSERT INTO workcontentattributeroles (contentid, attrname, rolename) VALUES ('ART121', 'Titolo', 'jacms:title');
INSERT INTO workcontentattributeroles (contentid, attrname, rolename) VALUES ('ART120', 'Titolo', 'jacms:title');
INSERT INTO workcontentattributeroles (contentid, attrname, rolename) VALUES ('ART111', 'Titolo', 'jacms:title');
INSERT INTO workcontentattributeroles (contentid, attrname, rolename) VALUES ('ART102', 'Titolo', 'jacms:title');
INSERT INTO workcontentattributeroles (contentid, attrname, rolename) VALUES ('ART180', 'Titolo', 'jacms:title');
INSERT INTO workcontentattributeroles (contentid, attrname, rolename) VALUES ('ART112', 'Titolo', 'jacms:title');
INSERT INTO workcontentattributeroles (contentid, attrname, rolename) VALUES ('ART104', 'Titolo', 'jacms:title');
INSERT INTO workcontentattributeroles (contentid, attrname, rolename) VALUES ('ART1', 'Titolo', 'jacms:title');
INSERT INTO workcontentattributeroles (contentid, attrname, rolename) VALUES ('ART179', 'Titolo', 'jacms:title');
INSERT INTO workcontentattributeroles (contentid, attrname, rolename) VALUES ('ART187', 'Titolo', 'jacms:title');
INSERT INTO workcontentattributeroles (contentid, attrname, rolename) VALUES ('EVN193', 'Titolo', 'jacms:title');
INSERT INTO workcontentattributeroles (contentid, attrname, rolename) VALUES ('EVN194', 'Titolo', 'jacms:title');
INSERT INTO workcontentattributeroles (contentid, attrname, rolename) VALUES ('EVN191', 'Titolo', 'jacms:title');
INSERT INTO workcontentattributeroles (contentid, attrname, rolename) VALUES ('EVN25', 'Titolo', 'jacms:title');
INSERT INTO workcontentattributeroles (contentid, attrname, rolename) VALUES ('EVN21', 'Titolo', 'jacms:title');
INSERT INTO workcontentattributeroles (contentid, attrname, rolename) VALUES ('EVN41', 'Titolo', 'jacms:title');
INSERT INTO workcontentattributeroles (contentid, attrname, rolename) VALUES ('EVN24', 'Titolo', 'jacms:title');
INSERT INTO workcontentattributeroles (contentid, attrname, rolename) VALUES ('EVN23', 'Titolo', 'jacms:title');
INSERT INTO workcontentattributeroles (contentid, attrname, rolename) VALUES ('EVN103', 'Titolo', 'jacms:title');
INSERT INTO workcontentattributeroles (contentid, attrname, rolename) VALUES ('EVN192', 'Titolo', 'jacms:title');
INSERT INTO workcontentattributeroles (contentid, attrname, rolename) VALUES ('EVN20', 'Titolo', 'jacms:title');