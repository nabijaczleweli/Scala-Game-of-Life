package com.nabijaczleweli.cgol.stuff

/** Implemented by worlds and such.
  *
  * So Minecraftish
  *
  * @author Jędrek
  * @since  23.03.14
  */
trait ICellAccess extends Cloneable with Iterable[Cell] {
	def height: Int

	def width: Int

	def height(n: Int): Unit

	def width(n: Int): Unit

	def cells: List[Cell]

	def cells(from: List[Cell]): Unit
}