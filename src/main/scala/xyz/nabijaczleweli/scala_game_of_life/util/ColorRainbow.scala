package xyz.nabijaczleweli.scala_game_of_life.util

import scala.swing.{Point, Color}
import java.awt.image._
import java.awt.{PaintContext, RenderingHints, Rectangle}
import java.awt.geom.{AffineTransform, Rectangle2D}
import scala.util.Random

/** @author Jędrzej
  * @since  15.05.14
  */
class ColorRainbow(val maxR: Int = 256, val maxG: Int = 256, val maxB: Int = 256, val alpha: Int = 255) extends Color(0) {
	private val rand = new Random

	override def createContext(cm: ColorModel, r: Rectangle, r2d: Rectangle2D, xform: AffineTransform, hints: RenderingHints) =
		new PaintContext {
			override def getRaster(x: Int, y: Int, w: Int, h: Int): Raster = {
				val arr = Array.fill(w * h)(getRGB)
				Raster.createRaster(new SinglePixelPackedSampleModel(DataBuffer.TYPE_BYTE, w, h, arr), new DataBufferInt(arr, arr.length), new Point(x, y))
			}

			override def getColorModel =
				ColorModel.getRGBdefault

			override def dispose() {}
		}

	override def getRed =
		rand nextInt maxR

	override def getGreen =
		rand nextInt maxG

	override def getBlue =
		rand nextInt maxB

	override def getAlpha =
		alpha

	// aaaaaaaarrrrrrrrggggggggbbbbbbbb
	override def getRGB =
		(getAlpha << 24) | (getRed <<  16) | (getGreen << 8) | getBlue
}
