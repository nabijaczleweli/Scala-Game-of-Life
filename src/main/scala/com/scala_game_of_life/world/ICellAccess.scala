package com.scala_game_of_life.world

import com.scala_game_of_life.cell.Cell
import scala.swing.Color
import com.scala_game_of_life.engine.save.Saveable

/** Implemented by worlds and such.
  *
  * @author Jędrzej
  * @since  23.03.14
  */
trait ICellAccess extends Saveable with Iterable[Cell] {
	var rules: WorldRules

	def cells: TraversableOnce[Cell]

	override def isEmpty: Boolean =
		cells.isEmpty

	override def iterator =
		cells.toList.iterator

	def count: Long

	/** Shall be overriden to match the human-readable name. */
	override def toString(): String

	final override def saveDomain =
		"world"

	override def saveName =
		toString()

	def backgroundColor: Color

	def cellAtCoords(x: Long, y: Long, create: Boolean): Cell

	def cellAtCoords(x: Long, y: Long, cell: Cell): Unit

	def generateCellAtCoords(x: Long, y: Long, c: Cell): Unit

	def generateCellAtCoords(x: Long, y: Long): Unit

	def isCellAt(x: Long, y: Long): Boolean

	def isCellAt(x: Long, y: Long, cell: Cell): Boolean

	/* I am half-tempted to call it `cellMeth`... */
	def cellMeta(x: Long, y: Long): Int

	/* I am half-tempted to call it `cellMeth`... */
	def cellMeta(x: Long, y: Long, newMeta: Int): Unit

	def cellState(x: Long, y: Long): Boolean

	def cellState(x: Long, y: Long, newState: Boolean): Unit

	def areCellsAt(xbeg: Long, ybeg: Long, xend: Long, yend: Long): Boolean =
		(for(x <- xbeg to xend; y <- ybeg to yend) yield isCellAt(x, y)).forall(b => b)
}