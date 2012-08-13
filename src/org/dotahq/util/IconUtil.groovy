package org.dotahq.util

import java.awt.image.BufferedImage

import javax.imageio.ImageIO

import org.dotahq.entity.hero.HeroBaseStats
import java.awt.Color

class IconUtil {
    private static final String imagesDir = 'images'
    static final int iconDim = 64
    static final BufferedImage emptyIcon = loadIconByPath("${imagesDir}/icon_empty.png")

    private static BufferedImage loadIconByPath(String path) {
        File targetFile = new File("$path")
        if (targetFile.exists()) {
            BufferedImage image = ImageIO.read(targetFile)
            assert image.width == iconDim
            assert image.height == iconDim
            return image
        } else {
            return new BufferedImage(iconDim, iconDim, BufferedImage.TYPE_INT_RGB)
        }
    }

    static BufferedImage getIconFor(HeroBaseStats base) {
        if (true) return  getRandomIcon()
        File targetFile = new File("${imagesDir}/heroes/${base.title}.png")
        if (targetFile.exists()) {
            BufferedImage image = ImageIO.read(targetFile)
            assert image.width == iconDim
            assert image.height == iconDim
            return image
        } else {
            return emptyIcon
        }
    }

    static BufferedImage getMapImage() {
        File targetFile = new File("${imagesDir}/map.png")
        if (!targetFile.exists()) {
            throw new FileNotFoundException(targetFile)
        }
        BufferedImage image = ImageIO.read(targetFile)
        return image
    }

    static BufferedImage getRandomIcon() {
        BufferedImage image = new BufferedImage(iconDim, iconDim, BufferedImage.TYPE_INT_RGB)
        image.createGraphics().with {
            int rnd = (int) (Math.random() * 10)
            switch (rnd) {
                case 0:
                    setColor(Color.black); break;
                case 1:
                    setColor(Color.blue); break;
                case 2:
                    setColor(Color.cyan); break;
                case 3:
                    setColor(Color.darkGray); break;
                case 4:
                    setColor(Color.red); break;
                case 5:
                    setColor(Color.green); break;
                case 6:
                    setColor(Color.magenta); break;
                case 7:
                    setColor(Color.yellow); break;
                case 8:
                    setColor(Color.gray); break;
                case 9:
                    setColor(Color.orange); //break;
            }
            fillRect(0, 0, iconDim, iconDim)
        }
        return image
    }
}

