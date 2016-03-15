package xyz.nabijaczleweli.scala_game_of_life.stuff

/** Container for common info 'bout chunks.
  *
  * @author Jędrzej
  * @since  24.03.14
  */
object Chunk extends Ordering[Chunk] {
	final val sizeX = 8
	final val sizeY = 8

	object AccessType extends Enumeration {
		type AccessType = Value
		final val Absolute, Local = Value
	}

	override def compare(x: Chunk, y: Chunk): Int =
		((x._xpos - y._xpos) + (x._ypos - y._ypos)).asInstanceOf[Int]
}

/** Used only in World as of the time being.
  *
  * @author Jędrzej
  * @since  24.03.14
  */
class Chunk(private val x: Long, private val y: Long) extends Iterable[Cell] with Traversable[Cell] {
	/** Access: _cells(ecks)(why). f.e: for x=1, y=2 _cells(1)(2) */
	protected var _cells = {
		val t = Array.ofDim[Cell](Chunk.sizeX, Chunk.sizeY)
		for(i <- t.indices; j <- t(i).indices)
			t(i)(j) = new Cell
		t
	}
	/** X position of the Chunk NOT in Cells' XY coord system. */
	protected var _xpos  = x
	/** Y position of the Chunk NOT in Cells' XY coord system. */
	protected var _ypos  = y

	/** Constructs Chunk from a given Array of Array of Cell.
	  * Everything beyond the Chunk.sizeX and Chunk.sizeY is cut.
	  *
	  * @throws ArrayIndexOutOfBoundsException if `from` is too small.
	  */
	def this(from: Array[Array[Cell]], x: Long, y: Long) = {
		this(x, y)
		_cells = Array.ofDim[Cell](Chunk.sizeX, Chunk.sizeY)
		for(i <- 0 until Chunk.sizeX)
			System.arraycopy(from(i), 0, _cells(i), 0, Chunk.sizeY)
	}

	/** Constructs Chunk from a given Array of Cell.
	  * Everything beyond the Chunk.sizeX and Chunk.sizeY is cut.
	  *
	  * @throws ArrayIndexOutOfBoundsException if `from` is too small.
	  */
	def this(from: Array[Cell], x: Long, y: Long) = {
		this(x, y)
		_cells = Array.ofDim[Cell](Chunk.sizeX, Chunk.sizeY)
		var fi = 0
		for(i <- 0 until Chunk.sizeX; j <- 0 until Chunk.sizeY) {
			_cells(i)(j) = from(fi)
			fi += 1
		}
	}

	def cell(x: Int, y: Int) =
		_cells(x)(y)

	/*def toArr: Array[Array[Cell]] =
			_cells // It doesn't return a reference, does it?*/

	override def iterator: Iterator[Cell] =
		new Iterator[Cell] {
			private var xpos = 0
			private var ypos = 0

			override def next() = {
				if(xpos == Chunk.sizeX) {
					xpos = 0
					ypos += 1
				}
				val t = xpos
				xpos += 1
				_cells(t)(ypos)
			}

			override def hasNext =
				Chunk.sizeX < (xpos + 1) && Chunk.sizeY < (ypos + 1)
		}

	/** Returns Tuple2(ecks, why) */
	def pos =
		(_xpos, _ypos)

	def cells: List[Cell] = {
		val t = new Array[Cell](Chunk.sizeX * Chunk.sizeY)
		var i = 0
		for(c <- this) {
			t(i) = c
			i += 1
		}
		t.toList
	}
}
