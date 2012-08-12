package org.dotahq.ui

import static org.dotahq.util.CustomUtil.*
import static org.dotahq.util.IconUtil.iconDim
import groovy.swing.SwingBuilder

import java.awt.BorderLayout as BL
import java.awt.Color

import javax.swing.JPanel

import org.dotahq.entity.hero.HeroBaseStats

class TavernContentPane extends JPanel {
	
	private final List <IconRenderer> heroBaseIcons
	private List <HeroBaseStats> heroBases
	
	public TavernContentPane() {
		this.heroBases = list()
		this.heroBaseIcons = list()
		this.setLayout(new BL())
		def content = (new SwingBuilder()).panel(preferredSize: list(iconDim * 4, iconDim * 3)) {
			gridLayout(cols: 4, rows: 3)
			def iconSize = list(iconDim, iconDim)
			12.times {
				heroBaseIcons << widget(new IconRenderer(), border: lineBorder(color: Color.black))
			}
		}
		this.add(content, BL.CENTER)
	}
	
	void setHeroList(List <HeroBaseStats> heroBases){
		this.heroBases = heroBases
		heroBases.eachWithIndex { el, idx ->
			heroBaseIcons[idx].setData(heroBases[idx], null) // TODO: Icon
		}
	}
}
