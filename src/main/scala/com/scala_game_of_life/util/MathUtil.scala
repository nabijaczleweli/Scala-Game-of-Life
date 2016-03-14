package com.scala_game_of_life.util

/** NOTE: if anyone notices anything that
  * has a method here written by hand - redirect
  * it to this object.
  *
  * @author Jędrzej
  * @since  21.04.14
  */
object MathUtil {
	/** Ceils. Works on mathematical integers. */
	@inline
	def ceil(leftside: Int, rightside: Int): Int =
		(leftside + (leftside % rightside)) / rightside

	/** Ceils. Works on mathematical integers. */
	@inline
	def ceil(leftside: Long, rightside: Long): Long =
		(leftside + (leftside % rightside)) / rightside

	/** Ceils. Works on mathematical integers. */
	@inline
	def ceil(leftside: BigInt, rightside: BigInt): BigInt =
		(leftside + (leftside % rightside)) / rightside

	/** Floors. Works on mathematical integers. */
	@inline
	def floor(leftside: Int, rightside: Int): Int =
		(leftside - (leftside % rightside)) / rightside

	/** Floors. Works on mathematical integers. */
	@inline
	def floor(leftside: Long, rightside: Long): Long =
		(leftside - (leftside % rightside)) / rightside

	/** Floors. Works on mathematical integers. */
	@inline
	def floor(leftside: BigInt, rightside: BigInt): BigInt =
		(leftside - (leftside % rightside)) / rightside

	@inline
	def diameter_d(side0: Double, side1: Double) =
		Math.sqrt(Math.pow(side0, 2) + Math.pow(side1, 2))

	@inline
	def diameter_i(side0: Int, side1: Int) =
		Math.sqrt(Math.pow(side0, 2) + Math.pow(side1, 2)).toInt
}
