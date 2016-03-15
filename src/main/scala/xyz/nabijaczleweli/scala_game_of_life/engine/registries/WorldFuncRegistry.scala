package xyz.nabijaczleweli.scala_game_of_life.engine.registries

import scala.collection.mutable

/** Stored: Lookup function for searching random cell coordinates: <tt>Int => TraversableOnce[(Long, Long)]</tt> (parameter: amount of cells to get)<br />
  * Key:    World name:                                            <tt>String</tt>
  *
  * @author Jędrzej
  * @since  22.04.14
  */
object WorldFuncRegistry extends Registry[Int => TraversableOnce[(Long, Long)], String] {
	private val _name2func = new mutable.HashMap[key_type, stored_type]

	override private[registries] def add(key: key_type, obj: stored_type) =
		_name2func += key -> obj

	override def get(key: key_type) =
		_name2func get key
}
