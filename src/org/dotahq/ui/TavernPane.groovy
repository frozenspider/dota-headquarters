package org.dotahq.ui

import static org.dotahq.util.CustomUtil.*
import static org.dotahq.util.IconUtil.iconDim
import groovy.swing.SwingBuilder

import java.awt.BorderLayout as BL
import java.awt.Color

import javax.swing.JPanel

class TavernPane extends JPanel{
	
	public TavernPane() {
		SwingBuilder swb = new SwingBuilder()
		this.setLayout(new BL())
		def content = swb.panel(preferredSize : list(iconDim * 4, iconDim * 3)) {
			gridLayout()
		}
		this.add(content, BL.CENTER)
	}
}
