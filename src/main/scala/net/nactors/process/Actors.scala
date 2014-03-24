package net.nactors.process

/** Ford distributing informations all over the place.
  *
  * @author Jędrzej
  * @since  22.04.14
  */
private[nactors] object Actors {
	private var currentActorID = 0

	def nextActorID = {
		currentActorID += 1
		currentActorID - 1
	}
}
