package utilTests

import org.junit.Test
import com.scala_game_of_life.util.DataUtil

/** @author Jędrzej
  * @since  07.05.14
  */
class DataUtilSeqTest {
	@Test
	def `{0, 1, 2, 3}.fold(1) == {{0, 1}, {2, 3}}`() {
		val arr = Array[Int](0, 1, 2, 3)
		val arrarr = DataUtil.fold[Int](arr, 1)
		assert(arrarr(0)(0) == arr(0))
		assert(arrarr(0)(1) == arr(1))
		assert(arrarr(1)(0) == arr(2))
		assert(arrarr(1)(1) == arr(3))
	}

	@Test
	def `{{0, 1}, {2, 3}}.unfold == {0, 1, 2, 3}`() {
		val arrarr = Array[Array[Int]](Array[Int](0, 1), Array[Int](2, 3))
		val arr = DataUtil.unfold[Int](arrarr)
		assert(arr(0) == arrarr(0)(0))
		assert(arr(1) == arrarr(0)(1))
		assert(arr(2) == arrarr(1)(0))
		assert(arr(3) == arrarr(1)(1))
	}
}
