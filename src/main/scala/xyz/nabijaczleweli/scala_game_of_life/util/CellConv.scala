package xyz.nabijaczleweli.scala_game_of_life.util

import xyz.nabijaczleweli.scala_game_of_life.cell.Cell
import xyz.nabijaczleweli.scala_game_of_life.engine.registries.CellRegistry

import scala.language.implicitConversions

/** @author Jędrzej
  * @since  14.05.14
  */
object CellConv {
	implicit def ShortToCell(self: Short) =
		new AnyRef {
			def toCell =
				CellRegistry.get(self).get
		}

	implicit def IntToCell(self: Int) =
		new AnyRef {
			def toCell =
				CellRegistry.get(self.toShort).get
		}

	implicit def CellToShort(self: Cell) =
		new AnyRef {
			def id =
				CellRegistry.get(self).get
		}
}
