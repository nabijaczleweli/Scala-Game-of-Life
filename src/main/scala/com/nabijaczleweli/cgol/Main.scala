package com.nabijaczleweli.cgol

import com.nabijaczleweli.cgol.stuff.{Cell, World}

/** Main launcher of CGOL.
  * @author Jędrek
  * @since  22.03.14
  */
object Main {
	def main(args: Array[String]) {
		for((s, w) <- MainResources.HelloStrings) {
			println(s)
			Thread sleep w
		}
		println()
		var w = new World()
		//w = w + new Cell
		for(i <- w.ceells)
			println(i)
	}
}

private object MainResources {
	final lazy val HelloStrings = Array[(String, Long)](("Hello.", 1000), ("What i have fo' you today is something pretty dang cool.", 200))
}