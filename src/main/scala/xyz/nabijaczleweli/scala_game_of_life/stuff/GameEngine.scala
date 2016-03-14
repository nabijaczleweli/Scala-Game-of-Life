package xyz.nabijaczleweli.scala_game_of_life.stuff

import xyz.nabijaczleweli.nactors.Actor

/** Processes all the everythings.
  *
  * @author Jędrzej
  * @since  01.04.14
  */
class GameEngine extends Actor {
	override def receive = {
		case Tick(world) =>
			println("Ticking world called \'" + world.toString + '\'')
	}
}

case class Tick(w: ICellAccess)
