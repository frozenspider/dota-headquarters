package org.dotahq.ui

import static org.dotahq.util.CustomUtil.*
import static org.dotahq.util.IconUtil.iconDim
import groovy.swing.SwingBuilder

import java.awt.BorderLayout as BL
import java.awt.Color

import javax.swing.JFrame
import javax.swing.JPanel
import javax.swing.JTextPane

class StrategyPane extends JPanel {
	
	private JTextPane commentTextPane
	
	public StrategyPane() {
		this.setLayout(new BL())
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
			def centerPanel = panel(constraints: BL.CENTER, border: border())  {
				borderLayout()
				def mapPanel = panel(constraints: BL.CENTER, border: border())  {
					borderLayout()
					label(text: "mapPanel", constraints: BL.CENTER)
				}
				def heroSettingsPanel = panel(constraints: BL.EAST, border: border())  {
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
	}
	
	public static void main(String[] args) {
		JFrame jFrame = new JFrame(defaultCloseOperation: javax.swing.WindowConstants.DISPOSE_ON_CLOSE)
		jFrame.with {
			setSize(640, 480)
			setLocationRelativeTo(null)
			setLayout(new BL())
			add(new StrategyPane(), BL.CENTER)
			show()
		}
	}
}
