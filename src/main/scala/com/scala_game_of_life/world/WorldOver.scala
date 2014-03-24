package com.scala_game_of_life.world

import java.awt.Color
import com.scala_game_of_life.engine.registries.GameRegistry
import java.lang.reflect.Field
import scala.collection.mutable

/** Overworld, as in Minecraft.
  *
  * @author Jędrzej
  * @since  11.04.14
  */
@SerialVersionUID(233223133)
class WorldOver private(`_ _ _ _ _ _ _ _ _`: () => Unit) extends World(() => {}) {
	WorldOver

	def this(from: List[Chunk]) {
		this(() => {})
		if(from != null)
			_chunks ++= from
		val f = classOf[World].getDeclaredField("__gcifccache")
		f setAccessible true
		f.set(this, new mutable.HashMap[(Long, Long), Int])
	}

	def this() =
		this(List[Chunk]())

	override def backgroundColor =
		new Color(0, 0, 255, 64)

	override def toString() =
		"Overworld"
}

private object WorldOver {
	GameRegistry.registerWorld(classOf[WorldOver], (_howmany: Int) => {
		import com.scala_game_of_life.engine.GameEngine._
		val _chunks = {
			var t: Field = null
			var tworld = world.getClass
			while(t == null)
				try {
					t = tworld.getDeclaredField("_chunks")
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