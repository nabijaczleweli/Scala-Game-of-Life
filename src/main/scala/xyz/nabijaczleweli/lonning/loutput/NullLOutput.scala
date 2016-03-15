package xyz.nabijaczleweli.lonning.loutput

import scala.annotation.elidable
import scala.annotation.elidable._

/** @author Jędrzej
  * @since  22.04.14
  */
object NullLOutput extends LogOutput {
	/** Does nothing
	  *
	  * @return true, but false when elided
	  */
	@elidable(WARNING)
	@inline
	override def warn(s: String): Boolean =
		true

	/** Does nothing
	  *
	  * @return true, but false when elided
	  */
	@elidable(FINE)
	@inline
	override def info(s: String): Boolean =
		true

	/** Does nothing
	  *
	  * @return true, but false when elided
	  */
	@elidable(WARNING)
	@inline
	override def debug(s: String): Boolean =
		true

	/** Does nothing
	  *
	  * @return true, but false when elided
	  */
	override def log(s: String): Boolean =
		true

	/** Does nothing
	  *
	  * @return true, but false when elided
	  */
	@elidable(WARNING)
	@inline
	override def warn(s: String, throwable: Throwable): Boolean =
		true

	/** Does nothing
	  *
	  * @return true, but false when elided
	  */
	@elidable(WARNING)
	@inline
	override def warn(s: String, varargs: Any*): Boolean =
		true

	/** Does nothing
	  *
	  * @return true, but false when elided
	  */
	@elidable(WARNING)
	@inline
	override def warn: Boolean = true

	/** Does nothing
	  *
	  * @return true, but false when elided
	  */
	@elidable(FINE)
	@inline
	override def info(s: String, throwable: Throwable): Boolean =
		true

	/** Does nothing
	  *
	  * @return true, but false when elided
	  */
	@elidable(FINE)
	@inline
	override def info(s: String, varargs: Any*): Boolean =
		true

	/** Does nothing
	  *
	  * @return true, but false when elided
	  */
	@elidable(FINE)
	@inline
	override def info: Boolean =
		true

	/** Does nothing
	  *
	  * @return true, but false when elided
	  */
	@elidable(WARNING)
	@inline
	override def debug(s: String, throwable: Throwable): Boolean =
		true

	/** Does nothing
	  *
	  * @return true, but false when elided
	  */
	@elidable(WARNING)
	@inline
	override def debug(s: String, varargs: Any*): Boolean =
		true

	/** Does nothing
	  *
	  * @return true, but false when elided
	  */
	@elidable(WARNING)
	@inline
	override def debug: Boolean =
		true

	/** Does nothing
	  *
	  * @return true, but false when elided
	  */
	@elidable(FINER)
	@inline
	override def log(s: String, throwable: Throwable): Boolean =
		true

	/** Does nothing
	  *
	  * @return true, but false when elided
	  */
	@elidable(FINER)
	@inline
	override def log(s: String, varargs: Any*): Boolean =
		true

	/** Does nothing
	  *
	  * @return true, but false when elided
	  */
	@elidable(FINER)
	@inline
	override def log: Boolean =
		true

	/** Does nothing
	  *
	  * @return true, but false when elided
	  */
	@elidable(SEVERE)
	@inline
	override def severe(s: String, throwable: Throwable): Boolean =
		true

	/** Does nothing
	  *
	  * @return true, but false when elided
	  */
	@elidable(SEVERE)
	@inline
	override def severe(s: String, varargs: Any*): Boolean =
		true

	/** Does nothing
	  *
	  * @return true, but false when elided
	  */
	@elidable(SEVERE)
	@inline
	override def severe: Boolean =
		true

	/** Does nothing
	  *
	  * @return true, but false when elided
	  */
	@elidable(SEVERE)
	@inline
	override def severe(s: String): Boolean =
		true

	/** Writes line containing date, time, etc + <code>s</code> line to the ERROR output.
	  *
	  * @return true
	  */
	@inline
	override def ERROR(s: String): Boolean =
		true

	/** Writes empty (note, however, all the dates and such are kept) line to the ERROR output.
	  *
	  * @return true
	  */
	@inline
	override def ERROR: Boolean =
		true

	/** Writes line containing date, time, etc + <code>s format varargs<code> line to the ERROR output.
	  *
	  * @return true
	  */
	@inline
	override def ERROR(s: String, varargs: Any*): Boolean =
		true

	/** Writes line containing date, time, etc + <code>s + throwable.getClass.getSimpleName</code> and
	  * repeatedly getStackTrace(i) for i 1 until getStackTrace.length line to the ERROR output.
	  *
	  * @return true
	  */
	@inline
	override def ERROR(s: String, throwable: Throwable): Boolean =
		true
}
