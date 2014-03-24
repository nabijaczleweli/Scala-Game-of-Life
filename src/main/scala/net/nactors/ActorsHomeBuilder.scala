package net.nactors

import net.nactors.process.{ActorThread, ActorsHome}

/** Used for building homes.
  *
  * @author Jędrzej
  * @since  22.04.14
  */
object ActorsHomeBuilder {
	@SafeVarargs
	def apply(name: String, actrs: Actor*): ActorsHome = {
		val ah = new ActorsHome(name)
		var idx = 0
		ah.actors = (for(a <- actrs if a != null) yield {
			a.home = Some(ah)
			a.myIndex = idx
			idx += 1
			a
		}).toVector
		idx = 0
		ah.actorThreads = for(a <- ah.actors) yield {
			val t = new ActorThread(a.name, ah, idx)
			idx += 1
			t
		}
		ah
	}
}
