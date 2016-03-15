package worldTests

import org.junit.Test
import xyz.nabijaczleweli.scala_game_of_life.stuff.{Chunk, World}

/** Test of creation of the World.
  *
  * @author Jędrzej
  * @since  26.03.14
  */
class ConstructionSize {
	@Test
	def `new World(1, 1).height == Chunk.sizeY/.width == Chunk.sizeX`() {
		val w = new World(1, 1)
		assert(w.height == Chunk.sizeY, w.height.toString)
		assert(w.width == Chunk.sizeX, w.width.toString)
	}

	@Test
	def `new World(9, 9).height == (Chunk.sizeY * 2)/.width == (Chunk.sizeX * 2)`() {
		val w = new World(9, 9)
		assert(w.height == (Chunk.sizeY * 2), w.height.toString)
		assert(w.width == (Chunk.sizeX * 2), w.width.toString)
	}

	@Test
	def `new World(1, 9).height == (Chunk.sizeY * 2)/.width == Chunk.sizeX`() {
		val w = new World(1, 9)
		assert(w.height == (Chunk.sizeY * 2), w.height.toString)
		assert(w.width == Chunk.sizeX, w.width.toString)
	}

	@Test
	def `new World(9, 1).height == Chunk.sizeY/.width == (Chunk.sizeX * 2)`() {
		val w = new World(9, 1)
		assert(w.height == Chunk.sizeY, w.height.toString)
		assert(w.width == (Chunk.sizeX * 2), w.width.toString)
	}

	@Test
	def `new World(8, 8).height == Chunk.sizeY/.width == Chunk.sizeX`() {
		val w = new World(8, 8)
		assert(w.height == Chunk.sizeY, w.height.toString)
		assert(w.width == Chunk.sizeX, w.width.toString)
	}

	@Test
	def `new World(0, 0).height == 0/.width == 0`() {
		val w = new World(0, 0)
		assert(w.height == 0, w.height.toString)
		assert(w.width == 0, w.width.toString)
	}
}
