package org.dotahq.ui

import java.awt.Graphics

import java.awt.image.BufferedImage
import javax.swing.JPanel

class ImageRenderingPanel extends JPanel {

    BufferedImage image

    @Override
    void paintComponent(Graphics g) {
        super.paintComponent(g)
        if (image) g.drawImage(image, 0, 0, null)
    }
}
