package com.nabijaczleweli.cgol.stuff

import scala.collection.JavaConverters._
import java.util

/** Basic world, nothing too fancy.
  *
  * Doesn't have to be too minecraftish since we don't run the server and such.
  *
  * @author Jędrzej
  * @since  23.03.14
  */
class World(/** WOOT! Anti accessor. */ @deprecated("DO NOT USE!") private final val ___abcdefghijklmnopqrstuvwxyz: Unit) extends ICellAccess {
	/** Not stored in xy order, just a raw array. */
	private var _chunks: Array[Chunk]
	private var _width: Long
	private var _height: Long

	def this(w: Int, h: Int) {
		this(Unit)
		_width = w + (w % Chunk.sizeX)
		_height = h + (h % Chunk.sizeY)
		_chunks = new Array[Chunk]((w / Chunk.sizeX) * (h / Chunk.sizeY))
	}


	override def cells(from: List[Cell]): Unit =
		_cells = {
			val temp = new util.ArrayList[Cell](from.length)
			for(i <- 0 to (from.length - 1))
				temp.set(i, from(i))
			temp
		}.asScala.asInstanceOf[Array[Cell]]

	def ceells: Array[Cell] = _cells

	override def cells: List[Cell] = {
		val t = new java.util.ArrayList[Cell](_cells.length)
		for(i <- 0 to (_cells.length - 1))
			t.set(i, _cells(i))
		t
	}.asScala.asInstanceOf[List[Cell]]

	override def width: Int =
		_width.toString.toInt

	override def height: Int =
		_height.toString.toInt

	override def width(n: Int): Unit =
		_width = n.toString.toInt

	override def height(n: Int): Unit =
		_height = n.toString.toInt

	override def clone: World = {
		val temp = new World(_width, _height)
		System.arraycopy(_cells, 0, temp._cells, 0, _cells.length)
		temp
	}

	def unary_+(n: Cell): World = {
		val temp = clone
		temp._cells = {
			val ttemp = new Array[Cell](temp._cells.length)
			for(i <- 0 to (temp._cells.length - 1))
				ttemp(i) = temp._cells(i)
			ttemp
		}
		temp._cells(temp._cells.length - 1) = n
		temp
	}

	def +(n: Cell): World = {
		unary_+(n)
	}

	override def iterator: Iterator[Cell] = {
		new scala.collection.Iterator[Cell] {
			val l       = _height * _width
			var thisPos = 0

			override def hasNext: Boolean = true

			override def next(): Cell = {
				val t = _cells(thisPos)
				thisPos += 1
				t
			}
		}
	}

	def getChunkFromCoords: Chunk = {

	}
}