package xyz.nabijaczleweli.scala_game_of_life.cell

import java.awt.Color
import xyz.nabijaczleweli.scala_game_of_life.engine.GameRenderer
import xyz.nabijaczleweli.scala_game_of_life.engine.save.Saveable
import xyz.nabijaczleweli.scala_game_of_life.entity.Entity
import xyz.nabijaczleweli.scala_game_of_life.util.MathUtil

/** Basic cell.
  *
  * @author Jędrzej
  * @since  24.03.14
  */
@SerialVersionUID(133312132133L)
abstract class Cell(val material: Material) extends Saveable {
	/** Called when random tick happens.
	  *
	  * world: use one from GameEngine$
	  *
	  * @see [[com.scala_game_of_life.engine.GameEngine]]
	  */
	def onUpdate(x: Long, y: Long): Unit = Unit

	/** Called whenever any CellAction happens.
	  *
	  * world: use one from GameEngine$
	  *
	  * @see [[com.scala_game_of_life.engine.GameEngine]]
	  */
	def onCellAction(x: Long, y: Long, action: CellAction.CellAction): Unit = Unit

	/** Called whenever any neighbouring cell happens to change.
	  *
	  * world: use one from GameEngine$
	  *
	  * @see [[com.scala_game_of_life.engine.GameEngine]]
	  */
	def onNeighbourCellChange(x: Long, y: Long, changedCell: (Cell, Long, Long), action: CellAction.CellAction): Unit = Unit

	def onEntityWalking(entity: Entity): Unit = Unit

	final override def saveDomain =
		null

	final override def saveName =
		null

	import GameRenderer._

	protected final val cellWidthDiv2  = MathUtil.ceil(cellWidth, 2)
	protected final val cellHeightDiv2 = MathUtil.ceil(cellHeight, 2)

	/** graph: draw on one in GameRenderer$
	  * world: use one from GameEngine$
	  *
	  * @see [[GameRenderer]]
	  * @see [[com.scala_game_of_life.engine.GameEngine]]
	  */
	def draw(onScreenX: Int, onScreenY: Int, worldY: Long, worldX: Long, world: ICellAccess) {
		graph setColor Color.magenta
		graph.fillRect(onScreenX, onScreenY, cellWidthDiv2, cellHeightDiv2)
		graph.fillRect(onScreenX + cellWidthDiv2, onScreenY + cellHeightDiv2, cellWidthDiv2, cellHeightDiv2)
		graph setColor Color.black
		graph.fillRect(onScreenX + cellWidthDiv2, onScreenY, cellWidthDiv2, cellHeightDiv2)
		graph.fillRect(onScreenX, onScreenY + cellHeightDiv2, cellWidthDiv2, cellHeightDiv2)
	}
}

object CellAction extends Enumeration {
	final type CellAction = Value
	final val RESURRECTED, KILLED, STAYED = Value
}

object Cell {
	final def countFunc(world: ICellAccess): (((Cell, Long, Long)) => Boolean) =
		par => world.cellState(par._2, par._3)
}
