package net.nactors.process

import java.util.UUID
import scala.collection.mutable
import net.lonning.Logger
import net.lonning.loutput.FileLOutput

/** @author Jędrzej
  * @since  22.04.14
  */
class ActorThread(private final val name: String, private final val home: ActorsHome, private[process] final val myIndex: Int) extends Thread(home, name) with Logger[FileLOutput] {
	private[nactors]       var mailbox = mutable.Queue[Any]()
	private[nactors] final val uuid    = UUID.randomUUID
	override               val logger  = new FileLOutput(s"Daemon $uuid", home.loggingFileName)(false)

	override def run() {
		try {
			while(!isInterrupted) {
				home synchronized {
					home.wait()
				}
				while(!mailbox.isEmpty)
					home.actors(myIndex).react(mailbox.dequeue(), this)
			}
		} catch {
			case _: InterruptedException =>
		}
		if(log(s"$name is done."))
			logger.close()
	}

	override def start() {
		log(s"$name got started.")
		super.start()
	}

	override def interrupt() {
		log(s"$name got interrupted.")
		super.interrupt()
	}

	log(s"$name got instantiated.")

	private[process] def updateidx() {
		val f = getClass.getDeclaredField("myIndex")
		f setAccessible true
		f.set(this, home.actorThreads.indexWhere(uuid == _.uuid))
	}
}
