package xyz.nabijaczleweli.scala_game_of_life.util

/** @author Jędrzej
  * @since  26.04.14
  */
object NumberUtil {
	private final val bin2longRegex = "^-?[01]+$".r
	private final val setbitRegex   = "^-?0*1?0*$".r

	/** @return Decimal value represened by bits in binstr. */
	def bin2long(binstr: String): Long = {
		if(binstr == null)
			throw new NullPointerException("null cannot be interpreted as a binary value!")
		if(bin2longRegex.findAllIn(binstr).isEmpty)
			throw new IllegalArgumentException(s"\'$binstr\' cannot be interpreted as a binary value!")
		val negative = binstr(0) == '-'
		val from = binstr.substring(if(negative) 1 else 0).reverse
		var value: Long = 0 // Enforced use of 64-bit mathematical integer.
		for(idx <- 0 until from.length)
			if(from(idx) == '1')
				value += (1 << idx.toLong)
		if(negative)
			-value
		else
			value
	}

	/** @return String representing binary value of decstr. */
	def long2bin(declong: Long): String = {
		if(declong == Long.MinValue)
			return "-111111111111111111111111111111111111111111111111111111111111111"
		else if(declong == 0)
			return "0"
		val negative = declong < 0
		var from = Math.abs(declong)
		var result = ""
		while(from != 0) {
			result += from % 2
			from >>>= 1
		}
		if(negative)
			s"$result-".reverse
		else
			result.reverse
	}

	/** @return number of first set most significant bit in binstr. */
	def setbit(binstr: String): Int = {
		if(binstr == null)
			throw new NullPointerException("null cannot have its bits searched!")
		if(!binstr.contains('1'))
			return 0
		if(setbitRegex.findAllIn(binstr).isEmpty)
			throw new IllegalArgumentException(s"\'$binstr\' cannot have its bits searched!")
		val from = binstr.reverse
		var curpos = 0
		for(c <- from) {
			if(c == '1')
				return curpos
			curpos += 1
		}
		-1
	}

	/** @return number of first set most significant bit in declong. */
	def setbit(declong: Long): Int =
		setbit(long2bin(declong))
}
