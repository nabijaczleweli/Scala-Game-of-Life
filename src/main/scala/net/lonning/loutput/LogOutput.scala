package net.lonning.loutput

import scala.annotation.elidable
import scala.annotation.elidable._

/** All subclasses <b>shall</b> <b>interhit</b> annotations and/or be <b>cast</b> to this trait.
  *
  * @author Jędrzej
  * @since  16.04.14
  */
trait LogOutput {
	/** Writes empty (note, however, all the dates and such are kept) line to the log output.
	  *
	  * @return true, but false when elided
	  */
	@elidable(FINER)
	@inline
	def log: Boolean =
		log("")

	/** Writes line containing date, time, etc + <code>s</code> line to the log output.
	  *
	  * @return true, but false when elided
	  */
	@elidable(FINER)
	@inline
	def log(s: String): Boolean

	/** Writes line containing date, time, etc + <code>s format varargs<code> line to the log output.
	  *
	  * @return true, but false when elided
	  */
	@elidable(FINER)
	@inline
	def log(s: String, varargs: Any*): Boolean =
		log(s format varargs)

	/** Writes line containing date, time, etc + <code>s + throwable.getClass.getSimpleName</code> and
	  * repeateadly getStackTrace(i) for i 1 until getStackTrace.length line to the log output.
	  *
	  * @return true, but false when elided
	  */
	@elidable(FINER)
	@inline
	def log(s: String, throwable: Throwable): Boolean = {
		val st = throwable.getStackTrace
		log(s"$s${throwable.getClass.getSimpleName }: ${throwable.getMessage }")
		for(i <- 0 until st.length)
			log(s"\t${st(i) }")
		true
	}

	/** Writes empty (note, however, all the dates and such are kept) line to the debug output.
	  *
	  * @return true, but false when elided
	  */
	@elidable(FINEST)
	@inline
	def debug: Boolean =
		debug("")

	/** Writes line containing date, time, etc + <code>s</code> line to the debug output.
	  *
	  * @return true, but false when elided
	  */
	@elidable(FINEST)
	@inline
	def debug(s: String): Boolean

	/** Writes line containing date, time, etc + <code>s format varargs<code> line to the debug output.
	  *
	  * @return true, but false when elided
	  */
	@elidable(FINEST)
	@inline
	def debug(s: String, varargs: Any*): Boolean =
		debug(s format varargs)

	/** Writes line containing date, time, etc + <code>s + throwable.getClass.getSimpleName</code> and
	  * repeateadly getStackTrace(i) for i 1 until getStackTrace.length line to the debug output.
	  *
	  * @return true, but false when elided
	  */
	@elidable(FINEST)
	@inline
	def debug(s: String, throwable: Throwable): Boolean = {
		val st = throwable.getStackTrace
		debug(s"$s${throwable.getClass.getSimpleName }: ${throwable.getMessage }")
		for(i <- 0 until st.length)
			debug(s"\t${st(i) }")
		true
	}

	/** Writes empty (note, however, all the dates and such are kept) line to the info output.
	  *
	  * @return true, but false when elided
	  */
	@elidable(FINE)
	@inline
	def info: Boolean =
		info("")

	/** Writes line containing date, time, etc + <code>s</code> line to the info output.
	  *
	  * @return true, but false when elided
	  */
	@elidable(INFO)
	@inline
	def info(s: String): Boolean

	/** Writes line containing date, time, etc + <code>s format varargs<code> line to the info output.
	  *
	  * @return true, but false when elided
	  */
	@elidable(FINE)
	@inline
	def info(s: String, varargs: Any*): Boolean =
		info(s format varargs)

	/** Writes line containing date, time, etc + <code>s + throwable.getClass.getSimpleName</code> and
	  * repeateadly getStackTrace(i) for i 1 until getStackTrace.length line to the info output.
	  *
	  * @return true, but false when elided
	  */
	@elidable(FINE)
	@inline
	def info(s: String, throwable: Throwable): Boolean = {
		val st = throwable.getStackTrace
		info(s"$s${throwable.getClass.getSimpleName }: ${throwable.getMessage }")
		for(i <- 0 until st.length)
			info(s"\t${st(i) }")
		true
	}

