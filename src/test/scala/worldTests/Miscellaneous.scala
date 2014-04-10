package worldTests

import org.junit.Test
import com.scala_game_of_life.stuff.{Chunk, World}
import java.lang.reflect.Field

/** Miscellaneous tests not deserving their own class.
  *
  * @author Jędrzej
  * @since  29.03.14
  */
class Miscellaneous {
	/** MIGHT be long since uses reflection. */
	@Test
	def `World(0, 0) add two times to same place -- protection`() {
		val w = new World(0, 0)
		w add new Chunk(0, 0)
		w add new Chunk(0, 1)
		w add new Chunk(0, 0)
		var f: Field = null
		for(i <- w.getClass.getDeclaredFields; if f == null)
			if(i.getName.contains("_chunks"))
				f = i
		f.setAccessible(true)
		assert(f.get(w).asInstanceOf[Array[Chunk]].length == 2, f.get(w).asInstanceOf[Array[Chunk]].length.toString)
	}
}
