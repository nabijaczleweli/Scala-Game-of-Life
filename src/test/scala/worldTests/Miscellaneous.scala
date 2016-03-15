package worldTests

import org.junit.Test
import java.lang.reflect.Field
import com.scala_game_of_life.world.WorldOver
import xyz.nabijaczleweli.scala_game_of_life.world.{WorldOver, Chunk}

/** Miscellaneous tests not deserving their own class.
  *
  * @author Jędrzej
  * @since  29.03.14
  */
class Miscellaneous {
	@Test
	def `World(0, 0) add two times to same place -- protection`() {
		val w = new WorldOver()
		w add new Chunk(0, 0)
		w add new Chunk(0, 1)
		w add new Chunk(0, 0)
		var f: Field = null
		for(i <- w.getClass.getSuperclass.getDeclaredFields; if f == null && i.getName.contains("_chunks"))
			f = i
		f.setAccessible(true)
		val _chunks = f.get(w).asInstanceOf[Vector[Chunk]]
		assert(_chunks.length == 2, _chunks.length.toString)
	}
}
