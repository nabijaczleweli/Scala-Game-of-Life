package xyz.nabijaczleweli.nactors.process

import xyz.nabijaczleweli.nactors.Actor

/** Public thing returned by the [[ActorsHome]].
  *
  * @author Jędrzej
  * @since  22.04.14
  */
class ActorPublic(private final val home: ActorsHome, private final val actorT: ActorThread, private final val actor: Actor) {
	@SafeVarargs
	final def !(what: Any*) {
		val toLog = what filterNot {actor.notLog.contains}
		if(!toLog.isEmpty)
			actorT.debug(s"I got ${if(toLog.size == 1) "a thing" else s"${toLog.size} things"}, ${if(toLog.size == 1) "it" else "them"} being: ")
		toLog.foreach(one => actorT.debug(s"\t$one"))
		what.foreach(actorT.mailbox enqueue _)
		home synchronized {
			home.notifyAll()
		}
	}
}
