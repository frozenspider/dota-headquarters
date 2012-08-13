package org.dotahq.ui

import javax.swing.TransferHandler as TH

import org.dotahq.util.DragAndDropUtil

import java.awt.Dimension
import java.awt.Graphics
import java.awt.Point
import java.awt.Toolkit
import java.awt.datatransfer.DataFlavor
import java.awt.image.BufferedImage
import javax.swing.JPanel
import java.awt.dnd.*

import static org.dotahq.util.IconUtil.getEmptyIcon

class RenderingPanel extends JPanel {

    BufferedImage image

    @Override
    void paintComponent(Graphics g) {
        super.paintComponent(g)
        if (image) g.drawImage(image, 0, 0, null)
    }
}
