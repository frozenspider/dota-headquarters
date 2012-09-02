package org.dotahq.ui.strategy

import static java.util.Collections.*

import java.awt.Dimension
import java.awt.GridBagConstraints as GBC
import java.awt.GridBagLayout

import javax.swing.SwingUtilities

import org.dotahq.entity.hero.HeroBaseStats
import org.dotahq.util.ImageUtil
import java.awt.dnd.DragSourceDropEvent
import javax.swing.JLabel
import java.awt.Color
import java.awt.Graphics

class EntityContainerPanel extends EntityDragDropPanel {

	private final JLabel caption
	private final List<EntityDragDropPanel> panels
	private final int gap = 20
	private final int iconDim
	def pinPoint

	public EntityContainerPanel(String captionText, int iconDim, Closure dropAction, boolean actsAsTrash = false) {
		def dropCallback = { hero, caller ->
			assert SwingUtilities.isEventDispatchThread()
			def callerContainer = caller.getParent()
			if (actsAsTrash) {
				dropAction(hero, callerContainer)
				return true
			}
			if (panels.contains(caller) || panels.size() >= 5) return false
			addPanelFor(hero)
			boolean layoutIsAdmissible = dropAction(hero, callerContainer)
			if (layoutIsAdmissible) {
				refreshPanels()
			} else {
				panels.pop()
			}
			return layoutIsAdmissible
		}
		this.panels = []
		this.iconDim = iconDim
		this.caption = new JLabel(captionText)
		this.setImage(null)
		this.setLayout(new GridBagLayout())
		this.setOpaque(false)
		this.setBackground(new Color(255, 255, 255, 128))
		allowDrop([HeroBaseStats.class], dropCallback)
		SwingUtilities.invokeLater({refreshPanels()})
	}

	private void addPanelFor(HeroBaseStats hero) {
		def panel = new EntityDragDropPanel().setData(
			hero,
			ImageUtil.scaledCopy(
				ImageUtil.getIconFor(hero),
				iconDim,
				iconDim
			)
		)
		panels << panel
		def dragCallback = { DragSourceDropEvent dsde ->
			if (!dsde.getDropSuccess()) return
			panels.remove(panel)
			refreshPanels()
		}
		panel.setToolTipText(hero.title)
		panel.allowDrag(false, dragCallback)
	}
	
	@Override
	void paintComponent(Graphics g) {
		// Ensure component background is applied
		g.color = background
		g.fillRect(0, 0, width, height)
		super.paintComponent(g)
	}

	List<HeroBaseStats> getData() {
		return panels.collect { it.getData() }
	}
	
	void setData(List<HeroBaseStats> heroBaseStats) {
		panels.clear()
		for (HeroBaseStats heroBase in heroBaseStats) {
			addPanelFor(heroBase)
		}
		SwingUtilities.invokeLater({
			refreshPanels()
		})
	}
	
	EntityContainerPanel setPinPoint(List<Integer> pinPoint) {
		setPinPoint(pinPoint[0], pinPoint[1])
	}

	EntityContainerPanel setPinPoint(int x, int y) {
		this.pinPoint = [x, y]
		setLocation((int) (x - width / 2), (int) (y - height / 2))
	}

	private void refreshPanels() {
		assert SwingUtilities.isEventDispatchThread()
		removeAll()
		GridBagLayout gbl = new GridBagLayout()
		if (panels.size() < 5) {
			def upToThreeFirst = panels.size() > 3 ? panels[0..<3] : panels
			gbl.rowHeights = nCopies(Math.max(panels.size(), 3), 0)
			gbl.rowWeights = [
				Double.MAX_VALUE,
				panels.size() < 4 ? Double.MIN_VALUE : [Double.MIN_VALUE, Double.MIN_VALUE],
				Double.MAX_VALUE
			].flatten()
			gbl.columnWidths = nCopies(3 + upToThreeFirst.size(), 0)
			gbl.columnWeights = [
				Double.MAX_VALUE,
				Double.MIN_VALUE,
				nCopies(upToThreeFirst.size(), Double.MIN_VALUE),
				Double.MAX_VALUE
			].flatten()
			setLayout(gbl)
			switch (upToThreeFirst.size()) {
				case 0:
				case 1:
					add(caption, new GBC(anchor: GBC.CENTER, gridx: 1, gridy: 1))
					break
				case 2:
					add(caption, new GBC(anchor: GBC.CENTER, gridwidth: 2, gridx: 1, gridy: 1))
					break
				case 3:
					add(caption, new GBC(anchor: GBC.CENTER, gridwidth: 3, gridx: 1, gridy: 1))
					break
				default:
					throw new RuntimeException("Unexpected panels size: ${panels.size()}")
			}

			upToThreeFirst.eachWithIndex { el, idx ->
				add(el, new GBC(anchor: GBC.CENTER, gridx: 1 + idx, gridy: 2));
			}
			if (panels.size() == 4) {
				add(panels.last(), new GBC(gridx: 2, gridy: 3));
			}
		} else {
			gbl.rowHeights = nCopies(5, 0)
			gbl.rowWeights = [1.0, Double.MIN_VALUE, Double.MIN_VALUE, Double.MIN_VALUE, 1.0]
			gbl.columnWidths = nCopies(8, 0)
			gbl.columnWeights = [1.0, nCopies(6, Double.MIN_VALUE), 1.0].flatten()
			setLayout(gbl)

			add(caption, new GBC(anchor: GBC.CENTER, gridwidth: 6, gridx: 1, gridy: 1))
			for (i in 0..<3) {
				add(panels[i], new GBC(anchor: GBC.CENTER, gridwidth: 2, gridx: 1 + i * 2, gridy: 2));
			}
			for (i in 3..<5) {
				add(panels[i], new GBC(anchor: GBC.CENTER, gridwidth: 2, gridx: 2 + (i - 3) * 2, gridy: 3));
			}
		}
		int w = panels.size() <= 3 ? panels.size() : 3
		int h = panels.size() <= 3 ? 1 : 2
		if (!w) w = 1
		int totalW = ((caption.preferredSize.width > (w * iconDim)) ? caption.preferredSize.width : (w * iconDim)) + gap
		int totalH = caption.preferredSize.height + h * iconDim + gap / 2
		setPreferredSize(new Dimension(totalW, totalH))
		setSize(preferredSize)
		if (pinPoint) setPinPoint(pinPoint)
		revalidate()
		repaint()
	}
}
