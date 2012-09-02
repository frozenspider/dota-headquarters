package org.dotahq.entity.hero

import groovy.transform.EqualsAndHashCode

import org.dotahq.entity.Attribute
/**
 * These are the hero "level-based" stats - those depending on a hero level: strength, agility and intelligence.
 *
 * @author FS
 */
@EqualsAndHashCode(includeFields=true)
class HeroLevelStats {
	
	private final def strnGrowth
	private final def agilGrowth
	private final def intlGrowth
	private final int mainAttr
	
	int strn
	int agil
	int intl
	
	public HeroLevelStats(Number strnGrowth, Number agilGrowth, Number intlGrowth, Attribute mainAttr) {
		this.strnGrowth = strnGrowth
		this.agilGrowth = agilGrowth
		this.intlGrowth = intlGrowth
		this.mainAttr = mainAttr as int
		setLevel(1)
	}
	
	def setLevel(int level){
		if (!(level in 1..25)) throw new IllegalArgumentException("Level must be between 1 and 25")
		
		def floor = { real -> (int)real }
		
		this.strn = floor(this.strnGrowth * (level-1))
		this.agil = floor(this.agilGrowth * (level-1))
		this.intl = floor(this.intlGrowth * (level-1))
	}
}
