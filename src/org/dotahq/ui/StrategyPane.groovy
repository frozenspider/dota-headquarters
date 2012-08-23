package org.dotahq.ui

import groovy.swing.SwingBuilder

import java.awt.BorderLayout as BL
import java.awt.Color

import javax.swing.JPanel
import javax.swing.JTextPane

import javax.swing.WindowConstants
import org.dotahq.util.DatabaseContainer
import org.dotahq.util.ImageUtil
import org.dotahq.entity.hero.strategy.LanePosition as LP
import org.dotahq.entity.hero.HeroBaseStats

class StrategyPane extends JPanel {

	private EntityContainerPanel shortLane, midLane, longLane, roam, jungle, jungle2
	private JTextPane commentTextPane
	private final Map<LP, EntityContainerPanel> positionToPanelMap
	private final Map<HeroBaseStats, LP> heroes

	public StrategyPane() {
		this.heroes = [:]
		this.setLayout(new BL())

		int iconSize = 48

		def content = (new SwingBuilder()).panel {
			def border = {
				lineBorder(color: Color.black)
			}
			borderLayout()
			def topPanel = panel(constraints: BL.NORTH, border: border()) {
				borderLayout()
				def heroesPanel = panel(new TavernContentPane(), constraints: BL.EAST, border: border())
				def tavernsPanel = panel(new TavernPane(), constraints: BL.CENTER, border: border())
			}
			def centerPanel = panel(constraints: BL.CENTER, border: border()) {
				borderLayout()
				def mapPanel = panel(
					new ImageRenderingPanel(image: ImageUtil.getMapImage()),
					constraints: BL.CENTER,
					layout: null,
					border: border()
				) {
					def rearrangeCallback = this.&panelsRearranged
					shortLane = panel(new EntityContainerPanel("Short", iconSize, rearrangeCallback), border: border())
					shortLane.setPinPoint(60, 150)
					midLane = panel(new EntityContainerPanel("Middle", iconSize, rearrangeCallback), border: border())
					midLane.setPinPoint(200, 260)
					longLane = panel(new EntityContainerPanel("Long", iconSize, rearrangeCallback), border: border())
					longLane.setPinPoint(400, 420)
					roam = panel(new EntityContainerPanel("Roam", iconSize, rearrangeCallback), border: border())
					roam.setPinPoint(310, 260)
					jungle = panel(new EntityContainerPanel("Jungle", iconSize, rearrangeCallback), border: border())
					jungle.setPinPoint(270, 360)
					jungle2 = panel(new EntityContainerPanel("Enemy Jungle", iconSize, rearrangeCallback), border: border())
					jungle2.setPinPoint(180, 100)
					def trashCan = panel(new EntityContainerPanel("DROP TO REMOVE", iconSize, rearrangeCallback, true),
						border: border())
					trashCan.setBackground(Color.WHITE)
					trashCan.setPinPoint(70, 420)
					label(text: "mapPanel", background: Color.red)
				}
				def heroSettingsPanel = panel(constraints: BL.EAST, border: border()) {
					borderLayout()
					label(text: "heroSettingsPanel", constraints: BL.CENTER)
				}
			}
			def bottomPanel = panel(constraints: BL.SOUTH, border: border()) {
				borderLayout()
				def commentsPanel = panel(constraints: BL.CENTER, border: border()) {
					borderLayout()
					commentTextPane = textPane(constraints: BL.CENTER)
				}
			}
		}
		this.add(content, BL.CENTER)
		this.positionToPanelMap = [
			(LP.SHORT): shortLane,
			(LP.MID): midLane,
			(LP.LONG): longLane,
			(LP.ROAM): roam,
			(LP.JUNGLE_FRIENDLY): jungle,
			(LP.JUNGLE_ENEMY): jungle2
		]
	}

	/**
	 * Called from entity panels when their content is rearranged.
	 * @param draggedAway dragged away hero
	 * @param dragAwayUnawareContainer a container, from which the element was dragged and which doesn't
	 * yet know about it (can be null)
	 * @return whether or not current layout is admissible (if not, changes will be reverted)
	 */
	private boolean panelsRearranged(HeroBaseStats draggedAway, JPanel dragAwayUnawareContainer) {
		println "StrategyPane.panelsRearranged()"
		heroes.clear()
		for (e in positionToPanelMap) {
			def refinedValues = e.value.is(dragAwayUnawareContainer) ? e.value.data - draggedAway : e.value.data
			for (hero in refinedValues) {
				if (heroes.containsKey(hero)) {
					// TODO: Notify user, that duplicates are not allowed
					return false
				}
				heroes << [(hero): e.key]
			}
		}
		println heroes
		return true
	}

	public boolean setStrategy(Map<HeroBaseStats, LP> heroes){
		def backup = this.heroes
		this.heroes.clear()
		this.heroes << heroes
		if (!panelsRearranged(null, null)){
			this.heroes.clear()
			this.heroes << backup
			return false
		}
		return true
	}

	public static void main(String[] args) {
		new DatabaseContainer()
		new SwingBuilder().frame(
			title: "Strategy Pane Demo",
			size: [640, 750],
			locationRelativeTo: null,
			defaultCloseOperation: WindowConstants.DISPOSE_ON_CLOSE,
			show: true,
			layout: new BL()) {
			widget(new StrategyPane(), constraints: BL.CENTER)
		}
	}
}