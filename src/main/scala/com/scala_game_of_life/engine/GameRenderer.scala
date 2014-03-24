package com.scala_game_of_life.engine

import java.awt._
import com.scala_game_of_life.world.{IEntityAccess, ICellAccess}
import net.lonning.loutput.ConsoleLOutput
import com.scala_game_of_life.util.MathUtil
import java.io.{File, FileInputStream, FileNotFoundException}
import scala.swing.MainFrame
import com.scala_game_of_life.engine.registries.{DisallowedSourcesRegistry, ScreenNameRegistry, MaxMetadataRegistry, GameRegistry}
import com.scala_game_of_life.PublicResources.gameEngine
import com.scala_game_of_life.engine.rendering._
import com.scala_game_of_life.engine.save.{SaveProcessor, Saveable}
import com.scala_game_of_life.Main
import com.scala_game_of_life.cell.Material
import net.lonning.Logger

/** Code for rendering things.
  *
  * <p>To schedule a frame repaint, do :<br />
  * <tt>&nbsp;&nbsp;GameRenderer synchronized {<br />
  * &nbsp;&nbsp;&nbsp;&nbsp;GameRenderer.notifyAll()<br />
  * &nbsp;&nbsp;}</tt></p>
  * <p>Anything is allowed to <tt>wait()</tt> on GameRenderer -
  * <tt>wait()</tt>er will be <tt>notifyAll()</tt>ed about every scheduled repaint.<br /><br /></p>
  *
  * @note using <tt>GameRenderer.notify()</tt> instead of <tt>GameRenderer.notifyAll()</tt>
  *       may make <tt>GameRenderer</tt> misbehave.
  *
  * @author Jędrzej
  * @since  10.04.14
  */
object GameRenderer extends ConsoleLOutput("S-GameRenderer") {
	final         val screenWidth        = Toolkit.getDefaultToolkit.getScreenSize.getWidth.toInt
	final         val screenHeight       = Toolkit.getDefaultToolkit.getScreenSize.getHeight.toInt
	/** To resemble 5x5 cell on 1600x900 screen. To be replaced with settings. */
	final         val cellWidth          = screenWidth / 320
	/** To resemble 5x5 cell on 1600x900 screen. To be replaced with settings. */
	final         val cellHeight         = screenHeight / 180
	final         val cellsInXAxis       = 200
	final         val cellsInYAxis       = 150
	final         val desiredSize        = new Dimension(cellWidth * cellsInXAxis, cellHeight * cellsInYAxis)
	private final val settingsPauseTexts = Vector[String]("Save world", "Load world", "Tick %%% time&&&.", "Exit")
	private final var choosesaveTexts    = Vector[String]("Name: %%%.")
	private final val savesDir           = new File(Saveable.rootPath)
	protected     var frame: MainFrame   = null
	/** Belongs to [[com.scala_game_of_life.Main]]. */
	var screenRepainter: RepainterThread = null
	var graph          : Graphics2D      = null

	def frameSize =
		frame.contents(0).size

	def drawCellsInWorld(world: ICellAccess, topLeftX: Int, topLeftY: Int) {
		graph setColor world.backgroundColor
		graph.fillRect(0, 0, frame.contents(0).size.width, frame.contents(0).size.height)
		for(xpos <- 0 until cellsInXAxis; ypos <- 0 until cellsInYAxis) {
			val c = world.cellAtCoords(xpos + topLeftX, ypos + topLeftY, create = true)
			if(c.material != Material.air)
				c.draw(xpos * cellWidth, ypos * cellHeight, topLeftX + xpos, topLeftY + ypos, world)
		}
	}

	def drawEntitiesInWorld(world: IEntityAccess, topLeftX: Float, topLeftY: Float) {
		for(ent <- world.entities if ent.isEntityAlive)
			if(!ent.isInvisible)
				ent.draw((ent.posX - topLeftX) * cellWidth, (ent.posY - topLeftY) * cellHeight)
			else
				ent.onInvisibleDraw()
		if(world.player.isEntityAlive)
			if(!world.player.isInvisible)
				world.player.draw()
			else
				world.player.onInvisibleDraw()
		for(par <- world.particles if par.isEntityAlive)
			par.draw((par.posX - topLeftX) * cellWidth, (par.posY - topLeftY) * cellHeight)
		world.collectGarbage()
	}

