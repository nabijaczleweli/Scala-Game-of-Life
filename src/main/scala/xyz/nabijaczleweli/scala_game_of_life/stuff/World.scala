package xyz.nabijaczleweli.scala_game_of_life.stuff

import java.io.{IOException, ObjectInputStream, ObjectOutputStream, ObjectStreamException}


/** Basic world, nothing too fancy.
  *
  * Has to be a bit hackish since ICellAccess uses ONLY cells, not chunks, but world stores chunks.
  *
  * @author Jędrzej
  * @since  23.03.14
  */
class World(private val from: List[Chunk]) extends ICellAccess {
	/** Not stored in xy order, just a raw array. */
	private var _chunks: Array[Chunk] =
		if(from != null) {
			val t = new Array[Chunk](from size)
			System.arraycopy(from toArray, 0, t, 0, from size)
			t
		} else
			null
	private var _width : Long         = -1
	private var _height: Long         = -1

	/** Constructs World with given size <u>in cells</u>, rounded <b>upwards</b> to size of a Chunk. */
	def this(_w: Int, _h: Int) = {
		this(null)
		if(_w == 0 || _h == 0) {
			_chunks = new Array[Chunk](0)
			_width = 0
			_height = 0
		} else {
			val w = (Math.ceil(_w.asInstanceOf[Double] / Chunk.sizeX.asInstanceOf[Double]) * Chunk.sizeX).asInstanceOf[Int]
			val h = (Math.ceil(_h.asInstanceOf[Double] / Chunk.sizeY.asInstanceOf[Double]) * Chunk.sizeY).asInstanceOf[Int]
			_width = w
			_height = h
			_chunks = new Array[Chunk](w * h)
			val t = Array.ofDim[Chunk](w, h)
			for(i <- 0 until w; j <- 0 until h)
				t(i)(j) = new Chunk(i, j)
			var u: List[Chunk] = List[Chunk]()
			for(i <- t; j <- i)
				u = u.::(j)
			System.arraycopy(u toArray, 0, _chunks, 0, w * h)
		}
	}

	override def cells: List[Cell] = {
		_chunks.length match {
			case 0 =>
				List[Cell](null)
			case _ =>
				val t = new Array[Cell](_chunks.length * Chunk.sizeX * Chunk.sizeY)
				var ind = 0
				for(i <- _chunks; j <- i) {
					t(ind) = j
					ind += 1
				}
				t.toList
		}
	}

	override def width: Int =
		_width.toString.toInt

	override def height: Int =
		_height.toString.toInt

	override def width(n: Int): Unit =
		_width = n.toString.toInt

	override def height(n: Int): Unit =
		_height = n.toString.toInt

	override def clone: World = {
		val temp = new World(_width.asInstanceOf[Int], _height.asInstanceOf[Int])
		System.arraycopy(_chunks, 0, temp._chunks, 0, _chunks.length)
		temp
	}

	def add(n: Chunk) {
		if(_chunks.length != 0) {
			val gcifc = getChunkIndexFromCoords(n.pos._1, n.pos._2)
			if(gcifc != Integer.MIN_VALUE) {
				if(_chunks.length == 1) {
					_chunks = new Array[Chunk](0)
				} else {
					val t = new Array[Chunk](_chunks.length - 1)
					var r = false
					for(i <- 0 until _chunks.length)
						if(i == gcifc)
							r = true
						else
							t(i - (if(r) 1 else 0)) = _chunks(i)
					_chunks = t
				}
			}
		}
		val newchunks = new Array[Chunk](_chunks.length + 1)
		System.arraycopy(_chunks, 0, newchunks, 0, _chunks length)
		newchunks(newchunks.length - 1) = n
		_chunks = newchunks
	}

	override def iterator: Iterator[Cell] =
		new scala.collection.Iterator[Cell] {
			var thisPos = 0

			override def hasNext: Boolean =
				thisPos < (_height * _width)

			override def next(): Cell = {
				val t = _chunks(thisPos)
				thisPos += 1
				t.cell(0, 0)
			}
		}

	def getChunkFromCoords(x: Long, y: Long): Chunk = {
		for(i <- _chunks)
			if(i.pos._1 == x && i.pos._2 == y)
				return i
		null
	}

	protected def getChunkIndexFromCoords(x: Long, y: Long): Int = {
		for(i <- 0 until _chunks.length)
			if(_chunks(i).pos._1 == x && _chunks(i).pos._2 == y)
				return i
		Integer.MIN_VALUE
	}

	override def isEmpty =
		_chunks.length == 0

	override def count = {
		var c: Long = 0
		for(i <- _chunks; j <- i)
			c += j.int
		c
	}

	override def add(from: Array[Cell], x: Long, y: Long) =
		this add new Chunk(from, x, y)

	override def toString() =
		"Overworld"

	@throws[ObjectStreamException]
	override protected def readObjectNoData(): Unit = {}

	@throws[IOException]
	@throws[ClassNotFoundException]
	override protected def readObject(in: ObjectInputStream): Unit = {
		in.defaultReadObject()
	}

	@throws[IOException]
	override protected def writeObject(out: ObjectOutputStream): Unit = {
		out.defaultWriteObject()
	}
}
