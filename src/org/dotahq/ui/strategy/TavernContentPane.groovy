package org.dotahq.ui.strategy

import static org.dotahq.util.CustomUtil.*
import static ImageUtil.iconDim
import groovy.swing.SwingBuilder

import java.awt.BorderLayout as BL
import java.awt.Color

import javax.swing.JPanel

import org.dotahq.entity.hero.HeroBaseStats

import org.dotahq.util.ImageUtil

class TavernContentPane extends JPanel {
	
	private final List<EntityDragDropPanel> heroBaseIcons
	private List<HeroBaseStats> heroBases
	
	public TavernContentPane() {
		this.heroBases = asList()
		this.heroBaseIcons = asList()
		this.setLayout(new BL())
		def content = (new SwingBuilder()).panel(preferredSize: asList(iconDim * 4, iconDim * 3)) {
			gridLayout(cols: 4, rows: 3)
			12.times {
				heroBaseIcons << panel(new EntityDragDropPanel().allowDrag(true))
			}
		}
		this.add(content, BL.CENTER)
		setHeroList(new ArrayList())
	}
	
	void setHeroList(List<HeroBaseStats> heroBases) {
		assert heroBases.size() <= heroBaseIcons.size()
		this.heroBases = heroBases
		heroBaseIcons.eachWithIndex { el, idx ->
			def heroBase = heroBases[idx]
			el.setData(heroBase, heroBase ? ImageUtil.getIconFor(heroBase) : ImageUtil.getEmptyIcon())
			el.setToolTipText(heroBase ? heroBase.title : "")
		}
		repaint()
	}
}
