package com.scala_game_of_life

import scala.swing._
import scala.swing.event.Key
import com.scala_game_of_life.world.{IEntityAccess, ICellAccess}
import scala.swing.Dimension
import net.lonning.loutput.ConsoleLOutput
import scala.annotation.elidable
import scala.annotation.elidable._
import net.nactors.ActorsHomeBuilder
import com.scala_game_of_life.engine._
import com.scala_game_of_life.engine.registries.{WorldNameRegistry, ScreenNameRegistry}
import com.scala_game_of_life.util.NumberUtil
import com.scala_game_of_life.engine.rendering.{RepainterThread, NoScreenData, SettingsPauseData, ExtendedScreenData}
import com.scala_game_of_life.engine.GameRenderer._
import java.lang.reflect.Field
import scala.collection.mutable
import java.io.File
import com.scala_game_of_life.engine.save.{SaveProcessor, Saveable}
import com.scala_game_of_life.engine.GameRenderer.RenderInfoHolder._
import net.lonning.Logger
import com.scala_game_of_life.engine.{TickCells, EnterTapped, TickEntities}
import scala.swing.event.{KeyPressed, KeyTyped}
import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

/** Main launcher of S-GOL.
  *
  * Extends Reactor not SwingApplication because
  * I(nabijaczleweli)'ve been getting weird errors about duplicate method&signature +
  * it's more fun this way.
  *
  * NOTE: All dates are in DD.MM.YY format.
  *
  * @author Jędrzej
  * @since  22.03.14
  */
object Main extends ConsoleLOutput("S-Main") with Reactor {

	import MainResources._
	import com.scala_game_of_life.PublicResources._
	import com.scala_game_of_life.engine.GameEngine.world

	def startup(args: Array[String]) {
		GameRendererResources
		var frame: MainFrame = null
		var frameField: Field = null
		val opcodes = mutable.Queue[() => Unit](() => {
			new File(Saveable.rootPath).mkdir
		}, () => {
			frame = top
		}, () => {
			if(frame.contents(0).size != desiredSize)
				frame.minimumSize = new Dimension(frame.minimumSize.width + (desiredSize.width - frame.contents(0).size.width), frame.minimumSize.height + (desiredSize.height - frame.contents(0).size.height))
		}, () => {
			frame = frame.pack()
		}, () => {
			frameField = GameRenderer.getClass.getDeclaredField("frame")
			frameField setAccessible true
		}, () => {
			frame.title = s"S-GOL:${(ScreenNameRegistry get GameRenderer.RenderInfoHolder.drawInfo).get}"
			frame.contents(0).requestFocus()
			frame.visible = true
			frameField.set(GameRenderer, frame)
		})
		for((s, w) <- helloStrings) {
			println(s)
			val t = System.currentTimeMillis
			if(!opcodes.isEmpty)
				opcodes.dequeue()()
			val ta = System.currentTimeMillis
			Thread sleep 0L.max(w - ta + t)
		}
		opcodes foreach {_()}
		ticker.start()
	}

	def saveWorld() =
		SaveProcessor.save[ICellAccess with IEntityAccess](world)

