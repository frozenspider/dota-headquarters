package org.dotahq.entity

import groovy.transform.EqualsAndHashCode

import org.dotahq.entity.hero.HeroBaseStats
import org.dotahq.entity.hero.strategy.LanePosition as LP

/**
 * A strategy layout - information about used heroes, their lanes, etc.
 *  
 * @author FS
 */
class StrategyLayout {
	
	private final Map<HeroBaseStats, LP> heroes
	String comment = ""
	
	public StrategyLayout(){
		this.heroes = [:]
	}
	
	StrategyLayout copy(){
		return new StrategyLayout().setTo(this)
	}
	
	StrategyLayout clear(){
		this.heroes.clear()
	}
	
	int size(){
		return this.heroes.size()
	}
	
	boolean containsHero(HeroBaseStats hero){
		return this.heroes.containsKey(hero)
	}
	
	StrategyLayout putIfNew(HeroBaseStats hero, LP lane) {
		if (!this.heroes.containsKey(hero)) {
			this.heroes.put(hero, lane)
		}
		return this
	}
	
	StrategyLayout setTo(StrategyLayout other) {
		this.heroes.clear()
		this.heroes << other.heroes
		return this
	}
	
	Map<HeroBaseStats, LP> heroesToLanesMap(){
		return ([:] << heroes)
	}
	
	//
	// Auto-generated and junk
	//
	@Override
	public String toString() {
		return "StrategyLayout [$heroes, '$comment']"
	}
	
	@Override
	public int hashCode() {
		final int prime = 31
		int result = 1
		result = prime * result + heroes?.hashCode()
		result = prime * result + comment?.hashCode()
		return result
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this.is(obj)) return true
		if (!obj) return false
		if (getClass() != obj.getClass()) return false
		StrategyLayout other = (StrategyLayout) obj
		if (heroes != other.heroes) return false
		if (comment != other.comment) return false
		return true
	}
	
}
