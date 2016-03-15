package xyz.nabijaczleweli.scala_game_of_life.engine

import xyz.nabijaczleweli.nactors.Actor
import xyz.nabijaczleweli.scala_game_of_life.cell.{CellAction, Cell}
import xyz.nabijaczleweli.scala_game_of_life.engine.registries.{WorldFuncRegistry, EnterActionRegistry}
import xyz.nabijaczleweli.scala_game_of_life.engine.save.SaveProcessor
import xyz.nabijaczleweli.scala_game_of_life.world.{ICellAccess, WorldOver, IEntityAccess}
import scala.util.Random
import CellAction.CellAction
import GameRenderer.RenderInfoHolder.drawInfo

/** Processes all the things.
  *
  * @author Jędrzej
  * @since  01.04.14
  */
object GameEngine extends Actor("S-Engine") {
	final var world: ICellAccess with IEntityAccess = new WorldOver
	/** All <tt>wait()</tt>ers are notified on this object whenever [[GameEngine]] changes its <tt>world</tt> on its own. */
	final val rand                                  = new Random
	final val nameOfSave: String                    = null

	override def receive = {
		case TickCells(0) =>
		case tal: TickCells if tal.amount < 0 =>
			warn(s"You cannot ${tal.toString.replaceAllLiterally("(", " ").replaceAllLiterally(")", "")} times since it\'s a negative amount!")
		case TickCells(amount) =>
			log(s"Ticking cells in \'$world\' $amount time${if(amount > 1) "s" else ""}...")
			val coordsfunc = WorldFuncRegistry.get(s"$world").get
			val timeBefore = System.currentTimeMillis.toDouble
			for(_ <- 1 to amount; (x, y) <- coordsfunc(world.rules.tickAtOnce)) {
				val c = world.cellAtCoords(x, y, create = false)
				c.onUpdate(x, y)
				val surr = surroundings(x, y)
				var changed: Option[CellAction] = None
				surr.count(Cell countFunc world) match {
					case cnt if !world.cellState(x, y) && world.rules.resurrect.contains(cnt) =>
						world.cellState(x, y, newState = true)
						c.onCellAction(x, y, CellAction.RESURRECTED)
						changed = Some(CellAction.RESURRECTED)
					case cnt if world.cellState(x, y) && world.rules.stillAlive.contains(cnt) =>
						c.onCellAction(x, y, CellAction.STAYED)
					case _ if world.cellState(x, y) =>
						world.cellState(x, y, newState = false)
						c.onCellAction(x, y, CellAction.KILLED)
						changed = Some(CellAction.KILLED)
					case _ =>
						c.onCellAction(x, y, CellAction.STAYED)
				}
				world.cellAtCoords(x, y, c)
				if(changed.isDefined)
					for((cell, ecks, why) <- surr)
						cell.onNeighbourCellChange(ecks, why, (c, x, y), changed.get)
			}
			val timeAfter = System.currentTimeMillis.toDouble
			info(s"Mean tick time: ${timeAfter / amount.toDouble - timeBefore / amount.toDouble}ms.")
			info(s"Overall ticking time: ${timeAfter - timeBefore}ms.")
			rand synchronized {
				rand.notifyAll()
			}
			GameRenderer synchronized {
				GameRenderer.notifyAll()
			}
		case SaveWorld() =>
			SaveProcessor.save[ICellAccess](world)
		case EnterTapped() =>
			EnterActionRegistry.get(drawInfo).get()
		case LoadWorld() =>
			world = SaveProcessor.load[ICellAccess with IEntityAccess](world)
			rand synchronized {
				rand.notifyAll()
			}
			GameRenderer synchronized {
				GameRenderer.notifyAll()
			}
		case iGnored =>
			throw new UnsupportedOperationException(s"I cannot handle \'$iGnored\'!")
	}

	def surroundings(x: Long, y: Long) =
		for(ix <- (x - 1) to (x + 1);
		    iy <- (y - 1) to (y + 1)
		    if ix != x
		    if iy != y
		) yield
			(world.cellAtCoords(ix, iy, create = false), ix, iy)
}

/** Processes all the things related to entities.
  *
  * @author Jędrzej
  * @since  14.05.14
  */
object EntityGameEngine extends Actor("S-EntityEngine", TickEntities() :: Nil) {
	import xyz.nabijaczleweli.scala_game_of_life.engine.GameEngine._

	override def receive = {
		case TickEntities() =>
			for(ent <- world.entities)
				ent.tick()
			if(world.player != null && world.player.isEntityAlive)
				world.player.tick()
			for(ent <- world.particles)
				ent.tick()
			rand synchronized {
				rand.notifyAll()
			}
			GameRenderer synchronized {
				GameRenderer.notifyAll()
			}
		case iGnored =>
			throw new UnsupportedOperationException(s"I cannot handle \'$iGnored\'!")
	}
}

/** Causes game engine to tick its current world `amount` times. */
final case class TickCells(amount: Int)

/** Causes entity engine to tick entities in its current world 1 time. */
final case class TickEntities()

/** Causes game engine to save its current `world`. */
final case class SaveWorld()

/** Causes game engine to set its `world` to a newly loaded one. */
final case class LoadWorld()

/** Causes game engine to invoke enter action for current screen. */
final case class EnterTapped()
