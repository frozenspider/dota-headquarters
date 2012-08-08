package org.dotahq.entity.hero

import static org.dotahq.entity.Attribute.*
import static org.junit.Assert.*

import org.dotahq.entity.Damage
import org.junit.Test

class HeroLevelStatsTest {
	
	@Test
	public final void testHeroStats() {
		assert new HeroLevelStats(0, 0, 0, STR) == new HeroLevelStats(0, 0, 0, STR)
		assert new HeroLevelStats(0, 0, 0, STR) != new HeroLevelStats(0, 1, 0, STR)
		assert new HeroLevelStats(0, 0, 0, AGI) != new HeroLevelStats(0, 0, 0, INT)
	}
	
	@Test
	public final void testSetLevel() {
		def heroLvlStats =  new HeroLevelStats(1,2,3,STR)
		assert heroLvlStats.strn == 0
		assert heroLvlStats.agil == 0
		assert heroLvlStats.intl == 0
		assert heroLvlStats.damage == heroLvlStats.strn
		assert heroLvlStats.armor == 0
		
		heroLvlStats.setLevel(25)
		assert heroLvlStats.strn == 1*24
		assert heroLvlStats.agil == 2*24
		assert heroLvlStats.intl == 3*24
		assert heroLvlStats.damage == heroLvlStats.strn
		assert heroLvlStats.armor == 2*24/7
		
		heroLvlStats.setLevel(11)
		assert heroLvlStats.strn == 1*10
		assert heroLvlStats.agil == 2*10
		assert heroLvlStats.intl == 3*10
		assert heroLvlStats.damage == heroLvlStats.strn
		assert heroLvlStats.armor == 2*10/7
	}
	
	@Test
	public final void testSetLevelTerrorblade() {
		def heroLvlStats =  new HeroLevelStats(1.9g, 3.2g, 1.75g, AGI)
		
		heroLvlStats.setLevel(2)
		assert heroLvlStats.strn == 1
		assert heroLvlStats.agil == 3
		assert heroLvlStats.intl == 1
		assert heroLvlStats.damage == heroLvlStats.agil
		assert (0.0..1.0).containsWithinBounds(heroLvlStats.armor)
		
		heroLvlStats.setLevel(25)
		assert heroLvlStats.strn == 45
		assert heroLvlStats.agil == 76
		assert heroLvlStats.intl == 42
		assert heroLvlStats.damage == heroLvlStats.agil
		assert (10.0..11.0).containsWithinBounds(heroLvlStats.armor)
	}
	
	/*-
	 @Test
	 public final void testSetLevelTerrorblade() {
	 def heroStats =  new HeroLevelStats(
	 new HeroBase([
	 minDamage: 48,
	 maxDamage: 54,
	 armor: 5.22g,
	 attrs: [15,22,19],
	 attrsPerLevel: [1.9g, 3.2g, 1.75g],
	 mainAttr: AGI,
	 ])
	 )
	 heroStats.setLevel(2)
	 assert heroStats.strn == 16
	 assert heroStats.agil == 25
	 assert heroStats.intl == 20
	 assert heroStats.damage == (new Damage(51, 57))
	 assert Math.round(heroStats.armor) == 6
	 heroStats.setLevel(25)
	 assert heroStats.strn == 60
	 assert heroStats.agil == 98
	 assert heroStats.intl == 61
	 assert heroStats.damage == (new Damage(124, 130))
	 assert Math.round(heroStats.armor) == 16
	 }*/
}