package org.dotahq.ui

import groovy.swing.SwingBuilder

import java.awt.BorderLayout as BL
import java.awt.Color

import javax.swing.JPanel
import javax.swing.JTextPane
import org.dotahq.util.IconUtil
import javax.swing.WindowConstants

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
            def centerPanel = panel(constraints: BL.CENTER, border: border()) {
                borderLayout()
                def mapPanel = panel(
                        new RenderingPanel(image: IconUtil.getMapImage()),
                        constraints: BL.CENTER,
                        layout: null,
                        border: border()) {
                    def jungle = panel(new EntityContainerPanel(), location: [100, 150], size: [350, 250], border: border())
                    label(text: "mapPanel", background: Color.red)
                }
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
        new SwingBuilder().frame(
                title: "Strategy Pane Demo",
                size: [640, 750],
                locationRelativeTo: null,
                defaultCloseOperation: WindowConstants.DISPOSE_ON_CLOSE,
                show: true,
                layout: new BL()) {
            widget(new StrategyPane(), constraints: BL.CENTER)
        }
    }
}