	/** Writes empty (note, however, all the dates and such are kept) line to the warning output.
	  *
	  * @return true, but false when elided
	  */
	@elidable(WARNING)
	@inline
	def warn: Boolean =
		warn("")

	/** Writes line containing date, time, etc + <code>s</code> line to the warning output.
	  *
	  * @return true, but false when elided
	  */
	@elidable(WARNING)
	@inline
	def warn(s: String): Boolean

	/** Writes line containing date, time, etc + <code>s format varargs<code> line to the warning output.
	  *
	  * @return true, but false when elided
	  */
	@elidable(WARNING)
	@inline
	def warn(s: String, varargs: Any*): Boolean =
		warn(s format varargs)

	/** Writes line containing date, time, etc + <code>s + throwable.getClass.getSimpleName</code> and
	  * repeateadly getStackTrace(i) for i 1 until getStackTrace.length line to the warning output.
	  *
	  * @return true, but false when elided
	  */
	@elidable(WARNING)
	@inline
	def warn(s: String, throwable: Throwable): Boolean = {
		val st = throwable.getStackTrace
		warn(s"$s${throwable.getClass.getSimpleName }: ${throwable.getMessage }")
		for(i <- 0 until st.length)
			warn(s"\t${st(i) }")
		true
	}

	/** Writes empty (note, however, all the dates and such are kept) line to the severe output.
	  *
	  * @return true, but false when elided
	  */
	@elidable(SEVERE)
	@inline
	def severe: Boolean =
		severe("")

	/** Writes line containing date, time, etc + <code>s</code> line to the severe output.
	  *
	  * @return true, but false when elided
	  */
	@elidable(SEVERE)
	@inline
	def severe(s: String): Boolean

	/** Writes line containing date, time, etc + <code>s format varargs<code> line to the severe output.
	  *
	  * @return true, but false when elided
	  */
	@elidable(SEVERE)
	@inline
	def severe(s: String, varargs: Any*): Boolean =
		severe(s format varargs)

	/** Writes line containing date, time, etc + <code>s + throwable.getClass.getSimpleName</code> and
	  * repeateadly getStackTrace(i) for i 1 until getStackTrace.length line to the severe output.
	  *
	  * @return true, but false when elided
	  */
	@elidable(SEVERE)
	@inline
	def severe(s: String, throwable: Throwable): Boolean = {
		val st = throwable.getStackTrace
		severe(s"$s${throwable.getClass.getSimpleName }: ${throwable.getMessage }")
		for(i <- 0 until st.length)
			severe(s"\t${st(i) }")
		true
	}

	/** Writes empty (note, however, all the dates and such are kept) line to the ERROR output.
	  *
	  * @return true, but false when elided
	  */
	@inline
	def ERROR: Boolean =
		ERROR("")

	/** Writes line containing date, time, etc + <code>s</code> line to the ERROR output.
	  *
	  * @return true
	  */
	@inline
	def ERROR(s: String): Boolean

	/** Writes line containing date, time, etc + <code>s format varargs<code> line to the ERROR output.
	  *
	  * @return true
	  */
	@inline
	def ERROR(s: String, varargs: Any*): Boolean =
		ERROR(s format varargs)

	/** Writes line containing date, time, etc + <code>s + throwable.getClass.getSimpleName</code> and
	  * repeateadly getStackTrace(i) for i 1 until getStackTrace.length line to the ERROR output.
	  *
	  * @return true
	  */
	@inline
	def ERROR(s: String, throwable: Throwable): Boolean = {
		val st = throwable.getStackTrace
		ERROR(s"$s${throwable.getClass.getSimpleName }: ${throwable.getMessage }")
		for(i <- 0 until st.length)
			ERROR(s"\t${st(i) }")
		true
	}
}
