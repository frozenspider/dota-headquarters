package org.dotahq.ui

import static java.util.Collections.*

import java.awt.Dimension
import java.awt.GridBagConstraints as GBC
import java.awt.GridBagLayout

import javax.swing.SwingUtilities

import org.dotahq.entity.hero.HeroBaseStats
import org.dotahq.util.IconUtil

class EntityContainerPanel extends EntityDragDropPanel {
	
	private final List<EntityDragDropPanel> panels
    private final int gap = 30

    public EntityContainerPanel() {
        this.panels = []
		this.setImage(null)
		this.setLayout(new GridBagLayout())
        allowDrop([HeroBaseStats.class]) { hero, caller ->
            try {
                if (panels.contains(caller) || panels.size() >= 5) return
                panels << new EntityDragDropPanel().setData(hero, IconUtil.getIconFor(hero)).allowDrag(false)
                assert SwingUtilities.isEventDispatchThread()
                rearrangePanels()
            } catch (e) {
                e.printStackTrace()
            }
        }
		SwingUtilities.invokeLater({rearrangePanels()})
    }

    private void rearrangePanels() {
        assert SwingUtilities.isEventDispatchThread()
        GridBagLayout gbl = new GridBagLayout()
        if (panels.size() < 5) {
            def upToThreeFirst = panels.size() > 3 ? panels[0..<3] : panels
            gbl.rowHeights = nCopies(2 + upToThreeFirst.size(), 0)
            gbl.rowWeights = [
                    Double.MAX_VALUE,
                    nCopies(upToThreeFirst.size(), Double.MIN_VALUE),
                    Double.MAX_VALUE
            ].flatten()
            gbl.columnWidths = nCopies(Math.max(panels.size(), 3), 0)
            gbl.columnWeights = [
                    Double.MAX_VALUE,
                    panels.size() < 4 ? Double.MIN_VALUE : [Double.MIN_VALUE, Double.MIN_VALUE],
                    Double.MAX_VALUE
            ].flatten()
			setLayout(gbl)
            upToThreeFirst.eachWithIndex { el, idx ->
                add(el, new GBC(anchor: GBC.CENTER, gridx: 1 + idx, gridy: 1));
            }
            if (panels.size() == 4) {
                add(panels.last(), new GBC(gridx: 2, gridy: 2));
            }
        } else {
            gbl.rowHeights = nCopies(4, 0)
            gbl.rowWeights = [1.0, Double.MIN_VALUE, Double.MIN_VALUE, 1.0]
            gbl.columnWidths = nCopies(8, 0)
            gbl.columnWeights = [1.0, nCopies(6, Double.MIN_VALUE), 1.0].flatten()
			setLayout(gbl)
            for (i in 0..<3) {
                add(panels[i], new GBC(anchor: GBC.CENTER, gridwidth: 2, gridx: 1 + i * 2, gridy: 1));
            }
            for (i in 3..<5) {
                add(panels[i], new GBC(anchor: GBC.CENTER, gridwidth: 2, gridx: 2 + (i-3) * 2, gridy: 2));
            }
        }
		int w = panels.size() <= 3 ? panels.size() : 3
		int h = panels.size() <= 3 ? 1 : 2
		if (!w) w = 1
		setPreferredSize(
			new Dimension(
				w * IconUtil.getIconDim() + gap,
				h * IconUtil.getIconDim() + gap
			)
		)
		setSize(preferredSize)
        revalidate()
    }
}
