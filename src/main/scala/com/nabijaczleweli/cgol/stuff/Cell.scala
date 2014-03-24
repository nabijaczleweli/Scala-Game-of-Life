package com.nabijaczleweli.cgol.stuff


/** Cell enumness.
  *
  * @author Jędrzej
  * @since  23.03.14
  */
protected object Cell extends Enumeration {
	type Cell = Value
	val LIVE, DEAD = Value
	final val rand = new scala.util.Random()

	def getRandomized =
		apply(rand.nextInt(2))
}

/** Basic cell.
  *
  * @author Jędrzej
  * @since  24.03.14
  */
class Cell {
	var state = Cell.getRandomized

	/** Returns Cell.toString. */
	override def toString =
		state.toString
}