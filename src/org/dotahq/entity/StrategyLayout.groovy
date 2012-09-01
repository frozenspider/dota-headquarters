package org.dotahq.entity

import groovy.transform.EqualsAndHashCode;

import java.util.Collection
import java.util.Map

import org.dotahq.entity.hero.HeroBaseStats
import org.dotahq.entity.hero.strategy.LanePosition as LP
import org.dotahq.ui.EntityContainerPanel

@EqualsAndHashCode
class StrategyLayout {
	private final Map<HeroBaseStats, LP> heroes
	
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
	
	@Override
	public String toString() {
		return "StrategyLayout [" + heroes + "]";
	}
}
