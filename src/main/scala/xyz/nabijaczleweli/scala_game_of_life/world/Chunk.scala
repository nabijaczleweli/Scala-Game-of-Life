package xyz.nabijaczleweli.scala_game_of_life.world

import java.awt.Color

import xyz.nabijaczleweli.scala_game_of_life.cell.Cell
import xyz.nabijaczleweli.scala_game_of_life.engine.GameEngine
import xyz.nabijaczleweli.scala_game_of_life.engine.registries.CellRegistry
import xyz.nabijaczleweli.scala_game_of_life.engine.save.Saveable
import xyz.nabijaczleweli.scala_game_of_life.util.CellConv._
import xyz.nabijaczleweli.scala_game_of_life.util.DataUtil

import scala.language.reflectiveCalls

/** Container for common info about chunks.
  *
  * @author Jędrzej
  * @since  24.03.14
  */
object Chunk {
	/* because prime and quite efficient. */
	final val sizeY, sizeX = 29
}

/** Used only in World as of the time being.
  *
  * @author Jędrzej
  * @since  24.03.14
  */
@SerialVersionUID(211112111212111L)
class Chunk(private val __x: Long, private val __y: Long) extends Saveable with ICellAccess with Comparable[Chunk] {
	/** Access: _cells(ecks * Chunk.sizeX + why). f.e: for x=1, y=2 _cells(1 * Chunk.sizeX  + 2)
	  *
	  * Short : Cell's id
	  * Int   : Cell's metadata
	  */
	private val _cells = new Array[(Short, Int)](Chunk.sizeX * Chunk.sizeY)
	for(i <- 0 until Chunk.sizeX; j <- 0 until Chunk.sizeY)
		_cells(i * Chunk.sizeX + j) = CellRegistry.get -> 0x10 * GameEngine.rand.nextInt(2)
	/** X and Y position of the Chunk NOT in Cells' XY coord system. */
	private val _xypos: (Long, Long) = (__x, __y)

	/** Constructs Chunk from a given Array of Array of Cell.
	  * Everything beyond the Chunk.sizeX and Chunk.sizeY is cut.
	  *
	  * @throws ArrayIndexOutOfBoundsException if `from` is too small.
	  */
	def this(from: Array[Array[(Short, Int)]], x: Long, y: Long) = {
		this(x, y)
		System.arraycopy(DataUtil.unfold[(Short, Int)](from).toArray, 0, _cells, 0, Chunk.sizeX * Chunk.sizeY)
	}

	/** Constructs Chunk from a given Array of Cell.
	  * Everything beyond the Chunk.sizeX and Chunk.sizeY is cut.
	  *
	  * @throws ArrayIndexOutOfBoundsException if `from` is too small.
	  */
	def this(from: Seq[(Short, Int)], x: Long, y: Long) =
		this(DataUtil.fold[(Short, Int)](from, Chunk.sizeX), x, y)

	override def compareTo(second: Chunk): Int =
		((_xypos._1 - second._xypos._1) + (_xypos._2 - second._xypos._2)).asInstanceOf[Int]

	/** Returns Tuple2(ecks, why) */
	def pos =
		_xypos

	override def cells =
		for(j <- 0 to Chunk.sizeX; c <- 0 to Chunk.sizeY) yield _cells(j * Chunk.sizeX + c)._1.toCell

	/** Please use this function instead of interhited one! */
	override def count = {
		var c = 0
		for(x <- 0 until Chunk.sizeX; y <- 0 until Chunk.sizeY) {
			if(cellState(x, y))
				c += 1
		}
		c
	}

	def getCellFromCoords(ecks: Long, why: Long): Cell =
		_cells((Math.abs(ecks.toInt) % Chunk.sizeX) * Chunk.sizeX + (Math.abs(why.toInt) % Chunk.sizeY))._1.toCell

	def cellAtCoords(ecks: Long, why: Long, cell: Cell) = {
		val x = Math.abs(ecks.toInt) % Chunk.sizeX
		val y = Math.abs(why.toInt) % Chunk.sizeY
		_cells(x * Chunk.sizeX + y) = cell.id -> _cells(x * Chunk.sizeX + y)._2
	}

	override def saveName =
		null

	def cellMetaPack(x: Long, y: Long): Int =
		_cells((Math.abs(x.toInt) % Chunk.sizeX) * Chunk.sizeX + (Math.abs(y.toInt) % Chunk.sizeY))._2 & 0x1f

	def cellMetaPack(ecks: Long, why: Long, newMetaPack: Int) {
		val x = Math.abs(ecks.toInt) % Chunk.sizeX
		val y = Math.abs(why.toInt) % Chunk.sizeY
		_cells(x * Chunk.sizeX + y) = _cells(x * Chunk.sizeX + y)._1 -> (newMetaPack & 0x1f)
	}

	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	override var rules = new WorldRules("/")

	override def cellMeta(x: Long, y: Long) =
		cellMetaPack(x, y) & 0xf

	override def cellMeta(x: Long, y: Long, newMeta: Int) =
		cellMetaPack(x, y, newMeta | (cellMetaPack(x, y) & 0x10))

	override def cellAtCoords(x: Long, y: Long, create: Boolean) =
		getCellFromCoords(x, y)

	override def isCellAt(x: Long, y: Long): Boolean =
		true

	override def isCellAt(x: Long, y: Long, cell: Cell): Boolean =
		true

	override def generateCellAtCoords(x: Long, y: Long, c: Cell) {}

	override def generateCellAtCoords(x: Long, y: Long) {}

	override def cellState(x: Long, y: Long) =
		(cellMetaPack(x, y) & 0x10) != 0

	override def cellState(x: Long, y: Long, newState: Boolean) =
		cellMetaPack(x, y, (if(newState) 0x10 else 0x00) | (cellMetaPack(x, y) & 0xf))

	override def backgroundColor =
		Color.black
}
