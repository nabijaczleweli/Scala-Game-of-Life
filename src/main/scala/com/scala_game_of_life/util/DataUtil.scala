package com.scala_game_of_life.util

import scala.reflect.ClassTag

/** @author Jędrzej
  * @since  07.05.14
  */
object DataUtil {
	final def fold[stored: ClassTag](toFold: Seq[stored], indexToFoldAt: Int): Array[Array[stored]] = {
		val resultArrArr = Array.ofDim[stored](MathUtil.floor(toFold.size, indexToFoldAt + 1), indexToFoldAt + 1)
		for(idx <- 0 until toFold.size)
			resultArrArr(MathUtil.floor(idx, indexToFoldAt + 1))(idx % (indexToFoldAt + 1)) = toFold(idx)
		resultArrArr
	}

	final def unfold[stored: ClassTag](toUnFold: Array[Array[stored]]): Seq[stored] =
		for(arri <- toUnFold; i <- arri) yield i

	final def removeFromVector[stored: ClassTag](toRemoveFrom: Vector[stored], idxToRemove: Int): Vector[stored] =
		if(idxToRemove == -1)
			toRemoveFrom
		else {
			var curidx = 0
			toRemoveFrom filterNot {_ => curidx += 1; curidx - 1 == idxToRemove}
		}
}
