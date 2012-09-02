package org.dotahq.ui.strategy

import groovy.swing.SwingBuilder

import java.awt.BorderLayout as BL
import java.awt.Color
import java.awt.FlowLayout

import javax.swing.JFileChooser
import javax.swing.JPanel
import javax.swing.JTextPane
import javax.swing.WindowConstants
import javax.swing.filechooser.FileFilter
import javax.swing.filechooser.FileNameExtensionFilter

import org.dotahq.entity.StrategyLayout
import org.dotahq.entity.hero.HeroBaseStats
import org.dotahq.persist.StrategySaveLoad
import org.dotahq.ui.common.JExtFileChooser
import org.dotahq.util.DatabaseContainer
import org.dotahq.util.DisplayUtils
import org.dotahq.util.FileSysUtil
import org.dotahq.util.ImageUtil

class StrategyPanel extends JPanel {
	
	private final DatabaseContainer		databaseContainer;	private final StrategySaveLoad		saveLoad
	
	private JTextPane 					commentTextPane
	private StrategyLayoutPanel 		stratLayoutPanel
	
	public StrategyPanel(DatabaseContainer databaseContainer, StrategySaveLoad saveLoad) {
		this.databaseContainer =  databaseContainer;
		this.saveLoad = saveLoad;
		this.setLayout(new BL())
		
		def content = (new SwingBuilder()).panel {
			def border = {
				lineBorder(color: Color.black)
			}
			borderLayout()
			def topPanel = panel(constraints: BL.NORTH) {
				borderLayout()
				def ctrlPanel = panel(constraints: BL.NORTH){
					flowLayout(alignment: FlowLayout.LEFT)
					button(action: action(closure: { saveClick() }), text: "Save")
					button(action: action(closure: { loadClick() }), text: "Load")
				}
				def heroesPanel = panel(new TavernContentPane(), constraints: BL.EAST)
				def tavernsPanel = panel(
				new TavernPanel(databaseContainer.taverns, heroesPanel), constraints: BL.CENTER)
			}
			def centerPanel = panel(constraints: BL.CENTER, border: border()) {
				borderLayout()
				stratLayoutPanel = panel(
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
	
	private JFileChooser getFileChooser() {
		JFileChooser chooser = new JExtFileChooser(FileSysUtil.strategiesDir)
		FileFilter filter = new FileNameExtensionFilter(
			"DotA Headquarters Strategies",
			FileSysUtil.strategiesExt
		)
		chooser.setFileFilter(filter)
		return chooser
	}
	
	void saveClick(){
		try {
			JFileChooser chooser = getFileChooser()
			if (chooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
				saveLoad.saveToFile(chooser.getSelectedFile(), stratLayoutPanel.strategy)
			}
		} catch (ex) {
			DisplayUtils.error(ex, this)
		}
	}
	
	void loadClick(){
		try {
			JFileChooser chooser = getFileChooser()
			if (chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
				StrategyLayout strategy = saveLoad.loadFromFile(chooser.getSelectedFile())
				strategy = synchWithDatabase(strategy)
				stratLayoutPanel.setStrategy(strategy)
			}
		} catch (ex) {
			DisplayUtils.error(ex, this)
		}
	}
	
	StrategyLayout synchWithDatabase(StrategyLayout strategy) {
		StrategyLayout synched = new StrategyLayout()
		for (heroToLane in strategy.heroesToLanesMap()) {
			HeroBaseStats heroBase = databaseContainer.update(heroToLane.key)
			if (heroBase) {
				synched.put(heroBase, heroToLane.value)
			} else {
				String msg = "Failed to update hero ${heroBase.title} on lane ${heroToLane.value}."
					+ " Please add manually."
				DisplayUtils.warning(msg, this)
			}
		}
		return synched
	}
	
	public static void main(String[] args) {
		def dbc = new DatabaseContainer()
		def saveLoad = new StrategySaveLoad()
		new SwingBuilder().frame(
			title: "Strategy Pane Demo",
			size: [640, 750],
			locationRelativeTo: null,
			defaultCloseOperation: WindowConstants.DISPOSE_ON_CLOSE,
			show: true,
			layout: new BL()) {
				widget(new StrategyPanel(dbc, saveLoad), constraints: BL.CENTER)
		}
	}
}