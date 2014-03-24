package com.scala_game_of_life.world

import com.scala_game_of_life.cell.Cell
import com.scala_game_of_life.util.MathUtil
import com.scala_game_of_life.engine.GameEngine
import com.scala_game_of_life.util.CellConv._
import scala.ref.WeakReference
import scala.collection.mutable
import scala.language.reflectiveCalls

/** Basic world, nothing too fancy.
  *
  * Has to be a bit hackish since ICellAccess uses ONLY cells, not chunks, but world stores chunks.
  *
  * @author Jędrzej
  * @since  23.03.14
  */
abstract class World protected(`_ _ _ _ _ _ _ _ _`: () => Unit) extends ICellAccess with IEntityAccess {
	init(new WeakReference[ICellAccess](this))
	protected       var _chunks       = Vector[Chunk]()
	final protected val airCell: Cell = 0.toCell
	override        var rules         = new WorldRules

	def this(from: TraversableOnce[Chunk]) {
		this(() => {})
		if(from != null)
			_chunks ++= from
	}

	def this() =
		this(List[Chunk]())

	override def cells: Iterable[Cell] =
		_chunks.isEmpty match {
			case true =>
				List.empty[Cell]
			case false =>
				var t = Stream[Cell]()
				for(i <- _chunks)
					t ++:= i.cells
				t
		}

	def add(n: Chunk) {
		val gcifc = getChunkIndexFromCoords(n.pos._1, n.pos._2)
		if(gcifc == -1)
			_chunks :+= n
		else
			_chunks = _chunks.updated(gcifc, n)
		__gcifccache = new mutable.HashMap[(Long, Long), Int]
	}

	override def iterator =
		cells.iterator

	@transient
	private var __gcifccache = new mutable.HashMap[(Long, Long), Int]

	protected def getChunkIndexFromCoords(_x: Long, _y: Long): Int = {
		if(_chunks.isEmpty)
			-1
		else {
			if(__gcifccache == null)
				__gcifccache = new mutable.HashMap[(Long, Long), Int]
			val xy = (MathUtil.floor(_x, Chunk.sizeX), MathUtil.floor(_y, Chunk.sizeY))
			val got = __gcifccache get xy
			if(got.isDefined)
				got.get
			else {
				val t = _chunks.indexWhere(_.pos == xy)
				if(t != -1)
					__gcifccache += xy -> t
				t
			}
		}
	}

	override def isEmpty =
		_chunks.isEmpty

	override def count = {
		var c: Long = 0
		for(i <- _chunks)
			c += i.count
		c
	}

	override def cellAtCoords(x: Long, y: Long, create: Boolean): Cell = {
		var ci = getChunkIndexFromCoords(x, y)
		if(ci == -1) {
			if(create) {
				while(ci == -1) {
					generateCellAtCoords(x, y)
					ci = getChunkIndexFromCoords(x, y)
				}
				_chunks(ci).getCellFromCoords(x, y)
			} else
				airCell
		} else
			_chunks(ci).getCellFromCoords(x, y)
	}

	override def generateCellAtCoords(x: Long, y: Long) =
		while(getChunkIndexFromCoords(x, y) == -1)
			add(new Chunk(MathUtil.floor(x, Chunk.sizeX), MathUtil.floor(y, Chunk.sizeY)))

	override def generateCellAtCoords(x: Long, y: Long, c: Cell) =
		while(getChunkIndexFromCoords(x, y) == -1) {
			add(new Chunk(MathUtil.floor(x, Chunk.sizeX), MathUtil.floor(y, Chunk.sizeY)))
			_chunks(getChunkIndexFromCoords(x, y)).cellAtCoords(x, y, c)
		}

	override def cellAtCoords(x: Long, y: Long, c: Cell) = {
		var ci = getChunkIndexFromCoords(x, y)
		if(ci == -1) {
			generateCellAtCoords(x, y)
			ci = getChunkIndexFromCoords(x, y)
		}
		_chunks(ci).cellAtCoords(x, y, c)
	}

	override def isCellAt(x: Long, y: Long, cell: Cell) =
		isCellAt(x, y) && cellAtCoords(x, y, create = false) == cell

	override def isCellAt(x: Long, y: Long) =
		getChunkIndexFromCoords(x, y) != -1

	override def cellMeta(x: Long, y: Long) =
		_chunks(getChunkIndexFromCoords(x, y)).cellMeta(x, y)

	override def cellMeta(x: Long, y: Long, newMeta: Int) =
		_chunks(getChunkIndexFromCoords(x, y)).cellMeta(x, y, newMeta)

	override def cellState(x: Long, y: Long) =
		getChunkIndexFromCoords(x, y) match {
			case ci if ci != -1 =>
				_chunks(ci).cellState(x, y)
			case _ =>
				GameEngine.rand.nextBoolean()
		}

	override def cellState(x: Long, y: Long, newState: Boolean) {
		var ci = getChunkIndexFromCoords(x, y)
		while(ci == -1) {
			generateCellAtCoords(x, y)
			ci = getChunkIndexFromCoords(x, y)
		}
		_chunks(ci).cellState(x, y, newState)
	}
}
