package utilTests

import org.junit.Test
import scala.annotation.tailrec
import com.scala_game_of_life.util.NumberUtil._
import scala.util.Random

/** @author Jędrzej
  * @since  26.04.14
  */
class NumberUtilBinDecTest {
	val rand = new Random

	@Test
	def recursive() {
		rec(0, 10000, 1)
		0 to 1000 foreach {
			cur =>
				val s = rand.nextLong()
				val ka = s + rand.nextInt(10000)
				rec(s, ka, rand.nextInt(cur + 1) + 1)
		}
	}

	@tailrec
	private def rec(what: Long, killAt: Long, step: Long) {
		if(what < killAt) {
			assert(bin2long(long2bin(what)) == what)
			rec(what + step, killAt, step)
		}
	}
}
