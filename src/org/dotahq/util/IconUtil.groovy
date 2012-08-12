package org.dotahq.util

import java.awt.image.BufferedImage

import javax.imageio.ImageIO

import org.dotahq.entity.hero.HeroBaseStats

class IconUtil {
	static final int iconDim = 64
	static final BufferedImage emptyIcon = new BufferedImage(iconDim, iconDim, BufferedImage.TYPE_INT_RGB)
	
	static BufferedImage getIconFor(HeroBaseStats base){
		File targetFile = new File("images/${base.title}.jpg")
		if (targetFile.exists()) {
			BufferedImage image = ImageIO.read(targetFile)
			assert image.width == iconDim
			assert image.height == iconDim
			return image
		} else {
			return emptyIcon
		}
	}
}

