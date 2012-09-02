package org.dotahq.entity.hero

import org.dotahq.entity.hero.strategy.LanePosition
import org.dotahq.entity.item.ItemSet
import org.dotahq.entity.hero.skill.SkillBuild

class Hero {
	private HeroBaseStats base
	private List <ItemSet> itemSets
	private List <SkillBuild> skillBuilds
	private LanePosition lanePosition

	public Hero (HeroBaseStats base){
		this.base = base
		this.itemSets = []
		this.skillBuilds = []
		this.lanePosition = null
	}

	public HeroBaseStats getBase(){
		return base
	}
}
