package chunkTests

import org.junit.Test
import xyz.nabijaczleweli.scala_game_of_life.world.Chunk
import scala.annotation.tailrec
import common.ThingsForTests.testCellID

/** For various tests involving counting stuffs.
  *
  * @author Jędrzej
  * @since  03.04.14
  */
class Counts {
	@Test
	def `new Chunk(clean array of array of Cell).count == 0`() {
		val c = new Chunk({
			                  val t = Array.ofDim[(Short, Int)](Chunk.sizeX, Chunk.sizeY)
			                  for(i <- 0 until Chunk.sizeX; j <- 0 until Chunk.sizeY)
				                  t(i)(j) = Tuple2(testCellID, 0x00)
			                  t
		                  }, 0, 0)
		assert(c.count == 0, "Size exceeded: " + c.count + ".")
	}

	@Test
	def `new Chunk(clean array of Cell).count != 0`() {
		val c = new Chunk({
			                  val t = Array.ofDim[(Short, Int)](Chunk.sizeX, Chunk.sizeY)
			                  for(i <- 0 until Chunk.sizeX; j <- 0 until Chunk.sizeY)
				                  t(i)(j) = Tuple2(testCellID, 0x00)
			                  t
		                  }, 0, 0)
		assert(c.count == 0, "Size exceeded: " + c.count + ".")
	}

	@Test
	def `new Chunk(full array of Cell).count == (Chunk.sizeX * Chunk.sizeY)`() {
		val c = new Chunk({
			                  val t = Array.ofDim[(Short, Int)](Chunk.sizeX, Chunk.sizeY)
			                  for(i <- 0 until Chunk.sizeX; j <- 0 until Chunk.sizeY)
				                  t(i)(j) = Tuple2(testCellID, 0x1f)
			                  t
		                  }, 0, 0)
		val t = c.count
		assert(t == (Chunk.sizeX * Chunk.sizeY), "Size too small: " + t.toString + '.')
	}

	@Test
	def `new Chunk(full array of array of Cell).count == (Chunk.sizeX * Chunk.sizeY)`() {
		val c = new Chunk({
			                  val t = Array.ofDim[(Short, Int)](Chunk.sizeX, Chunk.sizeY)
			                  for(i <- 0 until Chunk.sizeX; j <- 0 until Chunk.sizeY)
				                  t(i)(j) = Tuple2(testCellID, 0x1f)
			                  t
		                  }, 0, 0)
		val t = c.count
		assert(t == (Chunk.sizeX * Chunk.sizeY), "Size too small: " + t.toString + '.')
	}

	@Test
	def `new Chunk().count + new Chunk().count > 0`() {
		val c0 = new Chunk(0, 0)
		val c1 = new Chunk(0, 1)
		assert((c0.count + c1.count) > 0, "Size too small: " + (c0.count + c1.count).toString + ".")
	}

	/** MIGHT be long since recursively calls itself well over too many times.
	  *
	  * This test fails? Check if [[Chunk.sizeX]] or [[Chunk.sizeY]] were changed.
	  * If so - ask nabijaczleweli to make 'comprehensive chunk average live cells count' again.
	  *
	  * Is 3 minutes enough?
	  */
	@Test(timeout = 180000L)
	def `lots of chunks.count is proper`() {
		`lots of chunks.count > is proper_proper_test`(0)
	}

	@tailrec
	final def `lots of chunks.count > is proper_proper_test`(tsf: Int) {
		if(tsf < 1000) {
			var cl = Vector.empty[Chunk]
			for(i <- 0 to tsf)
				cl :+= new Chunk(0, 0)
			val c = {
				var t = 0L
				cl foreach {t += _.count}
				t
			}
			assert(c > 0, "Size too small: " + c.toString + ".")
			assert(c < (Chunk.sizeX * Chunk.sizeY * (tsf + 1)), "Size too big: " + c.toString + ".")
			assert(390 < c / (tsf + 1), "Avg too small: " + (c / (tsf + 1)).toString + ".")
			assert(450 > c / (tsf + 1), "Avg too big: " + (c / (tsf + 1)).toString + ".")
			`lots of chunks.count > is proper_proper_test`(tsf + 1)
		}
	}
}
