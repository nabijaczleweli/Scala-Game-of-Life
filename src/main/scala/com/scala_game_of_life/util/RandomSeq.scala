package com.scala_game_of_life.util

import scala.util.Random
import scala.collection.mutable

/** @author Jędrzej
  * @since  06.05.14
  */
class RandomSeq[stored] extends Seq[stored] {self =>
	private final val rand      = new Random
	private final var storedSeq = Vector[stored]()

	override def length: Int =
		storedSeq.length

	@deprecated("You cannot request an INDEXED element from a RANDOM sequence!", "0.9.8")
	override def apply(idx: Int): stored =
		apply()

	override def iterator: Iterator[stored] =
		new Iterator[stored] {
			private[RandomSeq] val theThings = {
				val q = mutable.Queue[stored]()
				storedSeq foreach {q enqueue _}
				q
			}

			override def hasNext: Boolean =
				!theThings.isEmpty

			override def next(): stored =
				theThings.dequeue()
		}

	def apply(): stored =
		storedSeq(rand nextInt storedSeq.length)

	def +=(obj: stored) =
		storedSeq +:= obj

	override def seq: Seq[stored] =
		iterator.toList
}

object RandomSeq {
	def apply[stored](elems: stored*): RandomSeq[stored] = {
		var rs = new RandomSeq[stored]
		elems foreach {rs += _}
		rs
	}
}