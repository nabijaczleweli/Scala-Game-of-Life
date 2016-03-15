package xyz.nabijaczleweli.lonning

import xyz.nabijaczleweli.lonning.loutput.LogOutput
import scala.annotation.elidable
import scala.annotation.elidable._
import scala.annotation.meta.field

/** Mixin trait for logging.
  *
  * @author Jędrzej
  * @since  18.04.14
  */
trait Logger[T <: LogOutput] {
	val logger: T

	/** Writes empty (note, however, all the dates and such are kept) line to the log output.
	  *
	  * @return true, but false when elided
	  */
	@elidable(FINER)
	def log: Boolean =
		logger.log

	/** Writes line containing date, time, etc + <code>s</code> line to the log output.
	  *
	  * @return true, but false when elided
	  */
	@elidable(FINER)
	def log(s: String): Boolean =
		logger.log(s)

	/** Writes line containing date, time, etc + <code>s format varargs<code> line to the log output.
	  *
	  * @return true, but false when elided
	  */
	@elidable(FINER)
	def log(s: String, varargs: Any*): Boolean =
		logger.log(s, varargs)

	/** Writes line containing date, time, etc + <code>s + throwable.getClass.getSimpleName</code> and
	  * repeateadly getStackTrace(i) for i 1 until getStackTrace.length line to the log output.
	  *
	  * @return true, but false when elided
	  */
	@elidable(FINER)
	def log(s: String, throwable: Throwable): Boolean =
		logger.log(s, throwable)

	/** Writes empty (note, however, all the dates and such are kept) line to the debug output.
	  *
	  * @return true, but false when elided
	  */
	@elidable(FINEST)
	def debug: Boolean =
		logger.debug

	/** Writes line containing date, time, etc + <code>s</code> line to the debug output.
	  *
	  * @return true, but false when elided
	  */
	@elidable(FINEST)
	def debug(s: String): Boolean =
		logger.debug(s)

	/** Writes line containing date, time, etc + <code>s format varargs<code> line to the debug output.
	  *
	  * @return true, but false when elided
	  */
	@elidable(FINEST)
	def debug(s: String, varargs: Any*): Boolean =
		logger.debug(s, varargs)

	/** Writes line containing date, time, etc + <code>s + throwable.getClass.getSimpleName</code> and
	  * repeateadly getStackTrace(i) for i 1 until getStackTrace.length line to the debug output.
	  *
	  * @return true, but false when elided
	  */
	@elidable(FINEST)
	def debug(s: String, throwable: Throwable): Boolean =
		logger.debug(s, throwable)

	/** Writes empty (note, however, all the dates and such are kept) line to the info output.
	  *
	  * @return true, but false when elided
	  */
	@elidable(FINE)
	def info: Boolean =
		logger.info

	/** Writes line containing date, time, etc + <code>s</code> line to the info output.
	  *
	  * @return true, but false when elided
	  */
	@elidable(INFO)
	def info(s: String): Boolean =
		logger.info(s)

	/** Writes line containing date, time, etc + <code>s format varargs<code> line to the info output.
	  *
	  * @return true, but false when elided
	  */
	@elidable(FINE)
	def info(s: String, varargs: Any*): Boolean =
		logger.info(s, varargs)

	/** Writes line containing date, time, etc + <code>s + throwable.getClass.getSimpleName</code> and
	  * repeateadly getStackTrace(i) for i 1 until getStackTrace.length line to the info output.
	  *
	  * @return true, but false when elided
	  */
	@elidable(FINE)
	def info(s: String, throwable: Throwable): Boolean =
		logger.info(s, throwable)

	/** Writes empty (note, however, all the dates and such are kept) line to the warning output.
	  *
	  * @return true, but false when elided
	  */
	@elidable(WARNING)
	def warn: Boolean =
		logger.warn

	/** Writes line containing date, time, etc + <code>s</code> line to the warning output.
	  *
	  * @return true, but false when elided
	  */
	@elidable(WARNING)
	def warn(s: String): Boolean =
		logger.warn(s)

	/** Writes line containing date, time, etc + <code>s format varargs<code> line to the warning output.
	  *
	  * @return true, but false when elided
	  */
	@elidable(WARNING)
	def warn(s: String, varargs: Any*): Boolean =
		logger.warn(s, varargs)

	/** Writes line containing date, time, etc + <code>s + throwable.getClass.getSimpleName</code> and
	  * repeateadly getStackTrace(i) for i 1 until getStackTrace.length line to the warning output.
	  *
	  * @return true, but false when elided
	  */
	@elidable(WARNING)
	def warn(s: String, throwable: Throwable): Boolean =
		logger.warn(s, throwable)

	/** Writes empty (note, however, all the dates and such are kept) line to the warning output.
	  *
	  * @return true, but false when elided
	  */
	@elidable(SEVERE)
	def severe: Boolean =
		logger.severe

	/** Writes line containing date, time, etc + <code>s</code> line to the warning output.
	  *
	  * @return true, but false when elided
	  */
	@elidable(SEVERE)
	def severe(s: String): Boolean =
		logger.severe(s)

	/** Writes line containing date, time, etc + <code>s format varargs<code> line to the warning output.
	  *
	  * @return true, but false when elided
	  */
	@elidable(SEVERE)
	def severe(s: String, varargs: Any*): Boolean =
		logger.severe(s, varargs)

	/** Writes line containing date, time, etc + <code>s + throwable.getClass.getSimpleName</code> and
	  * repeateadly getStackTrace(i) for i 1 until getStackTrace.length line to the warning output.
	  *
	  * @return true, but false when elided
	  */
	@elidable(SEVERE)
	def severe(s: String, throwable: Throwable): Boolean =
		logger.severe(s, throwable)

	/** Writes empty (note, however, all the dates and such are kept) line to the ERROR output.
	  *
	  * @return true, but false when elided
	  */
	def ERROR: Boolean =
		logger.ERROR

	/** Writes line containing date, time, etc + <code>s format varargs<code> line to the ERROR output.
	  *
	  * @return true, but false when elided
	  */
	def ERROR(s: String, varargs: Any*): Boolean =
		logger.ERROR(s, varargs)

	/** Writes line containing date, time, etc + <code>s + throwable.getClass.getSimpleName</code> and
	  * repeateadly getStackTrace(i) for i 1 until getStackTrace.length line to the ERROR output.
	  *
	  * @return true, but false when elided
	  */
	def ERROR(s: String, throwable: Throwable): Boolean =
		logger.ERROR(s, throwable)

	/** Writes line containing date, time, etc + <code>s</code> line to the ERROR output.
	  *
	  * @return true
	  */

	def ERROR(s: String): Boolean =
		logger.ERROR(s)
}
