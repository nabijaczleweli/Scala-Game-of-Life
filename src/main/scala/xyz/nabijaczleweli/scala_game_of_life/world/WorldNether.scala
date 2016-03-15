package xyz.nabijaczleweli.scala_game_of_life.world

import java.awt.Color
import java.lang.reflect.Field

import xyz.nabijaczleweli.scala_game_of_life.engine.registries.GameRegistry
import xyz.nabijaczleweli.scala_game_of_life.engine.GameEngine.{world, rand}

import scala.collection.mutable.{HashMap => mHashMap}

/** @author Jędrzej
  * @since  15.05.14
  */
@SerialVersionUID(233223133)
class WorldNether private(`_ _ _ _ _ _ _ _ _`: () => Unit) extends World(() => {}) {
	WorldNether

	def this(from: List[Chunk]) {
		this(() => {})
		if(from != null)
			_chunks ++= from
		val f = classOf[World].getDeclaredField("__gcifccache")
		f setAccessible true
		f.set(this, new mHashMap[(Long, Long), Int])
	}

	def this() =
		this(List[Chunk]())

	override def backgroundColor =
		Color.red

	override def toString() =
		"The Nether"
}

private object WorldNether {
	GameRegistry.registerWorld(classOf[WorldNether], (_howmany: Int) => {
		val _chunks = {
			var t: Field = null
			var tworld = world.getClass
			while(t == null)
				try {
					t = tworld getDeclaredField "_chunks"
				} catch {
					case _: Throwable =>
						tworld = tworld.getSuperclass.asInstanceOf[Class[_ <: ICellAccess with IEntityAccess]]
				}
			t.setAccessible(true)
			t get world
		}.asInstanceOf[Vector[Chunk]]
		if((_chunks.length * Chunk.sizeX * Chunk.sizeY) >= _howmany) {
			var tlist = List[(Long, Long)]()
			for(i <- 0 until _howmany) {
				var chunk = _chunks(rand nextInt _chunks.length)
				var coords = ((chunk.pos._1 * Chunk.sizeX) + rand.nextInt(Chunk.sizeX), (chunk.pos._2 * Chunk.sizeY) + rand.nextInt(Chunk.sizeY))
				while(tlist contains coords) {
					coords = ((chunk.pos._1 * Chunk.sizeX) + rand.nextInt(Chunk.sizeX), (chunk.pos._2 * Chunk.sizeY) + rand.nextInt(Chunk.sizeY))
					if(rand.nextInt % 20 == 0)
						chunk = _chunks(rand nextInt _chunks.length)
				}
				tlist ::= coords
			}
			tlist
		} else
			for(c <- _chunks;
			    x <- (c.pos._1 * Chunk.sizeX) until ((c.pos._1 * Chunk.sizeX) + Chunk.sizeX);
			    y <- (c.pos._2 * Chunk.sizeY) until ((c.pos._2 * Chunk.sizeY) + Chunk.sizeY)) yield (x, y)
	})
}
