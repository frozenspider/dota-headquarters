package org.dotahq.ui

import static org.dotahq.util.CustomUtil.*
import static ImageUtil.iconDim
import groovy.swing.SwingBuilder

import java.awt.BorderLayout as BL
import java.awt.Color

import javax.swing.JPanel

import org.dotahq.entity.hero.HeroBaseStats

import org.dotahq.util.ImageUtil

class TavernContentPane extends JPanel {
	
	private final List <EntityDragDropPanel> heroBaseIcons
	private List <HeroBaseStats> heroBases
	
	public TavernContentPane() {
		this.heroBases = list()
		this.heroBaseIcons = list()
		this.setLayout(new BL())
		def content = (new SwingBuilder()).panel(preferredSize: list(iconDim * 4, iconDim * 3)) {
			gridLayout(cols: 4, rows: 3)
			12.times {
				heroBaseIcons << widget(new EntityDragDropPanel().allowDrag(true), border: lineBorder(color: Color.black))
			}
		}
		this.add(content, BL.CENTER)

        // FIXME TODO
        setHeroList(Collections.nCopies(7, new HeroBaseStats()))
	}
	
	void setHeroList(List <HeroBaseStats> heroBases){
        assert heroBases.size() <= heroBaseIcons.size()
		this.heroBases = heroBases
        heroBaseIcons.eachWithIndex { el, idx ->
            def heroBase = heroBases[idx]
			el.setData(heroBase, heroBase ? ImageUtil.getIconFor(heroBase) : ImageUtil.getEmptyIcon())
		}
	}
}