	def drawSettings() {
		import RenderInfoHolder._
		assume(extendedInfo.isInstanceOf[SettingsPauseData])
		val height = frameSize.height
		val width = frameSize.width
		val textMetrics = graph getFontMetrics settingsFont
		val numberMetrics = graph getFontMetrics numberFont
		val privateExtendedInfo = extendedInfo.asInstanceOf[SettingsPauseData]
		def normalizedString =
			if(privateExtendedInfo.value.length != 0)
				privateExtendedInfo.value
			else
				"0"
		def normalizedInt =
			privateExtendedInfo.value.length max 1
		keyboardMode = metadata == 2
		privateExtendedInfo.value = privateExtendedInfo.value.replaceAll("[^0123456789]", "")
		if("[123456789]".r.findAllMatchIn(privateExtendedInfo.value).isEmpty)
			privateExtendedInfo.value = ""
		// Normalize `privateExtendedInfo.value`
		try {
			Integer valueOf normalizedString
		} catch {
			case _: NumberFormatException =>
				privateExtendedInfo.value = Int.MaxValue.toString
		}
		extendedInfo.value = privateExtendedInfo.value
		graph setColor Color.red
		graph.fillRect(0, 0, width, height)
		for(diameter <- 0 to MathUtil.diameter_i(height, width)) {
			val col = GameEngine.rand nextInt 64
			graph setColor new Color(col, col, col)
			graph.drawOval((width - diameter) / 2, (height - diameter) / 2, diameter, diameter)
		}
		graph setColor Color.white
		graph setFont settingsFont
		for(idx <- 0 until settingsPauseTexts.length) {
			val replaced = settingsPauseTexts(idx).replaceAll("%%%", normalizedString).replaceAll("&&&", if(Integer.valueOf(normalizedString) > 1 || Integer.valueOf(normalizedString) == 0) "s" else "")
			if(replaced == settingsPauseTexts(idx))
				graph.drawString(replaced, (width * .5f) - (textMetrics.stringWidth(replaced) / 2), (height / settingsPauseTexts.length) * (idx.toFloat + .5f))
			else {
				val split = replaced split s"[0123456789]{$normalizedInt,$normalizedInt}"
				graph.drawString(split(0),
				                 ((width * .5f) - (numberMetrics.stringWidth(normalizedString) / 2)) - textMetrics.stringWidth(split(0)),
				                 (height / settingsPauseTexts.length) * (idx.toFloat + .5f))
				graph setFont numberFont
				graph.drawString(normalizedString,
				                 (width * .5f) - (numberMetrics.stringWidth(normalizedString) / 2),
				                 (height / settingsPauseTexts.length) * (idx.toFloat + .5f))
				graph setFont settingsFont
				graph.drawString(split(1),
				                 ((width * .5f) - (numberMetrics.stringWidth(normalizedString) / 2)) + numberMetrics.stringWidth(normalizedString),
				                 (height / settingsPauseTexts.length) * (idx.toFloat + .5f))
			}
		}
		val replacedText = settingsPauseTexts(metadata).replaceAll("%%%", normalizedString).replaceAll("&&&", if(Integer.valueOf(normalizedString) > 1 || Integer.valueOf(normalizedString) == 0) "s" else "")
		graph.drawString("#", (width * .5f) - (textMetrics.stringWidth(replacedText.replaceAll("[0123456789]+", "")).toFloat * .5f) - (textMetrics.charWidth('#').toFloat * 2f).toInt - (numberMetrics.stringWidth(normalizedString).toFloat * (if(replacedText == settingsPauseTexts(metadata)) 0f else .5f)).toInt, (height / settingsPauseTexts.length) * (metadata.toFloat + .5f)) // Draws selection pointer at the left side
		graph.drawString("#", (width * .5f) + (textMetrics.stringWidth(replacedText.replaceAll("[0123456789]+", "")).toFloat * .5f) + textMetrics.charWidth('#') + (numberMetrics.stringWidth(normalizedString).toFloat * (if(replacedText == settingsPauseTexts(metadata)) 0f else .5f)).toInt, (height / settingsPauseTexts.length) * (metadata.toFloat + .5f)) // Draws selection pointer at the right side
	}

