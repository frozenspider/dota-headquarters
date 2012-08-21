package org.dotahq.util

import org.dotahq.entity.hero.HeroBaseStats

import java.awt.Color
import java.awt.image.BufferedImage
import javax.imageio.ImageIO
import java.awt.RenderingHints
import java.awt.Graphics2D
import org.dotahq.entity.hero.Hero

class ImageUtil {
	private static final String imagesDir = 'images'
	static final int iconDim = 64
	static final BufferedImage emptyIcon = loadImageByPath("${imagesDir}/icon_empty.png")

	private static BufferedImage loadImageByPath(String path) {
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

	static BufferedImage getIconFor(Hero hero) {
		return getIconFor(hero.base)
	}

	static BufferedImage getIconFor(HeroBaseStats base) {
		String noExtPath = "${imagesDir}/heroes/${base.title}"
		def iconFiles = ["png", "gif", "jpg", "jpeg"].collect {new File("${noExtPath}.${it}") }
		def iconFilesExist = iconFiles*.exists()
		if (iconFilesExist.contains(true)) {
			BufferedImage image = ImageIO.read(iconFiles[iconFilesExist.indexOf(true)])
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
			throw new FileNotFoundException(targetFile.getCanonicalPath())
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

	/**
	 * Convenience method that returns a scaled instance of the provided {@code BufferedImage}.
	 * <p>
	 * Source: http://today.java.net/pub/a/today/2007/04/03/perils-of-image-getscaledinstance.html
	 *
	 * @param img
	 *            the original image to be scaled
	 * @param targetWidth
	 *            the desired width of the scaled instance, in pixels
	 * @param targetHeight
	 *            the desired height of the scaled instance, in pixels
	 * @return a scaled version of the original {@code BufferedImage}
	 */
	public static BufferedImage scaledCopy(
		final BufferedImage img,
		final int targetWidth,
		final int targetHeight) {

		// hint - one of the rendering hints that corresponds to RenderingHints.KEY_INTERPOLATION
		// (e.g. VALUE_INTERPOLATION_NEAREST_NEIGHBOR, VALUE_INTERPOLATION_BILINEAR,
		// VALUE_INTERPOLATION_BICUBIC)
		final Object hint = RenderingHints.VALUE_INTERPOLATION_BILINEAR;

		BufferedImage ret = img;

		// Use multi-step technique: start with original size, then
		// scale down in multiple passes with drawImage()
		// until the target size is reached
		int w = img.getWidth()
		int h = img.getHeight()
		int imgType = BufferedImage.TYPE_INT_RGB // img.getType()

		if (w == targetWidth && h == targetHeight) {
			BufferedImage result = new BufferedImage(w, h, imgType)
			img.copyData(result.getRaster())
			return img
		}

		while (true) {
			if (w > targetWidth) {
				w /= 2;
				if (w < targetWidth) {
					w = targetWidth;
				}
			} else if (w < targetWidth) {
				w *= 2;
				if (w > targetWidth) {
					w = targetWidth;
				}
			}

			if (h > targetHeight) {
				h /= 2;
				if (h < targetHeight) {
					h = targetHeight;
				}
			} else if (h < targetHeight) {
				h *= 2;
				if (h > targetHeight) {
					h = targetHeight;
				}
			}

			final BufferedImage tmp = new BufferedImage(w, h, imgType);
			final Graphics2D g2 = tmp.createGraphics();
			g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, hint);
			g2.drawImage(ret, 0, 0, w, h, null);
			g2.dispose();

			ret = tmp;

			if (w == targetWidth && h == targetHeight) break
		}

		return ret;
	}

}

