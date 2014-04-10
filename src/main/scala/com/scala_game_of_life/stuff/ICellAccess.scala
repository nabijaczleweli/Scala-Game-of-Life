package com.scala_game_of_life.stuff

import java.io.{ObjectStreamException, IOException}

/** Implemented by worlds and such.
  *
  * So Minecraftish
  *
  * @author Jędrek
  * @since  23.03.14
  */
trait ICellAccess extends Cloneable with Iterable[Cell] with Serializable {
	def height: Int

	def width: Int

	def height(n: Int): Unit

	def width(n: Int): Unit

	def cells: List[Cell]

	override def isEmpty: Boolean

	def count: Long

	/** Coordinates (x, y) describes top-left corner. */
	def add(from: Array[Cell], x: Long, y: Long): Unit

	/** Should be overriden to match the human-readable name. */
	override def toString(): String

	@throws[IOException]
	protected def writeObject(out: java.io.ObjectOutputStream): Unit

	@throws[IOException]
	@throws[ClassNotFoundException]
	protected def readObject(in: java.io.ObjectInputStream): Unit

	@throws[ObjectStreamException]
	protected def readObjectNoData(): Unit
}