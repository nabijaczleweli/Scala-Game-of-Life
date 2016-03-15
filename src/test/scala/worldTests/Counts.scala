package worldTests

import org.junit.Test
import com.scala_game_of_life.world.WorldOver
import common.ThingsForTests.testCellID
import xyz.nabijaczleweli.scala_game_of_life.world.{WorldOver, Chunk}

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
		val w = new WorldOver()
		assert(w.count == 0, w.count.toString)
	}

	@Test
	def `new World(0, 0) add (Empty Chunk).count == 0v1`() {
		val w = new WorldOver()
		w add new Chunk({
			                val a = Array.ofDim[(Short, Int)](Chunk.sizeX, Chunk.sizeY)
			                for(x <- a.indices; i <- a(0).indices)
				                a(x)(i) = Tuple2(testCellID, 0)
			                a
		                }, 0, 0)
		assert(w.count == 0, w.count.toString)
	}

	@Test
	def `new World(0, 0) add (Empty Chunk).count == 0v2`() {
		val w = new WorldOver()
		w add new Chunk({
			                val a = Array.ofDim[(Short, Int)](Chunk.sizeX, Chunk.sizeY)
			                for(x <- a.indices; i <- a(0).indices)
				                a(x)(i) = Tuple2(testCellID, 0)
			                a
		                }, 0, 0)
		assert(w.count == 0, w.count.toString)
	}
}
