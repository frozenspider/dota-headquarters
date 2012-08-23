package org.dotahq.ui

import static org.dotahq.util.CustomUtil.*
import static org.dotahq.util.ImageUtil.iconDim
import groovy.swing.SwingBuilder

import java.awt.BorderLayout as BL

import javax.swing.JList
import javax.swing.JPanel
import javax.swing.ListSelectionModel as LSM
import javax.swing.event.ListSelectionEvent
import javax.swing.event.ListSelectionListener as LSL

import org.dotahq.entity.Side
import org.dotahq.entity.hero.tavern.Tavern

class TavernPane extends JPanel{
	
	private static final int perTavern = 12
	
	private final TavernContentPane	tavernContentPane
	
	public TavernPane(List <Tavern> taverns, TavernContentPane tavernContentPane) {
		List <Tavern> sentTaverns = new ArrayList()
		List <Tavern> scrgTaverns = new ArrayList()
		def addTavern = { Tavern tavern ->
			switch (tavern.side) {
				case Side.SENTINEL:
					sentTaverns << tavern
					break
				case Side.SCOURGE:
					scrgTaverns << tavern
					break
				default:
					throw new IllegalArgumentException("Unknown tavern side: ${tavern.side}")
			}
		}
		for (Tavern tavern in taverns) {
			if (tavern.heroBases.size() <= perTavern) {
				addTavern(tavern)
			} else {
				Tavern tail = new Tavern(asMap(tavern))
				
				for (int idx = 1; tail.heroBases; ++idx) {
					Tavern split = new Tavern(asMap(tail))
					split.name += " $idx"
					split.heroBases = split.heroBases[0..<Math.min(perTavern, split.heroBases.size())]
					addTavern(split)
					
					tail.heroBases = tail.heroBases.size() > 12 ? tail.heroBases[perTavern..-1] : null
				}
			}
		}
		this.tavernContentPane = tavernContentPane
		
		this.setLayout(new BL())
		def content = new SwingBuilder().panel(preferredSize : asList(iconDim * 4, iconDim * 3)) {
			gridLayout()
			
			JList sentList
			JList scrgList
			//			sentList.addLsetSeadd
			scrollPane() {
				sentList = list(items: sentTaverns, selectionMode: LSM.SINGLE_SELECTION)
				sentList.addListSelectionListener( { ListSelectionEvent event ->
					if (event.valueIsAdjusting || sentList.getSelectedIndex() == -1) { return }
					scrgList.setSelectedIndices(new int[0])
					applyTavern(sentList.getSelectedValue())
				} as LSL )
			}
			scrollPane() {
				scrgList = list(items: scrgTaverns, selectionMode: LSM.SINGLE_SELECTION)
				scrgList.addListSelectionListener( { ListSelectionEvent event ->
					if (event.valueIsAdjusting || scrgList.getSelectedIndex() == -1) { return }
					sentList.setSelectedIndices(new int[0])
					applyTavern(scrgList.getSelectedValue())
				} as LSL )
			}
		}
		this.add(content, BL.CENTER)
	}
	
	private void applyTavern(Tavern tavern){
		this.tavernContentPane.setHeroList(tavern.heroBases)
	}
}
