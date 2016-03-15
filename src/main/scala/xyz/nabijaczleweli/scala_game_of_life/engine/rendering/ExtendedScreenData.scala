package xyz.nabijaczleweli.scala_game_of_life.engine.rendering

import xyz.nabijaczleweli.lonning.Logger
import xyz.nabijaczleweli.lonning.loutput.ConsoleLOutput

/** Used to hold additional data for screen.
  *
  * @author Jędrzej
  * @since  27.04.14
  */
trait ExtendedScreenData[stored_type] extends Logger[ConsoleLOutput] {
	override val logger = new ConsoleLOutput(getClass.getSimpleName)

	private var stored: stored_type = startup

	def value: stored_type = stored

	def value_=(newData: stored_type): Unit =
		stored = newData

	protected def startup: stored_type = {
		ERROR(s"You MUST to override the \'startup\' method from the \'${getClass.getName}\' trait.", new NotImplementedError("You do must."))
		System exit 1
		while(true)
			throw null
		throw null
	}
}
