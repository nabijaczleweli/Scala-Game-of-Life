package xyz.nabijaczleweli.lonning.loutput

import xyz.nabijaczleweli.lonning.Formatter

import scala.annotation.elidable
import scala.annotation.elidable._
import Formatter._
import java.io.FileWriter

/** @author Jędrzej
  * @since  17.04.14
  */
class FileLOutput(final val name: String, final val filename: String)(implicit private final val overrideFile: Boolean = true) extends LogOutput with AutoCloseable {

	import java.lang.System.{lineSeparator => nl}

	/** Auromatically closes itself (implements AutoCloseable). */
	protected final lazy val fw = new FileWriter(filename, !overrideFile)

	/** Writes line containing date, time, etc + <code>s</code> line to the info output.
	  *
	  * @return true, but false when elided
	  */
	@elidable(INFO)
	@inline
	override def info(s: String): Boolean = {
		try {
			fw.write(s"${getPreStuffs(name, INFO)} $s$nl")
			ensureFlushed()
		} catch {
			case _: Throwable =>
		}
		true
	}

	/** Writes line containing date, time, etc + <code>s</code> line to the debug output.
	  *
	  * @return true, but false when elided
	  */
	@elidable(FINEST)
	@inline
	override def debug(s: String): Boolean = {
		try {
			fw.write(s"${getPreStuffs(name, FINEST)} $s$nl")
			ensureFlushed()
		} catch {
			case _: Throwable =>
		}
		true
	}

	/** Writes line containing date, time, etc + <code>s</code> line to the log output.
	  *
	  * @return true, but false when elided
	  */
	@elidable(FINER)
	@inline
	override def log(s: String): Boolean = {
		try {
			fw.write(s"${getPreStuffs(name, FINER)} $s$nl")
			ensureFlushed()
		} catch {
			case _: Throwable =>
		}
		true
	}

	/** Writes line containing date, time, etc + <code>s</code> line to the warning output.
	  *
	  * @return true, but false when elided
	  */
	@elidable(WARNING)
	@inline
	override def warn(s: String): Boolean = {
		try {
			fw.write(s"${getPreStuffs(name, WARNING)} $s$nl")
			ensureFlushed()
		} catch {
			case _: Throwable =>
		}
		true
	}

	def ensureFlushed() =
		try {
			fw.flush()
		} catch {
			case _: Throwable =>
		}

	override def close() =
		try {
			ensureFlushed()
			fw.close()
		} catch {
			case _: Throwable =>
		}


	/** Writes line containing date, time, etc + <code>s</code> line to the severe output.
	  *
	  * @return true, but false when elided
	  */
	@elidable(SEVERE)
	@inline
	override def severe(s: String): Boolean = {
		try {
			fw.write(s"${getPreStuffs(name, SEVERE)} $s$nl")
			ensureFlushed()
		} catch {
			case _: Throwable =>
		}
		true
	}

	/** Writes line containing date, time, etc + <code>s + throwable.getClass.getSimpleName</code> and
	  * repeatedly getStackTrace(i) for i 1 until getStackTrace.length line to the severe output.
	  *
	  * @return true, but false when elided
	  */
	@inline
	@elidable(SEVERE)
	override def severe(s: String, throwable: Throwable): Boolean = {
		try {
			val st = throwable.getStackTrace
			fw.write(s"${getPreStuffs(name, SEVERE)} $s${throwable.getClass.getSimpleName}: ${throwable.getMessage}$nl")
			for(i <- 0 until st.length)
				fw.write(s"${getPreStuffs(name, SEVERE)} \t${st(i).toString}$nl")
			ensureFlushed()
		} catch {
			case _: Throwable =>
		}
		true
	}