	/** Only jist with it is that it renders in about 125-156ms. Is it a problem? */
	def drawChooseSave() {
		import RenderInfoHolder._
		assume(extendedInfo.isInstanceOf[ChooseSaveData])
		val files = savesDir.listFiles.sortWith((lf, rf) => lf.getName.compareTo(rf.getName) < 0).toBuffer
		if(files.length > maxMetadata)
			warn(s"\'${Saveable.rootPath}\' consists of ${files.length} files, which is ${files.length - maxMetadata} more than maximum!")
		while(files.length > maxMetadata)
			files.remove(GameEngine.rand nextInt files.length)
		choosesaveTexts = (for(f <- files) yield f.getName) ++: choosesaveTexts.splitAt(choosesaveTexts.size - 1)._2
		val privateExtendedInfo = extendedInfo.asInstanceOf[ChooseSaveData]
		val height = frame.contents(0).size.height
		val width = frame.contents(0).size.width
		val metrics = graph getFontMetrics choosesaveFont
		keyboardMode = metadata == choosesaveTexts.length - 1
		graph setColor Color.red
		graph.fillRect(0, 0, width, height)
		for(x <- 0.to(width, 50); y <- 0.to(height, 50)) {
			val col = GameEngine.rand nextInt 64
			graph setColor new Color(col, col, col)
			for(ovalIn <- 0 to 25)
				graph.drawOval(x + ovalIn, y + ovalIn, 50 - ovalIn * 2, 50 - ovalIn * 2)
		}
		for(x <- (-25).to(width + 25, 50); y <- (-25).to(height + 25, 50)) {
			val col = GameEngine.rand nextInt 64
			graph setColor new Color(col, col, col)
			for(ovalIn <- 0 to 25)
				graph.drawOval(x + ovalIn, y + ovalIn, 50 - ovalIn * 2, 50 - ovalIn * 2)
		}
		graph setFont choosesaveFont
		graph setColor Color.white
		for(idx <- 0 until choosesaveTexts.length) {
			val replacedText = choosesaveTexts(idx).replaceAll("%%%", privateExtendedInfo.value)
			graph.drawString(replacedText, (width * .5f) - (metrics.stringWidth(replacedText) / 2), (height / choosesaveTexts.length) * (idx.toFloat + .5f))
		}
		val replacedText = choosesaveTexts(metadata).replaceAll("%%%", privateExtendedInfo.value)
		graph.drawString("•", (width * .5f) - (metrics.stringWidth(replacedText).toFloat * .5f) - (metrics.charWidth('•').toFloat * 2f).toInt, (height / choosesaveTexts.length) * (metadata.toFloat + .5f)) // Draws selection pointer at the left side
		graph.drawString("•", (width * .5f) + (metrics.stringWidth(replacedText).toFloat * .5f) + metrics.charWidth('•'), (height / choosesaveTexts.length) * (metadata.toFloat + .5f)) // Draws selection pointer at the right side
	}

	final val settingsFont   = Font.createFont(Font.TRUETYPE_FONT, GameRendererResources.settingsFontIstream).deriveFont(Font.TRUETYPE_FONT, 30)
	final val choosesaveFont = Font.createFont(Font.TRUETYPE_FONT, GameRendererResources.choosesaveFontIstream).deriveFont(Font.TRUETYPE_FONT, 30)
	final val numberFont     = Font.createFont(Font.TRUETYPE_FONT, GameRendererResources.numberFontIstream).deriveFont(Font.TRUETYPE_FONT, 25)
	final val painter        = new Thread("S-GameRenderer:repaint-scheduler") with Logger[ConsoleLOutput] {

		import RenderInfoHolder._

		override def run() {
			GameRegistry.registerScreen({}, 0, "Game", MetadataChangeSource.Anything :: Nil, maingame)
			GameRegistry.registerScreen({
				                            assume(extendedInfo.isInstanceOf[SettingsPauseData])
				                            val extInfo = extendedInfo.asInstanceOf[SettingsPauseData]
				                            metadata match {
					                            case 0 =>
						                            gameEngine ! SaveWorld()
					                            case 1 =>
						                            gameEngine ! LoadWorld()
					                            case 2 =>
						                            gameEngine ! TickCells(Integer valueOf (if(extInfo.value.length != 0) extInfo.value else "0"))
					                            case 3 =>
						                            Main.saveWorld()
						                            frame.closeOperation()
				                            }
			                            }, 3, "Settings", MetadataChangeSource.Nothing :: Nil, settingspause)
			GameRegistry.registerScreen({
				                            val extInfo = extendedInfo.asInstanceOf[ChooseSaveData]
				                            var doSwitch = false
				                            metadata match {
					                            case iGnored if iGnored == choosesaveTexts.length - 1 =>
						                            if(extInfo.value.length > 0) {
							                            val file = new File(s"${Saveable.rootPath}${SaveProcessor.separator}${extInfo.value}")
							                            file.mkdir
							                            val field = GameEngine.getClass getDeclaredField "nameOfSave"
							                            field setAccessible true
							                            field.set(GameEngine, extInfo.value)
							                            doSwitch = true
						                            } else
							                            info("You must enter at least one character!")
					                            case met =>
						                            val field = GameEngine.getClass getDeclaredField "nameOfSave"
						                            field setAccessible true
						                            field.set(GameEngine, choosesaveTexts(met))
						                            GameEngine.world = SaveProcessor.load[ICellAccess with IEntityAccess](GameEngine.world)
						                            GameEngine.rand synchronized {
							                            GameEngine.rand.notifyAll()
						                            }
						                            doSwitch = true
				                            }
				                            if(doSwitch) {
					                            drawInfo &= ~0xff
					                            drawInfo |= maingame
					                            if(screenRepainter != null) {
						                            screenRepainter.interrupt()
						                            screenRepainter.join()
					                            }
					                            screenRepainter = new RepainterThread(31L)
					                            metadata = 0
					                            frame.title = s"S-GOL:${(ScreenNameRegistry get drawInfo).get}"
				                            }
				                            GameRenderer synchronized {
					                            GameRenderer.notifyAll()
				                            }
			                            }, maxMetadata, "Choose save", MetadataChangeSource.Nothing :: Nil, choosesave)
			drawInfo |= choosesave
			extendedInfo = new ChooseSaveData().asInstanceOf[ExtendedScreenData[Any]]
			keyboardMode = true
			while(frame == null)
				Thread sleep 5
			screenRepainter = new RepainterThread(132) // 7.(57)FPS looks cool.
			try {
				while(!Thread.currentThread.isInterrupted) {
					GameRenderer synchronized {
						GameRenderer.wait()
					}
					frame.repaint()
				}
			} catch {
				case _: InterruptedException =>
			}
			debug("I'm done!")
		}

		override val logger = new ConsoleLOutput(name)

		start()
	}

