package net.nactors.process

import net.nactors.Actor
import java.util.UUID
import com.scala_game_of_life.util.DataUtil
import net.lonning.Logger
import net.lonning.loutput.FileLOutput

/** Contains <tt>Actor</tt>s and <tt>ActorThread</tt>s.
  *
  * @author Jędrzej
  * @since  22.04.14
  */
class ActorsHome(private final val homeName: String) extends ThreadGroup(homeName) with Logger[FileLOutput] {
	private[nactors] var actors          = Vector[Actor]()
	private[nactors] var actorThreads    = Vector[ActorThread]()
	private[nactors] val loggingFileName = s"Actors from $homeName.log"

	//new File(loggingFileName).delete

	def kill(actorName: String) {
		try {
			var tmpact = Vector[Actor]()
			var idx = -1
			var tmp = 0
			for(a <- actors; if idx == -1) {
				if(a.name == actorName)
					idx = tmp
				else
					tmpact +:= a
				tmp += 1
			}
			actors = tmpact
			actorThreads(idx).interrupt()
			actorThreads = DataUtil.removeFromVector[ActorThread](actorThreads, idx)
			actorThreads foreach {_.updateidx()}
		} catch {
			case _: ArrayIndexOutOfBoundsException =>
		}
	}

	def killAll() {
		actorThreads foreach {_.interrupt()}
		var idx = 0
		for(t <- actorThreads) {
			val timeStart = System.currentTimeMillis
			while(t.isAlive && (System.currentTimeMillis - timeStart) < 2500) // Kill after 2.5 seconds.
				Thread sleep 10
			if(t.isAlive) {
				println(s"Force killing $t.")
				actorThreads = actorThreads.updated(idx, null)
			}
			idx += 1
		}
		actorThreads = Vector[ActorThread]()
		actors = Vector[Actor]()
	}

	def add(actor: Actor) = {
		actor.home = Some(this)
		actors :+= actor
		actorThreads :+= new ActorThread(actor.name, this, actorThreads.length - 1)
		get(actor.name)
	}

	def get(actorName: String) = {
		val idx = actorThreads.indexWhere(_.getName == actorName)
		if(idx == -1)
			throw new IllegalArgumentException(s"Couldn't find name: \'$actorName\'.")
		if(!actorThreads(idx).isAlive)
			actorThreads(idx).start()
		new ActorPublic(this, actorThreads(idx), actors(idx))
	}

	def forUUID(uuid: UUID): String =
		actors(actorThreads.indexWhere(uuid == _.uuid)).name

	override val logger = new FileLOutput(homeName, loggingFileName)(true)
	logger.ensureFlushed()
}
