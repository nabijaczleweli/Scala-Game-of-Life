package utilTests

import org.junit.Test
import xyz.nabijaczleweli.scala_game_of_life.util.NumberUtil

/** @author Jędrzej
  * @since  26.04.14
  */
class NumberUtilDecTest {
	@Test
	def `9to1001`() {
		val t = NumberUtil long2bin 9
		assert(t == "1001", "Weird value: \'" + t + "\'.")
	}

	@Test
	def `-9to-1001`() {
		val t = NumberUtil long2bin -9
		assert(t == "-1001", "Weird value: \'" + t + "\'.")
	}

	@Test
	def `100to1100100`() {
		val t = NumberUtil long2bin 100
		assert(t == "1100100", "Weird value: \'" + t + "\'.")
	}

	@Test
	def `-100to1100100`() {
		val t = NumberUtil long2bin -100
		assert(t == "-1100100", "Weird value: \'" + t + "\'.")
	}

	@Test
	def `Long.MaxValueto111111111111111111111111111111111111111111111111111111111111111`() {
		val t = NumberUtil long2bin Long.MaxValue
		assert(t == "111111111111111111111111111111111111111111111111111111111111111", "Weird value: \'" + t + "\'.")
	}

	@Test
	def `Long.MinValueto-111111111111111111111111111111111111111111111111111111111111111`() {
		val t = NumberUtil long2bin Long.MinValue
		assert(t == "-111111111111111111111111111111111111111111111111111111111111111", "Weird value: \'" + t + "\'.")
	}
}