	Runtime.getRuntime addShutdownHook new Thread("S-GameRenderer:killer") with Logger[ConsoleLOutput] {
		override def run() {
			painter.interrupt()
			debug(s"Killed \'${painter.getName}\'")
			painter.join()
		}

		override val logger = new ConsoleLOutput("S-GameRenderer:killer")
	}

	def isMetadataChangeAllowed(inc: Boolean, source: MetadataChangeSource.MetadataChangeSource) = {
		val got = DisallowedSourcesRegistry.get(RenderInfoHolder.drawInfo).get
		if(got.contains(source) || got.contains(MetadataChangeSource.Anything))
			false
		else if(inc)
			if((RenderInfoHolder.drawInfo & RenderInfoHolder.choosesave) == 0)
				RenderInfoHolder.metadata < MaxMetadataRegistry.get(RenderInfoHolder.drawInfo).get
			else
				RenderInfoHolder.metadata < choosesaveTexts.length - 1
		else
			RenderInfoHolder.metadata > 0
	}

	object MetadataChangeSource extends Enumeration {
		type MetadataChangeSource = Value
		val Keyboard, Anything, Nothing = Value
	}

	object RenderInfoHolder {
		/** The bitstructure is as follows: 00000000000000000003222211111111
		  *
		  * <p>Where:<br />
		  * &nbsp;&nbsp;&nbsp;&nbsp;0 - unused<br />
		  * &nbsp;&nbsp;&nbsp;&nbsp;1 - rendering type (what sould be currently rendered)<br />
		  * &nbsp;&nbsp;&nbsp;&nbsp;2 - data for current rendering type (selection, etc.)<br />
		  * &nbsp;&nbsp;&nbsp;&nbsp;3 - keyboard mode<br /></p>
		  * Data for current rendering type needs to be reset after changing drawInfo.<br />
		  * Rendering type is stored by setting <b>individual bits</b>.<br />
		  * When doing right bitshifts, use <tt>>>></tt> instead of <tt>>></tt> because negatives might occur.
		  */
		// Enforced use of 32-bit mathematical integer.
		final var drawInfo     : Int  = 0
		final val maingame     : Byte = (1 << 0).toByte
		final val settingspause: Byte = (1 << 1).toByte
		final val choosesave   : Byte = (1 << 2).toByte

		final val maxMetadata = 0xf
		final val minMetadata = 0x0

		final var extendedInfo: ExtendedScreenData[Any] = NoScreenData.asInstanceOf[ExtendedScreenData[Any]]

		def metadata =
			(drawInfo >>> 8) & 0xf

		def metadata_=(meta: Int) {
			drawInfo &= ~0xf00
			drawInfo |= (meta & 0xf) << 8
		}

		def keyboardMode =
			(drawInfo & 0x1000) != 0

		def keyboardMode_=(km: Boolean) {
			drawInfo &= ~0x1000
			if(km)
				drawInfo |= 0x1000
		}
	}

}

object GameRendererResources extends ConsoleLOutput("S-GameRenderer:resources") {
	final val settingsFontIstream   = loadFont("Endor")
	final val choosesaveFontIstream = loadFont("Tribal Two")
	final val numberFontIstream     = loadFont("04B_30")

	private def loadFont(fontname: String) = {
		var istream: FileInputStream = null
		try {
			istream = new FileInputStream(s"assets${SaveProcessor.separator}fonts${SaveProcessor.separator}$fontname.ttf")
		} catch {
			case _: FileNotFoundException =>
				ERROR(s"Couldn't load the font file called \'assets${SaveProcessor.separator}fonts${SaveProcessor.separator}$fontname.ttf\'! Exitting!")
				System exit 1
		}
		debug(s"Succesfuly loaded the \'$fontname\' font!")
		istream
	}
}