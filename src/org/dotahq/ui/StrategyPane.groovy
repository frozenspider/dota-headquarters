package org.dotahq.ui

import groovy.swing.SwingBuilder

import java.awt.BorderLayout as BL
import java.awt.Color

import javax.swing.JPanel
import javax.swing.JTextPane
import javax.swing.WindowConstants

import org.dotahq.util.DatabaseContainer
import org.dotahq.util.ImageUtil

class StrategyPane extends JPanel {

	private JTextPane commentTextPane

	public StrategyPane(DatabaseContainer databaseContainer) {
		this.setLayout(new BL())

		def content = (new SwingBuilder()).panel {
			def border = {
				lineBorder(color: Color.black)
			}
			borderLayout()
			def topPanel = panel(constraints: BL.NORTH) {
				borderLayout()
				def heroesPanel = panel(new TavernContentPane(), constraints: BL.EAST)
				def tavernsPanel = panel(
					new TavernPane(databaseContainer.taverns, heroesPanel),
					constraints: BL.CENTER
				)
			}
			def centerPanel = panel(constraints: BL.CENTER, border: border()) {
				borderLayout()
				def stratLayoutPanel = panel(
					new StrategyLayoutPanel(ImageUtil.getMapImage()),
					constraints: BL.CENTER
				)
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
	}
	
	public static void main(String[] args) {
		def dbc = new DatabaseContainer()
		new SwingBuilder().frame(
			title: "Strategy Pane Demo",
			size: [640, 750],
			locationRelativeTo: null,
			defaultCloseOperation: WindowConstants.DISPOSE_ON_CLOSE,
			show: true,
			layout: new BL()) {
			widget(new StrategyPane(dbc), constraints: BL.CENTER)
		}
	}
}