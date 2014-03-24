package net.lonning.loutput

import scala.annotation.elidable
import scala.annotation.elidable._
import net.lonning.Formatter
import scala.io.AnsiColor._

/** Log output to Console.
  *
  * @author Jędrzej
  * @since  16.04.14
  */
class ConsoleLOutput(final val name: String)(implicit protected var color: Boolean = false) extends LogOutput {
	/** Writes line containing date, time, etc + <code>s</code> line to the info output.
	  *
	  * @return true, but false when elided
	  */
	@elidable(INFO)
	@inline
	override def info(s: String): Boolean = {
		println(s"${Formatter.getPreStuffs(name, INFO)} $s$end")
		true
	}

	/** Writes line containing date, time, etc + <code>s</code> line to the debug output.
	  *
	  * @return true, but false when elided
	  */
	@elidable(FINEST)
	@inline
	override def debug(s: String): Boolean = {
		println(s"${colorFor(FINEST)}${Formatter.getPreStuffs(name, FINEST)} $s$end")
		true
	}

	/** Writes line containing date, time, etc + <code>s</code> line to the log output.
	  *
	  * @return true, but false when elided
	  */
	@elidable(FINER)
	@inline
	override def log(s: String): Boolean = {
		println(s"${colorFor(FINER)}${Formatter.getPreStuffs(name, FINER)} $s$end")
		true
	}

	/** Writes line containing date, time, etc + <code>s</code> line to the warning output.
	  *
	  * @return true, but false when elided
	  */
	@elidable(WARNING)
	@inline
	override def warn(s: String): Boolean = {
		println(s"${colorFor(WARNING)}${Formatter.getPreStuffs(name, WARNING)} $s$end")
		true
	}

	@inline
	private def colorFor(level: Int) =
		if(color)
			level match {
				case WARNING =>
					YELLOW
				case FINER =>
					GREEN
				case FINEST =>
					GREEN_B
				case SEVERE =>
					YELLOW_B
				case 5000 =>
					RED_B
				case _ =>
					""
			}
		else
			""

	@inline
	private def end =
		if(color)
			RESET
		else
			""

	/** Writes line containing date, time, etc + <code>s</code> line to the severe output.
	  *
	  * @return true, but false when elided
	  */
	@elidable(SEVERE)
	@inline
	override def severe(s: String): Boolean = {
		println(s"${colorFor(SEVERE)}${Formatter.getPreStuffs(name, SEVERE)} $s$end")
		true
	}

	/** Writes line containing date, time, etc + <code>s</code> line to the ERROR output.
	  *
	  * @return true
	  */
	@inline
	override def ERROR(s: String): Boolean = {
		println(s"${colorFor(5000)}${Formatter.getPreStuffs(name, 5000)} $s$end")
		true
	}
}
