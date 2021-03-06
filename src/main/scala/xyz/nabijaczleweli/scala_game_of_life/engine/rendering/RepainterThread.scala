package xyz.nabijaczleweli.scala_game_of_life.engine.rendering

import xyz.nabijaczleweli.lonning.Logger
import xyz.nabijaczleweli.lonning.loutput.ConsoleLOutput
import xyz.nabijaczleweli.scala_game_of_life.engine.GameRenderer

/** A thread for repainting [[GameRenderer]].
  *
  * @author Jędrzej
  * @since  09.05.14
  */
final class RepainterThread(val delay: Long) extends Thread("S-GameRenderer:repainter") with Logger[ConsoleLOutput] {
	def this(FPS: Double) =
		this((1000D / FPS).toLong)

	override val logger = new ConsoleLOutput(getName)

	override def run() {
		try {
			while(!isInterrupted) {
				GameRenderer synchronized {
					GameRenderer.notifyAll()
				}
				Thread sleep delay
			}
		} catch {
			case _: InterruptedException =>
		}
		debug("I\'m done!")
	}

	super.start()
}
