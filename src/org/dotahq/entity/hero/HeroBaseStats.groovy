package org.dotahq.entity.hero

import org.dotahq.entity.Attribute;
import org.dotahq.entity.Side;

import groovy.transform.EqualsAndHashCode

@EqualsAndHashCode
class HeroBaseStats {
	Side side
	
	String name
	String title
	
	Integer minDamage
	Integer maxDamage
	Double  attackTime
	Integer range
	Boolean melee
	
	BigDecimal armor
	
	Integer moveSpeed
	
	Integer    strn
	Integer    agil
	Integer    intl
	BigDecimal strnGrowth
	BigDecimal agilGrowth
	BigDecimal intlGrowth
	Attribute  mainAttr

	@Override
	public String toString() {
		return "HeroBaseStats ($title)"
	}
}
