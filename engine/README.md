entando-core-engine
============

**Text encryption configuration**

Some plugins need to encrypt sensitive configuration data stored into the database. The key used for this encryption can be configured by editing the properties file ```security.properties```; you can find this file in ```src/main/config``` path of your project.

Please note that if you change the key the old data can't be decrypted anymore and must be inserted again.

Currently the BPM plugin and the Digital Exchange plugin use encrypted configuration data.

Entando Core is released under [GNU Lesser General Public License](https://www.gnu.org/licenses/lgpl-2.1.txt) v2.1

Enjoy!

*The Entando Team*
