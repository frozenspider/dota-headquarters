package org.dotahq.ui.strategy

import groovy.swing.SwingBuilder

import java.awt.BorderLayout as BL
import java.awt.Color
import java.awt.image.BufferedImage

import javax.swing.JPanel

import org.dotahq.entity.StrategyLayout
import org.dotahq.entity.hero.HeroBaseStats
import org.dotahq.entity.hero.strategy.LanePosition as LP

class StrategyLayoutPanel extends JPanel {
	
	private EntityContainerPanel shortLane, midLane, longLane, roam, jungle, jungle2
	private final Map<LP, EntityContainerPanel> positionToPanelMap
	private final StrategyLayout strategy
	
	public StrategyLayoutPanel(BufferedImage mapImage){
		int iconSize = 48
		this.strategy = new StrategyLayout()
		
		this.setLayout(new BL())
		def content = (new SwingBuilder()).panel(
				new ImageRenderingPanel(image: mapImage),
				constraints: BL.CENTER,
				layout: null
				) {
					def border = {
						lineBorder(color: Color.black)
					}
					def rearrangeCallback = this.&panelsRearranged
					this.shortLane = panel(new EntityContainerPanel("Short", iconSize, rearrangeCallback))
					this.shortLane.setPinPoint(80, 150)
					this.midLane = panel(new EntityContainerPanel("Middle", iconSize, rearrangeCallback), border: border())
					this.midLane.setPinPoint(200, 260)
					this.longLane = panel(new EntityContainerPanel("Long", iconSize, rearrangeCallback), border: border())
					this.longLane.setPinPoint(400, 420)
					this.roam = panel(new EntityContainerPanel("Roam", iconSize, rearrangeCallback), border: border())
					this.roam.setPinPoint(310, 260)
					this.jungle = panel(new EntityContainerPanel("Jungle", iconSize, rearrangeCallback), border: border())
					this.jungle.setPinPoint(270, 360)
					this.jungle2 = panel(new EntityContainerPanel("Enemy Jungle", iconSize, rearrangeCallback), border: border())
					this.jungle2.setPinPoint(200, 100)
					def trashCan = panel(new EntityContainerPanel("DROP TO REMOVE", iconSize, rearrangeCallback, true),
							border: border())
					trashCan.setBackground(Color.WHITE)
					trashCan.setPinPoint(70, 420)
					label(text: "mapPanel", background: Color.red)
				}
		this.add(content, BL.CENTER)
		this.positionToPanelMap = [
					(LP.SHORT): this.shortLane,
					(LP.MID): this.midLane,
					(LP.LONG): this.longLane,
					(LP.ROAM): this.roam,
					(LP.JUNGLE_FRIENDLY): this.jungle,
					(LP.JUNGLE_ENEMY): this.jungle2
				]
	}
	
	/**
	 * Called from entity panels when their content is rearranged.
	 *
	 * @param draggedAway
	 * 				dragged away hero
	 * @param dragAwayUnawareContainer
	 * 				a container, from which the element was dragged
	 * 				and which doesn't yet know about it (can be null)
	 *
	 * @return whether or not current layout is admissible (if not, changes will be reverted)
	 */
	private boolean panelsRearranged(HeroBaseStats draggedAway, JPanel dragAwayUnawareContainer) {
		StrategyLayout strategy = new StrategyLayout()
		for (e in positionToPanelMap) {
			def refinedValues = e.value.is(dragAwayUnawareContainer) ? e.value.data - draggedAway : e.value.data
			for (hero in refinedValues) {
				if (strategy.containsHero(hero)) {
					return false
				}
				strategy.put(hero, e.key)
			}
		}
		if (!validateStrategy(strategy)) {
			return false
		}
		this.strategy.setTo(strategy)
		println this.strategy
		return true
	}
	
	private boolean validateStrategy(StrategyLayout strategy) {
		return strategy.size() <= 5
	}
	
	
	public boolean setStrategy(StrategyLayout strategy){
		if (!validateStrategy(strategy)){
			return false
		}
		this.strategy.setTo(strategy)
		Map <LP, List <HeroBaseStats>> laneHeroes = [:]
		for (e in strategy.heroesToLanesMap()) {
			if (!laneHeroes.containsKey(e.value)) {
				laneHeroes << [(e.value): []]
			}
			laneHeroes[e.value] << e.key
		}
		for (e in positionToPanelMap) {
			e.value.setData(laneHeroes[e.key])
		}
		return true
	}
	
	public StrategyLayout getStrategy(){
		return strategy.copy()
	}
}
