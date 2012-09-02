/**
 * STEP 1: Download playdota's heroes page and parse a links on it.
 *
 * @author FS
 */

package org.dotahq.playdota

import static org.dotahq.playdota.ScriptPath.folder

def targetFile = new File("serializedTavernToHeroesLinksMap", folder)
if (targetFile.exists()) {
	throw new IllegalStateException("\"${targetFile.getAbsolutePath()}\" already exist. Are we already done here?")
}

def tavernToHeroesLinksMap = [:]

def url = new URL("http://www.playdota.com/heroes")
def content = url.getText()
def contentMatcher = content =~ /(?s)<div class="herolist" style=''>(.+?)<\/div>/
def allMatches = contentMatcher.findAll()
assert allMatches
for (match in allMatches) {
	def tavernMatch = (match =~ /(?s)<img src="http:\/\/media.playdota.com\/site\/hero\/([^.]+)[a-z." ]+\/><\/center>.+?<ul>(.+?)<\/ul>/)[0]
	def tavernName = tavernMatch[1].trim().replace("scourage", "scourge").split(" ")*.capitalize().sum {" $it"}.trim()
	def heroLinkMatcher = tavernMatch[2] =~ /<li><a href="([^"]+)"/
	def heroLinks = heroLinkMatcher.findAll().collect { "http://www.playdota.com${it[1]}" }

	tavernToHeroesLinksMap << [(tavernName): heroLinks]
}

assert tavernToHeroesLinksMap.values().flatten().size() == 108 // 6.74c

tavernToHeroesLinksMap.each {
	println it.key
	println it.value
}

targetFile.withObjectOutputStream {it.writeObject(tavernToHeroesLinksMap)}
