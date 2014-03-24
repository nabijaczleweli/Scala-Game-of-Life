package net.nactors

import net.nactors.process.{ActorsHome, ActorThread, Actors}
import net.lonning.loutput.{LogOutput, ConsoleLOutput}

/** Extend for being able to act in a spectable.
  *
  * @author Jędrzej
  * @since  22.04.14
  */
abstract class Actor(final val name: String = s"Actor #${Actors.nextActorID}", private[nactors] final val notLog: List[Any] = Nil) extends LogOutput {
	private[nactors] var home   : Option[ActorsHome] = None
	private[nactors] var myIndex: Int                = -1

	/** Place your switchcase here
	  *
	  * Use the <tt>throw new UnsupportedOperationException</tt>
	  * if you don't supplied thing - it is properly handled.
	  */
	def receive: PartialFunction[Any, Unit]

	private[nactors] final def react(toWhat: Any, thr: ActorThread): Unit =
		try {
			receive(toWhat)
		} catch {
			case uoe: UnsupportedOperationException =>
				val ts = s"\'$name\' cannot handle \'$toWhat\', and hence thrown "
				new ConsoleLOutput(thr.uuid.toString).severe(ts, uoe)
				thr.severe(ts, uoe)
			case me: MatchError =>
				val ts = s"\'$name\' failed to match \'$toWhat\', and hence thrown "
				new ConsoleLOutput(thr.uuid.toString).severe(ts, me)
				thr.warn(ts, me)
		}

	@SafeVarargs
	final def !(home: ActorsHome, msgs: Any*): Unit =
		home.actorThreads.foreach(thr => if(thr.getName == name) msgs.foreach(thr.mailbox enqueue _))

	/** Writes line containing date, time, etc + <code>s</code> line to the log output.
	  *
	  * @return true, but false when elided
	  */
	override def log(s: String): Boolean = {
		if(home.isDefined)
			home.get.actorThreads(myIndex) log s"[Actor itself] $s"
		true
	}

	/** Writes line containing date, time, etc + <code>s</code> line to the severe output.
	  *
	  * @return true, but false when elided
	  */
	override def severe(s: String): Boolean = {
		if(home.isDefined)
			home.get.actorThreads(myIndex) severe s"[Actor itself] $s"
		true
	}

	/** Writes line containing date, time, etc + <code>s</code> line to the warning output.
	  *
	  * @return true, but false when elided
	  */
	override def warn(s: String): Boolean = {
		if(home.isDefined)
			home.get.actorThreads(myIndex) warn s"[Actor itself] $s"
		true
	}

	/** Writes line containing date, time, etc + <code>s</code> line to the ERROR output.
	  *
	  * @return true
	  */
	override def ERROR(s: String): Boolean = {
		if(home.isDefined)
			home.get.actorThreads(myIndex) ERROR s"[Actor itself] $s"
		true
	}

	/** Writes line containing date, time, etc + <code>s</code> line to the debug output.
	  *
	  * @return true, but false when elided
	  */
	override def debug(s: String): Boolean = {
		if(home.isDefined)
			home.get.actorThreads(myIndex) debug s"[Actor itself] $s"
		true
	}

	/** Writes line containing date, time, etc + <code>s</code> line to the info output.
	  *
	  * @return true, but false when elided
	  */
	override def info(s: String): Boolean = {
		if(home.isDefined)
			home.get.actorThreads(myIndex) info s"[Actor itself] $s"
		true
	}
}
