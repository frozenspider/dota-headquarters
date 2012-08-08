package org.dotahq.entity.hero

import groovy.transform.EqualsAndHashCode

import org.dotahq.entity.Attribute

@EqualsAndHashCode(includeFields=true)
class HeroLevelStats {
	
	private final def strnGrowth
	private final def agilGrowth
	private final def intlGrowth
	private final int mainAttr
	
	int strn
	int agil
	int intl
	
	int damage
	def armor	
	public HeroLevelStats(def strnGrowth, def agilGrowth, def intlGrowth, Attribute mainAttr) {
		this.strnGrowth = strnGrowth
		this.agilGrowth = agilGrowth
		this.intlGrowth = intlGrowth
		this.mainAttr = mainAttr as int
		setLevel(1)
	}
	
	def setLevel(int level){
		if (!(level in 1..25)) throw new IllegalArgumentException("Level must be between 1 and 25")
		
		def floor = { real -> (int)real }
		def list = { Object... obj -> new ArrayList(Arrays.asList(obj))}
		
		this.strn = floor(this.strnGrowth * (level-1))
		this.agil = floor(this.agilGrowth * (level-1))
		this.intl = floor(this.intlGrowth * (level-1))
		
		this.damage = list(this.strn, this.agil, this.intl)[this.mainAttr]
		this.armor = agil / 7.0g
	}
}
