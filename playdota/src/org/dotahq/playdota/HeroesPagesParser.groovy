/**
 * STEP 3.1: Using downloaded hero pages, parses actual hero stats.
 *
 * @author FS
 */

package org.dotahq.playdota

import org.dotahq.entity.hero.tavern.Tavern
import org.dotahq.entity.hero.HeroBaseStats
import groovy.io.FileType
import org.hibernate.cfg.AnnotationConfiguration

import static org.dotahq.playdota.ScriptPath.folder
import org.dotahq.entity.Side
import org.dotahq.entity.Attribute

File heroesDataFile = new File("serializedHeroData", folder)
if (heroesDataFile.exists()) {
	throw new IllegalStateException("\"${heroesDataFile.getAbsolutePath()}\" already exist. Are we already done here?")
}

def tavernList = []
folder.eachFile(FileType.DIRECTORIES) { File dir ->
	final String tavernName = dir.getName()[3..-1]
	final Side side
	if (tavernName.startsWith("Sentinel")) {
		side = Side.SENTINEL
	} else if (tavernName.startsWith("Scourge")) {
		side = Side.SCOURGE
	} else {
		throw new RuntimeException("Unknown side for a tavern: $tavernName")
	}

	Tavern tavern = new Tavern(name: tavernName, side: side, heroBases: [])
	tavernList << tavern
	dir.eachFile { File heroPageFile ->
		def heroPageText = heroPageFile.getText("UTF-8")

		def nameTitleMatcher = heroPageText =~ /(?sm)<h1><img src="http:\/\/media.playdota.com\/[^"]+" alt="([^"]+)"\/><\/h1>[\s]+<h1 class="class">([^<]+)<\/h1>/
		assert nameTitleMatcher.find()

		def heroName = nameTitleMatcher[0][2]
		def heroTitle = nameTitleMatcher[0][1]

		def strnMatcher = heroPageText =~ /(?sm)<li>Strength <img src="http:\/\/media.playdota.com\/site\/strength(-c)?.jpg" alt="[^"]+"\/><br \/><span>(\d+) \+ ([.\d]+)<\/span><\/li>/
		assert strnMatcher.find()
		def strn = strnMatcher[0][2] as Integer
		def strnGrowth = strnMatcher[0][3] as BigDecimal

		def agilMatcher = heroPageText =~ /(?sm)<li>Agility <img src="http:\/\/media.playdota.com\/site\/agility(-c)?.jpg" alt="[^"]+"\/><br \/><span>(\d+) \+ ([.\d]+)<\/span><\/li>/
		assert agilMatcher.find()
		def agil = agilMatcher[0][2] as Integer
		def agilGrowth = agilMatcher[0][3] as BigDecimal

		def intlMatcher = heroPageText =~ /(?sm)<li>Intelligence <img src="http:\/\/media.playdota.com\/site\/intelligence(-c)?.jpg" alt="[^"]+"\/><br \/><span>(\d+) \+ ([.\d]+)<\/span><\/li>/
		assert intlMatcher.find()
		def intl = intlMatcher[0][2] as Integer
		def intlGrowth = intlMatcher[0][3] as BigDecimal

		def mainAttrList = [strnMatcher[0][1], agilMatcher[0][1], intlMatcher[0][1]].collect { it ? 1 : 0}
		assert mainAttrList.sum() == 1
		def mainAttr = Attribute.with {[STR, AGI, INT]}[mainAttrList.indexOf(1)]


		def damageMatcher = heroPageText =~ /<label>Damage:<\/label>\s*(\d+)\s*-\s*(\d+)/
		assert damageMatcher.find()
		def minDmg = damageMatcher[0][1] as Integer
		def maxDmg = damageMatcher[0][2] as Integer

		def armorMatcher = heroPageText =~ /<label>Armor:<\/label>\s*([\d.]+)/
		assert armorMatcher.find()
		def armor = armorMatcher[0][1] as BigDecimal

		def batMatcher = heroPageText =~ /<label>Base Attack Time:<\/label>\s*([\d.]+)/
		assert batMatcher.find()
		def bat = batMatcher[0][1] as BigDecimal

		def msMatcher = heroPageText =~ /<label>Movespeed:<\/label>\s*(\d+)/
		assert msMatcher.find()
		def ms = msMatcher[0][1] as Integer

		def rangeMatcher = heroPageText =~ /(?i)<label>Attack Range:<\/label>\s*(\d+)\s*(\(melee\))?/
		assert rangeMatcher.find()
		def range = rangeMatcher[0][1] as Integer
		def melee = rangeMatcher[0][2] as Boolean

		def heroBase = new HeroBaseStats(
			name: heroName,
			title: heroTitle,
			minDamage: minDmg,
			maxDamage: maxDmg,
			attackTime: bat,
			range: range,
			melee: melee,
			armor: armor,
			moveSpeed: ms,
			strn: strn,
			agil: agil,
			intl: intl,
			strnGrowth: strnGrowth,
			agilGrowth: agilGrowth,
			intlGrowth: intlGrowth,
			mainAttr: mainAttr
		)
		tavern.heroBases << heroBase
		println "${heroName}, the ${heroTitle}"
		println "     Strength:     $strn + $strnGrowth ${mainAttrList[0] ? '(Main)' : ''}"
		println "     Agility:      $agil + $agilGrowth ${mainAttrList[1] ? '(Main)' : ''}"
		println "     Intelligence: $intl + $intlGrowth ${mainAttrList[2] ? '(Main)' : ''}"
		println "     $mainAttr"
		println "  Damage:      $minDmg to $maxDmg"
		println "  Range:       $range ${melee ? '(melee)' : ''}"
		println "  Attack Time: $bat"
		println "  Armor:       $armor"
		println "  Movespeed:   $ms"
		println "===="
	}
}

heroesDataFile.withObjectOutputStream {it.writeObject(tavernList)}