	/** From SimpleSwingApplication. */
	def top = new MainFrame {
		/** Temporarily, just to make things easier. Adjusted later on. */
		minimumSize = GameRenderer.desiredSize

		contents = new FlowPanel {
			minimumSize = GameRenderer.desiredSize

			import com.scala_game_of_life.engine.GameRenderer.RenderInfoHolder._
			import com.scala_game_of_life.engine.GameRenderer.{screenRepainter, isMetadataChangeAllowed}

			override def paint(g: Graphics2D) {
				GameRenderer.graph = g
				@elidable(FINEST)
				val t = System.currentTimeMillis
				drawInfo & 0xff match {
					case `maingame` =>
						val topLeftX = Math.floor(world.player.posX) - (GameRenderer.cellsInXAxis / 2)
						val topLeftY = Math.floor(world.player.posY) - (GameRenderer.cellsInYAxis / 2)
						GameRenderer.drawCellsInWorld(world, topLeftX.toInt, topLeftY.toInt)
						GameRenderer.drawEntitiesInWorld(world, topLeftX.toFloat, topLeftY.toFloat)
					case `settingspause` =>
						GameRenderer.drawSettings()
					case `choosesave` =>
						GameRenderer.drawChooseSave()
					case iNformation =>
						ERROR(s"I cannot understand screen #${NumberUtil setbit iNformation & 0xff} - quitting!")
						closeOperation()
				}
				@elidable(FINEST)
				val td = System.currentTimeMillis
				if(screenRepainter == null)
					debug(s"Drawing: ${td - t}ms.")
			}

			focusable = true
			listenTo(keys)
			reactions += {
				case KeyPressed(_, Key.H, Key.Modifier.Shift, _) =>
					world.player setInvisible !world.player.isInvisible
				case KeyPressed(_, Key.T, 0, _) =>
					if((drawInfo & maingame) != 0)
						gameEngine ! TickCells(1)
				case KeyPressed(_, Key.N, Key.Modifier.Control, _) if (drawInfo & maingame) != 0 =>
					world.particles foreach {_.setDead()}
					world.entities foreach {_.setDead()}
					world.player.setDead()
					world.collectGarbage()
					System.gc()
					world = WorldNameRegistry.get(s"$world").get.newInstance
				case KeyPressed(_, Key.R, Key.Modifier.Control, _) =>
					GameRenderer synchronized {
						GameRenderer.notifyAll()
					}
				case KeyPressed(_, Key.Escape, 0, _) =>
					if((drawInfo & maingame) != 0) {
						drawInfo &= ~0xff
						drawInfo |= settingspause
						extendedInfo = new SettingsPauseData().asInstanceOf[ExtendedScreenData[Any]]
						screenRepainter = new RepainterThread(20D) // 20FPS looks cool.
						metadata = 0
					} else if((drawInfo & choosesave) != 0)
						closeOperation()
					else {
						drawInfo &= ~0xff
						drawInfo |= maingame
						extendedInfo = NoScreenData.asInstanceOf[ExtendedScreenData[Any]]
						if(screenRepainter != null) {
							screenRepainter.interrupt()
							screenRepainter.join()
						}
						screenRepainter = new RepainterThread(31L)
						metadata = 0
					}
					title = s"S-GOL:${(ScreenNameRegistry get drawInfo).get}"
					keyboardMode = false
				case KeyPressed(_, Key.Down, 0, _) =>
					if((drawInfo & maingame) != 0)
						world.player.addVelocity(0, .3f)
					else if(isMetadataChangeAllowed(inc = true, MetadataChangeSource.Keyboard))
						metadata += 1
				case KeyPressed(_, Key.Up, 0, _) =>
					if((drawInfo & maingame) != 0)
						world.player.addVelocity(0, -.3f)
					else if(isMetadataChangeAllowed(inc = false, MetadataChangeSource.Keyboard))
						metadata -= 1
				case KeyPressed(_, Key.Right, 0, _) =>
					world.player.addVelocity(.3f, 0)
				case KeyPressed(_, Key.Left, 0, _) =>
					world.player.addVelocity(-.3f, 0)
				case KeyPressed(_, Key.Enter, 0, _) =>
					gameEngine ! EnterTapped()
				case KeyTyped(_, keyC, mods, _) if keyboardMode && (keyC != Key.Enter.id || keyC == Key.Escape.id) =>
					//assume(extendedInfo.isInstanceOf[ExtendedScreenData[String]])
					val saveData = extendedInfo.asInstanceOf[ExtendedScreenData[String]]
					if(keyC == Key.BackSpace.id && mods == 0)
						if(saveData.value.length > 0)
							saveData.value = saveData.value.reverse.substring(1).reverse
						else
							saveData.value = ""
					else
						saveData.value += keyC
					extendedInfo.value = saveData.value
					GameRenderer synchronized {
						GameRenderer.notifyAll()
					}
			}
		}

		override def closeOperation() {
			if(screenRepainter != null)
				screenRepainter.interrupt()
			ticker.interrupt()
			val killer = Future {actorsHome.killAll()}
			for((s, w) <- byeStrings) {
				println(s)
				Thread sleep w
			}
			while(!killer.isCompleted)
				Thread sleep 10
			System exit 0
		}
	}

	/** From SwingApplication. */
	def main(args: Array[String]) =
		Swing onEDT startup(args)

	private object MainResources {
		final lazy val helloStrings = List[(String, Long)]("Hello." -> 1000L, "What i have fo' you today is something pretty dang cool." -> 680L, "And that is..." -> 1200L, "S-GOL: Scala-GameOfLife" -> 3456L, "" -> 200L, "Starting..." -> 500L, "NOW!" -> 20L, "" -> 0L, "" -> 0L)
		final lazy val byeStrings   = List[(String, Long)]("So!" -> 250L, "This is nabijaczleweli standing off." -> 850L, "Hope you guys enjoyed the game." -> 500L, "And taaaaaaaake it easy!" -> 1456L)
		final      val ticker       = new Thread("S-Main:ticker") with Logger[ConsoleLOutput] {
			override def run() {
				try {
					while((drawInfo & 0xff) != maingame)
						Thread sleep 100
					while(!isInterrupted) {
						entityEngine ! TickEntities()
						Thread sleep 50 // Yields 20TPS
					}
				} catch {
					case _: InterruptedException =>
				}
				debug("I'm done!")
			}

			override val logger = new ConsoleLOutput(name)
		}
	}

}

object PublicResources {
	final val actorsHome   = ActorsHomeBuilder("S-GOL", GameEngine, EntityGameEngine)
	final val gameEngine   = actorsHome get GameEngine.name
	final val entityEngine = actorsHome get EntityGameEngine.name
}