package utilTests

import org.junit.Test
import com.scala_game_of_life.util.NumberUtil

/** @author Jędrzej
  * @since  26.04.14
  */
class NumberUtilTestBinExceptions {
	@Test(expected = classOf[IllegalArgumentException])
	def invalidBinaryStringYieldsException0() {
		NumberUtil bin2long "a"
	}

	@Test(expected = classOf[IllegalArgumentException])
	def invalidBinaryStringYieldsException1() {
		NumberUtil bin2long ""
	}

	@Test(expected = classOf[IllegalArgumentException])
	def invalidBinaryStringYieldsException2() {
		NumberUtil bin2long "00102"
	}

	@Test(expected = classOf[IllegalArgumentException])
	def invalidBinaryStringYieldsException3() {
		NumberUtil bin2long "--101"
	}

	@Test(expected = classOf[IllegalArgumentException])
	def invalidBinaryStringYieldsException4() {
		NumberUtil bin2long "-101a"
	}

	@Test(expected = classOf[IllegalArgumentException])
	def invalidBinaryStringYieldsException5() {
		NumberUtil bin2long "101a"
	}
}
