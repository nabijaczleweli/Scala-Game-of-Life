package xyz.nabijaczleweli.scala_game_of_life.stuff

/** Cell enumness.
  *
  * @author Jędrzej
  * @since  23.03.14
  */
object Cell extends Enumeration {
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

	def this(b: Boolean) = {
		this
		if(b)
			state = Cell.LIVE
		else
			state = Cell.DEAD
	}

	/** Returns Cell.toString. */
	override def toString =
		state.toString

	def bool =
		state == Cell.LIVE

	def int =
		bool.asInstanceOf[Int]
}
