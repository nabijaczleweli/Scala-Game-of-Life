package xyz.nabijaczleweli.lonning.loutput

import xyz.nabijaczleweli.lonning.Formatter

import scala.annotation.elidable
import scala.annotation.elidable._
import Formatter._

/** @author Jędrzej
  * @since  17.04.14
  */
class StringLOutput(final val name: String) extends LogOutput {
	import java.lang.System.{lineSeparator => nl}

	@volatile
	var logged = ""

	/** Writes line containing date, time, etc + <code>s</code> line to the info output.
	  *
	  * @return true, but false when elided
	  */
	@elidable(INFO)
	@inline
	override def info(s: String): Boolean = {
		logged += s"${getPreStuffs(name, INFO)} $s$nl"
		true
	}

	/** Writes line containing date, time, etc + <code>s</code> line to the debug output.
	  *
	  * @return true, but false when elided
	  */
	@elidable(FINEST)
	@inline
	override def debug(s: String): Boolean = {
		logged += s"${getPreStuffs(name, FINEST) } $s$nl"
		true
	}

	/** Writes line containing date, time, etc + <code>s</code> line to the log output.
	  *
	  * @return true, but false when elided
	  */
	@elidable(FINER)
	@inline
	override def log(s: String): Boolean = {
		logged += s"${getPreStuffs(name, FINER)} $s$nl"
		true
	}

	/** Writes line containing date, time, etc + <code>s</code> line to the warning output.
	  *
	  * @return true, but false when elided
	  */
	@elidable(WARNING)
	@inline
	override def warn(s: String): Boolean = {
		logged += s"${getPreStuffs(name, WARNING)} $s$nl"
		true
	}

	/** Writes line containing date, time, etc + <code>s</code> line to the severe output.
	  *
	  * @return true, but false when elided
	  */
	@elidable(SEVERE)
	@inline
	override def severe(s: String): Boolean = {
		logged += s"${getPreStuffs(name, SEVERE)} $s$nl"
		true
	}

	/** Writes line containing date, time, etc + <code>s</code> line to the ERROR output.
	  *
	  * @return true
	  */
	@inline
	override def ERROR(s: String): Boolean = {
		logged += s"${getPreStuffs(name, 5000)} $s$nl"
		true
	}
}
