/**
 * STEP 3.2: Using downloaded hero pages, download hero icons.
 *
 * @author FS
 */

package org.dotahq.playdota

import static org.dotahq.playdota.ScriptPath.folder
import groovy.io.FileType
import org.dotahq.entity.Side
import org.dotahq.entity.hero.tavern.Tavern
import org.dotahq.entity.Attribute
import org.dotahq.entity.hero.HeroBaseStats

File imagesDir = new File("images", folder)
imagesDir.mkdir()

int total = new File("serializedTavernToHeroesLinksMap", folder).withObjectInputStream { it.readObject() }.values().flatten().size()
int curr = 0
folder.eachFile(FileType.DIRECTORIES) { File dir ->
	if (!dir.name.matches(/\d{2} .+/)) return
	dir.eachFile { File heroPageFile ->
		def heroPageText = heroPageFile.getText("UTF-8")

		def nameTitleMatcher = heroPageText =~ /(?sm)<h1><img src="http:\/\/media.playdota.com\/[^"]+" alt="([^"]+)"\/><\/h1>[\s]+<h1 class="class">([^<]+)<\/h1>/
		assert nameTitleMatcher.find()

		def heroName = nameTitleMatcher[0][2]
		def heroTitle = nameTitleMatcher[0][1]

		def iconMatcher = heroPageText =~ /<img src="(http:\/\/media.playdota.com\/hero\/[^"]+)" align="left" class="icon"\/>/
		assert iconMatcher.find()

		String iconLink = iconMatcher[0][1]
		String iconExt = iconLink[(iconLink.lastIndexOf('.') + 1)..-1]
		def iconFile = new File("$heroTitle.$iconExt", imagesDir)
		if (iconFile.exists()) {
			println "$heroTitle (${++curr} / ${total}) already exists, skipping"
		} else {
			iconFile.withOutputStream { it.write(new URL(iconLink).getBytes()) }
			println "$heroTitle (${++curr} / ${total}) done"
			Thread.sleep(200)
		}
	}
}