package chunkTests

import org.junit.Test
import com.scala_game_of_life.stuff.{Cell, Chunk}

/** For various tests involving vounting stuffs.
  *
  * @author Jędrzej
  * @since  03.04.14
  */
class Counts {
	lazy val boolCountFunc = (ce: Cell) => ce.bool

	@Test
	def `new Chunk(clean array of array of Cell, 0, 0).count == 0`() {
		val cellarrarr = Array.ofDim[Cell](Chunk.sizeX, Chunk.sizeY)
		for(i <- 0 until Chunk.sizeX)
			for(j <- 0 until Chunk.sizeY)
				cellarrarr(i)(j) = new Cell(false)
		val c = new Chunk(cellarrarr, 0, 0)
		assert(c.count(boolCountFunc) == 0, "Size exceeded.")
	}

	@Test
	def `new Chunk(clean array of Cell, 0, 0).count == 0`() {
		val cellarr = Array.ofDim[Cell](Chunk.sizeX * Chunk.sizeY)
		for(i <- 0 until (Chunk.sizeX * Chunk.sizeY))
			cellarr(i) = new Cell(false)
		val c = new Chunk(cellarr, 0, 0)
		assert(c.count(boolCountFunc) == 0, "Size exceeded.")
	}

	@Test
	def `new Chunk(0, 0).count + new Chunk(0, 1).count != 0`() {
		val c0 = new Chunk(0, 0)
		val c1 = new Chunk(0, 0)
		assert((c0.count(boolCountFunc) + c1.count(boolCountFunc)) == 0, "Size too small.")
	}
}
