package worldTests

import org.junit.Test
import com.scala_game_of_life.stuff._

/** For various tests involving counting stuff.
  *
  * Function names are pretty self-explanatory.
  *
  * @author Jędrzej
  * @since  30.03.14
  */
class Counts {
	@Test
	def `new World(0, 0).count == 0`() {
		val w = new World(0, 0)
		assert(w.count == 0, w.count.toString)
	}

	@Test
	def `new World(0, 0) add (Empty Chunk).count == 0`() {
		val w = new World(0, 0)
		w add new Chunk({
			val a = Array.ofDim[Cell](Chunk sizeX, Chunk sizeY)
			for(x <- a)
				for(i <- 0 until x.length) {
					x(i) = new Cell
					x(i).state = Cell.LIVE
				}
			a
		}, 0, 0)
		assert(w.count == 0, w.count.toString)
	}
}