	/** Writes line containing date, time, etc + <code>s + throwable.getClass.getSimpleName</code> and
	  * repeatedly getStackTrace(i) for i 1 until getStackTrace.length line to the warning output.
	  *
	  * @return true, but false when elided
	  */
	@inline
	@elidable(WARNING)
	override def warn(s: String, throwable: Throwable): Boolean = {
		try {
			val st = throwable.getStackTrace
			fw.write(s"${getPreStuffs(name, WARNING)} $s${throwable.getClass.getSimpleName}: ${throwable.getMessage}$nl")
			for(i <- 0 until st.length)
				fw.write(s"${getPreStuffs(name, WARNING)} \t${st(i).toString}$nl")
			ensureFlushed()
		} catch {
			case _: Throwable =>
		}
		true
	}

	/** Writes line containing date, time, etc + <code>s + throwable.getClass.getSimpleName</code> and
	  * repeatedly getStackTrace(i) for i 1 until getStackTrace.length line to the info output.
	  *
	  * @return true, but false when elided
	  */
	@inline
	@elidable(INFO)
	override def info(s: String, throwable: Throwable): Boolean = {
		try {
			val st = throwable.getStackTrace
			fw.write(s"${getPreStuffs(name, INFO)} $s${throwable.getClass.getSimpleName}: ${throwable.getMessage}$nl")
			for(i <- 0 until st.length)
				fw.write(s"${getPreStuffs(name, INFO)} \t${st(i).toString}$nl")
			ensureFlushed()
		} catch {
			case _: Throwable =>
		}
		true
	}

	/** Writes line containing date, time, etc + <code>s + throwable.getClass.getSimpleName</code> and
	  * repeatedly getStackTrace(i) for i 1 until getStackTrace.length line to the debug output.
	  *
	  * @return true, but false when elided
	  */
	@inline
	@elidable(FINEST)
	override def debug(s: String, throwable: Throwable): Boolean = {
		try {
			val st = throwable.getStackTrace
			fw.write(s"${getPreStuffs(name, FINEST)} $s${throwable.getClass.getSimpleName}: ${throwable.getMessage}$nl")
			for(i <- 0 until st.length)
				fw.write(s"${getPreStuffs(name, FINEST)} \t${st(i).toString}$nl")
			ensureFlushed()
		} catch {
			case _: Throwable =>
		}
		true
	}

	/** Writes line containing date, time, etc + <code>s + throwable.getClass.getSimpleName</code> and
	  * repeatedly getStackTrace(i) for i 1 until getStackTrace.length line to the log output.
	  *
	  * @return true, but false when elided
	  */
	@inline
	@elidable(FINER)
	override def log(s: String, throwable: Throwable): Boolean = {
		try {
			val st = throwable.getStackTrace
			fw.write(s"${getPreStuffs(name, FINER)} $s${throwable.getClass.getSimpleName}: ${throwable.getMessage}$nl")
			for(i <- 0 until st.length)
				fw.write(s"${getPreStuffs(name, FINER)} \t${st(i).toString}$nl")
			ensureFlushed()
		} catch {
			case _: Throwable =>
		}
		true
	}

	/** Writes line containing date, time, etc + <code>s</code> line to the ERROR output.
	  *
	  * @return true
	  */
	@inline
	override def ERROR(s: String): Boolean = {
		try {
			fw.write(s"${getPreStuffs(name, 5000)} $s$nl")
			ensureFlushed()
		} catch {
			case _: Throwable =>
		}
		true
	}

	/** Writes line containing date, time, etc + <code>s + throwable.getClass.getSimpleName</code> and
	  * repeatedly getStackTrace(i) for i 1 until getStackTrace.length line to the log output.
	  *
	  * @return true
	  */
	@inline
	override def ERROR(s: String, throwable: Throwable): Boolean = {
		try {
			val st = throwable.getStackTrace
			fw.write(s"${getPreStuffs(name, 5000)} $s${throwable.getClass.getSimpleName}: ${throwable.getMessage}$nl")
			for(i <- 0 until st.length)
				fw.write(s"${getPreStuffs(name, 5000)} \t${st(i).toString}$nl")
			ensureFlushed()
		} catch {
			case _: Throwable =>
		}
		true
	}
}
