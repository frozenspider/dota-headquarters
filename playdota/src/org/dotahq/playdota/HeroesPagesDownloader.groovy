/**
 * STEP 2: Using parsed heroes links map, download the actual hero pages.
 *
 * @author FS
 */

package org.dotahq.playdota

import static org.dotahq.playdota.ScriptPath.folder

def padded = { int i ->
	// Adds leading zero if needed
	return "${i < 10 ? 0 : ''}$i"
}

Map tavernToHeroesLinksMap = new File("serializedTavernToHeroesLinksMap", folder).withObjectInputStream { it.readObject() }
int total = tavernToHeroesLinksMap.values().flatten().size()
int curr = 0
int tavernId = 1
for (e in tavernToHeroesLinksMap) {
	String tavernName = e.key
	List<String> links = e.value

	println "Processing $tavernName"
	File folderForTavern = new File("${padded(tavernId)} $tavernName", folder)
	folderForTavern.mkdir()
	int heroId = 1
	for (link in links) {
		String heroFileName = link.split("/").last()
		def heroPageFile = new File("${padded(heroId)} ${heroFileName}.html", folderForTavern)
		if (!heroPageFile.exists()) {
			heroPageFile.write(new URL(link).getText(), "UTF-8")
			println "Wrote ${heroPageFile.name} (${++curr} / ${total})"
			Thread.sleep(500)
		} else {
			println "Wrote ${heroPageFile.name} (${++curr} / ${total}) (already exist, skipping)"
		}
		++heroId
	}
	++tavernId
}

