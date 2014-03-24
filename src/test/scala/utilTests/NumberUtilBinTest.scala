package utilTests

import org.junit.Test
import com.scala_game_of_life.util.NumberUtil

/** @author Jędrzej
  * @since  26.04.14
  */
class NumberUtilBinTest {
	@Test
	def `0to0`() {
		val t = NumberUtil.bin2long("0")
		assert(t == 0, "Weird value: \'" + t + "\'")
	}

	@Test
	def `1to1`() {
		val t = NumberUtil.bin2long("1")
		assert(t == 1, "Weird value: \'" + t + "\'")
	}

	@Test
	def `-1to-1`() {
		val t = NumberUtil.bin2long("-1")
		assert(t == -1, "Weird value: \'" + t + "\'")
	}

	@Test
	def `1001to9`() {
		val t = NumberUtil.bin2long("1001")
		assert(t == 9, "Weird value: \'" + t + "\'")
	}

	@Test
	def `-1001to-9`() {
		val t = NumberUtil.bin2long("-1001")
		assert(t == -9, "Weird value: \'" + t + "\'")
	}
}
